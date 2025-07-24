package utils.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeOperator {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        String formatted = now.format(formatter);
        return formatted;
    }

    public boolean isStillUseProgramGeneratedString(String isUserEnteredStr) {
        boolean isEnter = true;
        try  {
            TemporalAccessor parse = formatter.parse(isUserEnteredStr);
        } catch (Exception e) {
            //  这里忽略异常，用异常来判断是否 要更新(不能解析说明用户改过了)
            isEnter = false;
        }
        return isEnter;
    }
}
