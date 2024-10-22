package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        JFrame window = new JFrame("My Tetris"); // Titulo
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Botor Cerrar
        window.setResizable(false); // Ajustable No

        /// GamePanel
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();  

        window.setLocationRelativeTo(null); // Se ubicara en el centro de la pantalla
        window.setVisible(true); // Visible

        gp.startGame(); // Iniciamos el juego
    }
}