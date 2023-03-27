package org.budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MyBudgetManager implements BudgetManager {

    private int income = 0;
    private float purchaseAmount = 0;

    Set<Item> itemList = new TreeSet<>(new ExpenseManager.OrderByItemPrice().thenComparing(new ExpenseManager.OrderByItemName()));

    @Override
    public Map<String, Float> getSortedPurchases(int sortOption, int sortCatId, String[] purchaseCategory) {
        //purchaseCategory = {"Empty", "Food", "Clothes", "Entertainment", "Other", "All", "Back"};
        // sortPurchaseCategory = {"Empty", "Food", "Entertainment",
        //                "Clothes", "Other"}
        Map<String, Float> resultRecords = new LinkedHashMap<>();

        int[] categoryId = {1, 3, 2, 4};

        if (sortOption == 1) {
            itemList.stream().toList().forEach(item -> resultRecords.put(item.getItemName(), item.getItemPrice()));
        } else if (sortOption == 2) {
            int bumpIndex = 0;
            for (var category : categoryId) {
                ++bumpIndex;
                float total = 0;
                for (var item : itemList) {
                    if (item.getCategoryId() == category) {
                        total += item.getItemPrice();
                    }
                    // here purchaseCategory is sortPurchaseCategory - the
                    // order of elements is changed
                    resultRecords.put(purchaseCategory[bumpIndex], total);
                }
            }
        } else {
            // Sort by Type of purchase
            itemList.stream().toList().forEach(item -> {
                if (item.getCategoryId() == sortCatId) {
                    resultRecords.put(item.getItemName(), item.getItemPrice());
                }
            });
        }

        return resultRecords;
    }

    @Override
    public void addIncome(int income) {
        this.income = income;
    }

    @Override
    public void addPurchase(Item item) {
        itemList.add(item);
        purchaseAmount += item.getItemPrice();
    }

    @Override
    public Map<String, Float> getPurchases(int categoryId) {
        Map<String, Float> resultRecords = new LinkedHashMap<>();

        if (categoryId == 5) {
            itemList.stream().toList().forEach(item -> resultRecords.put(item.getItemName(), item.getItemPrice()));
        } else {
            itemList.stream().toList().forEach(item -> {
                if (item.getCategoryId() == categoryId) {
                    resultRecords.put(item.getItemName(), item.getItemPrice());
                }
            });
        }

        return resultRecords;
    }

    @Override
    public float getBalance() {
        return income - purchaseAmount;
    }

    @Override
    public boolean savePurchases(File fileName) {
        try (PrintWriter printWriter = new PrintWriter(fileName)) {
            for (Item item : itemList) {
                printWriter.println(item.getCategoryId() + "__" + item.getItemName() + "__" + item.getItemPrice());
            }
            printWriter.println(0 + "__" + "Total" + "__" + String.format("%.2f", purchaseAmount));
            printWriter.println(0 + "__" + "Income" + "__" + income);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean loadPurchases(File fileName) {
        try (Scanner scanner = new Scanner(fileName)) {
            while (scanner.hasNext()) {
//                System.out.println(scanner.nextLine());
                String[] temp = scanner.nextLine().split("__");
//                System.out.println(temp[0] + ": " + temp[1] + ": " + temp[2]);
                if (Objects.equals(temp[1], "Income")) {
                    income = (int) Float.parseFloat(temp[2]);
                } else if (Objects.equals(temp[1], "Total")) {
                    purchaseAmount = Float.parseFloat(temp[2]);

                } else {
                    itemList.add(new Item(Integer.parseInt(temp[0]), temp[1], Float.parseFloat(temp[2])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
