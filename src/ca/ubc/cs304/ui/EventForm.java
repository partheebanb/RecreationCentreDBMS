package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.helper.DateUtils;
import ca.ubc.cs304.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class EventForm implements DisposableWindow {
    private JList memberList;
    private JComboBox memberComboBox;
    private JButton addMemberButton;
    private JTextField eventName;
    private JSpinner dateSpinner;
    private JComboBox equipOrRoom;
    private JList bookableList;
    private JButton enterForm;
    private JComboBox bookableChoice;


    private JButton addBookable;
    private int branchId;
    private final DatabaseConnectionHandler dbHandler;

    private DefaultListModel<BookableModel> bookableListModel = new DefaultListModel<>();
    private DefaultListModel<MemberModel> memberListModel = new DefaultListModel<>();
    private DefaultListModel<EmployeeModel> employeeListModel = new DefaultListModel<>();

    private JPanel jpanel;
    private JList employeeList;
    private JButton addEmployeeButton;
    private JComboBox employeeComboBox;

    EventForm(DatabaseConnectionHandler dbHandler, int branchId) {
        this.dbHandler = dbHandler;
        this.branchId = branchId;

        JFrame frame = new JFrame("Booking Form");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        // Setup UI stuff
        setupDateSpinner();
        setupEnterButton();
        setupMemberComboBox();
        setupEmployeeComboBox();
        setupAddingBookable();

        memberList.setModel(memberListModel);
        bookableList.setModel(bookableListModel);
        employeeList.setModel(employeeListModel);
    }

    // When enterForm is pressed, we submit the new booking in the SQL statement
    // We also add any bookables that the user book into this booking
    private void setupEnterButton() {
        enterForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int eventId = dbHandler.eventHandler.getNextId();
                DayEventModel eventModel = new DayEventModel(
                        eventId,
                        eventName.getText(),
                        DateUtils.normalize((java.util.Date) dateSpinner.getValue()),
                        branchId);

                dbHandler.eventHandler.insertEvent(eventModel);

                for (int i = 0; i < bookableListModel.getSize(); i++) {
                    BookableModel bookableModel = bookableListModel.getElementAt(i);

                    UseRelation useRelation = new UseRelation(eventId, bookableModel.getBookableId());
                    dbHandler.eventHandler.insertUse(useRelation);
                }

                for (int i = 0; i < memberListModel.getSize(); i++) {
                    MemberModel memberModel = memberListModel.getElementAt(i);

                    AttendRelation attendRelation = new AttendRelation(memberModel.getId(), eventId);
                    dbHandler.eventHandler.insertAttend(attendRelation);
                }

                for (int i = 0; i < memberListModel.getSize(); i++) {
                    EmployeeModel employeeModel = employeeListModel.getElementAt(i);

                    HostRelation hostRelation = new HostRelation(employeeModel.getEmployeeId(), eventId);
                    dbHandler.eventHandler.insertHost(hostRelation);
                }

                close();
            }
        });
    }

    // Let user input the date in a spinner
    private void setupDateSpinner() {
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "h:mm a, EEE, dd-MMM-yyyy");
        dateSpinner.setEditor(timeEditor);
        dateSpinner.setValue(new Date(new java.util.Date().getTime()));
    }

    // Let user choose the member from a choice dialog
    private void setupMemberComboBox() {
        for (MemberModel memberModel: dbHandler.memberHandler.getMemberInfoInBranch(branchId)) {
            memberComboBox.addItem(memberModel);
        }
        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (memberComboBox.getSelectedItem() != null) {
                    memberListModel.addElement((MemberModel) memberComboBox.getSelectedItem());
                    memberComboBox.removeItemAt(memberComboBox.getSelectedIndex());
                }
            }
        });
    }

    private void setupEmployeeComboBox() {
        for (EmployeeModel employeeModel : dbHandler.employeeHandler.getEmployeeByBranch(branchId)) {
            employeeComboBox.addItem(employeeModel);
        }
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (employeeComboBox.getSelectedItem() != null) {
                    employeeListModel.addElement((EmployeeModel) employeeComboBox.getSelectedItem());
                    employeeComboBox.removeItemAt(memberComboBox.getSelectedIndex());
                }
            }
        });
    }

    // Let user add a bookable to this booking
    private void setupAddingBookable() {
        equipOrRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBookableList();
            }
        });
        refreshBookableList();

        addBookable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bookableChoice.getSelectedItem() != null) {
                    bookableListModel.addElement((BookableModel) bookableChoice.getSelectedItem());
                    refreshBookableList();
                }
            }
        });
    }

    // This configure the choice box for the bookable. It will change when the user re-select the equipment or room choice
    private void refreshBookableList() {
        bookableChoice.removeAllItems();
        if (equipOrRoom.getSelectedIndex() == 0) {
            for (BookableModel bookableModel : dbHandler.bookableHandler.getEquipmentInfoInBranch(branchId)) {
                if (!bookableListModel.contains(bookableModel))
                    bookableChoice.addItem(bookableModel);
            }
        } else {
            for (BookableModel bookableModel : dbHandler.bookableHandler.getRoomInfoInBranch(branchId)) {
                if (!bookableListModel.contains(bookableModel))
                    bookableChoice.addItem(bookableModel);
            }
        }
    }

    private void createUIComponents() {
        dateSpinner = new JSpinner(new SpinnerDateModel());
    }

    /*
     * THIS IS FOR UI PURPOSES ONLY THERE IS NO EXTRA FUNCTIONALITIES FOR THIS FEATURE AT ALL
     *
     * Disposable Window is a system implemented to keep the windows from being to cluttered
     * It closes all the childrens whenever the parent window is clicked so there won't be too many
     * windows running around
     */
    public void close() {
        SwingUtilities.getWindowAncestor(jpanel).dispose();
    }
}
