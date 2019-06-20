package com.satish.bunny.tanga;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.satish.bunny.tanga.Activities.Access.LoginActivity;
import com.satish.bunny.tanga.FireClasses.BillProducer;
import com.satish.bunny.tanga.FireClasses.ItemType;
import com.satish.bunny.tanga.MethodClasses.BillAR;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static DatabaseReference databaseMenu,databaseTypes,databaseNumberOfTables;
    private ListView lvItems;
    private static List<com.satish.bunny.tanga.FireClasses.Menu> menuList;
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
    private List<BillProducer> BPLIST;
    String flag ="";
    String MTpath="";
    static String BasicPath="";
    private static String TableNumber;
    static boolean paymentStatus=false;
    private ProgressDialog progressDialog,tableProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_order);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Menu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        tableProgressDialog = new ProgressDialog(this);
        tableProgressDialog.setCancelable(false);
        tableProgressDialog.setCanceledOnTouchOutside(false);
        ScannedPath = getIntent().getStringExtra("ScannedPath");
        path = getIntent().getStringExtra("WholePath");
        String RestaurantName = getIntent().getStringExtra("RestaurantName");
        setTitle(RestaurantName);
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TableNumber = null;
        paymentStatus = false;

        final String[] NumberOFTablesPath = new String[1];
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Table");
        alert.setMessage("Provide Your Table Number for Serving");
        alert.setCancelable(false);
        final  EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("Table Number:");
        editText.setPadding(5,15,5,15);
        editText.setMaxWidth(20);
        alert.setView(editText);
        alert.setPositiveButton("Submit",new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            NumberOFTablesPath[0] = ScannedPath.replace("/Menu","/Total Tables");
            final String EditTextInput = editText.getText().toString().trim();
            if(!EditTextInput.isEmpty()){
                if(Integer.parseInt(EditTextInput)==0
                    || Integer.parseInt(EditTextInput)>=50) {
                    Toast.makeText(OrderActivity.this, "No Space for Tables", Toast.LENGTH_SHORT).show();
                    recreate();
                    return;
                }
                else{
                    tableProgressDialog.setMessage("Checking...!");
                    tableProgressDialog.show();

                    databaseNumberOfTables = FirebaseDatabase.getInstance().getReference(NumberOFTablesPath[0]);
                    databaseNumberOfTables.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String TableLimit = dataSnapshot.getValue().toString().trim();
                            if(!TableLimit.isEmpty()&&TableLimit!=null) {
                                if(Integer.parseInt(TableLimit)<Integer.parseInt(EditTextInput)){
                                    tableProgressDialog.dismiss();
                                    Toast.makeText(OrderActivity.this, "Table Doesn't Exit", Toast.LENGTH_SHORT).show();
                                    recreate();
                                    return;
                                }
                                else{
                                    String UnavailableTablesPath = ScannedPath.replace("/Menu","/Orders");
                                    DatabaseReference UnavailableTables =
                                            FirebaseDatabase.getInstance().getReference(UnavailableTablesPath);
                                    UnavailableTables.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            tableProgressDialog.dismiss();
                                            for( DataSnapshot child : dataSnapshot.getChildren()){
                                                String ChildName = child.getKey().toString().trim();
                                                if(ChildName.contains(EditTextInput)){
                                                    Toast.makeText(OrderActivity.this, "Table Occupied by SomeOne", Toast.LENGTH_SHORT).show();
                                                    recreate();
                                                }
                                            }
                                            TableNumber= "Table-"+EditTextInput;

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    //TableNumber = "Table-"+editText.getText().toString().trim();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                }
                paymentStatus = true;

            }
        });

        lvItems = (ListView) findViewById(R.id.lvItems);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BPLIST = new ArrayList<>();
        menuList = new ArrayList<>();
        ItemTypes = new ArrayList<String>();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(100);

//        lvItems = (ListView) getListView();

       // Toast.makeText(OrderActivity.this, path, Toast.LENGTH_SHORT).show();
        if(path != null)
        {
            if(path.contains("Tanga")){
                Menu(path);
            }
        }

        if (ScannedPath != null) {
            if (ScannedPath.contains("Tanga")) {
                path = ScannedPath.replace("Menu/", "");
                paymentStatus=true;
                alert.show();
                Menu(path);
            }
        }
    }
    private void counter() {
        Count--;
        if(Count==0){
        }
        if(Count< 0)
        {
            recreate();
            Toast.makeText(OrderActivity.this, ""+"Menu Updated", Toast.LENGTH_SHORT).show();
            return;
        }
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
    private void  Menu(String path){
        progressDialog.show();
        BasicPath = path;
        MTpath = path+"MenuTypes/";
        path=path+"Menu/";

        databaseTypes = FirebaseDatabase.getInstance().getReference(MTpath);
        menuList.clear();
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
                    final String Choose = finalPath +ItemType;
                    databaseMenu = FirebaseDatabase.getInstance().getReference(Choose);
                    databaseMenu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            counter();
                            com.satish.bunny.tanga.FireClasses.Menu m =
                                    new com.satish.bunny.tanga.FireClasses.Menu("","",ItemType);
                            menuList.add(m);
                            for (DataSnapshot menuSnapshot : dataSnapshot.getChildren()) {
                                com.satish.bunny.tanga.FireClasses.Menu menu =
                                        menuSnapshot.getValue(com.satish.bunny.tanga.FireClasses.Menu.class);
                                menuList.add(menu);
                            }
                            //MenuList adapter = new MenuList(OrderActivity.this, menuList);
                            //lvItems.setAdapter(adapter);
                            if(menuList!=null && Count == 0) {
                                setupViewPager(viewPager);
                                tabLayout.setupWithViewPager(viewPager);
                                progressDialog.dismiss();
                            }
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter;
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(com.satish.bunny.tanga.FireClasses.Menu x : menuList){
            if(x.getItemCost()==""){
                flag = x.getItemType();
                OrderFragment orderFragment = new OrderFragment();

                Bundle bundle = new Bundle();
                bundle.putString("bundleItemType",flag);

                orderFragment.setArguments(bundle);
                adapter.addFragment(orderFragment,x.getItemType());
            }
                viewPager.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BillAR.clearBPList();
        finish();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            try{super.finishUpdate(container);}
            catch (NullPointerException nullpointerexception){
                Context context = OrderActivity.this;
                Intent mStartActivity = new Intent(context, OrderActivity.class);
                int mPendingIntentId = 123456;
                Toast.makeText(context, "Updating... Menu.", Toast.LENGTH_SHORT).show();
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, mPendingIntent);
                System.exit(0);
            }
        }

        @Override
        public Fragment getItem(int position){
            return mFragmentList.get(position);
        }
        @Override
        public  int getCount(){
            return mFragmentList.size();
        }
        public  void addFragment(OrderFragment fragment, String title){

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }

    public static List<com.satish.bunny.tanga.FireClasses.Menu> getMenuList() {
        return menuList;
    }

    public static String getTableNumber() {
        return TableNumber;
    }

    public static String getBasicPath() {
        return BasicPath;
    }

    public static boolean isPaymentStatus() {
        return paymentStatus;
    }
}
