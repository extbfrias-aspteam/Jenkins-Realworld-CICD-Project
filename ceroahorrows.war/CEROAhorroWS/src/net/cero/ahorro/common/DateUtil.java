package net.cero.ahorro.common;


import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

@Log4j2
public class DateUtil {
    public static final String YYYY_MM_dd = "yyyy-MM-dd";
    public static final String YYYY_MM_dd_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static Date getDateWithInitialDay(String referenceDate) {
        if(Objects.nonNull(referenceDate)){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_dd);
                java.util.Date date = sdf.parse(referenceDate);
                return new Date(date.getTime());
            }catch (Exception e){

                return null;
            }
        }
        return null;
    }

    public static Date getSqlDate(String referenceDate){
        return getSqlDateFromFormat(referenceDate, YYYY_MM_dd_HH_MM_SS);
    }
    public static Date getSqlDateFromFormat(String referenceDate, String format){
        if(Objects.nonNull(referenceDate)){
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                java.util.Date date = sdf.parse(referenceDate);
                return new Date(date.getTime());
            }catch (Exception e){
                log.error("Error al parsear la fecha {} {} \n {}", referenceDate, format, e);
                return null;
            }
        }
        return null;
    }

    public static Timestamp dateToSqlTimestamp(String reference, boolean endOfDay) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(reference);

            if(endOfDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR_OF_DAY, 23);
                calendar.add(Calendar.MINUTE, 59);

                return new Timestamp(calendar.getTime().getTime());
            }
            return new Timestamp(date.getTime());
        }catch (Exception e) {
            log.error("Error al obtener timestamp {}" , e);
        }
        return null;
    }

    public static boolean compareDaysBetweenDates(String initialRerefence, String endingReference) {
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(initialRerefence),
                    LocalDate.parse(endingReference));

        return daysBetween > 90;
    }

    public static Date getDateFromStringFormat(String referenceDate){
        try{
            SimpleDateFormat sdf= new SimpleDateFormat(YYYY_MM_dd);
            java.util.Date date = sdf.parse(referenceDate);

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            return new Date(calendar.getTime().getTime());
        }catch (Exception e){
            log.error("Error al parsear la fecha {}", e);
            return null;
        }
    }

    public static String getStringDateFromSqlDate(Date date){
        if(Objects.isNull(date))
            return "";

        return date.toString();
    }
}
