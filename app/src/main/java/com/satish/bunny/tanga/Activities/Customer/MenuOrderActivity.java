package com.satish.bunny.tanga.Activities.Customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.satish.bunny.tanga.Activities.Access.LoginActivity;
import com.satish.bunny.tanga.Activities.Bills.BillListActivity;
import com.satish.bunny.tanga.Adapters.MenuList;
import com.satish.bunny.tanga.FireClasses.BillProducer;
import com.satish.bunny.tanga.FireClasses.ItemType;
import com.satish.bunny.tanga.MethodClasses.BillAR;
import com.satish.bunny.tanga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuOrderActivity extends AppCompatActivity implements View.OnClickListener{
    private static DatabaseReference databaseMenu,databaseTypes,databaseUsers;
    private ListView lvItems;
    private List<com.satish.bunny.tanga.FireClasses.Menu> menuList;
    private Button bBill;
    private Spinner sItemType;
    //private ArrayAdapter<CharSequence> adapter;
    static int Count;
    int Length;
    private FirebaseUser firebaseUser;
    final int[] i = {0};
    final String Menu = "Menu/";
    String UserID="";
    private ArrayList<String> ItemTypes;
    String path = "";
    String ScannedPath = "";
    String MTpath;
    private List<BillProducer> BPLIST;
    static int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);
        Count = Length;
        ScannedPath = getIntent().getStringExtra("ScannedPath");
        path = getIntent().getStringExtra("WholePath");
            if (path == null && ScannedPath == null) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    UserID = firebaseUser.getUid();
                    databaseUsers = FirebaseDatabase.getInstance().getReference("Owners/" + UserID + "/Path");
                    databaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            path = dataSnapshot.getValue().toString();
                            path = path.replace("/Menu/", "");
                           // Toast.makeText(MenuOrderActivity.this, path, Toast.LENGTH_SHORT).show();
                            MTpath = path + "/MenuTypes";
                            path = path + "/Menu/";

                            ItemTypes = new ArrayList<String>();
                            databaseTypes = FirebaseDatabase.getInstance().getReference(MTpath);
                            databaseTypes.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ItemTypes.clear();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        ItemType IT = child.getValue(ItemType.class);
                                        ItemTypes.add(IT.getItemTypeName());
                                    }
                                    Count = ItemTypes.size();
                                    for (String x : ItemTypes) {
                                        final String ItemType = x;
                                        String Choose = path + ItemType;
                                        databaseMenu = FirebaseDatabase.getInstance().getReference(Choose);
                                        databaseMenu.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                counter();
                                                com.satish.bunny.tanga.FireClasses.Menu m = new com.satish.bunny.tanga.FireClasses.Menu("", "", ItemType);
                                                menuList.add(m);
                                                for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                                                    com.satish.bunny.tanga.FireClasses.Menu menu = menuSnapshot.getValue(com.satish.bunny.tanga.FireClasses.Menu.class);
                                                    menuList.add(menu);
                                                }
                                                MenuList adapter = new MenuList(MenuOrderActivity.this, menuList);
                                                lvItems.setAdapter(adapter);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            BPLIST = new ArrayList<>();
            menuList = new ArrayList<>();
            lvItems = (ListView) findViewById(R.id.lvItems);
            bBill = (Button) findViewById(R.id.bBill);
            bBill.setOnClickListener(this);
        if (path != null || ScannedPath != null) {
            if (path != null) {
                Menu(path);
            }
            if (ScannedPath != null) {
                if (ScannedPath.contains("Tanga")) {
                    path = ScannedPath.replace("/Menu/", "");
                    //Toast.makeText(MenuOrderActivity.this, path, Toast.LENGTH_SHORT).show();
                    // MTpath = path+"/MenuTypes";
                    // path=path+"/Menu/";
                    Menu(path);
                }
            }
        }
    }

    private void  Menu(String path){
        MTpath = path+"/MenuTypes";
        path=path+"/Menu/";

        ItemTypes = new ArrayList<String>();
        databaseTypes = FirebaseDatabase.getInstance().getReference(MTpath);
        final String finalPath = path;
        databaseTypes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ItemTypes.clear();
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    ItemType IT = child.getValue(ItemType.class);
                    ItemTypes.add(IT.getItemTypeName());
                }
                Count = ItemTypes.size();
                for(String x : ItemTypes)
                {
                    final String ItemType = x;
                    String Choose = finalPath +ItemType;
                    databaseMenu = FirebaseDatabase.getInstance().getReference(Choose);
                    databaseMenu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            counter();
                            com.satish.bunny.tanga.FireClasses.Menu m = new com.satish.bunny.tanga.FireClasses.Menu("","",ItemType);
                            menuList.add(m);
                            for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                                com.satish.bunny.tanga.FireClasses.Menu menu = menuSnapshot.getValue(com.satish.bunny.tanga.FireClasses.Menu.class);
                                menuList.add(menu);
                            }
                            MenuList adapter = new MenuList(MenuOrderActivity.this, menuList);
                            lvItems.setAdapter(adapter);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.privacy_policies:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://hmspt.000webhostapp.com/privacy_policy.html")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BPLIST.clear();
        BillAR.setBPList(BPLIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void counter() {
        Count--;
        if(Count< 0)
        {
            menuList.clear();
            Toast.makeText(this, "Menu Updated", Toast.LENGTH_SHORT).show();
            recreate();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        if(v==bBill){
                startActivity(new Intent(this,BillListActivity.class));
            }
    }
}
