package com.satish.bunny.tanga.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.satish.bunny.tanga.MethodClasses.DeleteItem;
import com.satish.bunny.tanga.FireClasses.ItemType;
import com.satish.bunny.tanga.R;

import java.util.List;

/**
 * Created by Bunny on 11/1/2017.
 */

public class TypesList extends ArrayAdapter<ItemType>{

    private Activity context;
    private List<ItemType> itemTypeList;

    public TypesList(Activity context, List<ItemType> itemTypeList){
        super(context, R.layout.item_list_layout,itemTypeList);
        this.context = context;
        this.itemTypeList = itemTypeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ItemType itemType = itemTypeList.get(position);
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewTypeName = inflater.inflate(R.layout.item_list_layout,null,true);
        TextView textViewTypeName = (TextView) listViewTypeName.findViewById(R.id.tvName);
        ImageView imageViewDelete = (ImageView) listViewTypeName.findViewById(R.id.ivDelete);
        String ItemTypeName="";
        ItemTypeName = itemType.getItemTypeName();
        textViewTypeName.setText(ItemTypeName);
        final String finalItemTypeName = ItemTypeName;
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteItem DI = new DeleteItem(context);
                DI.Delete(finalItemTypeName);
            }
        });
        return listViewTypeName;
    }
}
