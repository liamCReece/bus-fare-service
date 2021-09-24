package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.exception.InvalidTripException;
import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TapType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class TripValidationServiceUnitTest {

    private TripValidationService testInstance;

    @BeforeEach
    void setTestInstance() {
        testInstance = new TripValidationService();
    }

    @Test
    void validateStops_shouldThrowException_whenStopIdIsMissing() {
        InvalidTripException result = assertThrows(InvalidTripException.class, () ->
                testInstance.validateStops(List.of(BusTripStop.builder().pan("554").companyId("c1").busId("bus1")
                        .tapType(TapType.OFF).id("1").registeredDateTime(LocalDateTime.now()).build())));

        assertThat(result.getMessage()).isEqualTo("all stop attributes must not be null");
    }

    @Test
    void validateStops_shouldThrowException_whenStopIdIsUnknown() {
        InvalidTripException result = assertThrows(InvalidTripException.class, () ->
                testInstance.validateStops(List.of(BusTripStop.builder().stopId("Stop11").pan("554").companyId("c1").busId("bus1")
                        .tapType(TapType.OFF).id("1").registeredDateTime(LocalDateTime.now()).build())));

        assertThat(result.getMessage()).isEqualTo("stopId must be Stop1, Stop2 or Stop3");
    }


    @Test
    void validateStops_shouldNotThrowException_whenDataIsValid() {
        testInstance.validateStops(List.of(BusTripStop.builder().stopId("Stop1").pan("554").companyId("c1").busId("bus1")
                .tapType(TapType.OFF).id("1").registeredDateTime(LocalDateTime.now()).build()));

        testInstance.validateStops(List.of(BusTripStop.builder().stopId("Stop1").pan("554").companyId("c1").busId("bus1")
                .tapType(TapType.ON).id("1").registeredDateTime(LocalDateTime.now()).build()));
    }
}