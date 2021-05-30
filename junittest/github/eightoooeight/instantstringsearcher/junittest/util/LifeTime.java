package github.eightoooeight.instantstringsearcher.junittest.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class LifeTime {
    protected AtomicLong _startTimeInMS, _endTimeInMS;

    public LifeTime() {
        _startTimeInMS = new AtomicLong();
        _endTimeInMS = new AtomicLong();
    }

    public LifeTime(long startTimeInMS, long endTimeInMS) {
        _startTimeInMS = new AtomicLong(startTimeInMS);
        _endTimeInMS = new AtomicLong(endTimeInMS);
    }

    public long getStartTimeInMS() { return Optional.ofNullable(_startTimeInMS).map((v) -> v.get()).orElse(Long.valueOf(-1)); }
    public long getEndTimeInMS() { return Optional.ofNullable(_endTimeInMS).map((v) -> v.get()).orElse(Long.valueOf(-1)); }
    public void setStartTimeInMS(long startTimeInMS) { _startTimeInMS.set(startTimeInMS); }
    public void setEndTimeInMS(long endTimeInMS) { _endTimeInMS.set(endTimeInMS); }
}
