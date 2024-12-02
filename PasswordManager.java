import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class PasswordManager {
    private static final String DATA_FILE = "passwords.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static Map<String, List<Account>> categories = new HashMap<>();

    public static void main(String[] args) {
        loadFromFile();
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Password Manager, I am Big G!");
            System.out.println("1. Add Account");
            System.out.println("2. Delete Account");
            System.out.println("3. View Categories");
            System.out.println("4. Modify Account");
            System.out.println("5. Generate Password");
            System.out.println("6. Save and Exit");
            System.out.print("Please choose an option: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addAccount();
                    case 2 -> deleteAccount();
                    case 3 -> viewCategories();
                    case 4 -> modifyAccount();
                    case 5 -> generatePassword();
                    case 6 -> { saveToFile(); running = false; }
                    default -> System.out.println("Oops! That's not a valid choice. Please try again....");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number corresponding to your choice!");
            }
        }
    }

    private static void addAccount() {
        System.out.print("Enter the category for this account: ");
        String category = scanner.nextLine();
        System.out.print("Enter the account name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the username: ");
        String username = scanner.nextLine();
        System.out.print("Enter the password: ");
        String password = scanner.nextLine();

        categories.computeIfAbsent(category, k -> new ArrayList<>()).add(new Account(name, username, password));
        System.out.println("Account successfully added!");
    }

    private static void deleteAccount() {
        System.out.print("Enter the category: ");
        String category = scanner.nextLine();
        System.out.print("Enter the account name to delete: ");
        String name = scanner.nextLine();

        List<Account> accounts = categories.getOrDefault(category, new ArrayList<>());
        boolean removed = accounts.removeIf(account -> account.name.equals(name));
        if (removed) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("No account found with that name.");
        }
    }

    private static void viewCategories() {
        if (categories.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        categories.forEach((category, accounts) -> {
            System.out.println(category + ":");
            accounts.forEach(System.out::println);
        });
    }

    private static void modifyAccount() {
        System.out.print("Enter the category: ");
        String category = scanner.nextLine();
        System.out.print("Enter the account name to modify: ");
        String name = scanner.nextLine();

        List<Account> accounts = categories.getOrDefault(category, new ArrayList<>());
        for (Account account : accounts) {
            if (account.name.equals(name)) {
                System.out.print("New username: ");
                account.username = scanner.nextLine();
                System.out.print("New password: ");
                account.password = scanner.nextLine();
                System.out.println("Account updated successfully!");
                return;
            }
        }
        System.out.println("Account not found.");
    }

    private static void generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        System.out.println("Here's your generated password: " + password);
    }

    private static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (var entry : categories.entrySet()) {
                writer.write("Category: " + entry.getKey() + "\n");
                for (var account : entry.getValue()) {
                    writer.write(account + "\n");
                }
            }
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while saving data.");
        }
    }

    private static void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            String category = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Category: ")) {
                    category = line.substring(10);
                    categories.put(category, new ArrayList<>());
                } else if (category != null) {
                    String[] parts = line.split(", ");
                    categories.get(category).add(new Account(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh!");
        }
    }

    static class Account {
        String name, username, password;

        Account(String name, String username, String password) {
            this.name = name;
            this.username = username;
            this.password = password;
        }

        @Override
        public String toString() {
            return name + ", " + username + ", " + password;
        }
    }
}
