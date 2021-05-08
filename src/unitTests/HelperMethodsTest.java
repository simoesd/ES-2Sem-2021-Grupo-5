package unitTests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import helpers.HelperMethods;

class HelperMethodsTest {

    @Test
    void testCustomParseBoolean() {
        assertAll("Custom Parse Boolean",
                () -> {assertEquals(true, HelperMethods.customParseBoolean("true"));},
                () -> {assertEquals(true, HelperMethods.customParseBoolean("true"));},
                () -> {assertEquals(false, HelperMethods.customParseBoolean("false"));},
                () -> {assertEquals(false, HelperMethods.customParseBoolean("False"));},
                () -> {assertThrows(IllegalArgumentException.class, () -> HelperMethods.customParseBoolean("not a boolean"));}
                );
    }

    @Test
    void testGetCaseInsensitive() {
        HashMap<String, Integer> map = new HashMap<>();
        String key1 = "sameCaseKey";
        int val1 = 0;
        String key2 = "differentCaseKey";
        int val2 = 1;
        String key3 = "notPartOfTheMap";
        
        map.put(key1, val1);
        map.put(key2, val2);
        assertAll("Get Case Insensitive Test",
                () -> {assertEquals(val1, HelperMethods.getCaseInsensitive(map, key1));},
                () -> {assertEquals(val2, HelperMethods.getCaseInsensitive(map, key2));},
                () -> {assertEquals(null, HelperMethods.getCaseInsensitive(map, key3));}
        );
    }

    @Test
    void testContainsKeyCaseInsensitive() {
        HashMap<String, Integer> map = new HashMap<>();
        String key1 = "sameCaseKey";
        int val1 = 0;
        String key2 = "differentCaseKey";
        int val2 = 1;
        String key3 = "notPartOfTheMap";
        
        map.put(key1, val1);
        map.put(key2, val2);
        assertAll("Get Case Insensitive Test",
                () -> {assertTrue(HelperMethods.containsKeyCaseInsensitive(map, key1));},
                () -> {assertTrue(HelperMethods.containsKeyCaseInsensitive(map, key2));},
                () -> {assertFalse(HelperMethods.containsKeyCaseInsensitive(map, key3));}
        );
    }

}
