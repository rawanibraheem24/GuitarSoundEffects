package com.example.guitar.ui.presets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitar.MainActivity;
import com.example.guitar.MainFragment;
import com.example.guitar.R;

import java.util.List;
import java.util.Objects;

//PRESET ADAPTER CLASS TO BIND LIST TO PRESET FRAGMENT
public class PresetAdapter extends RecyclerView.Adapter<PresetAdapter.PresetViewHolder> {
    private List<Preset> presetList;

    public PresetAdapter(List<Preset> presetList) {
        this.presetList = presetList;
    }

    @NonNull
    @Override
    public PresetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_preset, parent, false);
        return new PresetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresetViewHolder holder, int position) {
        Preset preset = presetList.get(position);
        holder.nameTextView.setText(preset.getName());
        holder.volumeTextView.setText("Volume: " + preset.getVolume());
        holder.delayTextView.setText("Delay: " + preset.getDelay());
        holder.chorusTextView.setText("Chorus: " + preset.getChorus());
        holder.disTextView.setText("Distortion: " + preset.getDis());
        holder.wahTextView.setText("Wah-wah: " + preset.getWah());
        holder.reverbTextView.setText("Reverb: " + preset.getReverb());

        // Handle delete button click here
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    presetList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    notifyItemRangeChanged(adapterPosition, getItemCount());
                }
            }
        });

        // Handle apply button click
        holder.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int volume = Integer.parseInt(preset.getVolume());
                int delay = Integer.parseInt(preset.getDelay());
                int chorus = Integer.parseInt(preset.getChorus());
                int reverb = Integer.parseInt(preset.getReverb());
                int dis = Integer.parseInt(preset.getDis());
                int wah = Integer.parseInt(preset.getWah());

                // Prepare the data you want to send to the MainActivity
                String dataToSend = "Volume: " + String.valueOf(volume) + "\n" +
                        "Delay: " + String.valueOf(delay) + "\n" +
                        "Chorus: " + String.valueOf(chorus) + "\n" +
                        "Reverb: " + String.valueOf(reverb) + "\n" +
                        "Distortion: " + String.valueOf(dis) + "\n" +
                        "Wah-wah: " + String.valueOf(wah);

                // Get the MainActivity instance from the context
                MainActivity mainActivity = (MainActivity) v.getContext();

                // Call the sendDataOverBluetooth method to send data to the BluetoothGatt
                mainActivity.onSaveButtonClicked(dataToSend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presetList.size();
    }

    public class PresetViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView volumeTextView;
        TextView delayTextView;
        TextView chorusTextView;
        TextView disTextView;
        TextView wahTextView;
        TextView reverbTextView;
        AppCompatImageButton deleteButton;
        AppCompatImageButton applyButton;

        public PresetViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            volumeTextView = itemView.findViewById(R.id.volumeTextView);
            delayTextView = itemView.findViewById(R.id.delayTextView);
            chorusTextView = itemView.findViewById(R.id.chorusTextView);
            disTextView = itemView.findViewById(R.id.disTextView);
            wahTextView = itemView.findViewById(R.id.wahTextView);
            reverbTextView = itemView.findViewById(R.id.reverbTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            applyButton = itemView.findViewById(R.id.applyButton);
        }
    }
}
