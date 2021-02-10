package view.tables;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;
import utility.DBConnection;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Fees extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTextField txtFeeCrs;
	private JTextField txtFeeType;
	private JTable table;
	TextArea textAreaDescription = new TextArea();
	JSpinner spinnerAmount = new JSpinner();

	// User input Variables
	String feeCrs = null, feeAmount = null, feeType = null, feeDescription = null, findMe = null;

	// Required Variables

	PreparedStatement ps = null;
	ResultSet rs = null;
	int count = 0, row = 0, id = 0, column = 0, numFee = 0;
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
	String dataOutOfBounds = "Out of range. Insert between 4-60 characters in fee crs and fee type."
			+ " And not more than Integer_MAX_Value for amount and 255 characters for description.";

	public void clearData() {

		txtFeeCrs.setText(null);
		spinnerAmount.setModel(new SpinnerNumberModel(0, null, null, 500));
		txtFeeType.setText(null);
		textAreaDescription.setText(null);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM fees";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public boolean checkInputRange(int input) {
		if (input < 4) {
			return true;
		} else if (input > 60) {
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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fees frame = new Fees(Integer);
					frame.clearData();
					frame.loadData();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Fees(Integer roleNumId) {
		
		setResizable(false);
		setTitle("Fees");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 914, 564);

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
				new String[] { "Id", "Crs Id", "Amount", "Type", "Description" }) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.getColumnModel().getColumn(3).setResizable(false);
		table.getColumnModel().getColumn(4).setResizable(false);
		scrollPane.setViewportView(table);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();

				try {

					feeCrs = table.getModel().getValueAt(row, 1).toString();
					feeAmount = table.getModel().getValueAt(row, 2).toString();
					feeType = table.getModel().getValueAt(row, 3).toString();
					feeDescription = table.getModel().getValueAt(row, 4).toString();

					txtFeeCrs.setText(feeCrs);
					spinnerAmount.setValue(java.lang.Integer.parseInt(feeAmount));
					txtFeeType.setText(feeType);
					textAreaDescription.setText(feeDescription);

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
							readQuery = "SELECT * FROM fees WHERE fee_type LIKE '" + findMe.charAt(0) + "%' ";
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
									deleteQuery = "DELETE FROM fees WHERE fee_id = ?";
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
							feeCrs = txtFeeCrs.getText().toString();
							feeAmount = spinnerAmount.getValue().toString();
							feeType = txtFeeType.getText().toString();
							feeDescription = textAreaDescription.getText().toString();

							numFee = java.lang.Integer.parseInt(feeAmount);

							if (feeCrs.isEmpty() || feeAmount.isEmpty() || feeType.isEmpty()) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (checkInputRange(feeCrs.length()) || checkInputRange(feeType.length())
										|| feeAmount.length() > 11 || feeDescription.length() > 255) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {

									readQuery = "SELECT * FROM fees WHERE fee_type = ?";
									ps = DBConnection.connectDB().prepareStatement(readQuery);
									ps.setString(1, feeType);
									rs = ps.executeQuery();

									while (rs.next()) {
										count++;
									}
									if (count > 0) {
										JOptionPane.showMessageDialog(messageFrame, dataDuplicate, currentPanel,
												JOptionPane.WARNING_MESSAGE);
										clearData();
									} else {
										createQuery = "INSERT INTO fees(fee_crs_id, fee_amount, fee_type, fee_description)"
												+ " VALUES(?, ?, ?, ?)";
										ps = DBConnection.connectDB().prepareStatement(createQuery);
										ps.setString(1, feeCrs);
										ps.setInt(2, numFee);
										ps.setString(3, feeType);
										ps.setString(4, feeDescription);
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
							feeCrs = txtFeeCrs.getText().toString();
							feeAmount = spinnerAmount.getValue().toString();
							feeType = txtFeeType.getText().toString();
							feeDescription = textAreaDescription.getText().toString();

							numFee = java.lang.Integer.parseInt(feeAmount);
							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

							if (feeCrs.isEmpty() || feeAmount.isEmpty() || feeType.isEmpty()) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								if (checkInputRange(feeCrs.length()) || checkInputRange(feeType.length())
										|| feeAmount.length() > 11 || feeDescription.length() > 255) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									updateQuery = "UPDATE fees SET fee_crs_id = ?, fee_amount = ?, fee_type = ?, fee_description = ? WHERE fee_id = ?";
									ps = DBConnection.connectDB().prepareStatement(updateQuery);
									ps.setString(1, feeCrs);
									ps.setInt(2, numFee);
									ps.setString(3, feeType);
									ps.setString(4, feeDescription);
									ps.setInt(5, id);
									try {
										if (ps.executeUpdate() > 0) {
											JOptionPane.showMessageDialog(messageFrame, updateDataSuccess, currentPanel,
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

		JLabel lblFees = new JLabel("Fees");
		lblFees.setHorizontalAlignment(SwingConstants.CENTER);
		lblFees.setForeground(Color.WHITE);
		lblFees.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblFees.setBounds(0, 0, 882, 48);
		contentPane.add(lblFees);

		JLabel lblFeeCrs = new JLabel("CRS Id");
		lblFeeCrs.setForeground(Color.WHITE);
		lblFeeCrs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFeeCrs.setBounds(10, 60, 147, 30);
		contentPane.add(lblFeeCrs);

		JLabel lblAmount = new JLabel("Amount");
		lblAmount.setForeground(Color.WHITE);
		lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAmount.setBounds(192, 60, 147, 30);
		contentPane.add(lblAmount);

		txtFeeCrs = new JTextField();
		txtFeeCrs.setColumns(10);
		txtFeeCrs.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtFeeCrs.setBounds(10, 90, 147, 30);
		contentPane.add(txtFeeCrs);

		txtFeeType = new JTextField();
		txtFeeType.setColumns(10);
		txtFeeType.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtFeeType.setBounds(374, 90, 147, 30);
		contentPane.add(txtFeeType);

		JLabel lblDescription = new JLabel("Description");
		lblDescription.setForeground(Color.WHITE);
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDescription.setBounds(556, 59, 103, 30);
		contentPane.add(lblDescription);

		textAreaDescription.setBounds(556, 90, 316, 101);
		contentPane.add(textAreaDescription);

		JLabel lblFeeType = new JLabel("Fee type");
		lblFeeType.setForeground(Color.WHITE);
		lblFeeType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFeeType.setBounds(374, 59, 147, 30);
		contentPane.add(lblFeeType);

		spinnerAmount.setModel(new SpinnerNumberModel(0, null, null, 500));
		spinnerAmount.setBounds(192, 90, 147, 30);
		contentPane.add(spinnerAmount);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 882, 494);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);

	}

}
