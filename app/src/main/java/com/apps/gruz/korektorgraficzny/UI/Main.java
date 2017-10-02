package com.apps.gruz.korektorgraficzny.UI;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.apps.gruz.korektorgraficzny.Core.DatabaseInterface;
import com.apps.gruz.korektorgraficzny.Core.DatabaseReader;
import com.apps.gruz.korektorgraficzny.Core.Pdata;
import com.apps.gruz.korektorgraficzny.CustomViews.VerticalSeekBar;
import com.apps.gruz.korektorgraficzny.R;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private Equalizer mEqualizer;
    private boolean equalizerInit =false;
    private Intent music_upload;
    private  Timer seekBarTimer;

    @BindView(R.id.album_picture)
    ImageView albumPicture;

    @BindView(R.id.music_play_button)
    ImageView playButton;

    @BindView(R.id.music_stop_button)
    ImageView stopButton;

    @BindView(R.id.music_SseekBar)
    SeekBar musicSeekBar;

    @BindViews({R.id.seekbar_1,R.id.seekbar_2,R.id.seekbar_3,R.id.seekbar_4,R.id.seekbar_5})
    List<VerticalSeekBar> seekBars;


    @Override
    protected void onStop() {
        if(seekBarTimer != null) {
            seekBarTimer.cancel();
            timerTask.cancel();
            seekBarTimer.purge();
        }

        if(mMediaPlayer !=null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if(mEqualizer !=null) {
            mEqualizer.release();
            mEqualizer = null;
        }

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save: save(); return true;
            case R.id.action_exit: System.exit(1);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);


        music_upload = new Intent();
        music_upload.setType("audio/*");
        music_upload.setAction(Intent.ACTION_GET_CONTENT);
        chooseMusic();
    }

    @OnClick(R.id.music_list_button)
    public void chooseMusic(){
        startActivityForResult(music_upload,1);
    }

    private void init(){
        equalizerInit = true;

        seekBarTimer = new Timer();
        seekBarTimer.schedule(timerTask,0,1000);

        int minLevel = mEqualizer.getBandLevelRange()[0];
        int maxLevel = mEqualizer.getBandLevelRange()[1];

        int seekBarCenter = (maxLevel-minLevel)/2;

        int bandValues[] = new int[seekBars.size()];
        bandValues[0] = getIntent().getIntExtra(DatabaseInterface.COLUMN_NAME_BAND_1,seekBarCenter);
        bandValues[1] = getIntent().getIntExtra(DatabaseInterface.COLUMN_NAME_BAND_2,seekBarCenter);
        bandValues[2] = getIntent().getIntExtra(DatabaseInterface.COLUMN_NAME_BAND_3,seekBarCenter);
        bandValues[3] = getIntent().getIntExtra(DatabaseInterface.COLUMN_NAME_BAND_4,seekBarCenter);
        bandValues[4] = getIntent().getIntExtra(DatabaseInterface.COLUMN_NAME_BAND_5,seekBarCenter);

        for(int i = 0 ; i <seekBars.size();i++){
            final short band =(short) i;
            seekBars.get(i).setMax(maxLevel - minLevel);
            seekBars.get(i).setProgress(bandValues[i]);

            seekBars.get(i).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    mEqualizer.setBandLevel(band,(short) (progress + minLevel));
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
    }

    private void save(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.enter_name));
        EditText presetName = new EditText(this);
        alert.setView(presetName);
        alert.setPositiveButton(getResources().getText(R.string.ok_button), (dialogInterface, i) -> {
            DatabaseReader db = new DatabaseReader(Main.this);
            int values[] = new int[seekBars.size()];
            for(int j = 0 ; j < seekBars.size();j++)
                values[j] = seekBars.get(j).getProgress();

            Pdata pdata = new Pdata(presetName.getText().toString(),values);
            db.save(pdata);
        });
        alert.setNegativeButton(getResources().getText(R.string.cancel_button),null);
        alert.show();

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(mMediaPlayer != null && seekBarTimer !=null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mEqualizer.release();
                mMediaPlayer = null;
                mEqualizer = null;

            }

            playButton.setEnabled(false);
            stopButton.setEnabled(true);

            if(resultCode == RESULT_OK){


                Uri uri = data.getData();

                MediaMetadataRetriever metadata = new MediaMetadataRetriever();
                metadata.setDataSource(Main.this,uri);

                String duration = metadata.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                musicSeekBar.setMax(Integer.parseInt(duration)/1000);
                musicSeekBar.setProgress(0);
                mMediaPlayer = MediaPlayer.create(this,uri);
                musicSeekBar.setOnSeekBarChangeListener(musicSeekBarChange);
                byte[] picture = metadata.getEmbeddedPicture();

                if(picture !=null)
                    Glide.with(Main.this).load(picture).asBitmap().into(albumPicture);
                else
                    Glide.with(Main.this).load(R.drawable.ic_album_white_36dp).asBitmap().into(albumPicture);

                mEqualizer = new Equalizer(1,mMediaPlayer.getAudioSessionId());
                mEqualizer.setEnabled(true);
                mMediaPlayer.start();

                if(!equalizerInit)
                    init();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    SeekBar.OnSeekBarChangeListener musicSeekBarChange = new SeekBar.OnSeekBarChangeListener() {
        boolean touched = false;
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(touched)
            mMediaPlayer.seekTo(i*1000);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            touched =true;
                   }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            touched = false;

        }
    };

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            musicSeekBar.setProgress(mMediaPlayer.getCurrentPosition()/1000);
        }
    };

    @OnClick(R.id.music_play_button)
    public void playMusicButton(){
        if(mMediaPlayer != null)
            mMediaPlayer.start();
        playButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    @OnClick(R.id.music_stop_button)
    public void stopMusicButton(){
        if(mMediaPlayer != null)
            mMediaPlayer.pause();

        playButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

}
