import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Campaign {
    private int campaignId;
    private String name;
    private String description;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private int totalVouchers; // total number of vouchers to be delivered
    private int availableVouchers; // number of available vouchers to be delivered
    private CampaignStatusType status;
    private Strategy strategy;
    private CampaignVoucherMap campaignMap;
    private List<User> observers = new ArrayList<>(); // the users that have received at least
                                                        // one voucher during this campaign
    public int voucherId = 1;

    public Campaign(int campaignId, String name, String description, LocalDateTime start, LocalDateTime end, int budget, Strategy strategy_type) {
        this.campaignId = campaignId;
        this.name = name;
        this.description = description;
        this.totalVouchers = budget;
        this.availableVouchers = budget;
        this.strategy = strategy_type;
        this.start_date = start;
        this.end_date = end;
        this.observers = new ArrayList<>();
        this.campaignMap = new CampaignVoucherMap();
    }
    public Strategy getStrategy() {
        return strategy;
    }

    public Vector<Voucher> getVouchers() {
        Vector<Voucher> campaignVouchers = new Vector<>();

        for (User user : observers) {
            Vector<Voucher> userVouchers = campaignMap.get(user.getEmail());
            for (Voucher userVoucher : userVouchers) {
                if (!campaignVouchers.contains(userVoucher)) {
                    campaignVouchers.add(userVoucher);
                }
            }
        }
        return campaignVouchers;
    }

    public Voucher getVoucher(String code) {
        for (User user : observers) {
            Vector<Voucher> userVouchers = campaignMap.get(user.getEmail());
            for (Voucher userVoucher : userVouchers) {
                if (code.equals(userVoucher.getCode())) {
                    return userVoucher;
                }
            }
        }
        return null;
    }

    public Voucher generateVoucher(String email, String voucherType, float value) {
        Voucher voucher;
        if (getAvailableVouchers() > 0) {
            // generate the code of the voucher and make sure it's an unique one
            RandomString randomStr = new RandomString();
            String code = randomStr.getAlphaNumericString(5);

            boolean unique = false; // let's suppose the generated code is not unique

            while (!unique) {
                unique = true;
                for (Map.Entry<String, Vector<Voucher>> entry : this.getCampaignMap().entrySet()) {
                    Vector<Voucher> vectVouchers = entry.getValue();
                    for (Voucher v : vectVouchers) {
                        if (v.getCode().equals(code)) {
                            unique = false;
                            code = randomStr.getAlphaNumericString(5); // generate another code
                            break;
                        }
                    }
                    if (unique) {
                        break;
                    }
                }
            }


            if (voucherType.equals("GiftVoucher")) {
                voucher = new GiftVoucher(voucherId, code, email, campaignId, value);
            } else {
                voucher = new LoyaltyVoucher(voucherId, code, email, campaignId, value);
            }
            voucherId++;

            // check if the email is valid; an email is valid if it's not null
            boolean valid = false; // let's suppose the email it's not null
            if (email != null) {
                valid = true;
            }

            if (valid && (this.status.equals(CampaignStatusType.STARTED) || this.status.equals(CampaignStatusType.NEW))) {
                campaignMap.addVoucher(voucher);
                setAvailableVouchers(getAvailableVouchers() - 1);
                return voucher;
            }
        }
        return null;
    }

    public void redeemVoucher(String code, LocalDateTime date) {
        Voucher voucher;

        // search for the voucher in the campaignMap by its code
        for (Map.Entry<String, Vector<Voucher>> campaignEntry : getCampaignMap().entrySet()) {
            Vector<Voucher> campaignVouchers = campaignEntry.getValue();
            for (Voucher v : campaignVouchers) {
                if (v.getCode().equals(code)) {
                    voucher = v;

                    // change the status of the voucher and the usage date of the voucher
                    if (date.isAfter(this.start_date) && date.isBefore(this.end_date) || date.compareTo(this.start_date) == 0) {
                        if (this.getStatus().equals(CampaignStatusType.STARTED) && voucher.getStatus().equals(VoucherStatusType.UNUSED)) {
                            voucher.setStatus(VoucherStatusType.USED);
                            voucher.setUsage_date(date);
                        }
                    }
                }
            }
        }
    }

    public List<User> getObservers() {
        return this.observers;
    }

    // Observer pattern
    public void addObserver(User user) {
        if (!observers.contains(user)) {
            observers.add(user);
        }
    }

    public void removeObserver(User user) {
        observers.remove(user);
    }

    public void notifyAllObservers(Notification notification) {
        for (User user : observers) {
            user.update(notification);
        }
    }

    public int getCampaignId() {
        return campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public int getTotalVouchers() {
        return totalVouchers;
    }

    public void setTotalVouchers(int totalVouchers) {
        this.totalVouchers = totalVouchers;
    }

    public int getAvailableVouchers() {
        return availableVouchers;
    }

    public void setAvailableVouchers(int availableVouchers) {
        this.availableVouchers = availableVouchers;
    }

    public CampaignVoucherMap getCampaignMap() {
        return campaignMap;
    }

    public CampaignStatusType getStatus() {
        return status;
    }

    public void setStatus(CampaignStatusType status) {
        this.status = status;
    }

    public void setStatusAndNotify(Notification notification, CampaignStatusType status) {
        this.setStatus(status);
        notifyAllObservers(notification);
    }

    public Voucher executeStrategy() {
        return strategy.execute(this);
    }

}
