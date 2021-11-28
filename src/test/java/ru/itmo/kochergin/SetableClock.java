package ru.itmo.kochergin;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;

public class SetableClock extends Clock {
    private Instant now;

    public SetableClock(Instant now) {
        this.now = now;
    }

    @Override
    public ZoneId getZone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Clock withZone(ZoneId zoneId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return now;
    }

    public void setNow(Instant now) {
        this.now = now;
    }

    public void plus(long value, TemporalUnit unit) {
        setNow(now.plus(value, unit));
    }
}