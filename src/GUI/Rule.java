package GUI;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Rule {

	private JPanel panel_1;
	private JComboBox metric1;
	private JComboBox mathSymbol1;
	private JTextField value1;
	private JComboBox logicOp;
	private JComboBox metric2;
	private JComboBox mathSymbol2;
	private JTextField value2;
	private JCheckBox checkbox;

	public Rule(JPanel panel_1, JComboBox metric1, JComboBox mathSymbol1, JTextField value1, JComboBox logicOp,
			JComboBox metric2, JComboBox mathSymbol2, JTextField value2, JCheckBox checkbox) {
		super();
		this.panel_1 = panel_1;
		this.metric1 = metric1;
		this.mathSymbol1 = mathSymbol1;
		this.value1 = value1;
		this.logicOp = logicOp;
		this.metric2 = metric2;
		this.mathSymbol2 = mathSymbol2;
		this.value2 = value2;
		this.checkbox = checkbox;
	}

	public JPanel getPanel_1() {
		return panel_1;
	}

	public JComboBox getMetric1() {
		return metric1;
	}

	public JComboBox getMathSymbol1() {
		return mathSymbol1;
	}

	public JTextField getValue1() {
		return value1;
	}

	public JComboBox getLogicOp() {
		return logicOp;
	}

	public JComboBox getMetric2() {
		return metric2;
	}

	public JComboBox getMathSymbol2() {
		return mathSymbol2;
	}

	public JTextField getValue2() {
		return value2;
	}

	public JCheckBox getCheckbox() {
		return checkbox;
	}

	public void addConditionRule(JComboBox logicOp, JComboBox metric2, JComboBox mathSymbol2, JTextField value2) {
		this.logicOp = logicOp;
		this.metric2 = metric2;
		this.mathSymbol2 = mathSymbol2;
		this.value2 = value2;
	}

	public void removeConditionRule() {
		this.logicOp = null;
		this.metric2 = null;
		this.mathSymbol2 = null;
		this.value2 = null;
	}

	public Rule(JPanel panel_1, JComboBox metric1, JComboBox mathSymbol1, JTextField value1, JCheckBox checkbox) {
		super();
		this.panel_1 = panel_1;
		this.metric1 = metric1;
		this.mathSymbol1 = mathSymbol1;
		this.value1 = value1;
		this.checkbox = checkbox;
	}

}
