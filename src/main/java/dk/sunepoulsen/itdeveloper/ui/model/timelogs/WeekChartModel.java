package dk.sunepoulsen.itdeveloper.ui.model.timelogs;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@Data
public class WeekChartModel {
    private Map<DayOfWeek, Double> workingNorms;
    private Map<DayOfWeek, Double> workedHours;

    public WeekChartModel() {
        this.workingNorms = new HashMap<>();
        this.workedHours = new HashMap<>();
    }

    public void putWorkingNorm(DayOfWeek dayOfWeek, Double norm) {
        this.workingNorms.put(dayOfWeek, norm);
    }

    public void putWorkedHours(DayOfWeek dayOfWeek, Double hours) {
        this.workedHours.put(dayOfWeek, hours);
    }

    public Map<DayOfWeek, Double> accumulateWorkingNorms() {
        return accumulateMap(this.workingNorms);
    }

    public Map<DayOfWeek, Double> accumulateWorkedHours() {
        return accumulateMap(this.workedHours);
    }

    public Map<DayOfWeek, Double> accumulateMap(Map<DayOfWeek, Double> map) {
        Map<DayOfWeek, Double> result = new HashMap<>();

        Double accumulatedValue = 0.0;
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (map.containsKey(dayOfWeek)) {
                accumulatedValue = accumulatedValue + map.get(dayOfWeek);
            }

            result.put(dayOfWeek, accumulatedValue);
        }

        return result;
    }
}
