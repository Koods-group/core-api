package ci.koodysgroup.utils.functions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class GlobalFunction {

    public static String generatedOtpCode(int len)
    {
        String s = "";
        int ranNo;

        for (int i = 0; i < len; i++) {
            ranNo = new Random().nextInt(9);
            s = s.concat(Integer.toString(ranNo));
        }
        return s;
    }

    public static boolean compareDate(LocalDateTime date_time)
    {
        LocalDateTime to_day_time = LocalDateTime.now();

        LocalDate to_day = to_day_time.toLocalDate();
        LocalDate date = date_time.toLocalDate();

        return to_day.isEqual(date);
    }

    public static boolean elapsedTime(LocalDateTime from_date_time, int second_elapsed)
    {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(from_date_time, now);
        return duration.toSeconds() >= second_elapsed;
    }
}
