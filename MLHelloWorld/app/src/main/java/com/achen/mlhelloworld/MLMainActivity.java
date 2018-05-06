package com.achen.mlhelloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MLMainActivity extends AppCompatActivity {

    private MLQuestion[] mQuestionList;
    private int mCurrentQuestionIndex = 0;
    private TextView mQuestionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mlmain);

        showQuestion(mCurrentQuestionIndex);

        Button mTrueButton = findViewById(R.id.true_button);
        Button mFalseButton = findViewById(R.id.false_button);
        Button mNextButton = findViewById(R.id.next_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResult(true);
            }
        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleResult(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLQuestionResultType resultType = showNextQuestion();
                if (resultType != MLQuestionResultType.SUCCESS) {
                    handleError(resultType);
                }
            }
        });
    }

    MLQuestionResultType showNextQuestion() {
        int nextQuestionIndex = mCurrentQuestionIndex + 1;
        MLQuestionResultType result = showQuestion(nextQuestionIndex);
        if (result == MLQuestionResultType.SUCCESS) {
            mCurrentQuestionIndex = nextQuestionIndex;
        }
        return result;
    }

    MLQuestionResultType showQuestion(int index) {

        MLQuestion[] questionList = getQuestionList();
        if (questionList.length == 0) {
            // 没有问题了
            show(R.string.error_questionCountIsZero);
            return MLQuestionResultType.NULL;
        }

        if (index < 0 || index >= getQuestionList().length ) {
            // 没有下一个问题了
            show(R.string.error_nextQuestionIsNull);
            return MLQuestionResultType.NEXT_IS_NULL;
        }

        if (mQuestionView == null) {
            mQuestionView = findViewById(R.id.show_question);
        }
        mQuestionView.setText(getQuestionList()[index].getQuestionId());
        return MLQuestionResultType.SUCCESS;
    }

    MLQuestion[] getQuestionList() {
        if (mQuestionList == null) {
            mQuestionList = MLQuestion.createQuestionList();
        }
        return mQuestionList;
    }

    void show(int messageId) {
        Toast mToast = Toast.makeText(MLMainActivity.this, messageId, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP,0, 40);
        mToast.show();
    }
    void print(String log) {
        System.out.print(log);
    }

    void handleResult(Boolean value) {
        Boolean isResult = getQuestionList()[mCurrentQuestionIndex].getQuestionResult() == value;
        int messageId = isResult ? R.string.prompt_correctAnswer : R.string.error_wrongAnswer;
        show(messageId);
        if (isResult) {
            showNextQuestion();
        }
    }

    void handleError(MLQuestionResultType resultType) {
        if (resultType == MLQuestionResultType.SUCCESS) {
            return;
        }
        int errorId = 0;
        switch (resultType) {
            case NULL:
                errorId = R.string.error_questionCountIsZero;
                break;
            case NEXT_IS_NULL:
                errorId = R.string.error_nextQuestionIsNull;
                break;
            default:
                assert false;
                break;
        }

        show(errorId);
    }
}
