package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import utility.RoundedBorder;

public class Dasbboard extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	protected static final Integer Integer = null;
	protected static final String String = null;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dasbboard frame = new Dasbboard(Integer, String, String, Integer);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Dasbboard(final Integer userIdRow, final String userIdCol, final String tableName, final Integer roleNumId) {
		setResizable(false);
		setTitle("Dashboard");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 722, 448);

		// Images and Icons
		Image imgBackground = new ImageIcon(this.getClass().getResource("/wood-background.jpeg")).getImage();
		Icon dashboardStudents = new ImageIcon(this.getClass().getResource("/dashboard_students.png"));
		Icon dashboardTeachers = new ImageIcon(this.getClass().getResource("/dashboard_teachers.png"));
		Icon dashboardCourses = new ImageIcon(this.getClass().getResource("/dashboard_courses.png"));
		Icon dashboardExams = new ImageIcon(this.getClass().getResource("/dashboard_exams.png"));
		Icon dashboardDepartments = new ImageIcon(this.getClass().getResource("/dashboard_departments.png"));
		Icon dashboardAssignments = new ImageIcon(this.getClass().getResource("/dashboard_assignments.png"));
		Icon dashboardSections = new ImageIcon(this.getClass().getResource("/dashboard_sections.png"));
		Icon dashboardGrades = new ImageIcon(this.getClass().getResource("/dashboard_grades.png"));
		Icon dashboardFees = new ImageIcon(this.getClass().getResource("/dashboard_fees.png"));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(new MatteBorder(3, 0, 3, 0, (Color) new Color(139, 69, 19)));
		setJMenuBar(menuBar);

		JMenu mnProfile = new JMenu("Profile");
		mnProfile.setSize(new Dimension(145, 40));
		mnProfile.setHorizontalTextPosition(SwingConstants.CENTER);
		mnProfile.setHorizontalAlignment(SwingConstants.CENTER);
		mnProfile.setPreferredSize(new Dimension(145, 30));
		mnProfile.setMargin(new Insets(5, 25, 5, 10));
		menuBar.add(mnProfile);

		JMenuItem mntmViewProfile = new JMenuItem("View");
		mntmViewProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.users.Profile newFrame = new view.users.Profile(userIdRow, userIdCol, tableName);
				newFrame.setVisible(true);
			}
		});
		mntmViewProfile.setPreferredSize(new Dimension(145, 26));
		mntmViewProfile.setMargin(new Insets(2, 20, 2, 20));
		mnProfile.add(mntmViewProfile);

		JMenu mnNewMenu = new JMenu("Account Settings");
		mnNewMenu.setSize(new Dimension(145, 40));
		mnNewMenu.setMargin(new Insets(5, 25, 2, 10));
		mnNewMenu.setPreferredSize(new Dimension(145, 30));
		menuBar.add(mnNewMenu);

		JMenuItem mntmChangePwd = new JMenuItem("Change Password");
		mntmChangePwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.users.ChangePassword newFrame = new view.users.ChangePassword(userIdRow, userIdCol, tableName);
				newFrame.setVisible(true);
			}
		});
		mntmChangePwd.setPreferredSize(new Dimension(145, 30));
		mntmChangePwd.setMargin(new Insets(10, 20, 10, 20));
		mnNewMenu.add(mntmChangePwd);

		JMenuItem mntmLogOut = new JMenuItem("Log out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int status = JOptionPane.showConfirmDialog(null, "Do you really want to logout?", "Confirm?", JOptionPane.YES_NO_OPTION);
				if (status == JOptionPane.YES_OPTION) {
//					System.exit(0);
					setVisible(false);
					new view.MenuPage().setVisible(true);
				}
			}
		});
		mntmLogOut.setPreferredSize(new Dimension(145, 26));
		mntmLogOut.setMargin(new Insets(2, 20, 2, 20));
		mnNewMenu.add(mntmLogOut);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnStudents = new JButton("");
		btnStudents.setContentAreaFilled(false);
		btnStudents.setBorderPainted(false);
		btnStudents.setOpaque(false);
		btnStudents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Students newFrame = new view.tables.Students(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.loadComboBoxDept();
				newFrame.loadComboBoxGrade();
				newFrame.loadComboBoxRole();
				newFrame.loadComboBoxSection();
				newFrame.setVisible(true);
			}
		});

		JButton btnDepartments = new JButton("");
		btnDepartments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Departments newFrame = new view.tables.Departments(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.setVisible(true);
			}
		});
		btnDepartments.setIcon(dashboardDepartments);
		btnDepartments.setOpaque(false);
		btnDepartments.setContentAreaFilled(false);
		btnDepartments.setBorderPainted(false);
		btnDepartments.setBorder(new RoundedBorder(10));
		btnDepartments.setBounds(52, 15, 90, 73);
		contentPane.add(btnDepartments);

		JLabel lblDepartments = new JLabel("Departments");
		lblDepartments.setHorizontalTextPosition(SwingConstants.CENTER);
		lblDepartments.setHorizontalAlignment(SwingConstants.CENTER);
		lblDepartments.setForeground(Color.WHITE);
		lblDepartments.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDepartments.setBounds(10, 83, 174, 33);
		contentPane.add(lblDepartments);

		JLabel lblAssignments = new JLabel("Assignments");
		lblAssignments.setHorizontalTextPosition(SwingConstants.CENTER);
		lblAssignments.setHorizontalAlignment(SwingConstants.CENTER);
		lblAssignments.setForeground(Color.WHITE);
		lblAssignments.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAssignments.setBounds(10, 200, 174, 33);
		contentPane.add(lblAssignments);

		JButton btnAssignments = new JButton("");
		btnAssignments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Assignments newFrame = new view.tables.Assignments(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.loadComboBoxDept();
				newFrame.loadComboBoxTeacher();
				newFrame.setVisible(true);
			}
		});
		btnAssignments.setContentAreaFilled(false);
		btnAssignments.setBorderPainted(false);
		btnAssignments.setIcon(dashboardAssignments);
		btnAssignments.setOpaque(false);
		btnAssignments.setBorder(new RoundedBorder(10));
		btnAssignments.setBounds(53, 127, 90, 73);
		contentPane.add(btnAssignments);

		JLabel lblSections = new JLabel("Sections");
		lblSections.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSections.setHorizontalAlignment(SwingConstants.CENTER);
		lblSections.setForeground(Color.WHITE);
		lblSections.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSections.setBounds(271, 200, 174, 33);
		contentPane.add(lblSections);

		JButton btnSections = new JButton("");
		btnSections.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Sections newFrame = new view.tables.Sections(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.setVisible(true);
			}
		});
		btnSections.setIcon(dashboardSections);
		btnSections.setOpaque(false);
		btnSections.setContentAreaFilled(false);
		btnSections.setBorderPainted(false);
		btnSections.setBorder(new RoundedBorder(10));
		btnSections.setBounds(313, 127, 90, 73);
		contentPane.add(btnSections);
		btnStudents.setBounds(52, 265, 90, 73);
		btnStudents.setIcon(dashboardStudents);
		btnStudents.setBorder(new RoundedBorder(10));
		contentPane.add(btnStudents);

		JButton btnTeachers = new JButton("");
		btnTeachers.setIcon(dashboardTeachers);
		btnTeachers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Teachers newFrame = new view.tables.Teachers(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.loadComboBoxDept();
				newFrame.loadComboBoxRole();
				newFrame.setVisible(true);
			}
		});
		btnTeachers.setOpaque(false);
		btnTeachers.setContentAreaFilled(false);
		btnTeachers.setBorderPainted(false);
		btnTeachers.setBorder(new RoundedBorder(10));
		btnTeachers.setBounds(575, 127, 90, 73);
		contentPane.add(btnTeachers);

		JButton btnCourses = new JButton("");
		btnCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Courses newFrame = new view.tables.Courses(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.loadComboBoxDept();
				newFrame.setVisible(true);
			}
		});
		btnCourses.setIcon(dashboardCourses);
		btnCourses.setOpaque(false);
		btnCourses.setContentAreaFilled(false);
		btnCourses.setBorderPainted(false);
		btnCourses.setBorder(new RoundedBorder(10));
		btnCourses.setBounds(313, 265, 90, 73);
		contentPane.add(btnCourses);

		JButton btnExams = new JButton("");
		btnExams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Exams newFrame = new view.tables.Exams(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.loadComboBoxDept();
				newFrame.loadComboBoxFeeType();
				newFrame.setVisible(true);
			}
		});
		btnExams.setIcon(dashboardExams);
		btnExams.setOpaque(false);
		btnExams.setContentAreaFilled(false);
		btnExams.setBorderPainted(false);
		btnExams.setBorder(new RoundedBorder(10));
		btnExams.setBounds(575, 265, 90, 73);
		contentPane.add(btnExams);

		JLabel lblCourses = new JLabel("Courses");
		lblCourses.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCourses.setHorizontalAlignment(SwingConstants.CENTER);
		lblCourses.setForeground(Color.WHITE);
		lblCourses.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCourses.setBounds(271, 339, 174, 33);
		contentPane.add(lblCourses);

		JLabel lblTeachers = new JLabel("Teachers");
		lblTeachers.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTeachers.setHorizontalAlignment(SwingConstants.CENTER);
		lblTeachers.setForeground(Color.WHITE);
		lblTeachers.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblTeachers.setBounds(532, 201, 174, 33);
		contentPane.add(lblTeachers);

		JLabel lblExams = new JLabel("Exams");
		lblExams.setHorizontalTextPosition(SwingConstants.CENTER);
		lblExams.setHorizontalAlignment(SwingConstants.CENTER);
		lblExams.setForeground(Color.WHITE);
		lblExams.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblExams.setBounds(532, 339, 174, 33);
		contentPane.add(lblExams);

		JLabel lblStudents = new JLabel("Students");
		lblStudents.setHorizontalTextPosition(SwingConstants.CENTER);
		lblStudents.setHorizontalAlignment(SwingConstants.CENTER);
		lblStudents.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblStudents.setForeground(Color.WHITE);
		lblStudents.setBounds(10, 339, 174, 33);
		contentPane.add(lblStudents);

		JButton btnGrades = new JButton("");
		btnGrades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Grades newFrame = new view.tables.Grades(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.setVisible(true);
			}
		});
		btnGrades.setOpaque(false);
		btnGrades.setContentAreaFilled(false);
		btnGrades.setBorderPainted(false);
		btnGrades.setBorder(new RoundedBorder(10));
		btnGrades.setBounds(574, 15, 90, 73);
		btnGrades.setIcon(dashboardGrades);
		contentPane.add(btnGrades);

		JLabel lblGrades = new JLabel("Grades");
		lblGrades.setHorizontalTextPosition(SwingConstants.CENTER);
		lblGrades.setHorizontalAlignment(SwingConstants.CENTER);
		lblGrades.setForeground(Color.WHITE);
		lblGrades.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblGrades.setBounds(532, 86, 174, 33);
		contentPane.add(lblGrades);

		JButton btnFees = new JButton("");
		btnFees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.tables.Fees newFrame = new view.tables.Fees(roleNumId);
				newFrame.clearData();
				newFrame.loadData();
				newFrame.setVisible(true);
			}
		});
		btnFees.setOpaque(false);
		btnFees.setContentAreaFilled(false);
		btnFees.setBorderPainted(false);
		btnFees.setBorder(new RoundedBorder(10));
		btnFees.setBounds(313, 15, 90, 73);
		btnFees.setIcon(dashboardFees);
		contentPane.add(btnFees);

		JLabel lblFees = new JLabel("Fees");
		lblFees.setHorizontalTextPosition(SwingConstants.CENTER);
		lblFees.setHorizontalAlignment(SwingConstants.CENTER);
		lblFees.setForeground(Color.WHITE);
		lblFees.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblFees.setBounds(271, 83, 174, 33);
		contentPane.add(lblFees);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 716, 387);
		lblBackground.setIcon(new ImageIcon(imgBackground));
		contentPane.add(lblBackground);
	}
}
