package com.example.homework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.adapter.ProductListAdapter;
import com.example.common.Product;
import com.example.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderActivity extends AppCompatActivity {

    private RecyclerView rvProductList;
    private Button btnCreateOrder;
    private DatabaseHelper dbHelper;

    private ProductListAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dbHelper = new DatabaseHelper(this);
        productList = new ArrayList<>();

        rvProductList = findViewById(R.id.rv_product_list);
        btnCreateOrder = findViewById(R.id.btn_create_order);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setItemAnimator(null);
        adapter = new ProductListAdapter(CreateOrderActivity.this,productList);
        rvProductList.setAdapter(adapter);



        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProductList();
    }

    private void createOrder() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 更新商品庫存數量並新增訂單資料
        for (int i = 0; i < adapter.getItemCount(); i++) {
            ProductListAdapter.ViewHolder viewHolder = (ProductListAdapter.ViewHolder) rvProductList.findViewHolderForAdapterPosition(i);
            if (viewHolder != null && viewHolder.isProductSelected()) {
                int quantity = viewHolder.getProductQuantity();
                Product product = productList.get(i);

                // 在資料庫中新增訂單資料
                ContentValues orderValues = new ContentValues();
                orderValues.put(DatabaseHelper.COLUMN_ORDER_NAME, product.getProductName());
                orderValues.put(DatabaseHelper.COLUMN_ORDER_PRICE, product.getProductPrice());
                orderValues.put(DatabaseHelper.COLUMN_ORDER_QUANTITY, quantity);
                orderValues.put(DatabaseHelper.COLUMN_ORDER_TOTAL, product.getProductPrice() * quantity);
                long newRowId = db.insert(DatabaseHelper.TABLE_ORDERS, null, orderValues);

                if (newRowId != -1) {
                    // 更新商品庫存
                    ContentValues productValues = new ContentValues();
                    productValues.put(DatabaseHelper.COLUMN_STOCK, product.getProductStock() - quantity);

                    String selection = DatabaseHelper.COLUMN_ID + " = ?";
                    String[] selectionArgs = {product.getProductId()};

                    db.update(DatabaseHelper.TABLE_PRODUCTS, productValues, selection, selectionArgs);
                } else {
                    Toast.makeText(CreateOrderActivity.this, "建立訂單失敗，請重試", Toast.LENGTH_SHORT).show();
                }
            }
        }

        Toast.makeText(CreateOrderActivity.this, "訂單已成功建立", Toast.LENGTH_SHORT).show();

        // 重新載入商品清單
        loadProductList();
        db.close();
    }





    private void loadProductList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_PRICE,
                DatabaseHelper.COLUMN_STOCK
        };

        String selection = DatabaseHelper.COLUMN_STOCK + " > ?";
        String[] selectionArgs = { "0" };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_PRODUCTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        productList.clear();
        int itemCount = adapter.getItemCount();
        adapter.notifyItemRangeRemoved(0, itemCount);

        while (cursor.moveToNext()) {
            String productId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            String productName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE));
            int productStock = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK));

            productList.add(new Product(productId, productName, productPrice, productStock));
            adapter.notifyItemInserted(productList.size() - 1);
        }
        cursor.close();
        db.close();
    }




}

