package co.uk.epicguru.launcher.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import co.uk.epicguru.game.GameLoader;
import co.uk.epicguru.game.actions.Backup;
import co.uk.epicguru.game.actions.ResetVersions;
import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.connection.GameDataLoader;
import co.uk.epicguru.launcher.connection.LauncherUpdatesManager;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JPanel contentPane;
	private JLabel lblLoadingSplash;
	private JButton playButton;
	@SuppressWarnings("rawtypes")
	private JComboBox versionSelection;
	private static boolean run = true;
	private JTextPane news;
	private JLabel lblLoading;
	private JProgressBar progressBar;
	private JPanel options;
	private JButton runBackup;
	private JButton runReset;

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
		Frame instance = this;
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
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(() -> {
					GameLoader.play(instance);					
				}).start();
			}
		});
		playButton.setForeground(Color.BLACK);
		playButton.setBackground(Color.RED);
		playButton.setFont(new Font("Segoe Print", Font.BOLD, 34));
		
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.setBackground(Color.LIGHT_GRAY);
		
		lblLoadingSplash = new JLabel("Loading Splash...");
		lblLoadingSplash.setFont(new Font("Segoe Print", Font.BOLD, 13));
		
		progressBar = new JProgressBar();
		progressBar.setForeground(new Color(219, 112, 147));
		progressBar.setStringPainted(true);
		
		lblLoading = new JLabel("Loading...");
		lblLoading.setForeground(Color.WHITE);
		lblLoading.setFont(new Font("Space Bd BT", Font.BOLD, 15));
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
							.addComponent(versionSelection, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblLoading)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE))
						.addComponent(lblLoadingSplash, GroupLayout.PREFERRED_SIZE, 853, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblLoadingSplash, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(15)
					.addComponent(tabs, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblGameVersion)
							.addComponent(versionSelection, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblLoading)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(playButton, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
		);
		
		news = new JTextPane();
		news.setText("Loading game news...");
		news.setFont(new Font("SimSun", Font.BOLD, 16));
		news.setEditable(false);
		news.setBackground(Color.LIGHT_GRAY);
		tabs.addTab("News", new ImageIcon(Frame.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Left-Black.png")), news, null);
		
		options = new JPanel();
		tabs.addTab("Options", new ImageIcon(Frame.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")), options, null);
		
		JLabel backupLabel = new JLabel("Backup");
		backupLabel.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		
		runBackup = new JButton("Run Backup");
		runBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Run backup here
				boolean worked = Backup.backup(instance);
				
				if(worked){
					JOptionPane.showConfirmDialog(null, "Game data has been backed up. See " + new File(Main.gameBackup).getAbsolutePath() + 
							" for all backups.");
				}
			}
		});
		
		JLabel resetLabel = new JLabel("Reset");
		resetLabel.setFont(new Font("Segoe Print", Font.PLAIN, 14));
		
		runReset = new JButton("Run Reset");
		runReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Run reset here
				boolean worked = ResetVersions.resetVersions(instance);
				
				if(worked){
					JOptionPane.showConfirmDialog(null, "All versions (NOT GAME DATA) have been reset. They will have to be downloaded again.");
				}
			}
		});
		GroupLayout gl_options = new GroupLayout(options);
		gl_options.setHorizontalGroup(
			gl_options.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_options.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_options.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_options.createSequentialGroup()
							.addComponent(backupLabel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(runBackup))
						.addGroup(gl_options.createSequentialGroup()
							.addComponent(resetLabel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(runReset)))
					.addContainerGap(683, Short.MAX_VALUE))
		);
		gl_options.setVerticalGroup(
			gl_options.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_options.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_options.createParallelGroup(Alignment.BASELINE)
						.addComponent(backupLabel)
						.addComponent(runBackup))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_options.createParallelGroup(Alignment.LEADING)
						.addComponent(resetLabel, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_options.createSequentialGroup()
							.addGap(3)
							.addComponent(runReset)))
					.addContainerGap(155, Short.MAX_VALUE))
		);
		options.setLayout(gl_options);
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
	
	public void setBar(float p){
		this.progressBar.setValue((int) (p * 100f));
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getVersionSelection() {
		return versionSelection;
	}
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JButton getRunBackup() {
		return runBackup;
	}
	public JButton getRunReset() {
		return runReset;
	}
}
