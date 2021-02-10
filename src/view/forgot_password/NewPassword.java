package view.forgot_password;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

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

import utility.DBConnection;

public class NewPassword extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final java.lang.Integer Integer = null;
	protected static final String String = null;
	private JPanel contentPane;
	private JTextField txtNewPwd;
	private JTextField txtRePwd;

	public boolean isOutOfRange(int length) {
		if (length < 8 || length > 255) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewPassword frame = new NewPassword(Integer, String, String);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public NewPassword(final Integer userIdRow, final String userIdCol, final String tableName) {

		setTitle("Account Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 335);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Images or Icons
		Icon imgBackground = new ImageIcon(this.getClass().getResource("/wood-background.jpeg"));

		JLabel lblNewLabel = new JLabel("Change Password");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(0, 57, 434, 45);
		contentPane.add(lblNewLabel);

		JLabel lblNewPwd = new JLabel("New password");
		lblNewPwd.setForeground(Color.WHITE);
		lblNewPwd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewPwd.setBounds(62, 113, 136, 32);
		contentPane.add(lblNewPwd);

		txtNewPwd = new JTextField();
		txtNewPwd.setColumns(10);
		txtNewPwd.setBounds(208, 116, 156, 25);
		contentPane.add(txtNewPwd);

		JLabel lblRePwd = new JLabel("Retype password");
		lblRePwd.setForeground(Color.WHITE);
		lblRePwd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRePwd.setBounds(62, 157, 136, 32);
		contentPane.add(lblRePwd);

		txtRePwd = new JTextField();
		txtRePwd.setColumns(10);
		txtRePwd.setBounds(208, 160, 156, 25);
		contentPane.add(txtRePwd);

		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Required Variables
				String currentPanel = "Account Settings";
				String dataError = "Error! ";
				String dataEmpty = "Please dont leave the fields empty!";
				String dataOutOfRange = "The value should be between (8-255) characters!";
				String dataNotMatch = "The passwords are not the same!";
				String dataUpdated = "Password changed successfully!";

				JFrame messageFrame = new JFrame();

				try {

					// User Input Variables
					String newPass = txtNewPwd.getText();
					String rePass = txtRePwd.getText();

					if (newPass.isEmpty() || rePass.isEmpty()) {
						JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
								JOptionPane.WARNING_MESSAGE);
					} else {
						if (isOutOfRange(newPass.length()) || isOutOfRange(rePass.length())) {
							JOptionPane.showMessageDialog(messageFrame, dataOutOfRange, currentPanel,
									JOptionPane.WARNING_MESSAGE);
						} else {
							if (!newPass.equals(rePass)) {
								JOptionPane.showMessageDialog(messageFrame, dataNotMatch, currentPanel,
										JOptionPane.WARNING_MESSAGE);
							} else {
								String updateQuery = "UPDATE " + tableName + " SET password = ? WHERE " + userIdCol
										+ " = ?";
								PreparedStatement ps = DBConnection.connectDB().prepareStatement(updateQuery);
								ps.setString(1, newPass);
								ps.setInt(2, userIdRow);
								if (ps.executeUpdate() > 0) {
									JOptionPane.showMessageDialog(messageFrame, dataUpdated, currentPanel,
											JOptionPane.INFORMATION_MESSAGE);
									setVisible(false);
									new view.MenuPage().setVisible(true);
									ps.close();
								}

							}
						}
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSubmit.setBorder(new LineBorder(Color.WHITE, 2));
		btnSubmit.setBackground(new Color(139, 69, 19));
		btnSubmit.setBounds(146, 201, 66, 23);
		contentPane.add(btnSubmit);

		JButton btnExit = new JButton("Exit");
		btnExit.setForeground(Color.WHITE);
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExit.setBorder(new LineBorder(Color.WHITE, 2));
		btnExit.setBackground(new Color(139, 69, 19));
		btnExit.setBounds(234, 201, 66, 23);
		contentPane.add(btnExit);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 434, 296);
		lblBackground.setIcon(imgBackground);
		contentPane.add(lblBackground);
	}
}
