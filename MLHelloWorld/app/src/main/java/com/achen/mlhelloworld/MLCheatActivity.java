package com.achen.mlhelloworld;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
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
        final Button mShowAnswerButton = findViewById(R.id.show_answer_button);
        final TextView mAppVersionTextView = findViewById(R.id.app_version);

        Integer mAPILevelValue = new Integer(Build.VERSION.SDK_INT);
        mAppVersionTextView.setText("API Level " + mAPILevelValue.toString() );

        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAnswerValue) {
                    mAnswerTextView.setText(R.string.true_button);
                }else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShowResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setAnswerShowResult(boolean isAnswerShow) {
        Intent intent = new Intent();
        intent.putExtra(ML_CONSANT_SHOW_ANSWER_TAG, isAnswerShow);
        setResult(RESULT_OK, intent);
    }
}
