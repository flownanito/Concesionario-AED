import Models.Cliente;
import Models.Camion;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VentaForm extends JDialog {

    private JComboBox<Cliente> cbCliente;
    private JComboBox<Camion> cbCamion;
    private JTextField tfFechaCompra, tfEstadoVehiculo;
    private JTextArea areaResumen;

    public VentaForm(Frame owner) {
        super(owner, "Registrar Venta", true);
        setSize(550, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0;

        // 🔹 Cliente
        form.add(new JLabel("Cliente:"), c);
        c.gridx = 1;
        cbCliente = new JComboBox<>(DataStore.CLIENTES.toArray(new Cliente[0]));
        form.add(cbCliente, c);

        // 🔹 Camión
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Camión:"), c);
        c.gridx = 1;
        cbCamion = new JComboBox<>(DataStore.CAMIONES.toArray(new Camion[0]));
        form.add(cbCamion, c);

        // 🔹 Estado del vehículo
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Estado del vehículo:"), c);
        c.gridx = 1;
        String[] estados = {"Disponible", "Vendido", "En revisión", "En almacén"};
        JComboBox<String> cbEstado = new JComboBox<>(estados);
        form.add(cbEstado, c);

        // 🔹 Fecha de compra (por defecto hoy)
        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Fecha de compra:"), c);
        c.gridx = 1;
        tfFechaCompra = new JTextField(15);
        tfFechaCompra.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        form.add(tfFechaCompra, c);

        // 🔹 Resumen (para mostrar todos los datos)
        c.gridx = 0; c.gridy++;
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
        JButton btnCerrar = new JButton("Cerrar");
        botones.add(btnMostrar);
        botones.add(btnGuardar);
        botones.add(btnCerrar);

        btnMostrar.addActionListener(e -> {
            Cliente cliente = (Cliente) cbCliente.getSelectedItem();
            Camion camion = (Camion) cbCamion.getSelectedItem();
            if (cliente == null || camion == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente y un camión.");
                return;
            }

            String estado = (String) cbEstado.getSelectedItem();
            String fecha = tfFechaCompra.getText();

            String resumen = "===== DATOS DEL CLIENTE =====\n" +
                    "ID: " + cliente.getId() + "\n" +
                    "NIF: " + cliente.getNif() + "\n" +
                    "Nombre: " + cliente.getNombre() + "\n" +
                    "Dirección: " + cliente.getDireccion() + "\n" +
                    "Ciudad: " + cliente.getCiudad() + "\n\n" +

                    "===== DATOS DEL CAMIÓN =====\n" +
                    "ID: " + camion.getId() + "\n" +
                    "Marca: " + camion.getMarca() + "\n" +
                    "Modelo: " + camion.getModelo() + "\n" +
                    "Matrícula: " + camion.getMatricula() + "\n" +
                    "Precio de venta: " + camion.getPrecioVenta() + " €\n" +
                    "Estado actual: " + estado + "\n\n" +

                    "===== DETALLES DE LA VENTA =====\n" +
                    "Fecha de compra: " + fecha;

            areaResumen.setText(resumen);
        });

        btnGuardar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Venta registrada correctamente.");
            dispose();
        });

        btnCerrar.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }
}