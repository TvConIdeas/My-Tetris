package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/*
    En Tetris, cada figura se compone de 4 bloques.
    En esta clase vamos a darle vida a cada bloque
    y combinarlos para hacer "Tetrominos"
 */
public class Block extends Rectangle {

    /// Atributos
    public int x,y; // Posicion
    public static final int SIZE = 30; // 30x30 cada Bloque
    public Color c; // Color a eleguir

    /// Constructor, solo recibe el color
    public Block(Color c){
        this.c = c;
    }

    /// Metodo Draw
    public void draw(Graphics2D g2){
        int margin = 2; // Con esto cambiamos su diseño, sin alterar sus posiciones X e Y
        g2.setColor(c); // Seteamos Color
        g2.fillRect(x+margin,y+margin,SIZE-(margin*2),SIZE-(margin*2)); // Dibujamos el Bloque
    }

}
