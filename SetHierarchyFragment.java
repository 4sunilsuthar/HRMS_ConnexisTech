package com.lms.admin.lms;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class SetHierarchyFragment extends Fragment {

    AutoCompleteTextView autoTVNames;
    String[] SrNames = {"Android", "Xamrin", "IOS", "SQL", "JDBC", "Java"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_hierarchy, container, false);
        Log.i("abc", "in onCreateView method");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("abc", "in onCreate method");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("abc", "in onAttach method");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autoTVNames = getView().findViewById(R.id.autoTVNameXML);
        Log.i("abc", "before array adapter");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, SrNames);
        autoTVNames.setAdapter(arrayAdapter);
        autoTVNames.setThreshold(1);
        Log.i("abc", "After array adapter");
    }
}
