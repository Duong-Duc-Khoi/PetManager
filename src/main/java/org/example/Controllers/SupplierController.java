package org.example.Controllers;

import org.example.Models.Supplier;
import org.example.Services.SupplierService;
import org.example.Views.Pet.SupplierView;

import java.awt.*;
import java.util.List;

public class SupplierController {
    private SupplierService supplierService;

    public SupplierController() {
        this.supplierService = new SupplierService();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    public boolean addSupplier(Supplier supplier) {
        return supplierService.addSupplier(supplier);
    }

    public boolean updateSupplier(Supplier updatedSupplier) {
        return supplierService.updateSupplier(updatedSupplier);
    }

    public boolean deleteSupplier(int id) {
        return supplierService.deleteSupplier(id);
    }
}
