import Models.Cliente;

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
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = 0;

        tfId = new JTextField(15);
        tfId.setEditable(false);
        tfId.setText(DataStore.genererIdCliente());

        tfNif = new JTextField(20);
        tfNombre = new JTextField(20);
        tfDireccion = new JTextField(20);
        tfCiudad = new JTextField(20);

        form.add(new JLabel("ID cliente:"), c); c.gridx=1; form.add(tfId, c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("NIF:"), c); c.gridx=1; form.add(tfNif, c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Nombre completo:"), c); c.gridx=1; form.add(tfNombre, c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Dirección:"), c); c.gridx=1; form.add(tfDireccion, c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Ciudad residencia:"), c); c.gridx=1; form.add(tfCiudad, c);

        // Teléfonos dinámicos
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Telefonos (cantidad):"), c);
        c.gridx=1;
        Integer[] nums = new Integer[10];
        for(int i=0;i<10;i++) nums[i]=i+1;
        cbNumTelefonos = new JComboBox<>(nums);
        cbNumTelefonos.setSelectedIndex(0);
        JButton btnGenerarTelefonos = new JButton("Generar campos");
        JPanel telControl = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
        telControl.add(cbNumTelefonos);
        telControl.add(btnGenerarTelefonos);
        form.add(telControl, c);

        c.gridx=0; c.gridy++;
        c.gridwidth=2;
        telefonosPanel = new JPanel();
        telefonosPanel.setLayout(new BoxLayout(telefonosPanel, BoxLayout.Y_AXIS));
        telefonosPanel.setBorder(BorderFactory.createTitledBorder("Teléfonos"));
        telefonosPanel.setPreferredSize(new Dimension(420,120));
        JScrollPane sp = new JScrollPane(telefonosPanel);
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
            Cliente cliente = new Cliente();
            cliente.setId(tfId.getText().trim());
            cliente.setNif(tfNif.getText().trim());
            cliente.setNombre(tfNombre.getText().trim());
            cliente.setDireccion(tfDireccion.getText().trim());
            cliente.setCiudad(tfCiudad.getText().trim());
            java.util.List<String> telefonos = new ArrayList<>();
            for (JTextField f : telefonoFields) {
                String t = f.getText().trim();
                if (!t.isEmpty()) telefonos.add(t);
            }
            cliente.setTelefonos(telefonos);
            DataStore.CLIENTES.add(cliente);
            JOptionPane.showMessageDialog(this, "Cliente guardado en memoria: " + cliente.getId());
            // Generar nuevo id para el siguiente cliente
            tfId.setText(DataStore.genererIdCliente());
            // limpiar campos
            tfNif.setText(""); tfNombre.setText(""); tfDireccion.setText(""); tfCiudad.setText("");
            actualizarTelefonos(0);
        });

        cancelar.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void actualizarTelefonos(int n) {
        telefonosPanel.removeAll();
        telefonoFields.clear();
        for(int i=0;i<n;i++){
            JTextField tf = new JTextField(20);
            telefonoFields.add(tf);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(new JLabel("Teléfono " + (i+1) + ": "));
            p.add(tf);
            telefonosPanel.add(p);
        }
        telefonosPanel.revalidate();
        telefonosPanel.repaint();
    }
}
