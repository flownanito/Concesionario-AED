

import Models.Camion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CamionForm extends JDialog {
    private JTextField tfId, tfPrecioVenta, tfMatricula;
    private JComboBox<String> cbMarca, cbModelo;
    private JComboBox<String> cbColor;

    public CamionForm(Frame owner) {
        super(owner, "Generar Camión", true);
        setSize(500, 320);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.anchor = GridBagConstraints.WEST;
        c.gridx=0; c.gridy=0;

        tfId = new JTextField(12);
        tfId.setEditable(false);

        // Marca / Modelo
        cbMarca = new JComboBox<>(DataStore.MARCAS_MODELOS.keySet().toArray(new String[0]));
        cbModelo = new JComboBox<>();
        cbMarca.addActionListener(e -> actualizarModelos());
        actualizarModelos(); // inicializar modelos

        // Colores (ejemplo)
        cbColor = new JComboBox<>(new String[]{"Blanco","Negro","Rojo","Azul","Gris","Verde"});

        tfPrecioVenta = new JTextField(10);
        tfMatricula = new JTextField(10);

        form.add(new JLabel("ID camión:"), c); c.gridx=1; form.add(tfId,c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Marca:"), c); c.gridx=1; form.add(cbMarca,c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Modelo:"), c); c.gridx=1; form.add(cbModelo,c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Color:"), c); c.gridx=1; form.add(cbColor,c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Precio venta:"), c); c.gridx=1; form.add(tfPrecioVenta,c);
        c.gridx=0; c.gridy++;
        form.add(new JLabel("Matricula:"), c); c.gridx=1; form.add(tfMatricula,c);

        // Botón generar ID (simula que la BD lo generaría)
        JButton btnGenerarId = new JButton("Generar ID (según marca/modelo)");
        btnGenerarId.addActionListener(e -> {
            String marca = (String) cbMarca.getSelectedItem();
            String modelo = (String) cbModelo.getSelectedItem();
            if (marca == null || modelo == null) {
                JOptionPane.showMessageDialog(this, "Selecciona marca y modelo");
                return;
            }
            String id = DataStore.generarIdCamion(marca, modelo);
            tfId.setText(id);
        });

        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar camión");
        JButton cancelar = new JButton("Cerrar");
        botones.add(btnGenerarId);
        botones.add(guardar);
        botones.add(cancelar);

        guardar.addActionListener(e -> {
            if (tfId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Genera el ID del camión primero.");
                return;
            }
            Camion cobj = new Camion();
            cobj.setId(tfId.getText().trim());
            cobj.setMarca((String)cbMarca.getSelectedItem());
            cobj.setModelo((String)cbModelo.getSelectedItem());
            cobj.setColor((String)cbColor.getSelectedItem());
            cobj.setMatricula(tfMatricula.getText().trim());
            try {
                double p = Double.parseDouble(tfPrecioVenta.getText().trim());
                cobj.setPrecioVenta(p);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio no válido. Introduce número.");
                return;
            }
            DataStore.CAMIONES.add(cobj);
            JOptionPane.showMessageDialog(this, "Camión añadido: " + cobj.getId());
            // limpiar para nuevo
            tfId.setText("");
            tfMatricula.setText("");
            tfPrecioVenta.setText("");
        });

        cancelar.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void actualizarModelos() {
        String marca = (String) cbMarca.getSelectedItem();
        cbModelo.removeAllItems();
        if (marca != null) {
            List<String> modelos = DataStore.MARCAS_MODELOS.get(marca);
            for (String m : modelos) cbModelo.addItem(m);
        }
    }
}
