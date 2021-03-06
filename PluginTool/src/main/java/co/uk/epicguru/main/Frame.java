package co.uk.epicguru.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JTextField;

public class Frame extends JFrame {

	public static final long serialVersionUID = -3853677044691076714L;
	public JPanel contentPane;
	public JMenuItem fromFileMenu;
	public JMenuItem saveMenuButton;
	public JCheckBoxMenuItem autoSave;
	public JMenu recentMenu;
	public JMenuItem newMenuButton;
	public JTree paths;
	public JTextPane codeInfo;
	public JButton refresh;
	public JCheckBoxMenuItem folderStats;
	public JTextPane codeText;
	public JLabel pluginName;
	public JTextField pluginID;
	public JLabel lbPluginVersion;
	public JTextField pluginVersion;
	public JLabel lbPluginProvider;
	public JTextField pluginProvider;
	public JLabel lbPluginClass;
	public JTextField pluginClass;
	public JTextField pluginUserName;
	private JLabel lblUserFriendlyVersion;
	public JTextField pluginUserVersion;
	public JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public Frame() {
		Frame frame = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Code.close(frame);
			}
		});
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			e.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Plugin Tool for Final Outpost");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 693, 447);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		refresh = new JButton("");
		refresh.setToolTipText("Refresh All");
		refresh.setBounds(10, 36, 26, 20);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		refresh.setIcon(new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		contentPane.setLayout(null);
		contentPane.add(refresh);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.WHITE);
		menuBar.setBounds(0, 0, 700, 25);
		contentPane.add(menuBar);
		
		JMenu saveMenu = new JMenu("Save");
		menuBar.add(saveMenu);
		
		saveMenuButton = new JMenuItem("Save");
		saveMenu.add(saveMenuButton);
		
		JMenu openMenu = new JMenu("Open");
		menuBar.add(openMenu);
		
		fromFileMenu = new JMenuItem("From File");
		openMenu.add(fromFileMenu);
		
		recentMenu = new JMenu("Recent");
		openMenu.add(recentMenu);
		
		JMenu newMenu = new JMenu("New");
		menuBar.add(newMenu);
		
		newMenuButton = new JMenuItem("New Plugin");
		newMenu.add(newMenuButton);
		
		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(optionsMenu);
		
		autoSave = new JCheckBoxMenuItem("Auto Save");
		optionsMenu.add(autoSave);
		
		folderStats = new JCheckBoxMenuItem("Folder stats");
		folderStats.setSelected(true);
		optionsMenu.add(folderStats);
		
		JTabbedPane container = new JTabbedPane(JTabbedPane.TOP);
		container.setBounds(10, 67, 667, 340);
		contentPane.add(container);
		
		JPanel basicPanel = new JPanel();
		container.addTab("Basic", new ImageIcon(Frame.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")), basicPanel, null);
		basicPanel.setLayout(null);
		
		JLabel lblPluginId = new JLabel("Plugin ID");
		lblPluginId.setBounds(10, 11, 82, 14);
		basicPanel.add(lblPluginId);
		
		pluginID = new JTextField();
		pluginID.setBounds(113, 11, 198, 20);
		basicPanel.add(pluginID);
		pluginID.setColumns(10);
		
		lbPluginVersion = new JLabel("Plugin Version");
		lbPluginVersion.setBounds(10, 39, 82, 14);
		basicPanel.add(lbPluginVersion);
		
		pluginVersion = new JTextField();
		pluginVersion.setColumns(10);
		pluginVersion.setBounds(113, 39, 198, 20);
		basicPanel.add(pluginVersion);
		
		lbPluginProvider = new JLabel("Plugin Provider");
		lbPluginProvider.setBounds(10, 67, 82, 14);
		basicPanel.add(lbPluginProvider);
		
		pluginProvider = new JTextField();
		pluginProvider.setColumns(10);
		pluginProvider.setBounds(113, 67, 198, 20);
		basicPanel.add(pluginProvider);
		
		lbPluginClass = new JLabel("Plugin Main Class");
		lbPluginClass.setBounds(10, 95, 82, 14);
		basicPanel.add(lbPluginClass);
		
		pluginClass = new JTextField();
		pluginClass.setColumns(10);
		pluginClass.setBounds(113, 95, 198, 20);
		basicPanel.add(pluginClass);
		
		JLabel lbPluginName = new JLabel("User friendly name");
		lbPluginName.setBounds(10, 126, 119, 14);
		basicPanel.add(lbPluginName);
		
		pluginUserName = new JTextField();
		pluginUserName.setColumns(10);
		pluginUserName.setBounds(113, 126, 198, 20);
		basicPanel.add(pluginUserName);
		
		lblUserFriendlyVersion = new JLabel("User friendly version");
		lblUserFriendlyVersion.setBounds(10, 153, 119, 14);
		basicPanel.add(lblUserFriendlyVersion);
		
		pluginUserVersion = new JTextField();
		pluginUserVersion.setColumns(10);
		pluginUserVersion.setBounds(113, 153, 198, 20);
		basicPanel.add(pluginUserVersion);
		
		JPanel codePanel = new JPanel();
		container.addTab("Code", new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Left-Black.png")), codePanel, null);
		codePanel.setToolTipText("");
		codePanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 329, 310);
		codePanel.add(scrollPane);
		
		paths = new JTree();
		paths.setBorder(new LineBorder(new Color(255, 255, 255), 0));
		paths.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Plugin Root") {
				{
					
				}
			}
		));
		scrollPane.setViewportView(paths);
		
		codeInfo = new JTextPane();
		codeInfo.setBounds(328, 0, 334, 310);
		codeInfo.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 12));
		codeInfo.setEditable(false);
		codePanel.add(codeInfo);
		
		JPanel preview = new JPanel();
		container.addTab("Preview", new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Background-Color-Black.png")), preview, null);
		preview.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, 662, 310);
		preview.add(scrollPane_1);
		
		codeText = new JTextPane();
		codeText.setContentType("text/code");
		scrollPane_1.setViewportView(codeText);
		codeText.setEditable(false);
		
		pluginName = new JLabel("Load a plugin using Open> From File...");
		pluginName.setBounds(46, 36, 247, 20);
		contentPane.add(pluginName);
		
		Code.run(this);
	}
}
