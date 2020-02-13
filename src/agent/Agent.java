package agent;

import rocketenv.Rocket;
import rocketenv.Space;

/**
 * 車を動かす主体となるもの。
 */
abstract public class Agent {
    protected final Rocket mRocket;
    protected final Space mSpace;

    public Agent(Rocket mRocket, Space mSpace) {
        this.mRocket = mRocket;
        this.mSpace = mSpace;
    }

    @Override
    public String toString() {
        return "Agent of " + mRocket.toString();
    }
}
