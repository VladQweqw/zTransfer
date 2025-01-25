package main.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

import com.formdev.flatlaf.IntelliJTheme;
import main.com.pages.Discover;
import main.com.pages.Home;
import main.com.pages.Login;

public class CreateUI {
    private static JFrame frame = new JFrame("zFtransfer");
    private CardLayout cardLayout;
    private JPanel extendedMenu = new JPanel();
    private Boolean isOpen = false;
    private JPanel mainPanel;
    private final Integer width = 525;
    private final Integer heigth = 700;

    public static void loadResources() {
        try {
            ImageIcon icon = new ImageIcon("src/main.resources/images/logo.png");
            frame.setIconImage(icon.getImage());
        }catch(Exception ex) {
            System.out.println("Cannot set app icon");
        }

        try {
            InputStream mediumStream = CreateUI.class.getResourceAsStream("/fonts/Poppins-Medium.ttf");
            Font poppinsMedium = Font.createFont(Font.TRUETYPE_FONT, mediumStream).deriveFont(14f);

            UIManager.put("defaultFont", poppinsMedium);
            UIManager.put("Label.font", poppinsMedium);
            UIManager.put("Button.font", poppinsMedium);

        } catch (Exception e) {
            System.err.println("Failed to load custom font. Using default font.");
            e.printStackTrace();
        }

        try {
            IntelliJTheme.setup(
                    CreateUI.class.getResourceAsStream("/themes/default.theme.json")
            );
        }catch (Exception e) {
            System.out.println("Failed to load theme");
        }
    }

    public JPanel Header() {
        JPanel panel = new JPanel();

        ImageIcon icon = new ImageIcon(CreateUI.class.getResource("/images/hamburger.png"));
        JLabel menu = new JLabel(icon);

        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isOpen = !isOpen;
                extendedMenu.setVisible(isOpen);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                menu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menu.setCursor(Cursor.getDefaultCursor());
            }
        });

        JButton home_btn = new JButton("Home");
        JButton discover_btn = new JButton("Discover");
        JButton account_btn = new JButton("Account");

        account_btn.addActionListener(e -> {
            cardLayout.show(this.mainPanel, "account");
            account_btn.setBackground(new Color(0x723F90));

            home_btn.setBackground(new Color(0x45405C));
            discover_btn.setBackground(new Color(0x45405C));
        });

        home_btn.addActionListener(e -> {
            cardLayout.show(this.mainPanel, "home");

            home_btn.setBackground(new Color(0x723F90));

            discover_btn.setBackground(new Color(0x45405C));
            account_btn.setBackground(new Color(0x45405C));
        });

        discover_btn.addActionListener(e -> {
            cardLayout.show(this.mainPanel, "discover");

            discover_btn.setBackground(new Color(0x723F90));
            home_btn.setBackground(new Color(0x45405C));
            account_btn.setBackground(new Color(0x45405C));
        });

        extendedMenu.add(home_btn);
        extendedMenu.add(discover_btn);
        extendedMenu.setVisible(true);
        extendedMenu.setLayout(null);
        extendedMenu.setPreferredSize(new Dimension(200, 200));
        extendedMenu.setBounds(150, 0,200,200);

//        panel.add(extendedMenu);

//        panel.add(menu);
        panel.add(home_btn);
        panel.add(discover_btn);
        panel.add(account_btn);
        panel.setPreferredSize(new Dimension(width, 75));
        return panel;
    }

    public CreateUI() {
        loadResources();
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);

        Home home = new Home(width, heigth, frame);
        Discover discover = new Discover(width, heigth, frame);
        Login login = new Login(width, heigth, frame);

        mainPanel.add(home.getPanel(), "home");
        mainPanel.add(discover.getPanel(), "discover");
        mainPanel.add(login.getPanel(), "account");

        frame.setLayout(new BorderLayout());
        frame.add(Header(), BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, heigth);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

}
