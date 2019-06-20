package com.satish.bunny.tanga.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.satish.bunny.tanga.FireClasses.BillProducer;
import com.satish.bunny.tanga.R;

import java.util.List;

/**
 * Created by Bunny on 10/25/2017.
 */

public class BillList extends ArrayAdapter<BillProducer> {
    private Activity context;
    private List<BillProducer> BPList;
    public BillList(Activity context, List<BillProducer> BPList)
    {
        super(context, R.layout.bill_list_layout,BPList);
        this.context = context;
        this.BPList = BPList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BillProducer billProducer = BPList.get(position);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View ListViewItem = layoutInflater.inflate(R.layout.bill_list_layout,null,true);
        TextView lvBillItemName = (TextView) ListViewItem.findViewById(R.id.tvBillItemName);
        TextView lvBillItemCost = (TextView) ListViewItem.findViewById(R.id.tvBillItemCost);

        lvBillItemName.setText(billProducer.getItemName());
        lvBillItemCost.setText(billProducer.getItemCost());
        return ListViewItem;
    }
}
