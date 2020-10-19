package com.example.cardproximity.sound.utils;

public final class Constants {

    public static final int duration = 3; // seconds
    public static final int sampleRate = 8000;
    public static final int numSamples = duration * sampleRate;
    public static final double[] sample = new double[numSamples];
    public static final double freqOfTone = 440; // hz



    // Phones

    // Sender (output)
//    public static final String SENDER = "RSR1.200819.001.A1";
    public static final String SENDER = "QSR1.200715.002";

    // Reader (input)
    public static final String RECEIVER = "PPR1.180610.011";

}
