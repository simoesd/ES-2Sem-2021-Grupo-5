package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import rules.Condition;
import rules.Rule;

public class RuleGUI extends JPanel{
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -517667447012044612L;
	private JPanel parentPanel;
    private JCheckBox toRemoveCheckbox;
    private JTextField ruleTitle;
	private boolean isClassRule;
    
    private List<ConditionGUI> conditionsGUI = new ArrayList<>();
    private List<JComboBox<String>> logicOperators = new ArrayList<>();

    private JButton addConditionButton = new JButton("Add Condition");
    private JButton removeConditionButton = new JButton("Remove Condition");
    
    final String[] logicOpDefault = {"AND", "OR"};
    

    public RuleGUI(JPanel parentPanel, boolean isClassRule) {
        super();
        
        this.setLayout(new WrapLayout());
        this.setBackground(Color.DARK_GRAY);
        this.toRemoveCheckbox = new JCheckBox();
        this.toRemoveCheckbox.setBackground(Color.DARK_GRAY);
        this.ruleTitle = new JTextField(); 
        ruleTitle.setHorizontalAlignment(SwingConstants.CENTER);
        ruleTitle.setPreferredSize(new Dimension(83, 26));
        this.setBorder(new EmptyBorder(-10,30,-5,30));
        this.isClassRule = isClassRule;
        MainWindow.enableDefaultValue(ruleTitle, "Custom_Rule");
        this.parentPanel =  parentPanel;
        conditionsGUI.add(new ConditionGUI(isClassRule));
        
        
        addConditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (conditionsGUI.size() >= 8)
                    JOptionPane.showMessageDialog(parentPanel , "Não é possível adicionar mais condições à regra");
                else {
                    addNewLogicOperator(0);
                    addNewConditionComboBox();
                }
                initializePanel();
            }
        });
        
        removeConditionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                logicOperators.remove(logicOperators.size()-1);
                conditionsGUI.remove(conditionsGUI.size()-1);
                initializePanel();
            }
            
        });
        
        initializePanel();
    }
    
    public void addNewLogicOperator(int defaultIndex)
    {
        JComboBox<String> newLogicOp = new JComboBox<>(logicOpDefault);
        newLogicOp.setSelectedIndex(defaultIndex);
        logicOperators.add(newLogicOp);
    }
    
    public void addNewConditionComboBox()
    {
        conditionsGUI.add(new ConditionGUI(isClassRule));
    }
    
    public void addCondition(ConditionGUI newCondition)
    {
        conditionsGUI.add(newCondition);
    }
    
    public void initializePanel()
    {
        this.removeAll();
        this.add(toRemoveCheckbox);
        this.add(ruleTitle);
        this.add(conditionsGUI.get(0));
        
        int i = 1;
        for(JComboBox<String> logicOp: logicOperators)
        {
            this.add(logicOp);
            this.add(conditionsGUI.get(i++));
        }
        
        this.add(addConditionButton);
        if (conditionsGUI.size() > 1)
            this.add(removeConditionButton);
        
        this.parentPanel.updateUI();
    }
    
    
    public boolean isSelected()
    {
        return toRemoveCheckbox.isSelected();
    }
    
    public List<ConditionGUI> getConditions()
    {
        return conditionsGUI;
    }
    
    public JCheckBox getToRemoveCheckBox()
    {
        return toRemoveCheckbox;
    }
    
    public String getRuleTitleAsString() {
  		return ruleTitle.getText();
  	}
    
    public Rule generateRule()
    {
        LinkedList<Condition> conditions = new LinkedList<>();
        LinkedList<Integer> ruleLogicOperators = new LinkedList<>();
        
        conditionsGUI.forEach(x -> conditions.add(x.generateCondition()));
        
        for (JComboBox<String> logicOp: logicOperators)
        {
            switch (logicOp.getSelectedIndex()) {
                case 0:
                    ruleLogicOperators.add(Rule.AND);
                    break;
                case 1:
                    ruleLogicOperators.add(Rule.OR);
                    break;
            }
        }
        
        return new Rule(ruleTitle.getText(), conditions, ruleLogicOperators, isClassRule);
    }
    
    public void setupGUIFromRule(Rule rule)
    {
    	conditionsGUI.clear();
    	logicOperators.clear();
    	ruleTitle.setText(rule.getRuleName());
    	isClassRule = rule.isClassRule();
    	for (Condition condition: rule.getConditions())
    	{
    		ConditionGUI conditionGUI = new ConditionGUI(isClassRule);
    		conditionGUI.setupGUIFromCondition(condition);
    		addCondition(conditionGUI);
    	}
    	for (int logicOp: rule.getLogicOperators())
    		addNewLogicOperator(logicOp);
    	initializePanel();
    }
    
    public String getRuleTitle()
    {
    	return ruleTitle.getText();
    }

}