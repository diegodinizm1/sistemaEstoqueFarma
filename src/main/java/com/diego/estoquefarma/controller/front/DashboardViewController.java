package com.diego.estoquefarma.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardViewController {

    @GetMapping("/view/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}