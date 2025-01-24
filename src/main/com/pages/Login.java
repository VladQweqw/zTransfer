package main.com.pages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.com.API;
import main.com.Types.UserType;
import main.com.UserDetails;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Login extends Page {
    private JPanel panel = new JPanel();
    private JFrame frame;
    private final Integer width;
    private final Integer heigth;
    private API api = new API<UserType>("http://192.168.1.69:3003/users");
    // components
    private JScrollPane files_panel_el = new JScrollPane();

    private JTextField username_field = new JTextField("Username");
    private JPasswordField password_field = new JPasswordField("Password");;
    private JLabel username_el = new JLabel("Not logged in");

    private JTextField username_field() {
        username_field.setPreferredSize(new Dimension(width, 40));

        username_field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(username_field.getText().equals("Username")) {
                    username_field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(username_field.getText().isEmpty()) {
                    username_field.setText("Username");
                }
            }
        });

        return username_field;
    }

    private JTextField password_field() {
        password_field.setPreferredSize(new Dimension(width, 40));

        password_field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(password_field.getText().equals("Password")) {
                    password_field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(password_field.getText().isEmpty()) {
                    password_field.setText("Password");
                }
            }
        });

        return password_field;
    }

    private JButton submitBtn() {
        JButton login = new JButton("Log in / Register");

        login.addActionListener(e -> {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username_field.getText());
            credentials.put("password", password_field.getText());

            Gson gson = new Gson();
            Type userType = new TypeToken<UserType>() {}.getType();
            UserType user = (UserType) api.POST(gson.toJson(credentials), userType);

            try {
                UserDetails.setUser(user);
                if(UserDetails.getId() == null) throw new Exception("");
                this.username_el.setText("User: " + UserDetails.getUsername());
            }catch(Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Wrong Password",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        });

        return login;
    }

    private JButton logoutBtn() {
        JButton login = new JButton("Logout");

        login.addActionListener(e -> {
            UserDetails.logout();
            username_el.setText("Not logged in");

        });

        return login;
    }

    public Login(Integer width, Integer height, JFrame frame) {
        this.width = width - 100;
        this.heigth = height;
        this.frame = frame;

        JLabel title = new JLabel("Account");
        title.setPreferredSize(new Dimension(width - 20, 30));
        title.setHorizontalAlignment(SwingUtilities.CENTER);
        panel.add(title);

        username_el.setPreferredSize(new Dimension(width - 20, 30));
        username_el.setHorizontalAlignment(SwingUtilities.CENTER);
        panel.add(username_el);

        panel.add(username_field());
        panel.add(password_field());
        panel.add(submitBtn());
        panel.add(logoutBtn());
    }

    public JPanel getPanel() {
        return panel;
    }
}
