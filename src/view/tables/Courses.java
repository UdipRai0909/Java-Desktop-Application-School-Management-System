package view.tables;

import java.awt.EventQueue;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;
import utility.DBConnection;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

public class Courses extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTable table;
	JComboBox<String> cboDepartment = new JComboBox<String>();
	JComboBox<String> cboFeeType = new JComboBox<String>();
	TextArea textAreaRemarks = new TextArea();
	JSpinner spinnerCredits = new JSpinner();

	// User input Variables
	String title = null, credits = null, remarks = null, department = null, feeType = null, findMe = null;

	// Required Variables
	int departmentId = 0,  feeId = 0;
	double creditNum = 0.0;

	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0, row = 0, id = 0, column = 0;
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
	String dataRange = "Credits should be between (0.5 - 3.0)";
	private JTextField txtSearch;
	private JTextField txtTitle;

	public void clearData() {
		txtTitle.setText(null);
		spinnerCredits.setModel(new SpinnerNumberModel(0.0, 0.0, 3.0, 0.5));
		textAreaRemarks.setText(null);
		cboDepartment.setSelectedIndex(0);
		cboDepartment.setSelectedIndex(0);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM courses";
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
					Courses frame = new Courses(Integer);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Courses(Integer roleNumId) {
		setTitle("Courses");
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
				new String[] { "Id", "Title", "Credits", "Remarks", "Description", "Fee Type" }) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false, false };

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
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					row = table.getSelectedRow();

					title = table.getModel().getValueAt(row, 1).toString();
					credits = table.getModel().getValueAt(row, 2).toString();
					remarks = table.getModel().getValueAt(row, 3).toString();
					department = table.getModel().getValueAt(row, 4).toString();

					txtTitle.setText(title);
					spinnerCredits.setValue(Double.parseDouble(credits));
					textAreaRemarks.setText(remarks);

					id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

					readQuery = "SELECT * FROM courses WHERE course_id = ?";
					ps = DBConnection.connectDB().prepareStatement(readQuery);
					ps.setInt(1, id);
					rs = ps.executeQuery();

					if (rs.next()) {
						departmentId = java.lang.Integer.parseInt(rs.getString("department_id"));
					}

					cboDepartment.setSelectedIndex(departmentId);
					ps.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
							JOptionPane.ERROR_MESSAGE);
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
		btnRefresh.setBorder(null);
		btnRefresh.setForeground(new Color(0, 0, 0));
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 12));
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
							readQuery = "SELECT * FROM courses WHERE title LIKE '" + findMe.charAt(0) + "%' ";
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
									deleteQuery = "DELETE FROM courses WHERE course_id = ?";
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
				btnDelete.setBorder(null);
				btnDelete.setForeground(Color.WHITE);
				btnDelete.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnDelete.setBackground(Color.RED);
				btnDelete.setBounds(212, 454, 89, 23);
				contentPane.add(btnDelete);

				JButton btnCreate = new JButton("Add");
				btnCreate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						// User inputs
						title = txtTitle.getText().toString();
						credits = spinnerCredits.getValue().toString();
						remarks = textAreaRemarks.getText().toString();
						department = cboDepartment.getSelectedItem().toString();
						feeType = cboFeeType.getSelectedItem().toString();

						departmentId = cboDepartment.getSelectedIndex();
						feeId = cboFeeType.getSelectedIndex();

						if (title.isEmpty() || credits.isEmpty() || isEmptyCbox(department) || isEmptyCbox(feeType)) {
							JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
									JOptionPane.WARNING_MESSAGE);
							clearData();
						} else {
							if (checkInputRange(title.length()) || credits.length() > 3 || remarks.length() > 255) {
								JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();

							} else {
								try {
									creditNum = Double.parseDouble(credits);
									if (creditNum < 0.5 || creditNum > 3) {
										JOptionPane.showMessageDialog(messageFrame, dataRange, currentPanel,
												JOptionPane.WARNING_MESSAGE);
										clearData();
									} else {

										readQuery = "SELECT * FROM courses WHERE title = ?";
										ps = DBConnection.connectDB().prepareStatement(readQuery);
										ps.setString(1, title);
										rs = ps.executeQuery();

										while (rs.next()) {
											count++;
										}
										if (count > 0) {
											JOptionPane.showMessageDialog(messageFrame, dataDuplicate, currentPanel,
													JOptionPane.WARNING_MESSAGE);
											clearData();
										} else {
											createQuery = "INSERT INTO courses(title, credits, remarks, department_id, fee_id) VALUES(?, ?, ?, ?, ?)";
											ps = DBConnection.connectDB().prepareStatement(createQuery);
											ps.setString(1, title);
											ps.setDouble(2, creditNum);
											ps.setString(3, remarks);
											ps.setInt(4, departmentId);
											ps.setInt(5, feeId);
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
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(),
											currentPanel, JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				btnCreate.setBorder(null);
				btnCreate.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnCreate.setBackground(Color.GREEN);
				btnCreate.setBounds(10, 454, 89, 23);
				contentPane.add(btnCreate);

				JButton btnUpdate = new JButton("Update");
				btnUpdate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						// User inputs
						title = txtTitle.getText().toString();
						credits = spinnerCredits.getValue().toString();
						remarks = textAreaRemarks.getText().toString();
						department = cboDepartment.getSelectedItem().toString();

						departmentId = cboDepartment.getSelectedIndex();
						row = table.getSelectedRow();
						id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

						if (title.isEmpty() || credits.isEmpty() || isEmptyCbox(department)) {
							JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
									JOptionPane.WARNING_MESSAGE);
							clearData();
						} else {
							if (checkInputRange(title.length()) || credits.length() > 3 || remarks.length() > 255) {
								JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();

							} else {
								try {
									creditNum = Double.parseDouble(credits);
									if (creditNum < 0.5 || creditNum > 3) {
										JOptionPane.showMessageDialog(messageFrame, dataRange, currentPanel,
												JOptionPane.WARNING_MESSAGE);
										clearData();
									} else {
										updateQuery = "UPDATE courses SET title = ?, credits = ?, remarks = ?, department_id = ?, fee_id WHERE course_id = ?";
										ps = DBConnection.connectDB().prepareStatement(updateQuery);
										ps.setString(1, title);
										ps.setDouble(2, creditNum);
										ps.setString(3, remarks);
										ps.setInt(4, departmentId);
										ps.setInt(5, feeId);
										ps.setInt(6, id);
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
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(),
											currentPanel, JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
				});
				btnUpdate.setBorder(null);
				btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
				btnUpdate.setBackground(new Color(0, 191, 255));
				btnUpdate.setBounds(109, 454, 89, 23);
				contentPane.add(btnUpdate);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
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

		JLabel lblCourseTitle = new JLabel("Title");
		lblCourseTitle.setForeground(Color.WHITE);
		lblCourseTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCourseTitle.setBounds(10, 60, 147, 30);
		contentPane.add(lblCourseTitle);

		JLabel lblCredits = new JLabel("Credits");
		lblCredits.setForeground(Color.WHITE);
		lblCredits.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredits.setBounds(192, 60, 75, 30);
		contentPane.add(lblCredits);

		JLabel lblRemarks = new JLabel("Remarks");
		lblRemarks.setForeground(Color.WHITE);
		lblRemarks.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRemarks.setBounds(484, 54, 103, 30);
		contentPane.add(lblRemarks);

		JLabel lblDepartmentId = new JLabel("Department");
		lblDepartmentId.setForeground(Color.WHITE);
		lblDepartmentId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDepartmentId.setBounds(302, 60, 147, 30);
		contentPane.add(lblDepartmentId);
		cboDepartment.setModel(new DefaultComboBoxModel<String>(new String[] { "Select" }));

		cboDepartment.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboDepartment.setBounds(302, 90, 147, 30);
		contentPane.add(cboDepartment);

		textAreaRemarks.setBounds(484, 85, 388, 101);
		contentPane.add(textAreaRemarks);

		txtTitle = new JTextField();
		txtTitle.setColumns(10);
		txtTitle.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtTitle.setBounds(10, 90, 147, 30);
		contentPane.add(txtTitle);
		spinnerCredits.setModel(new SpinnerNumberModel(0.0, 0.0, 3.0, 0.5));

		spinnerCredits.setBounds(192, 90, 75, 30);
		contentPane.add(spinnerCredits);
		

		cboFeeType.setModel(new DefaultComboBoxModel<String>(new String[] {"Select"}));
		cboFeeType.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboFeeType.setBounds(10, 162, 147, 30);
		contentPane.add(cboFeeType);
		
		JLabel lblFeeType = new JLabel("Fee Type");
		lblFeeType.setForeground(Color.WHITE);
		lblFeeType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFeeType.setBounds(10, 132, 147, 30);
		contentPane.add(lblFeeType);
		
				JLabel lblBackground = new JLabel("");
				lblBackground.setBounds(0, 0, 882, 494);
				lblBackground.setIcon(iconBackground);
				contentPane.add(lblBackground);
	}
}
