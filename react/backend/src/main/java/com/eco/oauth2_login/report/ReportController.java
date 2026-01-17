package com.eco.oauth2_login.report;

import java.time.YearMonth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/izvjestaj")

public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{klijentId}/{mjesec}")
    public IzvjestajDTO pdf(@PathVariable Long klijentId, @PathVariable String mjesec) {
        YearMonth ym = YearMonth.parse(mjesec);
        return reportService.generirajMjesecniIzvjestajJson(klijentId, ym);
    }
}

