package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.TripStatus;
import com.littlepay.busfareservice.model.TripSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.littlepay.busfareservice.data.ConstantValues.STOP1_ID;
import static com.littlepay.busfareservice.data.ConstantValues.STOP1_STOP2_COST;
import static com.littlepay.busfareservice.data.ConstantValues.STOP1_STOP3_COST;
import static com.littlepay.busfareservice.data.ConstantValues.STOP2_ID;
import static com.littlepay.busfareservice.data.ConstantValues.STOP2_STOP3_COST;
import static com.littlepay.busfareservice.data.ConstantValues.STOP3_ID;

@Service
@Slf4j
public class TripSummaryCostEnricher {

    public TripSummary enrichTripSummaryWithCost(TripSummary trip) {
        if (TripStatus.COMPLETED.equals(trip.getStatus())) {
            double cost = calculateFareForCompletedTrip(trip.getFromStopId(), trip.getToStopId());
            log.debug("completed trip between {} and {}, cost is {}", trip.getFromStopId(), trip.getToStopId(), cost);
            return trip.toBuilder()
                    .chargeAmount(cost)
                    .build();
        } else if (TripStatus.INCOMPLETE.equals(trip.getStatus())) {
            double cost = calculateFareForIncompleteTrip(trip.getFromStopId());
            log.debug("incomplete trip from {}, cost is {}", trip.getFromStopId(), cost);
            return trip.toBuilder()
                    .chargeAmount(cost)
                    .build();
        }
        log.debug("cancelled trip {}, free of charge", trip.getFromStopId());
        return trip.toBuilder().chargeAmount(0.0).build();
    }

    private double calculateFareForIncompleteTrip(String stopId) {
        double cost;
        if (STOP1_ID.equalsIgnoreCase(stopId)
                || STOP3_ID.equalsIgnoreCase(stopId)) {
            cost = STOP1_STOP3_COST;
        } else {
            cost = STOP2_STOP3_COST;
        }
        return cost;
    }


    private double calculateFareForCompletedTrip(String origin, String destination) {
        double cost;
        if ((STOP1_ID.equalsIgnoreCase(origin) && STOP2_ID.equalsIgnoreCase(destination))
                || (STOP2_ID.equalsIgnoreCase(origin) && STOP1_ID.equalsIgnoreCase(destination))) {
            log.debug("user travel between stop 1 and 2");
            cost = STOP1_STOP2_COST;
        } else if ((STOP2_ID.equalsIgnoreCase(origin) && STOP3_ID.equalsIgnoreCase(destination))
                || (STOP3_ID.equalsIgnoreCase(origin) && STOP2_ID.equalsIgnoreCase(destination))) {
            log.debug("user travel between stop 2 and 3");
            cost = STOP2_STOP3_COST;
        } else {
            log.debug("user travelled between stop 1 and 3");
            cost = STOP1_STOP3_COST;
        }

        return cost;
    }

}
