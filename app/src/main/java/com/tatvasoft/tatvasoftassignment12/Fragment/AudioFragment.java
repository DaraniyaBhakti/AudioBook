package com.tatvasoft.tatvasoftassignment12.Fragment;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tatvasoft.tatvasoftassignment12.Adapter.AudioAdapter;
import com.tatvasoft.tatvasoftassignment12.AsyncLoader.AudioAsyncLoader;
import com.tatvasoft.tatvasoftassignment12.databinding.FragmentAudioBinding;

import java.util.List;

public class AudioFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<String>> {


    private LoaderManager loaderManager;
    AudioAdapter audioAdapter;
    public static FragmentAudioBinding fragmentAudioBinding;

    public AudioFragment() {
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
        fragmentAudioBinding = FragmentAudioBinding.inflate(inflater,container,false);
        initializeLoader();
        return fragmentAudioBinding.getRoot();
    }

    public void initializeLoader(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadLoader();
        }
    }

    private void loadLoader(){
        if (loaderManager.getLoader(3) == null) {
            loaderManager.initLoader(3,null,this);
        } else {
            loaderManager.restartLoader(3, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeLoader();
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return new AudioAsyncLoader(getContext());
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        audioAdapter = new AudioAdapter(data);
        fragmentAudioBinding.audioRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentAudioBinding.audioRecyclerView.setAdapter(audioAdapter);
        fragmentAudioBinding.grantPermissionText.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        audioAdapter.clearData();
    }
}