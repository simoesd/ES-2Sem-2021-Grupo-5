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
     *  Writes the specified rule list as a new entry in the specified file.
     *  Technically overwrites the entries already saved in the file, so it starts
     *  by reading the already saved info and appends the new data onto it before writing it.
     *  Every entry is composed by a list of rules and a time stamp. The time stamp is generated
     *  in runtime based on the system time when the writing is executed.
     *  
     *  @param rules list of rules to write into the specified file
     *  @param filePath path to the file where the new data should be written
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
     * @param filePath path to the file whose data should be read
     * @return a map with the info in the file. The keys are the time stamps of each
     * entry (at the time of writing) and the values are the corresponding list of rules
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
     * @param filePath path to the file whose data should be read
     * 
     */
    public static void clearHistory(String filePath)
    {
        File ruleFile = new File(filePath);
        ruleFile.delete();
    }
    
}




