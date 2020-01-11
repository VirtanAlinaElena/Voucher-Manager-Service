import java.util.Map;
import java.util.Vector;

public class StrategyB implements Strategy {
    public Voucher execute(Campaign c) {
        int nrMaxVouchers = 0;
        String loyalEmail = "";
        for (Map.Entry<String, Vector<Voucher>> entry : c.getCampaignMap().entrySet()) {
            int usedVouchers = 0;
            for (Voucher v : entry.getValue()) {
                if (v.getStatus().equals(VoucherStatusType.USED)) {
                    usedVouchers++;
                }
            }
            if (usedVouchers > nrMaxVouchers) {
                nrMaxVouchers = usedVouchers;
                loyalEmail = entry.getKey();
            }
        }
        return c.generateVoucher(loyalEmail, "LoyaltyVoucher", 50);
    }

    public String toString() {
        return "Strategy B";
    }
}
