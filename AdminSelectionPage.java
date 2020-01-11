import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class AdminSelectionPage extends JFrame implements ActionListener {
    JButton manageCampaignsButton;
    JButton manageVouchersButton;
    VMS management;

    private static AdminSelectionPage instanceAdmin = null;

    private AdminSelectionPage(String title, VMS management) {
        super(title);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLayout(new GridLayout(1, 2));

        this.management = management;
        manageCampaignsButton = new JButton("Manage the campaigns");
        manageVouchersButton = new JButton("Manage the vouchers");
        manageVouchersButton.setSize(300, 300);
        manageCampaignsButton.setSize(300, 300);
        this.add(manageCampaignsButton);
        this.add(manageVouchersButton);
        manageCampaignsButton.addActionListener(this);
        manageVouchersButton.addActionListener(this);
        pack();
        this.setSize(600, 600);
    }

    public static AdminSelectionPage getInstance(String title, VMS management) {
        if (instanceAdmin == null) {
            instanceAdmin = new AdminSelectionPage(title, management);
        }
        return instanceAdmin;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageCampaignsButton) {
            AdminPageManageCampaigns manageCamp = AdminPageManageCampaigns.getInstance("ADMIN - Manage campaigns", management);
            manageCamp.show();

            // creating timer task, timer
            java.util.Timer timer = new Timer();
            TimerTask tasknew = new TimerTask() {
                @Override
                public void run() {
                    for (int i = 0; i < manageCamp.getTable().getRowCount(); i++) {
                        if ((((String)manageCamp.getTable().getValueAt(i, 3)).compareTo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())) < 0) &&
                                (((String)manageCamp.getTable().getValueAt(i, 4)).compareTo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())) > 0)  ) {
                            manageCamp.getTable().setValueAt(CampaignStatusType.STARTED, i, 7);
                        }
//                        if (((String)manageCamp.getTable().getValueAt(i, 3)).compareTo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())) < 0) {
//                            manageCamp.getTable().setValueAt(CampaignStatusType.NEW, i, 7);
                       // }
                        if (((String)manageCamp.getTable().getValueAt(i, 4)).compareTo(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())) < 0) {
                            manageCamp.getTable().setValueAt(CampaignStatusType.EXPIRED, i, 7);
                        }
                    }
                }
            };
            // scheduling the task at interval
            timer.schedule(tasknew,100, 100);
        }

        if (e.getSource() == manageVouchersButton) {
            AdminPageManageVouchers manageVouch = AdminPageManageVouchers.getInstance("ADMIN - Manage vouchers", management);
            manageVouch.show();
        }
    }
}
