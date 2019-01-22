package android.com.android_module.tinker;

import android.com.android_module.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

public class TinkerActivity extends AppCompatActivity {

    public static void toActivity(Context context){
        Intent intent=new Intent(context, TinkerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinker);

        findViewById(R.id.btn_loadPatch).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_loadPatch:
                    loadPath();
                    break;
            }
        }
    };
    //加载patch
    private void loadPath(){
        String path=Environment.getExternalStorageDirectory()+ File.separator+"download/tpatch.apk";
        try {
            //TinkerManager.loadPatch(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
