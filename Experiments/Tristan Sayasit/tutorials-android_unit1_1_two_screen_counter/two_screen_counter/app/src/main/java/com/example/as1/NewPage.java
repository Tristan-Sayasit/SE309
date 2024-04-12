package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewPage extends AppCompatActivity {

    Button button2;
    TextView newHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpage);

        Bundle extras = getIntent().getExtras();

        button2 = findViewById(R.id.button2);
        newHeader = findViewById(R.id.textView2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(NewPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
        newHeader.setText(extras.getString("womp"));
    }

}
