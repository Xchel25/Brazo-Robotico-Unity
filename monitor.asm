.MODEL SMALL
.STACK 100h
.DATA
    ; Direcciones basadas en nuestra conexión (A8=CS, A0=C/D)
    UART_DATA EQU 0100h
    UART_CMD  EQU 0101h
    PPI_PORTA EQU 00h ; Suponiendo que el 8255A responde a esta dirección base
    PPI_CTRL  EQU 06h

.CODE
MAIN PROC
    ; Inicializar el PPI (8255A) para que todos los puertos sean de salida
    MOV DX, PPI_CTRL
    MOV AL, 80h ; Modo 0, todos los puertos como salida
    OUT DX, AL

    ; Inicializar el UART (8251A)
    MOV DX, UART_CMD
    MOV AL, 40h ; Reset por software
    OUT DX, AL
    MOV AL, 4Eh ; Modo: 8 bits, sin paridad, 1 stop bit, x16 clock
    OUT DX, AL
    MOV AL, 15h ; Comando: Habilitar Tx y Rx
    OUT DX, AL

BUCLE_ESPERA:
    ; Esperar a que llegue un byte por el puerto serial
    MOV DX, UART_CMD
    IN AL, DX
    TEST AL, 02h ; Revisa el bit RxRDY (Receiver Ready)
    JZ BUCLE_ESPERA ; Si no hay nada, sigue esperando

    ; Si llegó un dato, leerlo
    MOV DX, UART_DATA
    IN AL, DX

    ; Enviar el dato recibido directamente al Puerto A del PPI
    MOV DX, PPI_PORTA
    OUT DX, AL

    JMP BUCLE_ESPERA ; Volver a esperar el siguiente dato

MAIN ENDP
END MAIN