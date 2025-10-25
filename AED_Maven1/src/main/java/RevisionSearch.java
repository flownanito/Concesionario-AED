
import Models.Revision;
import DAO.RevisionDAO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RevisionSearch extends JDialog {

    private JTextField tfIdCliente, tfIdCamion;
    private JTextArea resultadoArea;

    public RevisionSearch(Frame owner) {
        super(owner, "Buscar Revisión", true);
        setSize(550, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel top = new JPanel(new GridLayout(2, 3, 6, 6));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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

        // Botón exportar
        JButton btnExportar = new JButton("Exportar a TXT");
        btnExportar.addActionListener(e -> exportarATXT());

        // Acción del botón Buscar
        btnBuscar.addActionListener(e -> buscarRevision());

        // Acción de nueva revisión
        btnNueva.addActionListener(e -> {
            new NuevaRevision((Frame) this.getOwner()).setVisible(true);
        });

        // Panel inferior
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottom.add(btnNueva);
        bottom.add(btnExportar);

        add(top, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // Buscar revisiones en la base de datos según los IDs
    private void buscarRevision() {
        String idCliente = tfIdCliente.getText().trim();
        String idCamion = tfIdCamion.getText().trim();

        if (idCliente.isEmpty() || idCamion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce ambos IDs para buscar.");
            return;
        }

        //buscar en la base datos
        List<Revision> revisiones = RevisionDAO.buscarRevisiones(idCliente, idCamion);

        // Busca la coincidencia por ambos IDs en memoria
        Revision encontrada = revisiones.stream()
                .filter(r -> idCliente.equals(r.getIdCliente()) && idCamion.equals(r.getIdCamion()))
                .findFirst()
                .orElse(null);

        if (encontrada == null) {
            resultadoArea.setText("No hay ninguna revisión con estos datos.");
        } else {
            resultadoArea.setText(formatRevision(encontrada));
        }
    }

    // Formatea los datos para mostrarlos
    private String formatRevision(Revision r) {
        StringBuilder sb = new StringBuilder();
        sb.append("Revisión ID: ").append(r.getId()).append("\n");
        sb.append("ID Camión: ").append(r.getIdCamion()).append("\n");
        sb.append("Fecha revisión: ").append(r.getFechaRevision()).append("\n\n");
        sb.append("Checklist:\n");
        for (Map.Entry<String, Boolean> e : r.getChecklist().entrySet()) {
            sb.append(" - ").append(e.getKey())
                    .append(": ").append(e.getValue() ? "✅" : "❌").append("\n");
        }
        sb.append("\nDetalles:\n").append(r.getDetalles() == null ? "(sin detalles)" : r.getDetalles()).append("\n");
        return sb.toString();
    }

    // Exporta el resultado a un archivo TXT
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
