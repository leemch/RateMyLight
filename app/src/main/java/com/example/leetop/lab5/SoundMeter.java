package com.example.leetop.lab5;

import android.media.MediaRecorder;

import java.io.IOException;

public class SoundMeter {
    private MediaRecorder mRecorder = null;

    public void start() {


        //Set the outpute file path to null so we are not actually saving the recordings
        try {
            if (mRecorder == null) {
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                mRecorder.prepare();
                mRecorder.start();
            }
        }
        catch (IOException e){

        }


    }


    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)

            //Convert to dB
            return 20 * Math.log10(mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;

    }
}
