/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package libCore;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author LinhTA
 */
public class Util {
    
    public static Map<String,Object> obj2Map(Object obj)
    {
        Map<String, Object> data = new HashMap<String, Object>();
        try
        {
            data = (HashMap<String, Object>) obj;
        }
        catch(Exception e)
        {
            try
            {
                JSONArray a = new JSONArray();
                a= (JSONArray)obj;
            
                for (int i = 0; i < a.size(); i++) {
                    data.put(String.valueOf(i), a.get(i));
                }
            }
            catch(Exception e2)
            {
                
            }
            
        }
        return data;
    }
    
   public static String obj2String(Object obj)
   {
       Gson j = new Gson();
       
       String strData = j.toJson(obj);
       
       return strData;
   }
    
   public static String arrayToString( String... strs )
   {
       String result    =   "";
       for( int i = 0; i < strs.length; ++i ) {
           if( i == strs.length - 1 ) {
               result   +=  strs[i];
           } else { 
               result   +=  strs[i] + ",";
           }
       }
       return result;
   }
}
