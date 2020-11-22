package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.MemberModel;

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
    private JList bookableList;
    private JButton addBookable;
    private int branchId;
    private final DatabaseConnectionHandler dbHandler;

    private ArrayList<DisposableWindow> childrenPanel = new ArrayList<>();

    BookingForm(DatabaseConnectionHandler dbHandler, int branchId) {
        this.dbHandler = dbHandler;
        this.branchId = branchId;

        JFrame frame = new JFrame("Booking Form");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        setupDateSpinner();
        setupEnterButton();
        setupMemberComboBox();

        // We don't want the windows to be too cluttered. So if we press the main window, we also kill all the children windows
        jpanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeAllChildren();
            }
        });
    }

    public void setupEnterButton() {
        enterForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(memberComboBox.getSelectedItem().toString());
            }
        });
    }

    public void setupDateSpinner() {
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "h:mm a, EEE, dd-MMM-yyyy");
        dateSpinner.setEditor(timeEditor);
        dateSpinner.setValue(new Date(new java.util.Date().getTime()));
    }

    public void setupMemberComboBox() {
        for (MemberModel memberModel: dbHandler.memberHandler.getMemberInfoInBranch(branchId)) {
            memberComboBox.addItem(memberModel);
        }
    }

    private void createUIComponents() {
        dateSpinner = new JSpinner(new SpinnerDateModel());
    }

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
