/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package im.expensive.protect;

import im.expensive.protect.SystemUtil;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class DisplayUtil$Frame
extends JFrame {
    public DisplayUtil$Frame() {
        this.setTitle("Verification failed.");
        this.setDefaultCloseOperation(2);
        this.setLocationRelativeTo(null);
        DisplayUtil$Frame.copyToClipboard();
        String message = "Sorry, you are not on the HWID list.\nHWID: " + SystemUtil.getSystemInfo() + "\n(Copied to clipboard.)";
        JOptionPane.showMessageDialog(this, message, "Could not verify your HWID successfully.", -1, UIManager.getIcon("OptionPane.errorIcon"));
    }

    public static void copyToClipboard() {
        StringSelection selection = new StringSelection(SystemUtil.getSystemInfo());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}

