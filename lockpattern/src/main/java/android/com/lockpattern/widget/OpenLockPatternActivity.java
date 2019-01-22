package android.com.lockpattern.widget;

import android.com.lockpattern.R;
import android.com.lockpattern.util.ACache;
import android.com.lockpattern.util.LockPatternUtil;
import android.com.lockpattern.widget.LockPatternView;
import android.content.Intent;
import android.net.wifi.aware.IdentityChangedListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class OpenLockPatternActivity extends AppCompatActivity {
    LockPatternView lockPatternView;
    TextView messageTv;
    Button forgetGestureBtn;

    private ACache aCache;
    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lock_pattern);

        lockPatternView=findViewById(R.id.lockPatternView);
        messageTv=findViewById(R.id.messageTv);
        forgetGestureBtn=findViewById(R.id.forgetGestureBtn);
        lockPatternView.setTactileFeedbackEnabled(true);

        forgetGestureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//忘记密码
                forgetGesturePasswrod();
            }
        });
        init();
    }

    private void init() {
        aCache = ACache.get(OpenLockPatternActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(ACache.GESTURE_PASSWORD);
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);

        //没有设置手势
        if(gesturePassword==null){//跳到设置界面，如果没有设置是不让到这个界面的。。。
            Intent intent=new Intent(this,SetLockPatternActivity.class);
            startActivityForResult(intent,ACache.SET_RESULT);
        }
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {
        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Toast.makeText(OpenLockPatternActivity.this,"打开手势成功",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.putExtra("data","open");
        setResult(ACache.OPEN_RESULT,intent);
        finish();
    }

    /**
     * 忘记手势密码
     */
    void forgetGesturePasswrod() {
        aCache.clear();//清除并跳到设置界面
        Intent intent=new Intent(this,SetLockPatternActivity.class);
        startActivityForResult(intent,ACache.SET_RESULT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG","open销毁了...");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==ACache.SET_RESULT&&resultCode==ACache.SET_RESULT){
            Log.e("TAG","设置手势成功");
        }else if(resultCode==ACache.SET_RESULT&&resultCode==ACache.SET_CANCEL_RESULT){
            Log.e("TAG","取消设置手势");
        }
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("data","cancel_open");
        setResult(ACache.OPEN_CANCEL_RESULT,intent);
        finish();
    }
}
