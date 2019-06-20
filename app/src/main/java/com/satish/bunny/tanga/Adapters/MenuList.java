package com.satish.bunny.tanga.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.satish.bunny.tanga.MethodClasses.BillAR;
import com.satish.bunny.tanga.FireClasses.Menu;
import com.satish.bunny.tanga.R;

import java.util.List;

import static com.satish.bunny.tanga.R.id.bDecrease;
import static com.satish.bunny.tanga.R.id.bIncrease;

/**
 * Created by Bunny on 9/17/2017.
 */

public class MenuList extends ArrayAdapter<Menu> {
    private static int ItemTotal = 0;
    private Activity context;
    private List<Menu> menuList;


    public MenuList(Activity context, List<Menu> menuList){
        super(context, R.layout.list_layout,menuList);
        this.context = context;
        this.menuList = menuList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Menu menu = menuList.get(position);
        final int[] Quantity = {0};
        LayoutInflater inflater = context.getLayoutInflater();
        LayoutInflater TypeInflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        View ListViewItemType = TypeInflater.inflate(R.layout.item_list_layout_seperater, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tvName);
        TextView textViewCost = (TextView) listViewItem.findViewById(R.id.tvCost);
        Button buttonIncrease = (Button) listViewItem.findViewById(bIncrease);
        Button buttonDecrease = (Button) listViewItem.findViewById(bDecrease);
        if(menu.getItemName().isEmpty())
        {
            TextView tvItemType = (TextView) ListViewItemType.findViewById(R.id.tvItemType);
            tvItemType.setText(menu.getItemType());
            return ListViewItemType;
        }
        final TextView textViewQuantity =(TextView) listViewItem.findViewById(R.id.tvQuantity);
        final String[] ItemName = new String[1];
        final String[] ItemCost = new String[1];
        final String[] SItemTotal = {""};
        textViewQuantity.setText(""+ Quantity[0]);
        ItemName[0] = textViewName.getText().toString();
        ItemCost[0] = textViewCost.getText().toString();
        textViewName.setText(menu.getItemName());
        textViewCost.setText(menu.getItemCost());
        //................................................................................................
        String sItemCost = menu.getItemCost();
        final String sItemName = menu.getItemName();

        final int IntItemCost = Integer.parseInt(sItemCost);

        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIncreaser();
            }

            private int tvIncreaser() {
                Quantity[0]++;
                String Q = String.valueOf(Quantity[0]);
                ItemTotal = (IntItemCost * Quantity[0]);
                textViewQuantity.setText(Q);
                BillAR billAR = new BillAR();
                SItemTotal[0] = String.valueOf(ItemTotal);
                billAR.Adder(sItemName, SItemTotal[0], Q);
                return Quantity[0];
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDecreaser();
            }

            private int tvDecreaser() {
                if(Quantity[0] !=0){
                    Quantity[0]--;
                    String Q = String.valueOf(Quantity[0]);
                    ItemTotal = (IntItemCost * Quantity[0]);
                    textViewQuantity.setText(Q);
                    BillAR billAR = new BillAR();
                    SItemTotal[0] = String.valueOf(ItemTotal);
                    billAR.Adder(sItemName, SItemTotal[0], String.valueOf(Quantity[0]));
                    return Quantity[0];
                }
                else{
                    Toast.makeText(getContext(), "Order, Can't be Negative.", Toast.LENGTH_SHORT).show();
                }
                textViewQuantity.setText(""+ Quantity[0]);
                return Quantity[0];
            }
        });
        return listViewItem;
    }
    public static void setItemTotal(int itemTotal) {
        ItemTotal = itemTotal;
    }

    public static int getItemTotal() { return ItemTotal;}
}