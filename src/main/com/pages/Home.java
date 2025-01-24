package main.com.pages;

import main.com.Wrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Home extends Page {
    private JPanel panel = new JPanel();
    private JFrame frame;

    private String file_path = "";
    private final Integer width;
    private final Integer heigth;
    private String room_id = "";
    private Wrapper wrapper;

    // components
    private JTextField room_id_field;
    private JButton enter_room_el;
    private JComboBox saved_rooms_el;
    private JButton file_explorer_el;
    private JButton add_file_el;
    private JLabel room_id_el = new JLabel("Room id: ");
    private JButton refresh_room_el;
    private JScrollPane files_panel_el;

    private JTextField roomIdField() {
        String default_text = "Room ID";

        room_id_field = new JTextField();
        room_id_field.setText(default_text);
        room_id_field.setPreferredSize(new Dimension(width, 40));

        room_id_field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(room_id_field.getText().equals(default_text)) {
                    room_id_field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(room_id_field.getText().isEmpty()) {
                    room_id_field.setText(default_text);
                }
            }
        });


        return room_id_field;
    }

    private JButton enterRoomBtn() {
        enter_room_el = new JButton("Enter room");
        enter_room_el.setPreferredSize(new Dimension((width / 2) - 20, 40));

        enter_room_el.addActionListener(e -> {
            this.room_id = room_id_field.getText();
            SetRoomId();
        });

        return enter_room_el;
    }
    private JComboBox savedRoomsBtn() {
        String[] items = {"a", "b", "C"};

        saved_rooms_el = new JComboBox<>(items);
        saved_rooms_el.setPreferredSize(new Dimension((width / 2) - 20, 40));
        saved_rooms_el.setSelectedIndex(0);

        saved_rooms_el.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) saved_rooms_el.getSelectedItem();
                room_id = selectedItem;
                SetRoomId();
            }
        });
        return saved_rooms_el;
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
    public JButton refreshRoomBtn() {
        refresh_room_el = new JButton("Refresh");

        refresh_room_el.addActionListener(e -> {
            System.out.println("refresh");
        });

        return refresh_room_el;
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

    //

    public void SetRoomId() {
        this.room_id_field.setText(room_id);
        this.room_id_el.setText("Room id: " + room_id);

    }

    public Home(Integer width, Integer height, JFrame frame) {
        this.width = width - 50;
        this.heigth = height;
        this.wrapper = new Wrapper(this.width, this.heigth);
        this.frame = frame;

        panel.add(roomIdField());
        panel.add(
                wrapper.Wrap(enterRoomBtn(), savedRoomsBtn())
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
