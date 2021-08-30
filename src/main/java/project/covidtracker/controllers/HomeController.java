package project.covidtracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.covidtracker.models.LocationData;
import project.covidtracker.services.CovidDataService;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    CovidDataService covidDataService;

    @GetMapping("/")
    public String getHomepage(Model model){
        List<LocationData> allData= covidDataService.getAllData();
        int totalCase = allData.stream().mapToInt(data -> data.getLatestCaseNumber()).sum();
        model.addAttribute("allData", allData);
        model.addAttribute("totalCase", totalCase);
        model.addAttribute("vietnamCase", covidDataService.getVietnamCase());
        return "homepage";
    }
}
