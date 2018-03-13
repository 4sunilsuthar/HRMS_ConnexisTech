package com.lms.admin.lms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 13-03-2018.
 */

public class SpinnerCustomAdapter extends BaseAdapter {
    public String empGlobalId = "";
    Context context;
    private List<SpinnerEmpDetails> empNamesList;

    SpinnerCustomAdapter(Context applicationContext, List<SpinnerEmpDetails> empNamesList) {
        this.context = applicationContext;
        this.empNamesList = empNamesList;
    }


    @Override
    public int getCount() {
        return empNamesList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.spinner_item_custom, null);
        ImageView profileIcon = convertView.findViewById(R.id.imageView_profile);
        TextView name = convertView.findViewById(R.id.textView_emp_name);
        TextView empId = convertView.findViewById(R.id.textView_emp_id);
//        alternate code for showing empProfile Image
//        Log.e("myTags","in getView function!!!@@@@");
        //profileIcon.setImageResource(profileUrls[position]);
        SpinnerEmpDetails spinnerEmpDetails = empNamesList.get(position);
        Picasso.with(context).load(spinnerEmpDetails.getProfileUrl()).into(profileIcon);
        name.setText(spinnerEmpDetails.getName());
        empId.setText(spinnerEmpDetails.getEmpId());
        Log.e("myTAZG", "seleccted emp's ID is : " + spinnerEmpDetails.getEmpId());
        empGlobalId = spinnerEmpDetails.getEmpId();
        return convertView;
    }

}
