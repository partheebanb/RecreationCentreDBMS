package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MemberWindow implements DisposableWindow {
    private JList programList;
    private JList bookingList;
    private JList accessList;
    private JLabel Accesses;
    private JTextField memberEmail;
    private JButton cancelButton;
    private JButton modifyButton;
    private JPanel jpanel;
    private JComboBox memberComboBox;
    private JButton deleteUser;
    private JLabel membershipType;
    private JLabel membershipCost;
    private JLabel gender;
    private JLabel memberDob;
    private JLabel memberSignUpDate;
    private JList registeredBranchesList;

    private DatabaseConnectionHandler dbHandler;

    private DefaultListModel<String> accessListModel;
    private DefaultListModel<String> bookingListModel;
    private DefaultListModel<String> programListModel;
    private DefaultListModel<String> registeredBranchesListModel;

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
        accessListModel = new DefaultListModel<>();
        bookingListModel = new DefaultListModel<>();
        programListModel = new DefaultListModel<>();
        registeredBranchesListModel = new DefaultListModel<>();

        accessList.setModel(accessListModel);
        bookingList.setModel(bookingListModel);
        programList.setModel(programListModel);
        registeredBranchesList.setModel(registeredBranchesListModel);

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

        membershipCost.setText(dbHandler.memberHandler.getMembershipPriceByType(memberModel.getMembershipType()));

        refreshList();
    }

    private void refreshList() {
        accessListModel.removeAllElements();
        for (String access : dbHandler.accessHandler.getAccessInBranchString(getSelectedMemberID())) {
            accessListModel.addElement(access);
        }

        bookingListModel.removeAllElements();
        for (String booking : dbHandler.bookingHandler.getBookingByMemberId(getSelectedMemberID())) {
            bookingListModel.addElement(booking);
        }

        programListModel.removeAllElements();
        for (String program : dbHandler.programHandler.getProgramByMemberID(getSelectedMemberID())) {
            programListModel.addElement(program);
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
}
