package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class RestaurantService {

    private final Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private final Map<String, LongAdder> stat = new ConcurrentHashMap<>();

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        stat.compute(restaurantName, (name, adder) -> {
            if (nonNull(adder)) {
                adder.increment();
                return adder;
            } else {
                LongAdder longAdder = new LongAdder();
                longAdder.increment();
                return longAdder;
            }
        });
    }

    public Set<String> printStat() {
        return stat.entrySet().stream()
            .map(e -> String.format("%s - %s", e.getKey(), e.getValue().longValue()))
            .collect(Collectors.toSet());
    }
}
