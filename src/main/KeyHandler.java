package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/// main.KeyHandler, esta clase nos permitara detectar los comandos del teclado
public class KeyHandler implements KeyListener {

    /// Creamos Booleanos
    public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed;

    @Override // Esta funcion es inecesaria para el juego
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode(); // get.KeyCode, nos regresa el numero de la tecla presionada en codigo ASCII

        // Si el usuario toca la tecla "W" (VK_W = constante con el codigo de la W)
        if (code == KeyEvent.VK_W){
            upPressed = true;
        }

        // Si el usuario toca la tecla "S" (VK_S = constante con el codigo de la S)
        if (code == KeyEvent.VK_S){
            downPressed = true;
        }

        // Si el usuario toca la tecla "A" (VK_A = constante con el codigo de la A)
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }

        // Si el usuario toca la tecla "D" (VK_D = constante con el codigo de la D)
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }

        // Si el usuario toca Espacio (VK_SPACE = constante con el codigo del SPACE)
        if (code == KeyEvent.VK_SPACE) {
            if (pausePressed) {
                pausePressed = false;
            } else {
                pausePressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}