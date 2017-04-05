package co.uk.epicguru.launcher.frame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import co.uk.epicguru.launcher.Main;

@SuppressWarnings("serial")
public class OkCancel extends JFrame {

	private JPanel contentPane;

	private boolean pressed = false;
	/**
	 * Create the frame.
	 */
	public OkCancel(Runnable OK, Runnable CANCEL, String title, String message) {
		OkCancel instance = this;
				
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				
				Main.print("Red X on window!");
				
				CANCEL.run();
				instance.stopWaiting();
			}			
		});
	
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setVisible(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle(title);
		setBounds(100, 100, 550, 144);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// OK
				instance.setVisible(false);
				OK.run();
				stopWaiting();
			}
		});
		btnOk.setBounds(171, 81, 89, 23);
		contentPane.add(btnOk);
		
		JButton btnCancel = new JButton("CANCEL");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// CANCEL
				instance.setVisible(false);
				CANCEL.run();
				stopWaiting();
			}
		});
		btnCancel.setBounds(283, 81, 89, 23);
		contentPane.add(btnCancel);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setEditable(false);
		textArea.setBounds(10, 11, 524, 60);
		contentPane.add(textArea);
		textArea.setText(message);
		
		while(!pressed){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) { }
		}
	}
	
	public void stopWaiting(){
		this.pressed = true;
	}
}
