package com.lms.admin.lms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

    }

    public void moveToActivityFunction(View view) {

        int id = view.getId();
        Intent intent;
        switch (id) {

            case R.id.imgAdminDashboardAddNewStories:
                intent = new Intent(AdminDashboardActivity.this, ManagePostActivity.class);
                startActivity(intent);
                break;
            case R.id.imgAdminDashboardPayrollMgt:
                intent = new Intent(AdminDashboardActivity.this, ViewPayrollSlipActivity.class);
                startActivity(intent);
                break;
            case R.id.imgAdminDashboardAddNewEmp:
                intent = new Intent(AdminDashboardActivity.this, RegisterNewEmpActivity.class);
                startActivity(intent);
                break;
            case R.id.imgAdminDashboardManageEmp:
                intent = new Intent(AdminDashboardActivity.this, ManageEmpDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.imgAdminDashboardLeaveDetails:
                intent = new Intent(AdminDashboardActivity.this, LeaveDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.imgAdminDashboardEditLeaveStructure:
                intent = new Intent(AdminDashboardActivity.this, EditLeaveStructureActivity.class);
                startActivity(intent);
                break;
        }

    }


}

