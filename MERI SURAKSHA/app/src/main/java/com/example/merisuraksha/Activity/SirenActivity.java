package com.example.merisuraksha.Activity;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.merisuraksha.R;

public class SirenActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView imageView;
    private boolean isSirenOn = false;
    private AudioManager audioManager;
    private AudioFocusRequest focusRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren);

        imageView = findViewById(R.id.siren_image);
        mediaPlayer = MediaPlayer.create(this, R.raw.police);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSiren();
            }
        });
    }

    private void toggleSiren() {
        if (isSirenOn) {
            // Turn off the siren
            stopSiren();
            // Change image to off state
            imageView.setImageResource(R.drawable.sound_off);
            isSirenOn = false;
        } else {
            // Turn on the siren
            startSiren();
            // Change image to on state
            imageView.setImageResource(R.drawable.sound_on);
            isSirenOn = true;
        }
    }

    private void startSiren() {
        if (mediaPlayer != null) {
            // Request audio focus
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                        .setAudioAttributes(attributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(new AudioManager.OnAudioFocusChangeListener() {
                            @Override
                            public void onAudioFocusChange(int focusChange) {
                                // Handle audio focus changes if needed
                            }
                        })
                        .build();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioManager.requestAudioFocus(focusRequest);
            }

            // Start the siren
            mediaPlayer.start();
        }
    }

    private void stopSiren() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            // Release audio focus
            if (focusRequest != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    audioManager.abandonAudioFocusRequest(focusRequest);
                }
            }
            // Prepare media player for next playback
            mediaPlayer.prepareAsync();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Reset the volume to maximum when the activity is started
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // Reset the volume to maximum if volume buttons are pressed
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Release the media player when the activity is stopped
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
