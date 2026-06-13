# 🥾 Brazo Robótico — Control con Java + Assembly

Proyecto de control de un brazo robótico desarrollado con **Java** e integrado con código **Assembly (x86)**. Incluye una interfaz gráfica interactiva que permite enviar secuencias de movimiento al brazo mediante instrucciones de bajo nivel. Proyecto de arquitectura de computadoras y sistemas embebidos.

---

## 🧰 Tecnologías utilizadas

- **Java** — lógica principal e interfaz gráfica (Swing)
- **Assembly x86 (TASM)** — control de bajo nivel del hardware
- **TASM / TLINK** — ensamblador y enlazador
- **Git LFS** — manejo de archivos binarios (.exe, .obj, .com)

---

## 📁 Estructura del proyecto

```
Brazo-Robotico-Unity/
├── Interfaz-Grafica/          # Interfaz visual en Java (VS Code)
├── TASM/                     # Archivos del ensamblador TASM
├── InterfazGrafica.java      # Clase principal de la GUI
├── EjecutarProyecto.java     # Punto de entrada del proyecto
├── Proyecto.java             # Lógica del proyecto
├── Operacion.java            # Definición de operaciones
├── Comando.java              # Comandos del brazo
├── ComandoMotor.java         # Control de motores
├── ComandoVelocidad.java     # Control de velocidad
├── Token.java                # Tokenizador de instrucciones
├── TextLineNumber.java       # Numeración de líneas en editor
├── cod.asm                   # Código Assembly principal
├── monitor.asm               # Monitor de comunicación
├── comandos.txt              # Archivo de comandos de ejemplo
├── secuencia.txt             # Secuencias de movimiento
└── compilar.bat              # Script de compilación
```

---

## ⚙️ Instalación y uso

### Requisitos previos
- **Java JDK 8+**
- **TASM** (Turbo Assembler) para compilar el Assembly
- Sistema operativo Windows (para ejecutar los binarios .COM/.EXE)

### Compilar el Assembly

```bat
compilar.bat
```

Este script usa TASM y TLINK para ensamblar `cod.asm` y `monitor.asm`.

### Ejecutar la interfaz Java

```bash
javac *.java
java EjecutarProyecto
```

---

## ✨ Características principales

- **Interfaz gráfica en Java Swing** con editor de instrucciones y numeración de líneas
- **Tokenizador propio** para parsear y validar secuencias de comandos
- **Control de motores** mediante instrucciones de velocidad y movimiento
- **Código Assembly x86** que se ejecuta directamente para controlar el hardware
- **Monitor de comunicación** entre Java y el módulo Assembly
- Soporte para cargar y ejecutar secuencias desde archivos `.txt`

---

## 📚 Conceptos aplicados

- Arquitectura de computadoras (x86)
- Programación de bajo nivel con Assembly
- Interfaz hombre-máquina (HMI)
- Comunicación entre lenguajes de alto y bajo nivel
- Diseño de compiladores / intérpretes (tokenización)

---

## 👤 Autor

**Xchel25** — [github.com/Xchel25](https://github.com/Xchel25)
