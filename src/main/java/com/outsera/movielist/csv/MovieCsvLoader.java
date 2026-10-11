package com.outsera.movielist.csv;

import com.outsera.movielist.domain.Movie;
import com.outsera.movielist.repository.MovieRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class MovieCsvLoader implements ApplicationRunner {

    private final MovieRepository movieRepository;
    private final Resource csv;

    public MovieCsvLoader(MovieRepository movieRepository, @Value("${app.csv-location}") Resource csv) {
        this.movieRepository = movieRepository;
        this.csv = csv;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        if (!csv.exists()) {
            throw new IllegalStateException("CSV not found: " + csv);
        }
        movieRepository.saveAll(readMovies());
    }

    private List<Movie> readMovies() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .get();
        List<Movie> movies = new ArrayList<>();
        try (Reader reader = new InputStreamReader(csv.getInputStream(), StandardCharsets.UTF_8)) {
            for (CSVRecord record : format.parse(reader)) {
                movies.add(new Movie(
                        Integer.valueOf(record.get("year")),
                        record.get("title"),
                        record.get("studios"),
                        record.get("producers"),
                        "yes".equalsIgnoreCase(record.get("winner"))
                ));
            }
        }
        return movies;
    }
}
