package com.example.cardproximity.sound;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SoundHandler {

    AppCompatActivity activity;
    boolean hasPermission;

    public SoundHandler(AppCompatActivity activity) {
        this.activity = activity;
        checkPermission();
    }

    public static final int duration = 3; // seconds
    public static final int sampleRate = 8000;
    public static final int numSamples = duration * sampleRate;
    public static final double[] sample = new double[numSamples];
    public static final double freqOfTone = 440; // hz

    SoundGenerator soundGenerator = new SoundGenerator();

    SoundAnalyzer soundAnalyzer = new SoundAnalyzer();



    public void checkPermission () {
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

    public void startListening() {
        soundAnalyzer.startListening(soundGenerator.getGeneratedSnd());
    }

    public void startPlay() {
        soundGenerator.playSound();
    }

    public void stopPlay() {

    }

    public void stopListening() {

    }
}
