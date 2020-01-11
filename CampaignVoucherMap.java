import java.util.*;

// K = user's email
// V = the collection of vouchers delivered during the campaign

public class CampaignVoucherMap extends ArrayMap<String, Vector<Voucher>> {
    public CampaignVoucherMap() {
        super();
    }
    boolean addVoucher(Voucher v) {
        Vector<Voucher> deliveredVouchers;
        String email = v.getEmail();
        boolean emailExistence = false; // checks if the email exists in the map of the campaign
        if (super.containsKey(email)) {
            emailExistence = true;
            deliveredVouchers = super.get(email);
        } else {
            deliveredVouchers = new Vector<>();
        }
        deliveredVouchers.add(v);
        this.put(email, deliveredVouchers);

        return emailExistence;
    }
}
