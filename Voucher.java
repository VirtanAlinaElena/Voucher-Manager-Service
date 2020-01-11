import java.time.LocalDateTime;

public abstract class Voucher {
    private int id; // voucher id
    private String code; // voucher code
    private VoucherStatusType status; //  the voucher can be USED or UNUSED
    private LocalDateTime usage_date; // the date when the vouched was used
    private String email; // the voucher was delivered to this email
    private int campaignId; // the id of the campaign that generated the voucher

    public Voucher(int id, String code, String email, int campaignId) {
        this.id = id;
        this.code = code;
        this.status = VoucherStatusType.UNUSED;
        this.usage_date = null;
        this.email = email;
        this.campaignId = campaignId;
    }

    public int getId() {
        return id;
    }

    public VoucherStatusType getStatus() {
        return status;
    }

    public void setStatus(VoucherStatusType status) {
        this.status = status;
    }

    public LocalDateTime getUsage_date() {
        return usage_date;
    }

    public void setUsage_date(LocalDateTime usage_date) {
        this.usage_date = usage_date;
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public int getCampaignId() {
        return campaignId;
    }

}
