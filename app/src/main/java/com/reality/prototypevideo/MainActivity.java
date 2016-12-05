package com.reality.prototypevideo;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity  implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,View.OnTouchListener{

    private VideoView videoView;
    boolean pauseVideo=false;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        //Reference of video view
        videoView= (VideoView) findViewById(R.id.video_view);
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnTouchListener(this);
//        playFileFromResources(R.raw.video);
        playFileFromSD(Constant.MEDIA_PATH);
        videoView.start();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("test","Screen rotate Identified :: ");
        super.onSaveInstanceState(outState);
//        playFileFromResources(R.raw.video);
        playFileFromSD(Constant.MEDIA_PATH);
        videoView.seekTo(position);
        videoView.start();
        pauseVideo=false;
        position=0;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        playFileFromResources(R.raw.video);
        playFileFromSD(Constant.MEDIA_PATH);
    }


    public boolean playFileFromResources(int fileRes){
        if(fileRes==0){
            stopPlaying();
            return false;
        }else{
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + fileRes));
            return true;
        }
    }

    public boolean playFileFromSD(String path){
        if(path!=null&& !path.isEmpty()){
            videoView.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory().getPath()+path));
            return true;

        }else {
            stopPlaying();
            return false;
        }
    }

    private void stopPlaying() {
        videoView.stopPlayback();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        Toast.makeText(MainActivity.this, Constant.TOAST_MSG+ApiHelperClass.getHttpResponseTime(),
                    Toast.LENGTH_SHORT).show();
        videoView.setVideoURI(Uri.parse(Environment.getExternalStorageDirectory().getPath()+Constant.MEDIA_PATH));
//
        videoView.start();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        mp.setLooping(true);

    }



    public void toggle(){
        if(pauseVideo){
//            playFileFromResources(R.raw.video);
            playFileFromSD(Constant.MEDIA_PATH);
            videoView.seekTo(position);
            videoView.start();
            pauseVideo = false;
            position = 0;
        }else{
            videoView.pause();
            position = videoView.getCurrentPosition();
            pauseVideo = true;
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        toggle();
        return false;
    }
}
