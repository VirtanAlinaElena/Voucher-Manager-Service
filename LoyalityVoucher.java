public class LoyalityVoucher extends Voucher {
    private int reduction; // the reduction which can be used only once during the campaign

    public int getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }
}
