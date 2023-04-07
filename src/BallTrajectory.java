
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BallTrajectory extends JPanel {
    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int OVAL_SIZE = 4;
    private static final double dt = 0.001;
    private static final double G = 9.8;
    private static double v0 = 15;
    private static  double alpha0 = 60;
    private static JFrame frame = new JFrame("Ball Trajectory");
    private static boolean check=true;
    private static boolean check2=true;
    private static boolean check3=true;
    private static boolean check4=true;
    public static double m=1;
    public static double R=1;
    public static double raw=1.225;//צפיפות אוויר
    public static double C=0.005 ;//מקדם גרר אוויר
    public static double k=0.5*raw*C*Math.PI*Math.pow(R,2);
    private static double K = k/m;




    private ArrayList<Point> points;
    private ArrayList<Point> pointNF;


    public BallTrajectory() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        points = new ArrayList<>();
        pointNF = new ArrayList<>();
    }

    /**
     * Calculates the trajectory of the ball using Euler's standard approximation
     * taking into account air friction.
     */
    public void calculateTrajectory() {
        points.clear();
        pointNF.clear();
        double vx = v0 * Math.cos(Math.toRadians(alpha0));
        double vy = v0 * Math.sin(Math.toRadians(alpha0));

        double vxNF = v0 * Math.cos(Math.toRadians(alpha0));
        double vyNF = v0 * Math.sin(Math.toRadians(alpha0));

        double x = 0.0;
        double y = 0.0;

        double xNF = 0.0;
        double yNF = 0.0;

        while (yNF >= 0)
        {
            vyNF += -G*dt;
            xNF+= vxNF*dt;
            yNF+= vyNF*dt;

            pointNF.add(new Point(xNF,yNF));
        }

        while (y >= 0.0)
        {
            double v = Math.sqrt(vx * vx + vy * vy);
            double f = K * v * v;
            double ax = -f * vx / v;
            double ay = -G - f * vy / v;

            vx += ax * dt;
            vy += ay * dt;

            x += vx * dt;
            y += vy * dt;

            points.add(new Point(x, y));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        int x0 = WIDTH / 8;
        int y0 = (int) (0.75 * HEIGHT);
        int gridSize = 50;
        Graphics2D g2d = (Graphics2D) g;

// Draw scale line
        g2d.setColor(Color.RED);
        int x1 = 100;
        int x2 = (int) (x1 + v0 * 10);
        int y1 = 100;
        g2d.drawLine(x1, y1, x2, y1);



// Create a JSlider to adjust the value of v0
            JSlider slider = new JSlider(0, 25, (int) v0);
            slider.setMajorTickSpacing(10);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setBounds(WIDTH / 2, 10, 250, 50); // updated bounds
            if (check) {
                slider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        v0 = slider.getValue();
                        calculateTrajectory(); // recalculate the trajectory with the new v0 value
                        repaint();
                    }
                });
                add(slider);
                check = false;
            }
            JSlider seekbar = new JSlider(0, 90, (int) alpha0);
            seekbar.setMajorTickSpacing(10);
            seekbar.setMinorTickSpacing(1);
            seekbar.setPaintTicks(true);
            seekbar.setPaintLabels(true);
            seekbar.setBounds(WIDTH / 2+350, 10, 250, 50); // updated bounds
            if (check2) {
                seekbar.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        alpha0 = (double) seekbar.getValue();
                        calculateTrajectory(); // recalculate the trajectory with the new v0 value
                        repaint();
                    }
                });
                add(seekbar);
                check2 = false;
            }
            JSlider seekbar2 = new JSlider(1, 25, (int) m);
            seekbar2.setMajorTickSpacing(4);
            seekbar2.setMinorTickSpacing(1);
            seekbar2.setPaintTicks(true);
            seekbar2.setPaintLabels(true);
            seekbar2.setBounds(WIDTH / 2-350, 10, 250, 50); // updated bounds
            if (check3) {
                seekbar2.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        m = (double) seekbar2.getValue();
                        K=k/m;
                        calculateTrajectory(); // recalculate the trajectory with the new v0 value
                        repaint();
                    }
                });
                add(seekbar2);
                check3 = false;
            }
            JSlider seekbar3 = new JSlider(0, 5, (int) R);
            seekbar3.setMajorTickSpacing(1);
            seekbar3.setMinorTickSpacing(1);
            seekbar3.setPaintTicks(true);
            seekbar3.setPaintLabels(true);
            seekbar3.setBounds(WIDTH/2 -700, 10, 250, 50); // updated bounds
            if (check4) {
                seekbar3.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        R = (double) seekbar3.getValue();
                        k=0.5*raw*C*Math.PI*Math.pow(R,2);
                        K=k/m;
                        calculateTrajectory(); // recalculate the trajectory with the new v0 value
                        repaint();
                    }
                });
                add(seekbar3);
                check4 = false;
            }


        {

            for (int i = x0; i <= WIDTH; i += gridSize) {
                g.setColor(Color.LIGHT_GRAY);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.drawLine(i, 0, i, HEIGHT);
            }
            for (int i = x0; i >= 0; i -= gridSize) {
                g.drawLine(i, 0, i, HEIGHT);
            }
            for (int i = y0; i <= HEIGHT; i += gridSize) {
                g.drawLine(0, i, WIDTH, i);
            }
            for (int i = y0; i >= 0; i -= gridSize) {
                g.drawLine(0, i, WIDTH, i);
            }
            g.setColor(Color.BLACK);
            g.drawLine(0, y0, WIDTH, y0);
            Font font = new Font("Arial", Font.BOLD, 30);
            g.setFont(font);
            g.drawString(" x (m)", WIDTH - 200, y0 + 40);
            g.drawString(" y (m)", x0 - 130, y0 - 650);
            g.drawLine(x0, 0, x0, HEIGHT);

            g.setColor(Color.BLUE);
            int lastX = x0;
            int lastY = y0;
            for (Point P : pointNF) {
                int x = (int) (x0 + P.x * gridSize);
                int y = (int) (y0 - P.y * gridSize);
                g.fillOval(x - OVAL_SIZE / 2, y - OVAL_SIZE / 2, OVAL_SIZE, OVAL_SIZE);
                g.drawLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
            }

            g.setColor(Color.RED);
            lastX = x0;
            lastY = y0;
            for (Point p : points) {
                int x = (int) (x0 + p.x * gridSize);
                int y = (int) (y0 - p.y * gridSize);
                g.fillOval(x - OVAL_SIZE / 2, y - OVAL_SIZE / 2, OVAL_SIZE, OVAL_SIZE);
                g.drawLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
            }
            int arrowSize = 12; // set the size of the arrow
            Graphics2D g2Dimention = (Graphics2D) g;
            g2Dimention.setStroke(new BasicStroke(5)); // set the thickness of the axis lines
            g2Dimention.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2Dimention.setColor(Color.BLACK);
            g2Dimention.drawLine(x0, y0, WIDTH, y0);
            g2Dimention.drawLine(x0, y0, x0, 0);

// Draw arrows on the x-axis
            g2Dimention.drawLine(WIDTH - 10, y0, WIDTH - arrowSize - 10, y0 - arrowSize);
            g2Dimention.drawLine(WIDTH - 10, y0, WIDTH - arrowSize - 10, y0 + arrowSize);

// Draw arrows on the y-axis
            g2Dimention.drawLine(x0, 2, x0 - arrowSize, arrowSize + 2);
            g2Dimention.drawLine(x0, 2, x0 + arrowSize, arrowSize + 2);
            g.setColor(Color.GRAY);


            // Draw x-axis coordinates
            Font font2 = new Font("Arial", Font.BOLD, 16);
            g2d.setFont(font2);
            g2d.setColor(Color.BLACK);
            for (int i = 0; i <= WIDTH / gridSize; i++) {
                String label = Integer.toString(i);
                int labelWidth = g2d.getFontMetrics().stringWidth(label);
                g2d.drawString(label, x0 + i * gridSize - labelWidth / 2, y0 + 20);
                g2d.drawLine(x0 + i * gridSize, y0 - 5, x0 + i * gridSize, y0 + 5);
            }

// Draw y-axis coordinates
            for (int i = 0; i <= HEIGHT / gridSize; i++) {
                String label = Integer.toString(i);
                int labelWidth = g2d.getFontMetrics().stringWidth(label);
                g2d.drawString(label, x0 - 25 - labelWidth, y0 - i * gridSize + 5);
                g2d.drawLine(x0 - 5, y0 - i * gridSize, x0 + 5, y0 - i * gridSize);
            }
        }

        Font font3 = new Font("Arial", Font.BOLD, 24);
        g.setFont(font3);
        g.drawString("R", (seekbar.getX() + seekbar.getWidth() / 2 - g.getFontMetrics().stringWidth("alpha") / 2)-130 , seekbar.getY() + seekbar.getHeight() + 20);
        g.drawString("m", (slider.getX() + slider.getWidth() / 2 - g.getFontMetrics().stringWidth("V0") / 2)-20, slider.getY() + slider.getHeight() + 20);
        g.drawString("alpha", (seekbar2.getX() + seekbar2.getWidth() / 2 - g.getFontMetrics().stringWidth("m") / 2)+80, seekbar2.getY() + seekbar2.getHeight() + 20);
        g.drawString("V0", (seekbar3.getX() + seekbar3.getWidth() / 2 - g.getFontMetrics().stringWidth("R") / 2)+245, seekbar3.getY() + seekbar3.getHeight() + 20);

        Font font = new Font("Arial", Font.BOLD, 30);
        g.setFont(font);
        g.setColor(Color.BLUE);
        g.drawString("בכחול מסלול התנועה ללא חיכוך (מסלול פרבולי)", (WIDTH / 2)-200, y0+60);
        g.setColor(Color.RED);
        g.drawString("באדום מסלול התנועה עם חיכוך (מסלול בליסטי)", (WIDTH / 2)-200, y0+100);
    }

    private class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Ball Trajectory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1, 0, 0));

        BallTrajectory panel = new BallTrajectory();
        panel.calculateTrajectory();
        frame.add(panel);

        frame.pack();
        frame.setVisible(true);
    }
}

