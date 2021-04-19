package rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import reader.Line;

public class Rule {
    public String ruleName;
    
    public String metric1;
    public String thresholdOp1;
    public int thresholdValue1;
    public String logicOp;
    public String metric2;
    public String thresholdOp2;
    public int thresholdValue2;

    public Rule(String ruleName, String metric1, String thresholdOp1, int thresholdValue1) {
        this.ruleName = ruleName;
        this.metric1 = metric1;
        this.thresholdOp1 = thresholdOp1;
        this.thresholdValue1 = thresholdValue1;
    }
    
    public static void main(String[] args) {
        Line line = new Line();
        line.metrics.put("NOM_class", "29");
        line.metrics.put("LOC_class", "1371");
        line.metrics.put("WMC_class", "328");
        line.metrics.put("LOC_method", "3");
        line.metrics.put("CYCLO_method", "1");
        
        ArrayList<Rule> rules = new ArrayList<>();
        
        Rule long_method = new Rule("long_method", "LOC_method", "gt", 50, "AND", "CYCLO_method", "gt", 10);
        Rule god_class = new Rule("god_class", "WMC_class", "gt", 50, "OR", "NOM_class", "gt", 10);
        
        rules.add(long_method);
        rules.add(god_class);
        
        for (Rule rule: rules) {
            boolean ruleValue = rule.evaluateLine(line);
            line.metrics.put(rule.ruleName, Boolean.toString(ruleValue));
        }
        Set<String> keys = line.metrics.keySet();
        for (Map.Entry<String, String> entry: line.metrics.entrySet())
        {
            System.out.println(entry.getKey() + ": "+ entry.getValue());
        }
    }

    public Rule(String ruleName, String metric1, String thresholdOp1, int thresholdValue1, String logicOp, String metric2, String thresholdOp2, int thresholdValue2) {   
        this.ruleName = ruleName;
        this.metric1 = metric1;
        this.thresholdOp1 = thresholdOp1;
        this.thresholdValue1 = thresholdValue1;
        this.logicOp = logicOp;
        this.metric2 = metric2;
        this.thresholdOp2 =  thresholdOp2;
        this.thresholdValue2 = thresholdValue2;
    }
    
    public boolean evaluateLine(Line line) {
        HashMap<String, String> metrics = line.getMetrics();
        int metric1Value = Integer.parseInt(metrics.get(metric1));
        boolean part1Value = false;
        switch (thresholdOp1) {
            case "lt":
                part1Value = metric1Value < thresholdValue1;
                break;
            case "lte":
                part1Value = metric1Value <= thresholdValue1;
                break;
            case "gt":
                part1Value = metric1Value > thresholdValue1;
                break;
            case "gte":
                part1Value = metric1Value >= thresholdValue1;
                break;
            default:
                break;
        }
        
        if (logicOp.isEmpty())
            return part1Value;
        
        int metric2Value = Integer.parseInt(metrics.get(metric2));
        boolean part2Value = false;
        switch (thresholdOp2) {
            case "lt":
                part2Value = metric2Value < thresholdValue2;
                break;
            case "lte":
                part2Value = metric2Value <= thresholdValue2;
                break;
            case "gt":
                part2Value = metric2Value > thresholdValue2;
                break;
            case "gte":
                part2Value = metric2Value >= thresholdValue2;
                break;
            default:
                break;
        }
        
        boolean fullExprValue = false;
        switch (logicOp) {
            case "AND":
                fullExprValue = part1Value && part2Value;
                break;
            case "OR":
                fullExprValue = part1Value || part2Value;
                break;
        }
        return fullExprValue;
    }
    

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setMetric1(String metric1) {
        this.metric1 = metric1;
    }

    public void setThresholdOp1(String thresholdOp1) {
        this.thresholdOp1 = thresholdOp1;
    }

    public void setThresholdValue1(int thresholdValue1) {
        this.thresholdValue1 = thresholdValue1;
    }

    public void setLogicOp(String logicOp) {
        this.logicOp = logicOp;
    }

    public void setMetric2(String metric2) {
        this.metric2 = metric2;
    }

    public void setThresholdOp2(String thresholdOp2) {
        this.thresholdOp2 = thresholdOp2;
    }

    public void setThresholdValue2(int thresholdValue2) {
        this.thresholdValue2 = thresholdValue2;
    }
    
}
