package com.vtech.audio.usesoxpipetoconvertvoice;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ProcessUtil {
    private static final String cmd="/data/data/"+getAppProcessName(MyActivityManager.getInstance().getCurrentActivity())+"/sox"+" /storage/sdcard0/a.wav /storage/stdcard0/b.wav -t waveaudio 0 silence 1 0.1 1% reverse silence 1 0.1 1% reverse pitch 660 tempo 1.25 echo 1.0 0.7 10 0.7 echo 1.0 0.7 12 0.7 echo 1.0 0.88 12 0.7 echo 1.0 0.88 30 0.7 echo 1.0 0.6 60 0.7  echo 0.8 0.88 6 0.4 vol 12dB  bass 15\n";
    private static final String TAG ="ProcessUtil" ;

    static{
        processUtil=new ProcessUtil(MyActivityManager.getInstance().getCurrentActivity());
        Init();
    };
    private static Context ct;
    private static ProcessUtil processUtil;
    private ProcessUtil(Context context){
        ct=context;
    }
    public static ProcessUtil getInstance()
    {
        return  processUtil;
    }
    private static byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
    private static void writeBytesToFile(byte[] bs,String fileName) throws IOException{
        File file = new File(
                //Environment.getExternalStorageDirectory(),
                fileName);
        OutputStream out = new FileOutputStream(file);
        InputStream is = new ByteArrayInputStream(bs);
        byte[] buff = new byte[1024];
        int len = 0;
        while((len=is.read(buff))!=-1){
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }
    private static void copyFile(InputStream in, OutputStream out){
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (IOException e){
            Log.e(TAG, "Failed to read/write asset file: ", e);
        }
    };
    private static void Init()
    {
          try {
                String pushPath=ct.getFilesDir().getPath()+"/executable/";
                InputStream input=MyActivityManager.getInstance().getCurrentActivity().getAssets().open("armeabi-v7a/sox");
                File outFile = new File(pushPath+"sox");
                FileOutputStream  out = new FileOutputStream(outFile);
                copyFile(input,out);
                assert(outFile.setExecutable(true));
                input.close();
                input = null;
                out.flush();
                out.close();
                out = null;

//            byte[] result=read(input);
//            Process process = Runtime.getRuntime().exec("/system/bin/chmod 774 /storage/sdcard0/sox");
//            writeBytesToFile(result,"/data/data/"+getAppProcessName(MyActivityManager.getInstance().getCurrentActivity())+"/sox");
//            Process myProcess=Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }
}
