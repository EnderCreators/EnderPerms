package com.endercreators.perms;

class RunThread extends Thread {
    public RunThread(String str) {
        super(str);

    }

    public void run(Runnable runnable) {
        runnable.run();
    }
}