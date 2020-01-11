import jdk.swing.interop.SwingInterOpUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

public class MainPage extends JFrame implements ActionListener {
    JLabel frameLabel;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;
    JButton loadDataButton;
    JFrame frame;
    VMS management;

    public MainPage(String title) {
        management = null;
        frame = new JFrame("Login Form");
        frameLabel = new JLabel("Login Form");
        frameLabel.setForeground(Color.blue);
        frameLabel.setFont(new Font("Serif", Font.BOLD, 20));

        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        loadDataButton = new JButton("Load");

        frameLabel.setBounds(200, 30, 400, 30);
        usernameLabel.setBounds(80, 70, 200, 30);
        passwordLabel.setBounds(80, 110, 200, 30);
        usernameField.setBounds(300, 70, 200, 30);
        passwordField.setBounds(300, 110, 200, 30);
        loginButton.setBounds(150, 160, 100, 30);
        loadDataButton.setBounds(300, 160, 100, 30);

        frame.add(frameLabel);
        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(loadDataButton);

        loginButton.addActionListener(this);
        loadDataButton.addActionListener(this);

        frame.setSize(600, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String uname = usernameField.getText();
            String pass = passwordField.getText();

            boolean loginSucceed = false;

            for (User u : management.getUsers()) {
                if (uname.equals(u.getEmail()) && pass.equals(u.getPassword())) {
                    usernameField.setText(null);
                    passwordField.setText(null);
                    loginSucceed = true;

                    if (u.getType().equals(UserType.ADMIN)) {
                        AdminSelectionPage adminPage = AdminSelectionPage.getInstance("Admin Selection Page", management);
                        adminPage.show();
                    }

                    if (u.getType().equals(UserType.GUEST)) {
                        UserSelectionPage userPage = new UserSelectionPage("User Selection Page", management, u);
                        userPage.show();
                    }
                }
            }
            if (loginSucceed == false) {
                usernameField.setText(null);
                passwordField.setText(null);
                JOptionPane.showMessageDialog(this, "Incorrect login or password",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == loadDataButton) {
            Test test = new Test();
            try {
                management = test.loadFiles("/home/alinavirtan/Desktop/tema_POO/VMStests/test09/input/users.txt",
                        "/home/alinavirtan/Desktop/tema_POO/VMStests/test09/input/campaigns.txt");
                management.setApplicationStartDate(LocalDateTime.now().withNano(0).withSecond(0));
            } catch(Exception except) {
                except.printStackTrace();
            }
        }
    }
}
