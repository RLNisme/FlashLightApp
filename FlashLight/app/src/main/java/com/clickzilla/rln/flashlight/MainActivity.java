package com.clickzilla.rln.flashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;


public class MainActivity extends ActionBarActivity {

    ImageView img;
    Switch cp;
    boolean state;
    RelativeLayout main;
    private Camera camera;
    android.hardware.Camera.Parameters params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        main = (RelativeLayout)findViewById(R.id.main);

        img = (ImageView)findViewById(R.id.imageView);
        img.setImageResource(R.drawable.star_off);

        cp = (Switch)findViewById(R.id.switch1);

        //isFlashSupport();  // check the device is has camera flash

        getCamera();

        cp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("State",isChecked+"");
                state = isChecked;
                if(isChecked == true){
                    flashOn();
                }else{
                    flashOff();
                }
            }
        });
    }

    private void isFlashSupport(){
        boolean hasFlas = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlas){

          AlertDialog ad = new AlertDialog.Builder(MainActivity.this).create();
            ad.setTitle("Error");
            ad.setMessage("Sorry!,Your device doesn't support flash.");
            ad.setButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                    return;
                }
            });
            ad.show();
        }

    }

    private void getCamera(){
        if(camera == null){
            try {
                camera = Camera.open();
                params = camera.getParameters();
            }catch (Exception e){
                e.printStackTrace();
                Log.d("Error",e.getMessage());
            }
        }
    }

    private void flashOn(){
        if(camera == null || params == null){
            return;
        }

        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();

        img.setImageResource(R.drawable.star);
        main.setBackgroundResource(R.drawable.back_ground_glow);
    }

    private void flashOff(){
        if(camera == null || params == null){
            return;
        }

        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();

        img.setImageResource(R.drawable.star_off);
        main.setBackgroundResource(R.drawable.back_ground);
    }


    @Override
    protected void onPause() {
        super.onPause();
        flashOff();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(state){
            flashOn();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }
}
