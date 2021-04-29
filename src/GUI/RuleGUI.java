package GUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RuleGUI extends JPanel{
    
    
    private JPanel parentPanel;
    private JCheckBox toRemoveCheckbox;
    
    private JTextField ruleTitle;
    
    private boolean isClassRule;
    
    private List<ConditionGUI> conditions = new ArrayList<>();
    private List<JComboBox<String>> logicOperators = new ArrayList<>();

    private JButton addConditionButton = new JButton("Add Condition");
    private JButton removeConditionButton = new JButton("Remove Condition");
    
    final String[] logicOpDefault = {"AND", "OR"};
    

    public RuleGUI(JPanel parentPanel, boolean isClassRule) {
        super();
        this.setLayout(new FlowLayout());
        this.toRemoveCheckbox = new JCheckBox();
        this.ruleTitle = new JTextField();
        this.isClassRule = isClassRule;
        MainWindow.enableDefaultValue(ruleTitle, "   Custom_Rule   ");
        this.parentPanel =  parentPanel;
        conditions.add(new ConditionGUI());
        
        addConditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (conditions.size() >= 8)
                    JOptionPane.showMessageDialog(parentPanel , "bruh");
                else {
                    logicOperators.add(new JComboBox(logicOpDefault));
                    conditions.add(new ConditionGUI());
                }
                initializePanel();
            }
        });
        
        removeConditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                logicOperators.remove(logicOperators.size()-1);
                conditions.remove(conditions.size()-1);
                initializePanel();
            }
            
        });
        
        initializePanel();
    }
    
    public void initializePanel()
    {
        this.removeAll();
        this.add(toRemoveCheckbox);
        this.add(ruleTitle);
        this.add(conditions.get(0));
        
        int i = 1;
        for(JComboBox<String> logicOp: logicOperators)
        {
            this.add(logicOp);
            this.add(conditions.get(i++));
        }
        
        this.add(addConditionButton);
        if (conditions.size() > 1)
            this.add(removeConditionButton);
        
        this.parentPanel.updateUI();
    }
    
    
    public boolean isSelected()
    {
        return toRemoveCheckbox.isSelected();
    }
    
    public List<ConditionGUI> getConditions()
    {
        return conditions;
    }
    
    public JCheckBox getToRemoveCheckBox()
    {
        return toRemoveCheckbox;
    }
    
    
    class ConditionGUI extends JPanel{
        private JComboBox<String> metric;
        private JComboBox<String> thresholdOperator;
        private JTextField thresholdValue;
        
        final String[] methodMetrics = {"CYCLO_METHOD", "LOC_METHOD"};
        final String[] classMetrics = {"LOC_CLASS", "WMC_CLASS", "NOM_CLASS"};
        final String[] thresholdOpDefault = {" > ", " < ", " \u2265 ", " \u2264 "};
        
        public ConditionGUI()
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
    }

}