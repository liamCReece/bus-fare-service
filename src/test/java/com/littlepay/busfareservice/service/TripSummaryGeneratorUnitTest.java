package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TapType;
import com.littlepay.busfareservice.model.TripStatus;
import com.littlepay.busfareservice.model.TripSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TripSummaryGeneratorUnitTest {
    private static final LocalDateTime START = LocalDateTime.of(2019, 12, 2, 1, 1, 1);
    private static final String PAN1 = "122000000000003";

    private TripSummaryGenerator testInstance;

    @BeforeEach
    void setTestInstance() {
        testInstance = new TripSummaryGenerator();
    }

    @Test
    void generateTripSummaries_shouldProcessCompleteTrip() {
        List<TripSummary> tripSummaries = testInstance.generateTripSummaries(singleCompletedTrip());
        assertThat(tripSummaries).isNotNull().hasSize(1);
        TripSummary trip = tripSummaries.get(0);

        assertThat(trip.getBusId()).isEqualTo("Bus17");
        assertThat(trip.getCompanyId()).isEqualTo("c1");
        assertThat(trip.getFromStopId()).isEqualTo("Stop1");
        assertThat(trip.getToStopId()).isEqualTo("Stop2");
        assertThat(trip.getPan()).isEqualTo(PAN1);
        assertThat(trip.getDurationSecs()).isEqualTo(600L);
        assertThat(trip.getStarted()).isEqualTo(START);
        assertThat(trip.getFinished()).isEqualTo(START.plusMinutes(10));
        assertThat(trip.getStatus()).isEqualTo(TripStatus.COMPLETED);
    }


    /**
     * this test is to simulate
     * user touch on at stop1 then 10 mins later touch off at stop 2 -> trip 1
     * user touch on at stop2 then get off at stop1 without touching off -> trip 2
     * user touch on stop1 and then changed his/her mind by touching off and get off the bus -> trip 3
     */
    @Test
    void generateTripSummaries_shouldProcessMultipleTripsOfDifferentType() {
        List<BusTripStop> allStops = new ArrayList<>();
        allStops.addAll(singleCompletedTrip());
        allStops.addAll(singleIncompleteTrip());
        allStops.addAll(singleCancelledTrip());

        List<TripSummary> result = testInstance.generateTripSummaries(allStops);
        assertThat(result).isNotNull().hasSize(3);

        TripSummary trip1 = result.get(0);
        assertThat(trip1.getBusId()).isEqualTo("Bus17");
        assertThat(trip1.getCompanyId()).isEqualTo("c1");
        assertThat(trip1.getFromStopId()).isEqualTo("Stop1");
        assertThat(trip1.getToStopId()).isEqualTo("Stop2");
        assertThat(trip1.getPan()).isEqualTo(PAN1);
        assertThat(trip1.getDurationSecs()).isEqualTo(600L);
        assertThat(trip1.getStarted()).isEqualTo(START);
        assertThat(trip1.getFinished()).isEqualTo(START.plusMinutes(10));
        assertThat(trip1.getStatus()).isEqualTo(TripStatus.COMPLETED);

        TripSummary trip2 = result.get(1);
        assertThat(trip2.getBusId()).isEqualTo("Bus18");
        assertThat(trip2.getCompanyId()).isEqualTo("c1");
        assertThat(trip2.getFromStopId()).isEqualTo("Stop2");
        assertThat(trip2.getToStopId()).isNull();
        assertThat(trip2.getPan()).isEqualTo(PAN1);
        assertThat(trip2.getDurationSecs()).isNull();
        assertThat(trip2.getStarted()).isEqualTo(START.plusMinutes(15));
        assertThat(trip2.getFinished()).isNull();
        assertThat(trip2.getStatus()).isEqualTo(TripStatus.INCOMPLETE);


        TripSummary trip3 = result.get(2);
        assertThat(trip3.getBusId()).isEqualTo("Bus19");
        assertThat(trip3.getCompanyId()).isEqualTo("c1");
        assertThat(trip3.getFromStopId()).isEqualTo("Stop1");
        assertThat(trip3.getToStopId()).isEqualTo("Stop1");
        assertThat(trip3.getPan()).isEqualTo(PAN1);
        assertThat(trip3.getDurationSecs()).isEqualTo(60L);
        assertThat(trip3.getStarted()).isEqualTo(START.plusMinutes(20));
        assertThat(trip3.getFinished()).isEqualTo(START.plusMinutes(21));
        assertThat(trip3.getStatus()).isEqualTo(TripStatus.CANCELLED);
    }

    public static List<BusTripStop> singleCompletedTrip() {
        return List.of(
                BusTripStop.builder().stopId("Stop1").busId("Bus17").companyId("c1").registeredDateTime(START).id("1").tapType(TapType.ON).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop2").busId("Bus17").companyId("c1").registeredDateTime(START.plusMinutes(10)).id("2").tapType(TapType.OFF).pan(PAN1).build()
        );
    }

    public static List<BusTripStop> singleIncompleteTrip() {
        return List.of(
                BusTripStop.builder().stopId("Stop2").busId("Bus18").companyId("c1").registeredDateTime(START.plusMinutes(15)).id("3").tapType(TapType.ON).pan(PAN1).build()
        );
    }

    public static List<BusTripStop> singleCancelledTrip() {
        return List.of(
                BusTripStop.builder().stopId("Stop1").busId("Bus19").companyId("c1").registeredDateTime(START.plusMinutes(20)).id("4").tapType(TapType.ON).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus19").companyId("c1").registeredDateTime(START.plusMinutes(21)).id("5").tapType(TapType.OFF).pan(PAN1).build()
        );
    }
}