package view.tables;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import net.proteanit.sql.DbUtils;
import utility.DBConnection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Departments extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;
	private JPanel contentPane;
	private JTable table;
	private JLabel lblDepartmentName;
	private JTextField txtDepartmentName;
	private JTextField txtNumberOfTeachers;
	final TextArea textAreaDescription = new TextArea();
	private JTextField txtSearch;

	// User input Variables
	String depName = null, numTeachers1 = null, myDesc = null, findMe = null;

	// Required Variables
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
	String currentPanel = "Department Panel";
	String dataNoConnection = "No connection established!";
	String dataDuplicate = "Duplicate value! The data already exists.";
	String dataDeleteMsg = "Are you sure you want to delete ?";
	String dataFound = " items found.";
	String dataNotFound = "No such data found!";
	String dataRange = "Number of teachers should be between (1-20)";

	public void clearData() {
		txtDepartmentName.setText(null);
		txtNumberOfTeachers.setText(null);
		textAreaDescription.setText(null);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM departments";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Departments frame = new Departments(Integer);
					frame.setVisible(true);
					frame.clearData();
					frame.loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Departments(Integer roleNumId) {
		setResizable(false);
		setTitle("Departments");
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
				new String[] { "Id", "Name", "Number of teachers", "Description" }) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();

				depName = table.getModel().getValueAt(row, 1).toString();
				numTeachers1 = table.getModel().getValueAt(row, 2).toString();
				myDesc = table.getModel().getValueAt(row, 3).toString();

				txtDepartmentName.setText(depName);
				txtNumberOfTeachers.setText(numTeachers1);
				textAreaDescription.setText(myDesc);
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
							readQuery = "SELECT * FROM departments WHERE dep_name LIKE '" + findMe.charAt(0) + "%' ";
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
			if(roleNumId == 1 || roleNumId == 2) {
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
									deleteQuery = "DELETE FROM departments WHERE department_id = ?";
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
						depName = txtDepartmentName.getText().toString();
						numTeachers1 = txtNumberOfTeachers.getText();
						myDesc = textAreaDescription.getText();

						if (depName.isEmpty() || numTeachers1.isEmpty()) {
							JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel, JOptionPane.WARNING_MESSAGE);
							clearData();
						} else {
							if (depName.length() < 3 || depName.length() > 60) {
								JOptionPane.showMessageDialog(messageFrame, dataInvalid, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();

							} else {
								try {
									int numTeachers2 = java.lang.Integer.parseInt(numTeachers1);
									if (numTeachers2 < 1 || numTeachers2 > 20) {
										JOptionPane.showMessageDialog(messageFrame, dataRange, currentPanel,
												JOptionPane.WARNING_MESSAGE);
										clearData();
									} else {
										if (myDesc.length() > 255) {
											JOptionPane.showMessageDialog(messageFrame, dataInvalid, currentPanel,
													JOptionPane.WARNING_MESSAGE);
											clearData();
										} else {

											readQuery = "SELECT * FROM departments WHERE dep_name = ?";
											ps = DBConnection.connectDB().prepareStatement(readQuery);
											ps.setString(1, depName);
											rs = ps.executeQuery();

											while (rs.next()) {
												count++;
											}
											if (count > 0) {
												JOptionPane.showMessageDialog(messageFrame, dataDuplicate, currentPanel,
														JOptionPane.WARNING_MESSAGE);
												clearData();
											} else {
												createQuery = "INSERT INTO departments(dep_name, no_of_teachers, description) VALUES(?, ?, ?)";
												ps = DBConnection.connectDB().prepareStatement(createQuery);
												ps.setString(1, depName);
												ps.setInt(2, numTeachers2);
												ps.setString(3, myDesc);
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
						depName = txtDepartmentName.getText().toString();
						numTeachers1 = txtNumberOfTeachers.getText();
						myDesc = textAreaDescription.getText();
						row = table.getSelectedRow();

						try {
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

							if (depName.isEmpty() || numTeachers1.isEmpty()) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (depName.length() < 3 || depName.length() > 60) {
									JOptionPane.showMessageDialog(messageFrame, dataInvalid, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();

								} else {
									try {
										int numTeachers2 = java.lang.Integer.parseInt(numTeachers1);
										if (numTeachers2 < 1 || numTeachers2 > 20) {
											JOptionPane.showMessageDialog(messageFrame, dataRange, currentPanel,
													JOptionPane.WARNING_MESSAGE);
											clearData();
										} else {
											if (myDesc.length() > 255) {
												JOptionPane.showMessageDialog(messageFrame, dataInvalid, currentPanel,
														JOptionPane.WARNING_MESSAGE);
												clearData();
											} else {
												updateQuery = "UPDATE departments SET dep_name = ?, no_of_teachers = ?, description = ? WHERE department_id = ?";
												ps = DBConnection.connectDB().prepareStatement(updateQuery);
												ps.setString(1, depName);
												ps.setInt(2, numTeachers2);
												ps.setString(3, myDesc);
												ps.setInt(4, id);
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
									} catch (Exception ex) {
										JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
												JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(messageFrame, dataError + ex.getMessage(), currentPanel,
									JOptionPane.ERROR_MESSAGE);
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

		// All the inputs
		txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtSearch.setBounds(417, 454, 151, 23);
		contentPane.add(txtSearch);

		JLabel lblTitle = new JLabel("Departments");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setBounds(0, 0, 882, 48);
		contentPane.add(lblTitle);

		lblDepartmentName = new JLabel("Department Name");
		lblDepartmentName.setForeground(Color.WHITE);
		lblDepartmentName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDepartmentName.setBounds(10, 60, 147, 30);
		contentPane.add(lblDepartmentName);

		txtDepartmentName = new JTextField();
		txtDepartmentName.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtDepartmentName.setBounds(10, 90, 147, 30);
		contentPane.add(txtDepartmentName);
		txtDepartmentName.setColumns(10);

		JLabel lblNumberOfTeachers = new JLabel("Number of teachers");
		lblNumberOfTeachers.setForeground(Color.WHITE);
		lblNumberOfTeachers.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumberOfTeachers.setBounds(10, 131, 147, 30);
		contentPane.add(lblNumberOfTeachers);

		txtNumberOfTeachers = new JTextField();
		txtNumberOfTeachers.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtNumberOfTeachers.setColumns(10);
		txtNumberOfTeachers.setBounds(10, 161, 147, 30);
		contentPane.add(txtNumberOfTeachers);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setForeground(Color.WHITE);
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDescription.setBounds(212, 60, 147, 30);
		contentPane.add(lblDescription);

		textAreaDescription.setBounds(212, 90, 251, 101);
		contentPane.add(textAreaDescription);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 882, 488);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);
	}
}
