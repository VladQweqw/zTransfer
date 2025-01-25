package main.com.pages;

import com.google.gson.reflect.TypeToken;
import main.com.API;
import main.com.Types.FileType;

import main.com.Types.RoomTypeUID;
import main.com.UserDetails;
import main.com.Wrapper;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JLabel selectedFilePath = new JLabel();
    private JButton enter_room_el;
    private DefaultComboBoxModel<RoomTypeUID> savedRoomsModel = new DefaultComboBoxModel<>();
    private JComboBox savedRoomsCombo = new JComboBox(savedRoomsModel);

    private JScrollPane filesPanelEl = new JScrollPane();

    private JButton file_explorer_el;
    private JButton add_file_el;
    private JLabel room_id_el = new JLabel("Room id: ");
    private JButton refresh_room_el;

    private JButton enterRoomBtn() {
        enter_room_el = new JButton("Enter room");
        enter_room_el.setPreferredSize(new Dimension((width / 2) - 20, 40));

        enter_room_el.addActionListener(e -> {
            UpdateRoomID();
            UpdateFiles();
        });

        return enter_room_el;
    }
    private JButton leaveRoomByn() {
        enter_room_el = new JButton("Leave room");
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
                selectedFilePath.setText(this.file_path);
            }
        });

        return file_explorer_el;
    }
    public JButton addFileBtn() {
        add_file_el = new JButton("Add");
        add_file_el.setPreferredSize(new Dimension((width / 2) - 20, 40));


        add_file_el.addActionListener(e -> {
            uploadFile();
            UpdateFiles();
        });

        return add_file_el;
    }

    public JPanel createFile(FileType fileObj) {
        if(fileObj == null) return null;

        JPanel file = new JPanel();
        file.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        file.setPreferredSize(new Dimension(width - 20, 60));

        JLabel file_name = new JLabel(fileObj.getName());
        file_name.setPreferredSize(new Dimension(150, 20));

        JLabel file_size = new JLabel(fileObj.getSize() + " - " + fileObj.getCreatedAt());
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
                JFileChooser chooser = new JFileChooser();

                chooser.setSelectedFile(new File(fileObj.getName()));

                int res = chooser.showOpenDialog(panel);
                if(res == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();
                    String dir = selectedFile.getAbsolutePath();
                    downloadFile(fileObj.getUrl(), dir);
                }

                System.out.println(fileObj.getUrl());
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
    public void createFilesPanel() {
        filesPanelEl.setPreferredSize(new Dimension(width, 325));
        filesPanelEl.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void downloadFile(String path, String dir) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .build();

        try {
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Paths.get(dir)));

            if(response.statusCode() == 200) {
                System.out.println("Downloaded");
                System.out.println(response.body());
            }else {
                System.out.println("Failed");
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("err");
        }

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
    public void UpdateRoomID() {
        this.room_id_el.setText("Room id: " + selectedRoom.get_id());
    }

    public void UpdateFiles() {
        JPanel wrapper = new JPanel();
        if(rooms != null) {
            wrapper.setLayout(new GridLayout(Math.max(rooms.size(), 6),1, 15, 10));

            if(selectedRoom.getFiles() == null) {
                System.out.println("No files here");
            }else {
                for(FileType file : selectedRoom.getFiles()) {
                    JPanel p = createFile(file);
                    p.revalidate();
                    p.repaint();
                    wrapper.add(p);
                }
            }
        }

        wrapper.revalidate();
        wrapper.repaint();

        filesPanelEl.setViewportView(wrapper);
        filesPanelEl.revalidate();
        filesPanelEl.repaint();

    }

    // API
    public void getRooms() {
        Type listType = new TypeToken<List<RoomTypeUID>>() {}.getType();
        if(UserDetails.getId() != null) {
            List<RoomTypeUID> rooms = (List<RoomTypeUID>) api.GET(UserDetails.getId() + "/rooms", listType);
            this.rooms = rooms;

        }else {
            System.out.println("No user");
        }
    }

    public void uploadFile() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("author", UserDetails.getId());
        credentials.put("name", new File(this.file_path).getName());
        credentials.put("upload_file", this.file_path);

        Type fileType = new TypeToken<FileType>() {}.getType();
        FileType file = (FileType) api.POSTFile(
                "http://192.168.1.69:3003/files?room_id=" + selectedRoom.get_id(),
                fileType,
                credentials);

        System.out.println(file);

    }

    public Home(Integer width, Integer height, JFrame frame) {
        this.width = width - 50;
        this.heigth = height;
        this.wrapper = new Wrapper(this.width, this.heigth);
        this.frame = frame;

        getRooms();
        createSavedRooms();
        createFilesPanel();

        panel.add(savedRoomsCombo);
        panel.add(
                wrapper.Wrap(enterRoomBtn(), leaveRoomByn())
        );

        room_id_el.setPreferredSize(new Dimension(350, 40));
        panel.add(
                wrapper.Wrap(room_id_el, refreshRoomBtn(), 45)
        );


        panel.add(filesPanelEl);

        panel.add(
                wrapper.Wrap(fileExplorerBtn(), addFileBtn())
        );

        panel.add(selectedFilePath);

    }

    public JPanel getPanel() {
        return panel;
    }
}
