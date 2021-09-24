package com.littlepay.busfareservice.cli;

import com.littlepay.busfareservice.service.CsvFileExporter;
import com.littlepay.busfareservice.service.FileManager;
import com.littlepay.busfareservice.service.StringToBusTripStopConverter;
import com.littlepay.busfareservice.service.TripSummaryCoordinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BusFareCommandLineRunnerUnitTest {

    private BusFareCommandLineRunner testInstance;

    @Mock
    private StringToBusTripStopConverter stringToBusTripStopConverter;

    @Mock
    private FileManager fileManager;

    @Mock
    private TripSummaryCoordinationService tripSummaryCoordinationService;

    @Mock
    private CsvFileExporter csvFileExporter;

    @BeforeEach
    void setTestInstance() {
        testInstance = new BusFareCommandLineRunner(stringToBusTripStopConverter, fileManager, tripSummaryCoordinationService, csvFileExporter);
    }

    @Test
    void run_shouldExit_whenCliArgumentIsMissing() throws IOException {
        testInstance.run();

        verify(fileManager, times(0)).fileToString(anyString());
        verify(stringToBusTripStopConverter, times(0)).getBusTripStopsFromString(anyString());
        verify(tripSummaryCoordinationService, times(0)).processTrips(anyList());
        verify(csvFileExporter, times(0)).exportToFile(any(), anyList());
    }

    @Test
    void run_shouldExit_whenTooManyCliArgument() throws IOException {
        testInstance.run("a1", "a2");

        verify(fileManager, times(0)).fileToString(anyString());
        verify(stringToBusTripStopConverter, times(0)).getBusTripStopsFromString(anyString());
        verify(tripSummaryCoordinationService, times(0)).processTrips(anyList());
        verify(csvFileExporter, times(0)).exportToFile(any(), anyList());
    }

    @Test
    void run_shouldTriggerDependencies() throws IOException {
        when(fileManager.fileToString(anyString())).thenReturn("content");
        when(stringToBusTripStopConverter.getBusTripStopsFromString(anyString())).thenReturn(new ArrayList<>());
        when(tripSummaryCoordinationService.processTrips(anyList())).thenReturn(new ArrayList<>());
        doNothing().when(csvFileExporter).exportToFile(any(), anyList());

        testInstance.run("a1");

        verify(fileManager, times(1)).fileToString(anyString());
        verify(stringToBusTripStopConverter, times(1)).getBusTripStopsFromString(anyString());
        verify(tripSummaryCoordinationService, times(1)).processTrips(anyList());
        verify(csvFileExporter, times(1)).exportToFile(any(), anyList());
    }
}