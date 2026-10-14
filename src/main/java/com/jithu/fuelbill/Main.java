package com.jithu.fuelbill;

import com.jithu.fuelbill.ui.BillingUI;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingUI::new);
    }
}