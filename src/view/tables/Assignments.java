package view.tables;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
import utility.DBConnection;

public class Assignments extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTextField txtTitle;
	private JTable table;
	JDateChooser dateDue = new JDateChooser();
	JDateChooser dateRelease = new JDateChooser();
	JComboBox<String> cboTeacher = new JComboBox<String>();
	JComboBox<String> cboSkill = new JComboBox<String>();
	JComboBox<String> cboDepartment = new JComboBox<String>();

	// User input Variables
	String title = null, releaseDate = null, dueDate = null, skillCap = null, department = null, teacher = null,
			findMe = null;

	// Required Variables
	int departmentId = 0, teacherId = 0;
	String firstName = null, middleName = null, lastName = null;

	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0, row = 0, id = 0, column = 0, parsedRlsDate = 0, parsedDueDate = 0;
	int confirmOption;
	String createQuery, readQuery, updateQuery, deleteQuery;

	// Infos and Error messages
	JFrame messageFrame = new JFrame();
	String createDataSuccess = "Success! Added data.";
	String updateDataSuccess = "Success! Edited data.";
	String deleteDataSuccess = "Success! Deleted data.";
	String dataWarning = "Warning! Cannot add data";
	String dataEmpty = "Empty Fields! Cannot add data";
	String dataInvalid = "Input out of range! Please insert a valid data.";
	String dataError = "Error! ";
	String currentPanel = "Exam Panel";
	String dataNoConnection = "No connection established!";
	String dataDuplicate = "Duplicate value! The data already exists.";
	String dataDeleteMsg = "Are you sure you want to delete ?";
	String dataFound = " items found.";
	String dataNotFound = "No such data found!";
	String dataOutOfBounds = "The value is out of bounds. Either characters null/too long OR Date is before 2010.";
	String dataDateError = "Please make sure the dates are correct.";

	public void clearData() {
		txtTitle.setText(null);
		dateRelease.setCalendar(null);
		dateDue.setCalendar(null);
		cboSkill.setSelectedIndex(0);
		cboDepartment.setSelectedIndex(0);
		cboTeacher.setSelectedIndex(0);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM assignments";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void loadComboBoxDept() {
		try {
			readQuery = "SELECT dep_name FROM departments";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				cboDepartment.addItem(rs.getString("dep_name"));
			}
			ps.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void loadComboBoxTeacher() {
		try {
			readQuery = "SELECT * FROM teachers";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				firstName = rs.getString("first_name");
				middleName = rs.getString("middle_name");
				lastName = rs.getString("last_name");
				cboTeacher.addItem(firstName + " " + middleName + " " + lastName);
			}
			ps.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public boolean isOutOfRange(int test) {

		if (test < 0) {
			return true;
		} else if (test > 100) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkInputRange(int input) {
		if (input < 3) {
			return true;
		} else if (input > 30) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmptyCbox(String input) {
		if (input != null && input.equals("Select")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDateRange(int year) {
		if (year > 2010) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Assignments frame = new Assignments(Integer);
					frame.setVisible(true);
					frame.clearData();
					frame.loadData();
					frame.loadComboBoxDept();
					frame.loadComboBoxTeacher();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Assignments(Integer roleNumId) {
		
		setResizable(false);
		setTitle("Assignments");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 525);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Images or Icons
		Icon iconBackground = new ImageIcon(this.getClass().getResource("/wood-background.jpeg"));

		// ScrollPane with Table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		scrollPane.setBounds(10, 275, 862, 168);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Id", "Title", "Release Date",
				"Due Date", "Skill Level", "Department Id", "Teacher Id" }) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(4).setResizable(false);
		table.getColumnModel().getColumn(5).setResizable(false);
		table.getColumnModel().getColumn(6).setResizable(false);
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();

				title = table.getModel().getValueAt(row, 1).toString();
				releaseDate = table.getModel().getValueAt(row, 2).toString();
				dueDate = table.getModel().getValueAt(row, 3).toString();
				skillCap = table.getModel().getValueAt(row, 4).toString();
				department = table.getModel().getValueAt(row, 5).toString();
				teacher = table.getModel().getValueAt(row, 6).toString();

				try {
					SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-mm-dd");
					Date selectedRlsDate = sd1.parse(releaseDate);
					Date selectedDueDate = sd1.parse(dueDate);

					txtTitle.setText(title);
					dateRelease.setDate(selectedRlsDate);
					dateDue.setDate(selectedDueDate);
					cboSkill.setSelectedItem(skillCap);

					id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

					readQuery = "SELECT * FROM assignments WHERE assignment_id = ?";
					ps = DBConnection.connectDB().prepareStatement(readQuery);
					ps.setInt(1, id);
					rs = ps.executeQuery();

					if (rs.next()) {
						departmentId = java.lang.Integer.parseInt(rs.getString("department_id"));
						teacherId = java.lang.Integer.parseInt(rs.getString("teacher_id"));
					}

					cboDepartment.setSelectedIndex(departmentId);
					cboTeacher.setSelectedIndex(teacherId);
					ps.close();

				} catch (Exception ex) {
					ex.getMessage();
					JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
							JOptionPane.ERROR_MESSAGE);
					clearData();
				}
			}
		});

		// All the Buttons
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearData();
				loadData();
			}
		});
		btnRefresh.setForeground(Color.BLACK);
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnRefresh.setBorder(null);
		btnRefresh.setBackground(new Color(0, 191, 255));
		btnRefresh.setBounds(783, 454, 89, 23);
		contentPane.add(btnRefresh);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					findMe = txtSearch.getText();
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					if (findMe.length() > 0) {
						try {
							readQuery = "SELECT * FROM assignments WHERE title LIKE '" + findMe.charAt(0) + "%' ";
							ps = DBConnection.connectDB().prepareStatement(readQuery);
							rs = ps.executeQuery();

							table.setModel(DbUtils.resultSetToTableModel(rs));
							model.setRowCount(0);
							int rows = table.getRowCount();
							if (rows > 0) {
								JOptionPane.showMessageDialog(messageFrame, rows + dataFound, currentPanel,
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(messageFrame, dataNotFound, currentPanel,
										JOptionPane.WARNING_MESSAGE);
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(messageFrame, dataInvalid, currentPanel,
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSearch.setBorder(null);
		btnSearch.setBackground(Color.GREEN);
		btnSearch.setBounds(577, 454, 89, 23);
		contentPane.add(btnSearch);

		try {
			if (roleNumId == 1 || roleNumId == 2) {

				JButton btnDelete = new JButton("Delete");
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());
							if (id > 0) {
								confirmOption = JOptionPane.showConfirmDialog(null, dataDeleteMsg, "Confirm !",
										JOptionPane.YES_NO_OPTION);
								if (confirmOption == JOptionPane.YES_OPTION) {
									deleteQuery = "DELETE FROM assignments WHERE assignment_id = ?";
									ps = DBConnection.connectDB().prepareStatement(deleteQuery);
									ps.setInt(1, id);
									if (ps.executeUpdate() > 0) {
										JOptionPane.showMessageDialog(messageFrame, deleteDataSuccess, currentPanel,
												JOptionPane.INFORMATION_MESSAGE);
										clearData();
										loadData();
										ps.close();
									} else {
										JOptionPane.showMessageDialog(messageFrame, dataNoConnection, currentPanel,
												JOptionPane.ERROR_MESSAGE);
										clearData();
										ps.close();
									}
								}
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
									JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				btnDelete.setForeground(Color.WHITE);
				btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnDelete.setBorder(null);
				btnDelete.setBackground(Color.RED);
				btnDelete.setBounds(212, 454, 89, 23);
				contentPane.add(btnDelete);

				JButton btnCreate = new JButton("Add");
				btnCreate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {
							// User inputs
							title = txtTitle.getText().toString();
							releaseDate = ((JTextField) dateRelease.getDateEditor().getUiComponent()).getText();
							dueDate = ((JTextField) dateDue.getDateEditor().getUiComponent()).getText();
							skillCap = cboSkill.getSelectedItem().toString();
							department = cboDepartment.getSelectedItem().toString();
							teacher = cboTeacher.getSelectedItem().toString();

							departmentId = cboDepartment.getSelectedIndex();
							teacherId = cboTeacher.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myReleaseDate = format.parse(releaseDate);
							Date myDueDate = format.parse(dueDate);

							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							parsedRlsDate = java.lang.Integer.parseInt(df.format(myReleaseDate));
							parsedDueDate = java.lang.Integer.parseInt(df.format(myDueDate));

							if (title.isEmpty() || releaseDate.isEmpty() || dueDate.isEmpty() || isEmptyCbox(skillCap)
									|| isEmptyCbox(department) || isEmptyCbox(teacher)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (isOutOfRange(title.length()) || !isDateRange(parsedRlsDate)
										|| !isDateRange(parsedDueDate)) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									if (myDueDate.before(myReleaseDate) || myReleaseDate.equals(myDueDate)) {
										JOptionPane.showMessageDialog(messageFrame, dataDateError, currentPanel,
												JOptionPane.WARNING_MESSAGE);

									} else {
										createQuery = "INSERT INTO assignments(title, release_date, deadline, skill_level, department_id, teacher_id)"
												+ " VALUES(?, ?, ?, ?, ?, ?)";
										ps = DBConnection.connectDB().prepareStatement(createQuery);
										ps.setString(1, title);
										ps.setObject(2, myReleaseDate);
										ps.setObject(3, myDueDate);
										ps.setString(4, skillCap);
										ps.setInt(5, departmentId);
										ps.setInt(6, teacherId);
										try {
											if (ps.executeUpdate() > 0) {
												JOptionPane.showMessageDialog(messageFrame, createDataSuccess,
														currentPanel, JOptionPane.INFORMATION_MESSAGE);
												clearData();
												loadData();
												ps.close();
											} else {
												JOptionPane.showMessageDialog(messageFrame, dataNoConnection,
														currentPanel, JOptionPane.ERROR_MESSAGE);
												clearData();
												ps.close();
											}
										} catch (Exception ex) {
											JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(),
													currentPanel, JOptionPane.ERROR_MESSAGE);
											clearData();
											ps.close();
										}
									}
								}
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
									JOptionPane.ERROR_MESSAGE);
							clearData();
						}
					}
				});
				btnCreate.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnCreate.setBorder(null);
				btnCreate.setBackground(Color.GREEN);
				btnCreate.setBounds(10, 454, 89, 23);
				contentPane.add(btnCreate);

				JButton btnUpdate = new JButton("Update");
				btnUpdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						try {
							// User inputs
							title = txtTitle.getText().toString();
							releaseDate = ((JTextField) dateRelease.getDateEditor().getUiComponent()).getText();
							dueDate = ((JTextField) dateDue.getDateEditor().getUiComponent()).getText();
							skillCap = cboSkill.getSelectedItem().toString();
							department = cboDepartment.getSelectedItem().toString();
							teacher = cboTeacher.getSelectedItem().toString();

							departmentId = cboDepartment.getSelectedIndex();
							teacherId = cboTeacher.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myReleaseDate = format.parse(releaseDate);
							Date myDueDate = format.parse(dueDate);

							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							parsedRlsDate = java.lang.Integer.parseInt(df.format(myReleaseDate));
							parsedDueDate = java.lang.Integer.parseInt(df.format(myDueDate));
							
							
							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());


							if (title.isEmpty() || releaseDate.isEmpty() || dueDate.isEmpty() || isEmptyCbox(skillCap)
									|| isEmptyCbox(department) || isEmptyCbox(teacher)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (isOutOfRange(title.length()) || !isDateRange(parsedRlsDate)
										|| !isDateRange(parsedDueDate)) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									if (myDueDate.before(myReleaseDate) || myReleaseDate.equals(myDueDate)) {
										JOptionPane.showMessageDialog(messageFrame, dataDateError, currentPanel,
												JOptionPane.WARNING_MESSAGE);

									} else {
										createQuery = "UPDATE assignments SET title = ?, release_date = ?, deadline = ?, skill_level = ?, department_id = ?, teacher_id = ? WHERE assignment_id = ?";
										ps = DBConnection.connectDB().prepareStatement(createQuery);
										ps.setString(1, title);
										ps.setObject(2, myReleaseDate);
										ps.setObject(3, myDueDate);
										ps.setString(4, skillCap);
										ps.setInt(5, departmentId);
										ps.setInt(6, teacherId);
										ps.setInt(7, id);
										try {
											if (ps.executeUpdate() > 0) {
												JOptionPane.showMessageDialog(messageFrame, updateDataSuccess,
														currentPanel, JOptionPane.INFORMATION_MESSAGE);
												clearData();
												loadData();
												ps.close();
											} else {
												JOptionPane.showMessageDialog(messageFrame, dataNoConnection,
														currentPanel, JOptionPane.ERROR_MESSAGE);
												clearData();
												ps.close();
											}
										} catch (Exception ex) {
											JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(),
													currentPanel, JOptionPane.ERROR_MESSAGE);
											clearData();
											ps.close();
										}
									}
								}
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
									JOptionPane.ERROR_MESSAGE);
							clearData();
						}
					}
				});
				btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnUpdate.setBorder(null);
				btnUpdate.setBackground(new Color(0, 191, 255));
				btnUpdate.setBounds(109, 454, 89, 23);
				contentPane.add(btnUpdate);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
					JOptionPane.ERROR_MESSAGE);
		}

		txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtSearch.setBounds(417, 454, 151, 23);
		contentPane.add(txtSearch);

		JLabel lblTitle = new JLabel("Assignments");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitle.setBounds(0, 0, 882, 48);
		contentPane.add(lblTitle);

		JLabel lblAssignTitle = new JLabel("Title");
		lblAssignTitle.setForeground(Color.WHITE);
		lblAssignTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAssignTitle.setBounds(10, 60, 245, 30);
		contentPane.add(lblAssignTitle);

		txtTitle = new JTextField();
		txtTitle.setColumns(10);
		txtTitle.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtTitle.setBounds(10, 90, 245, 30);
		contentPane.add(txtTitle);

		JLabel lblRelaseDate = new JLabel("Release Date");
		lblRelaseDate.setForeground(Color.WHITE);
		lblRelaseDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRelaseDate.setBounds(290, 60, 116, 30);
		contentPane.add(lblRelaseDate);

		JLabel lblSkillLevel = new JLabel("Skill Level");
		lblSkillLevel.setForeground(Color.WHITE);
		lblSkillLevel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSkillLevel.setBounds(592, 59, 74, 30);
		contentPane.add(lblSkillLevel);

		JLabel lblDueDate = new JLabel("Due Date");
		lblDueDate.setForeground(Color.WHITE);
		lblDueDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDueDate.setBounds(441, 59, 116, 30);
		contentPane.add(lblDueDate);

		JLabel lblDepartmentId = new JLabel("Department");
		lblDepartmentId.setForeground(Color.WHITE);
		lblDepartmentId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDepartmentId.setBounds(701, 59, 171, 30);
		contentPane.add(lblDepartmentId);

		cboDepartment.setModel(new DefaultComboBoxModel<String>(new String[] { "Select" }));
		cboDepartment.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboDepartment.setBounds(701, 90, 171, 30);
		contentPane.add(cboDepartment);

		dateRelease.setBounds(290, 90, 116, 30);
		contentPane.add(dateRelease);

		dateDue.setBounds(441, 90, 116, 30);
		contentPane.add(dateDue);

		JLabel lblTeacher = new JLabel("Teacher");
		lblTeacher.setForeground(Color.WHITE);
		lblTeacher.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTeacher.setBounds(10, 132, 279, 30);
		contentPane.add(lblTeacher);

		cboTeacher.setModel(new DefaultComboBoxModel<String>(new String[] { "Select" }));
		cboTeacher.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboTeacher.setBounds(10, 162, 279, 30);
		contentPane.add(cboTeacher);

		cboSkill.setModel(new DefaultComboBoxModel<String>(new String[] { "Select", "A+", "A", "B", "C", "D", "E" }));
		cboSkill.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboSkill.setBounds(592, 90, 74, 30);
		contentPane.add(cboSkill);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 882, 494);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);
	}

	@SuppressWarnings("unused")
	private static class __Tmp {
		private static void __tmp() {
			javax.swing.JPanel __wbp_panel = new javax.swing.JPanel();
		}
	}
}
