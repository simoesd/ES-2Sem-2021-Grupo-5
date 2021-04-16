package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import metricas.Maestro;
import reader.ExcelReader;
import reader.Line;

public class MainWindow {

	private JFrame frame;
	private JTextField analysisPathTextField;
	private JTextField importPathTextField;
	private JPanel mainPanel;
	private JPanel panel;
	private JPanel panel_2;
	private JPanel panel_3;
	private ArrayList<Rule> rules = new ArrayList<>();
	private int incrementer = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Code Smeller");
		frame.setBounds(100, 100, 942, 624);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel analysisPanel = new JPanel();
		JPanel importPanel = new JPanel();

		Dimension hMargin = new Dimension(10, 0);
		Dimension vMargin = new Dimension(0, 10);
		EmptyBorder fullPadding = new EmptyBorder(10, 10, 10, 10);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		// Rules Panel
		
		panel = new JPanel();
		mainPanel.add(panel, BorderLayout.EAST);
		panel.setLayout(new GridLayout(5, 1, 0, 0));

		panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(panel_2, BorderLayout.SOUTH);
		
		panel_3 = new JPanel();
		JLabel title = new JLabel("RULES");
		panel_3.add(title);
		mainPanel.add(panel_3, BorderLayout.NORTH);
		
		JButton addRuleButton = new JButton("Add Rule");
		panel_2.add(addRuleButton);
		addRuleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (incrementer <= 5) {
					JPanel panel_1 = new JPanel();
					panel.add(panel_1);
					panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

					JComboBox metric1 = new JComboBox();
					metric1.setModel(new DefaultComboBoxModel(
							new String[] { "CYCLO_METHOD", "LOC_METHOD", "LOC_CLASS", "WMC_CLASS", "NOM_CLASS" }));
					panel_1.add(metric1);

					JComboBox mathSymbol1 = new JComboBox();
					mathSymbol1.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "\u2265", "\u2264" }));
					panel_1.add(mathSymbol1);

					JTextField value1 = new JTextField();
					value1.setColumns(10);
					panel_1.add(value1);

					JComboBox logicOp = new JComboBox();
					logicOp.setModel(new DefaultComboBoxModel(new String[] { "AND", "OR" }));
					panel_1.add(logicOp);

					JComboBox metric2 = new JComboBox();
					metric2.setModel(new DefaultComboBoxModel(
							new String[] { "CYCLO_METHOD", "LOC_METHOD", "LOC_CLASS", "WMC_CLASS", "NOM_CLASS" }));
					panel_1.add(metric2);

					JComboBox mathSymbol2 = new JComboBox();
					mathSymbol2.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "\u2265", "\u2264" }));
					panel_1.add(mathSymbol2);

					JTextField value2 = new JTextField();
					value2.setColumns(10);
					panel_1.add(value2);

					JCheckBox checkbox = new JCheckBox("");
					panel_1.add(checkbox);
					Rule rule = new Rule(panel_1, metric1, mathSymbol1, analysisPathTextField, logicOp, metric2,
							mathSymbol2, value2, checkbox);
					rules.add(rule);
					mainPanel.updateUI();
					incrementer++;
				}else {
					JOptionPane.showMessageDialog(panel, "Atingiu o limite de 5 regras");
				}
			}
		});

		JButton removeRuleButton = new JButton("Remove Rule");
		panel_2.add(removeRuleButton);
		removeRuleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<Rule> tempRules = new ArrayList<>();
				rules.forEach(r -> tempRules.add(r));
				for (int i = 0; i < tempRules.size(); i++) {
					if (tempRules.get(i).getCheckbox().isSelected()) {
						rules.remove(tempRules.get(i));
						incrementer--;
					}
				}

				panel.removeAll();
				for (int i = 0; i < rules.size(); i++) {
					panel.add(rules.get(i).getPanel_1());
				}
				panel.updateUI();
			}
		});

		// Analysis Panel

		tabbedPane.addTab("Analyze Directory", analysisPanel);
		analysisPanel.setLayout(new BoxLayout(analysisPanel, BoxLayout.X_AXIS));
		analysisPanel.setBorder(fullPadding);

		analysisPathTextField = new JTextField();
		analysisPathTextField.setColumns(10);
		analysisPathTextField.setToolTipText("Directory Path...");
		analysisPanel.add(analysisPathTextField);
		analysisPanel.add(Box.createRigidArea(hMargin));

		JButton analyzeFileBrowserButton = new JButton("Browse...");
		analyzeFileBrowserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser analyzeFileChooser = new JFileChooser();
				analyzeFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int analyzeBrowserOutput = analyzeFileChooser.showOpenDialog(null);
				if (analyzeBrowserOutput == JFileChooser.APPROVE_OPTION) {
					analysisPathTextField.setText(analyzeFileChooser.getSelectedFile().getAbsolutePath());
					Maestro tempMaestro = new Maestro();
					tempMaestro.openFolder(analysisPathTextField.getText());
					showFilesToAnalyze(tempMaestro.getFilesInDirectory());
				}
			}
		});

		analysisPanel.add(analyzeFileBrowserButton);
		analysisPanel.add(Box.createRigidArea(hMargin));

		JButton startAnalysisButton = new JButton("  Analyze Directory  ");
		startAnalysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO an�lise (equipa winx)
				String directoryPath = analysisPathTextField.getText();
				Maestro maestro = new Maestro(directoryPath);

				String resultsFilePath = maestro.startMetricCounters();

				ImageIcon tempIcon = new ImageIcon("src/icons/excel.png");
				Image tempImg = tempIcon.getImage();
				BufferedImage bi = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
				Graphics g = bi.createGraphics();
				g.drawImage(tempImg, 0, 0, 40, 40, null, null);
				ImageIcon popupIcon = new ImageIcon(bi);

				Object[] popupOptions = { "Sim", "N�o" };
				int popupResult = JOptionPane.showOptionDialog(frame,
						"An�lise completa com sucesso! Deseja visualizar os resultados?", "An�lise completa!",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, popupIcon, popupOptions,
						popupOptions[0]);

				if (popupResult == 0)
					showImportedData(resultsFilePath);
				else {
					
					mainPanel.updateUI();
				}

			}
		});
		analysisPanel.add(startAnalysisButton);
		analysisPanel.add(Box.createRigidArea(hMargin));

		// Import Project Data Panel

		tabbedPane.addTab("Import Project Data", importPanel);
		importPanel.setLayout(new BoxLayout(importPanel, BoxLayout.X_AXIS));
		importPanel.setBorder(fullPadding);

		importPathTextField = new JTextField();
		importPathTextField.setColumns(10);
		importPathTextField.setToolTipText("File Path...");
		importPanel.add(importPathTextField);
		importPanel.add(Box.createRigidArea(hMargin));

		JButton importFileBrowserButton = new JButton("Browse...");
		importFileBrowserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser importFileChooser = new JFileChooser();
				FileNameExtensionFilter xlsxFilter = new FileNameExtensionFilter(".xlsx", "xlsx");
				importFileChooser.setFileFilter(xlsxFilter);
				int importBrowserOutput = importFileChooser.showOpenDialog(null);
				if (importBrowserOutput == JFileChooser.APPROVE_OPTION) {
					importPathTextField.setText(importFileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		importPanel.add(importFileBrowserButton);
		importPanel.add(Box.createRigidArea(hMargin));

		JButton importProjectButton = new JButton("Import Project Data");
		importProjectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!importPathTextField.getText().isEmpty()) {
					System.out.println(importPathTextField.getText());
					showImportedData(importPathTextField.getText()); // showImportedData(getFileName(importPathTextField.getText()));
				} else {
					JOptionPane.showMessageDialog(importProjectButton, "Preencha o path do ficheiro");
				}
			}
		});
		importPanel.add(importProjectButton);
		frame.getContentPane().add(tabbedPane, BorderLayout.SOUTH);
		importPanel.add(Box.createRigidArea(hMargin));

	}

	private void showImportedData(String fileToImport) {
		mainPanel.removeAll();

		// TODO ivocar a importa��o (equipa PRR)
		ArrayList<Line> lines = ExcelReader.readExcelFile(fileToImport);

		String[] columnNames = { "id", "pkg", "cls", "method", "nom_class", "loc_class", "wmc_class", "is_god",
				"loc_method", "cyclo_method", "is_long" };
		String[][] linesAsString = new String[lines.size()][];

		for (int i = 0; i < lines.size(); i++) {
			linesAsString[i] = lines.get(i).toArray();
		}

		JTable tempTable = new JTable(linesAsString, columnNames);
		tempTable.setAutoResizeMode(0);

		JScrollPane tableScrollPane = new JScrollPane(tempTable);
		tableScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		JLabel fileTitle = new JLabel(fileToImport, SwingConstants.CENTER);

		int[] projectData = getProjectData(lines);
		JLabel numPackagesLabel = new JLabel("Number of packages: " + projectData[0], SwingConstants.CENTER);
		JLabel numClassesLabel = new JLabel("Number of classes: " + projectData[1], SwingConstants.CENTER);
		JLabel numMethodsLabel = new JLabel("Number of methods: " + projectData[2], SwingConstants.CENTER);
		JLabel numLinesLabel = new JLabel("Number of lines: " + projectData[3], SwingConstants.CENTER);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		JPanel metricPanel = new JPanel(new GridLayout(2, 2));
		
		JButton backButton = new JButton("Go back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(panel, BorderLayout.EAST);
				mainPanel.add(panel_2, BorderLayout.SOUTH);
				mainPanel.add(panel_3, BorderLayout.NORTH);
				mainPanel.updateUI();
					
			}
		});
			
     	metricPanel.add(numPackagesLabel);
		metricPanel.add(numClassesLabel);
		metricPanel.add(numMethodsLabel);
		metricPanel.add(numLinesLabel);
		northPanel.add(fileTitle, BorderLayout.NORTH);
		northPanel.add(metricPanel, BorderLayout.CENTER);
		northPanel.add(backButton, BorderLayout.WEST);
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(tableScrollPane, BorderLayout.CENTER);
		mainPanel.updateUI();
	}

	private void showFilesToAnalyze(ArrayList<File> fileArray) {
		mainPanel.removeAll();

		ArrayList<String> filenameList = new ArrayList<>();
		for (File f : fileArray) {
			filenameList.add(f.getName());
		}

		JList fileList = new JList(filenameList.toArray());
		JScrollPane listScrollPane = new JScrollPane(fileList);

		mainPanel.add(listScrollPane, BorderLayout.CENTER);
		mainPanel.add(panel, BorderLayout.EAST);
		mainPanel.add(panel_2,BorderLayout.SOUTH);
		mainPanel.add(panel_3, BorderLayout.NORTH);
		mainPanel.updateUI();
	}

	public static String getFileName(String fullPath) {
		while (fullPath.endsWith("\\"))
			fullPath = fullPath.substring(0, fullPath.length() - 1);
		return fullPath.substring(fullPath.lastIndexOf("\\") + 1);
	}

	public static int[] getProjectData(ArrayList<Line> lines) {
		ArrayList<String> classNames = new ArrayList<>();
		int totalMethods = 0;
		int totalLinesOfCode = 0;

		for (Line line : lines) {
			if (!classNames.contains(line.getCls())) {
				classNames.add(line.getCls());
				totalMethods += line.getNom_class();
				totalLinesOfCode += line.getLoc_class();
			}
		}

		ArrayList<String> packageNames = new ArrayList<>();

		for (Line line : lines) {
			if (!packageNames.contains(line.getPkg()))
				packageNames.add(line.getPkg());
		}

		int[] result = { packageNames.size(), classNames.size(), totalMethods, totalLinesOfCode };
		return result;
	}

}
