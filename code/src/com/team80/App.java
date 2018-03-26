package com.team80;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import org.jdesktop.swingx.JXDatePicker;


// before starting the app, you should ssh to your ugrad account
// start sqlplus, and log in with your own account
// then run "start project.sql" to make sure our data is stored into the database
public class App {

    //<editor-fold desc="Fields">
    JPanel panelMain;
    JTabbedPane tab;
    private JTextField aidTextField;
    private JPasswordField aPasswordField;
    private JButton adminSignUpButton;
    private JButton adminSignInButton;
    private JButton hSignUpButton;
    private JButton hSignInButton;
    private JButton tSignUpButton;
    private JButton tSignInButton;
    private JButton hUpdateButton;
    private JButton hDeleteButton;
    private JButton hSearchButton;
    private JButton hInsertButton;
    private JTextField PID;
    private JTextField descriptionTextField;
    private JTable hTable;
    private JTextField pidTextField;
    private JButton logOutButton;
    private JTextField hostMsg;
    static PersistenceLayer persistenceLayer;
    private JTextField hidTextField;
    private JTextField hLoginTextField;
    private JPanel adminPanel;
    private JPanel hostPanel;
    private JPanel travellerPanel;
    private JPanel hostEditorPanel;
    private JPasswordField hPasswordField;
    private JTextField roomTextField;
    private JTextField residenceTextField;
    private JRadioButton checkedRadioButton;
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
    private JPanel contractReviewField;
    private JComboBox comboBox1;
    private JButton buttonForGO;
    private JSpinner spinner1;
    private JButton submitButton;
    private int hid;

    DefaultTableModel model = new DefaultTableModel();
    //</editor-fold>

    public App() {

        //<editor-fold desc="Init">
        panelMain.setPreferredSize(new Dimension(700, 300));

        String[] hTableColumns = {"PID", "From_Date", "To_Date", "Description"};
        model.setColumnIdentifiers(hTableColumns);
        hTable.setModel(model);
        hTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        hTable.setFillsViewportHeight(true);

        final JPanel adminLogin = (JPanel) tab.getComponentAt(0);
        final JPanel hostLogin = (JPanel) tab.getComponentAt(1);
        final JPanel travellerLogin = (JPanel) tab.getComponentAt(2);
        final JPanel hostEditor = (JPanel) tab.getComponentAt(3);
        final JPanel register = (JPanel) tab.getComponentAt(4);
        final JPanel travellerEditorPanel = (JPanel) tab.getComponentAt(5);

        // Hide the HostEditor tab on Login window
        tab.remove(hostEditor);
        tab.remove(register);
        //</editor-fold>
        //tab.remove(travellerEditorPanel);


        //<editor-fold desc="Host Editor Events">
        logOutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tab.add(hostLogin, 1);
                tab.setTitleAt(1, "Host");
                tab.remove(hostEditor);
                tab.setSelectedIndex(1);
            }
        });






        hSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                search();
            }
        });
        hUpdateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
                    int dialogResult = JOptionPane.showConfirmDialog (null, "Update selected record?","Warning", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION) {
                        int result = SQLUtil.updatePost(selectedRows, model, fromDate, toDate, description);
                        search();
                        if (result > 0) {
                            JOptionPane.showMessageDialog(null, "Update successful.", "Update Message", JOptionPane.INFORMATION_MESSAGE);
                            tab.remove(register);
                        } else if (result == 0) {
                            JOptionPane.showMessageDialog(null, "Invalid period", "Update Message", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Update failed. Please try again", "Update Message", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        hInsertButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (fromDatePicker.getDate() == null || toDatePicker.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Missing date", "New Posting Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date fromDate = new Date(fromDatePicker.getDate().getTime());
                Date toDate = new Date(toDatePicker.getDate().getTime());
                if(fromDate.after(toDate)){
                    JOptionPane.showMessageDialog(null, "Invalid period", "Update Message", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String description = descriptionTextField.getText();
                int result = SQLUtil.addPost(hid, fromDate, toDate, description);
                if(result == 1) {
                    JOptionPane.showMessageDialog(null, "New posting has been added", "New Posting Message", JOptionPane.INFORMATION_MESSAGE);
                    tab.remove(register);
                } else {
                    JOptionPane.showMessageDialog(null, "Insert failed. Please try again.", "New Posting Message", JOptionPane.ERROR_MESSAGE);
                }
                search();
            }
        });
        hDeleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int[] selectedRows = getSelected();
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please select a record", "Deletion Message", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int dialogResult = JOptionPane.showConfirmDialog (null, "Delete selected records?","Warning", JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    int[] result = SQLUtil.deletePosts(selectedRows, model);
                    hSearchButton.doClick();
                    search();
                    if(result.length > 0) {
                        JOptionPane.showMessageDialog(null, "Selected records have been removed.", "Deletion Message", JOptionPane.INFORMATION_MESSAGE);
                        tab.remove(register);
                    } else {
                        JOptionPane.showMessageDialog(null, "Deletion failed. Please try again.", "Deletion Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Register Events">
        rSignUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int cID;
                boolean isChecked;
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
                    } catch (SQLException e1) { }

                    roomNo = roomTextField.getText();
                    residence = residenceTextField.getText();
                    isChecked = checkedRadioButton.isSelected();
                    try {
                        dailyRate = Integer.parseInt(dailyRateTextField.getText());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "Invalid daily rate","Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                    if (roomNo.equals("") || residence.equals("") || dailyRate == -1) {
                        JOptionPane.showMessageDialog(null, "Info missing","Error Message", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    int result = SQLUtil.addHost(cID, isChecked, roomNo, residence, dailyRate);
                    if(result == 1) {
                        JOptionPane.showMessageDialog(null, "Registration successful. Please log in.", "Register Message", JOptionPane.INFORMATION_MESSAGE);
                        tab.remove(register);
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration failed. Please try again", "Register Message", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username. No such student","Error Message", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Host Events">
        hSignUpButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tab.add(register);
                tab.setTitleAt(3, "Register");
                tab.setSelectedIndex(3);
            }
        });
        hSignInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    hid = Integer.parseInt(hidTextField.getText());
                } catch(Exception e1) {
                    hLoginTextField.setText("Invalid username");
                    return;
                }
                ResultSet rs = SQLUtil.getHost(hid);

                try {
                    if (rs.next()) {
                        char[] pw = hPasswordField.getPassword();
                        if (hidTextField.getText().equals(String.valueOf(pw))) {
                            tab.add(hostEditor, 1);
                            tab.setTitleAt(1, "HostEditor");
                            tab.remove(hostLogin);
                            tab.setSelectedIndex(1);
                        } else {
                            hLoginTextField.setText("Invalid password. Try again.");
                            JOptionPane.showMessageDialog(null, "Invalid password. Try again.","Error Message", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        hid = -1;
                        hLoginTextField.setText("User does not exist. Please sign up first.");
                    }
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        });
        //</editor-fold>
    }

    //<editor-fold desc="Helper">
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


    public void search() {
        int pid;
        ResultSet rs = null;
        String pidStr = pidTextField.getText();
        if (!pidStr.equals("")) {
            try {
                pid = Integer.parseInt(pidStr);
                // TODO: add pid as search criterion

            } catch (Exception e1) {
                hLoginTextField.setText("Invalid username");
                return;
            }
        } else {
            rs = SQLUtil.findAllPosts(hid);
        }

        try {
            int pID;
            Date fromDate;
            Date toDate;
            String description = "";
            model.setRowCount(0);
            while (rs.next()) {
                pID = rs.getInt(1);
                fromDate = rs.getDate("fromdate");
                toDate = rs.getDate("todate");
                description = rs.getString("description");
                Object[] row = {pID, fromDate, toDate, description};
                model.addRow(row);
            }
        } catch (SQLException e1) { }
    }

    public int[] getSelected() {
        int[] selection = hTable.getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            selection[i] = hTable.convertRowIndexToModel(selection[i]);
        }
        return selection;
    }
    //</editor-fold>

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
