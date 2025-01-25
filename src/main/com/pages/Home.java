package main.com.pages;

import com.google.gson.reflect.TypeToken;
import main.com.API;
import main.com.Types.RoomType;
import main.com.Types.RoomTypeUID;
import main.com.UserDetails;
import main.com.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class Home extends Page {
    private JPanel panel = new JPanel();
    private JFrame frame;
    private String file_path = "";
    private final Integer width;
    private final Integer heigth;
    private Wrapper wrapper;
    private List<RoomTypeUID> rooms = null;
    private RoomTypeUID selectedRoom;

    private API api = new API("http://192.168.1.69:3003/users/");

    // components
    private JButton enter_room_el;
    private DefaultComboBoxModel<RoomTypeUID> savedRoomsModel = new DefaultComboBoxModel<>();
    private JComboBox savedRoomsCombo = new JComboBox(savedRoomsModel);
    private JButton file_explorer_el;
    private JButton add_file_el;
    private JLabel room_id_el = new JLabel("Room id: ");
    private JButton refresh_room_el;
    private JScrollPane files_panel_el;

    private JButton enterRoomBtn() {
        enter_room_el = new JButton("Enter room");
        enter_room_el.setPreferredSize(new Dimension((width / 2) - 20, 40));

        enter_room_el.addActionListener(e -> {
            UpdateRoomID();

        });

        return enter_room_el;
    }
    private void createSavedRooms() {
        if(rooms == null) return;

        DefaultComboBoxModel<RoomTypeUID> model = new DefaultComboBoxModel<>();
        model.addAll(rooms);

        JComboBox elem = new JComboBox<>(model);
        elem.setPreferredSize(new Dimension((width / 2) - 20, 40));
        elem.setSelectedIndex(0);

        elem.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof RoomTypeUID) {
                    RoomTypeUID room = (RoomTypeUID) value;
                    this.setText(room.getName());
                }

                return this;
            }
        });

        this.savedRoomsCombo = elem;
    }
    private JButton fileExplorerBtn() {
        file_explorer_el = new JButton("Select file");
        file_explorer_el.setPreferredSize(new Dimension((width / 2) - 20, 40));

        file_explorer_el.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();

            int res = chooser.showOpenDialog(panel);

            if(res == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                this.file_path = selectedFile.getAbsolutePath();
            }
        });

        return file_explorer_el;
    }
    public JButton addFileBtn() {
        add_file_el = new JButton("Add");
        add_file_el.setPreferredSize(new Dimension((width / 2) - 20, 40));


        add_file_el.addActionListener(e -> {
            System.out.println(this.file_path);
        });

        return add_file_el;
    }
    public JPanel createFile() {
        JPanel file = new JPanel();
        file.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        file.setPreferredSize(new Dimension(width - 20, 60));

        JLabel file_name = new JLabel("Poze_dji.rar");
        file_name.setPreferredSize(new Dimension(150, 20));

        JLabel file_size = new JLabel("23MB - 5h left");
        file_size.setSize(75, 30);

        JPanel file_details = new JPanel();
        file_details.setLayout(new GridLayout(2, 1, 10, 10));
        file_details.setPreferredSize(new Dimension(width - 100, 50));

        file_details.add(file_name);
        file_details.add(file_size);

        ImageIcon download_image = new ImageIcon(Home.class.getResource("/images/download.png"));
        JLabel download_btn = new JLabel(download_image);
        download_btn.setPreferredSize(new Dimension(50, 50));

        download_btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
    public JScrollPane filesPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new GridLayout(6,1, 15, 10));

        wrapper.add(createFile());
        wrapper.add(createFile());
        wrapper.add(createFile());
        wrapper.add(createFile());
        wrapper.add(createFile());
        wrapper.add(createFile());

        files_panel_el = new JScrollPane(wrapper);
        files_panel_el.setPreferredSize(new Dimension(width, 325));
        files_panel_el.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return files_panel_el;
    }

    // UI update
    public JButton refreshRoomBtn() {
        refresh_room_el = new JButton("Refresh");

        refresh_room_el.addActionListener(e -> {
            updateSavedRooms();
        });

        return refresh_room_el;
    }
    private void updateSavedRooms() {
        getRooms();
        savedRoomsModel.removeAllElements();
        savedRoomsModel.addAll(this.rooms);

        savedRoomsCombo.addActionListener(e -> {
            RoomTypeUID room = (RoomTypeUID) savedRoomsCombo.getSelectedItem();
            this.selectedRoom = room;
        });
    }

    //
    public void UpdateRoomID() {
        this.room_id_el.setText("Room id: " + selectedRoom.get_id());
    }

    // API
    public void getRooms() {
        try {
            Type listType = new TypeToken<List<RoomTypeUID>>() {}.getType();
            List<RoomTypeUID> rooms = (List<RoomTypeUID>) api.GET(UserDetails.getId() + "/rooms", listType);
            this.rooms = rooms;
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Home(Integer width, Integer height, JFrame frame) {
        this.width = width - 50;
        this.heigth = height;
        this.wrapper = new Wrapper(this.width, this.heigth);
        this.frame = frame;

        getRooms();
        createSavedRooms();

        panel.add(
                wrapper.Wrap(enterRoomBtn(), savedRoomsCombo)
        );

        room_id_el.setPreferredSize(new Dimension(350, 40));
        panel.add(
                wrapper.Wrap(room_id_el, refreshRoomBtn(), 45)
        );


        panel.add(filesPanel());

        panel.add(
                wrapper.Wrap(fileExplorerBtn(), addFileBtn())
        );

    }

    public JPanel getPanel() {
        return panel;
    }
}
