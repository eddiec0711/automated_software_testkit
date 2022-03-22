public class Calender {
    public static int daysBetweenTwoDates(int year1, int month1, int day1, int year2, int month2, int day2) {
        int days = 0;
        if (month1 < 1) {
            month1 = 1;
        }

        if (month2 < 1) {
            month2 = 1;
        }

        if (month1 > 12) {
            month1 = 12;
        }

        if (month2 > 12) {
            month2 = 12;
        }

        if (day1 < 1) {
            day1 = 1;
        }

        if (day2 < 1) {
            day2 = 1;
        }

        if (day1 > daysInMonth(month1, year1)) {
            day1 = daysInMonth(month1, year1);
        }

        if (day2 > daysInMonth(month2, year2)) {
            day2 = daysInMonth(month2, year2);
        }

        int year;
        if (year2 < year1 || year2 == year1 && month2 < month1 || year2 == year1 && month2 == month1 && day2 < day1) {
            year = month2;
            month2 = month1;
            month1 = year;
            year = day2;
            day2 = day1;
            day1 = year;
            year = year2;
            year2 = year1;
            year1 = year;
        }


        if (month1 == month2 && year1 == year2) {
            days = day2 - day1;
        } else {
            days = days + (daysInMonth(month1, year1) - day1);
            days += day2;
            if (year1 == year2) {
                for(year = month1 + 1; year < month2; ++year) {
                    days += daysInMonth(year, year1);
                }
            } else {
                int month;
                for(month = month1 + 1; month <= 12; ++month) {
                    days += daysInMonth(month, year1);
                }

                for(month = 1; month < month2; ++month) {
                    days += daysInMonth(month, year2);
                }

                for(year = year1 + 1; year < year2; ++year) {
                    days += 365;
                    if (isLeapYear(year)) {
                        ++days;
                    }
                }
            }
        }

        return days;
    }

    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    public static int daysInMonth(int month, int year) {
        int[] daysInMonthNonLeapYear = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return month == 2 && isLeapYear(year) ? 29 : daysInMonthNonLeapYear[month - 1];
    }
}
