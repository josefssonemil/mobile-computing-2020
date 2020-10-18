package com.example.cardproximity.sound;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cardproximity.sound.utils.Constants;

public class SoundHandler {

    private AppCompatActivity activity;
    private boolean hasPermission;

    private SoundGenerator soundGenerator;

    private SoundAnalyzer soundAnalyzer;

    enum Type {
        SENDER,
        RECEIVER
    }


    private Type current;

    // Samsung   Device ID:PPR1.180610.011

    // Emulator  Device ID:RSR1.200819.001.A1


    public SoundHandler(AppCompatActivity activity) {
        this.activity = activity;

        checkPermission();

        checkID(android.os.Build.ID);


        if(current == Type.SENDER){
            soundGenerator = new SoundGenerator();

        }

        else if (current == Type.RECEIVER){
            soundAnalyzer = new SoundAnalyzer();
        }
    }


    private void checkID(String ID) {
        if (ID.equals(Constants.SENDER)){
            current = Type.SENDER;
        }

        else if (ID.equals(Constants.RECEIVER)){
            current = Type.RECEIVER;
        }
    }




    private void checkPermission () {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1234);
            hasPermission = true;
            System.out.println(hasPermission);
        }
        else {
            System.out.println(hasPermission);

            hasPermission = false;
        }
    }

    public boolean hasPermssion (){
        return hasPermission;
    }


    public void start () {
        if (current == Type.SENDER){
            soundGenerator.play();
            System.out.println("Sender starts play");
        }

        else if (current == Type.RECEIVER){

            soundAnalyzer.startListening();
            System.out.println("Receiver starts listen");

        }
    }





}
