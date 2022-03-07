package com.tatvasoft.tatvasoftassignment12.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tatvasoft.tatvasoftassignment12.Fragment.ContactFragment;
import com.tatvasoft.tatvasoftassignment12.R;
import com.tatvasoft.tatvasoftassignment12.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new ContactFragment())
                .commit();
    }
}