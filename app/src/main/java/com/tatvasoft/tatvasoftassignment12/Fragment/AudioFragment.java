package com.tatvasoft.tatvasoftassignment12.Fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tatvasoft.tatvasoftassignment12.Adapter.AudioAdapter;
import com.tatvasoft.tatvasoftassignment12.AsyncLoader.AudioAsyncLoader;
import com.tatvasoft.tatvasoftassignment12.R;
import com.tatvasoft.tatvasoftassignment12.databinding.FragmentAudioBinding;

import java.util.List;

public class AudioFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>> {

    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    LoaderManager loaderManager;
    FragmentAudioBinding binding;

    public AudioFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAudioBinding.inflate(inflater,container,false);
        ((AppCompatActivity)requireActivity()).setTitle(getString(R.string.audio_files));
        loaderManager = requireActivity().getLoaderManager();
        checkPermission();

        return binding.getRoot();
    }

    private void loadLoader(){
        if (loaderManager.getLoader(3) == null) {
            loaderManager.initLoader(3,null,this);
        } else {
            loaderManager.restartLoader(3, null, this);
        }
    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        } else {
            loadLoader();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loaderManager.initLoader(3, null, this);
            }else if(shouldShowRequestPermissionRationale(permissions[0])){
                showDialogOK(getString(R.string.audio_alert_title),
                        getString(R.string.audio_alert_message),
                        (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                checkPermission();
                            }
                        });
            }
            else {
                Toast.makeText(getContext(), getString(R.string.audio_toast), Toast.LENGTH_LONG).show();
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
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new AudioAsyncLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        AudioAdapter audioAdapter = new AudioAdapter(data);
        binding.audioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.audioRecyclerView.setAdapter(audioAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
    }
}