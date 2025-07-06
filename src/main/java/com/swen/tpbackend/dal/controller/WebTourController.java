package com.swen.tpbackend.dal.controller;

import com.swen.tpbackend.dal.dto.TourDto;
import com.swen.tpbackend.business.TourService;
import com.swen.tpbackend.dal.entity.TourEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebTourController {

    private final TourService tourService;

    public WebTourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public String getTours(Model model) {
        List<TourEntity> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "tours"; //
    }

}
