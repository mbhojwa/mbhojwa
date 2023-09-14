package com.example.project10;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 *
 */

public class BottomSheetClass extends BottomSheetDialogFragment {
    private BottomSheetListener bottomSheetListener;

    static TextView Playing;
    static ProgressBar progressBar;
    Button playbutton;
    Button pausebutton;
    Switch switch12;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheetlayout, container, false);

        Playing = view.findViewById(R.id.textView_playing);
        progressBar = view.findViewById(R.id.progressBar);
        playbutton = view.findViewById(R.id.button_play);
        pausebutton = view.findViewById(R.id.button_pause);
        switch12 = view.findViewById(R.id.switch12);

        playbutton.setOnClickListener(new View.OnClickListener() {
            /**
             * onclick function for the play button
             * @param view
             */
            @Override
            public void onClick(View view) {
                bottomSheetListener.play();
            }
        });

        pausebutton.setOnClickListener(new View.OnClickListener() {
            /**
             * onclick function for the pause button
             * @param view
             */
            @Override
            public void onClick(View view) {
                bottomSheetListener.pause();
            }
        });

        switch12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * onchange function for the switch button
             * @param compoundButton
             * @param b
             */
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bottomSheetListener.shuffle(switch12.isChecked());
            }
        });

        return view;
    }

    public interface BottomSheetListener {
        void play();
        void pause();
        void shuffle(Boolean checked);
    }

    /**
     * on attach function to communicate with the activity
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        bottomSheetListener = (BottomSheetListener) context;
    }

}
