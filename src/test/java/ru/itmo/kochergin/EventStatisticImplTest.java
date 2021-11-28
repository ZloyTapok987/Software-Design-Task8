package ru.itmo.kochergin;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class EventStatisticImplTest {
    private SetableClock clock;
    private EventsStatistic eventsStatistic;

    @Before
    public void setUp() {
        clock = new SetableClock(Instant.now());
        eventsStatistic = new EventsStatisticImpl(clock);
    }

    @Test
    public void testStatisticByNonExistingName() {
        assertThat(eventsStatistic.getEventStatisticByName("Event")).isZero();
    }

    @Test
    public void testStatisticByName() {
        eventsStatistic.incEvent("Event1");
        eventsStatistic.incEvent("Event1");
        eventsStatistic.incEvent("Event2");

        assertThat(eventsStatistic.getEventStatisticByName("Event1"))
                .isEqualTo(1.0 / 30);
    }

    @Test
    public void testAllStatistic() {
        eventsStatistic.incEvent("Event1");
        clock.plus(30, ChronoUnit.MINUTES);

        eventsStatistic.incEvent("Event2");
        eventsStatistic.incEvent("Event2");
        clock.plus(30, ChronoUnit.MINUTES);

        eventsStatistic.incEvent("Event3");

        assertThat(eventsStatistic.getAllEventStatistic())
                .containsOnlyKeys("Event2", "Event3");
        assertThat(eventsStatistic.getAllEventStatistic())
                .containsEntry("Event2", 1.0 / 30);
        assertThat(eventsStatistic.getAllEventStatistic())
                .containsEntry("Event3", 1.0 / 60);
    }

}