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

public class RuleFileManager {
    
    public static final String HISTORY_FILE_PATH = "db.rul";
    
    public static void writeEntry(List<Rule> rules, String filePath)
    {
        File ruleFile = new File(filePath);
        HashMap<String, List<Rule>> readInfo = new HashMap<>();
        try
        {
//            ruleFile.createNewFile();
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
    
    public static void clearHistory(String filePath)
    {
        File ruleFile = new File(filePath);
        ruleFile.delete();
    }
    
}




