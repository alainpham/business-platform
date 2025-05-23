package com.codersandbox.bpm;

import java.util.Map;

public class VendorCancellation extends AbstractActivity {

    private static final String PROCESS_NAME = "CancellationProcess";

    public VendorCancellation() {
        setProcess(PROCESS_NAME);
        setName(this.getClass().getSimpleName());
        getNextActivities().put("NotifyCustomer", new NotifyCustomer());
        setDurationMillisecondsAverage(350);
        setDurationMillisecondsStdDeviation(20);
    }
    
    
}
