package com.codersandbox.bpm;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AbstractActivity {

    private String process;
    private String name;
    private Map<String, AbstractActivity> nextActivities = new HashMap<String, AbstractActivity>();
    private int durationMillisecondsAverage = 230;
    private int durationMillisecondsStdDeviation = 20;
    private Random random = new Random();
    private int failureRatePercentage = 8;

    protected static final String[] RANDOM_ERRORS = {
            "Database connection timeout",
            "Failed to authenticate",
            "Disk quota exceeded",
            "Invalid input format"
    };

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, AbstractActivity> getNextActivities() {
        return nextActivities;
    }

    public void setNextActivities(Map<String, AbstractActivity> nextActivities) {
        this.nextActivities = nextActivities;
    }

    public int getDurationMillisecondsAverage() {
        return durationMillisecondsAverage;
    }

    public void setDurationMillisecondsAverage(int durationMillisecondsAverage) {
        this.durationMillisecondsAverage = durationMillisecondsAverage;
    }

    public int getDurationMillisecondsStdDeviation() {
        return durationMillisecondsStdDeviation;
    }

    public void setDurationMillisecondsStdDeviation(int durationMillisecondsStdDeviation) {
        this.durationMillisecondsStdDeviation = durationMillisecondsStdDeviation;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getFailureRatePercentage() {
        return failureRatePercentage;
    }

    public void setFailureRatePercentage(int failureRatePercentage) {
        this.failureRatePercentage = failureRatePercentage;
    }

    public long simulateDuration() throws InterruptedException {
        double gaussian = random.nextGaussian();
        long duration = Math.round(durationMillisecondsAverage + gaussian * durationMillisecondsStdDeviation);
        if (duration < 0) {
            duration = 0;
        }
        Thread.sleep(duration);
        return duration;
    }

    public boolean simulateFailure() {
        boolean isError = getRandom().nextInt(100) < failureRatePercentage;
        return isError;
    }

    public void execute(String instanceId, Map<String, String> businessContext) throws Exception {
        long duration = simulateDuration();
        boolean isError = simulateFailure();

        logActivity(instanceId, businessContext, duration, isError);
        if (!isError) {
            for (Map.Entry<String, AbstractActivity> entry : nextActivities.entrySet()) {
                AbstractActivity nextActivity = entry.getValue();
                nextActivity.execute(instanceId, businessContext);
            }
        }

    }

    public void logActivity(String instanceId, Map<String, String> context, long duration, boolean isError) {
        String logPattern = "ts=%s process=%s activity=%s id=%s status=%s duration=%s msg=%s\n";
        String message = "\"" + getName() + " : executed with success\"";
        String status = isError ? "ERROR" : "OK";
        if (isError) {
            message = "\"" + getName() + " : " + RANDOM_ERRORS[getRandom().nextInt(RANDOM_ERRORS.length)];
        }
        System.out.printf(logPattern, System.currentTimeMillis(), getProcess(), getName(), instanceId, status, duration,
                message);
    }

}
