package com.example.cardproximity.sound;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.example.cardproximity.sound.utils.Constants;

public class SoundGenerator {

    /*private final int duration = Constants.duration; // seconds
    private final int sampleRate = Constants.sampleRate;
    private final int numSamples = Constants.numSamples;

    private final double freqOfTone = Constants.freqOfTone; // hz

    private final double[] sample = new double[numSamples];
    private final byte[] generatedSnd = new byte[2 * numSamples];

    private AudioTrack audioTrack;

    private boolean stopped;*/

    int sampleRate = 8000;
    double sample[] = null;
    byte generatedSnd[] = null;
    int m_ifreq = 400;
    Thread m_PlayThread = null;
    boolean m_bStop = false;
    AudioTrack m_audioTrack = null;
    int m_play_length = 1000;//in seconds


    public SoundGenerator(){
        genTone(m_play_length);


    }


   /* void genTone(){
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
    }*/

   /*public void playSound() {
        if (!stopped) {
            audioTrack.stop();
            audioTrack.reloadStaticData();
            stopped=true;
        }
        else {
            audioTrack.play();
            stopped=false;

        }
        System.out.println("play started");
    }*/


    protected double[] getSample() {
        return this.sample;
    }

    protected byte[] getGeneratedSnd(){
        return this.generatedSnd;
    }


    synchronized void stop() {
        m_bStop = true;
        if (m_PlayThread != null) {
            try {
                m_PlayThread.interrupt();
                m_PlayThread.join();
                m_PlayThread = null;
            } catch (Exception e) {

            }
        }
        if (m_audioTrack != null) {
            m_audioTrack.stop();
            m_audioTrack.release();
            m_audioTrack = null;
        }
    }

    synchronized void genTone(int iStep) {
        sample = new double[sampleRate];

        for (int i = 0; i < sampleRate; ++i) {
            sample[i] = Math.sin(2 * Math.PI * (i + iStep * sampleRate) / (sampleRate / m_ifreq));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        generatedSnd = new byte[2 * sampleRate];
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }


    synchronized void play() {
        m_bStop = false;
        m_PlayThread = new Thread() {
            public void run() {
                try {
                    int iToneStep = 0;

                    m_audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT, 2 * sampleRate,
                            AudioTrack.MODE_STREAM);

                    while (!m_bStop && m_play_length-- > 0) {
                        genTone(iToneStep++);

                        m_audioTrack.write(generatedSnd, 0, generatedSnd.length);
                        if (iToneStep == 1) {
                            m_audioTrack.play();
                        }
                    }
                } catch (Exception e) {
                    Log.e("Tone", e.toString());
                } catch (OutOfMemoryError e) {
                    Log.e("Tone", e.toString());
                }

            }
        };
        m_PlayThread.start();
    }

}
