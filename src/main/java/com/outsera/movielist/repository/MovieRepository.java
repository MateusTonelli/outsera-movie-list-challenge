package com.outsera.movielist.repository;

import com.outsera.movielist.award.ProducerInterval;
import com.outsera.movielist.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    String CONSECUTIVE_WIN_INTERVALS = """
            WITH winning_years AS (
                SELECT mp.producer, m.release_year
                FROM movie_producers mp
                JOIN movies m ON m.id = mp.movie_id
                WHERE m.winner = 1
            ),
            intervals AS (
                SELECT producer,
                       release_year - LAG(release_year) OVER by_producer AS interval,
                       LAG(release_year) OVER by_producer AS previous_win,
                       release_year AS following_win
                FROM winning_years
                WINDOW by_producer AS (PARTITION BY producer ORDER BY release_year)
            )
            SELECT producer, interval, previous_win, following_win
            FROM intervals
            """;

    @Query(nativeQuery = true, value = CONSECUTIVE_WIN_INTERVALS + """
            WHERE interval = (SELECT MIN(interval) FROM intervals)
            ORDER BY producer, previous_win
            """)
    List<ProducerInterval> findShortestIntervals();

    @Query(nativeQuery = true, value = CONSECUTIVE_WIN_INTERVALS + """
            WHERE interval = (SELECT MAX(interval) FROM intervals)
            ORDER BY producer, previous_win
            """)
    List<ProducerInterval> findLongestIntervals();
}
