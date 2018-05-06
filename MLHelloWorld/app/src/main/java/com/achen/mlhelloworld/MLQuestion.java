package com.achen.mlhelloworld;

/**
 * Created by achen on 2018/5/6.
 */

public class MLQuestion {
    private int mQuestionId;
    private Boolean mQuestionResult;
    /// 用户回答的是否正确
    public MLQuestionAnswerStatus mUserDidAnswerStatus = MLQuestionAnswerStatus.NO_ANSWER;

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

    public Integer questionStatusToIntValue() {
        switch (mUserDidAnswerStatus) {
            case NO_ANSWER:
                return 1;
            case ANSWER_IS_FALSE:
                return 2;
            case ANSWER_IS_TRUE:
                return 3;
            case LOCK_UP_ANSWER:
                return 4;
            default:
                return 1;
        }
    }

    public MLQuestionAnswerStatus questionIntValueToStatus(Integer value) {
        switch (value) {
            case 4:
                return MLQuestionAnswerStatus.LOCK_UP_ANSWER;
            case 3:
                return MLQuestionAnswerStatus.ANSWER_IS_TRUE;
            case 2:
                return MLQuestionAnswerStatus.ANSWER_IS_FALSE;
            default:
                return MLQuestionAnswerStatus.NO_ANSWER;
        }
    }
}
