package com.achen.mlhelloworld;

/**
 * Created by achen on 2018/5/6.
 */
// 用户回答题目的状态
public enum MLQuestionAnswerStatus {
//    没有答题
    NO_ANSWER,
//    回答错误
    ANSWER_IS_FALSE,
//    回答正确
    ANSWER_IS_TRUE,
//    查看了答案
    LOCK_UP_ANSWER,
}
