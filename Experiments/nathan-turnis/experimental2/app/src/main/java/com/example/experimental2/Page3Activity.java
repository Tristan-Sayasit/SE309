package com.example.experimental2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Page3Activity extends AppCompatActivity {

    Button page1Btn;
    Button page2Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_activity);

        page1Btn = findViewById(R.id.page1btn);
        page2Btn = findViewById(R.id.page2btn);

        page1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page3Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        page2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page3Activity.this, Page2Activity.class);
                startActivity(intent);
            }
        });

    }

}
