package view.users;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utility.DBConnection;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Profile extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final java.lang.Integer Integer = null;
	protected static final String String = null;
	private JPanel contentPane;
	private JLabel lblPwd;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profile frame = new Profile(Integer, String, String);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Profile(Integer userIdRow, String userIdCol, String tableName) {
		setTitle("Profile Page");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 323, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Images or Icons
		Icon iconBackground = new ImageIcon(this.getClass().getResource("/wood-background.jpeg"));

		JLabel lblProfile = new JLabel("Profile");
		lblProfile.setVerticalTextPosition(SwingConstants.BOTTOM);
		lblProfile.setVerticalAlignment(SwingConstants.BOTTOM);
		lblProfile.setFont(new Font("Tekton Pro Cond", Font.PLAIN, 45));
		lblProfile.setHorizontalAlignment(SwingConstants.CENTER);
		lblProfile.setForeground(Color.WHITE);
		lblProfile.setBounds(0, 11, 307, 55);
		contentPane.add(lblProfile);

		JLabel lblFname = new JLabel("First Name");
		lblFname.setForeground(Color.WHITE);
		lblFname.setHorizontalAlignment(SwingConstants.LEFT);
		lblFname.setBounds(52, 64, 79, 30);
		contentPane.add(lblFname);

		JLabel txtFname = new JLabel("");
		txtFname.setForeground(Color.WHITE);
		txtFname.setHorizontalAlignment(SwingConstants.LEFT);
		txtFname.setBounds(141, 64, 130, 30);
		contentPane.add(txtFname);

		JLabel lblMidName = new JLabel("Middle Name");
		lblMidName.setForeground(Color.WHITE);
		lblMidName.setHorizontalAlignment(SwingConstants.LEFT);
		lblMidName.setBounds(52, 96, 79, 30);
		contentPane.add(lblMidName);

		JLabel txtMidName = new JLabel("");
		txtMidName.setForeground(Color.WHITE);
		txtMidName.setHorizontalAlignment(SwingConstants.LEFT);
		txtMidName.setBounds(141, 96, 130, 30);
		contentPane.add(txtMidName);

		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setHorizontalAlignment(SwingConstants.LEFT);
		lblLastName.setBounds(52, 129, 79, 30);
		contentPane.add(lblLastName);

		JLabel txtLastName = new JLabel("");
		txtLastName.setForeground(Color.WHITE);
		txtLastName.setHorizontalAlignment(SwingConstants.LEFT);
		txtLastName.setBounds(141, 129, 130, 30);
		contentPane.add(txtLastName);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setForeground(Color.WHITE);
		lblGender.setHorizontalAlignment(SwingConstants.LEFT);
		lblGender.setBounds(52, 160, 79, 30);
		contentPane.add(lblGender);

		JLabel txtGender = new JLabel("");
		txtGender.setForeground(Color.WHITE);
		txtGender.setHorizontalAlignment(SwingConstants.LEFT);
		txtGender.setBounds(141, 160, 130, 30);
		contentPane.add(txtGender);

		JLabel lblDob = new JLabel("Date of Birth");
		lblDob.setForeground(Color.WHITE);
		lblDob.setHorizontalAlignment(SwingConstants.LEFT);
		lblDob.setBounds(52, 191, 79, 30);
		contentPane.add(lblDob);

		JLabel txtDob = new JLabel("");
		txtDob.setForeground(Color.WHITE);
		txtDob.setHorizontalAlignment(SwingConstants.LEFT);
		txtDob.setBounds(141, 191, 130, 30);
		contentPane.add(txtDob);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
		lblEmail.setBounds(52, 220, 79, 30);
		contentPane.add(lblEmail);

		JLabel txtEmail = new JLabel("");
		txtEmail.setForeground(Color.WHITE);
		txtEmail.setHorizontalAlignment(SwingConstants.LEFT);
		txtEmail.setBounds(141, 220, 130, 30);
		contentPane.add(txtEmail);

		JLabel lblContact = new JLabel("Contact");
		lblContact.setForeground(Color.WHITE);
		lblContact.setHorizontalAlignment(SwingConstants.LEFT);
		lblContact.setBounds(52, 252, 79, 30);
		contentPane.add(lblContact);

		JLabel txtContact = new JLabel("");
		txtContact.setForeground(Color.WHITE);
		txtContact.setHorizontalAlignment(SwingConstants.LEFT);
		txtContact.setBounds(141, 252, 130, 30);
		contentPane.add(txtContact);
		
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
		btnExit.setBounds(217, 321, 66, 23);
		contentPane.add(btnExit);
				
				lblPwd = new JLabel("Password");
				lblPwd.setHorizontalAlignment(SwingConstants.LEFT);
				lblPwd.setForeground(Color.WHITE);
				lblPwd.setBounds(52, 280, 79, 30);
				contentPane.add(lblPwd);
				
				JLabel txtPass = new JLabel("");
				txtPass.setHorizontalAlignment(SwingConstants.LEFT);
				txtPass.setForeground(Color.WHITE);
				txtPass.setBounds(141, 280, 130, 30);
				contentPane.add(txtPass);
				
						JLabel lblBackground = new JLabel("");
						lblBackground.setForeground(Color.WHITE);
						lblBackground.setBounds(0, 0, 307, 361);
						lblBackground.setIcon(iconBackground);
						contentPane.add(lblBackground);

		try {

			String readQuery = "SELECT * FROM  " + tableName + " WHERE " + userIdCol + " = ?";
			PreparedStatement ps = DBConnection.connectDB().prepareStatement(readQuery);
			ps.setInt(1, userIdRow);
			ResultSet rs = ps.executeQuery();

			if (!rs.next()) {
				JOptionPane.showMessageDialog(new JFrame(), "No user found", "Profile Panel",
						JOptionPane.ERROR_MESSAGE);
			} else {

				txtFname.setText(rs.getString("first_name"));
				txtMidName.setText(rs.getString("middle_name"));
				txtLastName.setText(rs.getString("last_name"));
				txtGender.setText(rs.getString("gender"));
				txtDob.setText(rs.getString("date_of_birth"));
				txtEmail.setText(rs.getString("email"));
				txtPass.setText(rs.getString("password"));
				txtContact.setText(rs.getString("contact"));
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Profile Panel", JOptionPane.ERROR_MESSAGE);
		}
	}
}
