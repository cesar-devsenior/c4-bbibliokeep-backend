package com.devsenior.cdiaz.bibliokeep.service;

import com.devsenior.cdiaz.bibliokeep.model.dto.stats.DashboardStatsResponse;

import java.util.UUID;

public interface StatsService {
    DashboardStatsResponse getDashboardStats(UUID ownerId);
}
