package com.huce.application.config;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Slf4j
@UtilityClass
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static final String YYYY_PT = "yyyy";
    private static final String YYYYmm_PT = "yyyyMM";

    public static Date convertStringToDate(String strDate, Boolean isFullDateTime) {
        if (strDate == null || "".equals(strDate)) {
            return null;
        }
        if (isFullDateTime) {
            if (strDate.length() != Constants.DATETIME_FORMAT.length()) {
                return null;
            }
        } else {
            if (strDate.length() != Constants.DATE_FORMAT.length()) {
                return null;
            }
        }
        try {
            Date date;
            SimpleDateFormat simpleDateFormat;
            if (isFullDateTime) {
                simpleDateFormat = new SimpleDateFormat(Constants.DATETIME_FORMAT);
            } else {
                simpleDateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
            }
            simpleDateFormat.setLenient(false);
            date = simpleDateFormat.parse(strDate);
            return date;
        } catch (ParseException e) {
            logger.error("Loi! convertStringToDate: " + e.getMessage());
        }
        return null;
    }

    public static Instant convertToInstant(String strDate, boolean isFullDateTime) {
        Date date = convertStringToDate(strDate, isFullDateTime);
        return date == null ? null : date.toInstant();
    }
}
