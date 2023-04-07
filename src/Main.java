import javax.swing.*;
import java.awt.*;

public class Main
{

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Ball Trajectory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1, 0, 0));

        BallTrajectory panel = new BallTrajectory();
        panel.calculateTrajectory();
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}