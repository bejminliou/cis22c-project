package main.java.ui;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    public Menu() {
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("1. Login");
        System.out.println("2. Create Account");
        // ... other menu options

        int choice = scanner.nextInt();
        scanner.nextLine(); // ...

        switch (choice) {
            case 1:
                // Call login() from Account class
                break;
            // ... other cases
        }
    }
}
