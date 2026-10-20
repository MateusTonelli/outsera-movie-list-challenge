package com.outsera.movielist.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 1000)
    private String studios;

    @Column(nullable = false, length = 2000)
    private String producers;

    @Column(nullable = false)
    private boolean winner;

    @ElementCollection
    @CollectionTable(name = "movie_producers", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "producer", nullable = false, length = 500)
    private List<String> producerNames = new ArrayList<>();

    protected Movie() {
    }

    public Movie(Integer releaseYear, String title, String studios, String producers, boolean winner) {
        this.releaseYear = releaseYear;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
        this.producerNames = new ArrayList<>(ProducerNames.parse(producers));
    }

    public Long getId() {
        return id;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public String getStudios() {
        return studios;
    }

    public String getProducers() {
        return producers;
    }

    public boolean isWinner() {
        return winner;
    }
}
