package com.codersandbox.bpm;

import java.util.Map;

public class CustomerOfferChoice extends AbstractActivity {

    private static final String PROCESS_NAME = "CancellationProcess";
    private int rejectionRate;
    public CustomerOfferChoice() {
        setProcess(PROCESS_NAME);
        setName(this.getClass().getSimpleName());
        getNextActivities().put("Refund", new Refund());
        getNextActivities().put("ConfirmNewBooking", new ConfirmNewBooking());
        setDurationMillisecondsAverage(100);
        setDurationMillisecondsStdDeviation(20);
        rejectionRate = 10; // 10% chance of rejection
    }

    @Override
    public void execute(String instanceId, Map<String, String> businessContext) throws Exception {
        long duration = simulateDuration();
        boolean isError = simulateFailure();

        logActivity(instanceId, businessContext, duration, isError);
        if (!isError) {
            if (Math.random() * 100 < rejectionRate) {
                getNextActivities().get("Refund").execute(instanceId, businessContext);
            } else {
                getNextActivities().get("ConfirmNewBooking").execute(instanceId, businessContext);
            }
        }

    }
    
}
