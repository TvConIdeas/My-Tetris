package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    /// Tamaño de la clase
    public static final int ANCHO = 1280;
    public static final int ALTO = 720;

    /// Atributos importantes
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm;

    /// Constructor
    public GamePanel() {
        // Configuraciones del panel
        this.setPreferredSize(new Dimension(ANCHO, ALTO));
        this.setBackground(new Color(11, 25, 44));
        this.setLayout(null);

        // Detectar el teclado
        this.addKeyListener(new KeyHandler());
        this.setFocusable(true); // Se concentra en el teclado

        pm = new PlayManager();
    }

    /// Iniciar Hilo
    public void startGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        /// Bucle del juego, Metodo Delta
        double drawInterval = 1000000000/FPS; // Es el intervalo de tiempo en nanosegundos entre cada actualizacion de pantalla
        double delta = 0; // Sirve como una variable para acumular el tiempo
        double lastTime = System.nanoTime(); // Agarra el tiempo actual cuando se inicia el programa
        long currentTime; // Variable para guardar el tiempo actual en la funcion while

        while(gameThread != null) {
            currentTime = System.nanoTime(); // Conseguimos el tiempo actual
            delta += (currentTime - lastTime) / drawInterval; // Guardamos en delta el tiempo transcurido
            lastTime = currentTime; // Traspasamos el tiempo actual al ultimo tiempo

            if (delta >= 1){ // Si delta es mayor a uno, actualiza y redibuja la pantalla
                update();
                repaint(); // Llama a PaintComponent
                delta--;
            }
        }
    }

    /// Metodo Update
    private void update(){
        if (KeyHandler.pausePressed == false && pm.gameOver == false){
            pm.update();
        }
    }

    /// Metodo Draw
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }


}
