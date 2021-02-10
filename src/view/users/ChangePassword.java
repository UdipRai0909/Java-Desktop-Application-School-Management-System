package view.users;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

public class ChangePassword extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final java.lang.Integer Integer = null;
	protected static final String String = null;

	private JPanel contentPane;
	private JTextField txtPrevPwd;
	private JTextField txtNewPwd;
	private JTextField txtRePwd;

	public void clearData() {
		txtPrevPwd.setText(null);
		txtNewPwd.setText(null);
		txtRePwd.setText(null);
	}

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
					ChangePassword frame = new ChangePassword(Integer, String, String);
					frame.setVisible(true);
					frame.clearData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChangePassword(final Integer userIdRow, final String userIdCol, final String tableName) {

		setTitle("Account Settings");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 335);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Images or Icons
		Icon iconBackground = new ImageIcon(this.getClass().getResource("/wood-background.jpeg"));

		JLabel lblPreviousPwd = new JLabel("Previous password");
		lblPreviousPwd.setForeground(Color.WHITE);
		lblPreviousPwd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPreviousPwd.setBounds(62, 90, 136, 32);
		contentPane.add(lblPreviousPwd);

		JLabel lblNewLabel = new JLabel("Change Password");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(0, 34, 434, 45);
		contentPane.add(lblNewLabel);

		txtPrevPwd = new JTextField();
		txtPrevPwd.setBounds(208, 93, 156, 25);
		contentPane.add(txtPrevPwd);
		txtPrevPwd.setColumns(10);

		JLabel lblNewPwd = new JLabel("New password");
		lblNewPwd.setForeground(Color.WHITE);
		lblNewPwd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewPwd.setBounds(62, 134, 136, 32);
		contentPane.add(lblNewPwd);

		txtNewPwd = new JTextField();
		txtNewPwd.setColumns(10);
		txtNewPwd.setBounds(208, 137, 156, 25);
		contentPane.add(txtNewPwd);

		JLabel lblRePwd = new JLabel("Retype password");
		lblRePwd.setForeground(Color.WHITE);
		lblRePwd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRePwd.setBounds(62, 178, 136, 32);
		contentPane.add(lblRePwd);

		txtRePwd = new JTextField();
		txtRePwd.setColumns(10);
		txtRePwd.setBounds(208, 181, 156, 25);
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
				String dataOldPwd = "You have entered your old password. Not allowed.";
				String dataWrongPrevPwd = "Previous password is not correct!";
				String dataNoConnection = "Could not connect to the database.";

				JFrame messageFrame = new JFrame();

				try {
					// User Input Variables
					String prevPass = txtPrevPwd.getText();
					String newPass = txtNewPwd.getText();
					String rePass = txtRePwd.getText();

					if (prevPass.isEmpty() || newPass.isEmpty() || rePass.isEmpty()) {
						JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
								JOptionPane.WARNING_MESSAGE);
					} else {
						if (isOutOfRange(prevPass.length()) || isOutOfRange(newPass.length())
								|| isOutOfRange(rePass.length())) {
							JOptionPane.showMessageDialog(messageFrame, dataOutOfRange, currentPanel,
									JOptionPane.WARNING_MESSAGE);
						} else {
							if (!newPass.equals(rePass)) {
								JOptionPane.showMessageDialog(messageFrame, dataNotMatch, currentPanel,
										JOptionPane.WARNING_MESSAGE);
							} else {
								try {
									PreparedStatement ps = null;
									ResultSet rs = null;

									String readQuery = "SELECT * FROM " + tableName + " WHERE " + userIdCol + " = ?";
									ps = DBConnection.connectDB().prepareStatement(readQuery);
									ps.setInt(1, userIdRow);
									rs = ps.executeQuery();
									if (!rs.next()) {
										JOptionPane.showMessageDialog(messageFrame, dataError + dataNoConnection,
												currentPanel, JOptionPane.ERROR_MESSAGE);
									} else {

										String oldPassword = rs.getString("password");

										if (!oldPassword.equals(prevPass)) {
											JOptionPane.showMessageDialog(messageFrame, dataWrongPrevPwd, currentPanel,
													JOptionPane.ERROR_MESSAGE);
										} else {
											if (oldPassword.equals(newPass)) {
												JOptionPane.showMessageDialog(messageFrame, dataOldPwd, currentPanel,
														JOptionPane.ERROR_MESSAGE);
											} else {
												ps.close();
												String updateQuery = "UPDATE " + tableName + " SET password = ? WHERE "
														+ userIdCol + " = ?";
												ps = DBConnection.connectDB().prepareStatement(updateQuery);
												ps.setString(1, newPass);
												ps.setInt(2, userIdRow);
												if (ps.executeUpdate() > 0) {
													JOptionPane.showMessageDialog(messageFrame, dataUpdated,
															currentPanel, JOptionPane.INFORMATION_MESSAGE);
													setVisible(false);
													ps.close();
												}
											}
										}
									}

								} catch (Exception ex) {
									JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(),
											currentPanel, JOptionPane.ERROR_MESSAGE);
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
		btnSubmit.setBounds(146, 222, 66, 23);
		contentPane.add(btnSubmit);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnExit.setForeground(Color.WHITE);
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExit.setBorder(new LineBorder(Color.WHITE, 2));
		btnExit.setBackground(new Color(139, 69, 19));
		btnExit.setBounds(234, 222, 66, 23);
		contentPane.add(btnExit);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 434, 296);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);
	}
}
