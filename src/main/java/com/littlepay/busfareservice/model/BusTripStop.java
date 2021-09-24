package com.littlepay.busfareservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.trimToNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusTripStop {
    @JsonProperty("ID")
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("DateTimeUTC")
    private LocalDateTime registeredDateTime;

    @JsonProperty("TapType")
    private TapType tapType;

    @JsonProperty("StopId")
    private String stopId;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("BusID")
    private String busId;

    @JsonProperty("PAN")
    private String pan;

    public String getId() {
        return trimToNull(id);
    }


    public String getStopId() {
        return trimToNull(stopId);
    }

    public String getCompanyId() {
        return trimToNull(companyId);
    }

    public String getBusId() {
        return trimToNull(busId);
    }

    public String getPan() {
        return trimToNull(pan);
    }
}
