import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class Campaign {
    private int id;
    private String name;
    private String description;
    private Date start_date;
    private Date end_date;
    private int total_vouchers; // total number of vouchers to be delivered
    private int available_vouchers; // number of available vouchers to be delivered
    private CampaignStatusType status;
    private Strategy strategy;

    public Campaign(int id, String name, String description, String date_start, String date_end, int nrVouchers,
                        Strategy strategy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.total_vouchers = nrVouchers;
        this.available_vouchers = nrVouchers;
        this.strategy = strategy;
        //Specifying the format of the date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            this.start_date = formatter.parse(date_start);
            this.end_date = formatter.parse(date_end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

 //   public void executeStrategy() {
  //      strategy.execute();
   // }

    public CampaignStatusType getStatus() {
        return status;
    }

    public void setStatus(CampaignStatusType status) {
        this.status = status;
    }

//    getVouchers() {
//
//    }
//
//    getVoucher(String code) {
//
//    }
//
//    redeemVoucher(String email, String voucherType, float value) {
//
//    }
//
//    redeemVoucher(String code, LocalDateTime date) {
//
//    }
//
//    getObservers() {
//
//    }
//
//    addObserver(User user) {
//
//    }
//
//    removeObserver(User user) {
//
//    }
//
//    notifyAllObservers(Notification notification) {
//
//    }

    public static void main(String[] args) {
        Campaign c = new Campaign(1, "Campaign-1", "Description-1","2018-08-03 13:30",
                                "2018-08-03 12:30", 100, new StrategyA());

    }
}
