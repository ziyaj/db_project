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
    private JPanel adminPanel;
    private JPanel hostPanel;
    private JPanel travellerPanel;
    private JPanel hostEditorPanel;
    private JPanel toDatePanel;
    private JPanel fromDatePanel;
    private JPanel registerPanel;
    private JPanel travellerEditorPanel;
    private JPanel filteredResultPanel;
    private JPanel postingFilterField1;
    private JPanel postingFilterField2;
    private JPanel AdminEditor;
    private JPanel contractReviewField;

    private JTabbedPane tab;

    private JButton hSignUpButton;
    private JButton hSignInButton;
    private JButton tSignInButton;
    private JButton hUpdateButton;
    private JButton hDeleteButton;
    private JButton postsButton;
    private JButton addPostButton;
    private JButton adminSignInButton;
    private JButton logOutButton;
    private JButton rSignUpButton;
    private JButton tFindPostingsButton;
    private JButton contractsButton;
    private JButton addReviewButton;
    private JButton reviewsButton;
    private JButton A_awardButton;
    private JButton A_Delete;
    private JButton A_Logout;
    private JButton HostSearch;
    private JButton TravelerSearch;
    private JButton tSignContractButton;
    private JButton tLogOutButton;
    private JButton tFindAllExistingContractButton;
    private JButton tReviewHost;
    private JButton tCancelContractButton;
    private JButton tFindCheapestPostingsButton;
    private JButton tFindMostExpensivePostings;

    private JTextField descriptionTextField;
    private JTextField aidTextField;
    private JTextField ratingTextField;
    private JTextField hostMsg;
    private JTextField hidTextField;
    private JTextField roomTextField;
    private JTextField residenceTextField;
    private JTextField dailyRateTextField;
    private JTextField rTextField;
    private JTextField tidTextField;
    private JTextField A_DeleteText;
    private JTextField A_hidTextfield;
    private JTextField universityTextField;
    private JTextField tLowestPrice;
    private JTextField tHighestPrice;
    private JTextField tHostID;

    private JPasswordField aPasswordField;
    private JPasswordField hPasswordField;
    private JPasswordField rPasswordField;
    private JPasswordField tPasswordField;

    private JXDatePicker toDatePicker;
    private JXDatePicker fromDatePicker;
    private JXDatePicker tFromDatePanel;
    private JXDatePicker tToDatePanel;

    private JSlider ratingSlider;
    private JSlider travellerReviewSlider;

    private JTable hTable;
    private JTable A_Table;
    private JTable tTable;

    DefaultTableModel hModel = createModel();
    DefaultTableModel A_model = createModel();
    DefaultTableModel tModel = createModel();

    private ButtonGroup group;
    private ButtonGroup group2;

    private JRadioButton bestHostRadioButton;
    private JRadioButton worstHostRadioButton;
    private JRadioButton hostWithContractsRadioButton;
    private JRadioButton allHostsRadioButton;
    private JRadioButton searchByIDRadioButton;
    private JRadioButton AllTravelerRadioButton;
    private JRadioButton AmazingTravelerRadioButton;

    private JCheckBox tDateRangeCheckBox;
    private JCheckBox tAddressCheckBox;
    private JCheckBox tUniversityCheckBox;
    private JCheckBox tHostCheckBox;
    private JCheckBox tRateCheckBox;

    private int hid;
    private int tid;

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
        tab.remove(AdminEditor);
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

        //<editor-fold desc="Admin Events">

        adminSignInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final String aidText = aidTextField.getText();
                if (aidText == null || aidText.isEmpty()) {
                    pleaseEnterUsername();
                } else {
                    final char[] pw = aPasswordField.getPassword();
                    if (pw == null || pw.length == 0) {
                        pleaseEnterPassword();
                    } else {
                        if (aidText.equals("admin") && String.valueOf(pw).equals("123456")) {
                            tab.add(AdminEditor, 0);
                            tab.setTitleAt(0, "Admin Editor");
                            tab.remove(adminPanel);
                            tab.setSelectedIndex(0);
                        } else {
                            invalidUsernamePassword();
                        }
                    }
                }
            }
        });

        //</editor-fold>

        //<editor-fold desc="Host Events">

        hSignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tab.add(registerPanel);
                tab.setTitleAt(3, "Register");
                tab.setSelectedIndex(3);
            }
        });

        hSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String hidText = hidTextField.getText();
                if (hidText == null || hidText.isEmpty()) {
                    pleaseEnterUsername();
                    return;
                }

                try {
                    hid = Integer.parseInt(hidTextField.getText());
                } catch (Exception e1) {
                    invalideUsername();
                    return;
                }

                final char[] pwText = hPasswordField.getPassword();
                if (pwText == null || pwText.length == 0) {
                    pleaseEnterPassword();
                    return;
                }

                final String pw = String.valueOf(pwText);

                if (SQLUtil.hostExists(hid)) {
                    if (SQLUtil.hasCorrectPassword(hid, pw)) {
                        hidTextField.setText("");
                        hPasswordField.setText("");
                        tab.add(hostEditorPanel, 1);
                        tab.setTitleAt(1, "Host Editor");
                        tab.remove(hostPanel);
                        tab.setSelectedIndex(1);
                    } else {
                        invalidUsernamePassword();
                    }
                } else {
                    hid = -1;
                    JOptionPane.showMessageDialog(null, "User does not exist. Please sign up first.", "Login error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //</editor-fold>

        //<editor-fold desc="Traveller Events">

        tSignInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String tidText = tidTextField.getText();
                if (tidText == null || tidText.isEmpty()) {
                    pleaseEnterUsername();
                    return;
                }
                try {
                    tid = Integer.parseInt(tidText);
                } catch (Exception e1) {
                    invalideUsername();
                    return;
                }

                final char[] pwText = tPasswordField.getPassword();

                if (pwText == null || pwText.length == 0) {
                    pleaseEnterPassword();
                    return;
                }

                final String pw = String.valueOf(pwText);
                if (SQLUtil.travelerExists(tid)) {
                    if (SQLUtil.hasCorrectPassword(tid, pw)) {
                        tidTextField.setText("");
                        tPasswordField.setText("");
                        tab.add(travellerEditorPanel, 1);
                        tab.setTitleAt(1, "Traveller Editor");
                        tab.remove(travellerPanel);
                        tab.setSelectedIndex(1);
                    } else {
                        invalidUsernamePassword();
                    }
                } else {
                    tid = -1;
                    JOptionPane.showMessageDialog(null, "User does not exist.", "Login error", JOptionPane.ERROR_MESSAGE);
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
                int[] selectedRows = getSelected(hTable);
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a record", "Update Message", JOptionPane.WARNING_MESSAGE);
                    return;
                } else if (selectedRows.length > 1) {
                    JOptionPane.showMessageDialog(null, "Please select only one record at a time", "Update Message", JOptionPane.WARNING_MESSAGE);
                } else {
                    Date fromDate = (fromDatePicker.getDate() == null) ? null : new Date(fromDatePicker.getDate().getTime());
                    Date toDate = (toDatePicker.getDate() == null) ? null : new Date(toDatePicker.getDate().getTime());
                    // model.getValueAt(selection[0], 2)
                    final String existingDescription = (String) hModel.getValueAt(selectedRows[0], 6);
                    final String textFieldDescription = descriptionTextField.getText();
                    final String description = (textFieldDescription == null || textFieldDescription.isEmpty()) ? existingDescription : textFieldDescription;

                    int dialogResult = JOptionPane.showConfirmDialog(null, "Update selected record?", "Warning", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {

                        if (SQLUtil.isUpdatedPostOverlapped(hid, selectedRows, hModel, fromDate, toDate)) {
                            JOptionPane.showMessageDialog(null, "Post overlaps with others", "Update Message", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        final int result = SQLUtil.updatePost(selectedRows, hModel, fromDate, toDate, description);
                        refreshAllPosts();

                        if (result > 0) {
                            JOptionPane.showMessageDialog(null, "Update successful.", "Update Message", JOptionPane.INFORMATION_MESSAGE);
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
                String description = descriptionTextField.getText();
                if (SQLUtil.isNewPostOverlapped(hid, fromDate, toDate)) {
                    JOptionPane.showMessageDialog(null, "Post overlaps with others", "Update Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int result = SQLUtil.addPost(hid, fromDate, toDate, description);
                if (result == 1) {
                    JOptionPane.showMessageDialog(null, "New posting has been added", "New Posting Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Insert failed. Post overlaps with others. Please try again.", "New Posting Message", JOptionPane.ERROR_MESSAGE);
                }
                refreshAllPosts();
            }
        });

        addReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = getSelected(hTable);
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a record", "Add Review Message", JOptionPane.WARNING_MESSAGE);
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

        hDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = getSelected(hTable);
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a record", "Deletion Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int dialogResult = JOptionPane.showConfirmDialog (null, "Delete selected records?","Warning", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    int[] result = SQLUtil.deletePost(selectedRows, hModel);
                    refreshAllPosts();
                    if(result.length > 0) {
                        JOptionPane.showMessageDialog(null, "Selected records have been removed.", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Deletion failed. Please try again.", "Deletion Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        //</editor-fold>

        //<editor-fold desc="Admin Editor Events">

        //Host Administration
        group = new ButtonGroup();
        group.add(allHostsRadioButton);
        group.add(searchByIDRadioButton);
        group.add(bestHostRadioButton);
        group.add(worstHostRadioButton);
        group.add(hostWithContractsRadioButton);

        //Traveller Administration
        group2 = new ButtonGroup();
        group2.add(AmazingTravelerRadioButton);
        group2.add(AllTravelerRadioButton);

        //Search Host
        HostSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (allHostsRadioButton.isSelected()) {
                    printTable(A_Table, A_model, SQLUtil.findAllHosts());
                } else if (searchByIDRadioButton.isSelected()) {
                    final String hidText = A_hidTextfield.getText();
                    if (hidText == null || hidText.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please input a hostid.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        hid = Integer.parseInt(hidText);
                        // host does not exist, send error message
                        if (!SQLUtil.hostExists(hid)) {
                            hostNotExist();
                        } else {
                            // host info
                            printTable(A_Table, A_model, SQLUtil.getHost(hid));
                        }
                    }
                } else if (bestHostRadioButton.isSelected()) {
                    printTable(A_Table, A_model, SQLUtil.findBestHosts());
                } else if (worstHostRadioButton.isSelected()) {
                    printTable(A_Table, A_model, SQLUtil.findWorstHosts());
                } else if (hostWithContractsRadioButton.isSelected()) {
                    printTable(A_Table, A_model, SQLUtil.findHostsContracts());
                } else {
                    JOptionPane.showMessageDialog(null, "Please make a selection!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        //A3: Delete a Host
        A_Delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (A_DeleteText.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please input hostid.", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
                } else {

                    hid = Integer.parseInt(A_DeleteText.getText());

                    //host does not exist,send error message
                    if (!SQLUtil.hostExists(hid)) {
                        hostNotExist();
                    } else {
                        //host info
                        ResultSet rs = SQLUtil.getHost(hid);
                        printTable(A_Table, A_model, rs);
                        int dialogResult = JOptionPane.showConfirmDialog(null, "Delete Host record?", "Warning", JOptionPane.YES_NO_OPTION);
                        //delete
                        if (dialogResult == JOptionPane.YES_OPTION) {
                            try {
                                SQLUtil.deleteHost(hid);
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage() + ". The host has an existing contract", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            JOptionPane.showMessageDialog(null, "Host record have been removed.", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
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

        //A2
        //Find host with lowest rating

        //Log out and bring back the user to the login page
        A_Logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                aidTextField.setText("");
                aPasswordField.setText("");
                tab.add(adminPanel, 0);
                tab.setTitleAt(0, "Admin");
                tab.remove(AdminEditor);
                tab.setSelectedIndex(0);
            }
        });

        //A3: Delete a Host
        TravelerSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (AllTravelerRadioButton.isSelected()) {
                    ResultSet rs = SQLUtil.findAllTravelers();
                    printTable(A_Table, A_model, rs);
                } else if (AmazingTravelerRadioButton.isSelected()) {
                    ResultSet rs = SQLUtil.findAmazingTravelers();
                    printTable(A_Table, A_model, rs);
                } else {
                    JOptionPane.showMessageDialog(null, "Please make a selection!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        //</editor-fold>

        //<editor-fold desc="Traveller Editor Events">

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
                String tUniversity = (universityTextField.getText() == null) ? null : new String(universityTextField.getText());

                int tLowestPriceVal = (tLowestPrice.getText().equals("")) ? 0 : Integer.parseInt(tLowestPrice.getText());
                int tHighestPriceVal = (tHighestPrice.getText().equals("")) ? 200 : Integer.parseInt(tHighestPrice.getText());

                ResultSet rs = SQLUtil.findPostsWithCondition(tUniversity, fromDate, toDate,
                        tLowestPriceVal, tHighestPriceVal,
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
                    JOptionPane.showMessageDialog(null, "Please enter a host ID to find your review", "Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    hostID = Integer.parseInt(tHostID.getText());
                }
                if (SQLUtil.addTravelerReview(tid, hostID, rating) == -1) {
                    JOptionPane.showMessageDialog(null, "Review not successful. Please try again.", "Error Message", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Review successfully added!", "Review Message", JOptionPane.INFORMATION_MESSAGE);
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
                int[] selectedRows = getSelected(tTable);

                //If nothing get selected
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a contract", "Deletion Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int dialogResult = JOptionPane.showConfirmDialog(null, "Cancel selected contract?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    for (final int column : selectedRows) {
                        int cid = (Integer) tModel.getValueAt(column, 0);
                        if (SQLUtil.deleteContract(cid) == -1) {
                            JOptionPane.showMessageDialog(null, "Cancellation not successful!", "Error Message", JOptionPane.WARNING_MESSAGE);
                        }
                        if (SQLUtil.deleteContract(cid) == 0) {
                            JOptionPane.showMessageDialog(null, "You can not cancel a current or past contract!", "Error Message", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                    tFindAllExistingContractButton.doClick();

                }
            }
        });

        tSignContractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = getSelected(tTable);

                //If nothing get selected
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a contract", "Deletion Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int dialogResult = JOptionPane.showConfirmDialog(null, "Sign contract for selected posting?", "Warning", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    for (final int column : selectedRows) {
                        int pid = (Integer) tModel.getValueAt(column, 0);
                        if (SQLUtil.transformPostingToContract(pid, tid) == -1) {
                            JOptionPane.showMessageDialog(null, "Signing not successful!", "Error Message", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Contract successfully generated!", "Contract Message", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }


                }
            }
        });

        tFindCheapestPostingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tSignContractButton.setEnabled(true);
                tCancelContractButton.setEnabled(false);
                ResultSet rs = SQLUtil.findCheapestPosts();
                printTable(tTable, tModel, rs);
            }
        });

        tFindMostExpensivePostings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tSignContractButton.setEnabled(true);
                tCancelContractButton.setEnabled(false);
                ResultSet rs = SQLUtil.findMostExpensivePosts();
                printTable(tTable, tModel, rs);
            }
        });

        //</editor-fold>

        PersistenceLayer.getInstance();
    }

    //<editor-fold desc="Helper">

    /**
     * Create a new table model in which data is not editable
     *
     * @return selected rows in a given table
     */
    private DefaultTableModel createModel() {
        return new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Return a selection in terms of the underlying TableModel
     *
     * @param table the table to fetch selected rows from
     * @return selected rows in a given table
     */
    private int[] getSelected(JTable table) {
        int[] selection = table.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            selection[i] = table.convertRowIndexToModel(selection[i]);
        }
        return selection;
    }

    /**
     * Load data to a given table and print it
     *
     * @param table the table to load the data
     * @param model the model that houses the data
     * @param rs the resultset that feeds desired data
     */
    private void printTable (JTable table, DefaultTableModel model, ResultSet rs){
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

    /**
     * Check if a given student exists in db and return their id as int
     *
     * @param id a string of the student id to search
     * @return id the matching student id; -1 if no match
     */
    private int isIdValid (String id){
        int cid = -1;
        try {
            cid = Integer.parseInt(id);
            ResultSet rs = SQLUtil.getStudent(cid);
            cid = rs.next() ? cid : -1;
        } catch (Exception e1) {
        }
        return cid;
    }

    /**
     * Used to fresh all posts after add, update, and delete action.
     */
    private void refreshAllPosts () {
        ResultSet rs = SQLUtil.findHostsPostings(hid);
        printTable(hTable, hModel, rs);
    }

    private void pleaseEnterUsername() {
        JOptionPane.showMessageDialog(null, "Please enter a username.", "Login error", JOptionPane.WARNING_MESSAGE);
    }

    private void pleaseEnterPassword() {
        JOptionPane.showMessageDialog(null, "Please enter password.", "Login error", JOptionPane.WARNING_MESSAGE);
    }

    private void invalidUsernamePassword() {
        JOptionPane.showMessageDialog(null, "Invalid username/password combination. Please try again.", "Login error", JOptionPane.ERROR_MESSAGE);
    }

    private void invalideUsername() {
        JOptionPane.showMessageDialog(null, "Invalid username. Username is your id.", "Login error", JOptionPane.ERROR_MESSAGE);
    }

    private void hostNotExist() {
        JOptionPane.showMessageDialog(null, "Host does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    //</editor-fold>

}


