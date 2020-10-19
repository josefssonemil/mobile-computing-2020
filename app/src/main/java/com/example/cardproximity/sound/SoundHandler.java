package com.example.cardproximity.sound;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cardproximity.sound.utils.Constants;

public class SoundHandler {



    private SoundGenerator soundGenerator;

    private SoundAnalyzer soundAnalyzer;

    enum Type {
        SENDER,
        RECEIVER
    }


    private Type current;

    // Samsung   Device ID:PPR1.180610.011

    // Emulator  Device ID:RSR1.200819.001.A1


    public SoundHandler() {


        checkID(android.os.Build.ID);


            soundGenerator = new SoundGenerator();


            soundAnalyzer = new SoundAnalyzer();

    }


    private void checkID(String ID) {
        if (ID.equals(Constants.SENDER)){
            current = Type.SENDER;
        }

        else {
            current = Type.RECEIVER;
        }

        /*else if (ID.equals(Constants.RECEIVER)){
            current = Type.RECEIVER;
        }*/
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

    public void stop(){
        if (current == Type.SENDER){
            soundGenerator.stop();
            System.out.println("Sender stops play");
        }

        else if (current == Type.RECEIVER){

            soundAnalyzer.stopListening();
            System.out.println("Receiver stops listen");

        }
    }

    public boolean checkStatus() {


        if (current == Type.RECEIVER) {
            if (soundGenerator.getFrequency() == soundAnalyzer.getResult()) {
                return true;
            }
            else {
                return false;

            }
        }

        else if (current == Type.SENDER) {
            return false;
        }


        return false;

        // Debug
      /* if (current == Type.SENDER) {
           if (soundGenerator.getFrequency() == soundGenerator.getFrequency()){
               System.out.println("success! frequencies matches");

               return true;
           }
           else {
               System.out.println("failure! frequencies does not match");

               return false;
           }
       }

       return false;*/

       /*
       if (soundGenerator.getFrequency() == soundAnalyzer.getResult()) {
           System.out.println("success! frequencies matches");
           return true;
       }

       else {
           System.out.println("failure! frequencies does not match");

           return false;
       }*/

    }





}
