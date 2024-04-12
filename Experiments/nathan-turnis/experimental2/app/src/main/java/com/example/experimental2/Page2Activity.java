package com.example.experimental2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Page2Activity extends AppCompatActivity {

    Button page1Btn;
    Button page3Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2_activity);

        page1Btn = findViewById(R.id.page1btn);
        page3Btn = findViewById(R.id.page3btn);

        page1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        page3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page2Activity.this, Page3Activity.class);
                startActivity(intent);
            }
        });
    }
}
