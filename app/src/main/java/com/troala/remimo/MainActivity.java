package com.troala.remimo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Cache;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(new SampleView(this));

        callNetwork();
    }

    /**
     * Display Animated GIF
     */
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

    /**
     *
     */
    private void callNetwork(){

        // V1 - stringRequest

        //1MB cache. With cache, it's quicker
        Cache cache = new DiskBasedCache(this.getCacheDir(), 1024 * 1024);
        //Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        //Instantiate the RequestQueue with cache and network
        RequestQueue queue = new RequestQueue(cache, network);
        //start the queue
        queue.start();

        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,
                        "http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this,
                                        "Receive" + response,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(
                                        MainActivity.this,
                                        "ERROR !!" + error,
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                );

        queue.add(stringRequest);


        // V2 - JsonObject
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.GET,
                        "http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC",
                        null,
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
    }
}
