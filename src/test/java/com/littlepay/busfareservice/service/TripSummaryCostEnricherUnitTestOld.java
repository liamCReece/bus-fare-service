//package com.littlepay.busfareservice.service;
//
//import com.littlepay.busfareservice.model.BusTripStop;
//import com.littlepay.busfareservice.model.TripDetails;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.littlepay.busfareservice.data.ConstantValues.STOP1_ID;
//import static com.littlepay.busfareservice.data.ConstantValues.STOP2_ID;
//import static com.littlepay.busfareservice.data.ConstantValues.STOP3_ID;
//import static org.assertj.core.api.Assertions.assertThat;
//
//class TripSummaryCostEnricherUnitTestOld {
//
//    private TripSummaryCostEnricher testInstance;
//
//    private static final LocalDateTime START_TIME = LocalDateTime.of(2021, 9, 21, 12, 30, 5);
//
//    private static final LocalDateTime END_TIME = LocalDateTime.of(2021, 9, 21, 12, 45, 0);
//
//    @BeforeEach
//    void setTestInstance() {
//        testInstance = new TripSummaryCostEnricher();
//    }
//
//    @Test
//    void deriveTripDetails_shouldProduceMaxCost_whenOneStopInTrip() {
//        BusTripStop testStop = BusTripStop.builder().stopId(STOP2_ID).registeredDateTime(START_TIME).build();
//        TripDetails result = testInstance.deriveTripDetailsOld(List.of(testStop));
//
//        //assumption for incomplete trip
//        //duration, end stop, end time will be nulls
//        assertThat(result.getChargeAmount()).isEqualTo(5.5);
//        assertThat(result.getDuration()).isNull();
//        assertThat(result.getStartTime()).isEqualTo(START_TIME);
//        assertThat(result.getEndTime()).isNull();
//        assertThat(result.getStartStop()).isEqualTo(STOP2_ID);
//        assertThat(result.getEndStop()).isNull();
//        assertThat(result.getStatus()).isEqualTo("INCOMPLETE");
//    }
//
//    @Test
//    void deriveTripDetails_shouldProduceZeroCost_whenTwoStepsAreTheSame() {
//        BusTripStop testStop = BusTripStop.builder().stopId(STOP2_ID).registeredDateTime(START_TIME).build();
//        TripDetails result = testInstance.deriveTripDetailsOld(List.of(testStop, testStop));
//
//        assertThat(result.getChargeAmount()).isEqualTo(0.0);
//        assertThat(result.getDuration()).isZero();
//        assertThat(result.getStartTime()).isEqualTo(START_TIME);
//        assertThat(result.getEndTime()).isEqualTo(START_TIME);
//        assertThat(result.getStartStop()).isEqualTo(STOP2_ID);
//        assertThat(result.getEndStop()).isEqualTo(STOP2_ID);
//        assertThat(result.getStatus()).isEqualTo("CANCELLED");
//    }
//
//    @Test
//    void deriveTripDetails_shouldProduceCorrectCost_ForTwoSteps() {
//        BusTripStop testStop = BusTripStop.builder().stopId(STOP2_ID).registeredDateTime(START_TIME).build();
//        BusTripStop testStop2 = BusTripStop.builder().stopId(STOP3_ID).registeredDateTime(END_TIME).build();
//        TripDetails result = testInstance.deriveTripDetailsOld(List.of(testStop, testStop2));
//
//        assertThat(result.getChargeAmount()).isEqualTo(5.50);
//        assertThat(result.getDuration()).isEqualTo(895);
//        assertThat(result.getStartTime()).isEqualTo(START_TIME);
//        assertThat(result.getEndTime()).isEqualTo(END_TIME);
//        assertThat(result.getStartStop()).isEqualTo(STOP2_ID);
//        assertThat(result.getEndStop()).isEqualTo(STOP3_ID);
//        assertThat(result.getStatus()).isEqualTo("COMPLETED");
//    }
//
//    @Test
//    void deriveTripDetails_shouldProduceCorrectCost_ForTwoStepsInReverseOrder() {
//        BusTripStop testStop = BusTripStop.builder().stopId(STOP3_ID).registeredDateTime(START_TIME).build();
//        BusTripStop testStop2 = BusTripStop.builder().stopId(STOP1_ID).registeredDateTime(END_TIME).build();
//        TripDetails result = testInstance.deriveTripDetailsOld(List.of(testStop, testStop2));
//
//        assertThat(result.getChargeAmount()).isEqualTo(7.30);
//        assertThat(result.getDuration()).isEqualTo(895);
//        assertThat(result.getStartTime()).isEqualTo(START_TIME);
//        assertThat(result.getEndTime()).isEqualTo(END_TIME);
//        assertThat(result.getStartStop()).isEqualTo(STOP3_ID);
//        assertThat(result.getEndStop()).isEqualTo(STOP1_ID);
//        assertThat(result.getStatus()).isEqualTo("COMPLETED");
//    }
//}