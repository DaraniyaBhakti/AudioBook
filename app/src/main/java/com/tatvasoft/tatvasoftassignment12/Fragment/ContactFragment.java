package com.tatvasoft.tatvasoftassignment12.Fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tatvasoft.tatvasoftassignment12.Adapter.ContactAdapter;
import com.tatvasoft.tatvasoftassignment12.Model.ContactsModel;
import com.tatvasoft.tatvasoftassignment12.databinding.FragmentContactBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    private LoaderManager loaderManager;
    private final ArrayList<ContactsModel> contactsModelArrayList = new ArrayList<>();
    private Map<Long, List<String>> phones;
    ContactAdapter contactAdapter;

    public String[] PROJECTION_NUMBER = new String[]{
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER};

    public String[] PROJECTION_DETAILS = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME};

    public String sortByName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
    public static FragmentContactBinding fragmentContactBinding;

    public ContactFragment(Context context) {
        this.context = context;
    }

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
        fragmentContactBinding = FragmentContactBinding.inflate(inflater,container,false);
        initializeLoader();
        return fragmentContactBinding.getRoot();
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
        contactAdapter = new ContactAdapter(contactsModelArrayList);
        setContact();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onResume() {
        super.onResume();
        setContact();
        initializeLoader();
    }

    public void setContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                getContactList();
            }
        } else {
            getContactList();
        }
    }

    public void getContactList() {
        fragmentContactBinding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentContactBinding.contactRecyclerView.setAdapter(contactAdapter);
    }

    public void initializeLoader(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            if (loaderManager.getLoader(1) == null) {
                loaderManager.initLoader(1, null, this);
            } else {
                loaderManager.restartLoader(1, null, this);
            }
        }
    }
}