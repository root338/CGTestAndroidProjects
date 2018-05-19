package com.achen.mlhelloworld;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MLMainActivity extends AppCompatActivity {

    /// 问题列表
    private MLQuestion[] mQuestionList;
    private int mCurrentQuestionIndex = 0;
    private TextView mQuestionView;
    private String[] mUserDidAnswer;
    private int[] mUserLockUpAnswer;
    /// 存储当前回答题目的索引key
    private final String CURRENT_QUESTION_INDEX_KEY = "CurrentQuestionIndexkey";
    /// 用户回答过的题目索引列表 key
    private final String USER_DID_ANSWER_KEY = "UserDidAnswerKey";
    /// 用户查看过答案的题目索引列表 key
    private final String USER_LOCK_UP_ANSWER_KEY = "UserLockUpAnswerKey";
    private Toast mToast;

    private final int REQUEST_CODE_CHEAT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        debugLog("onCreate(Bundle) called");
        setContentView(R.layout.activity_mlmain);

        if (savedInstanceState != null) {
            mCurrentQuestionIndex = savedInstanceState.getInt(CURRENT_QUESTION_INDEX_KEY);
            readUserDidAnswerList(savedInstanceState.getIntegerArrayList(USER_DID_ANSWER_KEY));
        }

        showQuestion(mCurrentQuestionIndex);

        Button mTrueButton = findViewById(R.id.true_button);
        Button mFalseButton = findViewById(R.id.false_button);
        ImageButton mNextButton = findViewById(R.id.next_button);
        ImageButton mPreviousButton = findViewById(R.id.previous_button);
        Button mShowAnswerButton = findViewById(R.id.show_answer_button);

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

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLQuestionResultType resultType = showPreviousQuestion();
                if (resultType != MLQuestionResultType.SUCCESS) {
                    handleError(resultType);
                }
            }
        });

        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mIntent = MLCheatActivity.newIntent(MLMainActivity.this, getCurrentQuestion().getQuestionResult());
                startActivityForResult(mIntent, REQUEST_CODE_CHEAT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) { return;}

            MLQuestion question = getCurrentQuestion();
            if (question.mUserDidAnswerStatus == MLQuestionAnswerStatus.NO_ANSWER) {
                if (MLCheatActivity.wasAnswerShow(data) == true) {
                    question.mUserDidAnswerStatus = MLQuestionAnswerStatus.LOCK_UP_ANSWER;
                }
            }
        }
    }

    // 获取当前的问题
    MLQuestion getCurrentQuestion() {
        return getQuestionList()[mCurrentQuestionIndex];
    }
    // 显示下一个问题
    MLQuestionResultType showNextQuestion() {
        int nextQuestionIndex = mCurrentQuestionIndex + 1;
        MLQuestionResultType result = showQuestion(nextQuestionIndex);
        if (result == MLQuestionResultType.SUCCESS) {
            mCurrentQuestionIndex = nextQuestionIndex;
        }
        return result;
    }
    // 显示上一个问题
    MLQuestionResultType showPreviousQuestion() {
        int previousQuestionIndex = mCurrentQuestionIndex - 1;
        MLQuestionResultType resultType = showQuestion(previousQuestionIndex);
        if (resultType == MLQuestionResultType.SUCCESS) {
            mCurrentQuestionIndex = previousQuestionIndex;
        }
        return resultType;
    }
    // 显示指定索引的问题
    MLQuestionResultType showQuestion(int index) {

        MLQuestion[] questionList = getQuestionList();
        if (questionList.length == 0) {
            // 没有问题了
            return MLQuestionResultType.NULL;
        }

        if (index < 0 ) {
            // 没有上一个问题了
            return MLQuestionResultType.PREVIOUS_IS_NULL;
        }

        if (index < 0 || index >= getQuestionList().length ) {
            // 没有下一个问题了
            return MLQuestionResultType.NEXT_IS_NULL;
        }

        if (mQuestionView == null) {
            mQuestionView = findViewById(R.id.show_question);
        }
        mQuestionView.setText(getQuestionList()[index].getQuestionId());
        return MLQuestionResultType.SUCCESS;
    }
    //获取问题列表
    MLQuestion[] getQuestionList() {
        if (mQuestionList == null) {
            mQuestionList = MLQuestion.createQuestionList();
        }
        return mQuestionList;
    }

    // 显示提示信息
    void show(int messageId) {
        if (mToast == null) {
            mToast = Toast.makeText(MLMainActivity.this, messageId, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.TOP,0, 40);
        }else {
            mToast.setText(messageId);
        }

        mToast.show();
    }

    // 处理回答的结果
    void handleResult(Boolean value) {
        MLQuestion currentQuestion = getQuestionList()[mCurrentQuestionIndex];
        if (currentQuestion.mUserDidAnswerStatus == MLQuestionAnswerStatus.LOCK_UP_ANSWER) {
            show(R.string.error_didLockUpAnswer);
            return;
        }
        if (currentQuestion.mUserDidAnswerStatus != MLQuestionAnswerStatus.NO_ANSWER) {
            show(R.string.error_didAnswer);
            return;
        }
        Boolean isResult = currentQuestion.getQuestionResult() == value;
        int messageId = isResult ? R.string.prompt_correctAnswer : R.string.error_wrongAnswer;
        show(messageId);
        currentQuestion.mUserDidAnswerStatus = isResult ? MLQuestionAnswerStatus.ANSWER_IS_TRUE : MLQuestionAnswerStatus.ANSWER_IS_FALSE;

        if (isResult) {
            showNextQuestion();
        }
    }
    // 处理回答结果的不同类型值
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
            case PREVIOUS_IS_NULL:
                errorId = R.string.error_previousQuestionIsNull;
                break;
            default:
                assert false;
                break;
        }

        show(errorId);
    }

    // debug 日志
    void debugLog(String log) {
        String mMainActivityTag = "MLMainActivity";
        Log.d(mMainActivityTag, log);
    }

    // 创建存储的问题列表
    ArrayList<Integer> saveUserDidAnswerQuestionList() {

        ArrayList<Integer> answerQuestionList = new ArrayList<Integer>();
        MLQuestion[] questionList = getQuestionList();
        for (int index = 0; index < questionList.length; index++) {
            MLQuestion question = questionList[index];
            Integer value = question.questionStatusToIntValue();
            answerQuestionList.add(value);
        }
//        return (Integer[]) answerQuestionList.toArray();
        return answerQuestionList;
    }

    // 读取存储的问题列表
    void readUserDidAnswerList(ArrayList<Integer> userDidAnswer) {
        MLQuestion[] questions = getQuestionList();
        for (int index = 0; index < questions.length; index++) {
            Integer value = userDidAnswer.get(index);
            MLQuestion question = questions[index];
            question.mUserDidAnswerStatus = question.questionIntValueToStatus(value);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_QUESTION_INDEX_KEY, mCurrentQuestionIndex);
        outState.putIntegerArrayList(USER_DID_ANSWER_KEY, saveUserDidAnswerQuestionList());
    }

    @Override
    protected void onStart() {
        super.onStart();
        debugLog("onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugLog("onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        debugLog("onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        debugLog("onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        debugLog("onDestroy() called");
    }
}
