package com.lms.admin.lms;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 29-03-2018.
 */

public class PayrollStructureAdapter extends ArrayAdapter {
    private static final String TAG = "PayrollStructureAdapter";
    private List<PayrollStructure> mPayrollStructureList = new ArrayList<>();

    PayrollStructureAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public void add(@Nullable PayrollStructure payrollStructure) {
        super.add(payrollStructure);
        mPayrollStructureList.add(payrollStructure);
    }

    @Override
    public int getCount() {
        return mPayrollStructureList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mPayrollStructureList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        PayrollStructureHolder holder;
        row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.payslip_format_custom, parent, false);
            holder = new PayrollStructureHolder();
            holder.tvTitle = row.findViewById(R.id.tv_payslip_structure_title);
            holder.tvValue = row.findViewById(R.id.tv_payslip_structure_value);
            row.setTag(holder);
        } else {
            holder = (PayrollStructureHolder) row.getTag();
        }
        PayrollStructure structure = (PayrollStructure) this.getItem(position);
        holder.tvTitle.setText(structure.getTitle());
        holder.tvValue.setText(structure.getVlaue());
        Log.e(TAG, "Value of " + holder.tvTitle.getText().toString() + " is: " + holder.tvValue.getText().toString());
        return row;
    }

    //ViewHolder class
    static class PayrollStructureHolder {
        TextView tvTitle;
        EditText tvValue;
    }
}
