

import Models.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataStore {
    // Simula tablas en memoria
    public static final List<Cliente> CLIENTES = new ArrayList<>();
    public static final List<Camion> CAMIONES = new ArrayList<>();
    public static final List<Revision> REVISIONES = new ArrayList<>();

    // Generador de IDs simples para clientes
    private static final AtomicInteger CLIENTE_SEQ = new AtomicInteger(1);

    // Marcas y modelos permitidos (5 marcas, cada una 3 modelos)
    public static final Map<String, List<String>> MARCAS_MODELOS = new LinkedHashMap<>();
    // Contadores por clave (inicialMarca+inicialModelo) -> next number
    private static final Map<String, Integer> SECUENCIAS = new HashMap<>();

    static {
        // Rellenar marcas y modelos reales de ejemplo
        MARCAS_MODELOS.put("Mercedes", Arrays.asList("Actros", "Atego", "Arocs"));
        MARCAS_MODELOS.put("Scania", Arrays.asList("R-series", "S-series", "P-series"));
        MARCAS_MODELOS.put("Volvo", Arrays.asList("FH", "FM", "FMX"));
        MARCAS_MODELOS.put("Iveco", Arrays.asList("Stralis", "S-Way", "Eurocargo"));
        MARCAS_MODELOS.put("MAN", Arrays.asList("TGX", "TGS", "TGM"));

        // inicializar contadores a 1
        for (Map.Entry<String, List<String>> e : MARCAS_MODELOS.entrySet()) {
            String marca = e.getKey();
            for (String modelo : e.getValue()) {
                String key = keyFrom(marca, modelo);
                SECUENCIAS.put(key, 1);
            }
        }
    }

    public static String genererIdCliente() {
        return String.format("C%04d", CLIENTE_SEQ.getAndIncrement());
    }

    private static String keyFrom(String marca, String modelo) {
        char a = Character.toUpperCase(marca.charAt(0));
        char b = Character.toUpperCase(modelo.charAt(0));
        return "" + a + b;
    }

    // Genera id_camion tipo "MM001", "MQ002" etc. Mantiene secuencias por pareja marca+modelo.
    public static synchronized String generarIdCamion(String marca, String modelo) {
        String key = keyFrom(marca, modelo);
        int seq = SECUENCIAS.getOrDefault(key, 1);
        String id = String.format("%s%03d", key, seq);
        SECUENCIAS.put(key, seq + 1);
        return id;
    }

    // Buscar revisión por id cliente y id camion
    public static Optional<Revision> buscarRevision(String idCliente, String idCamion) {
        return REVISIONES.stream()
            .filter(r -> r.getIdCliente().equalsIgnoreCase(idCliente) && r.getIdCamion().equalsIgnoreCase(idCamion))
            .findFirst();
    }
}
