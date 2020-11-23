package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.AccessRelation;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow {
    private JPanel mainPanel;
    private JButton addBooking;
    private JComboBox branchComboBox;
    private JList bookingList;
    private JList accessList;
    private JButton viewMembersButton;
    private DatabaseConnectionHandler dbHandler;

    private HashMap<Integer, String> memberIdToName;

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

        setupBranchComboBox();
        refreshLists();

        // When the add booking button is pressed, create a window that helps to add booking
        addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                childrenPanel.add(new BookingForm(dbHandler, getSelectedBranchId()));
            }
        });

        viewMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                childrenPanel.add(new MemberWindow(dbHandler));
            }
        });


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

    public int getSelectedBranchId() {
        BranchModel selectedBranch = (BranchModel) branchComboBox.getSelectedItem();
        return selectedBranch.getId();
    }

    public void refreshLists() {
        accessListModel.removeAllElements();
        for (String accessRelation : dbHandler.bookingHandler.getBookingInBranchString(getSelectedBranchId())) {
    //        accessListModel.addElement(accessRelation);
        }

        bookingListModel.removeAllElements();
        for (String booking : dbHandler.bookingHandler.getBookingInBranchString(getSelectedBranchId())) {
            bookingListModel.addElement(booking);
        }
    }

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
