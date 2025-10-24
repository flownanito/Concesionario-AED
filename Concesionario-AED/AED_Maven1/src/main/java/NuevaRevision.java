import Models.Revision;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class NuevaRevision extends JDialog {
    private JTextField tfIdCliente, tfIdCamion;
    private Map<String, JCheckBox> checks = new LinkedHashMap<>();
    private JTextArea taDetalles;

    public NuevaRevision(Frame owner) {
        super(owner, "Nueva Revisión", true);
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Panel superior para IDs
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        top.add(new JLabel("ID Cliente:")); tfIdCliente = new JTextField(10); top.add(tfIdCliente);
        top.add(new JLabel("ID Camión:")); tfIdCamion = new JTextField(10); top.add(tfIdCamion);
        add(top, BorderLayout.NORTH);

        // Checklist
        String[] items = {"frenos","aceite","agua","remolque","filtro_combustion","cabina","alineación","caja_cambios","correa","neumáticos","filtro_aire"};
        JPanel center = new JPanel(new GridLayout(0,2,8,8));
        center.setBorder(BorderFactory.createTitledBorder("Checklist (marcar si revisado)"));
        for (String it : items) {
            JCheckBox cb = new JCheckBox(it);
            checks.put(it, cb);
            center.add(cb);
        }

        // Panel de detalles
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de revisión (si es necesario)"));
        taDetalles = new JTextArea(6,40);
        JScrollPane sp = new JScrollPane(taDetalles);
        panelDetalles.add(sp, BorderLayout.CENTER);

        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.add(center, BorderLayout.NORTH);
        centerWrap.add(panelDetalles, BorderLayout.CENTER);

        add(centerWrap, BorderLayout.CENTER);

        // Botones
        JPanel bottom = new JPanel();
        JButton guardar = new JButton("Guardar Revisión");
        JButton cancelar = new JButton("Cerrar");
        bottom.add(guardar); bottom.add(cancelar);
        add(bottom, BorderLayout.SOUTH);

        guardar.addActionListener(e -> guardarRevision());
        cancelar.addActionListener(e -> dispose());
    }

    private void guardarRevision() {
        String idCliente = tfIdCliente.getText().trim();
        String idCamion = tfIdCamion.getText().trim();
        if (idCliente.isEmpty() || idCamion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce ID Cliente y ID Camión.");
            return;
        }
        Revision r = new Revision();
        r.setId("R" + System.currentTimeMillis());
        r.setIdCliente(idCliente);
        r.setIdCamion(idCamion);
        Map<String, Boolean> checklist = new LinkedHashMap<>();
        checks.forEach((k,v) -> checklist.put(k, v.isSelected()));
        r.setChecklist(checklist);
        r.setDetalles(taDetalles.getText().trim());
        DataStore.REVISIONES.add(r);
        JOptionPane.showMessageDialog(this, "Revisión guardada en memoria: " + r.getId());

        // limpiar campos
        tfIdCliente.setText(""); tfIdCamion.setText("");
        checks.values().forEach(cb -> cb.setSelected(false));
        taDetalles.setText("");
        dispose();
    }
}
