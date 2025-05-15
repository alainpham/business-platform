package com.codersandbox.bpm;

import java.util.Map;

public class ConfirmNewBooking extends AbstractActivity {

    private static final String PROCESS_NAME = "CancellationProcess";

    public ConfirmNewBooking() {
        setProcess(PROCESS_NAME);
        setName(this.getClass().getSimpleName());
        setDurationMillisecondsAverage(100);
        setDurationMillisecondsStdDeviation(20);
    }

    
}
