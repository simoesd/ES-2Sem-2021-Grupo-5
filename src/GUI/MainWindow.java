package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.util.SystemOutLogger;

import metricas.Maestro;
import reader.ExcelReader;
import reader.Line;
import rules.Rule;

public class MainWindow {

	private JFrame frame;
	private JTextField analysisPathTextField;
	private JTextField importPathTextField;
	private JPanel mainPanel;
	private JPanel panel;
	private JPanel panel_2;
	private JPanel panel_3;
	JScrollPane mainScrollPane;
	private ArrayList<RuleGUI> rulesGUI = new ArrayList<>();
	private int ruleNumber = 1;

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

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		frame.setBounds(0, 0, width - 100, height - 100);
		// frame.setResizable(false);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				frame.setDefaultCloseOperation(popupSaveClose());
			}
		});

		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel analysisPanel = new JPanel();
		JPanel importPanel = new JPanel();

		Dimension hMargin = new Dimension(10, 0);
		EmptyBorder fullPadding = new EmptyBorder(10, 10, 10, 10);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		// Rules Panel

		panel = new JPanel();

		mainScrollPane = new JScrollPane(panel);

		mainScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		mainPanel.add(mainScrollPane, BorderLayout.CENTER);
		GridLayout gridLayout = new GridLayout(6, 1, 0, 10);

		panel.setLayout(gridLayout);

		// panel.setBorder(new EmptyBorder(10,10,10,10));

		panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(panel_2, BorderLayout.SOUTH);

		panel_3 = new JPanel(new BorderLayout());
		JLabel title = new JLabel("CODE SMELLER");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.PLAIN, 40));
		panel_3.add(title, BorderLayout.CENTER);
		panel_3.setBorder(fullPadding);
		JButton saveData = new JButton("Save");
		JPanel panel_4 = new JPanel();
		panel_4.add(saveData);
		panel_3.add(panel_4, BorderLayout.EAST);

		saveData.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// savedata();
			}
		});

		title.setVisible(true);

		JLabel ruleTitle = new JLabel("RULES");
		ruleTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		ruleTitle.setVisible(false);
		ruleTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(ruleTitle);

		mainPanel.add(panel_3, BorderLayout.NORTH);

		JButton removeRuleButton = new JButton("Remove Rule");
		removeRuleButton.setEnabled(false);
		removeRuleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<RuleGUI> tempRules = new ArrayList<>();
				rulesGUI.forEach(r -> tempRules.add(r));
				for (int i = 0; i < tempRules.size(); i++) {
					if (tempRules.get(i).isSelected()) {
						rulesGUI.remove(tempRules.get(i));
						ruleNumber--;
					}
				}

				panel.removeAll();
				panel.add(ruleTitle);
				removeRuleButton.setEnabled(false);
				if (rulesGUI.isEmpty()) {
					ruleTitle.setVisible(false);
				}
				for (int i = 0; i < rulesGUI.size(); i++) {
					panel.add(rulesGUI.get(i));
				}
				panel.updateUI();
			}
		});

		JButton btnImportRules = new JButton("History");
		panel_2.add(btnImportRules);
		btnImportRules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFrame history = new JFrame("History");
				history.setBounds(0, 0, 500, 700);
				// frame.setResizable(false);
				history.getContentPane().setLayout(new BorderLayout(0, 0));
				JPanel panel_history = new JPanel(new BorderLayout());
				history.getContentPane().add(panel_history, BorderLayout.CENTER);
				history.setVisible(true);

//				importHistory();

				JPanel panel_southHistory = new JPanel(new BorderLayout());
				JPanel panel_northHistory = new JPanel();

				JButton importButton = new JButton("Import data");
				JButton clearButton = new JButton("Clear history");

				JLabel historyTitle = new JLabel("HISTORY");
				historyTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
				historyTitle.setHorizontalAlignment(SwingConstants.CENTER);

				panel_history.setBorder(new EmptyBorder(20, 20, 20, 20));

				panel_southHistory.add(importButton, BorderLayout.WEST);
				panel_southHistory.add(clearButton, BorderLayout.EAST);
				panel_northHistory.add(historyTitle);

				panel_history.add(panel_southHistory, BorderLayout.SOUTH);
				panel_history.add(panel_northHistory, BorderLayout.NORTH);

				importButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
//						importdata();

					}
				});

				clearButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
//						cleardata()

					}
				});

//		        HashMap<String, List<Rule>> ruleMap = RuleFileManager.readRules();
//		        Set<String> timestamps = ruleMap.keySet();
//		        
//		        for(Map.Entry<String, List<Rule>> rules: ruleMap.entrySet())
//		        {
//		            for(Rule rule: rules.getValue())
//		            {
//		                RuleGUI tempRuleGUI = rule.generateRuleGUI(panel);
//		                panel.add(tempRuleGUI);
//		            }
//		            break;
//		        }

			}
		});

		JButton addRuleButton = new JButton("Add Rule");
		panel_2.add(addRuleButton);
		panel_2.add(removeRuleButton);
		panel_2.setBorder(new EmptyBorder(10, 10, 0, 10));

		addRuleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ruleNumber <= 5) {
					ruleTitle.setVisible(true);

					Object[] popupOptions = { "Class", "Method" };
					int popupResult = JOptionPane.showOptionDialog(frame,
							"Do you wish to create a class or method rule?", "New rule option",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
							getPopupImageIcon("src/icons/java.png"), popupOptions, popupOptions[0]);

					if (popupResult >= 0) {
						RuleGUI rule = new RuleGUI(panel, popupResult == 0);

						rulesGUI.add(rule);

						rule.getToRemoveCheckBox().addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								boolean activateButton = false;

								for (RuleGUI tempRule : rulesGUI) {
									if (tempRule.isSelected())
										activateButton = true;
								}

								removeRuleButton.setEnabled(activateButton);
							}

						});

						panel.add(rule);

						mainPanel.updateUI();
						ruleNumber++;
					}
				} else {
					JOptionPane.showMessageDialog(panel, "Atingiu o limite de 5 regras");
				}
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

				String directoryPath = "";
				try {
					directoryPath = analysisPathTextField.getText();
					if (directoryPath.isEmpty()) {
						throw new Exception("Tem que ter um projeto selecionado antes de analisar");
					}

					if (replicatedTitledRules()) {
						throw new Exception("As regras t�m de ter nomes diferentes");
					}

					Maestro maestro = new Maestro(directoryPath);

					if (checkValidRule()) {
						List<Rule> rules = new ArrayList<>();

						rulesGUI.forEach(x -> rules.add(x.generateRule()));
						maestro.addRules(rules);

						String resultsFilePath = maestro.startMetricCounters();

						ImageIcon popupIcon = getPopupImageIcon("src/icons/excel.png");

						Object[] popupOptions = { "Sim", "N�o" };
						int popupResult = JOptionPane.showOptionDialog(frame,
								"An�lise completa com sucesso! Deseja visualizar os resultados?", "An�lise completa!",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, popupIcon, popupOptions,
								popupOptions[0]);

						if (popupResult == 0)
							showImportedData(resultsFilePath);

					}

				} catch (Exception e) {
					JOptionPane.showMessageDialog(panel, e.getMessage());

				}
			}

			private boolean replicatedTitledRules() {
				boolean sameTitle = false;
				for (int i = 0; i < rulesGUI.size(); i++) {
					for (int j = i + 1; j < rulesGUI.size(); j++) {
						if (rulesGUI.get(i).getRuleTitleAsString().equals(rulesGUI.get(j).getRuleTitleAsString())) {
							sameTitle = true;
							return sameTitle;
						}
					}
				}
				return sameTitle;
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
					showImportedData(importPathTextField.getText());
				} else {
					JOptionPane.showMessageDialog(importProjectButton, "Preencha o path do ficheiro");
				}
			}
		});
		importPanel.add(importProjectButton);
		frame.getContentPane().add(tabbedPane, BorderLayout.SOUTH);
		importPanel.add(Box.createRigidArea(hMargin));

	}

	private int popupSaveClose() {
		ImageIcon popupIcon = getPopupImageIcon("src/icons/question.png");
		Object[] popupOptions = { "Sim", "N�o" };

		int popupResult = JOptionPane.showOptionDialog(frame, "Deseja guardar o seu hist�rico?", "Salvar Regras",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, popupIcon, popupOptions, popupOptions[0]);

		if (popupResult == 0) {
			// savedata();
			return JFrame.EXIT_ON_CLOSE;

		} else if (popupResult == 1) {
			return JFrame.EXIT_ON_CLOSE;
		}

		return JFrame.DO_NOTHING_ON_CLOSE;

	}

	private boolean checkValidRule() {
		boolean isValid = true;
		for (RuleGUI rg : rulesGUI) {
			try {
				rg.getConditions().forEach(x -> Integer.parseInt(x.getValue()));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(panel, "Certifique-se que inseriu corretamente os valores");
				isValid = false;
				return isValid;
			}
		}

		return isValid;
	}

	private void showImportedData(String fileToImport) {
		mainPanel.removeAll();
		ArrayList<Line> lines = ExcelReader.readExcelFile(fileToImport);
		String[] columnNames = lines.get(0).getColumnNames();
		ArrayList<String[]> linesAsString = new ArrayList<>();

		for (int i = 0; i < lines.size(); i++) {
			linesAsString.add(lines.get(i).toArray()); // colocar uma linha em cada vetor do array
		}

		JTable tempTable = new JTable(linesAsString.toArray(new String[0][0]), columnNames);
		tempTable.setAutoResizeMode(0);

		JScrollPane tableScrollPane = new JScrollPane(tempTable);
		tableScrollPane.setBorder(new EmptyBorder(0, 20, 20, 0));
		try {

			if (analysisPathTextField.getText().contains("jasml_0.10")) {
//				comparewithCodeSmellsFile(columnNames, linesAsString);
			    temp(lines.get(0).getMetricNames(), lines);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				Maestro tempMaestro = new Maestro();
				tempMaestro.openFolder(analysisPathTextField.getText());
				showFilesToAnalyze(tempMaestro.getFilesInDirectory());

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

		JList<String> fileList = new JList(filenameList.toArray());
		JScrollPane listScrollPane = new JScrollPane(fileList);

		mainPanel.add(listScrollPane, BorderLayout.WEST);
		mainPanel.add(mainScrollPane, BorderLayout.CENTER);
		mainPanel.add(panel_2, BorderLayout.SOUTH);
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
				totalMethods += 5; // TODO fix with new line structure
				totalLinesOfCode += 5; // TODO fix with new line structure
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

	public static void enableDefaultValue(final JTextField tf, final String defaultValue) {
		// Set current value
		tf.setText(defaultValue);
		tf.setForeground(Color.gray);

		// Add listener
		tf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (tf.getText().equals(defaultValue)) {
					tf.setForeground(Color.black);
					tf.setText("");
				}
				super.focusGained(e);
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (tf.getText().equals("")) {
					tf.setForeground(Color.gray);
					tf.setText(defaultValue);
				}
				super.focusLost(e);
			}
		});
	}

	public static ImageIcon getPopupImageIcon(String iconPath) {

		ImageIcon tempIcon = new ImageIcon(iconPath);
		Image tempImg = tempIcon.getImage();
		BufferedImage bi = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(tempImg, 0, 0, 40, 40, null, null);
		return new ImageIcon(bi);
	}

	
	public void temp(String[] columnNames, ArrayList<Line> linesAsString)
	{
	    ArrayList<Line> dataToEvaluateCodeSmells = ExcelReader.readExcelFile("Code_Smells.xlsx");
	    JTable tempList = new JTable();
	    DefaultTableModel tempListModel = (DefaultTableModel) tempList.getModel();
	    tempListModel.addColumn(linesAsString.get(0).getColumnNames()[0]);
	    for (int i  = 0; i < linesAsString.size(); i++)
	    {
	        int j = 0;
	        String[] resultTableEntry = new String[4]; //TODO count number of rules
	        resultTableEntry[j++] = String.valueOf(i+1);
	        String ruleEvaluation;
	        
	        //get corresponding line from dataToEvaluateCodeSmells
	        Line correspondingLine = null;
	        for (Line line: dataToEvaluateCodeSmells)
	        {
	            if (line.getPkg().toLowerCase().equals(linesAsString.get(i).getPkg().toLowerCase()) && line.getCls().toLowerCase().equals(linesAsString.get(i).getCls().toLowerCase()) &&  line.getMeth�().toLowerCase().equals(linesAsString.get(i).getMeth�().toLowerCase()))
	                correspondingLine = line;
	        }
	        if (correspondingLine != null)
	        {
    	        for (int u = 0; u < linesAsString.get(i).getMetrics().size(); u++)
    	        {
        	        try {
                        boolean cellValue = customParseBoolean(linesAsString.get(i).metricsToArray()[u]);
                        String ruleName = columnNames[u];
                        if (i == 0)
                            tempListModel.addColumn(ruleName);
                        try {
                            if (correspondingLine.getMetrics().containsKey(ruleName))
                            {
                                boolean dataToEvaluateRuleValue = customParseBoolean(correspondingLine.getMetrics().get(ruleName));
                                if (cellValue)
                                    if (dataToEvaluateRuleValue)
                                        ruleEvaluation = "VP";
                                    else 
                                        ruleEvaluation = "FP";
                                else
                                    if (dataToEvaluateRuleValue)
                                        ruleEvaluation = "FN";
                                    else
                                        ruleEvaluation = "VN";
                            }
                            else 
                            {
                                ruleEvaluation = "NA";
                            }
                        } catch (IllegalArgumentException e) {
                            ruleEvaluation = "NA";
                        }
                        resultTableEntry[j++] = ruleEvaluation; 
                    } catch (IllegalArgumentException e) {
                        
                    }
    	        }
    	        tempListModel.addRow(resultTableEntry);
	        }
	        else 
	        {
	            for (; j < resultTableEntry.length; j++)
	                resultTableEntry[j] = "NA";
	            tempListModel.addRow(resultTableEntry);
	        }
	    }
	    JScrollPane tableScrollPane2 = new JScrollPane(tempList);
        tableScrollPane2.setBorder(new EmptyBorder(0, 10, 20, 20));
        mainPanel.add(tableScrollPane2, BorderLayout.EAST);
	}
	
	public static boolean customParseBoolean(String stringToParse) throws IllegalArgumentException {
	    if (stringToParse.equalsIgnoreCase("true"))
	        return true;
	    if (stringToParse.equalsIgnoreCase("false"))
	        return false;
        throw new IllegalArgumentException();
	}
}
