package net.roymond.shootingtime;

import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int NUMBER_OF_SHOTS_MAX = 20;
    private final int NUMBER_OF_SHOTS_MIN = 1;
    private final float TIME_BETWEEN_SHOTS_MAX = 10.0f;
    private final float TIME_BETWEEN_SHOTS_MIN = 1.0f;

    GridLayout startMenu;
    GridLayout gamePlaying;
    RelativeLayout gameOver;

    int numberOfShots;
    float timeBetweenShots;
    EditText numShots;
    EditText timeB;
    TextView shotsRemaining;
    TextView shotCalled;
    Random shotCallRand = new Random();
    Button startButton;
    int[] shotFiles;
    String[] shotNames;
    MediaPlayer sound;

    //Remove later
    int howMany = 0;

    public void checkShots(){
        if(numberOfShots > NUMBER_OF_SHOTS_MAX){
            numberOfShots = NUMBER_OF_SHOTS_MAX;
            Toast.makeText(getApplicationContext(),"Max Number of Shots Exceeded\nSetting to Max", Toast.LENGTH_SHORT).show();
        } else if(numberOfShots < NUMBER_OF_SHOTS_MIN){
            numberOfShots = NUMBER_OF_SHOTS_MIN;
            Toast.makeText(getApplicationContext(),"Min Number of Shots Reached\nSetting to Min", Toast.LENGTH_SHORT).show();
        }
        numShots.setText(String.valueOf(numberOfShots));
    }

    public void checkTime(){
        if(timeBetweenShots < TIME_BETWEEN_SHOTS_MIN){
            timeBetweenShots = TIME_BETWEEN_SHOTS_MIN;
            Toast.makeText(getApplicationContext(),"Min Time Reached\nSetting to Min", Toast.LENGTH_SHORT).show();
        } else if(timeBetweenShots > TIME_BETWEEN_SHOTS_MAX){
            timeBetweenShots = TIME_BETWEEN_SHOTS_MAX;
            Toast.makeText(getApplicationContext(),"Max Time Reached\nSetting to Max", Toast.LENGTH_SHORT).show();
        }
        timeB.setText(String.valueOf(timeBetweenShots));
    }


    public void increaseShots(View view){
        numberOfShots = Integer.parseInt(numShots.getText().toString());
        numberOfShots += 1;
        checkShots();
    }
    public void decreaseShots(View view){
        numberOfShots = Integer.parseInt(numShots.getText().toString());
        numberOfShots -= 1;
        checkShots();
    }

    public void increaseTime(View view){
        timeBetweenShots = Float.parseFloat(timeB.getText().toString());
        timeBetweenShots += 0.25f;
        checkTime();
    }
    public void decreaseTime(View view){
        timeBetweenShots = Float.parseFloat(timeB.getText().toString());
        timeBetweenShots -= 0.25f;
        checkTime();
    }

    public void shotCall(){
        int shotChosen = shotCallRand.nextInt(shotNames.length);
        if(sound!=null){ sound.reset(); }
        sound = MediaPlayer.create(this,shotFiles[shotChosen]);
        sound.start();
        shotCalled.setText(shotNames[shotChosen]);
        numberOfShots-=1;
        shotsRemaining.setText("SHOTS LEFT\n" + String.valueOf(numberOfShots));
    }

    public void endGame() {
        gamePlaying.setVisibility(View.INVISIBLE);
        startMenu.setVisibility(View.INVISIBLE);
        gameOver.setVisibility(View.VISIBLE);
        MediaPlayer gameOver = MediaPlayer.create(this, R.raw.gameover);
        gameOver.start();
    }

    public void playGame(){
        long countDownData = (long)(timeBetweenShots*1000 * (numberOfShots)); //Remember! Convert from Seconds to Milliseconds!
        Log.i("Time",String.valueOf(countDownData));
        new CountDownTimer(countDownData+100,(long)(timeBetweenShots*1000)){

            @Override
            public void onTick(long millisUntilFinished) {
                shotCall();
                howMany++;

            }

            @Override
            public void onFinish() {
                endGame();
                Log.i("How Many", String.valueOf(howMany));
            }
        }.start();
    }

    public void startGame(View view){
        timeBetweenShots = Float.parseFloat(timeB.getText().toString());
        numberOfShots = Integer.parseInt(numShots.getText().toString());
        startMenu.setVisibility(View.INVISIBLE);
        gamePlaying.setVisibility(View.VISIBLE);
        shotsRemaining.setText("SHOTS LEFT\n" + String.valueOf(numberOfShots));
        playGame();
    }

    public void playAgain(View view){
        gameOver.setVisibility(View.INVISIBLE);
        startMenu.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Layout Information
        startMenu = (GridLayout)findViewById(R.id.startMenu);
        gamePlaying = (GridLayout)findViewById(R.id.gamePlaying);
        gameOver = (RelativeLayout)findViewById(R.id.gameOver);
        startMenu.setVisibility(View.VISIBLE);
        gameOver.setVisibility(View.INVISIBLE);
        gamePlaying.setVisibility(View.INVISIBLE);

        numShots = (EditText)findViewById(R.id.numberShots);
        timeB = (EditText)findViewById(R.id.timeBetweenShots);
        shotsRemaining = (TextView)findViewById(R.id.shotsRemaining);
        shotCalled = (TextView)findViewById(R.id.shotCalled);
        startButton = (Button)findViewById(R.id.startButton);


        shotNames = new String[]{"Bottom Left","Bottom Right","Five Hole", "Top Left", "Top Right", "Crossbar"};
        shotFiles = new int[]{R.raw.bottomleft, R.raw.bottomright, R.raw.fivehole, R.raw.topleft, R.raw.topright, R.raw.crossbar};

        numShots.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                numberOfShots = Integer.parseInt(v.getText().toString());
                checkShots();
                return false;
            }
        });

        timeB.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                timeBetweenShots = Float.parseFloat(v.getText().toString());
                checkTime();
                return false;
            }
        });

    }

}
