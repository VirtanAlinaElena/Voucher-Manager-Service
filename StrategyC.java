import java.util.Map;
import java.util.Vector;

public class StrategyC implements Strategy {
    public Voucher execute(Campaign c) {
        int nrMinVouchers = 99999999;
        String poorestEmail = "";
        for (User observer : c.getObservers()) {
            int nrVouchers = 0;
            for (Map.Entry<Integer, Vector<Voucher>> entry : observer.getUserMap().entrySet()) {
                for (Voucher v : entry.getValue()) {
                    if (v.getCampaignId() == c.getCampaignId()) {
                        nrVouchers++;
                    }
                }
            }
            if (nrVouchers < nrMinVouchers) {
                nrMinVouchers = nrVouchers;
                poorestEmail = observer.getEmail();
            }
        }
        return c.generateVoucher(poorestEmail, "GiftVoucher", 100);
    }

    public String toString() {
        return "Strategy C";
    }
}
