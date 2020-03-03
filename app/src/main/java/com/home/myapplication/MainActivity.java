package com.home.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView Output, Rec;
    Button btn_start, btn_Rec;

    final static int init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    int cur_statue = init;
    int Count = 1;
    long BaseTime;
    long PauseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Output = findViewById(R.id.time_out);
        Rec = findViewById(R.id.record);
        btn_start = findViewById(R.id.btn_start);
        btn_Rec = findViewById(R.id.btn_rec);

        Rec.setMovementMethod(new ScrollingMovementMethod());

    }

    private void scrollBottom(TextView textView){
        int lineTop = textView.getLayout().getLineTop(textView.getLineCount());
        int scrollY = lineTop - textView.getHeight();
        if(scrollY > 0){
            textView.scrollTo(0, scrollY);
        }else{
            textView.scrollTo(0, 0);
        }
    }

    public void myOnClick(View v){
        switch (v.getId()){
            case R.id.btn_start:
                switch (cur_statue){
                    case init:
                        BaseTime = SystemClock.elapsedRealtime();
                        System.out.println(BaseTime);
                        myTimer.sendEmptyMessage(0);
                        btn_start.setText("일시정지");
                        btn_Rec.setEnabled(true);
                        cur_statue = Run;
                        break;
                    case Run:
                        myTimer.removeMessages(0);
                        PauseTime = SystemClock.elapsedRealtime();
                        btn_start.setText("시작");
                        btn_Rec.setText("리셋");
                        cur_statue = Pause;
                        break;
                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        BaseTime += (now - PauseTime);
                        btn_start.setText("일시정지");
                        btn_Rec.setText("기록");
                        cur_statue = Run;
                        break;


                }
                break;
            case  R.id.btn_rec:
                switch (cur_statue){
                    case Run:
                        String str = Rec.getText().toString();
                        str += String.format("%d. %s\n", Count, getTimeOut());
                        Rec.setText(str);
                        Count++;
                        break;

                    case Pause:
                        myTimer.removeMessages(0);

                        btn_start.setText("시작");
                        btn_Rec.setText("기록");
                        Output.setText("00:00:00");

                        cur_statue = init;
                        Count = 1;
                        Rec.setText("");
                        btn_Rec.setEnabled(false);
                        break;
                }
                break;
        }
    }

    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            Output.setText(getTimeOut());

            myTimer.sendEmptyMessage(0);
        }
    };
        String getTimeOut(){
            long now = SystemClock.elapsedRealtime();
            long outTime = now - BaseTime;
            String easy_outTime = String.format("%02d:%02d:%02d", outTime/1000 / 60,
                    (outTime/1000)%60,(outTime%1000)/10);
            return easy_outTime;
        }
}
