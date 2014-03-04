/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities.time;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author LinhTA
 */
public class UtilTime {
    
    public static long getTimeNow()
    {
        long timenow = 0;
        
        timenow = Calendar.getInstance(TimeZone.getDefault()).getTime().getTime();
        
        return timenow;
    }
    
    public static int getDaysDiff(long before, long after)
    {
        int days = 0;
        Date dateBefore = new Date(before);
        Date dateAfter = new Date(after);
        
        long diff = after  - before;
       
       
        long day =86400000;
        if(diff < day)
        {
            if(dateBefore.getDay() != dateAfter.getDay())
                days = 1;
            else
                days = 0;
        }
        else
        {
            days =(int) (diff/day);
            
            long d = diff%day;
            long time = (dateBefore.getHours()*3600 + dateBefore.getMinutes()*60 + dateBefore.getSeconds())*1000;
            d = d + time;
            
            days = days + (int)(d/day);
        }
        
        return days;
    }
    
    public static Long parseLong(int second)
    {
        Long time = new Long(100*second);
        
        return time;
    }
            
    
    public static void main(String[] args) {
        System.out.print( getDaysDiff(getTimeNow(),  getTimeNow()+86000000*12 ));
       
        
    }
}
