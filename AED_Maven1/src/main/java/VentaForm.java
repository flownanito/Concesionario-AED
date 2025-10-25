
import Models.Cliente;
import Models.Camion;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentaForm extends JDialog {

    private JComboBox<Cliente> cbCliente;
    private JComboBox<Camion> cbCamion;
    private JTextField tfFechaCompra;
    private JTextArea areaResumen;
    private JComboBox<String> cbEstado;

    public VentaForm(Frame owner) {
        super(owner, "Registrar Venta", true);
        setSize(550, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;

        // 🔹 Cliente
        form.add(new JLabel("Cliente:"), c);
        c.gridx = 1;
        cbCliente = new JComboBox<>(DataStore.CLIENTES.toArray(new Cliente[0]));
        cbCliente.setEditable(true);
        form.add(cbCliente, c);

        //  Boton Cliente
        c.gridx = 2;
        JButton btnBuscarCliente = new JButton("Buscar");
        form.add(btnBuscarCliente, c);
        
        btnBuscarCliente.addActionListener(e -> buscarCliente());

        // 🔹 Camión 
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Camión:"), c);
        c.gridx = 1;
        cbCamion = new JComboBox<>(DataStore.CAMIONES.toArray(new Camion[0]));
        cbCamion.setEditable(true);
        form.add(cbCamion, c);

        //Boton Camion
        c.gridx = 2;
        JButton btnBuscarCamion = new JButton("Buscar");
        form.add(btnBuscarCamion, c);

        //Accion del Boton
        btnBuscarCamion.addActionListener(e -> buscarCamion());

        // 🔹 Estado del vehículo
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Estado del vehículo:"), c);
        c.gridx = 1;
        String[] estados = {"Disponible", "Vendido", "En revisión", "En almacén"};
        cbEstado = new JComboBox<>(estados);
        form.add(cbEstado, c);

        // 🔹 Fecha de compra
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Fecha de compra:"), c);
        c.gridx = 1;
        tfFechaCompra = new JTextField(15);
        tfFechaCompra.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        form.add(tfFechaCompra, c);

        // 🔹 Resumen
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        areaResumen = new JTextArea(8, 40);
        areaResumen.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResumen);
        scroll.setBorder(BorderFactory.createTitledBorder("Resumen de la venta"));
        form.add(scroll, c);

        // 🔹 Botones
        JPanel botones = new JPanel();
        JButton btnMostrar = new JButton("Mostrar resumen");
        JButton btnGuardar = new JButton("Registrar venta");
        JButton btnExportarTXT = new JButton("Exportar a TXT");
        JButton btnCerrar = new JButton("Cerrar");
        botones.add(btnMostrar);
        botones.add(btnGuardar);
        botones.add(btnExportarTXT);
        botones.add(btnCerrar);

        btnMostrar.addActionListener(e -> mostrarResumen());
        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
            dispose();
        });
        btnCerrar.addActionListener(e -> dispose());
        btnExportarTXT.addActionListener(e -> exportarTXT());

        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void buscarCliente() {
        String input = cbCliente.getEditor().getItem().toString().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce ID/Nombre para buscar");
            return;
        }

        Cliente encontrado = null;
        for (Cliente c : DataStore.CLIENTES) {
            if (c.getId().equalsIgnoreCase(input) || c.getNombre().equalsIgnoreCase(input)) {
                encontrado = c;
                break;
            }
        }

        if (encontrado != null) {
            // Selecciona el cliente en el JComboBox
            cbCliente.setSelectedItem(encontrado);

            // Mostrar datos sólo si existe
            String datosCliente = "===== DATOS CLIENTE =====\n"
                    + "ID: " + encontrado.getId() + "\n"
                    + "NIF: " + encontrado.getNif() + "\n"
                    + "Nombre: " + encontrado.getNombre() + "\n"
                    + "Dirección: " + encontrado.getDireccion() + "\n"
                    + "Ciudad: " + encontrado.getCiudad() + "\n\n";

            areaResumen.setText(datosCliente);
            mostrarResumen();

        } else {
            areaResumen.setText(""); // limpia la info si no se encuentra
            JOptionPane.showMessageDialog(this, "Cliente no encontrado");
        }
    }

    private void buscarCamion() {
        String input = cbCamion.getEditor().getItem().toString().trim();
        String estado = (String) cbEstado.getSelectedItem();
        String fecha = tfFechaCompra.getText();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce ID/Matricula para buscar");
            return;
        }

        Camion encontrado = null;
        for (Camion c : DataStore.CAMIONES) {
            if (c.getId_camion().equalsIgnoreCase(input) || c.getMatricula().equalsIgnoreCase(input)) {
                encontrado = c;
                break;
            }
        }

        if (encontrado != null) {
            // Selecciona el camión en el JComboBox
            cbCamion.setSelectedItem(encontrado);

            // Mostrar datos sólo si existe
            String datosCamion = "===== DATOS CAMION =====\n"
                    + "ID: " + encontrado.getId_camion() + "\n"
                    + "Marca: " + encontrado.getMarca() + "\n"
                    + "Modelo: " + encontrado.getModelo() + "\n"
                    + "Matrícula: " + encontrado.getMatricula() + "\n"
                    + "Precio de venta: " + encontrado.getPrecioVenta() + " €\n"
                    + "Estado actual: " + estado + "\n\n";

            areaResumen.setText(datosCamion);
            mostrarResumen();

        } else {
            areaResumen.setText(""); // limpia la info si no se encuentra
            JOptionPane.showMessageDialog(this, "Camion no encontrado");
        }
    }

    private void mostrarResumen() {
        Cliente cliente = (Cliente) cbCliente.getSelectedItem();
        Camion camion = (Camion) cbCamion.getSelectedItem();

        if (cliente == null && camion == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente y un camión.");
            return;
        }

        String estado = (String) cbEstado.getSelectedItem();
        String fecha = tfFechaCompra.getText();

        String resumen = "===== DATOS DEL CLIENTE =====\n"
                + "ID: " + cliente.getId() + "\n"
                + "NIF: " + cliente.getNif() + "\n"
                + "Nombre: " + cliente.getNombre() + "\n"
                + "Dirección: " + cliente.getDireccion() + "\n"
                + "Ciudad: " + cliente.getCiudad() + "\n\n"
                + "===== DATOS DEL CAMIÓN =====\n"
                + "ID: " + camion.getId_camion() + "\n"
                + "Marca: " + camion.getMarca() + "\n"
                + "Modelo: " + camion.getModelo() + "\n"
                + "Matrícula: " + camion.getMatricula() + "\n"
                + "Precio de venta: " + camion.getPrecioVenta() + " €\n"
                + "Estado actual: " + estado + "\n\n"
                + "===== DETALLES DE LA VENTA =====\n"
                + "Fecha de compra: " + fecha;

        areaResumen.setText(resumen);
    }

    private void exportarTXT() {
        String resumen = areaResumen.getText();

        if (resumen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero genera el resumen antes de exportar a TXT.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar resumen de venta");
        fileChooser.setSelectedFile(new File("resumen_venta.txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();

            // Asegurar extensión .txt
            if (!archivo.getName().toLowerCase().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }

            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write("=== RESUMEN DE LA VENTA ===\n\n");
                writer.write(resumen);
                JOptionPane.showMessageDialog(this, "Archivo TXT guardado correctamente en:\n" + archivo.getAbsolutePath());

                // Abrir automáticamente
                Desktop.getDesktop().open(archivo);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage());
            }
        }
    }
}
