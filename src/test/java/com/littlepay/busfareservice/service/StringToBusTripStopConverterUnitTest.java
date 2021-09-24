package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TapType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StringToBusTripStopConverterUnitTest {

    private StringToBusTripStopConverter testInstance;

    @BeforeEach
    void setTestInstance() {
        testInstance = new StringToBusTripStopConverter();
    }

    @Test
    void getBusTripStopsFromString_shouldParseContent() throws IOException {
        String testContent =
                "ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN" + System.lineSeparator() +
                        "1, 22-01-2018 13:00:00, ON, Stop1, Company1, Bus37, 5500005555555559" + System.lineSeparator() +
                        "2, 22-01-2018 13:05:00, OFF, Stop2, Company1, Bus37, 5500005555555559";

        List<BusTripStop> result = testInstance.getBusTripStopsFromString(testContent);

        assertThat(result).isNotNull().hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(0).getBusId()).isEqualTo("Bus37");
        assertThat(result.get(0).getTapType()).isEqualTo(TapType.ON);
        assertThat(result.get(0).getStopId()).isEqualTo("Stop1");
        assertThat(result.get(0).getCompanyId()).isEqualTo("Company1");
        assertThat(result.get(0).getPan()).isEqualTo("5500005555555559");
        assertThat(result.get(0).getRegisteredDateTime()).isEqualTo(LocalDateTime.of(2018, 1, 22, 13, 0, 0));

        assertThat(result.get(1).getId()).isEqualTo("2");
        assertThat(result.get(1).getBusId()).isEqualTo("Bus37");
        assertThat(result.get(1).getTapType()).isEqualTo(TapType.OFF);
        assertThat(result.get(1).getStopId()).isEqualTo("Stop2");
        assertThat(result.get(1).getCompanyId()).isEqualTo("Company1");
        assertThat(result.get(1).getPan()).isEqualTo("5500005555555559");
        assertThat(result.get(1).getRegisteredDateTime()).isEqualTo(LocalDateTime.of(2018, 1, 22, 13, 5, 0));
    }

}