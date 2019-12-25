package com.vtech.audio.usesoxpipetoconvertvoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity" ;
    CopyElfs ce;
    private final String cmd="sox /sdcard/vtech/a.wav /sdcard/vtech/b.wav pitch 660 tempo 1.25 echo 1.0 0.7 10 0.7 echo 1.0 0.7 12 0.7 echo 1.0 0.88 12 0.7 echo 1.0 0.88 30 0.7 echo 1.0 0.6 60 0.7  echo 0.8 0.88 6 0.4 vol 12dB bass 15";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyActivityManager.getInstance().setCurrentActivity(this);
        Log.e(TAG,"Mission start");
        ProcessUtil processUtil=ProcessUtil.getInstance();
 //       ce= new CopyElfs(getBaseContext());
//        ce.copyAll2Data();
        System.out.println(callElf(cmd));
    }
    public String callElf(String cmd){
        Process p;
        String tmpText;
        String execResult = "";

        try {
            p = Runtime.getRuntime().exec(getFilesDir().getPath()+"/executable/"+cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((tmpText = br.readLine()) != null) {
                execResult += tmpText+"\n";
            }
            Log.e(TAG,execResult+" Misson Complete");
        }catch (IOException e){
            Log.i(TAG,e.toString());
        }
        return execResult;
    }
}
