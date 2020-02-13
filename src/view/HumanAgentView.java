package view;

import agent.HumanAgent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class HumanAgentView extends JDialog implements ChangeListener {
    private final HumanAgent mHumanAgent;
    private JSlider vSpeedSlider;

    public HumanAgentView(HumanAgent mHumanAgent, Window owner) {
        super(owner, "HumanAgentView", ModalityType.MODELESS);
        this.mHumanAgent = mHumanAgent;
        setSize(new Dimension(400, 200));

        vSpeedSlider = new JSlider(-70, 70, 0);
        vSpeedSlider.addChangeListener(this);
        add(vSpeedSlider);

        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        mHumanAgent.setMV((double) vSpeedSlider.getValue() * 0.1);
    }
}
