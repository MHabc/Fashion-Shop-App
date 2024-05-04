package com.example.testdesign1;

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
        ImageView imageView1 = findViewById(R.id.riq98ua6m5s);


        Glide.with(this).load("https://www.pngegg.com/vi/png-ejdbm#google_vignette").into(imageView1);

    }
}
