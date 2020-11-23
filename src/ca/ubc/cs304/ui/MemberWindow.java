package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
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

    private DatabaseConnectionHandler dbHandler;

    private DefaultListModel<String> accessListModel;
    private DefaultListModel<String> bookingListModel;
    private DefaultListModel<String> programListModel;

    MemberWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Booking Form");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        setupMemberComboBox();
        setupEmailButtons();

        refreshList();
        refreshMemberData();
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
                        memberEmail.getSelectedText());
            }
        });
    }

    private void refreshMemberData() {
        MemberModel memberModel = (MemberModel) memberComboBox.getSelectedItem();
        memberEmail.setText(memberModel.getEmail());
    }

    private void refreshList() {
        accessListModel.removeAllElements();
        for (String access : dbHandler.accessHandler.getAccessByMemberId(getSelectedMemberID())) {
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

    }

    private void setupMemberComboBox() {
        memberComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshList();
                refreshMemberData();
            }
        });

        for (MemberModel memberModel: dbHandler.memberHandler.getMemberInfo()) {
            memberComboBox.addItem(memberModel);
        }
    }

    private void createUIComponents() {
        accessListModel = new DefaultListModel<>();
        bookingListModel = new DefaultListModel<>();
        programListModel = new DefaultListModel<>();

        accessList = new JList(accessListModel);
        bookingList = new JList(bookingListModel);
        programList = new JList(programListModel);
    }

    public void close() {
        SwingUtilities.getWindowAncestor(jpanel).dispose();
    }

    public int getSelectedMemberID() {
        MemberModel selectedMember = (MemberModel) memberComboBox.getSelectedItem();
        return selectedMember.getId();
    }
}
