package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

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
	private JScrollPane mainScrollPane;
	private JTabbedPane tabbedPane;
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
		frame.getContentPane().setBackground(Color.DARK_GRAY);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();

		frame.setBounds(0, 0, width - 100, height - 100);
		frame.setLocationRelativeTo(null);
		// frame.setResizable(false);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				frame.setDefaultCloseOperation(popupSaveClose());
			}
		});

		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel analysisPanel = new JPanel();
		JPanel importPanel = new JPanel();

		Dimension hMargin = new Dimension(10, 0);
		EmptyBorder fullPadding = new EmptyBorder(10, 10, 10, 10);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.DARK_GRAY);
		mainPanel.setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		// Rules Panel

		panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		mainScrollPane = new JScrollPane(panel);
		
		mainScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		mainPanel.add(mainScrollPane, BorderLayout.CENTER);
		GridLayout gridLayout = new GridLayout(6, 1, 0, 10);

		panel.setLayout(gridLayout);

		panel_2 = new JPanel();
		panel_2.setBackground(Color.DARK_GRAY);
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(panel_2, BorderLayout.SOUTH);

		panel_3 = new JPanel(new BorderLayout());
		panel_3.setBackground(Color.DARK_GRAY);
		JLabel title = new JLabel("CODE SMELLER");
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.PLAIN, 40));
		panel_3.add(title, BorderLayout.CENTER);
		panel_3.setBorder(fullPadding);
		
		
		
		JButton btnSaveRules = new JButton("Save Rules");
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.DARK_GRAY);
		btnSaveRules.setEnabled(false);
		panel_4.add(btnSaveRules);
		panel_3.add(panel_4, BorderLayout.EAST);

		btnSaveRules.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                saveRuleHistory(true);
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
        JButton addRuleButton = new JButton("Add Rule");
        
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
					btnSaveRules.setEnabled(false);
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

				JDialog ruleHistoryDialog = new JDialog(frame, "Rule History");
		    	ruleHistoryDialog.setLayout(new BorderLayout());
		    	ruleHistoryDialog.setResizable(false);
		    	
		    	JPanel dialogTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		    	
		    	JLabel ruleHistoryTitle = new JLabel("RULE HISTORY", SwingConstants.CENTER);
		    	dialogTitlePanel.add(ruleHistoryTitle);
		    	ruleHistoryDialog.add(dialogTitlePanel, BorderLayout.NORTH);
		    	
		    	JPanel historyComboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                HashMap<String, List<Rule>> ruleMap = RuleFileManager.readRules(RuleFileManager.HISTORY_FILE_PATH);
                List<List<Rule>> rules = new LinkedList<>(ruleMap.values());
                Set<String> timestamps = ruleMap.keySet();
                
                JButton clearHistoryButton = new JButton("Clear Rule History");
                JButton importRulesButton = new JButton("Import Rule Set");
                JComboBox<String> ruleHistory = new JComboBox<String>();
                
                if (timestamps.size() > 0)
                {
                    timestamps.forEach(ruleHistory::addItem);
                    historyComboBoxPanel.add(ruleHistory);
                    importRulesButton.setEnabled(true);
                }
                else
                {
                    importRulesButton.setEnabled(false);
                    historyComboBoxPanel.add(new JLabel("Your rule set history is empty!"));
                }
                ruleHistoryDialog.add(historyComboBoxPanel, BorderLayout.CENTER);
                
                JPanel historyButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                
                historyButtonPanel.add(clearHistoryButton);
                clearHistoryButton.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        RuleFileManager.clearHistory(RuleFileManager.HISTORY_FILE_PATH);
                        Object[] popupOptions = { "Ok" };
                        JOptionPane.showOptionDialog(frame,
                                "Rule history cleared!", "Rule history cleared!",
                                JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, popupOptions,
                                popupOptions[0]);
                        ruleHistoryDialog.setVisible(false);
                    }
                });
                
                historyButtonPanel.add(importRulesButton);
                importRulesButton.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                        rulesGUI.clear();
                        panel.removeAll();
                        panel.add(ruleTitle);
                        for(Rule rule: rules.get(ruleHistory.getSelectedIndex()))
                        {
                            RuleGUI importedRuleGUI = new RuleGUI(panel, true);
                            importedRuleGUI.setupGUIFromRule(rule);
                            rulesGUI.add(importedRuleGUI);
                            importedRuleGUI.getToRemoveCheckBox().addActionListener(new ActionListener() {
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
                            
                            panel.add(importedRuleGUI);
                            
                            btnSaveRules.setEnabled(true);
                            ruleHistoryDialog.setVisible(false);
                        }
                        
                    }
                });
                
                ruleHistoryDialog.add(historyButtonPanel, BorderLayout.SOUTH);
                
                ruleHistoryDialog.pack();
                ruleHistoryDialog.setLocationRelativeTo(frame);
                ruleHistoryDialog.setVisible(true);
			}
				
		});


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
							getPopupImageIcon("src/icons/java.png"), popupOptions,
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
                        
                        btnSaveRules.setEnabled(true);
                        
                        mainPanel.updateUI();
                        ruleNumber++;
                    }
                } else {
                    JOptionPane.showMessageDialog(panel, "Atingiu o limite de 5 regras");
                }
            }
        });

		// Analysis Panel

		analysisPanel.setBackground(Color.DARK_GRAY);
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
					ArrayList<String> fileNameList = new ArrayList<>();
					tempMaestro.getFilesInDirectory().forEach(x -> fileNameList.add(x.getName()));
					showFilesToAnalyze(fileNameList);
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
						throw new Exception("As regras têm de ter nomes diferentes");
					}

					Maestro maestro = new Maestro(directoryPath);

					if (checkValidRule()) {
						List<Rule> rules = new ArrayList<>();

						rulesGUI.forEach(x -> rules.add(x.generateRule()));
						maestro.addRules(rules);
						frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						String resultsFilePath = maestro.startMetricCounters();
						frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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

		importPanel.setBackground(Color.DARK_GRAY);
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
				    frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					showImportedData(importPathTextField.getText());
					frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				} else {
					JOptionPane.showMessageDialog(importProjectButton, "Preencha o path do ficheiro");
				}
			}
		});
		importPanel.add(importProjectButton);
		frame.getContentPane().add(tabbedPane, BorderLayout.SOUTH);
		importPanel.add(Box.createRigidArea(hMargin));

	}

	protected void saveRuleHistory(boolean showConfirmationPopUp) {
	    List<Rule> rulesToWrite = new LinkedList<>();
        rulesGUI.forEach(x -> rulesToWrite.add(x.generateRule()));
        RuleFileManager.writeEntry(rulesToWrite, RuleFileManager.HISTORY_FILE_PATH);
        
        if (showConfirmationPopUp)
        {
            Object[] popupOptions = { "Ok" };
            JOptionPane.showOptionDialog(frame,
                    "Rule set saved successfuly!", "Rule set saved!",
                    JOptionPane.PLAIN_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, popupOptions,
                    popupOptions[0]);
        }
    }

    private int popupSaveClose() {
	    if (rulesGUI.isEmpty())
	        return JFrame.EXIT_ON_CLOSE;
		ImageIcon popupIcon = getPopupImageIcon("src/icons/question.png");
		Object[] popupOptions = { "Sim", "Não" };

		int popupResult = JOptionPane.showOptionDialog(frame, "Deseja guardar o seu histórico?", "Salvar Regras",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, popupIcon, popupOptions, popupOptions[0]);

		if (popupResult == 0) {
			saveRuleHistory(false);
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
				JOptionPane.showMessageDialog(panel, "Os valores das métricas a analisar teem que ser números inteiros.");
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

		JTable lineTable = new JTable(linesAsString.toArray(new String[0][0]), columnNames);
		lineTable.setAutoResizeMode(0);
		
		JPanel infoContainerPanel = new JPanel();
		infoContainerPanel.setBackground(Color.DARK_GRAY);

		JScrollPane tableScrollPane = new JScrollPane(lineTable);
		tableScrollPane.getViewport().setBackground(Color.DARK_GRAY);
		tableScrollPane.setBackground(Color.DARK_GRAY);
		tableScrollPane.setBorder(new EmptyBorder(0, 20, 20, 0));

		infoContainerPanel.add(tableScrollPane);
		
		
		try {
		    if (rulesGUI.size() > 0)
		    {
    		    JDialog codeSmellDialog = new JDialog(frame, "Code Smell Comparison", Dialog.ModalityType.DOCUMENT_MODAL);
                codeSmellDialog.setLayout(new BorderLayout());
                codeSmellDialog.setResizable(false);
                
                JPanel dialogTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                
                JLabel ruleHistoryTitle = new JLabel("Do you want to compare the extracted code smells with another file?", SwingConstants.CENTER);
                dialogTitlePanel.add(ruleHistoryTitle);
                codeSmellDialog.add(dialogTitlePanel, BorderLayout.NORTH);
                
                JPanel codeSmellFileBrowserPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextField codeSmellFileTextField = new JTextField();
                codeSmellFileTextField.setToolTipText("Browse...");
                codeSmellFileTextField.setPreferredSize(new Dimension(200, 26));
                JButton codeSmellFileBrowserButton = new JButton("Browse...");
                
                codeSmellFileBrowserButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        JFileChooser codeSmellFileChooser = new JFileChooser();
                        FileNameExtensionFilter xlsxFilter = new FileNameExtensionFilter(".xlsx", "xlsx");
                        codeSmellFileChooser.setFileFilter(xlsxFilter);
                        int codeSmellBrowserOutput = codeSmellFileChooser.showOpenDialog(null);
                        if (codeSmellBrowserOutput == JFileChooser.APPROVE_OPTION) {
                            codeSmellFileTextField.setText(codeSmellFileChooser.getSelectedFile().getAbsolutePath());
                        }
                    }
                });
                
                codeSmellFileBrowserPanel.add(codeSmellFileTextField);
                codeSmellFileBrowserPanel.add(codeSmellFileBrowserButton);
                codeSmellDialog.add(codeSmellFileBrowserPanel, BorderLayout.CENTER);
                
                
                JPanel codeSmellComparisonButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton noComparisonButton = new JButton("No");
                JButton compareButton = new JButton("Yes");
                
                noComparisonButton.addActionListener(new ActionListener() {
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        codeSmellDialog.setVisible(false);
                    }
                });
                
                compareButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTable codeSmellEvaluationTable = new JTable();
                        DefaultTableModel codeSmellEvaluationModel = (DefaultTableModel) codeSmellEvaluationTable.getModel();
                        
                        String[] ruleNames = lines.get(0).getRuleNames();
                        codeSmellEvaluationModel.addColumn(lines.get(0).getColumnNames()[0]); //create first column, for MethodID
                        Arrays.asList(ruleNames).forEach(codeSmellEvaluationModel::addColumn);
                        
                        String[][] codeSmellEvaluationContent = ExcelReader.compareCodeSmells(lines, codeSmellFileTextField.getText());
                        Arrays.asList(codeSmellEvaluationContent).forEach(codeSmellEvaluationModel::addRow);
    
                        JScrollPane tableScrollPane2 = new JScrollPane(codeSmellEvaluationTable);
                        tableScrollPane2.setBorder(new EmptyBorder(0, 10, 20, 20));
                        mainPanel.add(tableScrollPane2, BorderLayout.EAST);
                        mainPanel.updateUI();
                        codeSmellDialog.setVisible(false);
                    }
                });
                
                codeSmellComparisonButtonPanel.add(noComparisonButton);
                codeSmellComparisonButtonPanel.add(Box.createRigidArea(new Dimension(30, 15)));
                codeSmellComparisonButtonPanel.add(compareButton);
                codeSmellDialog.add(codeSmellComparisonButtonPanel, BorderLayout.SOUTH);
                
                codeSmellDialog.pack();
                codeSmellDialog.setLocationRelativeTo(frame);
                codeSmellDialog.setVisible(true);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		JLabel fileTitle = new JLabel(fileToImport, SwingConstants.CENTER);
		fileTitle.setForeground(Color.WHITE);

		int[] projectData = ExcelReader.getProjectStats(lines);
		JLabel numPackagesLabel = new JLabel("Number of packages: " + projectData[0], SwingConstants.CENTER);
		JLabel numClassesLabel = new JLabel("Number of classes: " + projectData[1], SwingConstants.CENTER);
		JLabel numMethodsLabel = new JLabel("Number of methods: " + projectData[2], SwingConstants.CENTER);
		JLabel numLinesLabel = new JLabel("Number of lines: " + projectData[3], SwingConstants.CENTER);
		numPackagesLabel.setForeground(Color.WHITE);
		numClassesLabel.setForeground(Color.WHITE);
		numMethodsLabel.setForeground(Color.WHITE);
		numLinesLabel.setForeground(Color.WHITE);
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.DARK_GRAY);
		northPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		JPanel metricPanel = new JPanel(new GridLayout(2, 2));
		metricPanel.setBackground(Color.DARK_GRAY);
		JButton backButton = new JButton("Go back");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				Maestro tempMaestro = new Maestro();
				tempMaestro.openFolder(analysisPathTextField.getText());
				ArrayList<String> fileNameList = new ArrayList<>();
                tempMaestro.getFilesInDirectory().forEach(x -> fileNameList.add(x.getName()));
				showFilesToAnalyze(fileNameList);

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

	private void showFilesToAnalyze(ArrayList<String> filenameList) {
		mainPanel.removeAll();

		JList<String> fileJList = new JList<String>(filenameList.toArray(new String[0]));
		JScrollPane listScrollPane = new JScrollPane(fileJList);

		mainPanel.add(listScrollPane, BorderLayout.WEST);
		mainPanel.add(mainScrollPane, BorderLayout.CENTER);
		mainPanel.add(panel_2, BorderLayout.SOUTH);
		mainPanel.add(panel_3, BorderLayout.NORTH);
		mainPanel.updateUI();
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