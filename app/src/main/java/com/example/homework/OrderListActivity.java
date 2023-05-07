package com.example.homework;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adapter.OrderListAdapter;
import com.example.common.Order;
import com.example.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderListAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        orderList = new ArrayList<>();
        adapter = new OrderListAdapter(orderList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadOrderList();
    }


    private void loadOrderList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ORDER_ID,
                DatabaseHelper.COLUMN_ORDER_NAME,
                DatabaseHelper.COLUMN_ORDER_PRICE,
                DatabaseHelper.COLUMN_ORDER_QUANTITY,
                DatabaseHelper.COLUMN_ORDER_TOTAL
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_ORDERS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        orderList = new ArrayList<>();
        double totalAmount = 0;

        while (cursor.moveToNext()) {
            String orderId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID));
            String orderName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_NAME));
            double orderPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PRICE));
            int orderQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_QUANTITY));
            double orderTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL));
            Log.d("lsh", String.valueOf(orderPrice)+"  "+orderName+"  "+orderQuantity+"  "+orderTotal);

            orderList.add(new Order(orderId, orderName, orderPrice, orderQuantity, orderTotal));
            totalAmount += orderTotal;
        }

        cursor.close();
        db.close();

        adapter = new OrderListAdapter(orderList);
        recyclerView.setAdapter(adapter);
    }


}

