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
    private JLabel reservationInLastEvent;
    private JList memberSignUpAll;
    private JTable eventBookableCountTable;
    private DatabaseConnectionHandler dbHandler;

    private DefaultTableModel accessAggregateTableModel = new DefaultTableModel();

    public ComplexQueriesWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Complex Queries");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);


        // One of complex query. Find the number of reservation in the most recent events
        reservationInLastEvent.setText(dbHandler.bookableHandler.countOfBookablesUsedInTheLatestEventDate() + "");
        // Big font
        reservationInLastEvent.setFont(new Font(reservationInLastEvent.getFont().getName(), Font.BOLD, reservationInLastEvent.getFont().getSize() + 4));


        setupEventBookableCountTable();
        setupMemberDivisionList();
        setupAccessAggregateTable();
    }

    // One of complex query. Use division to find the members that signs up in all the branches
    public void setupMemberDivisionList() {
        DefaultListModel defaultListModel = new DefaultListModel();
        memberSignUpAll.setModel(defaultListModel);
        for (String s : dbHandler.memberHandler.getMemberNameInAllBranches()) {
            defaultListModel.addElement(s);
        }
    }

    // One of complex query. Find the number of bookable each events uses
    public void setupEventBookableCountTable() {
        DefaultTableModel model = new DefaultTableModel();
        eventBookableCountTable.setModel(model);

        model.addColumn("Name");
        model.addColumn("Count");

        HashMap<String, Integer> map = dbHandler.bookableHandler.bookableCountGroupedByEvents();
        for (String s : map.keySet().stream()
                .sorted()
                .collect(Collectors.toList())) {
            model.addRow(new String[] {s, map.get(s) + ""});
        }
    }

    // One of complex query. Find the number of public area access for each member
    // that is larger than accessAggregateMinCount text field
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

    // Get the mincount, if user input malicious value, default it to 0
    // Then add the <Name, Access Count> pair into the table
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
