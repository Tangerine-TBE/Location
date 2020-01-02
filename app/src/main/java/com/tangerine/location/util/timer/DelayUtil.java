package com.tangerine.location.util.timer;

import java.util.TimerTask;

public class DelayUtil extends TimerTask {
    private ITimerListener listener;
    public DelayUtil (ITimerListener listener){
        this.listener = listener;
    }

    @Override
    public void run() {
        if (listener != null){
            listener.onTimer();
        }
    }
}
