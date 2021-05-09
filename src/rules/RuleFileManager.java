package rules;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

    /**
     * Class that deals with the rule history methods. Does not need instantiation as every method is static.
     */
public class RuleFileManager {
    
    /**
     * Path for the default rule history file.
     */
    public static final String HISTORY_FILE_PATH = "db.rul";
    
    /**
     * Save the specified rule list in the history file.
     * This method will read the history file by calling the {@link RuleFileManager#readRules(String)} method, 
     * and add a new entry to the read HashMap with the current date as a key and the list of rules as a value.
     * Finally, it writes the new HashMap to the file.
     * @param rules List of rules to write to history file.
     * @param filePath to Rule history file.
     */
    public static void writeEntry(List<Rule> rules, String filePath)
    {
        File ruleFile = new File(filePath);
        HashMap<String, List<Rule>> readInfo = new HashMap<>();
        try
        {
            readInfo = readRules(filePath);
            FileOutputStream fileWriter = new FileOutputStream(ruleFile);
            ObjectOutputStream oos = new ObjectOutputStream(fileWriter);

            Date date = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            readInfo.put(dateFormat.format(date), rules);
            oos.writeObject(readInfo);
            fileWriter.close();
            oos.close();
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Reads the first object in the specified file and returns it as a {@code HashMap<String, List<Rule>>}
     * 
     * @param filePath to Rule history file.
     * HashMap containing all saved rules. Each entry is composed by a key with the date the rule was saved and a value with a list of rules.
     * 
     */
    public static HashMap<String, List<Rule>> readRules(String filePath)
    {
        File ruleFile = new File(filePath);
        HashMap<String, List<Rule>> readInfo = new HashMap<>();
        try
        {
            if (!ruleFile.createNewFile()) {
                InputStream fileReader = FileInputStream.nullInputStream();
                try {
                    fileReader = new FileInputStream(ruleFile);
                    ObjectInputStream ois = new ObjectInputStream(fileReader);
                    Object obj = ois.readObject();
                    readInfo = (HashMap<String, List<Rule>>) obj;
                    ois.close();
                } catch (EOFException e1) {
                    
                } finally { 
                    fileReader.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            
        } finally {
            return readInfo;
        }
    }
    
    
    /**
     * Deletes the specified file.
     * 
     *@param filePath to Rule history file.
     * 
     */
    public static void clearHistory(String filePath)
    {
        File ruleFile = new File(filePath);
        ruleFile.delete();
    }
    
}




