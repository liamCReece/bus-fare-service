package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TapType;
import com.littlepay.busfareservice.model.TripStatus;
import com.littlepay.busfareservice.model.TripSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class TripSummaryCoordinationServiceIntegrationTest {
    private static final LocalDateTime START = LocalDateTime.of(2019, 12, 2, 1, 1, 1);
    private static final String PAN1 = "122000000000003";
    private static final String PAN2 = "34343434343434";
    private static final String PAN3 = "5555555555554444";


    @Autowired
    private TripSummaryCoordinationService testInstance;


    /**
     * this test is to simulate in time sequence
     * user1 tap on bus17 at stop1 -> trip1 started
     * user2 tap on bus18 at stop2 -> trip2 started
     * user3 tap on bus20 at stop2, gets off stop3 without touching off -> trip3 started and ended (incomplete)
     * user2 tap off on stop1 -> trip2 ended (complete)
     * user1 tap off at stop3 -> trip1 ended (complete)
     * user3 tap on and off at stop1 -> trip4 started and ended (cancelled)
     * user2 taps on bus18 at stop3 , gets off at stop2 without touch off -> trip5 started and ended (incomplete)
     */
    @Test
    void processTrips_shouldGenerateTripDetailsForDifferentUsers() {
        List<TripSummary> results = testInstance.processTrips(generateTripsNotInOrder());
        assertThat(results).isNotNull().hasSize(5);

        TripSummary trip1 = results.get(0);
        assertThat(trip1.getPan()).isEqualTo(PAN1);
        assertThat(trip1.getStatus()).isEqualTo(TripStatus.COMPLETED);
        assertThat(trip1.getChargeAmount()).isEqualTo(7.3);

        TripSummary trip2 = results.get(1);
        assertThat(trip2.getPan()).isEqualTo(PAN2);
        assertThat(trip2.getStatus()).isEqualTo(TripStatus.COMPLETED);
        assertThat(trip2.getChargeAmount()).isEqualTo(3.25);

        TripSummary trip3 = results.get(2);
        assertThat(trip3.getPan()).isEqualTo(PAN3);
        assertThat(trip3.getStatus()).isEqualTo(TripStatus.INCOMPLETE);
        assertThat(trip3.getChargeAmount()).isEqualTo(5.5);

        TripSummary trip4 = results.get(3);
        assertThat(trip4.getPan()).isEqualTo(PAN3);
        assertThat(trip4.getStatus()).isEqualTo(TripStatus.CANCELLED);
        assertThat(trip4.getChargeAmount()).isEqualTo(0.0);

        TripSummary trip5 = results.get(4);
        assertThat(trip5.getPan()).isEqualTo(PAN2);
        assertThat(trip5.getStatus()).isEqualTo(TripStatus.INCOMPLETE);
        assertThat(trip5.getChargeAmount()).isEqualTo(7.3);
    }

    private List<BusTripStop> generateTripsNotInOrder() {
        return List.of(
                BusTripStop.builder().stopId("Stop1").busId("Bus17").companyId("c1").registeredDateTime(START).id(String.valueOf(1)).tapType(TapType.ON).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop2").busId("Bus18").companyId("c2").registeredDateTime(START.plusMinutes(10)).id(String.valueOf(1 + 1)).tapType(TapType.ON).pan(PAN2).build(),
                BusTripStop.builder().stopId("Stop2").busId("Bus20").companyId("c3").registeredDateTime(START.plusMinutes(15)).id(String.valueOf(1 + 2)).tapType(TapType.ON).pan(PAN3).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus18").companyId("c2").registeredDateTime(START.plusMinutes(16)).id(String.valueOf(1 + 3)).tapType(TapType.OFF).pan(PAN2).build(),
                BusTripStop.builder().stopId("Stop3").busId("Bus17").companyId("c1").registeredDateTime(START.plusMinutes(18)).id(String.valueOf(1 + 4)).tapType(TapType.OFF).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus20").companyId("c3").registeredDateTime(START.plusMinutes(20)).id(String.valueOf(1 + 5)).tapType(TapType.ON).pan(PAN3).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus20").companyId("c3").registeredDateTime(START.plusMinutes(21)).id(String.valueOf(1 + 6)).tapType(TapType.OFF).pan(PAN3).build(),
                BusTripStop.builder().stopId("Stop3").busId("Bus19").companyId("c2").registeredDateTime(START.plusMinutes(21)).id(String.valueOf(1 + 7)).tapType(TapType.ON).pan(PAN2).build()
        );
    }
}