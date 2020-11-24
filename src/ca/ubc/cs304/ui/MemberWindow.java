package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BookingModel;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.DayEventModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MemberWindow implements DisposableWindow {
    private JTextField memberEmail;
    private JButton cancelButton;
    private JButton modifyButton;
    private JPanel jpanel;
    private JComboBox memberComboBox;
    private JButton deleteUser;
    private JLabel membershipType;
    private JLabel membershipCost;
    private JLabel memberGender;
    private JLabel memberDob;
    private JLabel memberSignUpDate;
    private JList registeredBranchesList;
    private JTable eventTable;
    private JTable bookingTable;
    private JTable accessTable;

    private DatabaseConnectionHandler dbHandler;

    private final TableModel eventTableModel = new TableModel();
    private final TableModel bookingTableModel = new TableModel();
    private final TableModel accessTableModel = new TableModel();
    private final DefaultListModel<String> registeredBranchesListModel = new DefaultListModel<>();

    MemberWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Membership Window");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        // Set up the listviews
        registeredBranchesList.setModel(registeredBranchesListModel);

        accessTable.setModel(accessTableModel);

        accessTableModel.addColumn("Branch Name");
        accessTableModel.addColumn("Area Name");
        accessTableModel.addColumn("Access Date");
        accessTableModel.addColumn("Access Time");

        bookingTable.setModel(bookingTableModel);

        bookingTableModel.addColumn("Branch Name");
        bookingTableModel.addColumn("Booking Date");
        bookingTableModel.addColumn("Booking Time");
        bookingTableModel.addColumn("Booking Reserves");

        eventTable.setModel(eventTableModel);

        eventTableModel.addColumn("Event Name");
        eventTableModel.addColumn("Branch Name");
        eventTableModel.addColumn("Event Date");
        eventTableModel.addColumn("Event Time");
        eventTableModel.addColumn("Event's Hosts");

        setupMemberComboBox();
        setupEmailButtons();
        setupDeleteUserButton();

        refreshList();
        refreshMemberData();
    }

    private void setupDeleteUserButton() {
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Confirm deleting member " + memberComboBox.getSelectedItem().toString(), "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    // TODO: DELETE USER HERE
                }
            }
        });
    }

    private void setupEmailButtons() {
        cancelButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               memberEmail.setText(((MemberModel) memberComboBox.getSelectedItem()).getEmail());
           }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbHandler.memberHandler.updateMemberEmail(
                        ((MemberModel) memberComboBox.getSelectedItem()).getId(),
                        memberEmail.getText());
            }
        });
    }

    private void refreshMemberData() {
        MemberModel memberModel = (MemberModel) memberComboBox.getSelectedItem();
        memberEmail.setText(memberModel.getEmail());

        membershipType.setText(memberModel.getMembershipType());
        memberSignUpDate.setText(memberModel.getRegistrationDate().toString());
        memberDob.setText(memberModel.getDateOfBirth().toString());
        memberGender.setText(memberModel.getGender());

        membershipCost.setText(dbHandler.memberHandler.getMembershipPriceByType(memberModel.getMembershipType()));

        refreshList();
    }

    private void refreshList() {
        removeAllElements(accessTableModel);
        for (ArrayList<String> access : dbHandler.accessHandler.getAccessDataForMember(getSelectedMemberID())) {
            accessTableModel.addRow(access.toArray());
        }

        removeAllElements(bookingTableModel);
        for (BookingModel booking : dbHandler.bookingHandler.getBookingByMemberId(getSelectedMemberID())) {
            ArrayList<String> s = new ArrayList<>();

            BranchModel branchModel = dbHandler.branchHandler.getBranchInfoByBranchId(booking.getBranchId());

            s.add(branchModel.getName());
            s.add(booking.getDate().toString());
            s.add(booking.getTime().toString());
            s.add(dbHandler.bookingHandler.getBookablesByBookingString(booking.getId()));

            bookingTableModel.addRow(s.toArray());
        }

        removeAllElements(eventTableModel);
        for (DayEventModel event : dbHandler.eventHandler.getEventModelForMember(getSelectedMemberID())) {
            ArrayList<String> s = new ArrayList<>();

            BranchModel branchModel = dbHandler.branchHandler.getBranchInfoByBranchId(event.getBranch_id());
            s.add(event.getName());
            s.add(branchModel.getName());
            s.add(event.getDate().toString());
            s.add(event.getTime().toString());
            s.add(dbHandler.eventHandler.getEmployeeManagingEventString(event.getEventId()));

            eventTableModel.addRow(s.toArray());
        }

        registeredBranchesListModel.removeAllElements();
        for (String branches : dbHandler.branchHandler.getBranchNamesByMemberId(getSelectedMemberID())) {
            registeredBranchesListModel.addElement(branches);
        }
    }

    private void setupMemberComboBox() {
        memberComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshMemberData();
            }
        });

        for (MemberModel memberModel: dbHandler.memberHandler.getMemberInfo()) {
            memberComboBox.addItem(memberModel);
        }
    }

    public void close() {
        SwingUtilities.getWindowAncestor(jpanel).dispose();
    }

    public int getSelectedMemberID() {
        MemberModel selectedMember = (MemberModel) memberComboBox.getSelectedItem();
        return selectedMember.getId();
    }

    private void removeAllElements(TableModel tableModel) {
        while (tableModel.getRowCount() != 0)
            tableModel.removeRow(0);
    }
}
