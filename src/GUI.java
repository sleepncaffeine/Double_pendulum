import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {

    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    private final Timer timer = new Timer(20, this);

    void createAndShowGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(new BorderLayout());
        this.setTitle("Double Pendulum Simulation");

        Simulation panel = new Simulation();
        this.add(panel, BorderLayout.CENTER);

        // Creating a panel to hold all sliders
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(13, 1));

        // Slider for mass1
        JLabel mass1Label = new JLabel("Mass 1: 20", JLabel.CENTER);
        JSlider mass1Slider = new JSlider(JSlider.HORIZONTAL, 10, 50, panel.getMass1());
        mass1Slider.addChangeListener(e -> {
            panel.setMass1(mass1Slider.getValue());
            mass1Label.setText("Mass 1: " + mass1Slider.getValue());
        });
        controlPanel.add(mass1Label);
        controlPanel.add(mass1Slider);

        // Slider for mass2
        JLabel mass2Label = new JLabel("Mass 2: 20", JLabel.CENTER);
        JSlider mass2Slider = new JSlider(JSlider.HORIZONTAL, 10, 50, panel.getMass2());
        mass2Slider.addChangeListener(e -> {
            panel.setMass2(mass2Slider.getValue());
            mass2Label.setText("Mass 2: " + mass2Slider.getValue());
        });
        controlPanel.add(mass2Label);
        controlPanel.add(mass2Slider);

        // Slider for radius1
        JLabel radius1Label = new JLabel("Radius 1: 50", JLabel.CENTER);
        JSlider radius1Slider = new JSlider(JSlider.HORIZONTAL, 30, 150, panel.getRadius1());
        radius1Slider.addChangeListener(e -> {
            panel.setRadius1(radius1Slider.getValue());
            radius1Label.setText("Radius 1: " + radius1Slider.getValue());
        });
        controlPanel.add(radius1Label);
        controlPanel.add(radius1Slider);

        // Slider for radius2
        JLabel radius2Label = new JLabel("Radius 2: 100", JLabel.CENTER);
        JSlider radius2Slider = new JSlider(JSlider.HORIZONTAL, 30, 150, panel.getRadius2());
        radius2Slider.addChangeListener(e -> {
            panel.setRadius2(radius2Slider.getValue());
            radius2Label.setText("Radius 2: " + radius2Slider.getValue());
        });
        controlPanel.add(radius2Label);
        controlPanel.add(radius2Slider);

        // Checkbox for dampening
        JCheckBox dampenCheckbox = new JCheckBox("Dampen", false);
        dampenCheckbox.setHorizontalAlignment(JLabel.CENTER);
        dampenCheckbox.addItemListener(e -> panel.setDampen(dampenCheckbox.isSelected()));
        controlPanel.add(dampenCheckbox);

        // Checkbox for toggling the trajectory
        JCheckBox trajectoryCheckbox = new JCheckBox("Show Trajectory", true);
        trajectoryCheckbox.setHorizontalAlignment(JLabel.CENTER);
        trajectoryCheckbox.addItemListener(e -> panel.toggleTrajectory(trajectoryCheckbox.isSelected()));
        controlPanel.add(trajectoryCheckbox);

        // Blank label for spacing
        controlPanel.add(new JLabel());

        // Button for resetting the simulation
        JButton resetButton = new JButton("Reset Simulation");
        resetButton.addActionListener(e -> panel.resetSimulation());
        controlPanel.add(resetButton);

        // Adding the control panel to the bottom of the frame
        controlPanel.setPreferredSize(new Dimension(150, HEIGHT));
        this.add(controlPanel, BorderLayout.EAST);

        timer.start();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.revalidate();
        this.repaint();
    }
}
