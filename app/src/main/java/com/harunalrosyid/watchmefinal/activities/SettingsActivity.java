package com.harunalrosyid.watchmefinal.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.harunalrosyid.watchmefinal.R;
import com.harunalrosyid.watchmefinal.fragments.SettingsFragment;

import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.setting_holder, new SettingsFragment()).commit();
    }


}
