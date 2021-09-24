package com.littlepay.busfareservice.service;

import com.littlepay.busfareservice.exception.InvalidTripException;
import com.littlepay.busfareservice.model.BusTripStop;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.littlepay.busfareservice.data.ConstantValues.STOP1_ID;
import static com.littlepay.busfareservice.data.ConstantValues.STOP2_ID;
import static com.littlepay.busfareservice.data.ConstantValues.STOP3_ID;

@Service
public class TripValidationService {

    public void validateStops(List<BusTripStop> stops) {
        stops.forEach(this::validateEachStop);
    }

    private void validateEachStop(BusTripStop stop) {
        if (StringUtils.isEmpty(stop.getBusId())
                || StringUtils.isEmpty(stop.getStopId())
                || StringUtils.isEmpty(stop.getCompanyId())
                || StringUtils.isEmpty(stop.getPan())
                || StringUtils.isEmpty(stop.getId())
                || stop.getRegisteredDateTime() == null) {
            throw new InvalidTripException("all stop attributes must not be null");
        }

        if (!STOP1_ID.equalsIgnoreCase(stop.getStopId())
                && !STOP2_ID.equalsIgnoreCase(stop.getStopId())
                && !STOP3_ID.equalsIgnoreCase(stop.getStopId())) {
            throw new InvalidTripException("stopId must be Stop1, Stop2 or Stop3");
        }
    }
}
