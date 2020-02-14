import view.MasterView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    new javax.swing.plaf.nimbus.NimbusLookAndFeel()
                    //new javax.swing.plaf.metal.MetalLookAndFeel()
                    //new javax.swing.plaf.synth.SynthLookAndFeel()
                    //"com.sun.java.swing.plaf.gtk.GTKLookAndFeel"
                    //"com.sun.java.swing.plaf.motif.MotifLookAndFeel"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        // write your code here
        new MasterView();
    }
}
