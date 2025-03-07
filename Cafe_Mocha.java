/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.cafe_mocha;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hirushi
 */
public class Cafe_Mocha {

//Register admin to the system
    private static void registerAdmin() throws IOException {
        Scanner input = new Scanner(System.in);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Cafe_Mocha_Admin.txt", true))) {
            String username = "";
            // Loop until the user enters a valid username
            while (true) {
                System.out.print("Enter username: ");
                username = input.nextLine();

                if (username.isEmpty()) {//check username is empty
                    System.out.println("Username cannot be empty.");
                } else if (username.length() < 5) { //username should atleast 5 characters
                    System.out.println("Username must be at least 5 characters long.");
                } else if (!isUserNameUnique(username)) { //check username is already exsist.
                    System.out.println("Username already exists.");
                } else {
                    // Username is valid, break out of the loop
                    break;
                }
            }

            System.out.print("Enter password: ");
            String password = input.nextLine();
            //check password is empty.
            while (password.isEmpty()) {
                System.out.print("Enter password: ");
                password = input.nextLine();
                if (password.isEmpty()) {
                    System.out.println("Password cannot be empty.");
                }
            }

            writer.write(username + " " + password);
            writer.newLine();
        }

        System.out.println("Admin registered successfully!");
    }

    //check username already registered or not.
    private static boolean isUserNameUnique(String username) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Admin.txt"))) {
            String check;
            while ((check = reader.readLine()) != null) {
                if (check.split(" ")[0].equalsIgnoreCase(username)) {
                    reader.close();
                    return false;
                }
            }
        }
        return true;
    }

    //Admin Login
    public static void loginAdmin() throws FileNotFoundException, IOException {
        Scanner input = new Scanner(System.in);
        int tryattempt = 0;//Max limit for enter username and password

        //User can try ony 3 times
        while (tryattempt < 3) {
            System.out.print("Please Enter Username :");
            String username = input.nextLine().trim();

            //check empty username
            if (username.isEmpty()) {
                System.out.println("Username Can't be Empty");
                continue;
            }
            System.out.print("Please Enter Password :");
            String password = maskPassword(input);

            //check empty Password
            if (password.isEmpty()) {
                System.out.println("Password can't be Empty.");
                continue;
            }
            //check Username and password is correct
            if (validUsernameAndPassword(username, password)) {
                System.out.println("Login Successful! Welcome " + username + ".");
                showAdminMenu();
                return;

            } else {
                System.out.println("Username or Password is Invalid");
                System.out.println("Attempt " + (tryattempt + 1) + " of 3 failed.");
                tryattempt++;
            }
        }
        System.out.println("Too Many Failed Attempts. System Blocked for 30 seconds;");
        try {
            Thread.sleep(30000);//System Block for 30 seconds
        } catch (InterruptedException ex) {
            Logger.getLogger(Cafe_Mocha.class.getName()).log(Level.SEVERE, "Error during sleep after failed login attempts", ex);
        }
        System.out.println("Please Try Again for login");
    }

    //Password Masked Method 
    // Note: Password masking works in console environments but may not work in all IDEs
    public static String maskPassword(Scanner input) {
        Console console = System.console();
        if (console != null) {
            char[] arrayPassword = console.readPassword();
            return new String(arrayPassword);
        } else {
            System.out.println("Password is Visible");
            return input.nextLine();
        }
    }

    //Check username and password is saved in file. username and password are parameters and return booleas\n value from this method
    public static boolean validUsernameAndPassword(String username, String password) throws FileNotFoundException, IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Admin.txt"));
            String check;

            while ((check = reader.readLine()) != null) {
                String[] checkArray = check.split("\\s+");
                if ((check.length() > 0 && checkArray[0].equalsIgnoreCase(username)) && (check.length() > 0 && checkArray[1].equals(password))) {
                    return true;
                }

            }
        } catch (IOException e) {
            System.out.println("e");
        }

        return false;
    }

    //show admin menu
    public static void showAdminMenu() throws IOException {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("===== Admin Menu =====");
            System.out.println("1. Register New Customer");
            System.out.println("2. Place Order");
            System.out.println("3. View All Orders");
            System.out.println("4. Add New Menu Item");
            System.out.println("5. View Menu");
            System.out.println("6. Help");
            System.out.println("7. Logout");
            System.out.print("Select the option : ");

            int number;
            try {
//convert string into an int. If the user enters something like "42", it becomes the integer 42.
                number = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (number) {
                case 1 ->
                    registerCustomer();
                case 2 ->
                    getOrderFromCustomer();
                case 3 ->
                    viewAllOrders();
                case 4 ->
                    addNewItem();
                case 5 ->
                    viewMenu();
                case 6 ->
                    helpSystem();
                case 7 -> {
                    logOut();
                    return; // Exit menu after logout
                }
                default ->
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    //Register Customer to the system
    public static void registerCustomer() throws IOException {
        //Get Customer Details
        Scanner input = new Scanner(System.in);
        System.out.print("Enter First Name :");
        String firstname = input.nextLine();

        System.out.print("Enter Last Name :");
        String lastname = input.nextLine();

        System.out.print("Enter Mobile Number :");

        String mobilenumber = input.nextLine();
        //Check Mobile Number is valid
        while (mobilenumber.length() != 10) {
            System.out.println("This Mobile Number is Invalid. Please Enter Exactly 10 digits ");
            mobilenumber = input.nextLine();
        }
        if (checkSavedMobileNumber(mobilenumber)) {
            System.out.println("This mobile number already registerd. Please Enter another mobile number");
            mobilenumber = input.nextLine();
        }
        System.out.print("Enter Address :");
        String address = input.nextLine();

        savedCustomerDetails(firstname, lastname, mobilenumber, address);

        System.out.println("Customer Registration Successful.");

        System.out.print("If You want to go to the menu Enter Yes :");
        String back = input.nextLine();
        if (back.equalsIgnoreCase("yes")) {
            showAdminMenu();
        }
    }

    //save customer firstname, lastname, mobilenumber,address in the "Cafe_Mocha_Customer.txt" file as a get paraeters. 
    public static void savedCustomerDetails(String firstname, String lastname, String mobilenumber, String address) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Cafe_Mocha_Customer.txt", true))) {
            writer.append(firstname).append(" ").append(lastname).append(" ").append(mobilenumber).append(" ").append(address);
            writer.newLine();
        }
    }

//check mobilenumber is already registerd using mobile parameter. Pass true or false boolean value. 
    public static boolean checkSavedMobileNumber(String mobileNumber) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3 && parts[2].equals(mobileNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

//Get order from the customer
    public static void getOrderFromCustomer() throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Mobile Number: ");
        String mobileNumber = input.nextLine().trim();

        //check mobile number is saved in file
        if (!checkSavedMobileNumber(mobileNumber)) {
            System.out.println("Customer not registered. Please register first.");
            return;
        }

        ArrayList<String> orderDetails = new ArrayList<>();
        ArrayList<String> quantities = new ArrayList<>();

        viewMenu();

        //Get Item IDs and Quentity while user enter done
        while (true) {
            System.out.print("Enter Item ID (or type 'Done' to finish): ");
            String itemID = input.nextLine().trim();

            if (itemID.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter Quantity: ");
            String quantity = input.nextLine().trim();

            orderDetails.add(itemID);
            quantities.add(quantity);
        }

        saveCustomerOrder(mobileNumber, orderDetails, quantities);
        calculateBill(mobileNumber, orderDetails, quantities);
    }

//get cutomer first name and last name from file for the bill. Use mobilenumber for parameter. Because details get according to the mobile number
    public static void getCustomerDetails(String mobileNumber) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Customer.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3 && mobileNumber.equals(parts[2])) {
                    String firstName = parts[0];
                    String lastName = parts[1];
                    String fullName = firstName + " " + lastName;
                    System.out.println("Customer: " + fullName);
                    return; // Customer found, no need to continue
                }
            }
        }
    }

//This method for view menu form "Cafe_Mocha_Menu.txt" file.
    public static void viewMenu() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Menu.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

//This for save orders in "Cafe_Mocha_Order.txt" file. Get mobilenumber order details value from parameters.
    public static void saveCustomerOrder(String mobileNumber, ArrayList<String> orderDetails, ArrayList<String> quantities) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Cafe_Mocha_Order.txt", true))) {
            writer.append("Mobile: ").append(mobileNumber);
            writer.newLine();

            for (int i = 0; i < orderDetails.size(); i++) {
                writer.write("Item ID: " + orderDetails.get(i) + " Quantity: " + quantities.get(i));
                writer.newLine();
            }
            writer.newLine();
        }
    }

//From this calling getCustomerDetails() and getCustomerOrderDetails() method passing arguments. 
    public static void calculateBill(String mobileNumber, ArrayList<String> orderDetails, ArrayList<String> quantities) throws IOException {
        try {

            getCustomerDetails(mobileNumber);
            getCustomerOrderDetails(mobileNumber, orderDetails, quantities);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Cafe_Mocha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get customer order details quantity Item IDs for bill.
    public static void getCustomerOrderDetails(String mobileNumber, ArrayList<String> orderDetails, ArrayList<String> quantities) throws IOException {
        System.out.println("=======================================");
        System.out.println("Item ID\t\tQuantity\tPrice (LKR)");
        System.out.println("---------------------------------------");

        //assign menu items for munuItems array
        ArrayList<String[]> menuItems = loadMenuItems();

        double total = 0.0;
        boolean hasValidItems = false;

        for (int i = 0; i < orderDetails.size(); i++) {
            String itemCode = orderDetails.get(i).trim();
            String quantityStr = quantities.get(i).trim();

            int quantity = parseQuantity(quantityStr, itemCode);
            if (quantity <= 0) {
                continue;
            }

            boolean itemFound = false;

            for (String[] menuItem : menuItems) {
                if (menuItem[0].equalsIgnoreCase(itemCode)) {
                    double price = Double.parseDouble(menuItem[2]);
                    double itemTotal = price * quantity; //Calculate full total
                    total += itemTotal;
                    System.out.printf("%s\t\t%d\t\t%.2f%n", itemCode, quantity, itemTotal);
                    itemFound = true;
                    hasValidItems = true;
                    break;
                }
            }

            if (!itemFound) {
                System.out.printf("%s\t\t%d\t\t%s%n", itemCode, quantity, "Item Not Found");
            }
        }

        if (hasValidItems) {
            printBillSummary(total, mobileNumber);
        } else {
            System.out.println("No valid items found in the order.");
        }
    }

//load menu items
    private static ArrayList<String[]> loadMenuItems() throws IOException {
        ArrayList<String[]> menuItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Menu.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.contains(":") || line.toLowerCase().contains("item code")) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length < 3) {
                    continue;
                }

                String itemCode = parts[0];
                String price = parts[parts.length - 1];
                StringBuilder itemName = new StringBuilder();
                for (int i = 1; i < parts.length - 1; i++) {
                    itemName.append(parts[i]).append(" ");
                }
                menuItems.add(new String[]{itemCode, itemName.toString().trim(), price});
            }
        }
        return menuItems;
    }

//Pass quantity for each item as a integer return type. As a prameters use itemcode and quantity arrays
    private static int parseQuantity(String quantityStr, String itemCode) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            System.err.printf("Invalid quantity '%s' for item %s. Skipping.%n", quantityStr, itemCode);
            return -1;
        }
    }

//This for print bill to the customer. Using parameters get mobilenumber and total. Also add disscount for bill.
    private static void printBillSummary(double total, String mobileNumber) throws IOException {
        double tax = total * 0.10;
        boolean isReturnCustomer = checkReturnCustomer(mobileNumber);
        double discount = isReturnCustomer ? total * 0.05 : 0.0;
        double finalTotal = total + tax - discount;

        System.out.println("---------------------------------------");
        System.out.printf("Subtotal: %.2f LKR%n", total);
        System.out.printf("Tax (10%%): %.2f LKR%n", tax);

        if (isReturnCustomer) {
            System.out.printf("Loyalty Discount (5%%): -%.2f LKR%n", discount);
        }

        System.out.printf("Total Bill Amount: %.2f LKR%n", finalTotal);
        System.out.println("=======================================");
    }

//Check customer is return customer get mobilenumber as a parameter and return true or false as bolean valus.
    private static boolean checkReturnCustomer(String mobileNumber) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Order.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mobile: " + mobileNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

//This method for display the help guides for users
    public static void helpSystem() {
        System.out.println("========== System Usage Guidelines ==========");
        System.out.println("1. Login & Access:");
        System.out.println("   - Log in using your assigned credentials.");
        System.out.println("   - Always log out after using the system.");
        System.out.println();

        System.out.println("2. Main Menu Navigation:");
        System.out.println("   - Use the menu options to:");
        System.out.println("     1. Add new menu items.");
        System.out.println("     2. Search for items.");
        System.out.println("     3. Place orders.");
        System.out.println("     4. View order details.");
        System.out.println("     5. View system usage guidelines.");
        System.out.println("     6. Exit the system.");
        System.out.println();

        System.out.println("3. Placing Orders:");
        System.out.println("   - Enter item codes and quantities correctly.");
        System.out.println("   - Double-check order details before confirming.");
        System.out.println();

        System.out.println("4. Menu File Handling:");
        System.out.println("   - Menu data is stored in 'Cafe_Mocha_Menu.txt'.");
        System.out.println("   - Do not modify this file directly.");
        System.out.println();

        System.out.println("5. Error Handling:");
        System.out.println("   - Follow on-screen prompts for any errors.");
        System.out.println("   - Report repeated errors to the system administrator.");
        System.out.println();

        System.out.println("6. Security & Data Privacy:");
        System.out.println("   - Customer data must remain confidential.");
        System.out.println("   - Access to customer data should be limited to authorized users.");
        System.out.println();

        System.out.println("7. System Maintenance & Backups:");
        System.out.println("   - Backup data regularly.");
        System.out.println("   - Report unusual behavior to IT support.");
        System.out.println();

        System.out.println("8. General Rules:");
        System.out.println("   - Use the system only for work purposes.");
        System.out.println("   - Do not install unauthorized software.");
        System.out.println();

    }

//This method for add new item to the menu
    public static void addNewItem() throws IOException {

        Scanner input = new Scanner(System.in);

        ArrayList<String> menuLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Menu.txt"))) {
            String check;
            while ((check = reader.readLine()) != null) {
                menuLines.add(check);
            }
        }

        String type = "";

        System.out.println("------Item Types------");
        System.out.println("Type 'Drinks : Enter 1");
        System.out.println("Type 'Snacks : Enter 2");
        System.out.println("Type 'Dessert : Enter 3");

        int number = input.nextInt();
        input.nextLine();

        switch (number) {
            case 1 ->
                type = "Drinks";

            case 2 ->
                type = "Snacks";

            case 3 ->
                type = "Desserts";
            default -> {
                System.out.println("Invalid Number. Please Enter correct number :");
                number = input.nextInt();
                input.nextLine();
            }
        }

        String itemCode;

        // Check if item code already exists
        while (true) {
            System.out.println("For Dessert Item COde - 'DS000' For Snackes - 's001' For Drinks - D000");
            System.out.print("Enter Item Code :");
            itemCode = input.nextLine();

            String itemCodePattern = switch (type) {
            case "Drinks" -> "D\\d{3}";
            case "Snacks" -> "S\\d{3}";
            case "Desserts" -> "DS\\d{3}";
            default -> "";
        };

        if (!itemCode.matches(itemCodePattern)) {
            System.out.println("Invalid item code format for " + type + ". Please follow the correct format.");
            continue;
        }
            
            boolean itemAlreadyExists = false;
            for (String menuLine : menuLines) {
                String[] checkArray = menuLine.trim().split("\\s+");
                if (checkArray.length > 0 && checkArray[0].equalsIgnoreCase(itemCode)) {
                    itemAlreadyExists = true;
                    break;
                }
            }

            if (itemAlreadyExists) {
                System.out.println("This Item Code Already Exists. Do you want to enter a new item code or go back to admin menu?");
                System.out.println("Type '1' to enter a new item code or '2' to go back to admin menu:");
                int choice = input.nextInt();
                input.nextLine();

                if (choice == 2) {
                    System.out.println("Returning to admin menu...");
                    return;
                }
            } else {
                break;
            }
        }

        System.out.println("Enter Item Name :");
        String itemName = input.nextLine();

        System.out.println("Enter Item Price :");
        String itemPrice = input.nextLine();

        String newLine = String.format("%-10s%12s%24s", itemCode, itemName, itemPrice);

        ArrayList<String> updatedLines = new ArrayList<>();
        boolean insideSection = false;
        boolean itemAlreadyExists = false;
        boolean itemAdded = false;

        for (String menuLine : menuLines) {
            updatedLines.add(menuLine);
            if (menuLine.trim().equalsIgnoreCase(type + " :")) {
                insideSection = true;
                continue;
            }

            if (insideSection) {
                if (menuLine.trim().isEmpty()) {
                    if (!itemAdded) {
                        updatedLines.add(newLine);
                        itemAdded = true;
                    }
                    insideSection = false;
                } else {
                    String[] checkArray = menuLine.trim().split("\\s+");
                    if (checkArray.length > 0 && checkArray[0].equalsIgnoreCase(itemCode)) {
                        itemAlreadyExists = true;
                        break;
                    }

                }

            }
        }

        if (!itemAdded) {
            updatedLines.add(newLine);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("Cafe_Mocha_Menu.txt"));
        for (String updatedLine : updatedLines) {
            writer.write(updatedLine);
            writer.newLine();
        }
        writer.close();
        System.out.println("Item Added Successfully. ");
    }

//This method for view all the orders
    public static void viewAllOrders() throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader("Cafe_Mocha_Order.txt"));
        System.out.println("=========AllOrders===========");
        String check;
        while ((check = reader.readLine()) != null) {
            if (check.trim().isEmpty()) {
                System.out.println("------------------------------");
            } else {
                System.out.println(check);
            }

        }

    }

//This for exit from the system.
    public static void logOut() {
        System.out.println("Log Out..........");
        System.out.println("Logout Successfully. Come Again!");
    }

//In main method display main menu for the users
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner input = new Scanner(System.in);

        File file2 = new File("Cafe_Mocha_Menu.txt");
        File file3 = new File("Cafe_Mocha_Customer.txt");
        File file4 = new File("Cafe_Mocha_Order.txt");
        File file5 = new File("Cafe_Mocha_Admin.txt");

        //Create Files if not exsist.
        if (!file2.exists()) {
            file2.createNewFile();
        }
        if (!file3.exists()) {
            file3.createNewFile();
        }
        if (!file4.exists()) {
            file4.createNewFile();
        }
        if (!file5.exists()) {
            file5.createNewFile();
        }
        //Menu Loop
        while (true) {
            System.out.println("Welcome To Cafe Mocha ");
            System.err.println("============================");
            System.out.println("1. Admin Login");
            System.out.println("2. Admin Registration");
            System.out.println("3. Help");
            System.out.println("4. Exit");
            System.out.print("Please Choose Your Option :");
            int number = input.nextInt();

            switch (number) {
                case 1 ->
                    loginAdmin();

                case 2 ->
                    registerAdmin();

                case 3 ->
                    helpSystem();

                case 4 -> {
                    logOut();
                    System.exit(0);
                }
                default ->
                    System.out.println("Invalid Option. Please enter correct option.");

            }

        }

    }

}
