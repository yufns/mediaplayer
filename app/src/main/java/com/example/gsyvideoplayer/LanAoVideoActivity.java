package com.example.gsyvideoplayer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.gsyvideoplayer.video.SampleVideo;

import static com.example.gsyvideoplayer.Main2Activity.URL_VIDEO;

/**
 * User Eric Hu
 * <p>Time 2019-06-17 15:28
 * <p>Email erichu1208@gmail.com
 * <p>Des
 */
public class LanAoVideoActivity extends AppCompatActivity {
    @BindView(R.id.video_player) SampleVideo videoPlayer;
    private String url;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lanao);
        ButterKnife.bind(this);
        url= getIntent().getStringExtra(URL_VIDEO);
    }
}
