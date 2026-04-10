package com.devsenior.cdiaz.bibliokeep.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.cdiaz.bibliokeep.config.security.UserPrincipal;
import com.devsenior.cdiaz.bibliokeep.model.dto.stats.DashboardStatsResponse;
import com.devsenior.cdiaz.bibliokeep.service.StatsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/dashboard")
    @ResponseStatus(HttpStatus.OK)
    public DashboardStatsResponse getDashboard() {
        var ownerId = getCurrentUserId();
        return statsService.getDashboardStats(ownerId);
    }

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        return principal.getId();
    }
}
