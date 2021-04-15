package GUI;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Rule {

	private JPanel panel_1;
	private JComboBox comboBox_1;
	private JComboBox comboBox;
	private JTextField textField;
	private JComboBox comboBox_3;
	private JComboBox comboBox_2;
	private JComboBox comboBox_4;
	private JTextField textField_1;
	private JCheckBox chckbxNewCheckBox;
	
	public Rule(JPanel panel_1, JComboBox comboBox_1, JComboBox comboBox, JTextField textField, JComboBox comboBox_3,
			JComboBox comboBox_2, JComboBox comboBox_4, JTextField textField_1, JCheckBox chckbxNewCheckBox) {
		super();
		this.panel_1 = panel_1;
		this.comboBox_1 = comboBox_1;
		this.comboBox = comboBox;
		this.textField = textField;
		this.comboBox_3 = comboBox_3;
		this.comboBox_2 = comboBox_2;
		this.comboBox_4 = comboBox_4;
		this.textField_1 = textField_1;
		this.chckbxNewCheckBox = chckbxNewCheckBox;
	}

	public JPanel getPanel_1() {
		return panel_1;
	}

	public JComboBox getComboBox_1() {
		return comboBox_1;
	}

	public JComboBox getComboBox() {
		return comboBox;
	}

	public JTextField getTextField() {
		return textField;
	}

	public JComboBox getComboBox_3() {
		return comboBox_3;
	}

	public JComboBox getComboBox_2() {
		return comboBox_2;
	}

	public JComboBox getComboBox_4() {
		return comboBox_4;
	}

	public JTextField getTextField_1() {
		return textField_1;
	}

	public JCheckBox getChckbxNewCheckBox() {
		return chckbxNewCheckBox;
	}
}
