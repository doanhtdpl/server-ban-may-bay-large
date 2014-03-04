/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package share;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *
 * @author LinhTA
 */
public class KeysDefinition {
      
    static String LIST_PEGS = "P_";
    static String LIST_EGGS = "E_";
    static String LIST_FRIENDS = "F_";
    static String LIST_COTES = "C_";
    static  String LIST_BOXS = "B_";
    
    static  String USER = "U_";
    static  String USERS = "Users";
    
    public static byte[] ramdomKey()
    {
        byte[] id= null;
        
        UUID uuid = UUID.randomUUID();
        long longOne = uuid.getMostSignificantBits();
        long longTwo = uuid.getLeastSignificantBits();
        
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(longOne);
        bb.putLong(longTwo);
        id= bb.array();
        
        return id;
    }
      
        public static String ramdomKeySTR()
    {
        String id= "";
        
        id = ramdomKey().toString();
        
        return id;
    }
        
        public static String getKeyUser(String uid)
        {
            return USER +uid;
        }
        
        public static String getKeyUser(int uid)
        {
            return  USER + String.valueOf(uid);
        }
        
        public static String getKeyUserList()
        {
            return USERS;
        }
        
      
        
       
        
        public static String getKey_ListFriends(int uid)
        {
            String key = "";
            
            key = LIST_FRIENDS + String.valueOf(uid);
            
            return key;
        }
        
        public static String getKey_ListFriends(String uid)
        {
            String key = "";
            
            key = LIST_FRIENDS + uid;
            
            return key;
        }
        
        
        
        public static String getKey_ListCotes(int uid)
        {
            String key = "";
            
            key = LIST_COTES + String.valueOf(uid);
            
            return key;
        }
        
        public static String getKey_ListCotes(String uid)
        {
            String key = "";
            
            key = LIST_COTES + uid;
            
            return key;
        }
        
        
}
