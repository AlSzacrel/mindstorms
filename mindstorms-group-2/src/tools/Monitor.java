package tools;

import lejos.nxt.comm.LCPBTResponder;

public class Monitor {

    public static void main(String[] args) {
        LCPBTResponder lcpThread = new LCPBTResponder();
        lcpThread.setDaemon(true);
        lcpThread.start();
    }
}
