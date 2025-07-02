package org.example.Controllers;

import org.example.Models.Food;
import org.example.Services.FoodService;

import java.util.List;

public class FoodController {
    private FoodService foodService;

    public FoodController() {
        this.foodService = new FoodService();
    }

    public List<Food> getAllFoods() {
        return foodService.getAllFoods();
    }

    public boolean addFood(Food food) {
        return foodService.addFood(food);
    }

    public boolean updateFood(Food food) {
        return foodService.updateFood(food);
    }

    public boolean deleteFood(int foodId) {
        return foodService.deleteFood(foodId);
    }

    public Food getFoodById(int foodId) {
        return foodService.getFoodById(foodId);
    }
}
