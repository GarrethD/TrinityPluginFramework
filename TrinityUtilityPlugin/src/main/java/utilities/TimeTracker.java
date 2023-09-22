package utilities;

import java.time.Duration;
import java.time.Instant;

public class TimeTracker {

    private static Instant startTime;
    private static Instant endTime;

    public static void setStartTime() {
        startTime = Instant.now();
    }

    public static void setEndTime() {
        endTime = Instant.now();
    }

    /**
     * Calculates the duration between the start and end time.
     *
     * @return the duration between the start and end time
     */
    public static Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**
     * Calculates the remaining duration of a process based on a given baseline time.
     *
     * @param baselineTime the baseline time in seconds
     * @return the remaining duration of the process
     */
    public static Duration getTimeLeft(int baselineTime) {
        Duration duration = getDuration();
        return Duration.ofSeconds(30).minus(duration.minusSeconds(baselineTime));
    }

}
