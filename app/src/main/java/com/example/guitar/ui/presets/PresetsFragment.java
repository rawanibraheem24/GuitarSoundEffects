package com.example.guitar.ui.presets;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitar.MainActivity;
import com.example.guitar.MainFragment;
import com.example.guitar.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PresetsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PresetAdapter adapter;
    private List<Preset> presetList;

    private boolean customLayoutVisible = false;
    private View customLayout; // Define customLayout variable

    // Save presets to SharedPreferences
    private void savePresets(List<Preset> presets) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("presets", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String presetsJson = gson.toJson(presets);
        editor.putString("presets", presetsJson);
        editor.apply();
    }

    // Load presets from SharedPreferences
    private List<Preset> loadPresets() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("presets", Context.MODE_PRIVATE);
        String presetsJson = sharedPreferences.getString("presets", "");
        if (!presetsJson.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Preset>>() {}.getType();
            List<Preset> loadedPresets = gson.fromJson(presetsJson, type);

            // Log the loaded presets
            for (Preset preset : loadedPresets) {
                Log.d("LoadedPresets", "Name: " + preset.getName());
            }

            return loadedPresets;
        }
        return new ArrayList<>(); // Return an empty list if no presets are found
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_presets, container, false);

        // Find your button
        View addButton = rootView.findViewById(R.id.addPresetButton);

        recyclerView = rootView.findViewById(R.id.presetsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load presets from SharedPreferences
        presetList = loadPresets();

        adapter = new PresetAdapter(presetList);
        recyclerView.setAdapter(adapter);


        // Set click listeners for the ImageButtons
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPresets();
            }
        });

        return rootView;
    }

    // Replace this with your logic to create the list of presets
    private List<Preset> createPresetList() {
        List<Preset> presets = new ArrayList<>();
        return presets;
    }

    private void showPresets() {
        // Get the LayoutInflater from the fragment's context
        LayoutInflater inflater = getLayoutInflater();
        Dialog presetsDialog = new Dialog(getContext(), R.style.CustomDialog);
        presetsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        presetsDialog.setContentView(R.layout.presets); // Use the custom layout
        AppCompatImageButton saveButton = presetsDialog.findViewById(R.id.saveButton);
        AppCompatImageButton closeButton = presetsDialog.findViewById(R.id.closeButton);
        AppCompatImageButton applyButton = presetsDialog.findViewById(R.id.applyButton);
        AppCompatImageButton deleteButton = presetsDialog.findViewById(R.id.deleteButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekBar volumeSeekBar = presetsDialog.findViewById(R.id.volumeSeek);
                SeekBar delaySeekBar = presetsDialog.findViewById(R.id.delaySeek);
                SeekBar chorusSeekBar = presetsDialog.findViewById(R.id.chorusSeek);
                SeekBar disSeekBar = presetsDialog.findViewById(R.id.disSeek);
                SeekBar wahSeekBar = presetsDialog.findViewById(R.id.wahSeek);
                SeekBar reverbSeekBar = presetsDialog.findViewById(R.id.reverbSeek);

                int volumeValue = volumeSeekBar.getProgress();
                int delayValue = delaySeekBar.getProgress();
                int chorusValue = chorusSeekBar.getProgress();
                int disValue = disSeekBar.getProgress();
                int wahValue = wahSeekBar.getProgress();
                int reverbValue = reverbSeekBar.getProgress();

                EditText nameEditText = presetsDialog.findViewById(R.id.profileNameEditText);
                String presetName = nameEditText.getText().toString();

                // Validate if the preset name is empty, and return if it is
                if (presetName.isEmpty()) {
                    // Handle this case, e.g., show an error message or return
                    return;
                }

                // Create a new Preset with all values
                Preset newPreset = new Preset(presetName, String.valueOf(volumeValue), String.valueOf(delayValue),
                        String.valueOf(chorusValue), String.valueOf(disValue), String.valueOf(wahValue), String.valueOf(reverbValue));

                // Add the new preset to the list
                presetList.add(newPreset);
                adapter.notifyDataSetChanged();

                savePresets(presetList);

                if (presetsDialog != null && presetsDialog.isShowing()) {
                    presetsDialog.dismiss();
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presetsDialog != null && presetsDialog.isShowing()) {
                    presetsDialog.dismiss();
                }
            }
        });

        if (applyButton != null) {
            // Set an OnClickListener for the "Apply" button
            applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the "Apply" button click action here
                    SeekBar volumeSeekBar = presetsDialog.findViewById(R.id.volumeSeek);
                    SeekBar delaySeekBar = presetsDialog.findViewById(R.id.delaySeek);
                    SeekBar chorusSeekBar = presetsDialog.findViewById(R.id.chorusSeek);
                    SeekBar disSeekBar = presetsDialog.findViewById(R.id.disSeek);
                    SeekBar wahSeekBar = presetsDialog.findViewById(R.id.wahSeek);
                    SeekBar reverbSeekBar = presetsDialog.findViewById(R.id.reverbSeek);

                    int volumeValue = volumeSeekBar.getProgress();
                    int delayValue = delaySeekBar.getProgress();
                    int chorusValue = chorusSeekBar.getProgress();
                    int disValue = disSeekBar.getProgress();
                    int wahValue = wahSeekBar.getProgress();
                    int reverbValue = reverbSeekBar.getProgress();

                    MainFragment.updateValues(volumeValue,delayValue,chorusValue,disValue,wahValue,reverbValue);

                }
            });
        }

        presetsDialog.show();
    }

}