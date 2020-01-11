import java.io.*;
import java.text.CollationElementIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {
    public VMS loadFiles(String filePath1, String filePath2) throws Exception {
            int nrUsers, nrCampaigns;
            LocalDateTime applicationDate;
            List<User>  users = new ArrayList<>();
            List<Campaign> campaigns = new ArrayList<>();
            File file1, file2;
            BufferedReader br1, br2;
            String st1, st2;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // read data about users from a file and put the users in a list of users
            file1 = new File(filePath1);
            br1 = new BufferedReader(new FileReader(file1));
            st1 = br1.readLine();
            nrUsers = Integer.parseInt(st1); // number of users
            while ((st1 = br1.readLine()) != null) {
                String[] infoUsers = st1.split(";", 5);
                if (infoUsers[4].equals("GUEST")) {
                    User user = new User(Integer.parseInt(infoUsers[0]), infoUsers[1], infoUsers[2], infoUsers[3],
                                        UserType.GUEST);
                    users.add(user);
                } else {
                    User user = new User(Integer.parseInt(infoUsers[0]), infoUsers[1], infoUsers[2], infoUsers[3],
                                        UserType.ADMIN);
                    users.add(user);
                }
            }

            // read data about campaigns from a file and put the campaigns in a list of campaigns
            file2 = new File(filePath2);
            br2 = new BufferedReader(new FileReader(file2));

            st2 = br2.readLine();
            nrCampaigns = Integer.parseInt(st2);

            st2 = br2.readLine();
            applicationDate = LocalDateTime.parse(st2, formatter);

            while ((st2 = br2.readLine()) != null) {
                String[] infoCampaigns = st2.split(";", 7);

                LocalDateTime startDate = LocalDateTime.parse(infoCampaigns[3], formatter);
                LocalDateTime endDate = LocalDateTime.parse(infoCampaigns[4], formatter);


                if (infoCampaigns[6].equals("A")) {
                    Campaign campaign = new Campaign(Integer.parseInt(infoCampaigns[0]), infoCampaigns[1], infoCampaigns[2],
                            startDate, endDate, Integer.parseInt(infoCampaigns[5]), new StrategyA());
                    campaigns.add(campaign);
                }

                if (infoCampaigns[6].equals("B")) {
                    Campaign campaign = new Campaign(Integer.parseInt(infoCampaigns[0]), infoCampaigns[1], infoCampaigns[2],
                            startDate, endDate, Integer.parseInt(infoCampaigns[5]), new StrategyB());
                    campaigns.add(campaign);
                }

                if (infoCampaigns[6].equals("C")) {
                    Campaign campaign = new Campaign(Integer.parseInt(infoCampaigns[0]), infoCampaigns[1], infoCampaigns[2],
                            startDate, endDate, Integer.parseInt(infoCampaigns[5]), new StrategyC());
                    campaigns.add(campaign);
                }
            }

            VMS management = VMS.getInstance(campaigns, users, applicationDate);
            return management;
    }

    public static void main(String[] args) throws Exception {
            int nrEvents, displayedEvents = 0;
            LocalDateTime applicationDate;
            File file3;
            BufferedReader br3;
            String st3;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            Test test = new Test();

            File fileOutput = new File("/home/alinavirtan/Desktop/tema_POO/output_test.txt");
            fileOutput.createNewFile();
            FileWriter writer = new FileWriter(fileOutput);

            VMS management = test.loadFiles("/home/alinavirtan/Desktop/tema_POO/VMStests/test09/input/users.txt",
                    "/home/alinavirtan/Desktop/tema_POO/VMStests/test09/input/campaigns.txt");

            // read data about events and perform the events
            file3 = new File("/home/alinavirtan/Desktop/tema_POO/VMStests/test09/input/events.txt");
            br3 = new BufferedReader(new FileReader(file3));

            st3 = br3.readLine();
            applicationDate = LocalDateTime.parse(st3, formatter);

            st3 = br3.readLine();
            nrEvents = Integer.parseInt(st3);

            while ((st3 = br3.readLine()) != null) {

                String[] info = st3.split(";", 10);

                // **** ADD CAMPAIGN ****
                if (info[1].equals("addCampaign")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    if (user.getType().equals(UserType.ADMIN)) {
                        Strategy strategy;
                        if (info[8].equals("A")) {
                            strategy = new StrategyA();
                        } else {
                            if (info[8].equals("B")) {
                                strategy = new StrategyB();
                            } else {
                                strategy = new StrategyC();
                            }
                        }

                        LocalDateTime startDate = LocalDateTime.parse(info[5], formatter);
                        LocalDateTime endDate = LocalDateTime.parse(info[6], formatter);

                        Campaign c = new Campaign(Integer.parseInt(info[2]),
                                info[3], info[4], startDate, endDate,
                                Integer.parseInt(info[7]), strategy);
                        management.addCampaign(c);
                    }
                }


                // **** EDIT CAMPAIGN ****
                // manage the editCampaign event
                if (info[1].equals("editCampaign")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    if (user.getType().equals(UserType.ADMIN)) {
                        LocalDateTime startDate = LocalDateTime.parse(info[5], formatter);
                        LocalDateTime endDate = LocalDateTime.parse(info[6], formatter);
                        Campaign newCampaign = new Campaign(Integer.parseInt(info[2]),
                                info[3], info[4], startDate, endDate, Integer.parseInt(info[7]), null);
                        management.updateCampaign(Integer.parseInt(info[2]), newCampaign);
                    }
                }


                // **** CANCEL CAMPAIGN ****
                // cancel the campaign with the provided campaignId
                if (info[1].equals("cancelCampaign")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    if (user.getType().equals(UserType.ADMIN)) {
                        management.cancelCampaign(Integer.parseInt(info[2]));
                    }
                }


                // **** GENERATE VOUCHER ****
                // userId;generateVoucher;campaignId;email;voucherType;value –genereaza un voucheravand detaliile specificate
                // si il distribuie utilizatorului avand adresa de e-mail precizata, daca utilizatorul este administrator
                if (info[1].equals("generateVoucher")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    Voucher voucher;

                    if (user.getType().equals(UserType.ADMIN)) {
                        Campaign c = management.getCampaign(Integer.parseInt(info[2]));
                        voucher = c.generateVoucher(info[3], info[4], Float.parseFloat(info[5]));

                        if (voucher != null) {
                            // the user who will receive the  voucher will become an observer for the company
                            // that generates that voucher; also, the voucher will be added to the user's map
                            for (User receiver : management.getUsers()) {
                                if (receiver.getEmail().equals(voucher.getEmail())) {
                                    management.getCampaign(voucher.getCampaignId()).addObserver(receiver);
                                    receiver.getUserMap().addVoucher(voucher);
                                }
                            }
                        }
                    }
                }


                // **** REDEEM VOUCHER ****
                // mark as used the voucher with id = info[3] from the campaign with id = info[2]
                if (info[1].equals("redeemVoucher")) {
                    // transform the string in a LocalDateTime type
                    LocalDateTime redeemDate = LocalDateTime.parse(info[4], formatter);

                    // get the user specified by the userId
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);

                    // redeem the voucher only if the user is an ADMIN
                    if (user.getType().equals(UserType.ADMIN)) {
                        Campaign c = management.getCampaign(Integer.parseInt(info[2]));
                        for (Map.Entry<String, Vector<Voucher>> entry : c.getCampaignMap().entrySet()) {
                            Vector<Voucher> v = entry.getValue();
                            for (Voucher voucher : v) {
                                if (voucher.getId() == Integer.parseInt(info[3])) {
                                    c.redeemVoucher(voucher.getCode(), redeemDate);
                                    break;
                                }
                            }
                        }
                    }
                }


                // **** GET VOUCHERS****
                // get the vouchers of the user with id = info[0] only if the user is a GUEST
                if (info[1].equals("getVouchers")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    if (user.getType().equals(UserType.GUEST)) {
                        String s = "[";
                        ArrayMap<Integer, Vector<Voucher>> userMap = user.getUserMap();
                        for (Map.Entry<Integer, Vector<Voucher>> entry : userMap.entrySet()) {
                            Vector<Voucher> vect = entry.getValue();
                            for (Voucher v : vect) {
                                    s = s + v.toString() + ", ";
                            }
                        }
                        if (s.length() > 1) {
                            s = s.subSequence(0, s.length() - 2) + "]";
                        } else {
                            s = s + "]";
                        }
                        //System.out.println(s);
                        writer.write(s);
                        writer.write("\n");
                        writer.flush();
                    }
                }


                // **** GET OBSERVERS ****
                if (info[1].equals("getObservers")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    if (user.getType().equals(UserType.ADMIN)) {
                        Campaign c = management.getCampaign(Integer.parseInt(info[2]));
                        String s = "[";
                        for (User u : c.getObservers()) {
                            s = s + u.toString();
                        }
                        s = s + "]";
                        //System.out.println(s);
                        writer.write(s);
                        writer.write("\n");
                        writer.flush();
                    }
                }


                // **** GET NOTIFICATIONS ****
                if (info[1].equals("getNotifications")) {
                    User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                    if (user.getType().equals(UserType.GUEST)) {
                        //System.out.println(user.getNotifications());
                        writer.write(user.getNotifications());
                        writer.write("\n");
                        writer.flush();
                    }
                }

                // **** GET VOUCHER *****
                if (info[1].equals("getVoucher")) {
                    if (info[1].equals("getVoucher")) {
                        User user = management.getUsers().get(Integer.parseInt(info[0]) - 1);
                        if (user.getType().equals(UserType.ADMIN)) {
                            Campaign c = management.getCampaign(Integer.parseInt(info[2]));
                            Voucher voucher = c.executeStrategy();

                            if (voucher != null) {
                                // the user who will receive the  voucher will become an observer for the company
                                // that generates that voucher; also, the voucher will be added to the user's map
                                for (User receiver : management.getUsers()) {
                                    if (receiver.getEmail().equals(voucher.getEmail())) {
                                        management.getCampaign(voucher.getCampaignId()).addObserver(receiver);
                                        receiver.getUserMap().addVoucher(voucher);
                                    }
                                }

                                // print the voucher
                                //System.out.println(voucher);
                                writer.write(voucher.toString());
                                writer.write("\n");
                                writer.flush();
                            }
                        }
                    }
                }
            }
            writer.close();
    }
}

