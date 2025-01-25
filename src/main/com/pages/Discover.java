package main.com.pages;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.com.API;
import main.com.Types.RoomType;
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
    private API api = new API("rooms");

    // components
    private JButton refresh_room_el;
    private JScrollPane roomsPanelEl = new JScrollPane();
    private JTextField room_name = new JTextField("Room name");
    private JTextField room_pass = new JTextField("Room password");
    public JButton refreshRoomBtn() {
        refresh_room_el = new JButton("Refresh");

        refresh_room_el.addActionListener(e -> {
            getRooms();
            updateRooms();
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
        if(room == null) return null;

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

        ImageIcon join_img = new ImageIcon(Home.class.getResource("/images/join.png"));
        JLabel download_btn = new JLabel(join_img);
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
    public void CreateRoomPanel() {
        this.roomsPanelEl.setPreferredSize(new Dimension(width, 325));
        this.roomsPanelEl.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }
    private void getRooms() {
        Type listType = new TypeToken<List<RoomType>>() {}.getType();
        List<RoomType> rooms = (List<RoomType>) api.GET("", listType);

        this.rooms = rooms;
        updateRooms();
    }
    private JPanel createRoom() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        wrapper.setPreferredSize(new Dimension(width, 100));

        room_name.setPreferredSize(new Dimension(200 , 40));
        room_pass.setPreferredSize(new Dimension(200, 40));

        JButton btn = new JButton("Create room");
        btn.addActionListener(e -> {
            postRoom();
        });

        wrapper.add(room_name);
        wrapper.add(room_pass);
        wrapper.add(btn);

        return wrapper;
    }
    public void updateRooms() {
        JPanel wrapper = new JPanel();

        if(rooms != null) {
            wrapper.setLayout(new GridLayout(Math.max(6, rooms.size()), 1, 15, 10));

            if (rooms.size() > 0) {
                for (RoomType room : rooms) {
                    JPanel p = generateRoom(room);
                    p.revalidate();
                    p.repaint();

                    wrapper.add(p);
                }
            } else {
                System.out.println("No rooms");
            }
        }

        wrapper.revalidate();
        wrapper.repaint();

        roomsPanelEl.setViewportView(wrapper);
        roomsPanelEl.revalidate();
        roomsPanelEl.repaint();
    }

    // API
    public void postRoom() {
        try {
            Map<String, String> credentials = new HashMap<>();
            if(UserDetails.getId() == null) throw new Exception("");

            credentials.put("name", room_name.getText());
            credentials.put("password", room_pass.getText());
            credentials.put("author", UserDetails.getId());

            Gson gson = new Gson();
            Type roomType = new TypeToken<RoomType>() {}.getType();
            api.POST("", gson.toJson(credentials), roomType);

            updateRooms();
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "You are not logged in",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    public Discover(Integer width, Integer height, JFrame frame) {
        this.width = width - 100;
        this.heigth = height;
        this.frame = frame;

        panel.add(roomsPanelEl);
        JLabel title = new JLabel("All rooms");
        title.setPreferredSize(new Dimension(width - 20, 30));
        title.setHorizontalAlignment(SwingUtilities.CENTER);
        panel.add(title);
        panel.add(createRoom());
        panel.add(refreshRoomBtn());

        panel.add(roomsPanelEl);

        getRooms();
        CreateRoomPanel();
    }

    public JPanel getPanel() {
        return panel;
    }
}
