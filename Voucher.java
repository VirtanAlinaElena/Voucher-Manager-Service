import java.util.Date;

public abstract class Voucher {
    private int id; // voucher id
    private int code; // voucher code
    private VoucherStatusType status; //  the voucher can be USED or UNUSED
    private Date usage_date; // the date when the vouched was used
    private String email; // the voucher was delivered to this email
    private int campaign_id; // the id of the campaign that generated the voucher
}
