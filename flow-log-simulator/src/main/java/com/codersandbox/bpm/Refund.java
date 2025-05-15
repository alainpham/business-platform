package com.codersandbox.bpm;

import java.util.Map;

public class Refund extends AbstractActivity {

    private static final String PROCESS_NAME = "CancellationProcess";

    public Refund() {
        setProcess(PROCESS_NAME);
        setName(this.getClass().getSimpleName());
        setDurationMillisecondsAverage(150);
        setDurationMillisecondsStdDeviation(20);
    }

    
}
