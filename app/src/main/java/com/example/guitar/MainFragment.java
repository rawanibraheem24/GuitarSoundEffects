package com.example.guitar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainFragment extends Fragment {
    private OnSaveButtonClickListener saveButtonClickListener;
    // Declare your ImageButtons here
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5, imageButton6;

    static int volume = 50;
    static int chorus = 50;
    static int delay = 50;
    static int reverb = 50;
    static int dis = 50;
    static int wah = 50;

    boolean saveButtonClicked = false;


        public interface OnSaveButtonClickListener {
        void onSaveButtonClicked(String dataToSend);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            saveButtonClickListener = (OnSaveButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSaveButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialize your ImageButtons by finding them within the root view
        imageButton1 = rootView.findViewById(R.id.imageButton1);
        imageButton2 = rootView.findViewById(R.id.imageButton2);
        imageButton3 = rootView.findViewById(R.id.imageButton3);
        imageButton4 = rootView.findViewById(R.id.imageButton4);
        imageButton5 = rootView.findViewById(R.id.imageButton5);
        imageButton6 = rootView.findViewById(R.id.imageButton6);

        // Set click listeners for the ImageButtons
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVolumeControlDialog();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelayControlDialog();
            }
        });


        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChorusControlDialog();
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation to the presets fragment here
                showDisControlDialog();
            }
        });

        imageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation to the presets fragment here
                showWahControlDialog();
            }
        });

        imageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle navigation to the presets fragment here
                showReverbControlDialog();
            }
        });

        return rootView;
    }

    private void showVolumeControlDialog() {

        // Create a new Dialog
        Dialog volumeDialog = new Dialog(getContext(), R.style.CustomDialog);


        volumeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        volumeDialog.setContentView(R.layout.volume_control); // Use the custom layout

        // Find the SeekBar and other views within the dialog
        SeekBar volumeSeekBar = volumeDialog.findViewById(R.id.volumeSeekBar);
        ImageButton closeButton = volumeDialog.findViewById(R.id.closeButton);

        if (volumeSeekBar == null) {
            Log.e("VolumeControl", "Volume SeekBar not found.");
            return;
        }

        if (closeButton == null) {
            Log.e("VolumeControl", "Close Button not found.");
            return;
        }

        // Set the initial volume value here
        volumeSeekBar.setProgress(volume);

        // Set a listener for the SeekBar to update the volume variable
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update your volume variable here based on 'progress'
                volume = progress; // Update the volume variable
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the volume control dialog using the class member
                if (volumeDialog != null && volumeDialog.isShowing()) {
                    volumeDialog.dismiss();
                }
            }
        });

        // Show the dialog
        if (volumeDialog != null) {
            volumeDialog.show();
        } else {
            Log.e("VolumeControl", "Volume Dialog is null.");
        }

        // Set an OnClickListener for the "Save" button
        ImageButton saveButton = volumeDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to indicate that the "Save" button was clicked
                saveButtonClicked = true;

                // Dismiss the volume control dialog
                if (volumeDialog != null && volumeDialog.isShowing()) {
                    volumeDialog.dismiss();
                }

                // Prepare the data you want to send to the Arduino
                String dataToSend = "Volume: " + String.valueOf(volume); // Replace with the volume data you want to send

                // Call the interface method to send data to the MainActivity
                saveButtonClickListener.onSaveButtonClicked(dataToSend);

                // Reset the flag after "Save" is clicked
                saveButtonClicked = false;
            }
        });
    }

    // DELAYYYY
    private void showDelayControlDialog() {

        // Create a new Dialog
        Dialog delayDialog = new Dialog(getContext(), R.style.CustomDialog);

        delayDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delayDialog.setContentView(R.layout.delay_control); // Use the custom layout

        // Find the SeekBar and other views within the dialog
        SeekBar delaySeekBar = delayDialog.findViewById(R.id.delaySeekBar);
        ImageButton closeButton = delayDialog.findViewById(R.id.closeButton);


        // Set the initial volume value here
        delaySeekBar.setProgress(delay);

        // Set a listener for the SeekBar to update the volume variable
        delaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update your volume variable here based on 'progress'
                delay = progress; // Update the volume variable
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the volume control dialog using the class member
                if (delayDialog != null && delayDialog.isShowing()) {
                    delayDialog.dismiss();
                }
            }
        });

        // Show the dialog
        if (delayDialog != null) {
            delayDialog.show();
        } else {
            Log.e("VolumeControl", "Volume Dialog is null.");
        }

        // Set an OnClickListener for the "Save" button
        ImageButton saveButton = delayDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to indicate that the "Save" button was clicked
                saveButtonClicked = true;

                // Dismiss the volume control dialog
                if (delayDialog != null && delayDialog.isShowing()) {
                    delayDialog.dismiss();
                }

                // Prepare the data you want to send to the Arduino
                String dataToSend = "Delay: " + String.valueOf(delay); // Replace with the volume data you want to send

                // Call the interface method to send data to the MainActivity
                saveButtonClickListener.onSaveButtonClicked(dataToSend);

                // Reset the flag after "Save" is clicked
                saveButtonClicked = false;
            }
        });
    }

    // chorus
    private void showChorusControlDialog() {
        // Create a new Dialog
        Dialog chorusDialog = new Dialog(getContext(), R.style.CustomDialog);

        chorusDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chorusDialog.setContentView(R.layout.chorus_control); // Use the custom layout

        // Find the SeekBar and other views within the dialog
        SeekBar chorusSeekBar = chorusDialog.findViewById(R.id.chorusSeekBar); // Change "delaySeekBar" to "chorusSeekBar"
        ImageButton closeButton = chorusDialog.findViewById(R.id.closeButton);

        // Set the initial chorus value here
        chorusSeekBar.setProgress(chorus); // Change "delay" to "chorus"

        // Set a listener for the SeekBar to update the chorus variable
        chorusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update your chorus variable here based on 'progress'
                chorus = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the chorus control dialog using the class member
                if (chorusDialog != null && chorusDialog.isShowing()) {
                    chorusDialog.dismiss();
                }
            }
        });

        // Show the dialog
        if (chorusDialog != null) {
            chorusDialog.show();
        } else {
            Log.e("ChorusControl", "Chorus Dialog is null.");
        }

        // Set an OnClickListener for the "Save" button
        ImageButton saveButton = chorusDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to indicate that the "Save" button was clicked
                saveButtonClicked = true;

                // Dismiss the chorus control dialog
                if (chorusDialog != null && chorusDialog.isShowing()) {
                    chorusDialog.dismiss();
                }

                // Prepare the data you want to send to the Arduino
                String dataToSend = "Chorus: " + String.valueOf(chorus); // Replace with the volume data you want to send

                // Call the interface method to send data to the MainActivity
                saveButtonClickListener.onSaveButtonClicked(dataToSend);

                // Reset the flag after "Save" is clicked
                saveButtonClicked = false;
            }
        });
    }

    //distortion
    private void showDisControlDialog() {
        // Create a new Dialog
        Dialog disDialog = new Dialog(getContext(), R.style.CustomDialog);

        disDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        disDialog.setContentView(R.layout.distortion_control); // Use the custom layout

        // Find the SeekBar and other views within the dialog
        SeekBar disSeekBar = disDialog.findViewById(R.id.disSeekBar); // Change "delaySeekBar" to "chorusSeekBar"
        ImageButton closeButton = disDialog.findViewById(R.id.closeButton);

        // Set the initial chorus value here
        disSeekBar.setProgress(dis); // Change "delay" to "chorus"

        // Set a listener for the SeekBar to update the chorus variable
        disSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update your chorus variable here based on 'progress'
                dis = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the chorus control dialog using the class member
                if (disDialog != null && disDialog.isShowing()) {
                    disDialog.dismiss();
                }
            }
        });

        // Show the dialog
        if (disDialog != null) {
            disDialog.show();
        } else {
            Log.e("ChorusControl", "Chorus Dialog is null.");
        }

        // Set an OnClickListener for the "Save" button
        ImageButton saveButton = disDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to indicate that the "Save" button was clicked
                saveButtonClicked = true;

                // Dismiss the chorus control dialog
                if (disDialog != null && disDialog.isShowing()) {
                    disDialog.dismiss();
                }

                // Prepare the data you want to send to the Arduino
                String dataToSend = "Distortion: " + String.valueOf(dis); // Replace with the volume data you want to send

                // Call the interface method to send data to the MainActivity
                saveButtonClickListener.onSaveButtonClicked(dataToSend);

                // Reset the flag after "Save" is clicked
                saveButtonClicked = false;
            }
        });
    }

    //wah
    private void showWahControlDialog() {
        // Create a new Dialog
        Dialog wahDialog = new Dialog(getContext(), R.style.CustomDialog);

        wahDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        wahDialog.setContentView(R.layout.wah_control); // Use the custom layout

        // Find the SeekBar and other views within the dialog
        SeekBar wahSeekBar = wahDialog.findViewById(R.id.wahSeekBar); // Change "delaySeekBar" to "chorusSeekBar"
        ImageButton closeButton = wahDialog.findViewById(R.id.closeButton);

        // Set the initial chorus value here
        wahSeekBar.setProgress(wah); // Change "delay" to "chorus"

        // Set a listener for the SeekBar to update the chorus variable
        wahSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update your chorus variable here based on 'progress'
                wah = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the chorus control dialog using the class member
                if (wahDialog != null && wahDialog.isShowing()) {
                    wahDialog.dismiss();
                }
            }
        });

        // Show the dialog
        if (wahDialog != null) {
            wahDialog.show();
        } else {
            Log.e("ChorusControl", "Chorus Dialog is null.");
        }

        // Set an OnClickListener for the "Save" button
        ImageButton saveButton = wahDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to indicate that the "Save" button was clicked
                saveButtonClicked = true;

                // Dismiss the chorus control dialog
                if (wahDialog != null && wahDialog.isShowing()) {
                    wahDialog.dismiss();
                }

                // Prepare the data you want to send to the Arduino
                String dataToSend = "Wah-wah: " + String.valueOf(wah); // Replace with the volume data you want to send

                // Call the interface method to send data to the MainActivity
                saveButtonClickListener.onSaveButtonClicked(dataToSend);

                // Reset the flag after "Save" is clicked
                saveButtonClicked = false;
            }
        });
    }

    //reverb
    private void showReverbControlDialog() {
        // Create a new Dialog
        Dialog reverbDialog = new Dialog(getContext(), R.style.CustomDialog);

        reverbDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reverbDialog.setContentView(R.layout.reverb_control); // Use the custom layout

        // Find the SeekBar and other views within the dialog
        SeekBar reverbSeekbar = reverbDialog.findViewById(R.id.reverbSeekBar);
        ImageButton closeButton = reverbDialog.findViewById(R.id.closeButton);

        // Set the initial chorus value here
        reverbSeekbar.setProgress(reverb); // Change "delay" to "chorus"

        // Set a listener for the SeekBar to update the chorus variable
        reverbSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update your chorus variable here based on 'progress'
                reverb = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the chorus control dialog using the class member
                if (reverbDialog != null && reverbDialog.isShowing()) {
                    reverbDialog.dismiss();
                }
            }
        });

        // Show the dialog
        if (reverbDialog != null) {
            reverbDialog.show();
        } else {
            Log.e("ChorusControl", "Chorus Dialog is null.");
        }

        // Set an OnClickListener for the "Save" button
        ImageButton saveButton = reverbDialog.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the flag to indicate that the "Save" button was clicked
                saveButtonClicked = true;

                // Dismiss the chorus control dialog
                if (reverbDialog != null && reverbDialog.isShowing()) {
                    reverbDialog.dismiss();
                }
                // Prepare the data you want to send to the Arduino
                String dataToSend = "Reverb: " + String.valueOf(reverb); // Replace with the volume data you want to send

                // Call the interface method to send data to the MainActivity
                saveButtonClickListener.onSaveButtonClicked(dataToSend);

                // Reset the flag after "Save" is clicked
                saveButtonClicked = false;
            }
        });
    }

    public static void updateValues(int newvolume, int newdelay, int newchorus, int newdis, int newwah, int newreverb) {
        volume = newvolume;
        delay = newdelay;
        chorus = newchorus;
        dis = newdis;
        wah = newwah;
        reverb = newreverb;
    }

}