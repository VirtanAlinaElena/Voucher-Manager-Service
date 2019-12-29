import java.util.*;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private UserType type; // the user can be an ADMIN or a GUEST
    private List<Notification> notifications;
}
