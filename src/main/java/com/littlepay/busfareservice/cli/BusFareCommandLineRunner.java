package com.littlepay.busfareservice.cli;

import com.littlepay.busfareservice.exception.InvalidTripException;
import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TripSummary;
import com.littlepay.busfareservice.service.CsvFileExporter;
import com.littlepay.busfareservice.service.FileManager;
import com.littlepay.busfareservice.service.StringToBusTripStopConverter;
import com.littlepay.busfareservice.service.TripSummaryCoordinationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class BusFareCommandLineRunner implements CommandLineRunner {

    private static final String OUTPUT_FILE = "trips.csv";

    private final StringToBusTripStopConverter stringToBusTripStopConverter;
    private final FileManager fileManager;
    private final TripSummaryCoordinationService tripSummaryCoordinationService;
    private final CsvFileExporter csvFileExporter;

    @Autowired
    public BusFareCommandLineRunner(StringToBusTripStopConverter stringToBusTripStopConverter,
                                    FileManager fileManager,
                                    TripSummaryCoordinationService tripSummaryCoordinationService,
                                    CsvFileExporter csvFileExporter) {
        this.stringToBusTripStopConverter = stringToBusTripStopConverter;
        this.fileManager = fileManager;
        this.tripSummaryCoordinationService = tripSummaryCoordinationService;
        this.csvFileExporter = csvFileExporter;
    }

    @Override
    public void run(String... args) {
        if (args.length != 1
                || StringUtils.isEmpty(args[0])) {
            log.error("Usage: <bus_trip.csv>");
            return;
        }
        String fileName = args[0];
        try {
            log.info("open file {}", fileName);
            String content = fileManager.fileToString(fileName);

            List<BusTripStop> stops = stringToBusTripStopConverter.getBusTripStopsFromString(content);

            List<TripSummary> tripSummaries = tripSummaryCoordinationService.processTrips(stops);

            csvFileExporter.exportToFile(new File(OUTPUT_FILE), tripSummaries);
            log.info("file saved to {}", OUTPUT_FILE);

        } catch (IOException exception) {
            log.error("error open/marshal/save file {}", exception.getMessage());
        } catch (InvalidTripException invalidTripException) {
            log.error("invalid trip detected : {}", invalidTripException.getMessage());
        }

    }

}
