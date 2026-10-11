package com.outsera.movielist.award;

import java.util.List;

public record AwardIntervalResponse(
        List<ProducerInterval> min,
        List<ProducerInterval> max
) {
}
