package com.scc;

import com.scc.dao.DatabaseConnection;
import com.scc.dao.UserDao;
import com.scc.model.User;
import com.scc.ui.AdminMenu;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final UserDao userDao = new UserDao();

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();

        boolean running = true;
        while (running) {
            System.out.println("\n==============================");
            System.out.println(" EBIKE RECORDS MANAGEMENT");
            System.out.println("==============================");
            System.out.println("1. LogIn");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print(" CHOOSE AN OPTION: ");

            switch (sc.nextLine().trim()) {
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

    private static void register() {
        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = sc.nextLine().trim();
        String role = askRole();
        if (role == null) return;

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }
        userDao.registerUser(username, password, role);
    }

    private static void login() {
        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        System.out.print("Enter your password: ");
        String password = sc.nextLine().trim();
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
        if (user.Roles().equalsIgnoreCase("ADMIN")) {
            new AdminMenu(sc).display(user);
        } else {
            System.out.println("(User menu coming soon.)");
        }
    }

    private static String askRole() {
        System.out.print("Select Role [1] USER [2] ADMIN: ");
        String r = sc.nextLine().trim();
        if (r.equals("1")) return "USER";
        if (r.equals("2")) return "ADMIN";
        System.out.println("Invalid role.");
        return null;
    }
}