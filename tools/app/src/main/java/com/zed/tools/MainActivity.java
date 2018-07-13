package com.zed.tools;

import android.content.Intent;
import android.view.View;

//import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zed.tools.R;
//import com.zed.tools.gsy.VideoPlayFragment;
import com.zed.tools.gsy.VideoPlayActivity;
//import com.zed.tools.llsy.JjdxmVideoActivity;
import com.zed.tools.rule.RuleActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreate() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void toGsy(View view) {
        startActivity(new Intent(this, VideoPlayActivity.class));
    }

    public void toJjdxm(View view) {
//        startActivity(new Intent(this, JjdxmVideoActivity.class));
    }

    public void toRule(View view) {
        startActivity(new Intent(this, RuleActivity.class));
    }

}
