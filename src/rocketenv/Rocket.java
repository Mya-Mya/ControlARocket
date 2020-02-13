package rocketenv;

import java.util.ArrayList;
import java.util.List;

/**
 * 設定値(MV)に基づいてエンジン出力を行うロケット。
 * モデル MV は速度
 */

public class Rocket {
    private List<RocketListener> mRocketListenerList;

    public void addCarListener(RocketListener l) {
        mRocketListenerList.add(l);
    }

    private void fireCarListeners(double deltaTime) {
        for (RocketListener mRocketListener : mRocketListenerList) mRocketListener.onRocketRan(deltaTime);
    }

    private double mv;//設定値(速度[m/s])
    private double mv0;//mvの初期値
    private double pv;//測定値(現在の位置[m])
    private double pv0;//pvの初期値
    private String name;

    /**
     * ロケットを生成する
     * @param pv ロケットの初期位置[N]
     * @param mass ロケットの質量[kg]
     * @param mvMax エンジン出力の最大値[N]
     * @param name
     */
    public Rocket(double pv, double mass,double mvMax, String name) {
        mRocketListenerList = new ArrayList<>();
        setPV(pv);
        pv0=pv;
        setMV(0);
        mv0=mv;
        this.name = name;
    }

    public double getMV() {
        return mv;
    }

    public double getPV() {
        return pv;
    }

    public void reset(){
        pv=pv0;
        mv=mv0;
    }
    /**
     * @param deltaTime 前回の実行からの空想上の経過時間
     */
    public void run(double deltaTime) {
        pv += mv*deltaTime;//velocity * deltaTime;
        fireCarListeners(deltaTime);
    }

    public void setMV(double mv) {
        this.mv = mv;
    }

    public void setPV(double pv) {
        this.pv = pv;
    }

    @Override
    public String toString() {
        return name;
    }
}
