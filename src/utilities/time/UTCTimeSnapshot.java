/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang.time.DateUtils;

/**
 *
 * @author quocpt
 */
public class UTCTimeSnapshot
{

    private long currentTime;

    public UTCTimeSnapshot()
    {
	takeSnapshot();
    }

    final public void takeSnapshot()
    {
	Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	Date d = cal.getTime();
	currentTime = d.getTime();
    }

    public void addSeconds(int seconds)
    {
	currentTime += seconds * 1000;
    }

    public long getMilisecondsLong()
    {
	return currentTime;
    }

    public int getSeconds()
    {
	return (int) (currentTime / 1000);
    }

    public long getSecondsLong()
    {
	return currentTime / 1000;
    }

    public int getMinutes()
    {
	return (int) (currentTime / 6000);
    }

    public long getMinutesLong()
    {
	return currentTime / 6000;
    }

    public int getHours()
    {
	return (int) (currentTime / 3600000);
    }

    public long getHoursLong()
    {
	return currentTime / 3600000;
    }

    public int getDates()
    {
	return (int) (currentTime / 86400000);
    }

    public long getDatesLong()
    {
	return currentTime / 86400000;
    }

    /**
     *
     * @return Trim current seconds to 00:00 of that date
     */
    public int getTruncateSeconds()
    {
	Date now = new Date(currentTime);
	Date trunc = DateUtils.truncate(now, Calendar.DATE);
	return (int) (trunc.getTime() / 1000);
    }

    /**
     *
     * @return Trim current seconds to 00:00 of that date
     */
    public long getTruncateSecondsLong()
    {
	Date d = DateUtils.truncate(currentTime, Calendar.DATE);
	return d.getTime() / 1000;
    }

    /**
     * See SimpleDateFormat. Example: [yyyy-MM-dd HH:mm:ss]
     */
    public static String getCurrentDate(String format)
    {
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat(format);
	return sdf.format(cal.getTime());
    }

    // Trim current seconds to 00:00 of that date
    public static int truncateSecond(int seconds)
    {
	Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	cal.setTimeInMillis((long) seconds * 1000l);
	Date d = DateUtils.truncate(cal.getTime(), Calendar.DATE);
	return (int) (d.getTime() / 1000);
    }
    
    public static void main(String[] args) {
       
	Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	Date d = cal.getTime();
	
        Calendar cal1 = Calendar.getInstance(TimeZone.getDefault());
         cal1.add(Calendar.HOUR, 1);
        Date d1 = cal1.getTime();
        
        System.out.print(DateUtils.isSameDay(d, d1));
    }
    
}
