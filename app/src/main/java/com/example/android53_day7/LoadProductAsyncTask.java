package com.example.android53_day7;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LoadProductAsyncTask extends AsyncTask<Void, Void, ArrayList<ProductModel>> {

    private String url;
    private TextView tvDemo;
    private ILoadProductsListener productsListener;

    public LoadProductAsyncTask(String url, TextView tvDemo) {
        this.url = url;
        this.tvDemo = tvDemo;
    }

    public void setProductsListener(ILoadProductsListener productsListener) {
        this.productsListener = productsListener;
    }

    @Override
    protected ArrayList<ProductModel> doInBackground(Void... voids) {
        String apiLink = url;
        ArrayList<ProductModel> res = new ArrayList<>();
        try {
            URL url = new URL(apiLink);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder stringBuilder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(inputString);
                }
//                Log.d("TAG", "doInBackground: " + stringBuilder);
                Gson gson = new Gson();
                ProductsRespondse productsRespondse = gson.fromJson(String.valueOf(stringBuilder), ProductsRespondse.class);
                res = (ArrayList<ProductModel>) productsRespondse.getProducts();

                Log.d("TAG", "doInBackground: limit: " + productsRespondse.getProducts().size());
            } catch (IOException e) {
//                e.printStackTrace();
                productsListener.onLoadProductError(e.getMessage());
            }
        } catch (MalformedURLException e) {
//            e.printStackTrace();
            productsListener.onLoadProductError(e.getMessage());
        }
        return res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }

//    @Override
//    protected ArrayList<ProductModel> doInBackground(String... strings) {
//        String apiLink = strings[0];
//        ArrayList<ProductModel> res = new ArrayList<>();
//        try {
//            URL url = new URL(apiLink);
//            try {
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream stream = urlConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
//                StringBuilder stringBuilder = new StringBuilder();
//                String inputString;
//                while ((inputString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(inputString);
//                }
////                Log.d("TAG", "doInBackground: " + stringBuilder);
//                Gson gson = new Gson();
//                ProductsRespondse productsRespondse = gson.fromJson(String.valueOf(stringBuilder), ProductsRespondse.class);
//                Log.d("TAG", "doInBackground: limit: " + productsRespondse.getProducts().size());
//                try {
//                    JSONObject jsonData = new JSONObject(String.valueOf(stringBuilder));
//                    int limit = jsonData.getInt("limit");
//                    Log.d("TAG", "doInBackground: " + limit);
//
//                    JSONArray productsData = jsonData.getJSONArray("products");
//                    for (int i = 0; i < productsData.length(); i++) {
//                        JSONObject productData = productsData.getJSONObject(i);
//                        ProductModel productModel = new ProductModel();
//                        productModel.setId(productData.getInt("id"));
//                        res.add(productModel);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return res;
//    }

    @Override
    protected void onPostExecute(ArrayList<ProductModel> productModels) {
        super.onPostExecute(productModels);
        if (productModels != null && productModels.size() > 0) {
            productsListener.onLoadProductSuccess(productModels);
        } else {
            productsListener.onLoadProductError("Load Error !");
        }
        tvDemo.setText(productModels.get(0).getTitle());

//        productModels.size();
    }
}
