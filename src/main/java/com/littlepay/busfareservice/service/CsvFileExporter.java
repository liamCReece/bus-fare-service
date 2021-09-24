package com.littlepay.busfareservice.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.littlepay.busfareservice.model.TripSummary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class CsvFileExporter {

    public void exportToFile(File file, List<TripSummary> tripSummaryList) throws IOException {

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema csvSchema = csvMapper.schemaFor(TripSummary.class);
        csvMapper.writerFor(TripSummary[].class)
                .with(csvSchema.withHeader())
                .writeValue(file, tripSummaryList.toArray(TripSummary[]::new));
    }
}
