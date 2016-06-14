package com.selim.testproject;

/**
 * Created by mohammadselimmiah on 13/06/2016.
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HandlerRunnableActivity extends Activity {

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Bitmap mBitmap;
    private int mDelay = 1000;
    private final Handler handler = new Handler();
    private boolean isImageLoaded =false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent obtainIntent = this.getIntent();
            String gotString = obtainIntent.getStringExtra("string");
            int gotInt = obtainIntent.getIntExtra("intValue", -1);
        if(gotInt!=-1 || gotString!=null)
            Toast.makeText(this, "Got String: " + gotString + "\n Got Int: " + gotInt, Toast.LENGTH_LONG).show();

        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        final Button loadButton = (Button) findViewById(R.id.loadButton);
        loadButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Thread(new LoadIconTask(R.drawable.painter)).start();
            }
        });

        final Button otherButton = (Button) findViewById(R.id.otherButton);
        otherButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HandlerRunnableActivity.this, isImageLoaded?"Image Loaded Successfully":"I'm Working ...", Toast.LENGTH_SHORT).show();
                //Toast.makeText(HandlerRunnableActivity.this,"I am Working ...", Toast.LENGTH_LONG).show();
            }
        });

        Button otherActivity = (Button) findViewById(R.id.other_activity);
        otherActivity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent otherActivityIntent = new Intent("com.selim.testproject.HandlerMessagesActivity");
                Intent otherActivityIntent = new Intent(HandlerRunnableActivity.this, HandlerMessagesActivity.class);
                otherActivityIntent.putExtra("string","This is a String value");
                otherActivityIntent.putExtra("intValue",1000);
                startActivity(otherActivityIntent);
                finish();
            }
        });
    }

    private class LoadIconTask implements Runnable {
        int resId;

        LoadIconTask(int resId) {
            this.resId = resId;
        }

        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            mBitmap = BitmapFactory.decodeResource(getResources(), resId);

            // Simulating long-running operation

            for (int i = 1; i < 11; i++) {
                sleep();
                final int step = i; //Final variable can be assigned once for each declaration
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(step * 10);
                    }
                });
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(mBitmap);
                }
            });

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isImageLoaded = true;
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            });
        }
    }

    private void sleep() {
        try {
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
