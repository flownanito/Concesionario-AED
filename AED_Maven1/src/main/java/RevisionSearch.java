import Models.Revision;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RevisionSearch extends JDialog {
    private JTextField tfIdCliente, tfIdCamion;
    private JTextArea resultadoArea;

    public RevisionSearch(Frame owner) {
        super(owner, "Buscar Revisión", true);
        setSize(550, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel top = new JPanel(new GridLayout(2,3,6,6));
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tfIdCliente = new JTextField(12);
        tfIdCamion = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnNueva = new JButton("Nueva Revisión");

        top.add(new JLabel("ID Cliente:")); 
        top.add(tfIdCliente); 
        top.add(new JLabel(""));
        top.add(new JLabel("ID Camión:")); 
        top.add(tfIdCamion); 
        top.add(btnBuscar);

        // Área de resultados
        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(resultadoArea);

        // Botón para exportar a TXT con nombre automático
        JButton btnExportar = new JButton("Exportar a TXT");
        btnExportar.addActionListener(e -> exportarATXT());

        // Acción de buscar
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

        // Acción de nueva revisión
        btnNueva.addActionListener(e -> {
            new NuevaRevision((Frame) this.getOwner()).setVisible(true);
        });

        // Panel inferior con botones
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.add(btnNueva);
        bottom.add(btnExportar);

        add(top, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
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

    private void exportarATXT() {
        if (resultadoArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay contenido para exportar.");
            return;
        }

        String idCliente = tfIdCliente.getText().trim();
        String idCamion = tfIdCamion.getText().trim();

        if (idCliente.isEmpty() || idCamion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Los IDs de cliente y camión son necesarios para exportar.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar revisión como...");
        // Nombre automático: revision_IDCliente_IDCamion.txt
        fileChooser.setSelectedFile(new File("revision_" + idCliente + "_" + idCamion + ".txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(resultadoArea.getText());
                JOptionPane.showMessageDialog(this, "Archivo guardado exitosamente:\n" + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage());
            }
        }
    }
}