package com.example.cardproximity.sound;

import com.example.cardproximity.sound.utils.Constants;

// Samsung   Device ID:PPR1.180610.011
// Emulator  Device ID:RSR1.200819.001.A1

public class SoundHandler {
    private SoundGenerator soundGenerator;
    private SoundAnalyzer soundAnalyzer;
    private Type current;
    private Boolean soundRunning;

    enum Type {
        SENDER,
        RECEIVER
    }

    public SoundHandler() {
        checkID(android.os.Build.ID);
        soundGenerator = new SoundGenerator();
        soundAnalyzer = new SoundAnalyzer();
        soundRunning = false;
    }

    private void checkID(String ID) {
        if (ID.equals(Constants.SENDER)) {
            current = Type.SENDER;
        } else {
            current = Type.RECEIVER;
        }
        /*else if (ID.equals(Constants.RECEIVER)){
            current = Type.RECEIVER;
        }*/
    }

    public void start() throws InterruptedException {
        if (current == Type.SENDER && !soundRunning) {
            soundGenerator.play();
            soundRunning = true;
        } else if (current == Type.RECEIVER) {
            // wait a little bit before listening
            Thread.sleep(500);
            soundAnalyzer.startListening();

        }
    }

    public void stop() {
        if (current == Type.SENDER && soundRunning) {
            soundGenerator.stop();
            soundRunning = false;
        } else if (current == Type.RECEIVER) {
            soundAnalyzer.stopListening();
        }
    }

    public boolean checkStatus() {
        if (current == Type.RECEIVER) {
            if (2196 == soundAnalyzer.getResult()) {
                return true;
            } else {
                return false;
            }
        } else if (current == Type.SENDER) {
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
