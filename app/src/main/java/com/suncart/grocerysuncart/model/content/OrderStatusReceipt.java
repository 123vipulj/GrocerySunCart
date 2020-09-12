package com.suncart.grocerysuncart.model.content;

public class OrderStatusReceipt {
    String id, created_at, product_mrp,
            order_qty, product_discount,
            product_name;

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public String getOrder_qty() {
        return order_qty;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public String getProduct_name() {
        return product_name;
    }
}
