package co.uk.epicguru.launcher.frame;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class DownloadProgress extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public JLabel nameOfDownload;
	public JProgressBar progressBar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DownloadProgress dialog = new DownloadProgress();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DownloadProgress() {
		setResizable(false);
		setTitle("Final Outpost Launcher - Download in progress");
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
