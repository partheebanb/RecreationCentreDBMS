package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ComplexQueriesWindow implements DisposableWindow {
    private JPanel jpanel;
    private JTable accessAggregateTable;
    private JTextField accessAggregateMinCount;
    private JButton accessAggregateRefreshButton;
    private JLabel ReservationInLastEvent;
    private DatabaseConnectionHandler dbHandler;

    private DefaultTableModel accessAggregateTableModel = new DefaultTableModel();

    public ComplexQueriesWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Booking Form");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        ReservationInLastEvent.setText(dbHandler.bookableHandler.countOfBookablesUsedInTheLatestEventDate() + " Bookable's Reservation");

        setupAccessAggregateTable();
    }

    public void setupAccessAggregateTable() {
        accessAggregateTable.setModel(accessAggregateTableModel);
        accessAggregateTableModel.addColumn("Name");
        accessAggregateTableModel.addColumn("Count");

        refreshAccessAggregateTable();

        accessAggregateRefreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAccessAggregateTable();
            }
        });
    }

    public void refreshAccessAggregateTable() {
        int minCount = 0;
        try {
            minCount = Integer.parseInt(accessAggregateMinCount.getText());
        } catch (NumberFormatException e) {
            minCount = 0;
        }

        while (accessAggregateTableModel.getRowCount() != 0) {
            accessAggregateTableModel.removeRow(0);
        }

        Map<MemberModel, Integer> memberCountMap = dbHandler.accessHandler.avgAccessGroupedByDateHaving(minCount);

        // Sort the map before adding all element, make it look pretty
        for (MemberModel memberModel : memberCountMap.keySet().stream()
                                        .sorted(Comparator.comparing(MemberModel::toString))
                                        .collect(Collectors.toList())) {
            // Add the row as key, value
            accessAggregateTableModel.addRow(new Object[]{memberModel, memberCountMap.get(memberModel)});
        }
    }

    public void close() {
        SwingUtilities.getWindowAncestor(jpanel).dispose();
    }
}
