package com.example.imagine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;



import java.io.File;
import java.util.ArrayList;



public class PlaySong extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();

        updateSeek.interrupt();
        mediaPlayer.pause();
        mediaPlayer.release();



    }

    TextView textView;
    TextView musicEnd;
    TextView musicUpdate;

    int flag=0;


    ImageView play,previous,next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);






        textView=findViewById(R.id.textview);
        play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        seekBar = findViewById(R.id.seekbar);
        musicUpdate=findViewById(R.id.musicUpdate);
        musicEnd=findViewById(R.id.musicEnd);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);






        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();




        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {



                String currentTime = CreateTime(progress,seekBar.getMax());
                musicUpdate.setText(currentTime);





            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                flag=1;




            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                flag=0;

            }
        });

/// FINALLY PROBLEM SHOT OUT


        updateSeek = new Thread(){
            @Override
            public void run() {
                int currentPosition;

                try{
                    while(true){


                        if(flag == 0) {
                            currentPosition = mediaPlayer.getCurrentPosition();
                            seekBar.setProgress(currentPosition);

                        }




                        Thread.sleep(500);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();



//media end time set

        String endTime= CreateTime(mediaPlayer.getDuration(),mediaPlayer.getDuration()+2000);
        musicEnd.setText(endTime);

//Handler for current time update but it give error when back

//        final Handler x=new Handler();
//
//        final int delay=1000;
//
//        x.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                if(flag==0) {
//
//
//                    String currentTime = CreatTime(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
//                    musicUpdate.setText(currentTime);
//
//
//                }
//
//                x.postDelayed(this,delay);
//            }
//        },delay);


        play.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.play);
                mediaPlayer.pause();
                textView.setTextColor(Color.parseColor("#A300FF"));
            }
            else{
                play.setImageResource(R.drawable.pause);
                mediaPlayer.start();
                textView.setTextColor(Color.parseColor("#FFFFFF"));
            }

        });

        previous.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(position!=0){
                position = position - 1;
            }
            else{
                position = songs.size() - 1;
            }
            Uri uri1 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());

            seekBar.setProgress(mediaPlayer.getCurrentPosition());

            String endTime1 = CreateTime(mediaPlayer.getDuration(),mediaPlayer.getDuration()+2000);
            musicEnd.setText(endTime1);




            textContent = songs.get(position).getName().replace(".mp3","");
            textView.setText(textContent);
        });

        next.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();

            if(position!=songs.size()-1){
                position = position + 1;
            }
            else{
                position = 0;
            }
            Uri uri12 = Uri.parse(songs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri12);
            mediaPlayer.start();
            play.setImageResource(R.drawable.pause);
            seekBar.setMax(mediaPlayer.getDuration());

            seekBar.setProgress(mediaPlayer.getCurrentPosition());

            String endTime12 = CreateTime(mediaPlayer.getDuration(),mediaPlayer.getDuration()+2000);
            musicEnd.setText(endTime12);



            textContent = songs.get(position).getName().replace(".mp3","");
            textView.setText(textContent);

        });



    }
private void next()
{
   // next.isPressed();
    mediaPlayer.stop();
    mediaPlayer.release();

    if(position!=songs.size()-1){
        position = position + 1;
    }
    else{
        position = 0;
    }
    Uri uri12 = Uri.parse(songs.get(position).toString());
    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri12);
    mediaPlayer.start();
    play.setImageResource(R.drawable.pause);
    seekBar.setMax(mediaPlayer.getDuration());

    seekBar.setProgress(mediaPlayer.getCurrentPosition());

    String endTime12 = CreateTime(mediaPlayer.getDuration(),mediaPlayer.getDuration()+2000);
    musicEnd.setText(endTime12);



    textContent = songs.get(position).getName().replace(".mp3","");
    textView.setText(textContent);

}


private String CreateTime(int Duration, int endTime)
{
    //end time parameter for auto switch next song with 800ms difference

    if(Duration+600>=endTime)
    {
        next();
    }
    String time="0";

   int sec=Duration/1000;

   int min=sec/60;
    time+=min+":";
    sec=sec%60;

    if(sec<10)
    {
        time+='0';
    }



    time+=sec;

    return time;

}


}