package ru.itmo.kochergin;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EventsStatisticImpl implements EventsStatistic {
    private final Clock clock;
    private final Map<String, List<Instant>> events = new HashMap<>();

    @Override
    public void incEvent(String name) {
        if (!events.containsKey(name)) {
            events.put(name, new ArrayList<>());
        }

        events.get(name).add(clock.instant());
    }

    @Override
    public double getEventStatisticByName(String name) {
        return getEventStatisticByName(name, true);
    }


    private double getEventStatisticByName(String name, boolean removeOld) {
        if (removeOld) {
            removeOldEvents();
        }

        if (!events.containsKey(name)) {
            return 0;
        }
        double MINUTES_IN_HOUR = 60;
        return events.get(name).size() / MINUTES_IN_HOUR;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        removeOldEvents();
        return events.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getEventStatisticByName(entry.getKey(), false)
                ));
    }

    @Override
    public void printStatistic() {
        Map<String, Double> statistic = getAllEventStatistic();

        for (String name : statistic.keySet()) {
            System.out.println(String.format("RPM for %s - %f", name, statistic.get(name)));
        }
    }

    private void removeOldEvents() {
        Instant hourAgo = clock.instant().minus(1, ChronoUnit.HOURS);

        for (String name : events.keySet()) {
            List<Instant> newInstants = events.get(name).stream()
                    .filter(instant -> instant.isAfter(hourAgo))
                    .collect(Collectors.toList());

            events.put(name, newInstants);
        }

        events.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}