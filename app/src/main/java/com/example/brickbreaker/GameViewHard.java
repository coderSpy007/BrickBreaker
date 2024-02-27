package com.example.brickbreaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameViewHard extends View {

    Context context;
    float ballX, ballY;
    Velocity velocity = new Velocity(70, 50);
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    Paint brickPaint = new Paint();
    float TEXT_SIZE = 120;
    float paddleX, paddleY;
    float oldX, oldPaddleX;
    int points = 0;
    int life = 3;
    Bitmap ball, paddle;
    int dWidth, dHeight;
    int ballWidth, ballHeight;
    MediaPlayer mpHIt, mpMiss, mpBreak;
    Random random;
    Brick[] bricks = new Brick[70];
    int numBricks = 0;
    int brokenBricks = 0;
    boolean gameOver = false;
    Bitmap background;


    public GameViewHard(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(),R.drawable.hardbg);
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ballhard);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddlee);
        handler = new Handler();
        velocity = new Velocity(70, 90);
        runnable = new Runnable() {
            @Override
            public void run() {

                invalidate();
            }
        };
        mpHIt = MediaPlayer.create(context, R.raw.hit);
        mpMiss = MediaPlayer.create(context, R.raw.miss);
        mpBreak = MediaPlayer.create(context, R.raw.breaking);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        healthPaint.setColor(Color.GREEN);
        brickPaint.setColor(Color.argb(255, 249, 129, 0));
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        random = new Random();
        ballX = random.nextInt(dWidth - 50);
        ballY  = dHeight / 3;
        paddleY = (dHeight * 4) / 5;
        paddleX = dWidth / 2 - paddle.getWidth() / 2;
        ballWidth = ball.getWidth();
        ballHeight = ball.getHeight();
        createBricks();
    }

    private void createBricks() {
        int brickWidth = dWidth / 14;
        int brickHeight = dHeight / 22;
        for (int column = 0; column < 14; column++) {
            for (int row = 0; row < 5; row++) {
                bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                numBricks++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background,0,0,null);
        ballX += velocity.getX();
        ballY += velocity.getY();

        // Ball collisions with screen edges
        if ((ballX >= dWidth - ball.getWidth()) || ballX <= 0) {
            velocity.setX(velocity.getX() * -1);
        }
        if (ballY <= 0) {
            velocity.setY(velocity.getY() * -1);
        }

        // Ball misses the paddle
        if (ballY > paddleY + paddle.getHeight()) {
            ballX = 1 + random.nextInt(dWidth - ball.getWidth() - 1);
            ballY = dHeight / 3;
            if (mpMiss != null) {
                mpMiss.start();
            }
            velocity.setX(xVelocity());
            velocity.setY(90);
            life--;
            if (life == 0) {
                gameOver = true;
                launchGameOver();
            }
        }

        // Ball collides with paddle
        if ((ballX + ball.getWidth() >= paddleX)
                && (ballX <= paddleX + paddle.getWidth())
                && (ballY + ball.getHeight() >= paddleY)
                && (ballY <= paddleY + paddle.getHeight())) {
            if (mpHIt != null) {
                mpHIt.start();
            }
            velocity.setX(velocity.getX() + 1);
            velocity.setY((velocity.getY() + 1) * -1);
        }

        // Ball collides with bricks
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                float brickLeft = bricks[i].column * bricks[i].width;
                float brickTop = bricks[i].row * bricks[i].height;
                float brickRight = brickLeft + bricks[i].width;
                float brickBottom = brickTop + bricks[i].height;

                if (ballX + ballWidth >= brickLeft
                        && ballX <= brickRight
                        && ballY + ballHeight >= brickTop
                        && ballY <= brickBottom) {

                    if (mpBreak != null) {
                        mpBreak.start();
                    }
                    velocity.setY((velocity.getY() + 1) * -1);
                    bricks[i].setInvisible();
                    points += 10;
                    brokenBricks++;
                    if (brokenBricks == 70) {
                        launchGameOver();
                    }
                }
            }
        }

        // Drawing ball, paddle, and bricks
        canvas.drawBitmap(ball, ballX, ballY, null);
        canvas.drawBitmap(paddle, paddleX, paddleY, null);
        for (int i = 0; i < numBricks; i++) {
            if (bricks[i].getVisibility()) {
                canvas.drawRect(bricks[i].column * bricks[i].width + 1, bricks[i].row * bricks[i].height + 1, bricks[i].column * bricks[i].width + bricks[i].width - 1, bricks[i].row * bricks[i].height + bricks[i].height - 1, brickPaint);
            }
        }

        // Drawing points and health bar
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);

        // Checking if the game is over
        if (brokenBricks == numBricks) {
            gameOver = true;
        }

        // Continuing the game loop
        if (!gameOver) {
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= paddleY){
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldPaddleX = paddleX;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newPaddleX = oldPaddleX - shift;
                if (newPaddleX <= 0)
                    paddleX = 0;
                else if (newPaddleX >= dWidth - paddle.getWidth())
                    paddleX = dWidth - paddle.getWidth();
                else
                    paddleX = newPaddleX;
            }
        }
        return true;
    }

    private void launchGameOver(){
        handler.removeCallbacksAndMessages(null);
        Intent intent = new Intent(context, GameOverHard.class);
        intent.putExtra("points", points);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
    private int xVelocity(){
        int[] values = {-35, -30, -25, 25, 30, 35, 38, 70};
        int index = random.nextInt(6);
        return values[index];
    }
}
