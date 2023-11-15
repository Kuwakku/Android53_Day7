package com.example.android53_day7;

import java.util.ArrayList;

public interface ILoadProductsListener {
    void onLoadProductSuccess(ArrayList<ProductModel> productModels);
    void onLoadProductError(String message);
}
