package com.zed.tools;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

//import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.zed.tools.R;
//import com.zed.tools.gsy.VideoPlayFragment;
import com.zed.tools.calendar.CalendarActivity;
import com.zed.tools.gsy.VideoPlayActivity;
//import com.zed.tools.llsy.JjdxmVideoActivity;
import com.zed.tools.rule.RuleActivity;

import permissions.dispatcher.PermissionUtils;

public class MainActivity extends BaseActivity {

    final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreate() {
        boolean hadPermission = PermissionUtils.hasSelfPermissions(this, permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hadPermission) {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 1110);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean sdPermissionResult = PermissionUtils.verifyPermissions(grantResults);
        if (!sdPermissionResult) {
            Toast.makeText(this, "没获取到sd卡权限，无法播放本地视频哦", Toast.LENGTH_LONG).show();
        }
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

    public void toDDCalendar(View view) {
        startActivity(new Intent(this, CalendarActivity.class));
    }

}
