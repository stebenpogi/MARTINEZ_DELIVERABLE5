package com.scc.ui;

import com.scc.model.User;

import java.util.Scanner;

public class UserMenu {

    private final Scanner scanner;

    public UserMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void display(User user) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n==========================================");
            System.out.println("              USER DASHBOARD              ");
            System.out.println("==========================================");
            System.out.println("1. View My Account");
            System.out.println("2. Logout");
            System.out.print("Choose an option (1-2): ");

            switch (scanner.nextLine().trim()) {
                case "1" -> System.out.println(user);
                case "2" -> {
                    System.out.println("Logged out.");
                    loggedIn = false;
                }
                default -> System.out.println("Invalid option. Choose 1-2.");
            }
        }
    }
}