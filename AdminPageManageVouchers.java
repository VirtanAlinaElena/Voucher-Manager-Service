import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Vector;

public class AdminPageManageVouchers extends JFrame implements KeyListener, ActionListener {
    VMS management;
    JTextField voucherCode;
    JTextField voucherInfo;
    JTextField campaignNumber;
    JLabel searchVoucherLabel;
    JLabel generateVoucherLabel;
    JLabel searchCampaignLabel;
    JTable table;
    DefaultTableModel model;
    JPanel panel1, panel2, panel3;
    JButton multipleDistributionButton;
    private static AdminPageManageVouchers instance = null;

    private AdminPageManageVouchers(String title, VMS management) {
        super(title);
        this.management = management;

        setLayout(new BorderLayout());
        setSize(new Dimension(1000, 1000));
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        multipleDistributionButton = new JButton("Multiple Voucher Distribution");
        // TextField & Labels for the text field
        panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        campaignNumber = new JTextField("Enter the campaign id here");
        searchCampaignLabel = new JLabel("Display information about campaign: ");
        panel1.add(searchCampaignLabel);
        panel1.add(campaignNumber);

        panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        voucherCode = new JTextField("Enter the voucher code here");
        searchVoucherLabel = new JLabel("Redeem the voucher: ");
        panel2.add(searchVoucherLabel);
        panel2.add(voucherCode);

        panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        voucherInfo = new JTextField("Enter the voucher information here", 50);
        generateVoucherLabel = new JLabel("Generate voucher: ");
        panel3.add(generateVoucherLabel);
        panel3.add(voucherInfo);
        panel3.add(multipleDistributionButton);

        JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout());
        panel4.add(panel2, BorderLayout.NORTH);
        panel4.add(panel3, BorderLayout.SOUTH);
        panel4.setVisible(true);

        model = new DefaultTableModel();
        table = new JTable(model);
        model.addColumn("VoucherId");
        model.addColumn("CampaignId");
        model.addColumn("Code");
        model.addColumn("Status");
        model.addColumn("Usage Date");
        model.addColumn("Sent to");
        model.addColumn("Type");
        model.addColumn("Budget");

        table.setPreferredScrollableViewportSize(new Dimension(1100, 150));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        this.add(panel1, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panel4, BorderLayout.SOUTH);

        voucherCode.addKeyListener(this);
        voucherInfo.addKeyListener(this);
        campaignNumber.addKeyListener(this);
        multipleDistributionButton.addActionListener(this);

        pack();
    }

    public static AdminPageManageVouchers getInstance(String title, VMS management) {
        if (instance == null) {
            instance = new AdminPageManageVouchers(title, management);
        }
        return instance;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == multipleDistributionButton) {
            File file;
            BufferedReader br;
            String str;
            int nrEmails = 0;

            file = new File("/home/alinavirtan/Desktop/tema_POO/emails.txt");
            try {
                br = new BufferedReader(new FileReader(file));
                nrEmails = Integer.parseInt(br.readLine());
                while ((str = br.readLine()) != null) {
                    String[] info = str.split(";", 5);
                    generateVoucherUI(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        // *** REDEEM A VOUCHER ***
        if (e.getSource() == voucherCode && e.getKeyCode() == KeyEvent.VK_ENTER) {
            Campaign c = management.getCampaign(Integer.parseInt(campaignNumber.getText()));
            // search the voucher by the code and mark it as used
            for (int i = 0; i < table.getRowCount(); i++) {
                String tableCode = (String)table.getValueAt(i,2);
                int id = (Integer)table.getValueAt(i, 0);

                if (tableCode.equals(voucherCode.getText())) {
                    LocalDateTime redeemDate = LocalDateTime.now().withNano(0).withSecond(0);
                    table.setValueAt(VoucherStatusType.USED, i, 3);
                    table.setValueAt(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(redeemDate), i, 4);
                    for (Map.Entry<String, Vector<Voucher>> entry : c.getCampaignMap().entrySet()) {
                        Vector<Voucher> v = entry.getValue();
                        for (Voucher voucher : v) {
                            if (voucher.getId() == id) {
                                c.redeemVoucher(voucher.getCode(), redeemDate);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // *** GENERATE A VOUCHER ***
        if (e.getSource() == voucherInfo && e.getKeyCode() == KeyEvent.VK_ENTER) {
            String[] info = voucherInfo.getText().split(";", 5);
            generateVoucherUI(info);
        }

    }

    public void generateVoucherUI(String[] info) {
        Campaign c = management.getCampaign(Integer.parseInt(campaignNumber.getText()));

        // generate the voucher
        Voucher voucher = c.generateVoucher(info[0], info[1], Integer.parseInt(info[2]));
        if (voucher != null) {
            for (User receiver : management.getUsers()) {
                if (receiver.getEmail().equals(voucher.getEmail()))
                    management.getCampaign(c.getCampaignId()).addObserver(receiver);
                    receiver.getUserMap().addVoucher(voucher);
            }
            model.insertRow(model.getRowCount(), new Object[]{model.getRowCount() + 1, c.getCampaignId(), voucher.getCode(), voucher.getStatus(), voucher.getUsage_date(),
                    voucher.getEmail(), info[1], Integer.parseInt(info[2])});
            AdminPageManageCampaigns camp = AdminPageManageCampaigns.getInstance("ADMIN - Manage campaigns", management);

            // put the voucher in the table
            for (int row = 0; row < camp.getTable().getRowCount(); row++) {
                if (c.getCampaignId() == (Integer)camp.getTable().getValueAt(row, 0))
                    camp.getTable().setValueAt((Integer)camp.getTable().getValueAt(row, 6) - 1, row, 6);
            }
        }
    }
}
