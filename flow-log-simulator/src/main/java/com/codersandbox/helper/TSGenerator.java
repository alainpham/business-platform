package com.codersandbox.helper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TSGenerator {

    private int periodHours = 3;
    private int startRampMinutes = 20;
    private int startRampDpmNoise = 5;
    private int startRampDpm = 10;
    private int dpm = 30;
    private int dpmNoise = 13;
    private int endRampMinutes = 30;
    private int endRampDpm = 45;
    private int endRampDpmNoise = 4;

    private List<LocalDateTime> timestampsList = new ArrayList<>();

    public TSGenerator() {
        // Default constructor
        this(3, 20, 5, 10, 30, 13, 30, 45, 4);
    }

    public TSGenerator(int periodHours, int startRampMinutes, int startRampDpmNoise, int startRampDpm, int dpm,
            int dpmNoise, int endRampMinutes, int endRampDpm, int endRampDpmNoise) {
        this.periodHours = periodHours;
        this.startRampMinutes = startRampMinutes;
        this.startRampDpmNoise = startRampDpmNoise;
        this.startRampDpm = startRampDpm;
        this.dpm = dpm;
        this.dpmNoise = dpmNoise;
        this.endRampMinutes = endRampMinutes;
        this.endRampDpm = endRampDpm;
        this.endRampDpmNoise = endRampDpmNoise;
    }

    public int getPeriodHours() {
        return periodHours;
    }

    public void setPeriodHours(int periodHours) {
        this.periodHours = periodHours;
    }

    public int getStartRampMinutes() {
        return startRampMinutes;
    }

    public void setStartRampMinutes(int startRampMinutes) {
        this.startRampMinutes = startRampMinutes;
    }

    public int getStartRampDpmNoise() {
        return startRampDpmNoise;
    }

    public void setStartRampDpmNoise(int startRampDpmNoise) {
        this.startRampDpmNoise = startRampDpmNoise;
    }

    public int getStartRampDpm() {
        return startRampDpm;
    }

    public void setStartRampDpm(int startRampDpm) {
        this.startRampDpm = startRampDpm;
    }

    public int getDpm() {
        return dpm;
    }

    public void setDpm(int dpm) {
        this.dpm = dpm;
    }

    public int getDpmNoise() {
        return dpmNoise;
    }

    public void setDpmNoise(int dpmNoise) {
        this.dpmNoise = dpmNoise;
    }

    public int getEndRampMinutes() {
        return endRampMinutes;
    }

    public void setEndRampMinutes(int endRampMinutes) {
        this.endRampMinutes = endRampMinutes;
    }

    public int getEndRampDpm() {
        return endRampDpm;
    }

    public void setEndRampDpm(int endRampDpm) {
        this.endRampDpm = endRampDpm;
    }

    public int getEndRampDpmNoise() {
        return endRampDpmNoise;
    }

    public void setEndRampDpmNoise(int endRampDpmNoise) {
        this.endRampDpmNoise = endRampDpmNoise;
    }

    public List<LocalDateTime> generateTimestamps() {

        LocalDateTime startTime = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(periodHours);
        LocalDateTime currentTime = startTime;
        timestampsList = new ArrayList<>();
        Random random = new Random();

        /* loop until the end of time each loop generates timestamps for a minute */
        while (currentTime.isBefore(endTime)) {
            int timestampsPerMinute;
            if (currentTime.isBefore(startTime.plusMinutes(startRampMinutes))) {
                timestampsPerMinute = startRampDpm + random.nextInt(startRampDpmNoise);
            } else if (currentTime.isBefore(endTime.minusMinutes(endRampMinutes))) {
                timestampsPerMinute = dpm + random.nextInt(dpmNoise);
            } else {
                timestampsPerMinute = endRampDpm + random.nextInt(endRampDpmNoise);
            }

            long stepsMS = 1000 * 60 / timestampsPerMinute;
            int i = 0;
            for (i = 0; i < timestampsPerMinute; i++) {
                timestampsList.add(currentTime);
                currentTime = currentTime.plus(stepsMS, ChronoUnit.MILLIS);
            }

            /* in case its not rounded up to exact next minute */
            if (currentTime.getSecond() != 0 || currentTime.getNano() != 0) {
                currentTime = currentTime.withSecond(0).withNano(0).plusMinutes(1);
            }

            //System.out.println("Generated " + i + " timestamps for " + currentTime + " stepMs " + stepsMS);
        }
        return timestampsList;
    }

    public List<LocalDateTime> getTimestampsList() {
        return timestampsList;
    }

}
