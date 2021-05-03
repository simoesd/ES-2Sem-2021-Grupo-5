package rules;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RuleFileManager {
    
    public static final String HISTORY_FILE_PATH = "teste.txt";
    
    public static void writeEntry(List<Rule> rules)
    {
        File ruleFile = new File(HISTORY_FILE_PATH);
        try
        {
            FileOutputStream fileWriter = new FileOutputStream(ruleFile);
            ruleFile.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(fileWriter);

            Date date = new Date(System.currentTimeMillis());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            
            RuleHistoryEntry newEntry = new RuleHistoryEntry(dateFormat.format(date), rules);
            oos.writeObject(newEntry);
        } catch (IOException e) {
            
        } finally {
        }
    }
    
    public static HashMap<String, List<Rule>> readRules()
    {
        File ruleFile = new File(HISTORY_FILE_PATH);
        HashMap<String, List<Rule>> readInfo = new HashMap<>();
        InputStream fileReader = FileInputStream.nullInputStream();
        try
        {
            ruleFile.createNewFile();
            fileReader = new FileInputStream(ruleFile);
            ObjectInputStream ois = new ObjectInputStream(fileReader);
            for (;;)
            {
                RuleHistoryEntry entry = (RuleHistoryEntry)ois.readObject();
                readInfo.put(entry.timeStamp, entry.rules);
            }
            
        } catch (EOFException e1) {
            fileReader.close();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            return readInfo;
        }
    }
    
    public static void clearHistory()
    {
        File ruleFile = new File(HISTORY_FILE_PATH);
        ruleFile.delete();
    }
    
}


class RuleHistoryEntry implements Serializable{
    String timeStamp;
    List<Rule> rules;
    

    RuleHistoryEntry(String timeStamp, List<Rule> rules)
    {
        this.timeStamp = timeStamp;
        this.rules = rules;
    }
}



