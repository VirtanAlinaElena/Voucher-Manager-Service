public class GiftVoucher extends Voucher {
    private int available_budget; // the available budget which can be used only once during the campaign

    public void setAvailable_budget(int available_budget) {
        this.available_budget = available_budget;
    }

    public int getAvailable_budget() {
        return available_budget;
    }
}
