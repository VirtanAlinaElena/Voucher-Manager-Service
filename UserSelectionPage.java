import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Vector;

public class UserSelectionPage extends JFrame implements ActionListener {
    JButton viewCampaignsButton;
    JButton viewVouchersButton;
    JButton viewNotificationsButton;
    VMS management;
    User user;
    public UserSelectionPage(String title, VMS management, User user) {
        super(title);
        this.user = user;
        this.management = management;
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLayout(new GridLayout(1, 3));

        viewCampaignsButton = new JButton("Your Campaigns");
        viewVouchersButton = new JButton("Your Vouchers");
        viewNotificationsButton = new JButton("Notifications");
        viewVouchersButton.setSize(300, 300);
        viewCampaignsButton.setSize(300, 300);
        viewNotificationsButton.setSize(300, 300);
        this.add(viewCampaignsButton);
        this.add(viewVouchersButton);
        this.add(viewNotificationsButton);

        viewCampaignsButton.addActionListener(this);
        viewVouchersButton.addActionListener(this);
        viewNotificationsButton.addActionListener(this);
        pack();
        this.setSize(600, 600);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewCampaignsButton) {
            JFrame f1 = new JFrame();
            JPanel panel = new JPanel();
            String str = "Hi, " + user.getName() + "! " + "Check out the list of campaigns you have received vouchers from:"+"\n";
            for (Map.Entry<Integer, Vector<Voucher>> entry : user.getUserMap().entrySet()) {
                int campaignId = entry.getKey();
                str = str + management.getCampaign(campaignId).getName() + "\n";
            }
            JTextArea text = new JTextArea(str, 37, 47);
            JScrollPane pane = new JScrollPane(text);

            panel.add(pane);
            f1.add(panel);
            f1.setTitle("Your Campaigns");
            f1.setSize(600, 600);
            f1.setVisible(true);
            f1.setDefaultCloseOperation(HIDE_ON_CLOSE);

        }

        if (e.getSource() == viewVouchersButton) {
            JFrame f2 = new JFrame();
            JPanel panel = new JPanel();

            String str = "Hi, " + user.getName() + "! " + "Check out your list of vouchers:" + "\n";
            for (Map.Entry<Integer, Vector<Voucher>> entry : user.getUserMap().entrySet()) {
                Vector<Voucher> vouchers = entry.getValue();
                for (Voucher v : vouchers) {
                    if (v.getEmail().equals(user.getEmail())) {
                        str = str + "The voucher " + v.getCode() + " from campaign " + v.getCampaignId() + ": "
                                + v.getStatus() + "\n";
                    }
                }
            }

            JTextArea text = new JTextArea(str, 37, 25);
            JScrollPane pane = new JScrollPane(text);

            panel.add(pane);
            f2.add(panel);
            f2.setTitle("Your Vouchers");
            f2.setSize(600, 600);
            f2.setVisible(true);
            f2.setDefaultCloseOperation(HIDE_ON_CLOSE);
        }

        if (e.getSource() == viewNotificationsButton) {
            JFrame f3 = new JFrame();
            JPanel panel = new JPanel();

            String str = "Hi, " + user.getName() + "! " + "Check out your list of notifications:" + "\n";
            for (Notification notif : user.obtainNotifications()) {
                if (notif.getType().equals(NotificationType.CANCEL)) {
                    str = str + "Campaign " + notif.getCampaignId() + " was cancelled at " +
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(notif.getDate()) + "\n";
                }
                if (notif.getType().equals(NotificationType.EDIT)) {
                    str = str + "Campaign " + notif.getCampaignId() + " was cancelled at " +
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(notif.getDate()) + "\n";
                }
            }

            JTextArea text = new JTextArea(str, 37, 47);
            JScrollPane pane = new JScrollPane(text);

            panel.add(pane);
            f3.add(panel);
            f3.add(panel);
            f3.setTitle("Notifications");
            f3.setSize(600, 600);
            f3.setVisible(true);
            f3.setDefaultCloseOperation(HIDE_ON_CLOSE);
        }
    }
}