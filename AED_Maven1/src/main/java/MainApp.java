

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestión Taller - Menú Principal");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JPanel p = new JPanel();
            p.setLayout(new GridLayout(4, 1, 10, 10));
            JButton b1 = new JButton("1. Generar cliente");
            JButton b2 = new JButton("2. Generar camión");
            JButton b3 = new JButton("3. Revisión (buscar)");
            JButton b4 = new JButton("4. Nueva revisión");

            b1.addActionListener(e -> new ClienteForm(frame).setVisible(true));
            b2.addActionListener(e -> new CamionForm(frame).setVisible(true));
            b3.addActionListener(e -> new RevisionSearch(frame).setVisible(true));
            b4.addActionListener(e -> new NuevaRevision(frame).setVisible(true));

            p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
            p.add(b1); p.add(b2); p.add(b3); p.add(b4);
            frame.add(p);
            frame.setVisible(true);
        });
    }
}
