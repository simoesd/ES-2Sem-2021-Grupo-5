package helpers;

import java.util.Map;

public class HelperMethods {
    
    public static boolean customParseBoolean(String stringToParse) throws IllegalArgumentException {
        if (stringToParse.equalsIgnoreCase("true"))
            return true;
        if (stringToParse.equalsIgnoreCase("false"))
            return false;
        throw new IllegalArgumentException();
    }
    
    public static <V> V getCaseInsensitive(Map<String, V> map, String key)
    {
        for (Map.Entry<String, V> entry: map.entrySet())
        {
            if (entry.getKey().equalsIgnoreCase(key))
                return entry.getValue();
        }
        return null;
    }
    
    public static <V> boolean containsKeyCaseInsensitive(Map<String, V> map, String key) 
    {
        for (Map.Entry<String, V> entry: map.entrySet())
        {   
            if (entry.getKey().equalsIgnoreCase(key))
                return true;
        }
        return false;
    }
    
}
