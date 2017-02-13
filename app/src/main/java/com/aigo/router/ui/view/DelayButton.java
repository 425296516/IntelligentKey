package com.aigo.router.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

import com.aigo.router.R;

/**
 * Created by zhangcirui on 2017/2/4.
 */

public class DelayButton extends Button {
    public interface OnDelayListener {
        public void onStartDelay(int second);

        public void onRun(int second);

        public void onFinish();
    }


    private boolean mIsDelaying = false;
    private DelayButton.OnDelayListener mListener;


    public DelayButton(Context context) {
        super(context);
    }

    public DelayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DelayButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDelayListener(DelayButton.OnDelayListener listener) {
        mListener = listener;
    }

    public boolean isDelay(){
        return mIsDelaying;
    }

    public void delay(final int second) {
        if (mListener != null) {
            mListener.onStartDelay(second);
        }
        mIsDelaying = true;
        DelayButton.this.setEnabled(false);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what < 0) {
                    stop();
                } else {
                    if (mListener != null)
                        mListener.onRun(msg.what);
                    (DelayButton.this).setTextColor(getResources().getColor(R.color.color_808080));
                    (DelayButton.this).setText(msg.what + "秒后重发");
                }
            }
        };
        new Thread(new Runnable() {
            int reduceTime = second;

            @Override
            public void run() {
                while (reduceTime > 0 && mIsDelaying) {
                    handler.sendEmptyMessage(reduceTime);
                    reduceTime--;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(-1);
            }
        }).start();
    }

    public void stop() {
        mIsDelaying = false;
        (DelayButton.this).setTextColor(getResources().getColor(R.color.color_0091ff));
        (DelayButton.this).setText("重新发送验证码");
        DelayButton.this.setEnabled(true);
        if (mListener != null)
            mListener.onFinish();
    }
}
