package com.outsera.movielist.award;

public record ProducerInterval(
        String producer,
        int interval,
        int previousWin,
        int followingWin
) {
}
