package org.example.Utils;

import org.example.Views.Auth.AccountView;
import org.example.Views.Auth.LoginView;
import org.example.Views.Auth.RegisterView;
import org.example.Views.Food.FoodCategoryView;
import org.example.Views.Pet.PetLogView;
import org.example.Views.Pet.PetView;
import org.example.Views.Pet.RecordLogView;
import org.example.Views.Pet.SupplierView;
import org.example.Views.Sale.PetSaleView;
import org.example.Views.Invoices.InvoicesView;
import org.example.Views.Statistic.StatisticsView;
import org.example.Views.Food.FoodView;


public class ViewManager {
    private static AccountView accountView;
    private static PetView petView;
    private static RegisterView registerView;
    private static LoginView loginView;
    private static SupplierView supplierView;
    private static PetLogView petLogView;
    private static RecordLogView recordLogView;
    private static PetSaleView petSaleView;
    private static InvoicesView invoiceView;
    private static StatisticsView statisticsView;
    private static FoodView foodView;
    private static FoodCategoryView foodCategoryView;


    public static void showRegisterView() {
        UIManagers.applyGlobalFont();
        if (registerView == null) {
            registerView = new RegisterView();
        }
        registerView.setVisible(true);
        registerView.toFront();
    }

    public static void showAccountView() {
        UIManagers.applyGlobalFont();
        if (accountView == null ) {
            accountView = new AccountView();
        }
        accountView.setVisible(true);
        accountView.toFront();
    }
    public static void showStatics() {
        UIManagers.applyGlobalFont();
        if (statisticsView == null) {
            statisticsView = new StatisticsView();
        } else {
            statisticsView.reload();
        }
        statisticsView.setVisible(true);
        statisticsView.toFront();
    }


    public static void showPetView() {
        UIManagers.applyGlobalFont();
        if (petView == null ) {
            petView = new PetView();
        }
        petView.setVisible(true);
        petView.toFront();
    }

    public static void showSupplier() {
        UIManagers.applyGlobalFont();
        if (supplierView == null ) {
            supplierView = new SupplierView();
        }
        supplierView.setVisible(true);
        supplierView.toFront();
    }

    public static void showPetLog() {
        UIManagers.applyGlobalFont();
        if (petLogView == null ) {
            petLogView = new PetLogView();
        }
        petLogView.setVisible(true);
        petLogView.toFront();
    }

    public static void showRecordLog() {
        UIManagers.applyGlobalFont();
        if (recordLogView == null ) {
            recordLogView = new RecordLogView();
        }
        recordLogView.setVisible(true);
        recordLogView.toFront();
    }

    public static void showPetSale() {
        UIManagers.applyGlobalFont();
        if (petSaleView == null ) {
            petSaleView = new PetSaleView();
        }
        petSaleView.setVisible(true);
        petSaleView.toFront();
    }

    public static void showInvoice() {
        UIManagers.applyGlobalFont();
        if (invoiceView == null ) {
            invoiceView = new InvoicesView();
        }
        invoiceView.setVisible(true);
        invoiceView.toFront();
    }

    public static void showFood() {
        UIManagers.applyGlobalFont();
        if (foodView == null) {
            foodView = new FoodView();
        }
        foodView.setVisible(true);
        foodView.toFront();
    }
    public static void showFoodCategory() {
        UIManagers.applyGlobalFont();
        if (foodCategoryView == null) {
            foodCategoryView = new FoodCategoryView();
        }
        foodCategoryView.setVisible(true);
        foodCategoryView.toFront();
    }



    public static void closeAll() {
        if (accountView != null) accountView.dispose();
        if (supplierView != null) supplierView.dispose();
        if (petView != null) petView.dispose();
        if (registerView != null) registerView.dispose();
        if (loginView != null) loginView.dispose();
        if (supplierView != null) supplierView.dispose();
        if (petLogView != null) petLogView.dispose();
        if (recordLogView != null) recordLogView.dispose();
        if (invoiceView != null) invoiceView.dispose();
        if (foodView != null) foodView.dispose();
        if (foodCategoryView != null) foodCategoryView.dispose();

    }
}
