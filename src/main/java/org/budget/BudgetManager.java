package org.budget;

import java.io.File;
import java.util.Map;

public interface BudgetManager {

    Map<String, Float> getSortedPurchases(int sortOption, int sortCatId, String[] purchaseCategory);

    void addIncome(int income);

    void addPurchase(ItemDetails itemDetails);

    Map<String, Float> getPurchases(int categoryId);

    float getBalance();

    boolean savePurchases(File fileName);

    boolean loadPurchases(File fileName);
}
