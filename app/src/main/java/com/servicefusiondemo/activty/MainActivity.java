package com.servicefusiondemo.activty;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.servicefusiondemo.R;
import com.servicefusiondemo.helper.PersonDetailsAdapter;
import com.servicefusiondemo.model.Person;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "Main Activity";
    private DatabaseReference personDatabase;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private ListView lvPerson;
    private static ArrayList<Person> arrayListPerson = new ArrayList<>();
    private ArrayList<String> keysArray;
    private PersonDetailsAdapter personDetailsAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUI();
        progressBar.setVisibility(View.VISIBLE);

        setListeners();
        personDatabase = FirebaseDatabase.getInstance().getReference();
        keysArray = new ArrayList<>();



        if (isDeviceOnline(this)) {
            personDatabase = FirebaseDatabase.getInstance().getReference();
            loadPersonData();
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, " No internet Connection ", Toast.LENGTH_SHORT).show();
        }

        checkArrayList();


        personDetailsAdapter = new PersonDetailsAdapter(MainActivity.this, arrayListPerson);
        lvPerson.setAdapter(personDetailsAdapter);

    }

    private void checkArrayList() {
        handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(5000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        if (arrayListPerson.size() == 0) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    private void setListeners() {
        lvPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PersonViewActivity.class);
                intent.putExtra("Position", position);
                intent.putExtra("Key", keysArray.get(position));
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonEditInsertActivity.class);
                intent.putExtra("Position", -1);
                startActivity(intent);
            }
        });

    }

    private void initUI() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lvPerson = (ListView) findViewById(R.id.PersonList);
    }

    private void loadPersonData() {

        Query queryRef = personDatabase.orderByChild("Persons");
        queryRef.addChildEventListener(childEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.e(TAG, "Added");
            Person person = dataSnapshot.getValue(Person.class);
            arrayListPerson.add(person);
            keysArray.add(dataSnapshot.getKey());
            Log.e(TAG + "Key", dataSnapshot.getKey());
            updateView();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e(TAG, "Changed");
            String newKey = dataSnapshot.getKey();
            int newIndex = keysArray.indexOf(newKey);
            Person person = dataSnapshot.getValue(Person.class);
            arrayListPerson.set(newIndex, person);
            updateView();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.e(TAG, "Removed");
            String deletedKey = dataSnapshot.getKey();
            int removedIndex = keysArray.indexOf(deletedKey);
            keysArray.remove(removedIndex);
            arrayListPerson.remove(removedIndex);
            updateView();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.e(TAG, "Moved");
            updateView();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Cancelled");
            updateView();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        arrayListPerson.clear();
        personDatabase.removeEventListener(childEventListener);
    }

    public static boolean isDeviceOnline(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isOnline = (networkInfo != null && networkInfo.isConnected());

        return isOnline;
    }

    private void updateView() {
        personDetailsAdapter.notifyDataSetChanged();
        lvPerson.invalidate();
        progressBar.setVisibility(View.GONE);
        lvPerson.setVisibility(View.VISIBLE);
    }
}
