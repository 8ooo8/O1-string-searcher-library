package github.eightoooeight.instantstringsearcher.junittest.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LifeTime
{
    protected AtomicLong startTimeInMS, endTimeInMS;

    public LifeTime()
    {
        startTimeInMS = new AtomicLong();
        endTimeInMS = new AtomicLong();
    }

    public LifeTime(long startTimeInMS, long endTimeInMS)
    {
        startTimeInMS = new AtomicLong(startTimeInMS);
        endTimeInMS = new AtomicLong(endTimeInMS);
    }

    public long getStartTimeInMS()
    {
        return Optional.ofNullable(startTimeInMS).map((v) -> v.get()).orElse(Long.valueOf(-1));
    }

    public long getEndTimeInMS()
    {
        return Optional.ofNullable(endTimeInMS).map((v) -> v.get()).orElse(Long.valueOf(-1));
    }

    public void setStartTimeInMS(long startTimeInMS)
    {
        startTimeInMS.set(startTimeInMS);
    }

    public void setEndTimeInMS(long endTimeInMS)
    {
        endTimeInMS.set(endTimeInMS);
    }
}
