package com.outsera.movielist.award;

import com.outsera.movielist.domain.Movie;
import com.outsera.movielist.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AwardIntervalService {

    private static final Comparator<ProducerInterval> INTERVAL_ORDER = Comparator
            .comparing(ProducerInterval::producer)
            .thenComparingInt(ProducerInterval::previousWin);

    private final MovieRepository movieRepository;

    public AwardIntervalService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public AwardIntervalResponse findMinAndMaxIntervals() {
        Map<String, List<Integer>> winsByProducer = new TreeMap<>();
        for (Movie movie : movieRepository.findByWinnerTrue()) {
            for (String producer : splitProducers(movie.getProducers())) {
                winsByProducer.computeIfAbsent(producer, key -> new ArrayList<>()).add(movie.getReleaseYear());
            }
        }

        List<ProducerInterval> intervals = new ArrayList<>();
        for (Map.Entry<String, List<Integer>> entry : winsByProducer.entrySet()) {
            List<Integer> years = entry.getValue();
            years.sort(Comparator.naturalOrder());
            for (int index = 0; index < years.size() - 1; index++) {
                int previousWin = years.get(index);
                int followingWin = years.get(index + 1);
                intervals.add(new ProducerInterval(
                        entry.getKey(),
                        followingWin - previousWin,
                        previousWin,
                        followingWin
                ));
            }
        }

        if (intervals.isEmpty()) {
            return new AwardIntervalResponse(List.of(), List.of());
        }

        int minimum = intervals.stream().mapToInt(ProducerInterval::interval).min().orElseThrow();
        int maximum = intervals.stream().mapToInt(ProducerInterval::interval).max().orElseThrow();
        return new AwardIntervalResponse(
                filterAndSort(intervals, minimum),
                filterAndSort(intervals, maximum)
        );
    }

    private static List<ProducerInterval> filterAndSort(List<ProducerInterval> intervals, int target) {
        return intervals.stream()
                .filter(interval -> interval.interval() == target)
                .sorted(INTERVAL_ORDER)
                .toList();
    }

    static List<String> splitProducers(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        String normalized = raw.replaceAll("(?i),\\s+and\\s+", ", ");
        normalized = normalized.replaceAll("(?i)\\s+and\\s+", ", ");
        return Arrays.stream(normalized.split(","))
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .toList();
    }
}
