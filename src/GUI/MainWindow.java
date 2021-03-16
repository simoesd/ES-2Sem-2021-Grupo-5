package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import main_package.Line;

public class MainWindow {

    private JFrame frame;
    private JTextField analysisPathTextField;
    private JTextField importPathTextField;
    private JPanel mainPanel;

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
        frame = new JFrame();
        frame.setBounds(100, 100, 780, 520);
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
        mainPanel.add(new JTextField("bruh"));
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        //Analysis Panel
        
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
                if (analyzeBrowserOutput == JFileChooser.APPROVE_OPTION) 
                {
                    analysisPathTextField.setText(analyzeFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        analysisPanel.add(analyzeFileBrowserButton);
        analysisPanel.add(Box.createRigidArea(hMargin));
        
        JButton startAnalysisButton = new JButton("  Analyze Directory  ");
        analysisPanel.add(startAnalysisButton);
        analysisPanel.add(Box.createRigidArea(hMargin));
        
        //Import Project Data Panel
        
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
                if (importBrowserOutput == JFileChooser.APPROVE_OPTION) 
                {
                    importPathTextField.setText(importFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        importPanel.add(importFileBrowserButton);
        importPanel.add(Box.createRigidArea(hMargin));
        
        JButton importProjectButton = new JButton("Import Project Data");
        importProjectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showImportedData();
            }
        });
        importPanel.add(importProjectButton);
        frame.getContentPane().add(tabbedPane, BorderLayout.SOUTH);
        importPanel.add(Box.createRigidArea(hMargin));
        
    }
    
    void showImportedData() {
        mainPanel.removeAll();
        
        
        int i = 0, j = 0, h = 0, k = 0, l = 0, m = 0;
        String[][] lines = {new Line(i++, "bruh", "bruh", "bruh", j++, h++, k++, true, l++, m++, true).toArray(), new Line(i++, "bruh", "bruh", "bruh", j++, h++, k++, true, l++, m++, true).toArray(), new Line(i++, "bruh", "bruh", "bruh", j++, h++, k++, true, l++, m++, true).toArray(), new Line(i++, "bruh", "bruh", "bruh", j++, h++, k++, true, l++, m++, true).toArray()};
        JList<Line> tempList = new JList(lines);
        String[] columnNames =  {"id", "nom_class", "loc_class", "wmc_class", "loc_method", "cyclo_method", "pkg", "cls", "method", "is_god", "is_long"};
        JTable tempTable = new JTable(lines, columnNames);
        tempTable.setAutoResizeMode(0);
        
        JScrollPane tableScrollPane = new JScrollPane(tempTable);
        tableScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.updateUI();
    }

}
