package org.budget;

class Item {
    private final int categoryId;
    private final String itemName;
    private final float itemPrice;

    public Item(int categoryId, String itemName, float itemPrice) {
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

}