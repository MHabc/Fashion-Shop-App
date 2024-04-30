package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Thay thế các ID bên dưới bằng ID thích hợp trong tệp layout của bạn
        ImageView imageView1 = findViewById(R.id.r2uj4u3im6tu);
        ImageView imageView2 = findViewById(R.id.rrysa8j6194j);
        ImageView imageView3 = findViewById(R.id.rak5z9lf9fo);
        ImageView imageView4 = findViewById(R.id.r60mrooqrwyx);
        ImageView imageView5 = findViewById(R.id.rqkwhumjzo1);
        ImageView imageView6 = findViewById(R.id.rxykxc9hpmts);
        ImageView imageView7 = findViewById(R.id.rl1u8m7vae0n);
        ImageView imageView8 = findViewById(R.id.rwo7dvr3o4d);

        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView1);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView2);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView3);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView4);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView5);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView6);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView7);
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(imageView8);
    }
}
