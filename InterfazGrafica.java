import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InterfazGrafica {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            crearYMostrarGUI();
        });
    }

    private static void crearYMostrarGUI() {
        JFrame ventana = new JFrame("Compilador -> Generador de Secuencia para Unity");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 800);
        ventana.setLocationRelativeTo(null);

        JTextArea areaDeTextoEntrada = new JTextArea();
        areaDeTextoEntrada.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaDeTextoEntrada.setText(
            "Robot r1\n\n" +
            "r1.repetir=2 {\n" +
            "  r1.base=180\n" +
            "  r1.velocidad=1\n" +
            "  r1.cuerpo=90\n" +
            "  r1.velocidad=2\n" +
            "  r1.garra=45\n" +
            "  r1.velocidad=3\n" +
            "  r1.garra=0\n" +
            "  r1.cuerpo=0\n" +
            "  r1.base=0\n" +
            "}"
        );
        
        JScrollPane panelDeScrollEntrada = new JScrollPane(areaDeTextoEntrada);
        TextLineNumber tln = new TextLineNumber(areaDeTextoEntrada);
        panelDeScrollEntrada.setRowHeaderView(tln);
        
        DefaultTableModel modeloTabla = new DefaultTableModel(new String[]{"Op", "Dir1", "Dir2", "Res"}, 0);
        JTable tablaResultados = new JTable(modeloTabla);
        
        JTextArea areaDeTextoAnalisis = new JTextArea();
        areaDeTextoAnalisis.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaDeTextoAnalisis.setEditable(false);
        
        JButton botonAnalizar = new JButton("Analizar y Generar 'secuencia'");
        botonAnalizar.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        botonAnalizar.addActionListener(e -> {
            modeloTabla.setRowCount(0);
            areaDeTextoAnalisis.setText("");
            String codigoFuente = areaDeTextoEntrada.getText();
            
            try {
                areaDeTextoAnalisis.append("--- FASE 1: ANALISIS LEXICO ---\n");
                List<Token> tokens = Proyecto.analizarLexico(codigoFuente);
                for (Token token : tokens) {
                    areaDeTextoAnalisis.append(token.toString() + "\n");
                }
                
                areaDeTextoAnalisis.append("\n--- FASE 2: ANALISIS SINTACTICO ---\n");
                List<Instruccion> programa = Proyecto.parsear(codigoFuente);
                areaDeTextoAnalisis.append("Sintaxis correcta.\n");
                
                areaDeTextoAnalisis.append("\n--- FASE 3: GENERANDO CODIGO INTERMEDIO ---\n");
                List<Operacion> operaciones = Proyecto.generarCodigoIntermedio(programa);
                for (Operacion op : operaciones) {
                    modeloTabla.addRow(new Object[]{op.op, op.dir1, op.dir2, op.res});
                }
                areaDeTextoAnalisis.append("Tabla de operaciones generada.\n");

                areaDeTextoAnalisis.append("\n--- FASE 4: GENERANDO ARCHIVO PARA UNITY ---\n");
                Proyecto.generarSecuenciaParaUnity(codigoFuente);
                areaDeTextoAnalisis.append("Archivo 'secuencia.txt' generado correctamente.\n\n");
                areaDeTextoAnalisis.append("¡PROCESO COMPLETO! Ya puedes ejecutar la secuencia desde Unity.");

                JOptionPane.showMessageDialog(ventana, "¡Éxito! 'secuencia' generada..", "Proceso Completado", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                // ... (manejo de errores)
            }
        });

        // --- ESTRUCTURA DE LA VENTANA ---
        JPanel panelEntrada = new JPanel(new BorderLayout());
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Código de Entrada"));
        panelEntrada.add(panelDeScrollEntrada);
        
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Tabla de Operaciones (Código Intermedio)"));
        panelTabla.add(new JScrollPane(tablaResultados));

        JPanel panelAnalisis = new JPanel(new BorderLayout());
        panelAnalisis.setBorder(BorderFactory.createTitledBorder("Resultado del Análisis"));
        panelAnalisis.add(new JScrollPane(areaDeTextoAnalisis));
        
        JPanel panelSur = new JPanel(new BorderLayout(10,10));
        panelSur.add(botonAnalizar, BorderLayout.CENTER);
        
        JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelTabla, panelAnalisis);
        splitPaneVertical.setResizeWeight(0.6);
        JSplitPane splitPanePrincipal = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelEntrada, splitPaneVertical);
        splitPanePrincipal.setResizeWeight(0.4);
        
        ventana.add(splitPanePrincipal, BorderLayout.CENTER);
        ventana.add(panelSur, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }
}