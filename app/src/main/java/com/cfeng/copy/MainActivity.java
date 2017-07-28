package com.cfeng.copy;

import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcfeng.copy.CopyCallBack;
import com.zcfeng.copy.FileCopyTool;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Handler handler=new Handler();
    TextView tv;
    EditText from;
    EditText to;
    ProgressBar fb;
    ProgressBar tb;
    ScrollView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.table);
        from= (EditText) findViewById(R.id.from);
        to= (EditText) findViewById(R.id.to);
        fb= (ProgressBar) findViewById(R.id.filepro);
        tb= (ProgressBar) findViewById(R.id.totalpro);
        sv= (ScrollView) findViewById(R.id.sv);
    }

    public void start(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                File f= Environment.getExternalStorageDirectory();
                FileCopyTool.
                        create().setFrom(f.getAbsolutePath()+"//"+from.getText())
                        .setTo(f.getAbsolutePath()+"//"+to.getText())
                        .setProgressCallBack(new CopyCallBack() {

                            @Override
                            public void showFilesSize(double arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void ShowTotalProcess(final int arg0) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        tv.append("\nTotal："+arg0);
                                        tb.setProgress(arg0);
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });

                            }

                            @Override
                            public void ShowStata(int arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void ShowProcess(final String arg0,final float arg1) {
                                // TODO Auto-generated method stub
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        tv.append("\ncopy "+arg0+" "+arg1+"%");
                                        fb.setDrawingCacheBackgroundColor(Color.YELLOW);
                                        fb.setProgress((int) arg1);
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }

                            @Override
                            public void ShowCheckProcess(final String filename, final float percent) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        tv.append("\ncheck "+filename+" "+percent+"%");
                                        fb.setDrawingCacheBackgroundColor(Color.GREEN);
                                        fb.setProgress((int) percent);
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });

                            }

                            @Override
                            public void ShowCheckResult(final String filename, final Boolean res) {
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        tv.append("\ncheck "+filename+" "+ (res?"检验无误":"文件错误"));
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });

                            }

                            @Override
                            public void ShowFileName(String arg0) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void OnFinish() {
                                // TODO Auto-generated method stub
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        tv.append("\n完成");
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }

                            @Override
                            public void OnError(final String arg0) {
                                // TODO Auto-generated method stub
                                handler.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        tv.append("\n error: "+arg0);
                                        sv.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });

                            }
                        }).start();
            }
        }).start();
    }
}
