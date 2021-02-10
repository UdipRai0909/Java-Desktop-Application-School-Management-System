package view.forgot_password;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ForgotPassword extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final String String = null;
	protected static final Integer Integer = null;
	
	private JPanel contentPane;
	private JTextField txtAnswer1;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ForgotPassword frame = new ForgotPassword(Integer, String, String);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ForgotPassword(final Integer userIdRow, final String userIdCol, final String tableName) {
		setResizable(false);
		setTitle("Account Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 619, 244);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Images or Icons
		Icon imgBackground = new ImageIcon(this.getClass().getResource("/sharingan-background.png"));
		
		JLabel lblQuestion1 = new JLabel("What was the name of your first pet ?");
		lblQuestion1.setForeground(Color.WHITE);
		lblQuestion1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblQuestion1.setBounds(195, 71, 272, 25);
		contentPane.add(lblQuestion1);
		
		JLabel lblNewLabel = new JLabel("Step 2 : Security Question");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(0, 11, 613, 41);
		contentPane.add(lblNewLabel);
		
		txtAnswer1 = new JTextField();
		txtAnswer1.setBorder(new LineBorder(Color.WHITE));
		txtAnswer1.setForeground(Color.WHITE);
		txtAnswer1.setBackground(Color.BLACK);
		txtAnswer1.setColumns(10);
		txtAnswer1.setBounds(195, 107, 272, 25);
		contentPane.add(txtAnswer1);
		
		JLabel lblQInitial = new JLabel("Q.");
		lblQInitial.setVerticalAlignment(SwingConstants.TOP);
		lblQInitial.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblQInitial.setFont(new Font("Hobo Std", Font.PLAIN, 30));
		lblQInitial.setForeground(Color.WHITE);
		lblQInitial.setBorder(null);
		lblQInitial.setBounds(152, 71, 35, 37);
		contentPane.add(lblQInitial);
		
		JLabel lblAInitial = new JLabel("A.");
		lblAInitial.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblAInitial.setVerticalAlignment(SwingConstants.TOP);
		lblAInitial.setForeground(Color.WHITE);
		lblAInitial.setFont(new Font("Hobo Std", Font.PLAIN, 30));
		lblAInitial.setBorder(null);
		lblAInitial.setBounds(151, 109, 35, 37);
		contentPane.add(lblAInitial);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame messageFrame = new JFrame();
				try {
					String answer = txtAnswer1.getText().toString();
					String myRealAnswer = "Cherry";
					String solution = myRealAnswer.toLowerCase();
					
					if (answer.isEmpty()) {
						JOptionPane.showMessageDialog(messageFrame, "Empty Field!", "Account Settings",
								JOptionPane.WARNING_MESSAGE);
					} else {
						if (answer.length() < 3 || answer.length() > 60) {
							JOptionPane.showMessageDialog(messageFrame, "Out of Range! Insert between (3-60) characters!", "Account Settings",
									JOptionPane.WARNING_MESSAGE);
						} else {
							if (!answer.toLowerCase().equals(solution)) {
								JOptionPane.showMessageDialog(messageFrame, "The answer is not correct!", "Account Settings",
										JOptionPane.WARNING_MESSAGE);
							} else {
								new view.forgot_password.NewPassword(userIdRow, userIdCol, tableName).setVisible(true);
								setVisible(false);
							}
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Account Settings", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBackground(Color.BLACK);
		btnSubmit.setBorder(new LineBorder(Color.RED));
		btnSubmit.setBounds(250, 157, 66, 23);
		contentPane.add(btnSubmit);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setForeground(Color.WHITE);
		btnExit.setBackground(Color.BLACK);
		btnExit.setBorder(new LineBorder(Color.RED));
		btnExit.setBounds(339, 157, 66, 23);
		contentPane.add(btnExit);
		
		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 613, 215);
		lblBackground.setIcon(imgBackground);
		contentPane.add(lblBackground);
	}

}
