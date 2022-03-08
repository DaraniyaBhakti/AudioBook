package com.tatvasoft.tatvasoftassignment12.Fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tatvasoft.tatvasoftassignment12.Adapter.ContactAdapter;
import com.tatvasoft.tatvasoftassignment12.Model.ContactsModel;
import com.tatvasoft.tatvasoftassignment12.R;
import com.tatvasoft.tatvasoftassignment12.databinding.FragmentContactBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUEST_CONTACTS = 101;
    private LoaderManager loaderManager;
    private final ArrayList<ContactsModel> contactsModelArrayList = new ArrayList<>();
    private Map<Long, List<String>> phones;

    public String[] PROJECTION_NUMBER = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER};

    public String[] PROJECTION_DETAILS = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME};

    public String sortByName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
    FragmentContactBinding binding;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = requireActivity().getLoaderManager();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater,container,false);
        ((AppCompatActivity)requireActivity()).setTitle(getString(R.string.contacts));
        setHasOptionsMenu(true);
        checkPermission();
        return binding.getRoot();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1) {
            return new CursorLoader(getContext(),
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION_NUMBER,
                    null,
                    null,
                    null);
        }
        return new CursorLoader(getContext(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION_DETAILS,
                null,
                null,
                sortByName);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()){
            case 1: {
                phones = new HashMap<>();
                if (cursor != null) {
                    while (!cursor.isClosed() && cursor.moveToNext()) {
                        long contactId = cursor.getLong(0);
                        String phone = cursor.getString(1);
                        List<String> list;
                        if (phones.containsKey(contactId)) {
                            list = phones.get(contactId);
                        } else {
                            list = new ArrayList<>();
                            phones.put(contactId, list);
                        }
                        assert list != null;
                        list.add(phone);
                    }
                    cursor.close();
                }
                loaderManager.initLoader(2, null, this);
                break;
            }
            case 2: {
                String demoName = "";
                if (cursor != null) {
                    while (!cursor.isClosed() && cursor.moveToNext()) {
                        long id = cursor.getLong(0);
                        String name = cursor.getString(1);
                        List<String> contactPhones = phones.get(id);
                        if (contactPhones != null) {
                            for (String phone : contactPhones) {
                                if(!demoName.contains(name)) {
                                    demoName = name;
                                    ContactsModel contactsModel = new ContactsModel();
                                    contactsModel.setContactName(name);
                                    contactsModel.setContactNumber(phone);
                                    contactsModelArrayList.add(contactsModel);
                                }
                            }
                        }
                    }
                    cursor.close();

                }
            }
        }
        ContactAdapter contactAdapter = new ContactAdapter(contactsModelArrayList);
        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.contactRecyclerView.setAdapter(contactAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
        } else {
            if (loaderManager.getLoader(1) == null) {
                loaderManager.initLoader(1, null, this);
            } else {
                loaderManager.restartLoader(1, null, this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loaderManager.initLoader(1,null,this);
            }else if(shouldShowRequestPermissionRationale(permissions[0])){
                showDialogOK(getString(R.string.contact_alert_title),
                        getString(R.string.contact_alert_message),
                        (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                checkPermission();
                            }
                        });
            }
            else {
                Toast.makeText(getContext(), getString(R.string.toast_contact), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showDialogOK(String title, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button, okListener)
                .create()
                .show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.option_menu_audio,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new AudioFragment())
                .addToBackStack(null)
                .commit();
        return super.onOptionsItemSelected(item);
    }

}