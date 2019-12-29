import java.util.*;

public class Notification {
    private NotificationType type; // notifies if the campaign is EDITED or CANCELED
    private Date date;             // the date when the notify has been sent
    private int campaign_id;       // the id of the campaign that has been modified
    List<String> codes;            // the codes that the user received during the campaign
}
