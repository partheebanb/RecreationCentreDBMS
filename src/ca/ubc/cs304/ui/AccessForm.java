package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.helper.DateUtils;
import ca.ubc.cs304.model.AccessRelation;
import ca.ubc.cs304.model.MemberModel;
import ca.ubc.cs304.model.PublicAreaModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class AccessForm implements DisposableWindow {
    private JButton enterForm;
    private JComboBox memberComboBox;
    private JSpinner dateSpinner;
    private JComboBox areaComboBox;
    private JPanel jpanel;

    private DatabaseConnectionHandler dbHandler;
    private int branchId;

    public AccessForm(DatabaseConnectionHandler dbHandler, int branchId) {
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

        setupDateSpinner();
        setupEnterButton();
        setupMemberComboBox();
        setupAreaComboBox();
    }

    private void setupEnterButton() {
        enterForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbHandler.accessHandler.insertAccess(new AccessRelation(
                        ((MemberModel) memberComboBox.getSelectedItem()).getId(),
                        ((PublicAreaModel) areaComboBox.getSelectedItem()).getAreaId(),
                        (DateUtils.normalize((java.util.Date) dateSpinner.getValue()))
                ));
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
    }

    private void setupAreaComboBox() {
        for (PublicAreaModel publicAreaModel : dbHandler.publicAreaHandler.getPublicAreaInfoInBranch(branchId)) {
            areaComboBox.addItem(publicAreaModel);
        }
    }

    private void createUIComponents() {
        dateSpinner = new JSpinner(new SpinnerDateModel());
    }

    public void close() {
        SwingUtilities.getWindowAncestor(jpanel).dispose();
    }
}
