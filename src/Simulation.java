import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Simulation extends JPanel {

    private static int initialX, initialY;
    private List<PointTime> lines = new LinkedList<>();

    private static class PointTime {
        int[] point;
        long timestamp;

        PointTime(int[] point, long timestamp) {
            this.point = point;
            this.timestamp = timestamp;
        }
    }

    static {
        initialX = (GUI.WIDTH - 150) / 2;
        initialY = GUI.HEIGHT / 2;
    }

    public int getMass1() {
        return mass1;
    }

    public void setMass1(int mass1) {
        this.mass1 = mass1;
    }

    public int getMass2() {
        return mass2;
    }

    public void setMass2(int mass2) {
        this.mass2 = mass2;
    }

    public int getRadius1() {
        return radius1;
    }

    public void setRadius1(int radius1) {
        this.radius1 = radius1;
    }

    public int getRadius2() {
        return radius2;
    }

    public void setRadius2(int radius2) {
        this.radius2 = radius2;
    }

    public void setDampen(boolean dampen) {
        this.dampen = dampen;
    }

    private boolean dampen = false;

    public void resetSimulation() {
        this.angle1 = Math.PI / 2;
        this.angle2 = Math.PI / 2;
        this.angularV1 = 0.0;
        this.angularV2 = 0.0;
        this.lines.clear();
    }

    private boolean trajectoryToggle = true;

    public void toggleTrajectory(boolean trajectoryToggle) {
        this.trajectoryToggle = trajectoryToggle;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);

        int x1 = (int) (radius1 * Math.sin(angle1) + initialX);
        int y1 = (int) (radius1 * Math.cos(angle1) + initialY);
        int x2 = (int) (radius2 * Math.sin(angle2) + x1);
        int y2 = (int) (radius2 * Math.cos(angle2) + y1);
        lines.add(new PointTime(new int[] { x2 - mass2 / 2, y2 - mass2 / 2 }, System.currentTimeMillis()));

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1));

        if (trajectoryToggle) {
            g2d.setColor(Color.WHITE);
            for (Iterator<PointTime> iter = lines.iterator(); iter.hasNext();) {
                PointTime pointTime = iter.next();
                int[] point = pointTime.point;
                long timestamp = pointTime.timestamp;
                if (System.currentTimeMillis() - timestamp > 5000) {
                    iter.remove();
                } else {
                    g2d.fillOval(point[0], point[1], 2, 2);
                }
            }
        }

        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(Color.BLUE);
        g2d.drawLine(initialX, initialY, x1 - mass1 / 2, y1 - mass1 / 2);
        g2d.drawLine(x1 - mass1 / 2, y1 - mass1 / 2,
                x2 - mass2 / 2, y2 - mass2 / 2);

        g2d.setColor(Color.GREEN);
        g2d.fillOval(x1 - mass1, y1 - mass1, mass1, mass1);

        g2d.setColor(Color.RED);
        g2d.fillOval(x2 - mass2, y2 - mass2, mass2, mass2);

        run();
    }

    private double gravity = 1;
    private int mass1 = 20, mass2 = 20;
    private double angle1 = Math.PI / 2, angle2 = Math.PI / 2;
    private double angularV1 = 0.0, angularV2 = 0.0;
    private int radius1 = 100, radius2 = 100;

    private double calcA1() {
        double x1 = -gravity * (2 * mass1 + mass2) * Math.sin(angle1);
        double x2 = -mass2 * gravity * Math.sin(angle1 - 2 * angle2);
        double x3 = -2 * Math.sin(angle1 - angle2) * mass2;
        double x4 = angularV2 * angularV2 * radius2
                + angularV1 * angularV1 * radius1 * Math.cos(angle1 - angle2);
        double den = radius1 * (2 * mass1 + mass2 - mass2 * Math.cos(2 * angle1 - 2 * angle2));
        return (x1 + x2 + x3 * x4) / den;
    }

    private double calcA2() {
        double x1 = 2 * Math.sin(angle1 - angle2);
        double x2 = (angularV1 * angularV1 * radius1 * (mass1 + mass2));
        double x3 = gravity * (mass1 + mass2) * Math.cos(angle1);
        double x4 = angularV2 * angularV2 * radius2 * mass2 * Math.cos(angle1 - angle2);
        double den = radius2 * (2 * mass1 + mass2 - mass2 * Math.cos(2 * angle1 - 2 * angle2));
        return (x1 * (x2 + x3 + x4)) / den;
    }

    private void run() {
        double angularA1 = calcA1();
        double angularA2 = calcA2();
        angularV1 += angularA1;
        angularV2 += angularA2;
        angle1 += angularV1;
        angle2 += angularV2;
        if (dampen) {
            double damp = 0.999;
            angularV1 *= damp;
            angularV2 *= damp;
        }
    }
}