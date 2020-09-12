package com.suncart.grocerysuncart.model.content;

public class OrderStatus {
    String id, razorpayOrderId, createdAt,
            orderStatus, product_mrp, order_qty, product_discount, product_name;

    public String getProduct_name() {
        return product_name;
    }

    public String getId() {
        return id;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getProduct_mrp() {
        return product_mrp;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public String getOrder_qty() {
        return order_qty;
    }
}
