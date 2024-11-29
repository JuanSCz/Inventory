package com.interonda.Inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<Map<String, Object>> items = List.of(
                Map.of("number", 148),
                Map.of("number", 78),
                Map.of("number", 368),
                Map.of("number", 45),
                Map.of("number", 92),
                Map.of("number", 148),
                Map.of("number", 78),
                Map.of("number", 368),
                Map.of("number", 45),
                Map.of("number", 92)
        );
        model.addAttribute("items", items);
        return "dashboard";
    }
}