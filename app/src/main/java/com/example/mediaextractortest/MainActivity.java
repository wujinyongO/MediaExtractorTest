package com.example.mediaextractortest;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MediaExtractor mMediaExtractor;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                getMediaExtractor().setDataSource(this, Uri.parse("android.resource://" + this.getPackageName() + "/raw/" + R.raw.mo), null);
            } catch(IOException e){
                e.printStackTrace();
            }

            int videoIndex = getTrackIndex("video/");
            if (videoIndex >= 0) {
                MediaFormat format = getMediaExtractor().getTrackFormat(videoIndex);
                mTextView.setText("duration: " + format.getLong(MediaFormat.KEY_DURATION) + "\n"
                        + "width: " + format.getInteger(MediaFormat.KEY_WIDTH) + "\n"
                        + "height: " + format.getInteger(MediaFormat.KEY_HEIGHT) + "\n");
            }
        }
    }

    private MediaExtractor getMediaExtractor() {
        if (mMediaExtractor == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mMediaExtractor = new MediaExtractor();
            }
        }
        return mMediaExtractor;
    }

    private Integer getTrackIndex(String target) {
        Integer trackIndex = null;
        int count = 0;//获取轨道数量
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            count = getMediaExtractor().getTrackCount();
            for (int i = 0; i < count; i++) {
                MediaFormat mediaFormat = getMediaExtractor().getTrackFormat(i);
                String currentTrack = mediaFormat.getString(MediaFormat.KEY_MIME);
                if (currentTrack.startsWith(target)) {
                    trackIndex = i;
                    break;
                }
            }
        }
        return trackIndex != null ? trackIndex.intValue() : -1;
    }
}
