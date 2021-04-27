package GUI;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RuleGUI {

	private JPanel panel_1;
	private JCheckBox checkbox;
	private JTextField title;
	//1st condition
	private JComboBox<String> metric1;
	private JComboBox<String> mathSymbol1;
	private JTextField value1;
	//2nd condition
	private JComboBox<String> logicOp1;
	private JComboBox<String> metric2;
	private JComboBox<String> mathSymbol2;
	private JTextField value2;
	//3rd condition
	private JComboBox<String> logicOp2;
	private JComboBox<String> metric3;
	private JComboBox<String> mathSymbol3;
	private JTextField value3;
	

	public RuleGUI(JPanel panel_1, JCheckBox checkbox, JTextField title, JComboBox<String> metric1,
			JComboBox<String> mathSymbol1, JTextField value1, JComboBox<String> logicOp1, JComboBox<String> metric2,
			JComboBox<String> mathSymbol2, JTextField value2, JComboBox<String> logicOp2, JComboBox<String> metric3,
			JComboBox<String> mathSymbol3, JTextField value3) {
		super();
		this.panel_1 = panel_1;
		this.checkbox = checkbox;
		this.title = title;
		this.metric1 = metric1;
		this.mathSymbol1 = mathSymbol1;
		this.value1 = value1;
		this.logicOp1 = logicOp1;
		this.metric2 = metric2;
		this.mathSymbol2 = mathSymbol2;
		this.value2 = value2;
		this.logicOp2 = logicOp2;
		this.metric3 = metric3;
		this.mathSymbol3 = mathSymbol3;
		this.value3 = value3;
	}

	public RuleGUI(JPanel panel_1, JTextField title, JComboBox<String> metric1, JComboBox<String> mathSymbol1, JTextField value1,
			JComboBox<String> logicOp, JComboBox<String> metric2, JComboBox<String> mathSymbol2, JTextField value2, JCheckBox checkbox) {
		super();
		this.panel_1 = panel_1;
		this.title = title;
		this.metric1 = metric1;
		this.mathSymbol1 = mathSymbol1;
		this.value1 = value1;
		this.logicOp1 = logicOp;
		this.metric2 = metric2;
		this.mathSymbol2 = mathSymbol2;
		this.value2 = value2;
		this.checkbox = checkbox;
	}

	public JPanel getPanel_1() {
		return panel_1;
	}

	public JComboBox<String> getMetric1() {
		return metric1;
	}

	public JComboBox<String> getMathSymbol1() {
		return mathSymbol1;
	}

	public JTextField getValue1() {
		return value1;
	}

	public JComboBox<String> getLogicOp1() {
		return logicOp1;
	}

	public JComboBox<String> getMetric2() {
		return metric2;
	}

	public JComboBox<String> getMathSymbol2() {
		return mathSymbol2;
	}

	public JTextField getValue2() {
		return value2;
	}


	public JCheckBox getCheckbox() {
		return checkbox;
	}
	public JComboBox<String> getLogicOp2() {
		return logicOp2;
	}

	public JComboBox<String> getMetric3() {
		return metric3;
	}

	public JComboBox<String> getMathSymbol3() {
		return mathSymbol3;
	}


	public JTextField getValue3() {
		return value3;
	}

	public void add2ndConditionRule(JComboBox<String> logicOp1, JComboBox<String> metric2, JComboBox<String> mathSymbol2, JTextField value2) {
		this.logicOp1 = logicOp1;
		this.metric2 = metric2;
		this.mathSymbol2 = mathSymbol2;
		this.value2 = value2;
	}

	public void add3rdConditionRule(JComboBox<String> logicOp2, JComboBox<String> metric3, JComboBox<String> mathSymbol3, JTextField value3) {
		this.logicOp2 = logicOp2;
		this.metric3 = metric3;
		this.mathSymbol3 = mathSymbol3;
		this.value3 = value3;
	}
	
	public void remove2ndConditionRule() {
		this.logicOp1 = null;
		this.metric2 = null;
		this.mathSymbol2 = null;
		this.value2 = null;
	}
	
	public void remove3rdConditionRule() {
		this.logicOp2 = null;
		this.metric3 = null;
		this.mathSymbol3 = null;
		this.value3 = null;
	}
	
	public RuleGUI(JPanel panel_1, JTextField title, JComboBox<String> metric1, JComboBox<String> mathSymbol1, JTextField value1, JCheckBox checkbox) {
		super();
		this.panel_1 = panel_1;
		this.title = title;
		this.metric1 = metric1;
		this.mathSymbol1 = mathSymbol1;
		this.value1 = value1;
		this.checkbox = checkbox;
	}

	public JTextField getTitle() {
		return title;
	}

}