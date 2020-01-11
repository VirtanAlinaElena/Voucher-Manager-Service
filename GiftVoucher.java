import java.time.format.DateTimeFormatter;

public class GiftVoucher extends Voucher {
    private float available_budget; // the available budget which can be used only once during the campaign

    public GiftVoucher(int id, String code, String email, int campaignId, float available_budget) {
        super(id, code, email, campaignId);
        this.available_budget = available_budget;
    }

    public float getAvailable_budget() {
        return available_budget;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (this.getUsage_date() != null) {
            return ("[" + super.getId() + ";" + super.getStatus() + ";" + super.getEmail() + ";"
                    + this.getAvailable_budget() + ";" + super.getCampaignId() + ";" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(super.getUsage_date()) + "]");
        } else {
            return ("[" + super.getId() + ";" + super.getStatus() + ";" + super.getEmail() + ";"
                    + this.getAvailable_budget() + ";" + super.getCampaignId() + ";" + super.getUsage_date() + "]");
        }
    }
}
