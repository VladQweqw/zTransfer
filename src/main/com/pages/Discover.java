package main.com.pages;

import com.github.weisj.jsvg.nodes.Use;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.com.API;
import main.com.Types.RoomType;
import main.com.Types.UserType;
import main.com.UserDetails;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Discover extends Page {
    private JPanel panel = new JPanel();
    private final Integer width;
    private JFrame frame;
    private final Integer heigth;
    private List<RoomType> rooms;
    private API api = new API("http://192.168.1.69:3003/rooms");

    // components
    private JButton refresh_room_el;
    private JScrollPane files_panel_el = new JScrollPane();
    private JTextField room_name = new JTextField("Room name");
    private JTextField room_pass = new JTextField("Room password");
    public JButton refreshRoomBtn() {
        refresh_room_el = new JButton("Refresh");

        refresh_room_el.addActionListener(e -> {
            getRooms();

            if(files_panel_el != null) {
                files_panel_el.setViewportView(null);
            }

            JScrollPane newRoomsPanel = roomsPanel();
            files_panel_el.setViewportView(newRoomsPanel.getViewport().getView());

            this.files_panel_el.revalidate();
            this.files_panel_el.repaint();
        });

        return refresh_room_el;
    }

    private Boolean joinRoom(String room_id, String password) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("user_id", UserDetails.getId());
        credentials.put("password", password);

        Gson gson = new Gson();
        String room  = api.POST("/join/" + room_id, gson.toJson(credentials));
        String clean = room.substring(1, room.length() - 1);

        if(clean.equals("success")) {
            return true;
        }else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Wrong Password",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
    }

    public void passwordModal(String room_id) {
        JDialog dialog = new JDialog(frame, "Password", true);
        dialog.setSize(300, 160);
        dialog.setLayout(new FlowLayout());

        JLabel text = new JLabel("Enter password");
        JTextField text_field = new JTextField();
        text_field.setPreferredSize(new Dimension(250, 30));
        JButton submit = new JButton("Join");
        JButton cancel = new JButton("Cancel");

        submit.addActionListener(e -> {
            if(joinRoom(room_id, text_field.getText())) {
                dialog.dispose();
            }
        });

        cancel.addActionListener(e -> {
            dialog.dispose();
        });

        dialog.add(text);
        dialog.add(text_field);
        dialog.add(cancel);
        dialog.add(submit);

        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    public JPanel generateRoom(RoomType room) {
        JPanel file = new JPanel();
        file.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        file.setPreferredSize(new Dimension(width - 20, 60));

        JLabel file_name = new JLabel(room.getName());
        file_name.setPreferredSize(new Dimension(150, 20));

        String txt;
        Integer people_inside = room.getPeopleInside().size();
        if(people_inside == 0) {
            txt = "0 people - ";
        }else if(people_inside < 2) {
            txt = people_inside + " person - ";
        }else {
            txt = people_inside + " people - ";
        }

        JLabel file_size =
                new JLabel(txt + room.getAuthor().getUsername());
        file_size.setSize(75, 30);

        JPanel file_details = new JPanel();
        file_details.setLayout(new GridLayout(2, 1, 10, 10));
        file_details.setPreferredSize(new Dimension(width - 100, 40));

        file_details.add(file_name);
        file_details.add(file_size);

        ImageIcon download_image = new ImageIcon(Home.class.getResource("/images/join.png"));
        JLabel download_btn = new JLabel(download_image);
        download_btn.setPreferredSize(new Dimension(50, 50));

        download_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    UserDetails.getId();
                    passwordModal(room.get_id());

                }catch(Exception ex) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "You're not logged in",
                            "Warning",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                download_btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                download_btn.setCursor(Cursor.getDefaultCursor());
            }
        });

        file.add(file_details);
        file.add(download_btn);
        return file;
    }
    public JScrollPane roomsPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new GridLayout(Math.max(6, rooms.size()),1, 15, 10));

        for (RoomType room : rooms) {
            wrapper.add(generateRoom(room));
        }
        System.out.println("sal");

        this.files_panel_el = new JScrollPane(wrapper);
        this.files_panel_el.setPreferredSize(new Dimension(width, 325));
        this.files_panel_el.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return this.files_panel_el;
    }

    private void getRooms() {
        Type listType = new TypeToken<List<RoomType>>() {}.getType();
        List<RoomType> rooms = (List<RoomType>) api.GET(listType);

        this.rooms = rooms;
        roomsPanel();
    }


    private JPanel createRoom() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        wrapper.setPreferredSize(new Dimension(width, 100));

        room_name.setPreferredSize(new Dimension(200 , 40));
        room_pass.setPreferredSize(new Dimension(200, 40));

        JButton btn = new JButton("Create room");
        btn.addActionListener(e -> {
            try {
                Map<String, String> credentials = new HashMap<>();

                System.out.println(UserDetails.getId());
                if(UserDetails.getId() == null) throw new Exception("");

                credentials.put("name", room_name.getText());
                credentials.put("password", room_pass.getText());
                credentials.put("author", UserDetails.getId());

                Gson gson = new Gson();
                Type roomType = new TypeToken<RoomType>() {}.getType();
                RoomType room = (RoomType) api.POST(gson.toJson(credentials), roomType);
            }catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "You are not logged in",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        wrapper.add(room_name);
        wrapper.add(room_pass);
        wrapper.add(btn);

        return wrapper;
    }

    public Discover(Integer width, Integer height, JFrame frame) {
        this.width = width - 100;
        this.heigth = height;
        this.frame = frame;
        getRooms();

        JLabel title = new JLabel("All rooms");
        title.setPreferredSize(new Dimension(width - 20, 30));
        title.setHorizontalAlignment(SwingUtilities.CENTER);
        panel.add(title);
        panel.add(createRoom());
        panel.add(refreshRoomBtn());

        panel.add(files_panel_el);
    }

    public JPanel getPanel() {
        return panel;
    }
}
