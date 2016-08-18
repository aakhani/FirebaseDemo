package com.servicefusiondemo.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicefusiondemo.R;
import com.servicefusiondemo.activty.PersonEditInsertActivity;
import com.servicefusiondemo.model.Person;

import java.util.ArrayList;

/**
 * Created by Avdhesh AKhani on 8/16/2016.
 */
public class PersonDetailsAdapter extends BaseAdapter {
    private ArrayList<Person> arrayListPerson;
    private Context context;
    private LayoutInflater inflater;

    public PersonDetailsAdapter(Context context, ArrayList<Person> arrayListPerson) {
        this.context = context;
        this.arrayListPerson = arrayListPerson;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayListPerson.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListPerson.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.list_person, null);
            holder = new Holder();
            holder.tvFName = (TextView) v.findViewById(R.id.tvFName);
            holder.tvLName = (TextView) v.findViewById(R.id.tvLName);
            holder.tvDOB = (TextView) v.findViewById(R.id.tvDOB);
            holder.tvZip= (TextView) v.findViewById(R.id.tvZip);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }

        holder.tvFName.setText("First Name: "+arrayListPerson.get(position).getfName());
        holder.tvLName.setText("Last Name: "+arrayListPerson.get(position).getlName());
        holder.tvDOB.setText("DOB: "+arrayListPerson.get(position).getDob());
        holder.tvZip.setText("Zip: "+arrayListPerson.get(position).getZip());

        return v;
    }

    class Holder {
        TextView tvFName,tvLName,tvDOB,tvZip;
    }

    public static void ShowConfirmDialog(Context context, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this entry?")
                .setCancelable(true)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // MainActivity.getInstance().deletePerson(position);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
