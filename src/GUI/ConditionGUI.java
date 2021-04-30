package GUI;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rules.Condition;

public class ConditionGUI extends JPanel {
    private JComboBox<String> metric;
    private JComboBox<String> thresholdOperator;
    private JTextField thresholdValue;
    
    final String[] methodMetrics = {"CYCLO_METHOD", "LOC_METHOD"};
    final String[] classMetrics = {"LOC_CLASS", "WMC_CLASS", "NOM_CLASS"};
    final String[] thresholdOpDefault = {" > ", " < ", " \u2265 ", " \u2264 "};
    
    public ConditionGUI(boolean isClassRule)
    {
        
        this.metric = isClassRule ? new JComboBox<String>(classMetrics) : new JComboBox<String>(methodMetrics);
        
        this.thresholdOperator = new JComboBox<String>(thresholdOpDefault);
        this.thresholdValue = new JTextField();
        MainWindow.enableDefaultValue(this.thresholdValue, "0");
        initializePanel();
        
    }
    
    public ConditionGUI(JComboBox<String> metric, JComboBox<String> thresholdOperator, JTextField thresholdValue)
    {
        this.metric = metric;
        this.thresholdOperator = thresholdOperator;
        this.thresholdValue = thresholdValue;
        initializePanel();
    }
    
    public void initializePanel()
    {
        this.removeAll();
        this.add(metric);
        this.add(thresholdOperator);
        this.add(thresholdValue);
    }
    
    public String getMetric()
    {
        return metric.getSelectedItem().toString();
    }
    
    public String getValue()
    {
        return thresholdValue.getText();
    }
    
    public Condition generateCondition()
    {
        int conditionThresholdOperator = 0;
        switch (thresholdOperator.getSelectedIndex()) {
            case 0:
                conditionThresholdOperator = Condition.GREATER_THAN;
                break;
            case 1:
                conditionThresholdOperator = Condition.LESS_THAN;
                break;
            case 2:
                conditionThresholdOperator = Condition.GREATER_THAN_EQUAL;
                break;
            case 3:
                conditionThresholdOperator = Condition.LESS_THAN_EQUAL;
                break;
        }
        int conditionThresholdValue = Integer.parseInt(thresholdValue.getText());
        return new Condition(metric.getSelectedItem().toString(), conditionThresholdOperator, conditionThresholdValue);
    }
    
    public void setDefaultThresholdOp(int defaultIndex)
    {
        thresholdOperator.setSelectedIndex(defaultIndex);
    }
    
    public void setDefaultMetric(String defaultMetric)
    {
        for (int i = 0; i < metric.getItemCount(); i++)
        {
            if (metric.getItemAt(i).toUpperCase().equals(defaultMetric.toUpperCase()))
                metric.setSelectedItem(metric.getItemAt(i));
        }
    }
    
    public void setDefaultThresholdValue(String defaultThresholdValue)
    {
        MainWindow.enableDefaultValue(thresholdValue, defaultThresholdValue);
    }
}
