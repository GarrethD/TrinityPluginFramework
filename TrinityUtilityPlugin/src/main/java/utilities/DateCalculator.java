package utilities;

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
}
