package com.satish.bunny.tanga.Activities.Owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.satish.bunny.tanga.Activities.Access.LoginActivity;
import com.satish.bunny.tanga.Activities.Customer.MenuOrderActivity;
import com.satish.bunny.tanga.Adapters.PresentItemsList;
import com.satish.bunny.tanga.FireClasses.ItemType;
import com.satish.bunny.tanga.FireClasses.Menu;
import com.satish.bunny.tanga.R;

import java.util.ArrayList;
import java.util.List;

public class MenuCreatorActivity extends AppCompatActivity implements View.OnClickListener{
    private String ItemType="",ItemName,ItemCost;
    private DatabaseReference databaseMenu,databaseMenuTypes;
    private String MenuPath = "";
    private Button bAdd;
    private Button bNext;
    private EditText etItemName;
    private EditText etItemCost;
    private ListView lvPresentItems;
    private Spinner sItemType;
    private String choose="";
    private List<Menu> menuList;
    private List<String> itemTypesList;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creator);
        bAdd = (Button) findViewById(R.id.bAdd);
        bNext = (Button) findViewById(R.id.bNext);
        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemCost = (EditText) findViewById(R.id.etItemCost);
        sItemType =(Spinner) findViewById(R.id.sItemType);

        final String BasicPath = getIntent().getStringExtra("BasicPath");
        if(BasicPath!=null) {
            itemTypesList = new ArrayList<String>();
                    String ItemsPathRef = BasicPath+"MenuTypes/";
                    databaseMenuTypes = FirebaseDatabase.getInstance().getReference(ItemsPathRef);
                    databaseMenuTypes.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            itemTypesList.clear();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                com.satish.bunny.tanga.FireClasses.ItemType IT = child.getValue(ItemType.class);
                                itemTypesList.add(IT.getItemTypeName());
                            }
                            ArrayAdapter<String> Adapter = new ArrayAdapter<String>(MenuCreatorActivity.this, R.layout.support_simple_spinner_dropdown_item, itemTypesList);
                            sItemType.setAdapter(Adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            lvPresentItems = (ListView) findViewById(R.id.lvPresentItems);
            menuList = new ArrayList<Menu>();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Fetching Menu");

            MenuPath = BasicPath+"Menu/";
                    sItemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String itemTypePath = String.valueOf(parent.getItemAtPosition(position));
                            ItemType = itemTypePath;
                            itemTypePath = itemTypePath + "/";
                            choose = MenuPath + itemTypePath;
                            progressDialog.show();
                            databaseMenu = FirebaseDatabase.getInstance().getReference(choose);
                            databaseMenu.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    menuList.clear();
                                    for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                                        Menu menu = menuSnapshot.getValue(Menu.class);
                                        menuList.add(menu);
                                    }
                                    progressDialog.dismiss();
                                    PresentItemsList adapter = new PresentItemsList(MenuCreatorActivity.this, menuList);
                                    lvPresentItems.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
        bNext.setOnClickListener(this);
        bAdd.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_logout:
                FirebaseAuth firebaseauth;
                firebaseauth = FirebaseAuth.getInstance();
                firebaseauth.signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.privacy_policies:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://hmspt.000webhostapp.com/privacy_policy.html")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void AddNewItemToMenu() {
        ItemName = etItemName.getText().toString().trim();
        ItemCost = etItemCost.getText().toString().trim();
        if(TextUtils.isEmpty(ItemName) || TextUtils.isEmpty(ItemCost)) {
            Toast.makeText(this, "Fields shouldn't be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if(ItemName.length()<2 || ItemCost.length()<2){
            Toast.makeText(this, "Invalid Entries", Toast.LENGTH_LONG).show();
            return;
        }
        ItemName = ItemName.toLowerCase();
        ItemName = CamelCase(ItemName);
        databaseMenu = FirebaseDatabase.getInstance().getReference(choose);
        Menu menu = new Menu(ItemCost,ItemName,ItemType);
        databaseMenu.child(ItemName).setValue(menu);
        Toast.makeText(this, ItemName+ " Added", Toast.LENGTH_SHORT).show();
    }
    private String CamelCase(String wholeString){
        String Words ="",Result="";
        Words = wholeString;
        char firstChar = Words.charAt(0);
        Result = Result+ Character.toUpperCase(firstChar);
        for(int i =1;i<Words.length();i++){
            char currenetChar = Words.charAt(i);
            char previousChar = Words.charAt(i-1);
            if((previousChar == ' ')||(previousChar == '-')||(previousChar == '&'))
            {
                Result = Result+Character.toUpperCase(currenetChar);
            }else{
                Result = Result + currenetChar;
            }
        }
        return Result;
    }
    @Override
    public void onClick(View v) {
        if(v==bAdd)
        {
            AddNewItemToMenu();
        }
        if(v==bNext)
        {
            startActivity(new Intent(MenuCreatorActivity.this,MenuOrderActivity.class));
        }

    }

    public String getChoose() {
        return choose;
    }
}