package com.servicefusiondemo.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.servicefusiondemo.R;
import com.servicefusiondemo.model.Person;

/**
 * Created by Avdhesh AKhani on 8/16/2016.
 */
public class PersonViewActivity extends AppCompatActivity {
    private TextView tvFName,tvLName,tvDOB,tvZip;
    private Button btnDelete,btnEdit;
    private int position;
    private String key;
    private DatabaseReference personDtabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_view);


        initUI();
        setListener();

        personDtabase = FirebaseDatabase.getInstance().getReference();
        position = getIntent().getIntExtra("Position", -1);
        key = getIntent().getStringExtra("Key");
        findPerson(position);
    }

    private void setListener() {

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePerson();
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonViewActivity.this,PersonEditInsertActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Position", position);
                intent.putExtra("Key",key);
                startActivity(intent);
                finish();
            }
        });
    }

    private void deletePerson() {
        personDtabase.child(key).removeValue();
    }

    private void initUI() {
        tvFName= (TextView) findViewById(R.id.tvFName);
        tvLName= (TextView) findViewById(R.id.tvLName);
        tvDOB= (TextView) findViewById(R.id.tvDOB);
        tvZip= (TextView) findViewById(R.id.tvZip);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
    }

    public void findPerson(int position) {

        personDtabase.child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Person personDetailsModel = dataSnapshot.getValue(Person.class);
                        tvFName.setText("First Name: "+personDetailsModel.getfName());
                        tvLName.setText("Last Name: "+personDetailsModel.getlName());
                        tvDOB.setText("DOB: "+personDetailsModel.getDob());
                        tvZip.setText("Zip: "+personDetailsModel.getZip());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}