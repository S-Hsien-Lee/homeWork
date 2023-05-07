package com.example.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.Product;
import com.example.homework.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_item, parent, false);
        return new ViewHolder(view, context, productList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvProductPrice.setText(String.valueOf(product.getProductPrice()));
        holder.tvProductStock.setText(String.valueOf(product.getProductStock()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductStock;
        private CheckBox cbSelectProduct;
        private EditText etProductQuantity;


        public ViewHolder(@NonNull View itemView,Context context,List<Product> productList) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductStock = itemView.findViewById(R.id.tv_product_stock);
            cbSelectProduct = itemView.findViewById(R.id.cb_select_product);
            etProductQuantity = itemView.findViewById(R.id.et_product_quantity);

            etProductQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return;
                    }

                    int maxStock = productList.get(position).getProductStock();
                    try {
                        int inputQuantity = Integer.parseInt(s.toString());

                        if (inputQuantity > maxStock) {
                            etProductQuantity.setText(String.valueOf(maxStock));
                            etProductQuantity.setSelection(etProductQuantity.getText().length());
                            Toast.makeText(context, "購買數量不能大於庫存數量", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Log.d("lsh",e.getMessage());
                    }
                }
            });

        }


        public boolean isProductSelected() {
            return cbSelectProduct.isChecked();
        }

        public int getProductQuantity() {
            try {
                return Integer.parseInt(etProductQuantity.getText().toString());
            } catch (NumberFormatException e) {
                return 0;
            }
        }


    }
}
