package com.tatvasoft.tatvasoftassignment12.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tatvasoft.tatvasoftassignment12.Adapter.ViewPagerAdapter;
import com.tatvasoft.tatvasoftassignment12.Fragment.AudioFragment;
import com.tatvasoft.tatvasoftassignment12.Fragment.ContactFragment;
import com.tatvasoft.tatvasoftassignment12.R;
import com.tatvasoft.tatvasoftassignment12.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tatvasoft.tatvasoftassignment12.Fragment.AudioFragment.fragmentAudioBinding;
import static com.tatvasoft.tatvasoftassignment12.Fragment.ContactFragment.fragmentContactBinding;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 100;
    ContactFragment contactsFragment;
    AudioFragment audioFilesFragment;
    ActivityMainBinding binding;
    private final String[] titles = new String[]{"Contacts","Audio Files"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FragmentStateAdapter fragmentStateAdapter = new ViewPagerAdapter(this);

        binding.viewPager.setAdapter(fragmentStateAdapter);
        binding.viewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(titles[position])).attach();

        if (checkAndRequestPermissions()) {
            contactsFragment = new ContactFragment();
            audioFilesFragment = new AudioFragment();

        }
    }
    private boolean checkAndRequestPermissions() {

        int permissionAudio = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionContact = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (permissionAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)     {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            Map<String, Integer> permissionHashMap = new HashMap<>();
            // Initialize the map with both permissions
            permissionHashMap.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            permissionHashMap.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    permissionHashMap.put(permissions[i], grantResults[i]);
                // Check for both permissions

                //permissions granted
                if (permissionHashMap.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && permissionHashMap.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // process the normal flow
                    audioFilesFragment = new AudioFragment();
                    contactsFragment = new ContactFragment();
                    //else any one or both the permissions are not granted
                } else {
                    //permission is denied---without checking never ask again---- so ask permission again explaining the usage of permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {
                        showDialogOK(getString(R.string.alert_title),
                                getString(R.string.alert_message),
                                (dialog, which) -> {
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        checkAndRequestPermissions();
                                    }
                                });
                    }
                    //permission is denied and never ask again is checked
                    else {
                        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED){
                            fragmentAudioBinding.grantPermissionText.setVisibility(View.VISIBLE);
                        }
                        if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED ){
                            fragmentContactBinding.grantPermissionTextContact.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    private void showDialogOK(String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button, okListener)
                .setCancelable(false)
                .create()
                .show();
    }
}