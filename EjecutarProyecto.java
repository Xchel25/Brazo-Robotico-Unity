import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EjecutarProyecto {

    public static void main(String[] args) {
        
        System.out.println("Preparando el comando para ejecutar DOSBox...");

        try {
            // ProcessBuilder es la forma moderna y recomendada para ejecutar comandos externos.
            // Primero, creamos una lista de strings para el comando y cada uno de sus argumentos.

            List<String> comando = new ArrayList<>();

            // Argumento 1: La ruta completa al ejecutable.
            // ¡Recuerda usar dobles diagonales (\\) en las rutas de Windows!
            comando.add("C:\\Program Files (x86)\\DOSBox-0.74-3\\DOSBox.exe");

            // Argumento 2 y 3: -c "mount c C:\ProyectoAutomatas"
            comando.add("-c");
            comando.add("mount c C:\\ProyectoAutomatas");

            // Argumento 4 y 5: -c "c:"
            comando.add("-c");
            comando.add("c:");

            // Argumento 6 y 7: -c "compilar.bat"
            comando.add("-c");
            comando.add("compilar.bat");


            // Creamos la instancia de ProcessBuilder con la lista de comandos.
            ProcessBuilder pb = new ProcessBuilder(comando);

            // Imprimimos el comando que vamos a ejecutar para verificar que esté correcto.
            System.out.println("Ejecutando: " + pb.command());

            // Iniciamos el proceso. Esto es lo que efectivamente lanza DOSBox.
            pb.start();

            System.out.println("El comando para iniciar DOSBox se ha enviado correctamente.");

        } catch (IOException e) {
            // Este bloque se ejecuta si Java no puede encontrar el archivo o hay otro error.
            System.err.println("Ocurrió un error al intentar ejecutar el comando.");
            e.printStackTrace();
        }
    }
} 