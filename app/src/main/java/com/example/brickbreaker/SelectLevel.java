package com.example.brickbreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class SelectLevel extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectlevel);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }
    public void startGame(View view){
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }
    public void startNormal(View view){
            GameViewNormal gameNormal = new GameViewNormal(this);
            setContentView(gameNormal);
    }
    public void startHard(View view){
        GameViewHard gameHard = new GameViewHard(this);
        setContentView(gameHard);
    }


}
