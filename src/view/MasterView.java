package view;

import agent.PIDAgent;
import rocketenv.Rocket;
import rocketenv.Space;
import util.LinearConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MasterView extends JFrame implements ActionListener, MouseListener, ComponentListener {
    private final Space mSpace;
    private Rocket mRocket;
    private JLabel vRocketStatusLabel;
    private double deltaTime;
    private LinearConverter linearRocketenvAndView;//X=rocketenv空間 Y=view空間

    public MasterView() {
        super("ControlARocket");
        setPreferredSize(new Dimension(800, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mSpace = new Space(0);
        deltaTime = 0.1;

        addComponentListener(this);
        addMouseListener(this);

        JToolBar vToolsBar=new JToolBar();
        vRocketStatusLabel=new JLabel();
        vToolsBar.add(vRocketStatusLabel);
        JButton vPanic=new JButton("RESET");
        vPanic.addActionListener(actionEvent -> {mRocket.reset();});
        vToolsBar.add(vPanic);
        add(vToolsBar,BorderLayout.NORTH);

        Rocket mPIDRocket = new Rocket(-3, 1,10, "PIDRocket");
        PIDAgent mPIDAgent = new PIDAgent(mPIDRocket, mSpace);
        new PIDAgentView(mPIDAgent, this);
        setRocket(mPIDRocket);

        pack();
        componentResized(null);//linearRocketenvAndViewを生成する。

        Timer timer = new Timer((int) (deltaTime * 1000), this);
        timer.start();
        setVisible(true);
    }

    private int rocketX = 0;

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, (int) (linearRocketenvAndView.fromXtoY(mSpace.getSV()) - 2), getWidth(), 4);

        g.setColor(Color.RED);
        rocketX += 20;
        if (rocketX > getWidth()) rocketX = 0;

        g.fillOval(rocketX, (int) (linearRocketenvAndView.fromXtoY(mRocket.getPV()) - 6), 12, 12);

    }


    public void setRocket(Rocket mRocket) {
        this.mRocket = mRocket;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        mRocket.run(deltaTime);
        vRocketStatusLabel.setText("y="+mRocket.getPV()+", v="+mRocket.getMV());
        repaint(5);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mSpace.setSV(linearRocketenvAndView.fromYtoX(mouseEvent.getY()));
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        if (linearRocketenvAndView == null) linearRocketenvAndView = new LinearConverter(3, 0, -3, getHeight());
        else linearRocketenvAndView.setRelation(3, 0, -3, getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }
}
