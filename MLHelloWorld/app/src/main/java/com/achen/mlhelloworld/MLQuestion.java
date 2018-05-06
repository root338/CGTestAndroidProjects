package com.achen.mlhelloworld;

/**
 * Created by achen on 2018/5/6.
 */

public class MLQuestion {
    private int mQuestionId;
    private Boolean mQuestionResult;

    static MLQuestion[] createQuestionList() {

        MLQuestion[] questionList = new MLQuestion[]{
                new MLQuestion(R.string.question_001, true),
                new MLQuestion(R.string.question_002, true),
                new MLQuestion(R.string.question_003, false),
                new MLQuestion(R.string.question_004, false),
                new MLQuestion(R.string.question_005, true),
                new MLQuestion(R.string.question_006, false)
        };

        return questionList;
    }

    MLQuestion(int questionId, Boolean result) {
        mQuestionId = questionId;
        mQuestionResult = result;
    }

    public int getQuestionId() {
        return mQuestionId;
    }

    public Boolean getQuestionResult() {
        return mQuestionResult;
    }
}
