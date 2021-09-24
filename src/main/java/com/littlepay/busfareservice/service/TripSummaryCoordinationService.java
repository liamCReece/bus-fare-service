package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TripSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TripSummaryCoordinationService {

    private final TripValidationService tripValidationService;
    private final TripSummaryCostEnricher tripSummaryCostEnricher;
    private final TripSummaryGenerator tripSummaryGenerator;

    @Autowired
    public TripSummaryCoordinationService(TripValidationService tripValidationService,
                                          TripSummaryCostEnricher tripSummaryCostEnricher,
                                          TripSummaryGenerator tripSummaryGenerator) {
        this.tripValidationService = tripValidationService;
        this.tripSummaryCostEnricher = tripSummaryCostEnricher;
        this.tripSummaryGenerator = tripSummaryGenerator;
    }

    public List<TripSummary> processTrips(List<BusTripStop> stops) {
        List<TripSummary> initialTrips = new ArrayList<>();
        tripValidationService.validateStops(stops);

        //partition stops by user/PAN
        Map<String, List<BusTripStop>> accountStopsMap = stops.stream().collect(Collectors.groupingBy(BusTripStop::getPan));

        accountStopsMap.forEach((pan, stopList) -> {
            log.debug("processing for pan {}", pan);
            initialTrips.addAll(tripSummaryGenerator.generateTripSummaries(stopList));
        });

        return initialTrips.stream().map(tripSummaryCostEnricher::enrichTripSummaryWithCost)
                .sorted(Comparator.comparing(TripSummary::getStarted))
                .collect(Collectors.toList());
    }

}
