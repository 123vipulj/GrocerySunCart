package com.suncart.grocerysuncart.model;

public class BestDealModel {
    Long id;
    int itemsLimit;
    String  productName, productMRP, productSP,
            totalStock, productWeight, productPics;

    public BestDealModel(Long id, String productName, String productMRP, String productSP, String totalStock, String productWeight, String productPics) {
        this.id = id;
        this.productName = productName;
        this.productMRP = productMRP;
        this.productSP = productSP;
        this.totalStock = totalStock;
        this.productWeight = productWeight;
        this.productPics = productPics;
    }

    public BestDealModel(Long id, int itemsLimit, String productName, String productMRP, String productSP, String totalStock, String productWeight, String productPics) {
        this.id = id;
        this.itemsLimit = itemsLimit;
        this.productName = productName;
        this.productMRP = productMRP;
        this.productSP = productSP;
        this.totalStock = totalStock;
        this.productWeight = productWeight;
        this.productPics = productPics;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getItemsLimit() {
        return itemsLimit;
    }

    public void setItemsLimit(int itemsLimit) {
        this.itemsLimit = itemsLimit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductMRP() {
        return productMRP;
    }

    public void setProductMRP(String productMRP) {
        this.productMRP = productMRP;
    }

    public String getProductSP() {
        return productSP;
    }

    public void setProductSP(String productSP) {
        this.productSP = productSP;
    }

    public String getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(String totalStock) {
        this.totalStock = totalStock;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPics() {
        return productPics;
    }

    public void setProductPics(String productPics) {
        this.productPics = productPics;
    }
}

