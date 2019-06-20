package com.satish.bunny.tanga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.satish.bunny.tanga.Activities.Bills.BillListActivity;
import com.satish.bunny.tanga.Adapters.MenuList;
import com.satish.bunny.tanga.FireClasses.BillProducer;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends ListFragment implements View.OnClickListener {
    private List<com.satish.bunny.tanga.FireClasses.Menu> menuList,fragmentMenuList;
    private Button bBill;
    final String Menu = "Menu/";
    private List<BillProducer> BPLIST;
    String bundleItemType="";

    public OrderFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BPLIST = new ArrayList<>();
        menuList = new ArrayList<>();
        fragmentMenuList = new ArrayList<>();
        menuList.clear();
        fragmentMenuList.clear();
        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            bundleItemType = bundle.getString("bundleItemType","");
            if(!menuList.contains(Menu)) {
                menuList = OrderActivity.getMenuList();
                for(com.satish.bunny.tanga.FireClasses.Menu x: menuList)
                {
                    if(x.getItemType().contains(bundleItemType))
                    {
                        fragmentMenuList.add(x);
                    }
                }
                CreateList();
            }
        }


    }

    private void CreateList() {
        MenuList MLAdapter = new MenuList(getActivity(),fragmentMenuList);
        setListAdapter(MLAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_order, container, false);
        bBill = (Button) rootView.findViewById(R.id.bBill);
        bBill.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v==bBill){
            startActivity(new Intent(getActivity(),BillListActivity.class));
        }
    }
}
