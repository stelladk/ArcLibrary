package com.stelladk.customimageview;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.stelladk.arclib.ArcLayout;
import com.stelladk.arclib.ArcShape;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        demoControls();
    }

    private void demoControls(){
        final ArcLayout arcLayout = findViewById(R.id.circle);

        LinearLayout topLeftControls = findViewById(R.id.topLeftControls);
        SeekBar topLeftSeekBar = topLeftControls.findViewById(R.id.arcTypeControls).findViewById(R.id.arcSeekBar);
        SwitchCompat topLeftSwitch = topLeftControls.findViewById(R.id.axisSwitch);

        LinearLayout topRightControls = findViewById(R.id.topRightControls);
        SeekBar topRightSeekBar = topRightControls.findViewById(R.id.arcTypeControls).findViewById(R.id.arcSeekBar);
        SwitchCompat topRightSwitch = topRightControls.findViewById(R.id.axisSwitch);

        LinearLayout bottomLeftControls = findViewById(R.id.bottomLeftControls);
        SeekBar bottomLeftSeekBar = bottomLeftControls.findViewById(R.id.arcTypeControls).findViewById(R.id.arcSeekBar);
        SwitchCompat bottomLeftSwitch = bottomLeftControls.findViewById(R.id.axisSwitch);

        LinearLayout bottomRightControls = findViewById(R.id.bottomRightControls);
        SeekBar bottomRightSeekBar = bottomRightControls.findViewById(R.id.arcTypeControls).findViewById(R.id.arcSeekBar);
        SwitchCompat bottomRightSwitch = bottomRightControls.findViewById(R.id.axisSwitch);

        SeekBar radiusSeekBar = findViewById(R.id.radiusSeekBar);

        topLeftSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                arcLayout.setTopLeftArcType(i-1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                arcLayout.redraw();
            }
        });
        topRightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                arcLayout.setTopRightArc(i-1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                arcLayout.redraw();
            }
        });
        bottomLeftSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                arcLayout.setBottomLeftArc(i-1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                arcLayout.redraw();
            }
        });
        bottomRightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                arcLayout.setBottomRightArc(i-1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                arcLayout.redraw();
            }
        });
        topLeftSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    arcLayout.setTopLeftOuterAxis(ArcShape.X_AXIS);
                }else{
                    arcLayout.setTopLeftOuterAxis(ArcShape.Y_AXIS);
                }
                arcLayout.redraw();
            }
        });
        topRightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    arcLayout.setTopRightOuterAxis(ArcShape.X_AXIS);
                }else{
                    arcLayout.setTopRightOuterAxis(ArcShape.Y_AXIS);
                }
                arcLayout.redraw();
            }
        });
        bottomLeftSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    arcLayout.setBottomLeftOuterAxis(ArcShape.X_AXIS);
                }else{
                    arcLayout.setBottomLeftOuterAxis(ArcShape.Y_AXIS);
                }
                arcLayout.redraw();
            }
        });
        bottomRightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    arcLayout.setBottomRightOuterAxis(ArcShape.X_AXIS);
                }else{
                    arcLayout.setBottomRightOuterAxis(ArcShape.Y_AXIS);
                }
                arcLayout.redraw();
            }
        });

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                arcLayout.setTopLeftRadius(i);
                arcLayout.setTopRightRadius(i);
                arcLayout.setBottomLeftRadius(i);
                arcLayout.setBottomRightRadius(i);
                arcLayout.redraw();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}