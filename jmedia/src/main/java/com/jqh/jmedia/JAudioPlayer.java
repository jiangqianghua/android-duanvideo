package com.jqh.jmedia;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by user on 2017/11/30.
 */
public class JAudioPlayer{
    protected static AudioTrack mAudioTrack;

        // Audio
        public static int audioInit(int sampleRate, boolean is16Bit, boolean isStereo, int desiredFrames) {
            int channelConfig = isStereo ? AudioFormat.CHANNEL_CONFIGURATION_STEREO : AudioFormat.CHANNEL_CONFIGURATION_MONO;
            int audioFormat = is16Bit ? AudioFormat.ENCODING_PCM_16BIT : AudioFormat.ENCODING_PCM_8BIT;
            int frameSize = (isStereo ? 2 : 1) * (is16Bit ? 2 : 1);
            Log.v("SDL", "SDL audio: wanted " + (isStereo ? "stereo" : "mono") + " " + (is16Bit ? "16-bit" : "8-bit") + " " + (sampleRate / 1000f) + "kHz, " + desiredFrames + " frames buffer");
            mAudioTrack = null ;
            if (sampleRate == 0)
                return -1 ;
            // Let the user pick a larger buffer if they really want -- but ye
            // gods they probably shouldn't, the minimums are horrifyingly high
            // latency already
            int size = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat) ;
            desiredFrames = Math.max(desiredFrames, (AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat) + frameSize - 1) / frameSize);

            if (mAudioTrack == null) {
                mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                        channelConfig, audioFormat, desiredFrames * frameSize, AudioTrack.MODE_STREAM);

                // Instantiating AudioTrack can "succeed" without an exception and the track may still be invalid
                // Ref: https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/AudioTrack.java
                // Ref: http://developer.android.com/reference/android/media/AudioTrack.html#getState()

                if (mAudioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
                    Log.e("SDL", "Failed during initialization of Audio Track");
                    mAudioTrack = null;
                    return -1;
                }

                mAudioTrack.play();
            }

            Log.v("SDL", "SDL audio: got " + ((mAudioTrack.getChannelCount() >= 2) ? "stereo" : "mono") + " " + ((mAudioTrack.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) ? "16-bit" : "8-bit") + " " + (mAudioTrack.getSampleRate() / 1000f) + "kHz, " + desiredFrames + " frames buffer");

            return 0;
        }

        public static void audioWriteShortBuffer(short[] buffer) {
            if(mAudioTrack == null)
                return ;
            for (int i = 0; i < buffer.length; ) {
                int result = mAudioTrack.write(buffer, i, buffer.length - i);
                if (result > 0) {
                    i += result;
                } else if (result == 0) {
                    try {
                        Thread.sleep(1);
                    } catch(InterruptedException e) {
                        // Nom nom
                    }
                } else {
                    Log.w("SDL", "SDL audio: error return from write(short)");
                    return;
                }
            }
        }

        public static void audioWriteByteBuffer(byte[] buffer) {
            if(mAudioTrack == null)
                return ;
            for (int i = 0; i < buffer.length; ) {
                int result = mAudioTrack.write(buffer, i, buffer.length - i);
                if (result > 0) {
                    i += result;
                } else if (result == 0) {
                    try {
                        Thread.sleep(1);
                    } catch(InterruptedException e) {
                        // Nom nom
                    }
                } else {
                    Log.w("SDL", "SDL audio: error return from write(byte)");
                    return;
                }
            }
        }

        public static void audioQuit() {
            if (mAudioTrack != null) {
                mAudioTrack.stop();
                mAudioTrack = null;
            }
        }
}
