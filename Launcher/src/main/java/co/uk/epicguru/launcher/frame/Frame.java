package co.uk.epicguru.launcher.frame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.connection.GameDataLoader;
import co.uk.epicguru.launcher.connection.LauncherUpdatesManager;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JPanel contentPane;
	private JLabel lblLoadingSplash;
	private JTextPane news;
	private JButton playButton;
	@SuppressWarnings("rawtypes")
	private JComboBox versionSelection;
	private static boolean run = true;

	/**
	 * Launch the application.
	 */
	public static void run(){
		try {
			
			// Look and feel
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e){
				e.printStackTrace();
				Main.print("Failed to set nice look-and-feel :(");
			}
			
			// Download latest before launcher opens.
			LauncherUpdatesManager.downloadLatest();
			
			// Frame
			Frame frame = new Frame();
			frame.setVisible(true);
			
			// Get data
			GameDataLoader.run(frame);
			
			while(run){
				Thread.sleep(30 * 1000); // 30 seconds
				update();
			}
			
		} catch (Exception e) {
			Main.print("Crash in Frame.run(), passing on exception.");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void update(){
		
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Frame() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				// Window close
				run = false;
			}
		});
		setTitle("Final Outpost - " + Main.VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 899, 449);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		versionSelection = new JComboBox();
		versionSelection.setFont(new Font("Segoe Print", Font.BOLD, 12));
		versionSelection.setModel(new DefaultComboBoxModel(new String[] {"Loading versions..."}));
		versionSelection.setSelectedIndex(0);
		versionSelection.setMaximumRowCount(1000);
		
		JLabel lblGameVersion = new JLabel("Game version");
		lblGameVersion.setFont(new Font("Segoe Print", Font.BOLD, 12));
		
		playButton = new JButton("PLAY");
		playButton.setForeground(Color.BLACK);
		playButton.setBackground(Color.RED);
		playButton.setFont(new Font("Segoe Print", Font.BOLD, 34));
		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setBackground(Color.LIGHT_GRAY);
		
		lblLoadingSplash = new JLabel("Loading Splash...");
		lblLoadingSplash.setFont(new Font("Segoe Print", Font.BOLD, 13));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(tabs, GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
						.addComponent(playButton, GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblGameVersion, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(versionSelection, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblLoadingSplash, GroupLayout.PREFERRED_SIZE, 853, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblLoadingSplash, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
					.addComponent(tabs, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGameVersion)
						.addComponent(versionSelection, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(playButton, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
		);
		
		news = new JTextPane();
		news.setEditable(false);
		news.setBackground(Color.LIGHT_GRAY);
		news.setFont(new Font("SimSun", Font.BOLD, 16));
		news.setText("Loading game news...");
		tabs.addTab("News", new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/web/skin/AlignLeft_16x16_JFX.png")), news, null);
		contentPane.setLayout(gl_contentPane);
	}
	public JLabel getSplash() {
		return lblLoadingSplash;
	}
	public JTextPane getNews() {
		return news;
	}
	public JButton getPlayButton() {
		return playButton;
	}
	@SuppressWarnings("rawtypes")
	public JComboBox getVersionSelection() {
		return versionSelection;
	}
}
