package com.achen.mlhelloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MLMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlmain);

        print("执行 onCreate(Bundle)方法");

        Button mNextButton = (Button) findViewById(R.id.nextButton);
        Button mPreviousButton = (Button) findViewById(R.id.previousButton);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("你点击了nextButton");
                show(R.string.message_next);
            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("你点击了previousButton");
                show(R.string.message_previous);
            }
        });
    }

    void show(int messageId) {
        Toast.makeText(MLMainActivity.this, messageId, Toast.LENGTH_SHORT).show();
    }
    void print(String log) {
        System.out.print(log);
    }
}
