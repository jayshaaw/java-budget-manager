package org.budget;

import java.io.*;
import java.util.*;

class ExpenseManager {

    static class OrderByItemPrice implements Comparator<Item> {
        @Override
        public int compare(Item item, Item t1) {
            return Float.compare(t1.getItemPrice(), item.getItemPrice());
        }
    }

    static class OrderByItemName implements Comparator<Item> {

        @Override
        public int compare(Item item, Item t1) {
            return t1.getItemName().compareTo(item.getItemName());
        }
    }

}

public class Main {
    public static void main(java.lang.String[] args) {

        BudgetManager budgetManager = new MyBudgetManager();

        String menu = """
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                5) Save
                6) Load
                7) Analyze (Sort)
                0) Exit""";

        String purchaseMenu = """
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) Back""";

        String showPurchaseMenu = """
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) All
                6) Back""";

        String sortMenu = """
                How do you want to sort?
                1) Sort all purchases
                2) Sort by type
                3) Sort certain type
                4) Back""";

        String sortMenuType = """
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other""";

        String[] purchaseCategory = {"Empty", "Food", "Clothes", "Entertainment", "Other", "All", "Back"};

        String[] sortPurchaseCategory = {"Empty", "Food", "Entertainment", "Clothes", "Other", "Extra"};

        File fileName = new File("./purchases.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.println(menu);
        ExpenseManager expenseManager = new ExpenseManager();
        boolean loop = true;

        while (loop) {
            int mainAction = scanner.nextInt();
            switch (mainAction) {
                case 1 -> {
                    //Add income
                    System.out.println();
                    System.out.println("Enter income: ");
                    budgetManager.addIncome(scanner.nextInt());
                    System.out.println("Income was added!");
                    System.out.println();
                    System.out.println(menu);
                }
                case 2 -> {
                    // Add purchase
                    System.out.println();
                    System.out.println(purchaseMenu);

                    boolean exitPurchaseMenu = false;

                    while (!exitPurchaseMenu) {
                        Scanner scannerPurchase = new Scanner(System.in);
                        int addPurchaseType = scannerPurchase.nextInt();

                        if (addPurchaseType != 5) {
                            System.out.println();
                            System.out.println("Enter purchase name: ");
                            Scanner itemNameScan = new Scanner(System.in);
                            String itemName = itemNameScan.nextLine();

                            System.out.println("Enter its price: ");
                            Scanner itemPriceScan = new Scanner(System.in);
                            float itemPrice = itemPriceScan.nextFloat();

                            budgetManager.addPurchase(new Item(addPurchaseType, itemName, itemPrice));
                            System.out.println("Purchase was added!");

                            System.out.println();
                            System.out.println(purchaseMenu);
                        } else {
                            // purchaseMenu - back selected
                            exitPurchaseMenu = true;
                        }
                    }
                    System.out.println();
                    System.out.println(menu);
                }
                case 3 -> {
                    // Show list of purchases
                    System.out.println();
                    int categoryId = 5;
                    Map<String, Float> result = budgetManager.getPurchases(categoryId);

                    float total = 0;

                    if (result.size() > 0) {
                        System.out.println();
                        System.out.println(showPurchaseMenu);
                        boolean exitPurchaseMenu = false;
                        while (!exitPurchaseMenu) {

                            Scanner categoryScanner = new Scanner(System.in);

                            categoryId = categoryScanner.nextInt();
                            if (categoryId != 6) {
                                total = 0;
                                System.out.println();
                                System.out.println(purchaseCategory[categoryId] + ":");
                                result = budgetManager.getPurchases(categoryId);
                                if (result.size() > 0) {
                                    for (var finalResult : result.entrySet()) {
                                        total += finalResult.getValue();
                                        System.out.printf("%s $%.2f", finalResult.getKey(), finalResult.getValue());
                                        System.out.println();
                                    }
                                    System.out.printf("Total sum: $%.2f", total);
                                    System.out.println();
                                } else {
                                    System.out.println("The purchase list is empty!");
                                }
                                System.out.println();
                                System.out.println(showPurchaseMenu);
                            } else {
                                exitPurchaseMenu = true;
                            }
                        }
                    } else {
                        System.out.println();
                        System.out.println("The purchase list is empty!");
                    }
                    System.out.println();
                    System.out.println(menu);
                }
                case 4 -> {
                    //Display balance
                    System.out.println();
                    float balance = budgetManager.getBalance();
                    balance = balance >= 0 ? balance : 0;
                    System.out.printf("Balance: $%.2f", balance);
                    System.out.println();
                    System.out.println();
                    System.out.println(menu);
                }
                case 5 -> {
                    // Save all purchases to the fileName
                    System.out.println();
                    boolean saveStatus = budgetManager.savePurchases(fileName);
//                    System.out.println("Item purchases saved: " + saveStatus);
                    if (saveStatus) System.out.println("Purchases were saved!");
                    System.out.println();
                    System.out.println(menu);
                }
                case 6 -> {
                    // Load all purchases from the fileName
                    System.out.println();
                    boolean loadStatus = budgetManager.loadPurchases(fileName);
                    if (loadStatus) System.out.println("Purchases were loaded!");
                    System.out.println();
                    System.out.println(menu);
                }
                case 7 -> {
                    // Analyze sort options
                    System.out.println();
                    int sortOption = 1;
                    int sortCatId = 0;
                    Map<String, Float> result;

                    float total = 0;
                    String[] sortCategory = {"Empty", "All", "Types"};
                    System.out.println();
                    System.out.println(sortMenu);

                    boolean exitSortMenu = false;
                    while (!exitSortMenu) {
                        Scanner sortScanner = new Scanner(System.in);
                        sortOption = sortScanner.nextInt();
                        if (sortOption != 4) {
                            total = 0;
                            System.out.println();

                            if (sortOption == 1) {
                                result = budgetManager.getSortedPurchases(sortOption, sortCatId, purchaseCategory);
                                if (result.size() > 0) {
                                    System.out.println(sortCategory[sortOption] + ":");
                                    displaySortResults(result);
                                } else {
                                    System.out.println("The purchase list is empty!");
                                }
                            } else if (sortOption == 2) {
                                result = budgetManager.getSortedPurchases(sortOption, sortCatId, sortPurchaseCategory);
                                Map<String, Float> result1 = Map.of(sortPurchaseCategory[1], 0f, sortPurchaseCategory[2], 0f, sortPurchaseCategory[3], 0f, sortPurchaseCategory[4], 0f);
                                System.out.println(sortCategory[sortOption] + ":");
                                if (result.size() == 0) {
                                    displaySortResultsByType(result1);
                                } else {
                                    displaySortResultsByType(result);
                                }

                            } else {
                                System.out.println(sortMenuType);
                                Scanner sortCategoryId = new Scanner(System.in);
                                sortCatId = sortCategoryId.nextInt();
                                System.out.println();
                                result = budgetManager.getSortedPurchases(sortOption, sortCatId, purchaseCategory);
                                if (result.size() > 0) {
                                    System.out.println(purchaseCategory[sortCatId] + ":");
                                    displaySortResults(result);
                                } else {
                                    System.out.println("The purchase list is empty!");
                                }
                            }
                            System.out.println();
                            System.out.println(sortMenu);
                        } else {
                            exitSortMenu = true;
                        }
                    }
                    System.out.println();
                    System.out.println(menu);
                }
                case 0 -> {
                    loop = false;
                    System.out.println();
                    System.out.println("Bye!");
                }
            }
        }
    }

    private static void displaySortResults(Map<String, Float> result) {
        float total = 0;
        for (var finalResult : result.entrySet()) {
            total += finalResult.getValue();
            System.out.printf("%s $%.2f", finalResult.getKey(), finalResult.getValue());
            System.out.println();
        }
        System.out.printf("Total sum: $%.2f", total);
        System.out.println();
    }

    private static void displaySortResultsByType(Map<String, Float> result) {
        float total = 0;
        for (var finalResult : result.entrySet()) {
            total += finalResult.getValue();
            System.out.printf("%s - $%.2f", finalResult.getKey(), finalResult.getValue());
            System.out.println();
        }
        System.out.println();
    }
}

