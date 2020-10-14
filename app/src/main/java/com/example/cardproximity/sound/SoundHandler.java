package com.example.cardproximity.sound;

public class SoundHandler {

    public static final int duration = 3; // seconds
    public static final int sampleRate = 8000;
    public static final int numSamples = duration * sampleRate;
    public static final double[] sample = new double[numSamples];
    public static final double freqOfTone = 440; // hz

    SoundGenerator soundGenerator = new SoundGenerator();

    SoundAnalyzer soundAnalyzer = new SoundAnalyzer();


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
