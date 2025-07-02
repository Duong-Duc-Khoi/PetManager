package org.example.Controllers;

import org.example.Models.PetCategory;
import org.example.Services.PetCategoryService;

import java.util.List;

public class PetCategoryController {
    private PetCategoryService petCategoryService;

    public PetCategoryController() {
        this.petCategoryService = new PetCategoryService();
    }

    public List<PetCategory> getAllCategories() {
        return petCategoryService.getAllCategories();
    }

    public boolean addCategory(PetCategory category) {
        return petCategoryService.addCategory(category);
    }

    public boolean updateCategory(PetCategory category) {
        return petCategoryService.updateCategory(category);
    }

    public boolean deleteCategory(int id) {
        return petCategoryService.deleteCategory(id);
    }

    public boolean exportCategoriesToExcel(String filePath) {
        return petCategoryService.exportCategoriesToExcel(filePath);
    }
}
