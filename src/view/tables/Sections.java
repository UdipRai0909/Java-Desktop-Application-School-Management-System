package view.tables;

import java.awt.EventQueue;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;
import utility.DBConnection;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class Sections extends JFrame {

	private static final long serialVersionUID = 1L;
	protected static final Integer Integer = null;

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTextField txtRoom;
	private JTable table;
	JSpinner spinnerNumStd = new JSpinner();
	JSpinner spinnerNumSeats = new JSpinner();
	@SuppressWarnings("rawtypes")
	JComboBox cboStatus = new JComboBox();
	@SuppressWarnings("rawtypes")
	JComboBox cboRating = new JComboBox();

	// User input Variables
	String roomVal = null, numStudents = null, numSeats = null, rating = null, status = null, findMe = null;

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
	String currentPanel = "Section Panel";
	String dataNoConnection = "No connection established!";
	String dataDuplicate = "Duplicate value! The data already exists.";
	String dataDeleteMsg = "Are you sure you want to delete ?";
	String dataFound = " items found.";
	String dataNotFound = "No such data found!";
	String dataOutOfBounds = "The value is out of bounds.";

	public void clearData() {
		txtRoom.setText(null);
		spinnerNumStd.setModel(new SpinnerNumberModel(15, 15, 200, 1));
		spinnerNumSeats.setModel(new SpinnerNumberModel(15, 15, 200, 1));
		cboRating.setSelectedIndex(0);
		cboStatus.setSelectedIndex(0);
		count = 0;
	}

	public void loadData() {
		try {
			readQuery = "SELECT * FROM sections";
			ps = DBConnection.connectDB().prepareStatement(readQuery);
			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			ps.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public boolean isOutOfRange(int test) {

		if (test < 15) {
			return true;
		} else if (test > 200) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkInputRange(int input) {
		if (input < 0) {
			return true;
		} else if (input > 10) {
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
					Sections frame = new Sections(Integer);
					frame.setVisible(true);
					frame.clearData();
					frame.loadData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Sections(Integer roleNumId) {
		
		setResizable(false);
		setTitle("Sections");
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
				new String[] { "Id", "Room", "Number of students", "Number of seats", "Rating", "Status" }) {
			/**
			 * 
			 */
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
				row = table.getSelectedRow();
				try {
					roomVal = table.getModel().getValueAt(row, 1).toString();
					numStudents = table.getModel().getValueAt(row, 2).toString();
					numSeats = table.getModel().getValueAt(row, 3).toString();
					rating = table.getModel().getValueAt(row, 4).toString();
					status = table.getModel().getValueAt(row, 5).toString();

					txtRoom.setText(roomVal);
					spinnerNumStd.setValue(java.lang.Integer.parseInt(numStudents));
					spinnerNumSeats.setValue(java.lang.Integer.parseInt(numSeats));
					cboRating.setSelectedItem(rating);
					cboStatus.setSelectedItem(status);
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
							readQuery = "SELECT * FROM sections WHERE room_number LIKE '" + findMe.charAt(0) + "%' ";
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
									deleteQuery = "DELETE FROM sections WHERE section_id = ?";
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
							roomVal = txtRoom.getText().toString();
							numStudents = spinnerNumStd.getValue().toString();
							numSeats = spinnerNumSeats.getValue().toString();
							rating = cboRating.getSelectedItem().toString();
							status = cboStatus.getSelectedItem().toString();

							if (roomVal.isEmpty() || isEmptyCbox(rating) || isEmptyCbox(status)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								int numStudents1 = java.lang.Integer.parseInt(numStudents);
								int numSeats1 = java.lang.Integer.parseInt(numSeats);
								if (isOutOfRange(numStudents1) || isOutOfRange(numSeats1)
										|| checkInputRange(roomVal.length()) || checkInputRange(status.length())) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									createQuery = "INSERT INTO sections(room_number, no_of_students, no_of_seats, rating, active_status)"
											+ " VALUES(?, ?, ?, ?, ?)";
									ps = DBConnection.connectDB().prepareStatement(createQuery);
									ps.setString(1, roomVal);
									ps.setInt(2, numStudents1);
									ps.setInt(3, numSeats1);
									ps.setString(4, rating);
									ps.setString(5, status);
									try {
										if (ps.executeUpdate() > 0) {
											JOptionPane.showMessageDialog(messageFrame, createDataSuccess, currentPanel,
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
							roomVal = txtRoom.getText().toString();
							numStudents = spinnerNumStd.getValue().toString();
							numSeats = spinnerNumSeats.getValue().toString();
							rating = cboRating.getSelectedItem().toString();
							status = cboStatus.getSelectedItem().toString();

							row = table.getSelectedRow();
							id = java.lang.Integer.parseInt(table.getModel().getValueAt(row, column).toString());

							if (roomVal.isEmpty() || isEmptyCbox(rating) || isEmptyCbox(status)) {
								JOptionPane.showMessageDialog(messageFrame, dataEmpty, currentPanel,
										JOptionPane.WARNING_MESSAGE);
								clearData();
							} else {
								int numStudents1 = java.lang.Integer.parseInt(numStudents);
								int numSeats1 = java.lang.Integer.parseInt(numSeats);
								if (isOutOfRange(numStudents1) || isOutOfRange(numSeats1)
										|| checkInputRange(roomVal.length()) || checkInputRange(status.length())) {
									JOptionPane.showMessageDialog(messageFrame, dataOutOfBounds, currentPanel,
											JOptionPane.WARNING_MESSAGE);
									clearData();
								} else {
									updateQuery = "UPDATE sections SET room_number = ?, no_of_students = ?, no_of_seats = ?, rating = ?, active_status = ? WHERE section_id = ?";
									ps = DBConnection.connectDB().prepareStatement(updateQuery);
									ps.setString(1, roomVal);
									ps.setInt(2, numStudents1);
									ps.setInt(3, numSeats1);
									ps.setString(4, rating);
									ps.setString(5, status);
									ps.setInt(6, id);
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

		JLabel lblSections = new JLabel("Sections");
		lblSections.setHorizontalAlignment(SwingConstants.CENTER);
		lblSections.setForeground(Color.WHITE);
		lblSections.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblSections.setBounds(0, 0, 882, 48);
		contentPane.add(lblSections);

		JLabel lblRoom = new JLabel("Room");
		lblRoom.setForeground(Color.WHITE);
		lblRoom.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRoom.setBounds(10, 60, 147, 30);
		contentPane.add(lblRoom);

		txtRoom = new JTextField();
		txtRoom.setColumns(10);
		txtRoom.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		txtRoom.setBounds(10, 90, 147, 30);
		contentPane.add(txtRoom);

		JLabel lblNumStudents = new JLabel("No of Students");
		lblNumStudents.setForeground(Color.WHITE);
		lblNumStudents.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumStudents.setBounds(192, 60, 89, 30);
		contentPane.add(lblNumStudents);

		cboRating.setModel(new DefaultComboBoxModel(new String[] { "Select", "A+", "A", "B", "C", "D", "E" }));
		cboRating.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboRating.setBounds(440, 90, 103, 30);
		contentPane.add(cboRating);

		JLabel lblRating = new JLabel("Rating");
		lblRating.setForeground(Color.WHITE);
		lblRating.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblRating.setBounds(440, 60, 103, 30);
		contentPane.add(lblRating);

		spinnerNumStd.setModel(new SpinnerNumberModel(15, 15, 200, 1));
		spinnerNumStd.setBounds(192, 90, 89, 30);
		contentPane.add(spinnerNumStd);

		spinnerNumSeats.setModel(new SpinnerNumberModel(15, 15, 200, 1));
		spinnerNumSeats.setBounds(316, 90, 89, 30);
		contentPane.add(spinnerNumSeats);

		JLabel lblNumSeats = new JLabel("No of seats");
		lblNumSeats.setForeground(Color.WHITE);
		lblNumSeats.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNumSeats.setBounds(316, 60, 89, 30);
		contentPane.add(lblNumSeats);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setForeground(Color.WHITE);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStatus.setBounds(578, 60, 103, 30);
		contentPane.add(lblStatus);

		cboStatus.setModel(new DefaultComboBoxModel(new String[] { "Select", "Active", "Not active" }));
		cboStatus.setBorder(new LineBorder(UIManager.getColor("Button.shadow")));
		cboStatus.setBounds(578, 90, 103, 30);
		contentPane.add(cboStatus);

		JLabel lblBackground = new JLabel("");
		lblBackground.setBounds(0, 0, 882, 494);
		lblBackground.setIcon(iconBackground);
		contentPane.add(lblBackground);
	}

}
