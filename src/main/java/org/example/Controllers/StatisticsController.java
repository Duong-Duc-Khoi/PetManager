package org.example.Controllers;

import java.util.Map;

import org.example.Services.StatisticsService;

public class StatisticsController {
    private StatisticsService statisticsService;

    public StatisticsController() {
        statisticsService = new StatisticsService();
    }

    public int getTotalInvoices() {
        return statisticsService.getTotalInvoices();
    }

    public double getTotalRevenue() {
        return statisticsService.getTotalRevenue();
    }

    public int getSoldPets() {
        return statisticsService.getSoldPets();
    }

    public int getAvailablePets() {
        return statisticsService.getAvailablePets();
    }

    public Map<String, Double> getMonthlyRevenue() {
        return statisticsService.getMonthlyRevenue();
    }
}
