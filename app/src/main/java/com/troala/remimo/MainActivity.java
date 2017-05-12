package com.troala.remimo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(new SampleView(this));
    }


    private static class SampleView extends View {
        private Movie mMovie;
        private long mMovieStart;

        public SampleView(Context context){
            super(context);
            //setFocusable(true); // Sets wether the view can be focused

            java.io.InputStream is;
            is = context.getResources().openRawResource(R.drawable.tttbproductive);

            mMovie = Movie.decodeStream(is);

        }

        @Override
        protected void onDraw(Canvas canvas){

            long now = android.os.SystemClock.uptimeMillis(); // We get here the system's time.

            if (mMovieStart == 0){
                mMovieStart = now; // We set the time when we start the "movie"
            }

            if (mMovie != null){
                int duration = mMovie.duration();
                if(duration == 0){
                    duration = 2000; //We set how long we want the animation to run
                }
                int relTime = (int)((now - mMovieStart) % duration);
                mMovie.setTime(relTime);
                mMovie.draw(canvas, getWidth() - mMovie.width(),
                        getHeight() - mMovie.height());

                invalidate(); //todo Don't understand what this does.
            }
        }
    }
}
