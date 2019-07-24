package com.seakleang.testremotecamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.seakleang.testremotecamera.ws.ImageService;
import com.seakleang.testremotecamera.ws.ServiceGenerator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Spinner spinner;
    private ImageService imageService;
    private Bitmap oldBitmap, newBitmap;
    private String[] items = new String[16];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        spinner = findViewById(R.id.spinner);
        for (int i = 1; i <=16; i++) {
            items[i - 1] = i + "";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
        imageService = ServiceGenerator.createService(ImageService.class);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadImage();
                handler.postDelayed(this, 200);
            }
        };

        handler.postDelayed(runnable, 200);

    }

    private void loadImage() {

        imageService.getImage(spinner.getSelectedItem().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("getImage", "success");
                    if (response.body() != null) {
                        newBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        Glide.with(MainActivity.this)
                                .load(newBitmap)
                                .placeholder(new BitmapDrawable(getResources(), oldBitmap))
                                .into(imageView);
                        oldBitmap = newBitmap;
                    } else {
                        Log.d("getImage", "body null");
                    }
                } else {
                    Log.d("getImage", "fail");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("getImage", "error");
            }
        });

    }
}
