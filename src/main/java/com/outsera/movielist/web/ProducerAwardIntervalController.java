package com.outsera.movielist.web;

import com.outsera.movielist.award.AwardIntervalResponse;
import com.outsera.movielist.award.AwardIntervalService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/producers/award-intervals", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProducerAwardIntervalController {

    private final AwardIntervalService awardIntervalService;

    public ProducerAwardIntervalController(AwardIntervalService awardIntervalService) {
        this.awardIntervalService = awardIntervalService;
    }

    @GetMapping
    public AwardIntervalResponse getAwardIntervals() {
        return awardIntervalService.findMinAndMaxIntervals();
    }
}
