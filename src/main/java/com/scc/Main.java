package com.scc;

import com.scc.ui.Authentication;
import com.scc.dao.DatabaseInitializer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (!DatabaseInitializer.initialize()) {
            System.err.println("Cannot start: database is not ready.");
            return;
        }
        try (Scanner sc = new Scanner(System.in)) {
            new Authentication(sc).start();
        }
    }
}