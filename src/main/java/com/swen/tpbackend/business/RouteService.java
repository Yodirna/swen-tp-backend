package com.swen.tpbackend.business;

import com.swen.tpbackend.dal.dto.RouteDto;
import org.springframework.stereotype.Service;

@Service
public interface RouteService {
    RouteDto getRoute(String from, String to, String transportType);
}
