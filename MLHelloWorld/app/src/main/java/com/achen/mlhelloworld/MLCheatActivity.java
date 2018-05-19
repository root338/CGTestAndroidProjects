package com.achen.mlhelloworld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MLCheatActivity extends AppCompatActivity {

    private final static String ML_CONSANT_ANSWER_VALUE_TAG = "com.achen.mlhellworld.mlcheatActivity";
    private final static String ML_CONSANT_SHOW_ANSWER_TAG = "com.achen.mlhellworld.mlcheatActivity.show.answer";

    public static Intent newIntent(Context context, Boolean answerValue) {
        Intent intent = new Intent(context, MLCheatActivity.class);
        intent.putExtra(ML_CONSANT_ANSWER_VALUE_TAG, answerValue);
        return intent;
    }

    public static Boolean wasAnswerShow(Intent intent) {
        return intent.getBooleanExtra(ML_CONSANT_SHOW_ANSWER_TAG, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlcheat);

        final Boolean mAnswerValue = getIntent().getBooleanExtra(ML_CONSANT_ANSWER_VALUE_TAG, false);

        final TextView mAnswerTextView = findViewById(R.id.question_answer);
        Button mShowAnswerButton = findViewById(R.id.show_answer_button);

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnswerValue) {
                    mAnswerTextView.setText(R.string.true_button);
                }else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShowResult(true);
            }
        });
    }

    private void setAnswerShowResult(boolean isAnswerShow) {
        Intent intent = new Intent();
        intent.putExtra(ML_CONSANT_SHOW_ANSWER_TAG, isAnswerShow);
        setResult(RESULT_OK, intent);
    }
}
