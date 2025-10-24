
import Models.Revision;

import javax.swing.*;
import java.awt.*;

public class RevisionSearch extends JDialog {
    private JTextField tfIdCliente, tfIdCamion;
    private JTextArea resultadoArea;

    public RevisionSearch(Frame owner) {
        super(owner, "Buscar Revisión", true);
        setSize(550, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(2,3,6,6));
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tfIdCliente = new JTextField(12);
        tfIdCamion = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnNueva = new JButton("Nueva Revisión");;

        top.add(new JLabel("ID Cliente:")); top.add(tfIdCliente); top.add(new JLabel(""));
        top.add(new JLabel("ID Camión:")); top.add(tfIdCamion); top.add(btnBuscar);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(resultadoArea);

        btnBuscar.addActionListener(e -> {
            String idC = tfIdCliente.getText().trim();
            String idCam = tfIdCamion.getText().trim();
            if (idC.isEmpty() || idCam.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Introduce ambos IDs para buscar.");
                return;
            }
            var opt = DataStore.buscarRevision(idC, idCam);
            if (opt.isPresent()) {
                Revision r = opt.get();
                resultadoArea.setText(formatRevision(r));
            } else {
                resultadoArea.setText("No hay ninguna revisión con estos datos.");
            }
        });

        btnNueva.addActionListener(e -> {
            new NuevaRevision((Frame) this.getOwner()).setVisible(true);
        });

        add(top, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(btnNueva, BorderLayout.SOUTH);
    }

    private String formatRevision(Revision r) {
        StringBuilder sb = new StringBuilder();
        sb.append("Revisión ID: ").append(r.getId()).append("\n");
        sb.append("ID Cliente: ").append(r.getIdCliente()).append("\n");
        sb.append("ID Camión: ").append(r.getIdCamion()).append("\n");
        sb.append("Checklist:\n");
        r.getChecklist().forEach((k,v)-> sb.append(" - ").append(k).append(": ").append(v? "Sí":"No").append("\n"));
        sb.append("Detalles:\n").append(r.getDetalles()==null? "(sin detalles)" : r.getDetalles()).append("\n");
        return sb.toString();
    }
}
