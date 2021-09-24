package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.TripStatus;
import com.littlepay.busfareservice.model.TripSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.littlepay.busfareservice.data.ConstantValues.STOP1_ID;
import static com.littlepay.busfareservice.data.ConstantValues.STOP2_ID;
import static com.littlepay.busfareservice.data.ConstantValues.STOP3_ID;
import static org.assertj.core.api.Assertions.assertThat;

class TripSummaryCostEnricherUnitTest {
    private TripSummaryCostEnricher testInstance;

    @BeforeEach
    void setTestInstance() {
        testInstance = new TripSummaryCostEnricher();
    }

    @Test
    void enrichTripSummaryWithCost_cancelledTripAtStop1() {
        TripSummary cancelledStop1 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.CANCELLED)
                .fromStopId(STOP1_ID)
                .toStopId(STOP1_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(cancelledStop1);
        assertThat(result.getChargeAmount()).isEqualTo(0.0);
    }

    @Test
    void enrichTripSummaryWithCost_cancelledTripAtStop2() {
        TripSummary cancelledStop2 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.CANCELLED)
                .fromStopId(STOP2_ID)
                .toStopId(STOP2_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(cancelledStop2);
        assertThat(result.getChargeAmount()).isEqualTo(0.0);
    }

    @Test
    void enrichTripSummaryWithCost_cancelledTripAtStop3() {
        TripSummary cancelledStop3 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.CANCELLED)
                .fromStopId(STOP3_ID)
                .toStopId(STOP3_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(cancelledStop3);
        assertThat(result.getChargeAmount()).isEqualTo(0.0);
    }

    @Test
    void enrichTripSummaryWithCost_incompleteTripAtStop1() {
        TripSummary incompleteStop1 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.INCOMPLETE)
                .fromStopId(STOP1_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(incompleteStop1);
        assertThat(result.getChargeAmount()).isEqualTo(7.3);
    }

    @Test
    void enrichTripSummaryWithCost_incompleteTripAtStop2() {
        TripSummary incompleteStop2 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.INCOMPLETE)
                .fromStopId(STOP2_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(incompleteStop2);
        assertThat(result.getChargeAmount()).isEqualTo(5.5);
    }

    @Test
    void enrichTripSummaryWithCost_incompleteTripAtStop3() {
        TripSummary incompleteStop3 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.INCOMPLETE)
                .fromStopId(STOP3_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(incompleteStop3);
        assertThat(result.getChargeAmount()).isEqualTo(7.3);
    }

    @Test
    void enrichTripSummaryWithCost_completedTripBetweenStop1_2() {
        TripSummary complete12 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.COMPLETED)
                .fromStopId(STOP1_ID)
                .toStopId(STOP2_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(complete12);
        assertThat(result.getChargeAmount()).isEqualTo(3.25);
    }

    @Test
    void enrichTripSummaryWithCost_completedTripBetweenStop2_1() {
        TripSummary complete21 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.COMPLETED)
                .fromStopId(STOP2_ID)
                .toStopId(STOP1_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(complete21);
        assertThat(result.getChargeAmount()).isEqualTo(3.25);
    }

    @Test
    void enrichTripSummaryWithCost_completedTripBetweenStop1_3() {
        TripSummary complete13 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.COMPLETED)
                .fromStopId(STOP1_ID)
                .toStopId(STOP3_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(complete13);
        assertThat(result.getChargeAmount()).isEqualTo(7.3);
    }

    @Test
    void enrichTripSummaryWithCost_completedTripBetweenStop3_1() {
        TripSummary complete31 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.COMPLETED)
                .fromStopId(STOP3_ID)
                .toStopId(STOP1_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(complete31);
        assertThat(result.getChargeAmount()).isEqualTo(7.3);
    }

    @Test
    void enrichTripSummaryWithCost_completedTripBetweenStop2_3() {
        TripSummary complete23 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.COMPLETED)
                .fromStopId(STOP2_ID)
                .toStopId(STOP3_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(complete23);
        assertThat(result.getChargeAmount()).isEqualTo(5.5);
    }

    @Test
    void enrichTripSummaryWithCost_completedTripBetweenStop3_2() {
        TripSummary complete32 = TripSummary.builder()
                .pan("001")
                .busId("bus1")
                .companyId("c1")
                .status(TripStatus.COMPLETED)
                .fromStopId(STOP3_ID)
                .toStopId(STOP2_ID)
                .build();

        TripSummary result = testInstance.enrichTripSummaryWithCost(complete32);
        assertThat(result.getChargeAmount()).isEqualTo(5.5);
    }
}