package org.example.Controllers;

import org.example.Models.FoodCategory;
import org.example.Services.FoodCategoryService;

import java.util.List;

public class FoodCategoryController {
    private final FoodCategoryService service = new FoodCategoryService();

    public List<FoodCategory> getAll() {
        return service.getAll();
    }

    public boolean add(FoodCategory category) {
        return service.add(category);
    }

    public boolean update(FoodCategory category) {
        return service.update(category);
    }

    public boolean delete(int id) {
        return service.delete(id);
    }
}
