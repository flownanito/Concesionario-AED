
import Models.Camion;
import DAO.CamionDAO;
import Utils.Validador;

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
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;

        tfId = new JTextField(12);
        tfId.setEditable(true);

        // Marca / Modelo
        cbMarca = new JComboBox<>(DataStore.MARCAS_MODELOS.keySet().toArray(new String[0]));
        cbModelo = new JComboBox<>();
        cbMarca.addActionListener(e -> actualizarModelos());
        actualizarModelos(); // inicializar modelos

        // Colores (ejemplo)
        cbColor = new JComboBox<>(new String[]{"Blanco", "Negro", "Rojo", "Azul", "Gris", "Verde"});

        tfPrecioVenta = new JTextField(10);
        tfMatricula = new JTextField(10);

        form.add(new JLabel("ID camión:"), c);
        c.gridx = 1;
        form.add(tfId, c);
        c.gridx = 1;
        JButton btnBorrarCliente = new JButton("🗑️");
        c.gridx = 2;
        form.add(btnBorrarCliente, c);
        /*        btnBorrarCamion.addActionListener(e -> {
            String id = tfId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de cliente para borrar.");
                return;
            }
         */

        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Marca:"), c);
        c.gridx = 1;
        form.add(cbMarca, c);

        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Modelo:"), c);
        c.gridx = 1;
        form.add(cbModelo, c);

        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Color:"), c);
        c.gridx = 1;
        form.add(cbColor, c);
        c.gridx = 1;
        JButton btnActualizarColor = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarColor, c);
        btnActualizarColor.addActionListener(e -> {
            String id = tfId.getText().trim();
            String nuevoColor = (String) cbColor.getSelectedItem();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de camión para actualizar.");
                return;
            }

            boolean ok = CamionDAO.actualizarColor(id, nuevoColor);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Modelo actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del camión.");
            }
        });

        //Apartado de precio
        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Precio venta:"), c);
        c.gridx = 1;
        form.add(tfPrecioVenta, c);
        c.gridx = 1;
        JButton btnActualizarPrecio = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarPrecio, c);
        btnActualizarPrecio.addActionListener(e -> {
            String id = tfId.getText().trim();
            String textoPrecio = tfPrecioVenta.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de camión para actualizar.");
                return;
            }

            if (textoPrecio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe introducir un nuevo precio de venta.");
                return;
            }

            double nuevoPrecio;
            try {
                nuevoPrecio = Double.parseDouble(textoPrecio);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número válido (usa punto decimal).");
                return;
            }

            if (nuevoPrecio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor que 0.");
                return;
            }

            boolean ok = CamionDAO.actualizarPrecio(id, nuevoPrecio);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Precio actualizado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del camión.");
            }
        });

        c.gridx = 0;
        c.gridy++;
        form.add(new JLabel("Matricula:"), c);
        c.gridx = 1;
        form.add(tfMatricula, c);
        c.gridx = 1;
        JButton btnActualizarMatricula = new JButton("✎");
        c.gridx = 2;
        form.add(btnActualizarMatricula, c);
        btnActualizarMatricula.addActionListener(e -> {
            String id = tfId.getText().trim();
            String nuevoMatricula = tfMatricula.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe existir un ID de camión para actualizar.");
                return;
            }

            if (!nuevoMatricula.matches("^[0-9]{4}\\s[A-Z]{3}$")) {
                JOptionPane.showMessageDialog(this, "Matricula no valida. Ej(1234 ABC).");
                return;
            }

            boolean ok = CamionDAO.actualizarMatricula(id, nuevoMatricula);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Matricula actualizada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar. Verifique el ID del camión.");
            }
        });

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

            String matricula = tfMatricula.getText().trim();
            String precioStr = tfPrecioVenta.getText().trim();

            // Validaciones
            if (!Validador.validarMatricula(matricula)) {
                JOptionPane.showMessageDialog(this, "Matrícula no válida. Debe tener 4 números, un espacio y 3 letras mayúsculas (ej: 1234 ABC).");
                return;
            }

            /*if (!Validador.validarPrecio(precioStr)) {
                JOptionPane.showMessageDialog(this, "Precio no válido. Usa formato numérico con punto decimal (ej: 100.00).");
                return;
            }*/
            // Si todo es correcto
            Camion cobj = new Camion();
            cobj.setId(tfId.getText().trim());
            cobj.setMarca((String) cbMarca.getSelectedItem());
            cobj.setModelo((String) cbModelo.getSelectedItem());
            cobj.setColor((String) cbColor.getSelectedItem());
            cobj.setMatricula(matricula);
            cobj.setPrecioVenta(Double.parseDouble(precioStr));

            //Guarda en la base datos
            boolean ok = CamionDAO.insertarCamion(cobj);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Camión guardado en la base de datos correctamente: " + cobj.getId_camion());
                // limpiar campos
                tfId.setText("");
                tfMatricula.setText("");
                tfPrecioVenta.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el camión en la base de datos.");
            }

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
            for (String m : modelos) {
                cbModelo.addItem(m);
            }
        }
    }
}
