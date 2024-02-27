package com.example.brickbreaker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TextView historyTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        historyTextView = findViewById(R.id.historyTextView);
        databaseHelper = new DatabaseHelper(this);

        displayGameHistory();
    }

    private void displayGameHistory() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_POINTS, DatabaseHelper.COLUMN_LEVEL, DatabaseHelper.COLUMN_TIMESTAMP};

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int pointsColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_POINTS);
                int levelColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LEVEL);
                int timestampColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIMESTAMP);

                StringBuilder historyBuilder = new StringBuilder();

                do {
                    if (pointsColumnIndex != -1 && levelColumnIndex != -1 && timestampColumnIndex != -1) {
                        int points = cursor.getInt(pointsColumnIndex);
                        String level = cursor.getString(levelColumnIndex);
                        String timestamp = cursor.getString(timestampColumnIndex);

                        historyBuilder.append(points)
                                .append("                  ").append(level)
                                .append("             ").append(timestamp)
                                .append("\n\n");
                    } else {
                        Log.e("HistoryActivity", "Invalid column index found");
                    }
                } while (cursor.moveToNext());

                cursor.close();

                historyTextView.setText(historyBuilder.toString());
            } else {
                historyTextView.setText("No game history available,  Return to go break brick now!!");
            }
        }
    }
    public void returnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void resetClick(View view) {
        resetData();
        Toast.makeText(this, "History reset successfully", Toast.LENGTH_SHORT).show();
    }
    private void resetData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.resetData();
    }

}
