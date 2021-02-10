package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JCheckBox;

public class MenuPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuPage frame = new MenuPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuPage() {
		setResizable(false);
		setTitle("Menu Page");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 338, 334);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Image imgStudent = new ImageIcon(this.getClass().getResource("/student-small.png")).getImage();
		Image imgTeacher = new ImageIcon(this.getClass().getResource("/teacher-small.png")).getImage();
		Image imgAdmin = new ImageIcon(this.getClass().getResource("/admin-small.png")).getImage();
		Image imgBg = new ImageIcon(this.getClass().getResource("/school-back-1.jpg")).getImage();

		JButton btnLogin = new JButton("Login");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogin.setBackground(Color.BLACK);

		JLabel lblFooter = new JLabel("\r\nContact: +12 475 693  ");
		lblFooter.setToolTipText("");
		lblFooter.setHorizontalAlignment(SwingConstants.LEFT);
		lblFooter.setForeground(Color.WHITE);
		lblFooter.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblFooter.setBounds(10, 264, 148, 30);
		contentPane.add(lblFooter);

		final JCheckBox checkBoxTeacher = new JCheckBox("Teacher");
		checkBoxTeacher.setOpaque(false);
		checkBoxTeacher.setHorizontalAlignment(SwingConstants.CENTER);
		checkBoxTeacher.setForeground(Color.WHITE);
		checkBoxTeacher.setFont(new Font("Tahoma", Font.PLAIN, 11));
		checkBoxTeacher.setBounds(216, 163, 93, 23);
		contentPane.add(checkBoxTeacher);

		final JCheckBox checkBoxStudent = new JCheckBox("Student");
		checkBoxStudent.setOpaque(false);
		checkBoxStudent.setHorizontalAlignment(SwingConstants.CENTER);
		checkBoxStudent.setForeground(Color.WHITE);
		checkBoxStudent.setFont(new Font("Tahoma", Font.PLAIN, 11));
		checkBoxStudent.setBounds(110, 163, 93, 23);
		contentPane.add(checkBoxStudent);

		final JCheckBox checkBoxAdmin = new JCheckBox("Admin");
		checkBoxAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		checkBoxAdmin.setForeground(Color.WHITE);
		checkBoxAdmin.setFont(new Font("Tahoma", Font.PLAIN, 11));
		checkBoxAdmin.setOpaque(false);
		checkBoxAdmin.setBounds(10, 163, 93, 23);
		contentPane.add(checkBoxAdmin);

		JLabel lblChooseYourRole = new JLabel("Choose your role ");
		lblChooseYourRole.setBorder(new MatteBorder(0, 0, 2, 0, (Color) Color.BLACK));
		lblChooseYourRole.setHorizontalAlignment(SwingConstants.CENTER);
		lblChooseYourRole.setForeground(Color.WHITE);
		lblChooseYourRole.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChooseYourRole.setBounds(32, 85, 268, 30);
		contentPane.add(lblChooseYourRole);

		JLabel lblHeader = new JLabel("School Management System");
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setForeground(Color.WHITE);
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblHeader.setBounds(0, 11, 323, 30);
		contentPane.add(lblHeader);
		btnLogin.setBorder(new LineBorder(new Color(255, 0, 0), 2));
		btnLogin.setBounds(133, 195, 66, 23);
		contentPane.add(btnLogin);

		JLabel lblStudent = new JLabel("");
		lblStudent.setBounds(141, 123, 40, 40);
		lblStudent.setIcon(new ImageIcon(imgStudent));
		contentPane.add(lblStudent);

		JLabel lblTeacher = new JLabel("");
		lblTeacher.setBounds(244, 123, 40, 40);
		lblTeacher.setIcon(new ImageIcon(imgTeacher));
		contentPane.add(lblTeacher);

		JLabel lblAdmin = new JLabel("");
		lblAdmin.setBounds(38, 123, 40, 40);
		lblAdmin.setIcon(new ImageIcon(imgAdmin));
		contentPane.add(lblAdmin);

		JLabel labelBackground = new JLabel("");
		labelBackground.setVerticalTextPosition(SwingConstants.TOP);
		labelBackground.setVerticalAlignment(SwingConstants.TOP);
		labelBackground.setOpaque(true);
		labelBackground.setIgnoreRepaint(true);
		labelBackground.setHorizontalTextPosition(SwingConstants.LEFT);
		labelBackground.setHorizontalAlignment(SwingConstants.LEFT);
		labelBackground.setForeground(Color.WHITE);
		labelBackground.setFocusCycleRoot(true);
		labelBackground.setBackground(Color.PINK);
		labelBackground.setAutoscrolls(true);
		labelBackground.setAlignmentY(0.0f);
		labelBackground.setBounds(0, 0, 322, 310);
		labelBackground.setIcon(new ImageIcon(imgBg));
		contentPane.add(labelBackground);

		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				JFrame msgFrame = new JFrame();

				String adminMsg = "Do you want to login as admin?";
				String teacherMsg = "Do you want to login as teacher?";
				String studentMsg = "Do you want to login as student?";
				String confirm = "Confirm !";
				String myPanel = "Login Panel";
				String noneSelected = "No checkbox selected!";
				String multipleSelected = "Multiple checkbox selected! Select only one checkbox.";
				int testMe;

				boolean admSelect = checkBoxAdmin.isSelected();
				boolean tchSelect = checkBoxTeacher.isSelected();
				boolean stdSelect = checkBoxStudent.isSelected();
				try {
					// Multiple checkboxes selected
					if ((admSelect && tchSelect) || (tchSelect && stdSelect) || (stdSelect && admSelect)
							|| (admSelect && tchSelect && stdSelect)) {
						JOptionPane.showMessageDialog(msgFrame, multipleSelected, myPanel, JOptionPane.ERROR_MESSAGE);

					} else if (checkBoxAdmin.isSelected()) {
						testMe = JOptionPane.showConfirmDialog(null, adminMsg, confirm, JOptionPane.YES_NO_OPTION);
						
						if (testMe == JOptionPane.YES_OPTION) {
							view.LoginForm newFrame = new view.LoginForm("admin");
							newFrame.setVisible(true);
							setVisible(false);
						}

					} else if (checkBoxTeacher.isSelected()) {
						testMe = JOptionPane.showConfirmDialog(null, teacherMsg, confirm, JOptionPane.YES_NO_OPTION);
						
						if (testMe == JOptionPane.YES_OPTION) {
							view.LoginForm newFrame = new view.LoginForm("teacher");
							newFrame.setVisible(true);
							setVisible(false);
						}

					} else if (checkBoxStudent.isSelected()) {
						testMe = JOptionPane.showConfirmDialog(null, studentMsg, confirm, JOptionPane.YES_NO_OPTION);
						
						if (testMe == JOptionPane.YES_OPTION) {
							view.LoginForm newFrame = new view.LoginForm("student");
							newFrame.setVisible(true);
							setVisible(false);
						}

					} else {
						JOptionPane.showMessageDialog(msgFrame, noneSelected, myPanel, JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(msgFrame, ex.getMessage(), myPanel, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
