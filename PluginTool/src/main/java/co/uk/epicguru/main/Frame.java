package co.uk.epicguru.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JCheckBoxMenuItem;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame extends JFrame {

	public static final long serialVersionUID = -3853677044691076714L;
	public JPanel contentPane;
	public JTextField pluginID;
	public JTextField pluginProvider;
	public JTextField pluginVersion;
	public JTree tree;
	public JMenuItem fromFileMenu;
	public JMenuItem saveMenuButton;
	public JCheckBoxMenuItem autoSave;
	public JMenu recentMenu;
	public JMenuItem newMenuButton;

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
		setResizable(false);
		setTitle("Plugin Tool for Final Outpost");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 693, 447);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton refresh = new JButton("");
		refresh.setBounds(10, 36, 26, 20);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		refresh.setIcon(new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		contentPane.setLayout(null);
		contentPane.add(refresh);
		
		JLabel lbPluginID = new JLabel("Plugin ID");
		lbPluginID.setBounds(10, 67, 97, 14);
		contentPane.add(lbPluginID);
		
		pluginID = new JTextField();
		pluginID.setToolTipText("The non changeable, string ID for your plugin.");
		pluginID.setColumns(10);
		pluginID.setBounds(141, 67, 86, 20);
		contentPane.add(pluginID);
		
		JLabel lbPluginProvider = new JLabel("Plugin Provider");
		lbPluginProvider.setBounds(10, 97, 97, 14);
		contentPane.add(lbPluginProvider);
		
		pluginProvider = new JTextField();
		pluginProvider.setToolTipText("Your name here :D ");
		pluginProvider.setColumns(10);
		pluginProvider.setBounds(141, 94, 86, 20);
		contentPane.add(pluginProvider);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 153, 217, 254);
		contentPane.add(scrollPane);
		
		tree = new JTree();
		scrollPane.setViewportView(tree);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("TEMP") {
				{
					
				}
			}
		));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.WHITE);
		menuBar.setBounds(0, 0, 700, 25);
		contentPane.add(menuBar);
		
		JMenu saveMenu = new JMenu("Save");
		menuBar.add(saveMenu);
		
		saveMenuButton = new JMenuItem("Save");
		saveMenu.add(saveMenuButton);
		
		autoSave = new JCheckBoxMenuItem("Auto Save");
		saveMenu.add(autoSave);
		
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
		
		JLabel lbPluginVersion = new JLabel("Plugin Game Version");
		lbPluginVersion.setBounds(10, 125, 121, 14);
		contentPane.add(lbPluginVersion);
		
		pluginVersion = new JTextField();
		pluginVersion.setToolTipText("The GAME VERSION that the plugin is built for");
		pluginVersion.setColumns(10);
		pluginVersion.setBounds(141, 122, 86, 20);
		contentPane.add(pluginVersion);
		
		Code.run(this);
	}
}
