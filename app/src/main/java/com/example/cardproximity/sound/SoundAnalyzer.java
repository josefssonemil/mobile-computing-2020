package com.example.cardproximity.sound;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

public class SoundAnalyzer {

    final int buffersize = Constants.numSamples;

    AudioRecord recorder;

    public SoundAnalyzer(){
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                11025, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, buffersize);
    }

    public void startListening(byte[] generatedSnd){
        recorder.startRecording();
        recorder.read(generatedSnd,0,generatedSnd.length);
        System.out.println("listen started");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            System.out.println(recorder.getMetrics());
        }

    }


}
