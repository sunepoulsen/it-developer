package dk.sunepoulsen.itdeveloper.formatter;

import lombok.Data;

@Data
public class FlexFormatter {
    private final double weeklyNorm;
    private final double dailyNorm;
    private boolean formatWeeks;
    private boolean formatDays;
    private boolean formatHours;
    private boolean formatMinutes;

    public FlexFormatter(double weeklyNorm, double dailyNorm) {
        this.weeklyNorm = weeklyNorm;
        this.dailyNorm = dailyNorm;
        this.formatWeeks = true;
        this.formatDays = true;
        this.formatHours = true;
        this.formatMinutes = true;
    }

    public String format(double balance) {
        StringBuilder result = new StringBuilder();

        double remainBalance = Math.abs(balance);
        if (balance < 0.0) {
            result.append("-");
        }

        if (formatWeeks && remainBalance >= weeklyNorm) {
            int weeks = Double.valueOf(Math.floor(remainBalance / weeklyNorm)).intValue();
            result.append(weeks);
            result.append("w ");

            remainBalance = remainBalance - (weeks * weeklyNorm);
        }

        if (formatDays && remainBalance >= dailyNorm) {
            int days = Double.valueOf(Math.floor(remainBalance / dailyNorm)).intValue();
            result.append(days);
            result.append("d ");

            remainBalance = remainBalance - (days * dailyNorm);
        }

        if (formatHours && remainBalance >= 1.0) {
            int hours = Double.valueOf(Math.floor(remainBalance)).intValue();
            result.append(hours);
            result.append("h ");

            remainBalance = remainBalance - Math.floor(remainBalance);
        }

        if (formatMinutes) {
            int minutes = Double.valueOf(remainBalance * 60.0).intValue();
            result.append(minutes);
            result.append("m");
        }

        return result.toString().trim();
    }
}
