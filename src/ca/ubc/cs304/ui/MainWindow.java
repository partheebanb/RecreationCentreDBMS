package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BranchModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow {
    private JPanel mainPanel;
    private JButton addBooking;
    private JComboBox branchComboBox;
    private JList bookingList;
    private JList accessList;
    private JButton viewMembersButton;
    private JButton addAccessButton;
    private JButton viewComplexButton;
    private DatabaseConnectionHandler dbHandler;

    private ArrayList<DisposableWindow> childrenPanel = new ArrayList<>();

    private DefaultListModel<String> bookingListModel;
    private DefaultListModel<String> accessListModel;

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

        // Setup all the list views
        accessList.setModel(accessListModel);
        bookingList.setModel(bookingListModel);

        refreshLists();

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
                refreshLists();
            }
        });

        refreshLists();
    }

    // This refresh of the accesses and booking
    public void refreshLists() {
        accessListModel.removeAllElements();
        bookingListModel.removeAllElements();

        for (String access : dbHandler.accessHandler.getAccessInBranchString(getSelectedBranchId())) {
            accessListModel.addElement(access);
        }

        for (String booking : dbHandler.bookingHandler.getBookingInBranchString(getSelectedBranchId())) {
            bookingListModel.addElement(booking);
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
                refreshLists();
            }
        });
    }

    public int getSelectedBranchId() {
        BranchModel selectedBranch = (BranchModel) branchComboBox.getSelectedItem();
        return selectedBranch.getId();
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

    private void createUIComponents() {
        accessListModel = new DefaultListModel<>();
        bookingListModel = new DefaultListModel<>();

        accessList = new JList(accessListModel);
        bookingList = new JList(bookingListModel);
    }
}
