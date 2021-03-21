package game;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window
{
    public Window (int width, int height, String title, Game game) {
        var dim = new Dimension(width, height);
        game.viewport().setPreferredSize(dim);

        JFrame frame = new JFrame (title);
        frame.add(game.viewport());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible (true);
    }
}
