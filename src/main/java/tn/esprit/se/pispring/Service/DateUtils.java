package tn.esprit.se.pispring.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date getEndDateOfMonth(Date startDate) {
        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        YearMonth yearMonth = YearMonth.from(localStartDate);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        return Date.from(endOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
