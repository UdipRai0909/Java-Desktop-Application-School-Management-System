package view;

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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import utility.DBConnection;
import utility.MyEmailValidator;

public class LoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final String String = null;
	
	private JPanel panelLogin;
	private JTextField txtEmail;
	private JPasswordField pwdPass;

	
	public String getQuery(String roleName) {
		
		switch(roleName) {
		case "admin":
			return "SELECT * FROM admins WHERE email = ? AND password = ? ";
		case "teacher":
			return "SELECT * FROM teachers WHERE email = ? AND password = ? ";
		case "student":
			return "SELECT * FROM students WHERE email = ? AND password = ? ";
		default:
			return null;
		}
	}
	
	
	public String getUserId(String userId) {
		
		switch(userId) {
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
	
	
	public String getTableName(String roleName) {
		
		switch(roleName) {
		case "admin":
			return "admins";
		case "teacher":
			return "teachers";
		case "student":
			return "students";
		default:
			return null;
		}
	}
	
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm frame = new LoginForm(String);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginForm(final String roleName) {
		setResizable(false);
		setTitle("Login Form");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 619, 314);
		panelLogin = new JPanel();
		panelLogin.setBackground(new Color(0, 128, 128));
		panelLogin.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelLogin);
		panelLogin.setLayout(null);
		
		// Images or Icons
		Icon imgBackground = new ImageIcon(this.getClass().getResource("/sharingan-background.png"));

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblEmail.setBounds(172, 84, 110, 25);
		panelLogin.add(lblEmail);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(172, 119, 110, 25);
		panelLogin.add(lblPassword);

		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblNewLabel.setBounds(0, 32, 613, 41);
		panelLogin.add(lblNewLabel);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(293, 84, 159, 25);
		panelLogin.add(txtEmail);

		pwdPass = new JPasswordField();
		pwdPass.setBounds(293, 119, 159, 25);
		panelLogin.add(pwdPass);

		
		JButton btnForgotPwd = new JButton("Forgot password?");
		btnForgotPwd.setForeground(Color.WHITE);
		btnForgotPwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tableName = getTableName(roleName);
				new view.forgot_password.FindEmail(tableName, roleName).setVisible(true);;
				setVisible(false);
			}
		});
		btnForgotPwd.setBorder(new LineBorder(Color.RED, 2));
		btnForgotPwd.setBackground(Color.BLACK);
		btnForgotPwd.setBounds(293, 167, 126, 25);
		panelLogin.add(btnForgotPwd);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JFrame msg = new JFrame();
				try {
					String email = txtEmail.getText().toString();
					String pass = java.lang.String.valueOf(pwdPass.getPassword());

					if (email.isEmpty() || pass.isEmpty()) {
						JOptionPane.showMessageDialog(msg, "Empty Fields", "Login Panel",
								JOptionPane.ERROR_MESSAGE);
					} else {
						MyEmailValidator validateEmail = new MyEmailValidator();
						if (!validateEmail.validate(email)) {
							JOptionPane.showMessageDialog(msg, "Invalid Email.", "Login Panel",
									JOptionPane.WARNING_MESSAGE);
						} else {
							String readQuery = getQuery(roleName);
							PreparedStatement ps = DBConnection.connectDB().prepareStatement(readQuery);
							ps.setString(1, email);
							ps.setString(2, pass);
							ResultSet rs = ps.executeQuery();

							if (!rs.next()) {
								JOptionPane.showMessageDialog(msg, "Error! No such data found.", "Login Panel",
										JOptionPane.ERROR_MESSAGE);
							} else {
								String userIdCol = getUserId(roleName);
								String userIdRow1 = rs.getString(userIdCol);
								String roleId = rs.getString("role_id");
								try {
									int userIdRow = Integer.parseInt(userIdRow1);
									int roleNumId = Integer.parseInt(roleId);
									String tableName = getTableName(roleName);
									JOptionPane.showMessageDialog(msg, "Success! Logged in.", "Login Panel",
											JOptionPane.INFORMATION_MESSAGE);
									new view.Dasbboard(userIdRow, userIdCol, tableName, roleNumId).setVisible(true);
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
					JOptionPane.showMessageDialog(msg, "Error! No such data found.", "Login Panel",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBorder(new LineBorder(Color.RED, 2));
		btnLogin.setBackground(Color.BLACK);
		btnLogin.setBounds(218, 167, 65, 25);
		panelLogin.add(btnLogin);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 613, 285);
		lblBackground.setIcon(imgBackground);
		panelLogin.add(lblBackground);
	}

}
