package co.uk.epicguru.launcher.frame;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import co.uk.epicguru.launcher.Main;
import co.uk.epicguru.launcher.connection.LauncherUpdatesManager;

@SuppressWarnings("serial")
public class Frame extends JFrame {

	private JPanel contentPane;
	public static JLabel latestVersion;

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
			
		} catch (Exception e) {
			Main.print("Crash in Frame.run(), passing on exception.");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		setTitle("Final Outpost Launcher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		latestVersion = new JLabel("Latest Version - Unable to connect!");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(latestVersion, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(231, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(latestVersion, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(225, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
