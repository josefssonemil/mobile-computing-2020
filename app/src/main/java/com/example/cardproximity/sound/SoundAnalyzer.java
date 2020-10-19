package com.example.cardproximity.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.example.cardproximity.sound.utils.Complex;
import com.example.cardproximity.sound.utils.Constants;
import com.example.cardproximity.sound.utils.FFT;

public class SoundAnalyzer {

    private final int buffersize = 1024;
    private short[] buffer = new short[buffersize];


    AudioRecord recorder;

    private double result;

    public SoundAnalyzer(){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                8000, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, buffersize);
    }

    public void startListening(){
        recorder.startRecording();

            int bufferReadResult = recorder.read(buffer, 0, buffersize); // record data from mic into buffer
            if (bufferReadResult > 0) {
                double result = parseInput();
                this.result = result;
                System.out.println("Result: " + result);

        }
    }

    public void stopListening(){
        recorder.stop();
    }


    private double parseInput () {
        double[] magnitude = new double[buffersize / 2];

        //Create Complex array for use in FFT
        Complex[] fftTempArray = new Complex[buffersize];
        for (int i = 0; i < buffersize; i++) {
            fftTempArray[i] = new Complex(buffer[i], 0);
        }

        //Obtain array of FFT data
        final Complex[] fftArray = FFT.fft(fftTempArray);
        // calculate power spectrum (magnitude) values from fft[]
        for (int i = 0; i < (buffersize / 2) - 1; ++i) {

            double real = fftArray[i].re();
            double imaginary = fftArray[i].im();
            magnitude[i] = Math.sqrt(real * real + imaginary * imaginary);

        }

        // find largest peak in power spectrum
        double max_magnitude = magnitude[0];
        int max_index = 0;
        for (int i = 0; i < magnitude.length; ++i) {
            if (magnitude[i] > max_magnitude) {
                max_magnitude = (int) magnitude[i];
                max_index = i;
            }
        }
        double freq = 44100 * max_index / buffersize;//here will get frequency in hz like(17000,18000..etc)

        return freq;
    }

    public double getresult() {
        return this.result;
    }


}
