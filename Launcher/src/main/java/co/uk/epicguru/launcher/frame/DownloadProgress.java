package co.uk.epicguru.launcher.frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import co.uk.epicguru.launcher.connection.LauncherUpdatesManager;

@SuppressWarnings("serial")
public class DownloadProgress extends JFrame {

	private final JPanel contentPanel = new JPanel();

	public JLabel nameOfDownload;
	public JProgressBar progressBar;

	/**
	 * Create the dialog.
	 */
	public DownloadProgress() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				LauncherUpdatesManager.closingWindow();
			}
		});
		setResizable(false);
		setTitle("Final Outpost Launcher - Installing update...");
		setBounds(100, 100, 469, 103);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNowDownloading = new JLabel("Download in progress : ");
			lblNowDownloading.setBounds(114, 10, 113, 14);
			contentPanel.add(lblNowDownloading);
		}
		{
			nameOfDownload = new JLabel("NAME OF DOWNLOAD");
			nameOfDownload.setBounds(232, 10, 107, 14);
			contentPanel.add(nameOfDownload);
		}
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 35, 433, 18);
		contentPanel.add(progressBar);
	}
}
