package com.example.brickbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverHard extends AppCompatActivity {
    TextView hPoint;
    ImageView hightest;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_hard);

        hightest = findViewById(R.id.hight);
        hPoint= findViewById(R.id.point);

        int points = getIntent().getExtras().getInt("points");
        String level = "HARD";

        if (points == 700) {
            hightest.setVisibility(View.VISIBLE);
        }

        hPoint.setText(String.valueOf(points));
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.insertGameData(points, level);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOverHard.this, SelectLevel.class);
        startActivity(intent);
        finish();
    }


    public void exit(View view) {
        finish();
    }
}
