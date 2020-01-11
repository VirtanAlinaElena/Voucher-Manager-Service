import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Notification {
    private NotificationType type; // notifies if the campaign is EDITED or CANCELED
    private LocalDateTime date;             // the date when the notify has been sent
    private int campaignId;       // the id of the campaign that has been modified
    private List<Integer> voucherCodes;            // the codes of the [ vouchers that the user received during the campaign

    public Notification(int campaignId, List<Integer> voucherCodes, LocalDateTime date, NotificationType type) {
        this.campaignId = campaignId;
        this.voucherCodes = voucherCodes;
        this.date = date;
        this.type = type;
    }

    public NotificationType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public List<Integer> getVoucherCodes() {
        return voucherCodes;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public void setVoucherCodes(List<Integer> voucherCodes) {
        this.voucherCodes = voucherCodes;
    }

    public String toString() {
        return "[" + this.getCampaignId() + ";" + this.getVoucherCodes() + ";" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(this.getDate()) + ";" + this.getType() + "]";
    }
}
