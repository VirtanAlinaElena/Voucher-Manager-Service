
// K = id of the campaign
// V = the collection of vouchers received by a user

import java.util.Vector;

public class UserVoucherMap extends ArrayMap<Integer, Vector<Voucher>> {
    public UserVoucherMap() {
        super();
    }
    public boolean addVoucher(Voucher v) {
        int campaignId = v.getCampaignId();
        boolean campaignExistance = false;
        Vector<Voucher> receivedVouchers;

        if (super.containsKey(campaignId) == true) {
            campaignExistance = true;
            receivedVouchers = super.get(campaignId);
        } else {
            receivedVouchers = new Vector<>();
        }

        receivedVouchers.add(v);
        this.put(campaignId, receivedVouchers);

        return campaignExistance;
    }
}
