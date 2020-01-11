import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminPageManageCampaigns extends JFrame implements ActionListener, ChangeListener {
    JButton addCampaignButton;
    JButton editCampaignButton;
    JButton cancelCampaignButton;
    JButton viewDetailsButton;
    JTextArea details;
    JTable table;
    JPanel buttonsPanel;
    JPanel viewDetailsPanel;
    DefaultTableModel model;
    VMS management;

    private static AdminPageManageCampaigns instance = null;

    private AdminPageManageCampaigns(String title, VMS management) {
        super(title);
        this.management = management;
        setLayout(new BorderLayout());
        setSize(new Dimension(1000, 1000));
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        viewDetailsPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(viewDetailsPanel, BoxLayout.Y_AXIS);

        details = new JTextArea("To add a new campaign, add campaign details here and then click on addCampaignButton" + "\n" +
                                "To edit a campaign, add campaign details here and then click on editCampaignButton" + "\n" +
                               "To cancel a campaign, add the campaign id here and then click on cancelCampaignButton" + "\n" +
                            "To view details about a campaign, add the campaign id here and then click on viewDetailsButton");
        details.setRows(4);
        details.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                details.setText("");
            }
        });
        details.setAlignmentX(Component.CENTER_ALIGNMENT);

        addCampaignButton = new JButton("addCampaign");
        editCampaignButton = new JButton("editCampaign");
        cancelCampaignButton = new JButton("cancelCampaign");
        viewDetailsButton = new JButton("viewDetails");

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("Id");
        model.addColumn("Name");
        model.addColumn("Description");
        model.addColumn("Start Date");
        model.addColumn("End Date");
        model.addColumn("Total Vouchers");
        model.addColumn("Available Vouchers");
        model.addColumn("Status");
        model.addColumn("Strategy");

        // Table Sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
        table.setRowSorter(sorter);

        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        // Table Populating
        if (management != null) {
            for (int i = 1; i <= management.getCampaigns().size(); i++) {
                Campaign c = management.getCampaign(i);
                model.insertRow(model.getRowCount(), new Object[]{c.getCampaignId(), c.getName(), c.getDescription(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(c.getStart_date()),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(c.getEnd_date()),
                        c.getTotalVouchers(), c.getAvailableVouchers(), c.getStatus(), c.getStrategy()});
            }
        }

        table.setPreferredScrollableViewportSize(new Dimension(1100, 150));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        viewDetailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Buttons Panel
        buttonsPanel.add(addCampaignButton);
        buttonsPanel.add(editCampaignButton);
        buttonsPanel.add(cancelCampaignButton);

        // View Details Panel
        viewDetailsPanel.add(viewDetailsButton);
        viewDetailsPanel.add(details);
        viewDetailsPanel.setLayout(boxLayout);

        this.add(scrollPane, BorderLayout.NORTH);
        this.add(buttonsPanel, BorderLayout.CENTER);
        this.add(viewDetailsPanel, BorderLayout.SOUTH);

        addCampaignButton.addActionListener(this);
        editCampaignButton.addActionListener(this);
        cancelCampaignButton.addActionListener(this);
        viewDetailsButton.addActionListener(this);
        pack();
    }

    public JTable getTable() {
        return table;
    }

    public static AdminPageManageCampaigns getInstance(String title, VMS management) {
        if (instance == null) {
            instance = new AdminPageManageCampaigns(title, management);
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == details) {
            details.setText("");
        }

        // *** ADD CAMPAIGN ***
        if (e.getSource() == addCampaignButton) {
            String[] info = details.getText().split(";", 10);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(info[3], format);
            LocalDateTime endDate = LocalDateTime.parse(info[4], format);

            Strategy strategy;
            if (info[6].equals("A")) {
                strategy = new StrategyA();
            } else {
                if (info[6].equals("B")) {
                    strategy = new StrategyB();
                } else {
                    strategy = new StrategyC();
                }
            }

            Campaign c = new Campaign(Integer.parseInt(info[0]),
                    info[1], info[2], startDate, endDate,
                    Integer.parseInt(info[5]), strategy);

            // check if the campaign exists in the VMS
            boolean campaignExistance = false;
            for (int i = 0; i < table.getRowCount() && !campaignExistance; i++) {
                if (c.getCampaignId() == (Integer)table.getValueAt(i, 0) ||
                    c.getName().equals((String)table.getValueAt(i, 1))) {
                    campaignExistance = true;
                }
            }

            // display an error window if the campaign exists or add the campaign to the VMS and to the tabel if
            // the campaign doesn't exist
            if (campaignExistance) {
                JOptionPane.showMessageDialog(this, "The campaign already exists in the VMS",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                management.addCampaign(c);
                model.insertRow(model.getRowCount(), new Object[]{c.getCampaignId(), c.getName(), c.getDescription(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(c.getStart_date()),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(c.getEnd_date()),
                        c.getTotalVouchers(), c.getAvailableVouchers(), c.getStatus(), strategy.toString()});
                details.setText("");
            }

        }

        // *** EDIT CAMPAIGN ***
        if (e.getSource() == editCampaignButton) {
            String[] info = details.getText().split(";", 10);
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(info[3], format);
            LocalDateTime endDate = LocalDateTime.parse(info[4], format);

            Campaign newCampaign = new Campaign(Integer.parseInt(info[0]), info[1], info[2], startDate, endDate, Integer.parseInt(info[5]), null);

            // check if the campaign can be modified based on its status
            boolean editCampaignPossibility = false;
            int index = 0;
            for (int i = 0; i < table.getRowCount() && !editCampaignPossibility; i++) {
                if ((int)table.getValueAt(i, 0) == Integer.parseInt(info[0]) &&
                                (table.getValueAt(i, 7).equals(CampaignStatusType.NEW) ||
                                table.getValueAt(i, 7).equals(CampaignStatusType.STARTED))) {
                    editCampaignPossibility = true;
                    index = i;

                }
            }

            if (editCampaignPossibility == true) {
                management.updateCampaign(Integer.parseInt(info[0]), newCampaign);
                Campaign c = management.getCampaign(Integer.parseInt(info[0]));
                table.setValueAt(c.getName(), index, 1);
                table.setValueAt(c.getDescription(), index, 2);
                table.setValueAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(c.getStart_date()), index, 3);
                table.setValueAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(c.getEnd_date()), index, 4);
                table.setValueAt(c.getTotalVouchers(), index, 5);
                table.setValueAt(c.getAvailableVouchers(), index, 6);
                table.setValueAt(c.getStatus(), index, 7);
            } else {
                JOptionPane.showMessageDialog(this, "This campaign can't be modified",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // *** CANCEL CAMPAIGN ***
        if (e.getSource() == cancelCampaignButton) {
            int id = Integer.parseInt(details.getText());
            management.cancelCampaign(id);
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((int)table.getValueAt(i, 0) == id) {
                    table.setValueAt(CampaignStatusType.CANCELED, i, 7);
                }
            }
        }

        // *** VIEW DETAILS ABOUT CAMPAIGN ***
        if (e.getSource() == viewDetailsButton) {
            int id = Integer.parseInt(details.getText());
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((int)table.getValueAt(i, 0) == id) {
                    String s = "";
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        if (j == table.getColumnCount() - 1) {
                            s = s + table.getValueAt(i, j);
                        } else {
                            s = s + table.getValueAt(i, j) + ", ";
                        }
                    }
                    details.setText(s);
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {

    }
}
