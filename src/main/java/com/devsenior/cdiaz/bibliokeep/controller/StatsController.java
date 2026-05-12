package com.devsenior.cdiaz.bibliokeep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.bibliokeep.model.dto.stats.DashboardStatsResponse;
import com.devsenior.cdiaz.bibliokeep.service.StatsService;
import com.devsenior.cdiaz.bibliokeep.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;
    private final SecurityUtils securityUtils;

    @GetMapping("/dashboard")
    @ResponseStatus(HttpStatus.OK)
    public DashboardStatsResponse getDashboard() {
        var ownerId = securityUtils.getCurrentUserId();
        return statsService.getDashboardStats(ownerId);
    }

}
