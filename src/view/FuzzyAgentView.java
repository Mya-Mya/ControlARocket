package view;

import agent.FuzzyAgent;

import javax.swing.*;
import java.awt.*;

public class FuzzyAgentView extends JDialog {
    public FuzzyAgentView(FuzzyAgent mFuzzyAgent, Window you){
        super(you,"FuzzyAgentView",ModalityType.MODELESS);
        setSize(new Dimension(100,100));
        add(new JLabel("FuzzyAgent is running! Good Luck!"));
        pack();
        setVisible(true);
    }
}
