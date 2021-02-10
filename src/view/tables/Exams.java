package view.tables;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import com.toedter.calendar.JDateChooser;

import net.proteanit.sql.DbUtils;
import utility.DBConnection;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Exams extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTable table;
	private JTextField txtSearch;
	private JTextField txtSubject;
	private JTextField txtTime;
	TextArea textAreaRemarks = new TextArea();
	@SuppressWarnings("rawtypes")
	JComboBox cboExamType = new JComboBox();
	JComboBox<String> cboDepartment = new JComboBox<String>();
	JComboBox<String> cboFeeType = new JComboBox<String>();
	JDateChooser dateExam = new JDateChooser();

	// User input Variables
	String examType = null, subject = null, examDate = null, time = null, remarks = null, department = null, feeType = null,
			findMe = null;

	// Required Variables
	int departmentId = 0, feeId = 0;

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
	String currentPanel = "Exam Panel";
	String dataNoConnection = "No connection established!";
	String dataDuplicate = "Duplicate value! The data already exists.";
	String dataDeleteMsg = "Are you sure you want to delete ?";
	String dataFound = " items found.";
	String dataNotFound = "No such data found!";
	String dataOutOfBounds = "The value is out of bounds.";
	String dataNewYear = "Choose 2021 as year.";

	public void clearData() {
		cboExamType.setSelectedIndex(0);
		txtSubject.setText(null);
		dateExam.setCalendar(null);
		txtTime.setText(null);
		textAreaRemarks.setText(null);
		cboDepartment.setSelectedIndex(0);
		cboFeeType.setSelectedIndex(0);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM exams";
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
	
	public void loadComboBoxFeeType() {
		try {
			readQuery = "SELECT fee_type FROM fees";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			while (rs.next()) {
				cboFeeType.addItem(rs.getString("fee_type"));
			}
			ps.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public boolean isOutOfRange(int test) {

		if (test < 0) {
			return true;
		} else if (test > 60) {
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
		if (year == 2021) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Exams frame = new Exams(Integer);
					frame.setVisible(true);
					frame.clearData();
					frame.loadData();
					frame.loadComboBoxDept();
					frame.loadComboBoxFeeType();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Exams(Integer roleNumId) {
		
		setResizable(false);
		setTitle("Exams");
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
				new String[] { "Id", "Exam Type", "Subject", "Exam Date", "Time", "Remarks", "Department Id", "Fee Id"}) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false };

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
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();

				examType = table.getModel().getValueAt(row, 1).toString();
				subject = table.getModel().getValueAt(row, 2).toString();
				examDate = table.getModel().getValueAt(row, 3).toString();
				time = table.getModel().getValueAt(row, 4).toString();
				remarks = table.getModel().getValueAt(row, 5).toString();
				department = table.getModel().getValueAt(row, 6).toString();
				feeType = table.getModel().getValueAt(row, 7).toString();

				try {
					SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-mm-dd");
					Date selectedDate = sd1.parse(examDate);

					cboExamType.setSelectedItem(examType);
					txtSubject.setText(subject);
					dateExam.setDate(selectedDate);
					txtTime.setText(time);
					textAreaRemarks.setText(remarks);

					id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

					readQuery = "SELECT * FROM exams WHERE exam_id = ?";
					ps = DBConnection.connectDB().prepareStatement(readQuery);
					ps.setInt(1, id);
					rs = ps.executeQuery();

					if (rs.next()) {
						departmentId = java.lang.Integer.parseInt(rs.getString("department_id"));
						feeId = java.lang.Integer.parseInt(rs.getString("fee_id"));
					}

					cboDepartment.setSelectedIndex(departmentId);
					cboFeeType.setSelectedIndex(feeId);
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
							readQuery = "SELECT * FROM exams WHERE exam_type LIKE '" + findMe.charAt(0) + "%' ";
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
									deleteQuery = "DELETE FROM exams WHERE exam_id = ?";
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
							examType = cboExamType.getSelectedItem().toString();
							subject = txtSubject.getText().toString();
							examDate = ((JTextField) dateExam.getDateEditor().getUiComponent()).getText();
							time = txtTime.getText().toString();
							remarks = textAreaRemarks.getText().toString();
							department = cboDepartment.getSelectedItem().toString();
							feeType = cboFeeType.getSelectedItem().toString();

							departmentId = cboDepartment.getSelectedIndex();
							feeId = cboFeeType.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myDob = format.parse(examDate);
							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							dobYear = java.lang.Integer.parseInt(df.format(myDob));

							if (isEmptyCbox(examType) || subject.isEmpty() || examDate.isEmpty() || time.isEmpty()
									|| isEmptyCbox(department) || isEmptyCbox(feeType)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (isOutOfRange(examType.length()) || isOutOfRange(subject.length())
										|| checkInputRange(time.length()) || remarks.length() > 255) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									if (!isDateRange(dobYear)) {
										JOptionPane.showMessageDialog(messageFrame, dataNewYear, currentPanel,
												JOptionPane.WARNING_MESSAGE);

									} else {
										createQuery = "INSERT INTO exams(exam_type, subject, exam_date, time, remarks, department_id, fee_id)"
												+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
										ps = DBConnection.connectDB().prepareStatement(createQuery);
										ps.setString(1, examType);
										ps.setString(2, subject);
										ps.setObject(3, myDob);
										ps.setString(4, time);
										ps.setString(5, remarks);
										ps.setInt(6, departmentId);
										ps.setInt(7, feeId);
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
							examType = cboExamType.getSelectedItem().toString();
							subject = txtSubject.getText().toString();
							examDate = ((JTextField) dateExam.getDateEditor().getUiComponent()).getText();
							time = txtTime.getText().toString();
							remarks = textAreaRemarks.getText().toString();
							department = cboDepartment.getSelectedItem().toString();
							feeType = cboFeeType.getSelectedItem().toString();

							departmentId = cboDepartment.getSelectedIndex();
							feeId = cboFeeType.getSelectedIndex();

							SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
							Date myDob = format.parse(examDate);
							SimpleDateFormat df = new SimpleDateFormat("yyyy");
							dobYear = java.lang.Integer.parseInt(df.format(myDob));

							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

							if (isEmptyCbox(examType) || subject.isEmpty() || examDate.isEmpty() || time.isEmpty()
									|| isEmptyCbox(department) || isEmptyCbox(feeType)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (isOutOfRange(examType.length()) || isOutOfRange(subject.length())
										|| checkInputRange(time.length()) || remarks.length() > 255) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									if (!isDateRange(dobYear)) {
										JOptionPane.showMessageDialog(messageFrame, dataNewYear, currentPanel,
												JOptionPane.WARNING_MESSAGE);

									} else {
										updateQuery = "UPDATE exams SET exam_type = ?, subject = ?, exam_date = ?, time = ?, remarks = ?, department_id = ?, fee_id = ? WHERE exam_id = ?";
										ps = DBConnection.connectDB().prepareStatement(updateQuery);
										ps.setString(1, examType);
										ps.setString(2, subject);
										ps.setObject(3, myDob);
										ps.setString(4, time);
										ps.setString(5, remarks);
										ps.setInt(6, departmentId);
										ps.setInt(7, feeId);
										ps.setInt(8, id);
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

		JLabel lblTitle = new JLabel("Exams");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitle.setBounds(0, 0, 882, 48);
		contentPane.add(lblTitle);

		JLabel lblExamType = new JLabel("Exam Type");
		lblExamType.setForeground(Color.WHITE);
		lblExamType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblExamType.setBounds(10, 60, 147, 30);
		contentPane.add(lblExamType);

		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setForeground(Color.WHITE);
		lblSubject.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSubject.setBounds(192, 60, 147, 30);
		contentPane.add(lblSubject);

		txtSubject = new JTextField();
		txtSubject.setColumns(10);
		txtSubject.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtSubject.setBounds(192, 90, 147, 30);
		contentPane.add(txtSubject);

		JLabel lblTime = new JLabel("Time");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTime.setBounds(10, 132, 178, 30);
		contentPane.add(lblTime);

		JLabel lblExamDate = new JLabel("Exam Date");
		lblExamDate.setForeground(Color.WHITE);
		lblExamDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblExamDate.setBounds(374, 60, 147, 30);
		contentPane.add(lblExamDate);

		txtTime = new JTextField();
		txtTime.setColumns(10);
		txtTime.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtTime.setBounds(10, 162, 147, 30);
		contentPane.add(txtTime);

		JLabel lblRemarks = new JLabel("Remarks");
		lblRemarks.setForeground(Color.WHITE);
		lblRemarks.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRemarks.setBounds(556, 59, 103, 30);
		contentPane.add(lblRemarks);

		JLabel lblDepartmentId = new JLabel("Department");
		lblDepartmentId.setForeground(Color.WHITE);
		lblDepartmentId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDepartmentId.setBounds(192, 131, 147, 30);
		contentPane.add(lblDepartmentId);
		cboDepartment.setModel(new DefaultComboBoxModel(new String[] { "Select" }));

		cboDepartment.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboDepartment.setBounds(192, 163, 147, 30);
		contentPane.add(cboDepartment);

		cboExamType.setModel(new DefaultComboBoxModel(new String[] { "Select", "Essay", "Multiple Choice", "Open-book",
				"Take-home", "Problem/case-based", "Terminal" }));
		cboExamType.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboExamType.setBounds(10, 90, 147, 30);
		contentPane.add(cboExamType);

		dateExam.setBounds(374, 90, 147, 30);
		contentPane.add(dateExam);

		textAreaRemarks.setBounds(556, 90, 316, 101);
		contentPane.add(textAreaRemarks);
		
		JLabel lblFeeType = new JLabel("Fee type");
		lblFeeType.setForeground(Color.WHITE);
		lblFeeType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFeeType.setBounds(374, 132, 147, 30);
		contentPane.add(lblFeeType);
		

		cboFeeType.setModel(new DefaultComboBoxModel(new String[] {"Select"}));
		cboFeeType.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboFeeType.setBounds(374, 164, 147, 30);
		contentPane.add(cboFeeType);
		
				JLabel lblBackground = new JLabel("");
				lblBackground.setBounds(0, 0, 882, 494);
				lblBackground.setIcon(iconBackground);
				contentPane.add(lblBackground);
	}
}
