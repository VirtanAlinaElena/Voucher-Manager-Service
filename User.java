import java.util.*;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private UserType type; // the user can be an ADMIN or a GUEST
    private UserVoucherMap userMap;
    List<Notification> notifications;

    public User(int id, String name, String password, String email, UserType type) {
        this.userId = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.type = type;
        this.userMap = new UserVoucherMap();
        this.notifications = new ArrayList<>();
    }

    public void update(Notification notification) {
        List<Integer> voucherCodes = new ArrayList<>();
        Vector<Voucher> vouchers = this.getUserMap().get(notification.getCampaignId());

        for (Voucher vouch : vouchers) {
            voucherCodes.add(vouch.getId());
        }

        Notification notification1 = new Notification(notification.getCampaignId(), null, notification.getDate(), notification.getType());
        notification1.setVoucherCodes(voucherCodes);
        this.notifications.add(notification1);
    }

    public String getPassword() {
        return this.password;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public UserType getType() {
        return this.type;
    }

    public UserVoucherMap getUserMap() {
        return this.userMap;
    }

    public void setUserMap(UserVoucherMap userMap) {
        this.userMap = userMap;
    }

    public String getNotifications() {
        String s = "";
        for (Notification notif : notifications) {
            s = s + notif.toString();
        }
        if (s == "") {
            return "[]";
        }
        return s;
    }

    public List<Notification> obtainNotifications() {
        return notifications;
    }

    public String toString() {
        return "[" + this.getUserId() + ";" + this.getName() + ";" + this.getEmail() + ";" + this.getType() + "]";
    }
}
