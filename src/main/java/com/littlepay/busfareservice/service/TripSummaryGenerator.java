package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TapType;
import com.littlepay.busfareservice.model.TripStatus;
import com.littlepay.busfareservice.model.TripSummary;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class TripSummaryGenerator {

    /**
     * @param stops from single user/PAN
     * @return trip summaries
     */
    public List<TripSummary> generateTripSummaries(List<BusTripStop> stops) {
        List<TripSummary> results = new ArrayList<>();

        //sort the stops by time
        List<BusTripStop> stopsSorted = stops.stream().sorted(Comparator.comparing(BusTripStop::getRegisteredDateTime))
                .collect(Collectors.toList());

        AtomicInteger counter = new AtomicInteger(0);
        //convert list of stop into trips
        stopsSorted.forEach(stop -> {
            int currentIndex = counter.get();
            if (TapType.ON.equals(stop.getTapType())) {
                if (currentIndex + 1 < stopsSorted.size()) {
                    BusTripStop nextStop = stopsSorted.get(currentIndex + 1);
                    if (TapType.OFF.equals(nextStop.getTapType())) {
                        results.add(createTripWithTwoStops(stop, nextStop));
                    } else {
                        results.add(createTripWithSingleStop(stop));
                    }
                } else {
                    results.add(createTripWithSingleStop(stop));
                }
            }
            counter.incrementAndGet();
        });

        return results;
    }

    private TripSummary createTripWithSingleStop(BusTripStop stop) {
        return TripSummary.builder()
                .started(stop.getRegisteredDateTime())
                .fromStopId(stop.getStopId())
                .companyId(stop.getCompanyId())
                .busId(stop.getBusId())
                .pan(stop.getPan())
                .status(TripStatus.INCOMPLETE)
                .build();
    }

    private TripSummary createTripWithTwoStops(BusTripStop origin, BusTripStop destination) {
        TripStatus status = TripStatus.COMPLETED;
        if (StringUtils.equalsIgnoreCase(origin.getStopId(), destination.getStopId())) {
            status = TripStatus.CANCELLED;
        }

        return TripSummary.builder()
                .started(origin.getRegisteredDateTime())
                .finished(destination.getRegisteredDateTime())
                .durationSecs(calculateTripDuration(origin.getRegisteredDateTime(), destination.getRegisteredDateTime()))
                .fromStopId(origin.getStopId())
                .toStopId(destination.getStopId())
                .companyId(origin.getCompanyId())
                .busId(origin.getBusId())
                .pan(origin.getPan())
                .status(status)
                .build();
    }

    private long calculateTripDuration(LocalDateTime start, LocalDateTime finish) {
        return ChronoUnit.SECONDS.between(start, finish);
    }
}
