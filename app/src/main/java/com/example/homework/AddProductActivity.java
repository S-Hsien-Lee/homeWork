package com.example.homework;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helper.DatabaseHelper;


public class AddProductActivity extends AppCompatActivity {

    private EditText etProductId;
    private EditText etProductName;
    private EditText etProductPrice;
    private EditText etProductStock;
    private Button btnAddProduct;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etProductId = findViewById(R.id.et_product_id);
        etProductName = findViewById(R.id.et_product_name);
        etProductPrice = findViewById(R.id.et_product_price);
        etProductStock = findViewById(R.id.et_product_stock);
        btnAddProduct = findViewById(R.id.btn_add_product);
        dbHelper = new DatabaseHelper(getApplicationContext());

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("lsh","lsh近來1");
                String productId = etProductId.getText().toString().trim();
                String productName = etProductName.getText().toString().trim();
                String productPrice = etProductPrice.getText().toString().trim();
                String productStock = etProductStock.getText().toString().trim();

                if (productId.isEmpty() || productName.isEmpty() || productPrice.isEmpty() || productStock.isEmpty()) {
                    Toast.makeText(AddProductActivity.this, "請填寫所有欄位", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.COLUMN_ID, productId);
                    contentValues.put(DatabaseHelper.COLUMN_NAME, productName);
                    contentValues.put(DatabaseHelper.COLUMN_PRICE, Double.parseDouble(productPrice));
                    contentValues.put(DatabaseHelper.COLUMN_STOCK, Integer.parseInt(productStock));

                    long newRowId = db.insert(DatabaseHelper.TABLE_PRODUCTS, null, contentValues);
                    if (newRowId != -1) {
                        etProductId.setText("");
                        etProductName.setText("");
                        etProductPrice.setText("");
                        etProductStock.setText("");
                        Toast.makeText(AddProductActivity.this, "商品已成功新增", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddProductActivity.this, "新增商品失敗，請檢查商品編號是否重複", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            }
        });
    }

}
