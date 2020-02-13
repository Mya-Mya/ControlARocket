package rocketenv;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * 目標値(SV)を管理する。
 */
public class Space {
    private PropertyChangeSupport pcs;

    public void addPCL(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    private double sv;

    public Space(double sv) {
        pcs = new PropertyChangeSupport(this);
        setSV(sv);
    }

    public double getSV() {
        return sv;
    }

    public void setSV(double sv) {
        double oldSV = this.sv;
        this.sv = sv;
        pcs.firePropertyChange("Space.setSV", oldSV, sv);
    }
}
