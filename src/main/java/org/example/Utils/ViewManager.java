package org.example.Utils;

import org.example.Views.Auth.AccountView;
import org.example.Views.Auth.LoginView;
import org.example.Views.Auth.RegisterView;
import org.example.Views.Pet.PetLogView;
import org.example.Views.Pet.PetView;
import org.example.Views.Pet.RecordLogView;
import org.example.Views.Pet.SupplierView;
import org.example.Views.Sale.PetSaleView;
import org.example.Views.Invoices.InvoicesView;
import org.example.Views.Statistic.StatisticsView;

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
        if (accountView == null || !accountView.isDisplayable()) {
            accountView = new AccountView();
        }
        accountView.setVisible(true);
        accountView.toFront();
    }
    public static void showStatics() {
        UIManagers.applyGlobalFont();
        if (statisticsView == null || statisticsView.isDisplayable()) {
            statisticsView = new StatisticsView();
        }
        statisticsView.setVisible(true);
        statisticsView.toFront();
    }

    public static void showPetView() {
        UIManagers.applyGlobalFont();
        if (petView == null || !petView.isDisplayable()) {
            petView = new PetView();
        }
        petView.setVisible(true);
        petView.toFront();
    }

    public static void showSupplier() {
        UIManagers.applyGlobalFont();
        if (supplierView == null || supplierView.isDisplayable()) {
            supplierView = new SupplierView();
        }
        supplierView.setVisible(true);
        supplierView.toFront();
    }

    public static void showPetLog() {
        UIManagers.applyGlobalFont();
        if (petLogView == null || petLogView.isDisplayable()) {
            petLogView = new PetLogView();
        }
        petLogView.setVisible(true);
        petLogView.toFront();
    }

    public static void showRecordLog() {
        UIManagers.applyGlobalFont();
        if (recordLogView == null || recordLogView.isDisplayable()) {
            recordLogView = new RecordLogView();
        }
        recordLogView.setVisible(true);
        recordLogView.toFront();
    }

    public static void showPetSale() {
        UIManagers.applyGlobalFont();
        if (petSaleView == null || petSaleView.isDisplayable()) {
            petSaleView = new PetSaleView();
        }
        petSaleView.setVisible(true);
        petSaleView.toFront();
    }

    public static void showInvoice() {
        UIManagers.applyGlobalFont();
        if (invoiceView == null || invoiceView.isDisplayable()) {
            invoiceView = new InvoicesView();
        }
        invoiceView.setVisible(true);
        invoiceView.toFront();
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
    }
}
