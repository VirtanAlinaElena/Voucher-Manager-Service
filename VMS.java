import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class VMS {
    private List<Campaign> campaigns;
    private List<User> users;
    private LocalDateTime applicationStartDate;

    public void setApplicationStartDate(LocalDateTime applicationStartDate) {
        this.applicationStartDate = applicationStartDate;
    }

    public LocalDateTime getApplicationStartDate() {
        return applicationStartDate;
    }

    // Singleton Pattern
    private static VMS instance = null;
    private VMS(List<Campaign> campaigns, List<User> users, LocalDateTime applicationStartDate) {
        this.campaigns = campaigns;
        this.users = users;
        this.applicationStartDate = applicationStartDate;
        for (Campaign c : campaigns) {
            if (c.getStart_date().isBefore(applicationStartDate)) {
                c.setStatus(CampaignStatusType.NEW);
            }

            if (applicationStartDate.isAfter(c.getStart_date()) && applicationStartDate.isBefore(c.getEnd_date()) ||
                    c.getStart_date().compareTo(applicationStartDate) == 0) {
                c.setStatus(CampaignStatusType.STARTED);
            }
            if (applicationStartDate.compareTo(c.getEnd_date()) == 0 || applicationStartDate.isAfter(c.getEnd_date())) {
                c.setStatus(CampaignStatusType.EXPIRED);
            }
        }
    }

    public static VMS getInstance(List<Campaign> campaigns, List<User> users, LocalDateTime applicationStartDate) {
        if (instance == null) {
            instance = new VMS(campaigns, users, applicationStartDate);
        }
        return instance;
    }

    // return a list of available campaigns
    public List<Campaign> getCampaigns() {
        return campaigns;
    }

    // get a campaign by its id
    public Campaign getCampaign(Integer id) {
        for (Campaign c : campaigns) {
            if (c.getCampaignId() == id) {
                return c;
            }
        }
        return null;
    }

    // update a campaign's information based on its status
    public void updateCampaign(Integer id, Campaign campaign) {
        for (Campaign c : campaigns) {
            if (c.getCampaignId() == id) {
                // I modify the campaign c only if its status is NEW or STARTED
                if (c.getStatus().equals(CampaignStatusType.NEW) || c.getStatus().equals(CampaignStatusType.STARTED)) {
                    if (c.getStatus().equals(CampaignStatusType.NEW)) {
                        c.setName(campaign.getName());
                        c.setDescription(campaign.getDescription());
                        c.setStart_date(campaign.getStart_date());
                        c.setEnd_date(campaign.getEnd_date());

                    } else if (c.getStatus().equals(CampaignStatusType.STARTED)) {
                        c.setTotalVouchers(campaign.getTotalVouchers());
                        c.setAvailableVouchers(campaign.getAvailableVouchers());
                        c.setEnd_date(campaign.getEnd_date());
                    } else {
                        break;
                    }

                    if (applicationStartDate.isBefore(c.getStart_date())) {
                        Notification notification = new Notification(c.getCampaignId(), null, applicationStartDate, NotificationType.EDIT);
                        c.setStatusAndNotify(notification, CampaignStatusType.NEW);
                    }

                    if (applicationStartDate.isBefore(c.getEnd_date()) && applicationStartDate.isAfter(c.getStart_date()) ||
                            applicationStartDate.compareTo(c.getStart_date()) == 0) {
                        Notification notification = new Notification(c.getCampaignId(), null, applicationStartDate, NotificationType.EDIT);
                        c.setStatusAndNotify(notification, CampaignStatusType.STARTED);
                    }

                    if (applicationStartDate.isAfter(c.getEnd_date()) || applicationStartDate.compareTo(c.getEnd_date()) == 0) {
                        Notification notification = new Notification(c.getCampaignId(), null, applicationStartDate, NotificationType.EDIT);
                        c.setStatusAndNotify(notification, CampaignStatusType.EXPIRED);
                    }
                }
            }
        }
    }

    public void cancelCampaign(Integer id) {
        for (Campaign c : campaigns) {
            if (c.getCampaignId() == id) {
                if (c.getStatus().equals(CampaignStatusType.NEW) || c.getStatus().equals(CampaignStatusType.STARTED)) {
                    Notification notification = new Notification(c.getCampaignId(), null, applicationStartDate, NotificationType.CANCEL);
                    c.setStatusAndNotify(notification, CampaignStatusType.CANCELED);
                }
            }
        }
    }

    public void addCampaign(Campaign campaign) {
        if (applicationStartDate.isBefore(campaign.getStart_date())) {
            campaign.setStatus(CampaignStatusType.NEW);
        }

        if (applicationStartDate.isBefore(campaign.getEnd_date()) && applicationStartDate.isAfter(campaign.getStart_date())
                || applicationStartDate.compareTo(campaign.getStart_date()) == 0) {
            campaign.setStatus(CampaignStatusType.STARTED);
        }

        if (applicationStartDate.isAfter(campaign.getEnd_date()) || applicationStartDate.compareTo(campaign.getEnd_date()) == 0) {
            campaign.setStatus(CampaignStatusType.EXPIRED);
        }

        campaigns.add(campaign);
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        users.add(user);
    }

}
