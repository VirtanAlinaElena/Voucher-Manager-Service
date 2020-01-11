import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrategyA implements Strategy {

    public Voucher execute(Campaign c) {
        List<User> observers = c.getObservers();
        List<Integer> observersIds = new ArrayList<>();
        for (User observer : observers) {
            observersIds.add(observer.getUserId());
        }

        // select randomly the id of a campaign's observer
        Random rnd = new Random();
        int randId = 0;
        while (!observersIds.contains(randId)) {
            randId = rnd.nextInt(observersIds.size());
        }

        // get the email of the randomly selected user
        String randomEmail = "";
        for (User observer : observers) {
            if (observer.getUserId() == randId) {
                randomEmail = observer.getEmail();
            }
        }

        // generate the voucher
        return c.generateVoucher(randomEmail, "GiftVoucher", 100);
    }

    public String toString() {
        return "Strategy A";
    }
}
