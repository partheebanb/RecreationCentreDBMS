package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BookingModel;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.DayEventModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow {
    private JPanel mainPanel;
    private JButton addBooking;
    private JComboBox branchComboBox;
    private JButton viewMembersButton;
    private JButton addAccessButton;
    private JButton viewComplexButton;
    private JButton addAnEventButton;

    private DatabaseConnectionHandler dbHandler;

    private ArrayList<DisposableWindow> childrenPanel = new ArrayList<>();

    private JTable bookingTable;
    private JTable accessTable;
    private JTable eventTable;
    private TableModel bookingTableModel = new TableModel();
    private TableModel accessTableModel = new TableModel();
    private TableModel eventTableModel = new TableModel();

    public MainWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Main Window");
        frame.setContentPane(this.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make the screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        // Set up the branch choice box
        setupBranchComboBox();

        // Setup all the table views
        bookingTable.setModel(bookingTableModel);

        bookingTableModel.addColumn("Member Name");
        bookingTableModel.addColumn("Booking Date");
        bookingTableModel.addColumn("Booking Time");
        bookingTableModel.addColumn("Booking Reserves");

        accessTable.setModel(accessTableModel);

        accessTableModel.addColumn("Member Name");
        accessTableModel.addColumn("Area Name");
        accessTableModel.addColumn("Access Date");
        accessTableModel.addColumn("Access Time");

        eventTable.setModel(eventTableModel);

        eventTableModel.addColumn("Event Name");
        eventTableModel.addColumn("Event Date");
        eventTableModel.addColumn("Event Time");
        eventTableModel.addColumn("Event's Hosts");
        eventTableModel.addColumn("Event's Attendee");

        refreshTables();

        // Set what all the buttons do
        // We add the childrens to the children panel so we can kill it easily (less clutter)
        addBooking.addActionListener(e ->
                childrenPanel.add(new BookingForm(dbHandler, getSelectedBranchId())));

        addAccessButton.addActionListener(e ->
                childrenPanel.add(new AccessForm(dbHandler, getSelectedBranchId())));

        viewMembersButton.addActionListener(e ->
                childrenPanel.add(new MemberWindow(dbHandler)));

        viewComplexButton.addActionListener(e ->
                childrenPanel.add(new ComplexQueriesWindow(dbHandler)));

        // We don't want the windows to be too cluttered. So if we press the main window, we also kill all the children windows
        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                closeAllChildren();
                refreshTables();
            }
        });

        refreshTables();
    }

    // This refresh of the accesses and booking
    public void refreshTables() {
        removeAllElements(bookingTableModel);
        for (BookingModel booking : dbHandler.bookingHandler.getBookingInBranchString(getSelectedBranchId())) {
            ArrayList<String> s = new ArrayList<>();

            MemberModel member = dbHandler.memberHandler.selectMemberWithId(booking.getMemberId());

            s.add(member.getFirstName() + " " + member.getLastName());
            s.add(booking.getDate().toString());
            s.add(booking.getTime().toString());
            s.add(dbHandler.bookingHandler.getBookablesByBookingString(booking.getId()));

            bookingTableModel.addRow(s.toArray());
        }

        removeAllElements(accessTableModel);
        for (ArrayList<String> access : dbHandler.accessHandler.getAccessInBranchString(getSelectedBranchId())) {
            accessTableModel.addRow(access.toArray());
        }

        removeAllElements(eventTableModel);
        for (DayEventModel event : dbHandler.eventHandler.getEventModelOnBranch(getSelectedBranchId())) {
            ArrayList<String> s = new ArrayList<>();
            s.add(event.getName());
            s.add(event.getDate().toString());
            s.add(event.getTime().toString());
            s.add(dbHandler.eventHandler.getEmployeeManagingEventString(event.getEventId()));
            s.add(dbHandler.eventHandler.getMemberAttendingEvent(event.getEventId()));

            eventTableModel.addRow(s.toArray());
        }
    }

    // Let the user choose which branch it want to operate on
    // This data will be used to generate the lists and when creating bookings/accesses
    private void setupBranchComboBox() {
        for (BranchModel branchModel : dbHandler.branchHandler.getBranchInfo()) {
            branchComboBox.addItem(branchModel);
        }
        branchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTables();
            }
        });
    }

    public int getSelectedBranchId() {
        BranchModel selectedBranch = (BranchModel) branchComboBox.getSelectedItem();
        return selectedBranch.getId();
    }

    private void removeAllElements(TableModel tableModel) {
        while (tableModel.getRowCount() != 0)
            tableModel.removeRow(0);
    }

    /*
     * THIS IS FOR UI PURPOSES ONLY THERE IS NO EXTRA FUNCTIONALITIES FOR THIS FEATURE AT ALL
     *
     * Disposable Window is a system implemented to keep the windows from being to cluttered
     * It closes all the childrens whenever the parent window is clicked so there won't be too many
     * windows running around
     */
    private void closeAllChildren() {
        for(DisposableWindow disposableWindow : childrenPanel) {
            disposableWindow.close();
        }
    }
}
