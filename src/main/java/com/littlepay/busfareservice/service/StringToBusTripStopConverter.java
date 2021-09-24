package com.littlepay.busfareservice.service;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.littlepay.busfareservice.model.BusTripStop;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class StringToBusTripStopConverter {
    private final CsvMapper csvMapper;

    public StringToBusTripStopConverter() {
        this.csvMapper = new CsvMapper();
        this.csvMapper.registerModule(new JavaTimeModule());
        this.csvMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    public List<BusTripStop> getBusTripStopsFromString(String content) throws IOException {
        CsvSchema orderLineSchema = CsvSchema.emptySchema().withHeader();
        MappingIterator<BusTripStop> orderLines = csvMapper.readerFor(BusTripStop.class)
                .with(orderLineSchema)
                .readValues(content);

        return orderLines.readAll();
    }
}
