package main.com;

import javax.swing.*;
import java.awt.*;

public class Wrapper {
    private final Integer width;
    private final Integer heigth;

    public Wrapper(Integer width, Integer heigth) {
        this.width = width;
        this.heigth = heigth;
    }

    public JPanel Wrap(JComponent x, JComponent y, Integer height) {
        JPanel wrapper = new JPanel();
        wrapper.add(x);
        wrapper.add(y);

        wrapper.setPreferredSize(new Dimension(width, height));
        return wrapper;
    }

    public JPanel Wrap(JComponent x, JComponent y) {
        JPanel wrapper = new JPanel();
        wrapper.add(x);
        wrapper.add(y);

        wrapper.setPreferredSize(new Dimension(width, 60));
        return wrapper;
    }
}
