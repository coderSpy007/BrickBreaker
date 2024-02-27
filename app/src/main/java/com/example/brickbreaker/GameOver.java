package com.example.brickbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView tvPoints;
    ImageView ivNewHighest;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        ivNewHighest = findViewById(R.id.ivNewHeighest);
        tvPoints = findViewById(R.id.tvPoints);

        int points = getIntent().getExtras().getInt("points");
        String level = "EASY";

        if (points == 240) {
            ivNewHighest.setVisibility(View.VISIBLE);
        }

        tvPoints.setText(String.valueOf(points));
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.insertGameData(points, level);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, SelectLevel.class);
        startActivity(intent);
        finish();
    }


    public void exit(View view) {
        finish();
    }


}
