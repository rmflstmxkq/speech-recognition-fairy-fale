package com.example.rmfls.fairy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.rmfls.fairy.R;

public class select extends AppCompatActivity {

    ImageButton fairy1;
    ImageButton fairy2;
    ImageButton fairy3;
    ImageButton fairy4;
    ImageButton left;
    ImageButton right;

    int page = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select);

        fairy1 = (ImageButton) findViewById(R.id.fairy01);
        fairy2 = (ImageButton) findViewById(R.id.fairy02);
        fairy3 = (ImageButton) findViewById(R.id.fairy03);
        fairy4 = (ImageButton) findViewById(R.id.fairy04);
        left = (ImageButton) findViewById(R.id.left);
        right = (ImageButton) findViewById(R.id.right);

        repage();

        fairy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page==1) {
                    Intent intent = new Intent(select.this, com.example.rmfls.fairy.MainActivity.class);
                    intent.putExtra("selectfairy", 1);
                    startActivity(intent);
                }
                if(page==2){
                    no();
                }
            }
        });
        fairy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(select.this, MainActivity.class);
                //startActivity(intent);
                no();
            }
        });
        fairy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(select.this, MainActivity.class);
                //startActivity(intent);
                no();
            }
        });
        fairy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(select.this, MainActivity.class);
                // startActivity(intent);
                no();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page--;
                repage();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                repage();
            }
        });

    }

    void no() {
        Toast.makeText(this, "미구현", Toast.LENGTH_LONG).show();
    }

    void repage() {
        if (page == 1) {
            fairy1.setImageResource(R.drawable.first);
            fairy2.setImageResource(R.drawable.first1);
            fairy3.setImageResource(R.drawable.first2);
            fairy4.setImageResource(R.drawable.second);
            left.setVisibility(View.INVISIBLE);
            right.setVisibility(View.VISIBLE);
        }
        if (page == 2) {
            fairy1.setImageResource(R.drawable.second1);
            fairy2.setImageResource(R.drawable.second2);
            fairy3.setImageResource(R.drawable.second3);
            fairy4.setImageResource(R.drawable.third);
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.INVISIBLE);
        }
    }
}
