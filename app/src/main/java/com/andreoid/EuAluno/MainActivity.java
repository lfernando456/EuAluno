package com.andreoid.EuAluno;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andreoid.EuAluno.broadcast_receivers.NotificationEventReceiver;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);
        //getWindow().setWindowAnimations(R.anim.fade_out);
        pref = getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        initFragment();
    }

    private void initFragment(){
        if(pref.getBoolean(Constants.IS_LOGGED_IN,false)){
            //Starting login activity

            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            finish();

            startActivity(intent);
        }else {
            setTheme(R.style.AppTheme);
            Fragment fragment;
            fragment = new LoginFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.fragment_frame, fragment);
            ft.commit();
        }

    }

}
