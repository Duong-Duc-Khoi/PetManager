package org.example;

import org.example.Views.Auth.LoginView;
import org.example.Views.Auth.RegisterView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
    }

}
