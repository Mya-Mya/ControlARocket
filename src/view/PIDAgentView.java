package view;

import agent.PIDAgent;
import util.LinearConverter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIDAgentView extends JDialog implements ChangeListener, ActionListener {
    private final PIDAgent mPIDAgent;

    private JSlider vKpSlider;
    private JSlider vKiSlider;
    private JSlider vKdSlider;

    private JLabel vKpValueLabel;
    private JLabel vKiValueLabel;
    private JLabel vKdValueLabel;

    private LinearConverter mSliderToAgent;

    public PIDAgentView(PIDAgent mPIDAgent, Window owner) {
        super(owner, "PIDAgentView");
        this.mPIDAgent = mPIDAgent;
        mSliderToAgent =new LinearConverter(0,0,1000,5);//X=Sliderの設定値、Y=PIDエージェントに行う設定値
        setSize(new Dimension(400, 300));

        JPanel vOperationPanel = new JPanel();
        vOperationPanel.setLayout(new GridLayout(3, 3, 30, 0));

        JLabel vKpHelper = new JLabel("Kp", SwingConstants.RIGHT);
        vKpSlider = new JSlider(0, 1000, 0);
        vKpSlider.addChangeListener(this);
        vKpValueLabel = new JLabel();
        JLabel vKiHelper = new JLabel("Ki", SwingConstants.RIGHT);
        vKiSlider = new JSlider(0, 1000, 0);
        vKiSlider.addChangeListener(this);
        vKiValueLabel = new JLabel();
        JLabel vKdHelper = new JLabel("Kd", SwingConstants.RIGHT);
        vKdSlider = new JSlider(0, 1000, 0);
        vKdSlider.addChangeListener(this);
        vKdValueLabel = new JLabel();

        vOperationPanel.add(vKpHelper);
        vOperationPanel.add(vKpSlider);
        vOperationPanel.add(vKpValueLabel);
        vOperationPanel.add(vKiHelper);
        vOperationPanel.add(vKiSlider);
        vOperationPanel.add(vKiValueLabel);
        vOperationPanel.add(vKdHelper);
        vOperationPanel.add(vKdSlider);
        vOperationPanel.add(vKdValueLabel);

        JButton vDeleteIntegral = new JButton("積分を削除");
        vDeleteIntegral.addActionListener(this);

        add(vOperationPanel, BorderLayout.CENTER);
        add(vDeleteIntegral, BorderLayout.WEST);

        vKpSlider.setValue((int) mSliderToAgent.fromYtoX(mPIDAgent.getKp()));
        vKiSlider.setValue((int) mSliderToAgent.fromYtoX(mPIDAgent.getKi()));
        vKdSlider.setValue((int) mSliderToAgent.fromYtoX(mPIDAgent.getKd()));
        vKpValueLabel.setText(Double.toString(mSliderToAgent.fromXtoY(vKpSlider.getValue())));
        vKiValueLabel.setText(Double.toString(mSliderToAgent.fromXtoY(vKiSlider.getValue())));
        vKdValueLabel.setText(Double.toString(mSliderToAgent.fromXtoY(vKdSlider.getValue())));

        pack();
        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        Object source = changeEvent.getSource();
        if (source == vKpSlider) {
            double Kp=mSliderToAgent.fromXtoY(vKpSlider.getValue());
            mPIDAgent.setKp(Kp);
            vKpValueLabel.setText(Double.toString(Kp));
        } else if (source == vKiSlider) {
            double Ki=mSliderToAgent.fromXtoY(vKiSlider.getValue());
            mPIDAgent.setKi(Ki);
            vKiValueLabel.setText(Double.toString(Ki));
        } else if (source == vKdSlider) {
            double Kd=mSliderToAgent.fromXtoY(vKdSlider.getValue());
            mPIDAgent.setKd(Kd);
            vKdValueLabel.setText(Double.toString(Kd));
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        mPIDAgent.setIntegratedError(0);
    }
}
