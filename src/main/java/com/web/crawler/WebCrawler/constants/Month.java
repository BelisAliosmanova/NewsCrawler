package com.web.crawler.WebCrawler.constants;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Тъй като във vesti.bg датите са написани
// така: 25 април 2024, вместо 25.04.2024
// ни се налага да направим енъм, който превръща месеца в число
public enum Month {
    JANUARY(1, "януари", "january"),
    FEBRUARY(2, "февруари", "february"),
    MARCH(3, "март", "march"),
    APRIL(4, "април", "april"),
    MAY(5, "май", "may"),
    JUNE(6, "юни", "june"),
    JULY(7, "юли", "july"),
    AUGUST(8, "август", "august"),
    SEPTEMBER(9, "септември", "september"),
    OCTOBER(10, "остомври", "october"),
    NOVEMBER(11, "ноември", "november"),
    DECEMBER(12, "декември", "december"),
    ;

    private static final Map<Integer, Month> monthMap = new HashMap<>();

    static {
        for (Month month : values()) {
            monthMap.put(month.getMonthRow(), month);
        }
    }
    private final int monthRow;
    private final List<String> monthNames;

    Month(int monthRow, String... monthNames) {
        this.monthRow = monthRow;
        this.monthNames = Arrays.asList(monthNames);
    }

    public Integer getMonthRow() {
        return monthRow;
    }

    private List<String> getMonthNames() {
        return monthNames;
    }

    public static Month getMonth(String month) {
        for (Map.Entry<Integer, Month> entry : monthMap.entrySet()) {
            if (entry.getValue().getMonthNames().contains(month.toLowerCase())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
