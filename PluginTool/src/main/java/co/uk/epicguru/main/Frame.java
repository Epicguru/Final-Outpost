package co.uk.epicguru.main;

import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.border.EtchedBorder;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

public class Frame extends JFrame {

	private static final long serialVersionUID = -3853677044691076714L;
	private JPanel contentPane;
	private JTextField rootPath;
	private JTextField pluginID;
	private JTextField pluginProvider;

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
	public Frame() {
		setResizable(false);
		setTitle("Plugin Tool for Final Outpost");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 693, 447);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		rootPath = new JTextField();
		rootPath.setBounds(10, 59, 637, 20);
		rootPath.setColumns(10);
		
		JLabel lbRootPath = new JLabel("Root project path");
		lbRootPath.setBounds(10, 39, 85, 14);
		
		JButton refresh = new JButton("");
		refresh.setBounds(653, 59, 26, 20);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		refresh.setIcon(new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
		contentPane.setLayout(null);
		contentPane.add(refresh);
		
		JLabel lbPluginID = new JLabel("Plugin ID");
		lbPluginID.setBounds(10, 90, 42, 14);
		contentPane.add(lbPluginID);
		
		pluginID = new JTextField();
		pluginID.setColumns(10);
		pluginID.setBounds(107, 90, 86, 20);
		contentPane.add(pluginID);
		contentPane.add(rootPath);
		contentPane.add(lbRootPath);
		
		JLabel lbPluginProvider = new JLabel("Plugin Provider");
		lbPluginProvider.setBounds(10, 120, 71, 14);
		contentPane.add(lbPluginProvider);
		
		pluginProvider = new JTextField();
		pluginProvider.setColumns(10);
		pluginProvider.setBounds(107, 117, 86, 20);
		contentPane.add(pluginProvider);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 157, 183, 147);
		contentPane.add(scrollPane);
		
		JTree tree = new JTree();
		scrollPane.setViewportView(tree);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Plugin Root") {
				{
					DefaultMutableTreeNode node_1;
					DefaultMutableTreeNode node_2;
					node_1 = new DefaultMutableTreeNode("assets");
						node_2 = new DefaultMutableTreeNode("Texture");
							node_2.add(new DefaultMutableTreeNode("Example.png"));
							node_2.add(new DefaultMutableTreeNode("Thing.png"));
						node_1.add(node_2);
						node_2 = new DefaultMutableTreeNode("Why Here");
							node_2.add(new DefaultMutableTreeNode("Random.png"));
							node_2.add(new DefaultMutableTreeNode("Only Shows Textures.png"));
						node_1.add(node_2);
					getContentPane().add(node_1);
				}
			}
		));
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(10, 7, 97, 21);
		contentPane.add(menuBar);
	}
}
