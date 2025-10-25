package Utils;

import java.util.regex.*;

public class Validador {

    // Valida NIF: 8 números seguidos de una letra mayúscula
    public static boolean validarNIF(String nif) {
        if (nif == null) return false;
        Pattern patron = Pattern.compile("^[0-9]{8}[A-Z]$");
        Matcher mat = patron.matcher(nif.trim());
        return mat.matches();
    }

    // Valida teléfono: exactamente 9 números
    public static boolean validarTelefono(String telefono) {
        if (telefono == null) return false;
        Pattern patron = Pattern.compile("^[0-9]{9}$");
        Matcher mat = patron.matcher(telefono.trim());
        return mat.matches();
    }

    // Valida matrícula: 4 números, un espacio y 3 letras mayúsculas
    public static boolean validarMatricula(String matricula) {
        if (matricula == null) return false;
        Pattern patron = Pattern.compile("^[0-9]{4}\\s[A-Z]{3}$");
        Matcher mat = patron.matcher(matricula.trim());
        return mat.matches();
    }

    // Valida precio: formato monetario con hasta 2 decimales (ej. 100 o 100.00)
    /*public static boolean validarPrecio(String precio) {
        if (precio == null) return false;
        Pattern patron = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
        Matcher mat = patron.matcher(precio.trim());
        return mat.matches();
    }*/
}
