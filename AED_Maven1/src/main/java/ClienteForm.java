
import Models.Cliente;
import DAO.ClienteDAO;
import Utils.Validador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ClienteForm extends JDialog {

    private JTextField tfId;
    private JTextField tfNif, tfNombre, tfDireccion, tfCiudad;
    private JPanel telefonosPanel;
    private JComboBox<Integer> cbNumTelefonos;
    private java.util.List<JTextField> telefonoFields = new ArrayList<>();

    public ClienteForm(Frame owner) {
        super(owner, "Generar Cliente", true);
        setSize(500, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;

        tfId = new JTextField(15);
        tfId.setEditable(true);
        tfId.setText(DataStore.genererIdCliente());

        tfNif = new JTextField(20);
        tfNombre = new JTextField(20);
        tfDireccion = new JTextField(20);
        tfCiudad = new JTextField(20);

        form.add(new JLabel("ID cliente:"), c);
        c.gridx = 1;
        form.add(tfId, c);
        c.gridx = 1;
        JButton btnBorrarCliente = new JButton("🗑️");
        c.gridx = 2;
        form.add(btnBorrarCliente, c);
        btnBorrarCliente.addActionListener(e -> {
            String id = tfId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de cliente para borrar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que deseas borrar este cliente y todos sus datos?",
                    "Confirmar borrado",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = ClienteDAO.borrarCliente(id);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Cliente borrado correctamente.");
                    // Limpiar campos
                    tfId.setText("");
                    tfNif.setText("");
                    tfNombre.setText("");
                    tfDireccion.setText("");
                    tfCiudad.setText("");
                    actualizarTelefonos(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al borrar. Verifique el ID.");
                }
            }
        });

        
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("NIF:"), c);
        c.gridx = 1;
        form.add(tfNif, c);
        c.gridx = 1;
        JButton btnActualizarNIF = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarNIF, c);
        btnActualizarNIF.addActionListener(e -> {
            String id = tfId.getText().trim();
            String nuevoNif = tfNif.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de cliente para actualizar.");
                return;
            }

            // Validar formato del NIF (usa tu Validador existente)
            if (!Validador.validarNIF(nuevoNif)) {
                JOptionPane.showMessageDialog(this,
                        "NIF no válido. Debe tener 8 números y una letra mayúscula (ej: 12345678A).");
                return;
            }

            boolean ok = ClienteDAO.actualizarNIF(id, nuevoNif);
            if (ok) {
                JOptionPane.showMessageDialog(this, "NIF actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del cliente.");
            }
        });

        
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Nombre completo:"), c);
        c.gridx = 1;
        form.add(tfNombre, c);
        c.gridx = 1;
        JButton btnActualizarNombre = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarNombre, c);
        btnActualizarNombre.addActionListener(e -> {
            String id = tfId.getText().trim();
            String nuevoNombre = tfNombre.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de cliente para actualizar.");
                return;
            }
            if (!nuevoNombre.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]{1,50}$")) {
                JOptionPane.showMessageDialog(this, "Nombre no válida. Solo letras (máx. 50 caracteres).");
                return;
            }
            boolean ok = ClienteDAO.actualizarNombre(id, nuevoNombre);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Nombre actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del cliente.");
            }
        });

        
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Dirección:"), c);
        c.gridx = 1;
        form.add(tfDireccion, c);
        c.gridx = 1;
        JButton btnActualizarDireccion = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarDireccion, c);
        btnActualizarDireccion.addActionListener(e -> {
            String id = tfId.getText().trim();
            String nuevaDireccion = tfDireccion.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de cliente para actualizar.");
                return;
            }

            // Validar formato de dirección
            if (!nuevaDireccion.matches("^c/\\s.+\\s+n/\\s+\\d+$")) {
                JOptionPane.showMessageDialog(this,
                        "Dirección no válida. Debe seguir el formato: c/ Calle n/ Número");
                return;
            }

            boolean ok = ClienteDAO.actualizarDireccion(id, nuevaDireccion);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Dirección actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del cliente.");
            }
        });

        
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Ciudad residencia:"), c);
        c.gridx = 1;
        form.add(tfCiudad, c);
        c.gridx = 1;
        JButton btnActualizarCiudad = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarCiudad, c);
        btnActualizarCiudad.addActionListener(e -> {
            String id = tfId.getText().trim();
            String nuevaCiudad = tfCiudad.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de cliente para actualizar.");
                return;
            }
            if (!nuevaCiudad.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]{1,50}$")) {
                JOptionPane.showMessageDialog(this, "Ciudad no válida. Solo letras (máx. 50 caracteres).");
                return;
            }
            boolean ok = ClienteDAO.actualizarCiudad(id, nuevaCiudad);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ciudad actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del cliente.");
            }
        });

        // Teléfonos dinámicos
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Telefonos (cantidad):"), c);
        c.gridx = 1;
        Integer[] nums = new Integer[10];
        for (int i = 0; i < 10; i++) {
            nums[i] = i + 1;
        }

        cbNumTelefonos = new JComboBox<>(nums);
        cbNumTelefonos.setSelectedIndex(0);
        JButton btnGenerarTelefonos = new JButton("Generar campos");
        JPanel telControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        telControl.add(cbNumTelefonos);
        telControl.add(btnGenerarTelefonos);
        form.add(telControl, c);
        JButton btnBorrarTelefonos = new JButton("✎");
        c.gridx = 2;
        form.add(btnBorrarTelefonos, c);
        
        
        
        
        
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        telefonosPanel = new JPanel();
        telefonosPanel.setLayout(new BoxLayout(telefonosPanel, BoxLayout.Y_AXIS));
        telefonosPanel.setBorder(BorderFactory.createTitledBorder("Teléfonos"));

        //JScrollPane con scroll vertical pero segun necesidad
        JScrollPane sp = new JScrollPane(telefonosPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(420, 120)); // tamaño visible del scroll
        form.add(sp, c);

        btnGenerarTelefonos.addActionListener(e -> {
            int cantidad = (Integer) cbNumTelefonos.getSelectedItem();
            actualizarTelefonos(cantidad);
        });

        // botones guardar/cancelar
        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar cliente");
        JButton cancelar = new JButton("Cerrar");
        botones.add(guardar);
        botones.add(cancelar);

        guardar.addActionListener(e -> {
            String nif = tfNif.getText().trim();
            String ciudad = tfCiudad.getText().trim();
            String direccion = tfDireccion.getText().trim();

            // Validar NIF
            if (!Validador.validarNIF(nif)) {
                JOptionPane.showMessageDialog(this, "NIF no válido. Debe tener 8 números y una letra mayúscula (ej: 12345678A).");
                return;
            }

            // Validar dirección
            if (!direccion.matches("^c/\\s.+\\s+n/\\s+\\d+$")) {
                JOptionPane.showMessageDialog(this, "Dirección no válida. Debe seguir el formato: c/ Calle n/ Número");
                return;
            }

            // Validar ciudad (solo letras, máx. 50)
            if (!ciudad.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑ\\s]{1,50}$")) {
                JOptionPane.showMessageDialog(this, "Ciudad no válida. Solo letras (máx. 50 caracteres).");
                return;
            }

            // Validar teléfonos
            for (JTextField f : telefonoFields) {
                String t = f.getText().trim();
                if (!t.isEmpty() && !Validador.validarTelefono(t)) {
                    JOptionPane.showMessageDialog(this, "Teléfono no válido. Debe tener exactamente 9 números.");
                    return;
                }
            }

            // Si todo es válido, crear cliente
            Cliente cliente = new Cliente();
            cliente.setId(tfId.getText().trim());
            cliente.setNif(nif);
            cliente.setNombre(tfNombre.getText().trim());
            cliente.setDireccion(direccion);
            cliente.setCiudad(ciudad);

            java.util.List<String> telefonos = new ArrayList<>();
            for (JTextField f : telefonoFields) {
                String t = f.getText().trim();
                if (!t.isEmpty()) {
                    telefonos.add(t);
                }
            }
            cliente.setTelefonos(telefonos);

            //Guarda en la base datos
            boolean ok = ClienteDAO.insertarCliente(cliente);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cliente guardado en la base de datos correctamente: " + cliente.getId());
                // limpiar campos
                tfId.setText("");
                tfNif.setText("");
                tfNombre.setText("");
                tfDireccion.setText("");
                tfCiudad.setText("");
                actualizarTelefonos(0);
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el cliente en la base de datos.");
            }

        });

        cancelar.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void actualizarTelefonos(int n) {
        telefonosPanel.removeAll();
        telefonoFields.clear();
        for (int i = 0; i < n; i++) {
            JTextField tf = new JTextField(20);
            telefonoFields.add(tf);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(new JLabel("Teléfono " + (i + 1) + ": "));
            p.add(tf);
            telefonosPanel.add(p);
        }
        telefonosPanel.revalidate();
        telefonosPanel.repaint();
    }

}
