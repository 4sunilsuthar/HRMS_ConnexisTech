package com.lms.admin.lms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class EditUserProfileFragment extends Fragment {
    private static final String TAG = "EditUserProfileFragment";
    private EditText edEmpName, edEmpGender, edEmpPhone, edEmpAddress, edEmpTopSkills, edEmpQualification;
    private Button btnUpdateProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get all the values here
        btnUpdateProfile = getView().findViewById(R.id.btn_save_profile);
        edEmpName = getView().findViewById(R.id.ed_edit_emp_name);
        edEmpGender = getView().findViewById(R.id.ed_edit_gender);
        edEmpPhone = getView().findViewById(R.id.ed_edit_phone);
        edEmpAddress = getView().findViewById(R.id.ed_edit_address);
        edEmpTopSkills = getView().findViewById(R.id.ed_edit_top_skills);
        edEmpQualification = getView().findViewById(R.id.ed_edit_qualification);
//        tvEmpEmail = getView().findViewById(R.id.ed_edit_email);
//        imgCoverArt = getView().findViewById(R.id.img_cover_art);
//        imgEmpProfile = getView().findViewById(R.id.img_profile_image);

        //when view is created then fetch values from db and display it here
        //also make sure only values existing are to be shown
        //hide other layouts completely
        //execute the background task and perform desired operations


    }
}
