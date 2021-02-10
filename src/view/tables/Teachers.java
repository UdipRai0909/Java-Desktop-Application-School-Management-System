package view.tables;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
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

import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
import utility.DBConnection;
import utility.MyEmailValidator;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;

public class Teachers extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTextField txtFirstName;
	private JTextField txtMidName;
	private JTextField txtLastName;
	private JTextField txtEmail;
	private JPasswordField pwdPass;
	private JTextField txtAddress;
	private JTextField txtContact;
	private JTable table;
	@SuppressWarnings("rawtypes")
	JComboBox cboGender = new JComboBox();
	JComboBox<String> cboDepartment = new JComboBox<String>();
	JComboBox<String> cboRole = new JComboBox<String>();
	JDateChooser dateDob = new JDateChooser();
	JSpinner spinnerHours = new JSpinner();

	// User input Variables
	String firstName = null, middleName = null, lastName = null, email = null, password = null, address = null,
			gender = null, dateOfBirth = null, contactNumber = null, hours = null, department = null, role = null,
			findMe = null;

	// Required Variables
	int fnameLength = 0, midLength = 0, lnameLength = 0, pwdLength = 0, cctLength = 0, departmentId = 0, roleId = 0;

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
	String currentPanel = "Teacher Panel";
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
		spinnerHours.setModel(new SpinnerNumberModel(0, 0, 6, 1));
		cboDepartment.setSelectedIndex(0);
		cboRole.setSelectedIndex(0);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM teachers";
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
					Teachers frame = new Teachers(Integer);
					frame.setVisible(true);
					frame.loadData();
					frame.clearData();
					frame.loadComboBoxDept();
					frame.loadComboBoxRole();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Teachers(final Integer roleNumId) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Teachers");
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
						"Date of Birth", "Contact", "Hours", "Department Id", "Role Id" }) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false, false, false, false, false, false };

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
				hours = table.getModel().getValueAt(row, 10).toString();
				department = table.getModel().getValueAt(row, 11).toString();
				role = table.getModel().getValueAt(row, 12).toString();

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
					spinnerHours.setValue(java.lang.Integer.valueOf(hours));

					id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

					readQuery = "SELECT * FROM teachers WHERE teacher_id = ?";
					ps = DBConnection.connectDB().prepareStatement(readQuery);
					ps.setInt(1, id);
					rs = ps.executeQuery();

					if (rs.next()) {
						departmentId = java.lang.Integer.parseInt(rs.getString("department_id"));
						roleId = java.lang.Integer.parseInt(rs.getString("role_id"));
					}

					cboDepartment.setSelectedIndex(departmentId);
					cboRole.setSelectedIndex(roleId);
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
							readQuery = "SELECT * FROM teachers WHERE first_name LIKE '" + findMe.charAt(0) + "%' ";
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
			if (roleNumId == 1) {

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
									deleteQuery = "DELETE FROM teachers WHERE teacher_id = ?";
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
							hours = spinnerHours.getValue().toString();
							department = cboDepartment.getSelectedItem().toString();
							role = cboRole.getSelectedItem().toString();

							fnameLength = firstName.length();
							midLength = middleName.length();
							lnameLength = middleName.length();
							pwdLength = password.length();
							cctLength = contactNumber.length();
							departmentId = cboDepartment.getSelectedIndex();
							roleId = cboRole.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myDob = format.parse(dateOfBirth);
							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							dobYear = java.lang.Integer.parseInt(df.format(myDob));

							if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
									|| address.isEmpty() || isEmptyCbox(gender) || dateOfBirth.isEmpty()
									|| contactNumber.isEmpty() || isEmptyCbox(department) || isEmptyCbox(role)) {
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
											if (role.equals("student")) {
												JOptionPane.showMessageDialog(messageFrame, dataNotPrivileged,
														currentPanel, JOptionPane.ERROR_MESSAGE);
											} else {
												readQuery = "SELECT * FROM teachers WHERE first_name = ? AND contact = ?";
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
													createQuery = "INSERT INTO teachers(first_name, middle_name, last_name, email, password, address, gender, date_of_birth, contact, hours, department_id, role_id)"
															+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
													ps.setString(10, hours);
													ps.setInt(11, departmentId);
													ps.setInt(12, roleId);
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
							hours = spinnerHours.getValue().toString();
							department = cboDepartment.getSelectedItem().toString();
							role = cboRole.getSelectedItem().toString();

							fnameLength = firstName.length();
							midLength = middleName.length();
							lnameLength = middleName.length();
							pwdLength = password.length();
							cctLength = contactNumber.length();
							departmentId = cboDepartment.getSelectedIndex();
							roleId = cboRole.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myDob = format.parse(dateOfBirth);
							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							dobYear = java.lang.Integer.parseInt(df.format(myDob));

							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

							if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()
									|| address.isEmpty() || isEmptyCbox(gender) || dateOfBirth.isEmpty()
									|| contactNumber.isEmpty() || isEmptyCbox(department) || isEmptyCbox(role)) {
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
											if (role.equals("student")) {
												JOptionPane.showMessageDialog(messageFrame, dataNotPrivileged,
														currentPanel, JOptionPane.ERROR_MESSAGE);
											} else {
												updateQuery = "UPDATE teachers SET first_name = ?, middle_name = ?, last_name = ?, email = ?, password = ?, address = ?, gender = ?, date_of_birth = ?, contact = ?, hours = ?, department_id = ?, role_id = ? WHERE teacher_id = ?";
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
												ps.setString(10, hours);
												ps.setInt(11, departmentId);
												ps.setInt(12, roleId);
												ps.setInt(13, id);
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
		txtSearch.setBounds(415, 455, 151, 23);
		contentPane.add(txtSearch);

		JLabel lblTitle = new JLabel("Teachers");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitle.setBounds(-2, 1, 882, 48);
		contentPane.add(lblTitle);

		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setForeground(Color.WHITE);
		lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFirstName.setBounds(8, 61, 147, 30);
		contentPane.add(lblFirstName);

		txtFirstName = new JTextField();
		txtFirstName.setColumns(10);
		txtFirstName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtFirstName.setBounds(8, 91, 147, 30);
		contentPane.add(txtFirstName);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(8, 132, 147, 30);
		contentPane.add(lblPassword);

		JLabel lblMiddleName = new JLabel("Middle Name");
		lblMiddleName.setForeground(Color.WHITE);
		lblMiddleName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMiddleName.setBounds(190, 61, 147, 30);
		contentPane.add(lblMiddleName);

		txtMidName = new JTextField();
		txtMidName.setColumns(10);
		txtMidName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtMidName.setBounds(190, 91, 147, 30);
		contentPane.add(txtMidName);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setForeground(Color.WHITE);
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(554, 60, 178, 30);
		contentPane.add(lblEmail);

		txtLastName = new JTextField();
		txtLastName.setColumns(10);
		txtLastName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtLastName.setBounds(372, 90, 147, 30);
		contentPane.add(txtLastName);

		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(Color.WHITE);
		lblLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLastName.setBounds(372, 61, 147, 30);
		contentPane.add(lblLastName);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtEmail.setBounds(554, 90, 178, 30);
		contentPane.add(txtEmail);
		cboGender.setModel(
				new DefaultComboBoxModel(new String[] { "Select", "Male", "Female", "Trans", "Bi", "Lesbian" }));

		cboGender.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboGender.setBounds(767, 90, 103, 30);
		contentPane.add(cboGender);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setForeground(Color.WHITE);
		lblGender.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGender.setBounds(767, 61, 103, 30);
		contentPane.add(lblGender);

		pwdPass = new JPasswordField();
		pwdPass.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		pwdPass.setBounds(8, 162, 147, 30);
		contentPane.add(pwdPass);

		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtAddress.setBounds(554, 162, 178, 30);
		contentPane.add(txtAddress);

		JLabel lblAddress = new JLabel("Address");
		lblAddress.setForeground(Color.WHITE);
		lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAddress.setBounds(554, 132, 178, 30);
		contentPane.add(lblAddress);

		JLabel lblDateOfBirth = new JLabel("Date of Birth");
		lblDateOfBirth.setForeground(Color.WHITE);
		lblDateOfBirth.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDateOfBirth.setBounds(190, 132, 147, 30);
		contentPane.add(lblDateOfBirth);

		JLabel lblContactNumber = new JLabel("Contact Number");
		lblContactNumber.setForeground(Color.WHITE);
		lblContactNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblContactNumber.setBounds(372, 132, 147, 30);
		contentPane.add(lblContactNumber);

		txtContact = new JTextField();
		txtContact.setColumns(10);
		txtContact.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtContact.setBounds(372, 161, 147, 30);
		contentPane.add(txtContact);

		JLabel lblHours = new JLabel("Hours");
		lblHours.setForeground(Color.WHITE);
		lblHours.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHours.setBounds(767, 132, 103, 30);
		contentPane.add(lblHours);

		JLabel lblDepartmentId = new JLabel("Department");
		lblDepartmentId.setForeground(Color.WHITE);
		lblDepartmentId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDepartmentId.setBounds(8, 203, 147, 30);
		contentPane.add(lblDepartmentId);
		cboDepartment.setModel(new DefaultComboBoxModel(new String[] { "Select" }));

		cboDepartment.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboDepartment.setBounds(8, 235, 147, 30);
		contentPane.add(cboDepartment);

		JLabel lblRole = new JLabel("Role");
		lblRole.setForeground(Color.WHITE);
		lblRole.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRole.setBounds(190, 203, 147, 30);
		contentPane.add(lblRole);
		cboRole.setModel(new DefaultComboBoxModel(new String[] { "Select" }));

		cboRole.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboRole.setBounds(190, 235, 147, 30);
		contentPane.add(cboRole);

		dateDob.setBounds(190, 162, 147, 30);
		contentPane.add(dateDob);
		spinnerHours.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));

		spinnerHours.setModel(new SpinnerNumberModel(0, 0, 6, 1));
		spinnerHours.setBounds(767, 162, 103, 30);
		contentPane.add(spinnerHours);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(-2, 1, 882, 494);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);
	}

	@SuppressWarnings("unused")
	private static class __Tmp {
		private static void __tmp() {
			@SuppressWarnings("unused")
			javax.swing.JPanel __wbp_panel = new javax.swing.JPanel();
		}
	}
}
