package com.emjiayuan.app.event;

import com.emjiayuan.app.entity.Product;

import java.util.List;

public class ColUpdateEvent {
    private List<Product> productList;

    public ColUpdateEvent(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
