package view.tables;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
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
import javax.swing.JPasswordField;
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
import utility.MyEmailValidator;

public class Students extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTextField txtFirstName;
	private JTable table;
	private JTextField txtMidName;
	private JTextField txtLastName;
	private JTextField txtEmail;
	private JTextField txtAddress;
	private JPasswordField pwdPass;
	private JTextField txtContact;
	TextArea textAreaRemarks = new TextArea();
	@SuppressWarnings("rawtypes")
	JComboBox cboGender = new JComboBox();
	JDateChooser dateDob = new JDateChooser();
	@SuppressWarnings("rawtypes")
	JComboBox cboAttendance = new JComboBox();
	JComboBox<String> cboDepartment = new JComboBox<>();
	JComboBox<String> cboGrade = new JComboBox<>();
	JComboBox<String> cboRole = new JComboBox<>();
	JComboBox<String> cboSection = new JComboBox<String>();

	// User input Variables
	String firstName = null, middleName = null, lastName = null, email = null, password = null, address = null,
			gender = null, dateOfBirth = null, contactNumber = null, attendance = null, department = null, grade = null,
			role = null, section = null, findMe = null;

	// Required Variables
	int fnameLength = 0, midLength = 0, lnameLength = 0, pwdLength = 0, cctLength = 0, departmentId = 0, gradeId = 0,
			roleId = 0, sectionId = 0;

	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0, row = 0, id = 0, column = 0, dobYear = 0;
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
	String currentPanel = "Student Panel";
	String dataNoConnection = "No connection established!";
	String dataDuplicate = "Duplicate value! The data already exists.";
	String dataDeleteMsg = "Are you sure you want to delete ?";
	String dataFound = " items found.";
	String dataNotFound = "No such data found!";
	String dataRange1 = "Please insert a valid grade like A, A+, B-.";
	String dataRange2 = "Percentage should be between (1-100)";
	String dataOutOfBounds = "The value is out of bounds.";
	String dataNotPrivileged = "You don't have the sufficient rights to assign the role.";
	String dataEmailInvalid = "Invalid Email. Please try again.";

	public void clearData() {
		txtFirstName.setText(null);
		txtMidName.setText(null);
		txtLastName.setText(null);
		txtEmail.setText(null);
		pwdPass.setText(null);
		txtAddress.setText(null);
		cboGender.setSelectedIndex(0);
		dateDob.setCalendar(null);
		txtContact.setText(null);
		cboDepartment.setSelectedIndex(0);
		cboGrade.setSelectedIndex(0);
		cboRole.setSelectedIndex(0);
		cboAttendance.setSelectedIndex(0);
		cboSection.setSelectedIndex(0);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM students";
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

	public void loadComboBoxGrade() {
		try {
			readQuery = "SELECT grade FROM grades";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				cboGrade.addItem(rs.getString("grade"));
			}
			ps.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void loadComboBoxRole() {
		try {
			readQuery = "SELECT role_name FROM roles";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				cboRole.addItem(rs.getString("role_name"));
			}
			ps.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public void loadComboBoxSection() {
		try {
			readQuery = "SELECT room_number FROM sections";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				cboSection.addItem(rs.getString("room_number"));
			}
			ps.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public boolean isOutOfRange(int test) {
		int maxVal = java.lang.Integer.MAX_VALUE;

		if (test < 0) {
			return true;
		} else if (test > maxVal) {
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
		if (year > 1930) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Students frame = new Students(Integer);
					frame.setVisible(true);
					frame.clearData();
					frame.loadData();
					frame.loadComboBoxDept();
					frame.loadComboBoxGrade();
					frame.loadComboBoxRole();
					frame.loadComboBoxSection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Students(final Integer roleNumId) {
		setResizable(false);
		setTitle("Students");
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
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Id", "First Name", "Middle Name", "Last Name", "Email", "Password", "Address", "Gender",
						"Date of Birth", "Contact", "Attendance", "Department Id", "Grade Id", "Role Id", "Section Id" }) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false, false,
					false, false, false, false, false, false };

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
		table.getColumnModel().getColumn(7).setResizable(false);
		table.getColumnModel().getColumn(8).setResizable(false);
		table.getColumnModel().getColumn(9).setResizable(false);
		table.getColumnModel().getColumn(10).setResizable(false);
		table.getColumnModel().getColumn(11).setResizable(false);
		table.getColumnModel().getColumn(12).setResizable(false);
		table.getColumnModel().getColumn(13).setResizable(false);
		table.getColumnModel().getColumn(14).setResizable(false);
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();

				firstName = table.getModel().getValueAt(row, 1).toString();
				middleName = table.getModel().getValueAt(row, 2).toString();
				lastName = table.getModel().getValueAt(row, 3).toString();
				email = table.getModel().getValueAt(row, 4).toString();
				password = table.getModel().getValueAt(row, 5).toString();
				address = table.getModel().getValueAt(row, 6).toString();
				gender = table.getModel().getValueAt(row, 7).toString();
				dateOfBirth = table.getModel().getValueAt(row, 8).toString();
				contactNumber = table.getModel().getValueAt(row, 9).toString();
				attendance = table.getModel().getValueAt(row, 10).toString();
				department = table.getModel().getValueAt(row, 11).toString();
				grade = table.getModel().getValueAt(row, 12).toString();
				role = table.getModel().getValueAt(row, 13).toString();
				section = table.getModel().getValueAt(row, 14).toString();

				SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-mm-dd");
				try {
					Date selectedDate = sd1.parse(dateOfBirth);
					dateDob.setDate(selectedDate);

					txtFirstName.setText(firstName);
					txtMidName.setText(middleName);
					txtLastName.setText(lastName);
					txtEmail.setText(email);
					pwdPass.setText(password);
					txtAddress.setText(address);
					cboGender.setSelectedItem(gender);
					txtContact.setText(contactNumber);
					cboAttendance.setSelectedItem(attendance);

					id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

					readQuery = "SELECT * FROM students WHERE student_id = ?";
					ps = DBConnection.connectDB().prepareStatement(readQuery);
					ps.setInt(1, id);
					rs = ps.executeQuery();

					if (rs.next()) {
						departmentId = java.lang.Integer.parseInt(rs.getString("department_id"));
						gradeId = java.lang.Integer.parseInt(rs.getString("grade_id"));
						roleId = java.lang.Integer.parseInt(rs.getString("role_id"));
						sectionId = java.lang.Integer.parseInt(rs.getString("section_id"));
					}

					cboDepartment.setSelectedIndex(departmentId);
					cboGrade.setSelectedIndex(gradeId);
					cboRole.setSelectedIndex(roleId);
					cboSection.setSelectedIndex(sectionId);
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
							readQuery = "SELECT * FROM students WHERE first_name LIKE '" + findMe.charAt(0) + "%' ";
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
									deleteQuery = "DELETE FROM students WHERE student_id = ?";
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
							firstName = txtFirstName.getText().toString();
							middleName = txtMidName.getText().toString();
							lastName = txtLastName.getText().toString();
							email = txtEmail.getText().toString();
							password = String.valueOf(pwdPass.getPassword());
							address = txtAddress.getText().toString();
							gender = cboGender.getSelectedItem().toString();
							dateOfBirth = ((JTextField) dateDob.getDateEditor().getUiComponent()).getText();
							contactNumber = txtContact.getText().toString();
							attendance = cboAttendance.getSelectedItem().toString();
							department = cboAttendance.getSelectedItem().toString();
							grade = cboGrade.getSelectedItem().toString();
							role = cboRole.getSelectedItem().toString();
							section = cboSection.getSelectedItem().toString();

							fnameLength = firstName.length();
							midLength = middleName.length();
							lnameLength = middleName.length();
							pwdLength = password.length();
							cctLength = contactNumber.length();
							departmentId = cboDepartment.getSelectedIndex();
							gradeId = cboGrade.getSelectedIndex();
							roleId = cboRole.getSelectedIndex();
							sectionId = cboRole.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myDob = format.parse(dateOfBirth);
							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							dobYear = java.lang.Integer.parseInt(df.format(myDob));

							if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
									|| address.isEmpty() || isEmptyCbox(gender) || dateOfBirth.isEmpty()
									|| contactNumber.isEmpty() || isEmptyCbox(attendance) || isEmptyCbox(department)
									|| isEmptyCbox(grade) || isEmptyCbox(role) || isEmptyCbox(section)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (checkInputRange(fnameLength) || midLength > 30 || checkInputRange(lnameLength)
										|| pwdLength < 8 || pwdLength > 255 || checkInputRange(cctLength)
										|| cctLength < 10) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									MyEmailValidator emailValidator = new MyEmailValidator();
									if (!emailValidator.validate(email.trim())) {
										JOptionPane.showMessageDialog(messageFrame, dataEmailInvalid, currentPanel,
												JOptionPane.WARNING_MESSAGE);
									} else {

										if (!isDateRange(dobYear)) {
											JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
													JOptionPane.WARNING_MESSAGE);
										} else {
											if (!role.equals("student")) {
												JOptionPane.showMessageDialog(messageFrame, dataNotPrivileged,
														currentPanel, JOptionPane.ERROR_MESSAGE);
											} else {
												readQuery = "SELECT * FROM students WHERE first_name = ? AND contact = ?";
												ps = DBConnection.connectDB().prepareStatement(readQuery);
												ps.setString(1, firstName);
												ps.setString(2, contactNumber);
												rs = ps.executeQuery();
												while (rs.next()) {
													count++;
												}
												if (count > 0) {
													JOptionPane.showMessageDialog(messageFrame, dataDuplicate,
															currentPanel, JOptionPane.WARNING_MESSAGE);
													clearData();
												} else {
													createQuery = "INSERT INTO students(first_name, middle_name, last_name, email, password, address, gender, date_of_birth, contact, attendance, department_id, grade_id, role_id, section_id)"
															+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
													ps = DBConnection.connectDB().prepareStatement(createQuery);
													ps.setString(1, firstName);
													ps.setString(2, middleName);
													ps.setString(3, lastName);
													ps.setString(4, email);
													ps.setString(5, password);
													ps.setString(6, address);
													ps.setString(7, gender);
													ps.setObject(8, myDob);
													ps.setString(9, contactNumber);
													ps.setString(10, attendance);
													ps.setInt(11, departmentId);
													ps.setInt(12, gradeId);
													ps.setInt(13, roleId);
													ps.setInt(14, sectionId);
													try {
														if (ps.executeUpdate() > 0) {
															JOptionPane.showMessageDialog(messageFrame,
																	createDataSuccess, currentPanel,
																	JOptionPane.INFORMATION_MESSAGE);
															clearData();
															loadData();
															ps.close();
														} else {
															JOptionPane.showMessageDialog(messageFrame,
																	dataNoConnection, currentPanel,
																	JOptionPane.ERROR_MESSAGE);
															clearData();
															ps.close();
														}
													} catch (Exception ex) {
														JOptionPane.showMessageDialog(messageFrame,
																dataError + ex.getMessage(), currentPanel,
																JOptionPane.ERROR_MESSAGE);
														clearData();
														ps.close();
													}
												}
											}
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
							firstName = txtFirstName.getText().toString();
							middleName = txtMidName.getText().toString();
							lastName = txtLastName.getText().toString();
							email = txtEmail.getText().toString();
							password = String.valueOf(pwdPass.getPassword());
							address = txtAddress.getText().toString();
							gender = cboGender.getSelectedItem().toString();
							dateOfBirth = ((JTextField) dateDob.getDateEditor().getUiComponent()).getText();
							contactNumber = txtContact.getText().toString();
							attendance = cboAttendance.getSelectedItem().toString();
							department = cboAttendance.getSelectedItem().toString();
							grade = cboGrade.getSelectedItem().toString();
							role = cboRole.getSelectedItem().toString();
							section = cboSection.getSelectedItem().toString();

							fnameLength = firstName.length();
							midLength = middleName.length();
							lnameLength = middleName.length();
							pwdLength = password.length();
							cctLength = contactNumber.length();
							departmentId = cboDepartment.getSelectedIndex();
							gradeId = cboGrade.getSelectedIndex();
							roleId = cboRole.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myDob = format.parse(dateOfBirth);
							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							dobYear = java.lang.Integer.parseInt(df.format(myDob));

							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

							if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
									|| address.isEmpty() || isEmptyCbox(gender) || dateOfBirth.isEmpty()
									|| contactNumber.isEmpty() || isEmptyCbox(attendance) || isEmptyCbox(department)
									|| isEmptyCbox(grade) || isEmptyCbox(role) || isEmptyCbox(section)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (checkInputRange(fnameLength) || midLength > 30 || checkInputRange(lnameLength)
										|| pwdLength < 8 || pwdLength > 255 || checkInputRange(cctLength)
										|| cctLength < 10) {

									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									MyEmailValidator emailValidator = new MyEmailValidator();
									if (!emailValidator.validate(email.trim())) {
										JOptionPane.showMessageDialog(messageFrame, dataEmailInvalid, currentPanel,
												JOptionPane.WARNING_MESSAGE);
									} else {

										if (!isDateRange(dobYear)) {
											JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
													JOptionPane.WARNING_MESSAGE);
										} else {
											if (!role.equals("student")) {
												JOptionPane.showMessageDialog(messageFrame, dataNotPrivileged,
														currentPanel, JOptionPane.ERROR_MESSAGE);
											} else {
												updateQuery = "UPDATE students SET first_name = ?, middle_name = ?, last_name = ?, email = ?, password = ?, address = ?, gender = ?, date_of_birth = ?, contact = ?, attendance = ?, department_id = ?, grade_id = ?, role_id = ?, section_id = ? WHERE student_id = ?";
												ps = DBConnection.connectDB().prepareStatement(updateQuery);
												ps.setString(1, firstName);
												ps.setString(2, middleName);
												ps.setString(3, lastName);
												ps.setString(4, email);
												ps.setString(5, password);
												ps.setString(6, address);
												ps.setString(7, gender);
												ps.setObject(8, myDob);
												ps.setString(9, contactNumber);
												ps.setString(10, attendance);
												ps.setInt(11, departmentId);
												ps.setInt(12, gradeId);
												ps.setInt(13, roleId);
												ps.setInt(14, sectionId);
												ps.setInt(15, id);
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
													JOptionPane.showMessageDialog(messageFrame,
															dataError + ex.getMessage(), currentPanel,
															JOptionPane.ERROR_MESSAGE);
													System.out.print(ex.getStackTrace());
													ex.printStackTrace();
													clearData();
													ps.close();
												}
											}
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

		JLabel lblTitle = new JLabel("Students");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitle.setBounds(0, 0, 882, 48);
		contentPane.add(lblTitle);

		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setForeground(Color.WHITE);
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFirstName.setBounds(10, 60, 147, 30);
		contentPane.add(lblFirstName);

		txtFirstName = new JTextField();
		txtFirstName.setColumns(10);
		txtFirstName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtFirstName.setBounds(10, 90, 147, 30);
		contentPane.add(txtFirstName);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(10, 131, 147, 30);
		contentPane.add(lblPassword);

		JLabel lblMiddleName = new JLabel("Middle Name");
		lblMiddleName.setForeground(Color.WHITE);
		lblMiddleName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMiddleName.setBounds(192, 60, 147, 30);
		contentPane.add(lblMiddleName);

		txtMidName = new JTextField();
		txtMidName.setColumns(10);
		txtMidName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtMidName.setBounds(192, 90, 147, 30);
		contentPane.add(txtMidName);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(556, 59, 178, 30);
		contentPane.add(lblEmail);

		txtLastName = new JTextField();
		txtLastName.setColumns(10);
		txtLastName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtLastName.setBounds(374, 89, 147, 30);
		contentPane.add(txtLastName);

		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLastName.setBounds(374, 60, 147, 30);
		contentPane.add(lblLastName);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtEmail.setBounds(556, 89, 178, 30);
		contentPane.add(txtEmail);

		cboGender.setModel(
				new DefaultComboBoxModel(new String[] { "Select", "Male", "Female", "Trans", "Bi", "Lesbian" }));
		cboGender.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboGender.setBounds(769, 89, 103, 30);
		contentPane.add(cboGender);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setForeground(Color.WHITE);
		lblGender.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGender.setBounds(769, 60, 103, 30);
		contentPane.add(lblGender);

		pwdPass = new JPasswordField();
		pwdPass.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		pwdPass.setBounds(10, 161, 147, 30);
		contentPane.add(pwdPass);

		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtAddress.setBounds(556, 161, 178, 30);
		contentPane.add(txtAddress);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setForeground(Color.WHITE);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAddress.setBounds(556, 131, 178, 30);
		contentPane.add(lblAddress);

		JLabel lblDateOfBirth = new JLabel("Date of Birth");
		lblDateOfBirth.setForeground(Color.WHITE);
		lblDateOfBirth.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDateOfBirth.setBounds(192, 131, 147, 30);
		contentPane.add(lblDateOfBirth);

		dateDob.setBounds(192, 161, 147, 30);
		contentPane.add(dateDob);

		JLabel lblContactNumber = new JLabel("Contact Number");
		lblContactNumber.setForeground(Color.WHITE);
		lblContactNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblContactNumber.setBounds(374, 131, 147, 30);
		contentPane.add(lblContactNumber);

		txtContact = new JTextField();
		txtContact.setColumns(10);
		txtContact.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtContact.setBounds(374, 160, 147, 30);
		contentPane.add(txtContact);

		JLabel lblAttendance = new JLabel("Attendance");
		lblAttendance.setForeground(Color.WHITE);
		lblAttendance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAttendance.setBounds(769, 131, 103, 30);
		contentPane.add(lblAttendance);

		cboAttendance.setModel(new DefaultComboBoxModel(
				new String[] { "Select", "< 20%", "(20-50)%", "50%", "(50-70)%", "(70-90)%", ">90%" }));
		cboAttendance.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboAttendance.setBounds(769, 161, 103, 30);
		contentPane.add(cboAttendance);

		JLabel lblDepartmentId = new JLabel("Department");
		lblDepartmentId.setForeground(Color.WHITE);
		lblDepartmentId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDepartmentId.setBounds(10, 202, 147, 30);
		contentPane.add(lblDepartmentId);
		cboDepartment.setModel(new DefaultComboBoxModel(new String[] { "Select" }));

		cboDepartment.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboDepartment.setBounds(10, 234, 147, 30);
		contentPane.add(cboDepartment);

		JLabel lblGradeId = new JLabel("Grade");
		lblGradeId.setForeground(Color.WHITE);
		lblGradeId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGradeId.setBounds(192, 202, 147, 30);
		contentPane.add(lblGradeId);
		cboGrade.setModel(new DefaultComboBoxModel(new String[] { "Select" }));

		cboGrade.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboGrade.setBounds(192, 234, 147, 30);
		contentPane.add(cboGrade);

		JLabel lblRole = new JLabel("Role");
		lblRole.setForeground(Color.WHITE);
		lblRole.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRole.setBounds(374, 202, 147, 30);
		contentPane.add(lblRole);

		cboRole.setModel(new DefaultComboBoxModel(new String[] { "Select" }));
		cboRole.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboRole.setBounds(374, 234, 147, 30);
		contentPane.add(cboRole);

		JLabel lblSection = new JLabel("Section");
		lblSection.setForeground(Color.WHITE);
		lblSection.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSection.setBounds(556, 202, 147, 30);
		contentPane.add(lblSection);

		cboSection.setModel(new DefaultComboBoxModel(new String[] { "Select" }));
		cboSection.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboSection.setBounds(556, 234, 147, 30);
		contentPane.add(cboSection);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 882, 494);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);
	}
}
