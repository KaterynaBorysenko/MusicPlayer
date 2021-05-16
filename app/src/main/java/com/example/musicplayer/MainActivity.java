package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    volatile MediaPlayer mp;
    int totalTime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn= findViewById(R.id.playBtn);
        elapsedTimeLabel=findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel= findViewById(R.id.remainingTimeLabel);


        mp=MediaPlayer.create(this,R.raw.mp3);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime=mp.getDuration();

        positionBar= findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        volumeBar= findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volumeNum=progress/100f;
                mp.setVolume(volumeNum,volumeNum);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        new Thread(() -> {
            while (mp!=null);
            try {
                Message msg=new Message();
                msg.what=mp.getCurrentPosition();
                handler.sendMessage(msg);
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    public Handler handler=new  Handler(){

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg){
            int currentPosition=msg.what;
            positionBar.setProgress(currentPosition);

            String elapseTime=createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapseTime);

            String remainingTime=createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("-"+remainingTime);

        }
    };
    public String createTimeLabel(int time){
        int min=time/1000/60;
        int sec=time/1000%60;

        String timeLabel = min + ":";
        if (sec<10)timeLabel+="0";
        timeLabel+=sec;

        return timeLabel;
    }

    public void playBtnClick(View view) {
        if (!mp.isPlaying()){
            mp.start();
            playBtn.setBackgroundResource(R.drawable.st);
        }else{
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.m2);
        }




    }
}

