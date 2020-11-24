package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.helper.DateUtils;
import ca.ubc.cs304.model.BookableModel;
import ca.ubc.cs304.model.BookingModel;
import ca.ubc.cs304.model.MemberModel;
import ca.ubc.cs304.model.ReserveRelation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.util.ArrayList;

public class BookingForm implements DisposableWindow {
    private JButton enterForm;
    private JComboBox memberComboBox;
    public JPanel jpanel;
    private JSpinner dateSpinner;
    private JComboBox equipOrRoom;
    private JComboBox bookableChoice;
    private JButton addBookable;
    private int branchId;
    private final DatabaseConnectionHandler dbHandler;

    private ArrayList<DisposableWindow> childrenPanel = new ArrayList<>();

    private DefaultListModel<BookableModel> model;
    private JList bookableList;

    BookingForm(DatabaseConnectionHandler dbHandler, int branchId) {
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
        setupAddingBookable();

        // We don't want the windows to be too cluttered. So if we press the main window, we also kill all the children windows
        jpanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeAllChildren();
            }
        });
    }

    // When enterForm is pressed, we submit the new booking in the SQL statement
    // We also add any bookables that the user book into this booking
    private void setupEnterButton() {
        enterForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookingNumber = dbHandler.bookingHandler.getNextId();
                BookingModel bookingModel = new BookingModel(bookingNumber,
                        DateUtils.normalize((java.util.Date) dateSpinner.getValue()),
                        ((MemberModel) memberComboBox.getSelectedItem()).getId(),
                        branchId);

                dbHandler.bookingHandler.insertBooking(bookingModel);

                for (int i = 0; i < model.getSize(); i++) {
                    BookableModel bookableModel = model.getElementAt(i);

                    ReserveRelation reserveRelation = new ReserveRelation(bookingNumber, bookableModel.getBookableId());
                    dbHandler.reserveHandler.insertReserve(reserveRelation);
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
        for (MemberModel memberModel: dbHandler.memberHandler.getMemberSignedUpInBranch(branchId)) {
            memberComboBox.addItem(memberModel);
        }
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
                model.addElement((BookableModel) bookableChoice.getSelectedItem());
                refreshBookableList();
            }
        });
    }

    // This configure the choice box for the bookable. It will change when the user re-select the equipment or room choice
    private void refreshBookableList() {
        bookableChoice.removeAllItems();
        if (equipOrRoom.getSelectedIndex() == 0) {
            for (BookableModel bookableModel : dbHandler.bookableHandler.getEquipmentInfoInBranch(branchId)) {
                if (!model.contains(bookableModel))
                    bookableChoice.addItem(bookableModel);
            }
        } else {
            for (BookableModel bookableModel : dbHandler.bookableHandler.getRoomInfoInBranch(branchId)) {
                if (!model.contains(bookableModel))
                    bookableChoice.addItem(bookableModel);
            }
        }
    }

    private void createUIComponents() {
        dateSpinner = new JSpinner(new SpinnerDateModel());
        model = new DefaultListModel<>();
        bookableList = new JList(model);
    }

    /*
     * THIS IS FOR UI PURPOSES ONLY THERE IS NO EXTRA FUNCTIONALITIES FOR THIS FEATURE AT ALL
     *
     * Disposable Window is a system implemented to keep the windows from being to cluttered
     * It closes all the childrens whenever the parent window is clicked so there won't be too many
     * windows running around
     */
    private void closeAllChildren() {
        for (DisposableWindow disposableWindow : childrenPanel) {
            disposableWindow.close();
        }
    }

    public void close() {
        closeAllChildren();
        SwingUtilities.getWindowAncestor(jpanel).dispose();
    }
}
