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

import com.satish.bunny.tanga.FireClasses.Menu;
import com.satish.bunny.tanga.MethodClasses.DeleteItem;
import com.satish.bunny.tanga.R;

import java.util.List;

/**
 * Created by Bunny on 10/23/2017.
 */

public class PresentItemsList extends ArrayAdapter<Menu>{
    private Activity context;
    private List<Menu> menuList;
    String ItemType;
    String ItemName;
    int flag = 0;
    public PresentItemsList(Activity context,List<Menu> menuList)
    {
        super(context, R.layout.item_list_layout,menuList);
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Menu menu = menuList.get(position);
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.item_list_layout,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tvName);
        ImageView imageViewDelete = (ImageView) listViewItem.findViewById(R.id.ivDelete);
        //Button ButtonDelete = (Button) listViewItem.findViewById(R.id.bDelete);
        textViewName.setText(menu.getItemName());
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemType = menu.getItemType();
                ItemName = menu.getItemName();
                DeleteItem DI = new DeleteItem(context);
                DI.Delete(ItemName,ItemType);
            }
        });
        return listViewItem;
    }
}
