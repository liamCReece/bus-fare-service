package com.littlepay.busfareservice.utils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.littlepay.busfareservice.model.BusTripStop;
import com.littlepay.busfareservice.model.TapType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * used to generate test files
 */
@Disabled
public class TestFileGenerator {
    private static final LocalDateTime START = LocalDateTime.of(2019, 12, 2, 1, 1, 1);
    private static final String PAN1 = "122000000000003";
    private static final String PAN2 = "34343434343434";
    private static final String PAN3 = "5555555555554444";
    private static final String PAN4 = "5019717010103742";
    private static final String PAN5 = "6011000400000000";


    @Test
    void generateTestFile() throws IOException {
        List<BusTripStop> testList = new ArrayList<>();
        LocalDateTime start = START;
        int id = 1;
        for (int i = 0; i < 1; i++) {
            testList.addAll(generateTripsInOrder(start, id));
            start = testList.get(testList.size() - 1).getRegisteredDateTime().plusSeconds(30L);
            id = Integer.parseInt(testList.get(testList.size() - 1).getId()) + 1;
            testList.addAll(generateTripsNotInOrder(start, id));
            start = testList.get(testList.size() - 1).getRegisteredDateTime().plusSeconds(45L);
            id = Integer.parseInt(testList.get(testList.size() - 1).getId()) + 1;
        }

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        CsvSchema csvSchema = csvMapper.schemaFor(BusTripStop.class);
        csvMapper.writerFor(BusTripStop[].class)
                .with(csvSchema.withHeader())
                .writeValue(new File("4_MixedTypeTrips.csv"), testList.toArray(BusTripStop[]::new));

    }


    private List<BusTripStop> generateTripsInOrder(LocalDateTime startTime, int startId) {
        return List.of(
                BusTripStop.builder().stopId("Stop1").busId("Bus17").companyId("c1").registeredDateTime(startTime).id(String.valueOf(startId)).tapType(TapType.ON).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop2").busId("Bus17").companyId("c1").registeredDateTime(startTime.plusMinutes(10)).id(String.valueOf(startId + 1)).tapType(TapType.OFF).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus18").companyId("c2").registeredDateTime(startTime.plusMinutes(15)).id(String.valueOf(startId + 2)).tapType(TapType.ON).pan(PAN2).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus18").companyId("c2").registeredDateTime(startTime.plusMinutes(16)).id(String.valueOf(startId + 3)).tapType(TapType.OFF).pan(PAN2).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus19").companyId("c3").registeredDateTime(startTime.plusMinutes(18)).id(String.valueOf(startId + 4)).tapType(TapType.ON).pan(PAN3).build()
        );
    }

    private List<BusTripStop> generateTripsNotInOrder(LocalDateTime startTime, int startId) {
        return List.of(
                BusTripStop.builder().stopId("Stop1").busId("Bus17").companyId("c1").registeredDateTime(startTime).id(String.valueOf(startId)).tapType(TapType.ON).pan(PAN1).build(),
                BusTripStop.builder().stopId("Stop2").busId("Bus18").companyId("c2").registeredDateTime(startTime.plusMinutes(10)).id(String.valueOf(startId + 1)).tapType(TapType.ON).pan(PAN4).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus20").companyId("c3").registeredDateTime(startTime.plusMinutes(15)).id(String.valueOf(startId + 2)).tapType(TapType.ON).pan(PAN5).build(),
                BusTripStop.builder().stopId("Stop1").busId("Bus18").companyId("c2").registeredDateTime(startTime.plusMinutes(16)).id(String.valueOf(startId + 3)).tapType(TapType.OFF).pan(PAN4).build(),
                BusTripStop.builder().stopId("Stop3").busId("Bus17").companyId("c1").registeredDateTime(startTime.plusMinutes(18)).id(String.valueOf(startId + 4)).tapType(TapType.OFF).pan(PAN1).build()
        );
    }
}
