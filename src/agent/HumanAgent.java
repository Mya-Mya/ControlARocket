package agent;

import rocketenv.Rocket;
import rocketenv.Space;

public class HumanAgent extends Agent {
    public HumanAgent(Rocket mRocket, Space mSpace) {
        super(mRocket, mSpace);
    }

    public void setMV(double angle) {
        mRocket.setMV(angle);
    }
}
