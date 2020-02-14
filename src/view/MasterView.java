package view;

import agent.FuzzyAgent;
import agent.PIDAgent;
import rocketenv.Rocket;
import rocketenv.Space;
import util.LinearConverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MasterView extends JFrame implements ActionListener, MouseListener, ComponentListener {
    private final Space mSpace;
    private JLabel vRocketStatusLabel;
    private double deltaTime;
    private LinearConverter linearRocketenvAndView;//X=rocketenv空間 Y=view空間

    private Rocket mFuzzyRocket;
    private Rocket mPIDRocket;

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
        vPanic.addActionListener(actionEvent -> {
            mFuzzyRocket.reset();
            mPIDRocket.reset();
        });
        vToolsBar.add(vPanic);
        add(vToolsBar,BorderLayout.NORTH);

        /*
        ロケットとそのエージェントの起動
         */
        mFuzzyRocket = new Rocket(-3,  "FuzzyRocket");
        FuzzyAgent mFuzzyAgent=new FuzzyAgent(mFuzzyRocket,mSpace);
        new FuzzyAgentView(mFuzzyAgent,this);

        mPIDRocket =new Rocket(-3,"PIDRocket");
        PIDAgent mPIDAgent = new PIDAgent(mPIDRocket, mSpace);
        new PIDAgentView(mPIDAgent, this);

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

        rocketX += 20;
        if (rocketX > getWidth()) rocketX = 0;

        g.setColor(Color.RED);
        int y=(int) (linearRocketenvAndView.fromXtoY(mFuzzyRocket.getPV()) - 6);
        g.fillOval(rocketX, y, 12, 12);
        g.drawString(mFuzzyRocket.toString(),rocketX,y);

        g.setColor(Color.BLUE);
        y= (int)(linearRocketenvAndView.fromXtoY(mPIDRocket.getPV())-6);
        g.fillOval(rocketX,y,12,12);
        g.drawString(mPIDRocket.toString(),rocketX,y+24);

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        mFuzzyRocket.run(deltaTime);
        mPIDRocket.run(deltaTime);
        //vRocketStatusLabel.setText("y="+ mFuzzyRocket.getPV()+", v="+ mFuzzyRocket.getMV());
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
