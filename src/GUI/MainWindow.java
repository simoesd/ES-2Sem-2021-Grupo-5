package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import metricas.Maestro;
import reader.ExcelReader;
import reader.Line;
import rules.Rule;
import rules.RuleFileManager;

public class MainWindow {

	private JFrame frame;
	private JTextField analysisPathTextField;
	private JTextField importPathTextField;
	private JPanel mainPanel;
	private JPanel panel;
	private JPanel panel_2;
	private JPanel panel_3;
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
		frame.setBounds(100, 100, 1242, 624);
		frame.setResizable(false);
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
		mainPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(6, 1, 0, 0));

		panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(panel_2, BorderLayout.SOUTH);

		panel_3 = new JPanel();
		JLabel title = new JLabel("CODE SMELLER");
		title.setFont(new Font("Tahoma", Font.PLAIN, 40));
		panel_3.add(title);
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
//		
//		JButton btnImportRules = new JButton("Import Rules");
//		panel_2.add(btnImportRules);
//		btnImportRules.addActionListener(new ActionListener() {
//		    
//		    @Override
//		    public void actionPerformed(ActionEvent e) {
//		        panel.removeAll();
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
//		        
//		    }
//		});

		JButton addRuleButton = new JButton("Add Rule");
		panel_2.add(addRuleButton);
		panel_2.add(removeRuleButton);
		
		addRuleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ruleNumber <= 5) 
				{
					ruleTitle.setVisible(true);

					Object[] popupOptions = { "Class", "Method" };
                    int popupResult = JOptionPane.showOptionDialog(frame,
                            "Do you wish to create a class or method rule?", "New rule option",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, getPopupImageIcon("src/icons/java.png"), popupOptions,
                            popupOptions[0]);

                    if (popupResult >= 0)
                    {
                        RuleGUI rule = new RuleGUI(panel, popupResult == 0);
                        
                        rulesGUI.add(rule);
                        
                        rule.getToRemoveCheckBox().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                boolean activateButton = false;
                                
                                for (RuleGUI tempRule: rulesGUI)
                                {
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

					Maestro maestro = new Maestro(directoryPath);

					if (checkValidRule()) 
					{
					    List<Rule> rules = new ArrayList<>();
					    
					    rulesGUI.forEach(x -> rules.add(x.generateRule()));
					    maestro.addRules(rules);
					    
						String resultsFilePath = maestro.startMetricCounters();
						
						ImageIcon popupIcon = getPopupImageIcon("src/icons/excel.png");

						Object[] popupOptions = { "Sim", "Não" };
						int popupResult = JOptionPane.showOptionDialog(frame,
								"Análise completa com sucesso! Deseja visualizar os resultados?", "Análise completa!",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, popupIcon, popupOptions,
								popupOptions[0]);

						if (popupResult == 0)
							showImportedData(resultsFilePath);

					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(panel, e.getMessage());

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
			linesAsString.add(lines.get(i).toArray());
		}

		JTable tempTable = new JTable(linesAsString.toArray(new String[0][0]), columnNames);
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
		mainPanel.add(panel, BorderLayout.CENTER);
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
}
