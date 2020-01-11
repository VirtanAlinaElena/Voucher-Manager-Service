import java.time.format.DateTimeFormatter;

public class LoyaltyVoucher extends Voucher {
    private float reduction; // the reduction which can be used only once during the campaign

    public LoyaltyVoucher(int id, String code, String email, int campaignId,float reduction) {
        super(id, code, email, campaignId);
        this.reduction = reduction;
    }

    public float getReduction() {
        return reduction;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (super.getUsage_date() != null) {
            return ("[" + super.getId() + ";" + super.getStatus() + ";" + super.getEmail() + ";"
                    + this.getReduction() + ";" + super.getCampaignId() + ";" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(super.getUsage_date()) + "]");
        } else {
            return ("[" + super.getId() + ";" + super.getStatus() + ";" + super.getEmail() + ";"
                    + this.getReduction() + ";" + super.getCampaignId() + ";" + super.getUsage_date() + "]");
        }
    }
}
