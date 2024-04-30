package utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateCalculator {
    /**
     * This method is used to get a new date by adding or subtracting the specified number of days from the current date.
     *
     * @param daysToAddOrSubtract the number of days to add or subtract
     * @param datePattern the pattern for formatting the resulting date
     * @return the resulting date as a string formatted according to the specified pattern
     */
    public static String getDate(int daysToAddOrSubtract,String datePattern) {
        //Get today's date
        Date currentDate = new Date();
        //Create a Calendar object
        Calendar calendar = Calendar.getInstance();
        //Set the calendar to today's date
        calendar.setTime(currentDate);
        //Add or subtract the specified number of days
        calendar.add(Calendar.DAY_OF_YEAR, daysToAddOrSubtract);
        //Get the new date after adding or subtracting days
        Date newDate = calendar.getTime();
        //Create a date formatter for the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        //Format the new date and return the result
        return dateFormat.format(newDate);
    }
    public static String getDayOfWeek(String dateString, String datePattern) throws ParseException {
        // Parse the input date string into a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        Date date = dateFormat.parse(dateString);

        // Set the date into a Calendar object
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Determine the day of the week
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Mapping the day number to the name of the day
        switch(dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                // In case of an unexpected value, return an empty string or an error message
                return "Invalid day";
        }
    }
}
