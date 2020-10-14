package com.example.cardproximity.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SoundGenerator {

    private final int duration = Constants.duration; // seconds
    private final int sampleRate = Constants.sampleRate;
    private final int numSamples = Constants.numSamples;

    private final double freqOfTone = Constants.freqOfTone; // hz

    private final double[] sample = new double[numSamples];
    private final byte[] generatedSnd = new byte[2 * numSamples];


    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

   public void playSound(){

        genTone();

        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        System.out.println("play started");
    }


    protected double[] getSample() {
        return this.sample;
    }

    protected byte[] getGeneratedSnd(){
        return this.generatedSnd;
    }

}
