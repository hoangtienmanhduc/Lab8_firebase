package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    EditText edt_Name;
    EditText edt_Pos;
    Button btnadd;
    Button btnedit;
    Button btndelete;
    FirebaseAuth mAuth;

    DatabaseReference dbRf;
    Employee emp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_Name = findViewById(R.id.edt_Name);
        edt_Pos = findViewById(R.id.edt_Position);
        btnadd = findViewById(R.id.btn_Add);
        btnedit = findViewById(R.id.btn_Udate);
        btndelete = findViewById(R.id.btn_Delete);
        btnLogOut = findViewById(R.id.btn_Logout);
        mAuth = FirebaseAuth.getInstance();

        emp = new Employee();
        btnadd.setOnClickListener(view -> {
            dbRf = FirebaseDatabase.getInstance().getReference().child("Employee");
            try {
                if(TextUtils.isEmpty(edt_Name.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please Enter employee Name",Toast.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(edt_Pos.getText().toString()))
                    Toast.makeText(getApplicationContext(),"Please Enter employee Pos",Toast.LENGTH_SHORT).show();
                else{
                    emp.setName(edt_Name.getText().toString().trim());
                    emp.setPosition(edt_Pos.getText().toString().trim());
                    dbRf.push().setValue(emp);
                    Toast.makeText(getApplicationContext(),"Data inserted succesfully",Toast.LENGTH_SHORT).show();
                }
            }catch(NumberFormatException e){
                Toast.makeText(getApplicationContext(),"Invalid contact number",Toast.LENGTH_SHORT).show();
            }
        });
        btnedit.setOnClickListener(view -> {
           DatabaseReference update = FirebaseDatabase.getInstance().getReference().child("Employee");
            update.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("-N1ZJVnpOPL1-HpEQtxv"))
                        try {
                            emp.setName(edt_Name.getText().toString().trim());
                            emp.setPosition(edt_Pos.getText().toString().trim());
                            dbRf = FirebaseDatabase.getInstance().getReference().child("Employee").child("-N1ZJVnpOPL1-HpEQtxv");
                            dbRf.setValue(emp);
                            Toast.makeText(getApplicationContext(), "Data update succesfully", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Invalid contact number", Toast.LENGTH_SHORT).show();
                        }
                    else
                        Toast.makeText(getApplicationContext(), "No sourse to update", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
        btndelete.setOnClickListener(view -> {
            DatabaseReference delete = FirebaseDatabase.getInstance().getReference().child("Employee");
            delete.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("-N1ZJVnpOPL1-HpEQtxv")) {
                        dbRf = FirebaseDatabase.getInstance().getReference().child("Employee").child("-N1ZJVnpOPL1-HpEQtxv");
                        dbRf.removeValue();
                        Toast.makeText(getApplicationContext(), "Data delete succesfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "No sourse data to update", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }
}