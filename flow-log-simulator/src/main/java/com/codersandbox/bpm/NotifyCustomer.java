package com.codersandbox.bpm;

import java.util.Map;

public class NotifyCustomer extends AbstractActivity {

    private static final String PROCESS_NAME = "CancellationProcess";

    public NotifyCustomer() {
        setProcess(PROCESS_NAME);
        setName(this.getClass().getSimpleName());
        getNextActivities().put("CustomerChoice", new CustomerOfferChoice());
        setDurationMillisecondsAverage(100);
        setDurationMillisecondsStdDeviation(20);
    }

    
}
