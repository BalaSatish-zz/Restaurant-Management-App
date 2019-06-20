package com.satish.bunny.tanga.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.satish.bunny.tanga.FireClasses.PlaceOrderMain;
import com.satish.bunny.tanga.R;

import java.util.List;

/**
 * Created by Bunny on 11/23/2017.
 */

public class OrdersListAdapter extends ArrayAdapter<PlaceOrderMain> {
    private Activity context;
    private List<PlaceOrderMain> OrdersList;
    public OrdersListAdapter(Activity context,List<PlaceOrderMain> OrdersList)
    {
        super(context, R.layout.orders_list_layout,OrdersList);
        this.context = context;
        this.OrdersList = OrdersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PlaceOrderMain orderMain = OrdersList.get(position);
        LayoutInflater inflater = context.getLayoutInflater();
        View ListOrderItem = inflater.inflate(R.layout.orders_list_layout,null,true);
        TextView lvOrderItemName = (TextView) ListOrderItem.findViewById(R.id.tvOrderItemName);
        TextView lvOrderItemQuantity = (TextView) ListOrderItem.findViewById(R.id.tvOrderItemQuantity);

        lvOrderItemName.setText(orderMain.getItemName());
        lvOrderItemQuantity.setText(orderMain.getItemQuantity());
        return ListOrderItem;
    }
}
