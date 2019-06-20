package com.satish.bunny.tanga.Activities.Details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.satish.bunny.tanga.Activities.Access.LoginActivity;
import com.satish.bunny.tanga.Activities.Customer.MenuOrderActivity;
import com.satish.bunny.tanga.FireClasses.Form;
import com.satish.bunny.tanga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private TextView tvUserEmail;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etNickName;
    private EditText etMobileNumber;
    private Button bLogout;
    private Button bSubmit;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);
        tvUserEmail.setText("Welcome "+user.getEmail());
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etNickName = (EditText) findViewById(R.id.etNickName);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        bLogout = (Button) findViewById(R.id.bLogout);
        bLogout.setOnClickListener(this);
        bSubmit = (Button) findViewById(R.id.bSubmit);
        bSubmit.setOnClickListener(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
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

    private void submitForm(){
        String FirstName = etFirstName.getText().toString().trim();
        String LastName = etLastName.getText().toString().trim();
        String NickName = etNickName.getText().toString().trim();
        String MobileNumber = etMobileNumber.getText().toString().trim();

        FirstName = FirstName.toLowerCase();
        LastName =LastName.toLowerCase();
        NickName = LastName.toLowerCase();
        FirstName = CamelCase(FirstName);
        LastName = CamelCase(LastName);
        NickName = CamelCase(NickName);
        String UserType = "Customer";
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(TextUtils.isEmpty(FirstName) || TextUtils.isEmpty(LastName) || TextUtils.isEmpty(NickName)|| TextUtils.isEmpty(MobileNumber))
        {
            Toast.makeText(this, "Fields cannot be empty...!", Toast.LENGTH_LONG).show();
            return;
        }
        if( FirstName.length()<3 || LastName.length()<3 || NickName.length()<2)
        {
            Toast.makeText(this, "Invalid details.", Toast.LENGTH_LONG).show();
            return;
        }
        if(MobileNumber.length()<10)
        {
            Toast.makeText(this, "Invalid mobile number.", Toast.LENGTH_LONG).show();
            return;
        }
        Form form = new Form(FirstName, LastName, NickName, MobileNumber, UserType);
        databaseReference.child(user.getUid()).setValue(form);
        Toast.makeText(DetailsActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),MenuOrderActivity.class);
        finish();
        startActivity(i);
    }
    private String CamelCase(String wholeString){
        String Words ="",Result="";
        Words = wholeString;
        char firstChar = Words.charAt(0);
        Result = Result+ Character.toUpperCase(firstChar);
        for(int i =1;i<Words.length();i++){
            char currentChar = Words.charAt(i);
            char previousChar = Words.charAt(i-1);
            if(previousChar == ' ')
            {
                Result = Result+Character.toUpperCase(currentChar);
            }else{
                Result = Result + currentChar;
            }
        }
        return Result;
    }
    @Override
    public void onClick(View v) {
        if(v == bLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(DetailsActivity.this,LoginActivity.class));
        }
        if(v == bSubmit){
            submitForm();
        }
    }
}
