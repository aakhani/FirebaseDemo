package com.servicefusiondemo.activty;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.servicefusiondemo.R;
import com.servicefusiondemo.model.Person;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Avdhesh AKhani on 8/16/2016.
 */
public class PersonEditInsertActivity extends AppCompatActivity {

    Button bOK,bCancel;
    Person person;
    int position;
    EditText edtFName,edtLName,edtDate,edtZip;
    CoordinatorLayout cl;
    private DatabaseReference personDtabase;
    private String key;

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_insert_edit);

        initUI();
        setListener();

        position = getIntent().getIntExtra("Position", -1);
        key = getIntent().getStringExtra("Key");
        personDtabase =   FirebaseDatabase.getInstance().getReference();

        if(position != -1) {
            getSupportActionBar().setTitle("Edit Entry");
            findPerson(position);
            person = new Person();
        }
        else {
            getSupportActionBar().setTitle("Add Entry");
            person = null;
        }


    }

    private void setListener() {
        bOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtFName.getText().toString().trim().equals("") || edtLName.getText().toString().trim().equals("") ||
                        edtDate.getText().toString().trim().equals("") || edtZip.getText().toString().trim().equals("")) {
                    final Snackbar snackBar = Snackbar.make(cl, "Please enter all the fields.", Snackbar.LENGTH_LONG);
                    snackBar.setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                    return;
                }

                if (edtZip.getText().toString().trim().length() != 5){
                    final Snackbar snackBar = Snackbar.make(cl, "Please enter 5 digits for Zip.", Snackbar.LENGTH_LONG);
                    snackBar.setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();
                    return;
                }

                Person p = new Person();
                p.setfName(edtFName.getText().toString());
                p.setlName(edtLName.getText().toString());
                p.setDob(edtDate.getText().toString());
                p.setZip(edtZip.getText().toString());
                if (person == null)
                    addPerson(p);
                else
                    updatePersonDetails(p);
                finish();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });
    }

    private void openDatePickerDialog() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String mm = checkDigit(monthOfYear + 1);
                        String dd = checkDigit(dayOfMonth);

                        String sDate = mm+ "/" + dd + "/" + year; // suppose you create this type of date as string then

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                        Date date = null;
                        try {
                            date = sdf.parse(sDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar c = Calendar.getInstance();
                        if(c.getTime().compareTo(date)>0){
                            edtDate.setText(mm+ "/" + dd + "/" + year);
                        }else {
                            Toast.makeText(PersonEditInsertActivity.this,"Date should not be after todays date",Toast.LENGTH_SHORT).show();
                        }


                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void initUI() {
        cl = (CoordinatorLayout) findViewById(R.id.cdlayout);

        edtFName = (EditText) findViewById(R.id.edtFName);
        edtLName = (EditText) findViewById(R.id.edtLName);
        edtDate = (EditText) findViewById(R.id.edtDob);
        edtZip = (EditText) findViewById(R.id.edtZip);

        bOK = (Button) findViewById(R.id.bOk);
        bCancel = (Button) findViewById(R.id.bCancel);

    }

    private void addPerson(Person p) {

        Person person = new Person();
        person.setfName(p.getfName());
        person.setlName(p.getlName());
        person.setDob(p.getDob());
        person.setZip(p.getZip());

        String key =  personDtabase.child("Persons").push().getKey();
        Map<String, Object> postValues = person.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        personDtabase.updateChildren(childUpdates);
    }

    private void updatePersonDetails(Person p) {

        Person person = new Person();
        person.setfName(p.getfName());
        person.setlName(p.getlName());
        person.setDob(p.getDob());
        person.setZip(p.getZip());

        personDtabase.child(key).setValue(p);


    }

    public void findPerson(int position) {
        personDtabase.child(key).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Person person = dataSnapshot.getValue(Person.class);
                        edtFName.setText(person.getfName());
                        edtLName.setText(person.getlName());
                        edtDate.setText(person.getDob());
                        edtZip.setText(person.getZip());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private String checkDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }
}