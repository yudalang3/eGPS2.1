package utils;

import com.alibaba.fastjson.JSONObject;
import utils.storage.FinalFields;
import utils.storage.MapPersistence;
import utils.storage.ObjectPersistence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Map;
import java.util.Optional;

public class EGPSGuiUtil {

    // Specifies the path to save dialog sizes, note that this should be for common dialogs only
    private static String DIALOG_SAVE_PATH = FinalFields.OBJECT_DIR.concat("/simplest.IDE.size.ser");

    /**
     * Creates a simple IDE-like interface that allows data loading and subsequent program execution.
     * This is useful for applications where data loading is time-consuming.
     *
     * @param buttonAction The action to be performed when the button is clicked.
     */
    public static void universalSimplestIDE(Runnable buttonAction) {

        JButton jButton = new JButton("Click me");
        jButton.addActionListener(e -> {
            new Thread(buttonAction).start();
        });

        Optional<Rectangle> dialogSize = getDialogSize();


        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Universal simplest IDE");

            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            jPanel.add(jButton, BorderLayout.CENTER);
            frame.add(jPanel);

            frame.setSize(400, 200);

            if (dialogSize.isPresent()){
                Rectangle rectangle = dialogSize.get();
                frame.setSize(rectangle.getSize());
                frame.setLocation(rectangle.getLocation());

            }else {
                frame.setLocationRelativeTo(null);
            }
            frame.setVisible(true);
            frame.setAlwaysOnTop(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Rectangle bounds = frame.getBounds();
                    ObjectPersistence.saveObjectByObjectOutput(bounds, DIALOG_SAVE_PATH);
                    System.exit(0);
                }
            });

        });
    }

    private static Optional<Rectangle> getDialogSize() {

        Object objectByObjectInput = ObjectPersistence.getObjectByObjectInput(DIALOG_SAVE_PATH);
        if (objectByObjectInput == null) {
            return Optional.empty();
        } else {
            Rectangle object = (Rectangle) objectByObjectInput;
            return Optional.of(object);
        }
    }
}
