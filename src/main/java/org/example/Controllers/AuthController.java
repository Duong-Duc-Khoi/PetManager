package org.example.Controllers;

import org.example.DTO.LoginRequest;
import org.example.Services.AuthService;
import org.example.Services.AuthService;
import org.example.Views.Auth.LoginView;
import org.example.Views.HomeView;

import javax.swing.*;

public class AuthController {
    private AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public boolean login(String username, String password) {
        boolean result = authService.login(username, password);
        return result;
    }

    public boolean register(String username, String password, String role) {
        boolean result = authService.register(username, password, role);
        return result;
    }
}
