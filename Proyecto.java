import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// --- ESTRUCTURAS DE DATOS ---
interface Instruccion {}
// Se le añade 'public' para que sean visibles desde otras clases
class Token {
    public String tipo; public String valor;
    Token(String tipo, String valor) { this.tipo = tipo; this.valor = valor; }
    @Override public String toString() { return String.format("[%s]: %s", tipo, valor); }
}
class Operacion {
    public String op; public String dir1; public String dir2; public String res;
    Operacion(String op, String dir1, String dir2, String res) { this.op = op; this.dir1 = dir1; this.dir2 = dir2; this.res = res; }
}
record ComandoMotor(String propiedad, float valor, int velocidad) implements Instruccion {}
record ComandoVelocidad(int valor) implements Instruccion {} // Usado para parsear
record BloqueRepetir(int veces, List<Instruccion> comandos) implements Instruccion {}

public class Proyecto {

    // Metodo principal que ahora es llamado por la GUI
    public static void generarSecuenciaParaUnity(String codigoFuente) throws IOException {
        String rutaProyecto = "C:\\Users\\yaxit\\OneDrive\\Documentos\\ProyectoAutomatas";
        String nombreArchivo = "secuencia.txt";
        
        List<Instruccion> programaParseado = parsear(codigoFuente);
        
        File archivoSecuencia = new File(rutaProyecto, nombreArchivo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSecuencia))) {
            boolean seEncontroRepetir = false;
            for (Instruccion instruccion : programaParseado) {
                if (instruccion instanceof BloqueRepetir bloque) {
                    seEncontroRepetir = true;
                    writer.write("repetir " + bloque.veces());
                    writer.newLine();
                    for (Instruccion cmd : bloque.comandos()) {
                        if (cmd instanceof ComandoMotor cmdMotor) {
                            writer.write(cmdMotor.propiedad() + " " + cmdMotor.valor() + " " + cmdMotor.velocidad());
                            writer.newLine();
                        }
                    }
                    writer.write("finrepetir");
                    writer.newLine();
                }
            }
            
            if (!seEncontroRepetir) {
                writer.write("repetir 1");
                writer.newLine();
                for (Instruccion instruccion : programaParseado) {
                    if (instruccion instanceof ComandoMotor cmdMotor) {
                         writer.write(cmdMotor.propiedad() + " " + cmdMotor.valor() + " " + cmdMotor.velocidad());
                         writer.newLine();
                    }
                }
                writer.write("finrepetir");
                writer.newLine();
            }
        }
        System.out.println("Archivo 'secuencia.txt' generado exitosamente.");
    }

    // ##### PARSER CORREGIDO Y FINAL #####
    // Ahora entiende secuencias con y sin bloque 'repetir' y en cualquier orden
    public static List<Instruccion> parsear(String codigoFuente) {
        List<Instruccion> programaParseado = new ArrayList<>();
        List<String> lineasComandos = List.of(codigoFuente.split("\n"));
        
        boolean tieneBloqueRepetir = lineasComandos.stream().anyMatch(linea -> linea.trim().toLowerCase().startsWith("r1.repetir"));

        if (tieneBloqueRepetir) {
            for (int i = 0; i < lineasComandos.size(); i++) {
                String linea = lineasComandos.get(i).trim().toLowerCase();
                if (linea.startsWith("r1.repetir")) {
                    int veces = Integer.parseInt(linea.split("=")[1].replace("{", "").trim());
                    List<Instruccion> comandosDelBloque = new ArrayList<>();
                    i++;
                    while (i < lineasComandos.size() && !lineasComandos.get(i).trim().equals("}")) {
                        String lineaBloque = lineasComandos.get(i).trim().toLowerCase();
                        if (lineaBloque.contains("=")) {
                            String[] partes = lineaBloque.split("=");
                            String propiedad = partes[0].split("\\.")[1].trim();
                            int valor = Integer.parseInt(partes[1].trim());
                            if (propiedad.equalsIgnoreCase("velocidad")) {
                                comandosDelBloque.add(new ComandoVelocidad(valor));
                            } else {
                                int velocidad = 1;
                                if (i + 1 < lineasComandos.size() && lineasComandos.get(i + 1).trim().toLowerCase().startsWith("r1.velocidad")) {
                                    velocidad = Integer.parseInt(lineasComandos.get(i + 1).trim().toLowerCase().split("=")[1].trim());
                                }
                                comandosDelBloque.add(new ComandoMotor(propiedad, valor, velocidad));
                            }
                        }
                        i++;
                    }
                    programaParseado.add(new BloqueRepetir(veces, comandosDelBloque));
                }
            }
        } else {
             List<ComandoMotor> comandosSimples = new ArrayList<>();
            int velocidadActual = 1;
             for (int i = 0; i < lineasComandos.size(); i++) {
                String linea = lineasComandos.get(i).trim().toLowerCase();
                if (linea.isEmpty() || linea.startsWith("robot")) continue;
                if (linea.contains("=")) {
                    String[] partes = linea.split("=");
                    String propiedad = partes[0].split("\\.")[1].trim();
                    float valor = Float.parseFloat(partes[1].trim());
                    if (propiedad.equalsIgnoreCase("velocidad")) {
                        velocidadActual = (int)valor;
                    } else {
                        comandosSimples.add(new ComandoMotor(propiedad, valor, velocidadActual));
                    }
                }
            }
            if (!comandosSimples.isEmpty()) {
                programaParseado.add(new BloqueRepetir(1, new ArrayList<>(comandosSimples)));
            }
        }
        return programaParseado;
    }

    // --- LÓGICA RESTAURADA PARA EL ANÁLISIS LÉXICO ---
    public static List<Token> analizarLexico(String codigoFuente) {
        List<Token> tokens = new ArrayList<>();
        String codigoParaAnalisis = codigoFuente.replace("\n", " ").replace("{", " { ").replace("}", " } ").replace("=", " = ");
        String[] palabras = codigoParaAnalisis.split("\\s+");
        for (String palabra : palabras) {
            if (palabra.isEmpty()) continue;
            if (palabra.startsWith("r1.")) {
                 String[] partes = palabra.split("\\.");
                 tokens.add(new Token("IDENTIFICADOR", partes[0]));
                 tokens.add(new Token("SIMBOLO", "."));
                 if(partes.length > 1) tokens.add(new Token("PARTE_ROBOT", partes[1]));
            } else if (palabra.equalsIgnoreCase("robot")) {
                tokens.add(new Token("PALABRA_RESERVADA", palabra));
            } else if (palabra.matches("-?[0-9]+(\\.[0-9]+)?")) {
                 tokens.add(new Token("NUMERO", palabra));
            } else if (palabra.matches("[.={}]")) {
                 tokens.add(new Token("SIMBOLO", palabra));
            } else {
                 tokens.add(new Token("DESCONOCIDO", palabra));
            }
        }
        tokens.add(new Token("FIN_DE_ARCHIVO", "EOF"));
        return tokens;
    }

    // --- LÓGICA RESTAURADA PARA LA TABLA DE OPERACIONES ---
    public static List<Operacion> generarCodigoIntermedio(List<Instruccion> programaParseado) {
        List<Operacion> operaciones = new ArrayList<>();
        operaciones.add(new Operacion("Cargar", "Robot", "", "r1"));
        for(Instruccion instruccion : programaParseado) {
            if (instruccion instanceof BloqueRepetir bloque) {
                 operaciones.add(new Operacion("Repetir", String.valueOf(bloque.veces()), "", ""));
                 for(Instruccion cmd : bloque.comandos()) {
                    if (cmd instanceof ComandoMotor cmdMotor) {
                        operaciones.add(new Operacion("Cargar", String.valueOf(cmdMotor.valor()), "", "ax"));
                        operaciones.add(new Operacion("Llamar", cmdMotor.propiedad(), "r1", ""));
                        operaciones.add(new Operacion("Velocidad", String.valueOf(cmdMotor.velocidad()), "", ""));
                    }
                 }
            }
        }
        return operaciones;
    }
}