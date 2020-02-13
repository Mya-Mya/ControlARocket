package agent;

import rocketenv.Rocket;
import rocketenv.RocketListener;
import rocketenv.Space;

public class PIDAgent extends Agent implements RocketListener {
    private double error = 0;//偏差
    private double oldError = 0;//前回の偏差
    private double integratedError = 0;//積分偏差
    private double differentiatedError = 0;//微分偏差
    private double Kp = 1.6;//Pゲイン
    private double Ki = 3.5;//Iゲイン
    private double Kd = 0.4;//Dゲイン

    public PIDAgent(Rocket mRocket, Space mSpace) {
        super(mRocket, mSpace);
        mRocket.addCarListener(this);
    }

    public double getError() {
        return error;
    }

    public double getIntegratedError() {
        return integratedError;
    }

    public double getDifferentiatedError() {
        return differentiatedError;
    }

    public double getKp() {
        return Kp;
    }

    public double getKi() {
        return Ki;
    }

    public double getKd() {
        return Kd;
    }

    @Override
    public void onRocketRan(double deltaTime) {
        oldError = error;
        //偏差の収集
        error = mSpace.getSV() - mRocket.getPV();
        //積分
        integratedError += error * deltaTime;
        //微分
        differentiatedError = (error - oldError) / deltaTime;
        //PID制御
        mRocket.setMV(Kp * error + Ki * integratedError + Kd * differentiatedError);
    }

    public void setKp(double kp) {
        Kp = kp;
    }

    public void setKi(double ki) {
        Ki = ki;
    }

    public void setKd(double kd) {
        Kd = kd;
    }

    public void setIntegratedError(double integratedError) {
        this.integratedError = integratedError;
    }
}
