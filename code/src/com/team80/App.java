package com.team80;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Hashtable;
import org.jdesktop.swingx.JXDatePicker;
import static java.sql.Types.*;


// before starting the app, you should ssh to your ugrad account
// start sqlplus, and log in with your own account
// then run "start project.sql" to make sure our data is stored into the database
public class App {

    //<editor-fold desc="Fields">
    JPanel panelMain;

    private JTextField aidTextField;
    private JPasswordField aPasswordField;
    private JButton adminSignUpButton;
    private JButton adminSignInButton;
    private JButton hSignUpButton;
    private JButton hSignInButton;
    private JButton tSignInButton;
    private JButton hUpdateButton;
    private JButton hDeleteButton;
    private JButton postsButton;
    private JButton addPostButton;
    private JTextField descriptionTextField;
    private JTable hTable;
    private JTextField ratingTextField;
    private JButton logOutButton;
    private JTextField hostMsg;
    static PersistenceLayer persistenceLayer;
    private JTextField hidTextField;
    private JPanel adminPanel;
    private JPanel hostPanel;
    private JPanel travellerPanel;
    private JPanel hostEditorPanel;
    private JPasswordField hPasswordField;
    private JTextField roomTextField;
    private JTextField residenceTextField;
    private JTextField dailyRateTextField;
    private JTextField rTextField;
    private JPasswordField rPasswordField;
    private JButton rSignUpButton;
    private JTextField fromDateTextField;
    private JPanel toDatePanel;
    private JXDatePicker toDatePicker;
    private JPanel fromDatePanel;
    private JXDatePicker fromDatePicker;
    private JTextField tidTextField;
    private JPasswordField tPasswordField;
    private JPanel registerPanel;
    private JPanel travellerEditorPanel;
    private JPanel filteredResultPanel;
    private JPanel postingFilterField1;
    private JPanel postingFilterField2;
    private JButton tFindPostingsButton;
    private JPanel AdminPanel;
    private JButton contractsButton;
    private JButton addReviewButton;
    private JSlider ratingSlider;
    private JButton reviewsButton;

    private JPanel AdminEditor;
    private JButton A_awardButton;
    private JButton A_Delete;
    private JButton A_Logout;
    private JTextField AdminLoginText;
    private JButton HR_hostButton;
    private JButton LR_HostButton;
    private JTabbedPane tab;
    private JButton SuperT;
    private JTextField A_hidTextfield;

    private int hid;
    DefaultTableModel hModel = new DefaultTableModel();

    //admin id
    private int aid;

    //table model for Admin
    private JTable A_Table;
    private JButton FindHost;
    DefaultTableModel A_model = new DefaultTableModel();



    private JSlider travellerReviewSlider;
    private JTable tTable;
    private JButton tSignContractButton;
    private JTextField universityTextField;
    private JXDatePicker tFromDatePanel;
    private JXDatePicker tToDatePanel;
    private JTextField tLowestPrice;
    private JTextField tHighestPrice;
    private JButton tLogOutButton;
    private JPanel contractReviewField;
    private JButton tFindAllExistingContractButton;
    private JButton tReviewHost;
    private JButton tCancelContractButton;
    private JCheckBox tDateRangeCheckBox;
    private JCheckBox tAddressCheckBox;
    private JCheckBox tUniversityCheckBox;
    private JCheckBox tHostCheckBox;
    private JCheckBox tRateCheckBox;
    private JTextField tHostID;

    private int tid;

    DefaultTableModel tModel = new DefaultTableModel();

    //</editor-fold>

    public App() {

        //<editor-fold desc="Init">
        panelMain.setPreferredSize(new Dimension(1200, 600));
        hTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        hTable.setFillsViewportHeight(true);

        //table for Admin
        if (A_Table == null) {
            System.out.println("atable is null");
        }
        A_Table.setModel(A_model);
        A_Table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        A_Table.setFillsViewportHeight(true);

        // Hide the all Editor tabs before user is logged in
        tab.remove(hostEditorPanel);
        tab.remove(registerPanel);
        tab.remove(travellerEditorPanel);

        ratingSlider.setMaximum(10);
        ratingSlider.setMinimum(1);
        travellerReviewSlider.setMaximum(10);
        travellerReviewSlider.setMinimum(1);
        Hashtable labelTable = new Hashtable();
        for (int i = 1; i <= 10; i++) {
            labelTable.put(i, new JLabel(String.valueOf(i)));
        }
        ratingSlider.setLabelTable(labelTable);
        ratingSlider.setPaintLabels(true);
        travellerReviewSlider.setLabelTable(labelTable);
        travellerReviewSlider.setPaintLabels(true);



        //</editor-fold>

        //<editor-fold desc="Host Editor Events">
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tab.add(hostPanel, 1);
                tab.setTitleAt(1, "Host");
                tab.remove(hostEditorPanel);
                tab.setSelectedIndex(1);
            }
        });
        contractsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hUpdateButton.setEnabled(false);
                addPostButton.setEnabled(false);
                hDeleteButton.setEnabled(false);
                addReviewButton.setEnabled(true);
                ResultSet rs = SQLUtil.findHostsContracts(hid);
                printTable(hTable, hModel, rs);
            }
        });
        postsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addReviewButton.setEnabled(false);
                hUpdateButton.setEnabled(true);
                addPostButton.setEnabled(true);
                hDeleteButton.setEnabled(true);
                refreshAllPosts();
            }
        });
        reviewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hUpdateButton.setEnabled(false);
                addPostButton.setEnabled(false);
                hDeleteButton.setEnabled(false);
                addReviewButton.setEnabled(false);
                ResultSet rs = SQLUtil.findHostsReviews(hid);
                printTable(hTable, hModel, rs);
            }
        });
        hUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = getSelected();
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a record", "Update Message", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (selectedRows.length > 1) {
                    JOptionPane.showMessageDialog(null, "Please select only one record at a time", "Update Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    Date fromDate = (fromDatePicker.getDate() == null) ? null : new Date(fromDatePicker.getDate().getTime());
                    Date toDate = (toDatePicker.getDate() == null) ? null : new Date(toDatePicker.getDate().getTime());
                    String description = descriptionTextField.getText();

                    int dialogResult = JOptionPane.showConfirmDialog(null, "Update selected record?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        int result = SQLUtil.updatePost(selectedRows, hModel, fromDate, toDate, description);
                        refreshAllPosts();

                        if (result > 0) {
                            JOptionPane.showMessageDialog(null, "Update successful.", "Update Message", JOptionPane.INFORMATION_MESSAGE);
                        } else if (result == 0) {
                            JOptionPane.showMessageDialog(null, "Invalid period", "Update Message", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Update failed. Please try again", "Update Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        addPostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fromDatePicker.getDate() == null || toDatePicker.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Missing date", "New Posting Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date fromDate = new Date(fromDatePicker.getDate().getTime());
                Date toDate = new Date(toDatePicker.getDate().getTime());
                if (fromDate.after(toDate)) {
                    JOptionPane.showMessageDialog(null, "Invalid period", "Update Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String description = descriptionTextField.getText();
                int result = SQLUtil.addPost(hid, fromDate, toDate, description);
                if (result == 1) {
                    JOptionPane.showMessageDialog(null, "New posting has been added", "New Posting Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Insert failed. Please try again.", "New Posting Message", JOptionPane.ERROR_MESSAGE);
                }
                refreshAllPosts();
            }
        });
        addReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = getSelected();
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a record", "Add Review Message", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (selectedRows.length > 1) {
                    JOptionPane.showMessageDialog(null, "Please select only one record at a time", "Add Review Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    int rating = ratingSlider.getValue();
                    int tid = (Integer) hModel.getValueAt(selectedRows[0], 3);
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Add a host review?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        int result = SQLUtil.addHostReview(hid, tid, rating);
                        if (result > 0) {
                            JOptionPane.showMessageDialog(null, "Added successfully.", "Add Review Message", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed. Please try again", "Add Review Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        //</editor-fold>

        //<editor-fold desc="Register Events">
        rSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int cID;
                String roomNo = "";
                String residence = "";
                int dailyRate = -1;
                if ((cID = isIdValid(rTextField.getText())) != -1) {
                    ResultSet rs = SQLUtil.getHost(cID);
                    try {
                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "User already exists", "Register Message", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } catch (SQLException e1) {
                    }

                    roomNo = roomTextField.getText();
                    residence = residenceTextField.getText();
                    try {
                        dailyRate = Integer.parseInt(dailyRateTextField.getText());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Invalid daily rate", "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                    if (roomNo.equals("") || residence.equals("") || dailyRate == -1) {
                        JOptionPane.showMessageDialog(null, "Info missing", "Error Message", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int result = SQLUtil.addHost(cID, roomNo, residence, dailyRate);
                    if (result == 1) {
                        JOptionPane.showMessageDialog(null, "Registration successful. Please log in.", "Register Message", JOptionPane.INFORMATION_MESSAGE);
                        tab.remove(registerPanel);
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration failed. Please try again", "Register Message", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username. No such student", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //</editor-fold>

        //<editor-fold desc="Host Events">
        hSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tab.add(registerPanel);
                tab.setTitleAt(4, "Register");
                tab.setSelectedIndex(4);
            }
        });
        hSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hid = Integer.parseInt(hidTextField.getText());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "Invalid username.", "Login error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String pw = String.valueOf(hPasswordField.getPassword());
                if (pw.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter password.", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (SQLUtil.hostExists(hid)) {
                    if (SQLUtil.hasCorrectPassword(hid, pw)) {
                        hidTextField.setText("");
                        hPasswordField.setText("");
                        tab.add(hostEditorPanel, 1);
                        tab.setTitleAt(1, "Host Editor");
                        tab.remove(hostPanel);
                        tab.setSelectedIndex(1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid password. Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    hid = -1;
                    JOptionPane.showMessageDialog(null, "User does not exist. Please sign up first.", "Login error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //</editor-fold>

        //<editor-fold desc="Admin Editor Events">
        /*
        Functions for Admin
         */
        //login to admin panel
        adminSignInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    aid = Integer.parseInt(aidTextField.getText());
                } catch (Exception e1) {
                    aidTextField.setText("Invalid username");
                    return;
                }
                //TODO: pw & aid ???
                char[] pw = aPasswordField.getPassword();


                if (aidTextField.getText().equals(String.valueOf(pw))) {
                    tab.add(AdminEditor, 0);
                    tab.setTitleAt(0, "AdminEditor");
                    tab.remove(adminPanel);
                    tab.setSelectedIndex(0);
                } else {
                    AdminLoginText.setText("Invalid password. Try again.");
                    JOptionPane.showMessageDialog(null, "Invalid password. Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //show award message
        A_awardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "You have awarded this amazing traveller!!!", "Award Message", JOptionPane.INFORMATION_MESSAGE);
            }
        });


        //Log out and bring back the user to the login page
        A_Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tab.add(adminPanel, 0);
                tab.setTitleAt(0, "Admin");
                tab.remove(AdminEditor);
                tab.setSelectedIndex(0);
            }
        });

        //A1
        FindHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               ResultSet rs = SQLUtil.findHostsContracts();
               printTable(A_Table,A_model,rs);
            }
        });

        //A2
        //Find Host with highest rating
        HR_hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ResultSet rs = SQLUtil.findBestHosts();
                printTable(A_Table, A_model, rs);

            }
        });


        //A2
        //Find host with lowest rating
        LR_HostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                ResultSet rs = SQLUtil.findWorstHosts();
                printTable(A_Table, A_model, rs);
            }
        });

        //<editor-fold desc="Traveller Editor Events">



        tSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tid = Integer.parseInt(tidTextField.getText());
                } catch(Exception e1) {
                    JOptionPane.showMessageDialog(null, "Invalid traveler name. Please try again.", "Error Message", JOptionPane.ERROR_MESSAGE);                   return;
                }
                ResultSet rs = SQLUtil.getStudent(tid);
                String pw = String.valueOf(tPasswordField.getPassword());
                if (pw.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter your password.", "Error Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (SQLUtil.travelerExists(tid)) {
                    if (SQLUtil.hasCorrectPassword(tid, pw)) {
                        tidTextField.setText("");
                        tPasswordField.setText("");
                        tab.add(travellerEditorPanel, 1);
                        tab.setTitleAt(1, "Traveller Editor");
                        tab.remove(travellerPanel);
                        tab.setSelectedIndex(1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid password. Try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    tid = -1;
                    JOptionPane.showMessageDialog(null, "User does not exist. Please sign up first.", "Login error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        tLogOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tab.add(travellerPanel, 1);
                tab.setTitleAt(1, "Traveller");
                tab.remove(travellerEditorPanel);
                tab.setSelectedIndex(1);
            }
        });

        tFindPostingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tSignContractButton.setEnabled(true);
                tCancelContractButton.setEnabled(false);
                Date fromDate = (tFromDatePanel.getDate() == null) ? null : new Date(tFromDatePanel.getDate().getTime());
                Date toDate = (tToDatePanel.getDate() == null) ? null : new Date(tToDatePanel.getDate().getTime());
                String tUniversity = (universityTextField.getText() == null)? null : new String(universityTextField.getText());

                int tLowestPriceVal = (tLowestPrice.getText().equals(""))? 0 : Integer.parseInt(tLowestPrice.getText());
                int tHighestPriceVal = (tHighestPrice.getText().equals(""))? 200 : Integer.parseInt(tHighestPrice.getText());

                ResultSet rs = SQLUtil.findPostsWithCondition(tUniversity,fromDate, toDate,
                        tLowestPriceVal,tHighestPriceVal,
                        tDateRangeCheckBox.isSelected(), tHostCheckBox.isSelected(), tAddressCheckBox.isSelected(),
                        tUniversityCheckBox.isSelected(), tRateCheckBox.isSelected());
                printTable(tTable, tModel, rs);
            }
        });

        tReviewHost.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tSignContractButton.setEnabled(false);
                tCancelContractButton.setEnabled(false);
                int hostID = 0;
                int rating = travellerReviewSlider.getValue();
                if (tHostID.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter a host ID to find your review","Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    hostID = Integer.parseInt(tHostID.getText());
                }
                if (SQLUtil.addTravelerReview(tid,hostID,rating) == -1) {
                    JOptionPane.showMessageDialog(null, "Review not successful. Please try again.","Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Review successfully added!","Review Message", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        tFindAllExistingContractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tSignContractButton.setEnabled(false);
                tCancelContractButton.setEnabled(true);
                ResultSet rs = SQLUtil.findTravelerContracts(tid);
                printTable(tTable, tModel, rs);
            }
        });


        tCancelContractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tGetSelected();

                //If nothing get selected
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a contract", "Deletion Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int dialogResult = JOptionPane.showConfirmDialog (null, "Cancel selected contract?","Warning", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    //TODO: Validate if this successfully cancel selected contract
                    for (final int column : selectedRows) {
                        int cid = (Integer) tModel.getValueAt(column, 0);
                        if (SQLUtil.deleteContract(cid) == -1) {
                            JOptionPane.showMessageDialog(null, "Cancellation not successful!", "Error Message", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                    refreshAllPosts();

                }
            }
        });

        tSignContractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tGetSelected();

                //If nothing get selected
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a contract", "Deletion Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int dialogResult = JOptionPane.showConfirmDialog (null, "Sign contract for selected posting?","Warning", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    for (final int column : selectedRows) {
                        int pid = (Integer) tModel.getValueAt(column, 0);
                        //TODO: sign contract can not get host id.
                        if (SQLUtil.transformPostingToContract(pid,tid) == -1) {
                            JOptionPane.showMessageDialog(null, "Signing not successful!", "Error Message", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Contract successfully generated!", "Contract Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }



                }
            }
        });


        //A3: Delete a Host
        A_Delete.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent actionEvent) {

                hid = Integer.parseInt(A_hidTextfield.getText());

                //host does not exist,send error message
                if (!SQLUtil.hostExists(hid)) {
                    A_hidTextfield.setText("Invalid Host ID!");
                } else {
                    //host info
                    ResultSet rs = SQLUtil.getHost(hid);
                    printTable(A_Table, A_model, rs);
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Delete Host record?", "Warning", JOptionPane.YES_NO_OPTION);
                    //delete
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        SQLUtil.deleteHost(hid);
                        JOptionPane.showMessageDialog(null, "Host record have been removed.", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        });

        //A4: Find Travellers who have been to every university
        SuperT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ResultSet rs = SQLUtil.findAmazingTravelers();
                printTable(A_Table, A_model, rs);
            }
        });
        //</editor-fold>

    }


    //<editor-fold desc="Helper">

    public int[] tGetSelected() {
        int[] selection = tTable.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            selection[i] = tTable.convertRowIndexToModel(selection[i]);
        }
        return selection;
    }


    public void printTable(JTable table, DefaultTableModel model, ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String[] columnNames = new String[count];
            int[] types = new int[count];
            for (int i = 0; i < count; i++) {
                columnNames[i] = rsmd.getColumnLabel(i + 1);
                types[i] = rsmd.getColumnType(i + 1);
            }

            model.setRowCount(0);
            model.setColumnCount(count);
            while (rs.next()) {
                Object[] row = new Object[count];
                for (int i = 0; i < count; i++) {
                    switch (types[i]) {
                        case NUMERIC:
                            row[i] = rs.getInt(i + 1);
                            break;
                        case CHAR:
                            row[i] = rs.getString(i + 1);
                            break;
                        case TIMESTAMP:
                            row[i] = rs.getDate(i + 1);
                            break;
                        default:
                    }
                }
                model.addRow(row);
            }

            model.setColumnIdentifiers(columnNames);
            model.fireTableDataChanged();
            table.setModel(model);
            table.setRowHeight(30);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public int isIdValid(String id) {
        int cid = -1;
        try {
            cid = Integer.parseInt(id);
            ResultSet rs = SQLUtil.getStudent(cid);
            cid = rs.next() ? cid : -1;
        } catch (Exception e1) {
        }
        return cid;
    }

    public void refreshAllPosts() {
        ResultSet rs = SQLUtil.findHostsPostings(hid);
        printTable(hTable, hModel, rs);
    }

    public int[] getSelected() {
        int[] selection = hTable.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            selection[i] = hTable.convertRowIndexToModel(selection[i]);
        }
        return selection;
    }

    //</editor-fold>
}

