package com.outsera.movielist.award;

import com.outsera.movielist.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AwardIntervalService {

    private final MovieRepository movieRepository;

    public AwardIntervalService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public AwardIntervalResponse findMinAndMaxIntervals() {
        return new AwardIntervalResponse(
                movieRepository.findShortestIntervals(),
                movieRepository.findLongestIntervals()
        );
    }
}
