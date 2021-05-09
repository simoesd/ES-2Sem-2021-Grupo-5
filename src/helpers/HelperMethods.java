package helpers;

import java.util.Map;
import java.util.SortedMap;
import com.codahale.metrics.Counter;

/**
 * Class that contains utility methods. Does not need instantiation as every method is static.
 */
 
public class HelperMethods {
    
    
    /** Alternative to the default Java {@code Boolean#parseBoolean(String)} method.
     * Returns {@code true} if the specified string's content is equal to "true". This check is case insensitive.
     * Returns {@code false} if the specified string's content is equal to "false". This check is case insensitive.
     * Throws an exception if the strings content is not equivalent to either "true" or "false".
     * 
     * @param stringToParse string whose content will be parsed for a logic value.
     * @return {@code true} if the specified string's content is equal to "true". {@code false} if the specified string's content is equal to "false". This check is case insensitive.
     * @throws IllegalArgumentException if the provided string's content is not equivalent to "true" or "false".
     * 
     * @see Boolean#parseBoolean(String)
     */
    
    public static boolean customParseBoolean(String stringToParse) throws IllegalArgumentException {
        if (stringToParse.equalsIgnoreCase("true"))
            return true;
        if (stringToParse.equalsIgnoreCase("false"))
            return false;
        throw new IllegalArgumentException();
    }
    
    /**
     * Case insensitive alternative to the regular {@code Map#get(Object)} for maps with String keys.
     * Implemented to make checking for rules/metrics with the same name more user-friendly, while still allowing custom capitalization
     * for table headers when the rule/metric name is shown in the GUI.
     * 
     * @param map map whose content will be checked
     * @param key key which will be searched for in the map, to find the corresponding value. This search is case insensitive.
     * @return the first value whose key corresponds to the specified key. There may exist multiple in the map (with keys with differing capitalizations). {@code null} if there isn't a matching key in the specified map.
     * 
     * @see Map#get(Object)
     */
    public static <V> V getCaseInsensitive(Map<String, V> map, String key)
    {
        for (Map.Entry<String, V> entry: map.entrySet())
        {
            if (entry.getKey().equalsIgnoreCase(key))
                return entry.getValue();
        }
        return null;
    }
    
    /**
     * Case insensitive alternative to the regular {@code Map#containsKey(Object)} for maps with String keys.
     * Returns {@code true} if the specified map contains a matching key to the specified key, ignoring capitalization.
     *
     * @param map map whose content will be checked
     * @param key key whose presence in this list is to be tested
     * @return {@code true} if this map contains a mapping for the specified key. This check is case insensitive.
     * 
     */
    public static <V> boolean containsKeyCaseInsensitive(Map<String, V> map, String key) 
    {
        for (Map.Entry<String, V> entry: map.entrySet())
        {   
            if (entry.getKey().equalsIgnoreCase(key))
                return true;
        }
        return false;
    }
	
	public static boolean compareSortedMapCounters(SortedMap<String, Counter> objects,SortedMap<String, Counter> objects2){
		for(String s: objects.keySet()){
			if (objects.get(s).getCount()!=objects2.get(s).getCount()) {
				return false;
			}
		}
		return true;
	}
}
