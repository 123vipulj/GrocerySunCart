package com.suncart.grocerysuncart.model;

public class BestDealModel {
    String id, productName, productMRP, productSP,
            totalStock, productWeight, productPics;

    public BestDealModel(String id, String productName, String productMRP, String productSP, String totalStock, String productWeight, String productPics) {
        this.id = id;
        this.productName = productName;
        this.productMRP = productMRP;
        this.productSP = productSP;
        this.totalStock = totalStock;
        this.productWeight = productWeight;
        this.productPics = productPics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
