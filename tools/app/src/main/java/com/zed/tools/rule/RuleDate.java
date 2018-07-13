package com.zed.tools.rule;

public class RuleDate {
    protected String mRuleDate;
    protected long mRuleTime;

    public RuleDate() {
    }

    public RuleDate(String mRuleDate, long mRuleTime) {
        this.mRuleDate = mRuleDate;
        this.mRuleTime = mRuleTime;
    }

    public String getDate() {
        return mRuleDate;
    }

    public long getTime() {
        return mRuleTime;
    }
}
