package view.forgot_password;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import utility.DBConnection;
import utility.MyEmailValidator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class FindEmail extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final String String = null;

	private JPanel contentPane;
	private JTextField txtEmail;

	public String getUserId(String userId) {

		switch (userId) {
		case "admin":
			return "admin_id";
		case "teacher":
			return "teacher_id";
		case "student":
			return "student_id";
		default:
			return null;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FindEmail frame = new FindEmail(String, String);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FindEmail(final String tableName, final String roleName) {
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

		JLabel lblEmail = new JLabel("Email Address");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmail.setBounds(195, 71, 272, 25);
		contentPane.add(lblEmail);

		JLabel lblTitle = new JLabel("Step 1 : Email");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblTitle.setBounds(0, 11, 613, 41);
		contentPane.add(lblTitle);

		txtEmail = new JTextField();
		txtEmail.setForeground(Color.WHITE);
		txtEmail.setColumns(10);
		txtEmail.setBorder(new LineBorder(Color.WHITE));
		txtEmail.setBackground(Color.BLACK);
		txtEmail.setBounds(195, 107, 272, 25);
		contentPane.add(txtEmail);

		JLabel lblQInitial = new JLabel("Q.");
		lblQInitial.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblQInitial.setVerticalAlignment(SwingConstants.TOP);
		lblQInitial.setForeground(Color.WHITE);
		lblQInitial.setFont(new Font("Hobo Std", Font.PLAIN, 30));
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
				JFrame msg = new JFrame();
				try {
					String email = txtEmail.getText().toString();

					if (email.isEmpty()) {
						JOptionPane.showMessageDialog(msg, "Empty Fields", "Login Panel", JOptionPane.ERROR_MESSAGE);
					} else {
						MyEmailValidator validateEmail = new MyEmailValidator();
						if (!validateEmail.validate(email)) {
							JOptionPane.showMessageDialog(msg, "Invalid Email.", "Login Panel",
									JOptionPane.WARNING_MESSAGE);
						} else {
							String readQuery = "SELECT * FROM " + tableName + " WHERE email = ?";
							PreparedStatement ps = DBConnection.connectDB().prepareStatement(readQuery);
							ps.setString(1, email);
							ResultSet rs = ps.executeQuery();

							if (!rs.next()) {
								JOptionPane.showMessageDialog(msg, "Error! No such email found in database.",
										"Login Panel", JOptionPane.ERROR_MESSAGE);
							} else {
								String userIdCol = getUserId(roleName);
								String userIdRow1 = rs.getString(userIdCol);
								try {
									int userIdRow = Integer.parseInt(userIdRow1);
									new view.forgot_password.ForgotPassword(userIdRow, userIdCol, tableName).setVisible(true);
									setVisible(false);
									ps.close();
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(msg, "Warning! Index out of bounds.", "Login Panel",
											JOptionPane.WARNING_MESSAGE);
								}
							}
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(msg, "Warning! Index out of bounds.", "Login Panel",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBorder(new LineBorder(Color.RED));
		btnSubmit.setBackground(Color.BLACK);
		btnSubmit.setBounds(250, 157, 66, 23);
		contentPane.add(btnSubmit);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new view.MenuPage().setVisible(true);
			}
		});
		btnExit.setForeground(Color.WHITE);
		btnExit.setBorder(new LineBorder(Color.RED));
		btnExit.setBackground(Color.BLACK);
		btnExit.setBounds(339, 157, 66, 23);
		contentPane.add(btnExit);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 613, 215);
		lblBackground.setIcon(imgBackground);
		contentPane.add(lblBackground);
	}

}
