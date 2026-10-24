package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {

    private final Scanner scanner;
    private final UserDao userDao = new UserDao();

    public Authentication(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Main loop: LogIn / Register / Exit. */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n==============================");
            System.out.println(" EBIKE RECORDS MANAGEMENT");
            System.out.println("==============================");
            System.out.println("1. LogIn");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print(" CHOOSE AN OPTION: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void register() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();
        String role = askRole();
        if (role == null) return;

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }
        userDao.registerUser(username, password, role);
    }

    private void login() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();
        String role = askRole();
        if (role == null) return;

        User user = userDao.findForLogin(username, password, role);
        if (user == null) {
            System.out.println("Login failed. Check your username, password and role.");
            return;
        }
        if (user.Status().equalsIgnoreCase("PENDING")) {
            System.out.println("Your account is still waiting for admin approval.");
            return;
        }
        if (!user.Status().equalsIgnoreCase("APPROVED")) {
            System.out.println("Your account is " + user.Status() + ". Contact the admin.");
            return;
        }

        System.out.println("Welcome, " + user.Username() + "!");
        routeByRole(user);
    }

    /** Sends the logged-in user to the menu for their role. */
    private void routeByRole(User user) {
        switch (user.Roles().toUpperCase()) {
            case "ADMIN" -> new AdminMenu(scanner).display(user);
            case "USER"  -> new UserMenu(scanner).display(user);
            default      -> System.out.println("Unknown role: " + user.Roles());
        }
    }

    private String askRole() {
        System.out.print("Select Role [1] USER [2] ADMIN: ");
        String r = scanner.nextLine().trim();
        if (r.equals("1")) return "USER";
        if (r.equals("2")) return "ADMIN";
        System.out.println("Invalid role.");
        return null;
    }
}