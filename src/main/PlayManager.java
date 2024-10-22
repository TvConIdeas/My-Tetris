package main;

import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/// Clase PlayManager
///
/// Se encargara de dibujar el area de juego
/// Manegar las "Figuras" de Tetris
/// Funciones basicas del Gameplay (Borrar Lineas, puntaje, etc.)
public class PlayManager {

    /// Zona Principal de Juego
    final int ANCHO = 360;
    final int ALTO = 600;
    public static int leftX;
    public static int rightX;
    public static int topY;
    public static int bottomY;

    /// Mino (Actual)
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;

    /// Mino (Siguiente)
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>(); // Pondremos a los Minos staticos aca

    /// Otros
    public static int dropInterval = 40; // El Mino caera cada 60 FPS, es decir un segundo
    boolean gameOver;

    /// Efectos
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    /// Constructor
    public PlayManager(){

        // Area principal de juego
        leftX = (GamePanel.ANCHO/2) - (ANCHO/2); // 1280/2 - 360/2 = 460
        rightX = leftX + ANCHO; // 460 + 360 = 820
        topY = 50; // 50 pixeles abajo del tope
        bottomY = topY + ALTO; // 50 + 600 = 650

        // Ubicamos el inicio de los MINO
        MINO_START_X = leftX + (ANCHO/2) - Block.SIZE;
        MINO_START_Y = topY + Block.SIZE;

        // Ubicamos el siguiente Mino en la ventana Next
        NEXTMINO_X = rightX + 185;
        NEXTMINO_Y = topY + 500;

        // Iniciamos el Mino inicial
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X,MINO_START_Y);

        // Eleguimos y ubicamos el siguiente Mino
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);
    }

    /// Seleccionar Mino Aleatorio
    private Mino pickMino(){

        Mino mino = null; // Creamos un Mino Null

        int i = new Random().nextInt(7); // Generamos un numero random entre 0 y 6

        switch (i){ // Depende el numero, creamos cierto Mino
            case 0: mino = new Mino_L1();break;
            case 1: mino = new Mino_L2();break;
            case 2: mino = new Mino_Square();break;
            case 3: mino = new Mino_Bar();break;
            case 4: mino = new Mino_T();break;
            case 5: mino = new Mino_Z1();break;
            case 6: mino = new Mino_Z2();break;
        }
        return mino; // Lo retornamos
    }

    /// Metodo Update
    public void update(){

        // Si el Mino actual esta activo
        if (currentMino.active == false){
            // Si no esta activo, lo ubicamos en el ArrayList
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // Checkear si es Game Over
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                // Esto significa que el MinoActual, esta colisionando con el X e Y del inicio
                gameOver = true;
            }

            currentMino.deactivating = false;

            // Luego reemplazamos el Mino actual por el Siguiente
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X,MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);

            // Cuando el Mino se desactive, checkeamos si podemos borrar lineas
            checkDelete();

        }else {
            currentMino.update();
        }
    }

    /// Metodo Ckeck Delete
    ///
    /// Lo que tenemos que lograr, es contar la cantidad de bloques en la linea
    /// y si esta igual a 12 (limite) eliminarla
    private void checkDelete(){

        int x = leftX;
        int y = topY;
        int blockCount = 0;

        while (x < rightX && y < bottomY){

            for(int i = 0; i < staticBlocks.size(); i++){
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    // Si en esa posicion hay un bloque estatico, aumentamos el contador
                    blockCount++;
                }
            }

            x+= Block.SIZE; // Nos movemos por Bloques

            if (x == rightX){ // Cuando llegue al final de la linea

                if(blockCount == 12){ // Si al terminar la linea, el contador es igual a 12, significa que debemos eliminar la linea

                    effectCounterOn = true; // Enviamos que hay una linea para borrar
                    effectY.add(y); // Enviamos la linea de la linea a borrar

                    for(int i = staticBlocks.size()-1; i > -1; i--) {
                        // Eliminamos todos los bloques de esa linea
                        if (staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }

                    // Una vez eliminada la linea, debemos desplazar las filas de arriba, hacia abajo
                    for (int i = 0 ; i < staticBlocks.size() ; i++){
                        if (staticBlocks.get(i).y < y){
                            // Si un bloque se encuentra en esa linea, se lo mueve un bloque hacia abajo
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0; // Reiniciamos el contador
                x = leftX; // Empezamos desde la izquierda
                y += Block.SIZE; // Nos movemos por Bloques
            }
        }

    }

    /// Metodo Draw
    public void draw(Graphics2D g2){

        /// Dibujamos el Area de Juego
        g2.setColor(Color.WHITE); // Le damos Color
        g2.setStroke(new BasicStroke(4f)); // El ancho del trazo
        g2.drawRect(leftX - 4, topY-4, ANCHO+8, ALTO+8); // Dibujar el Rectangulo
        // Se le resta 4 y se le suma 8, para que la colision quede del lado de adentro del area.

        /// Dibujamos una segunda ventana (La sala de espera de la figuras)
        int x = rightX + 100;
        int y = bottomY - 200;
        g2.drawRect(x,y,200,200);

        /// Le agregamos texto
        g2.setFont(new Font("Arial",Font.PLAIN,30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);
        /* g2.setFont(new Font("Arial", Font.PLAIN, 30));
        Cambia la fuente utilizada por "Arial", con el estilo "PLAIN" (sin negritas o cursivas) y
        un tamaño de 30 píxeles. Afectando a texto posteriores

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Activa el "antialiasing" para el texto, suavizando los bordes del texto y verse mejor. */

        /// Dibujar el Mino Actual
        if (currentMino != null){ // Primero nos fijamos de que no sea nulo, para evitar errores
            currentMino.draw(g2);
        }

        /// Dibujar el siguiento Mino
        nextMino.draw(g2);

        /// Dibujamos los Blockes Estaticos (ArrayList)
        for (int i = 0; i < staticBlocks.size(); i++){
            staticBlocks.get(i).draw(g2);
        }

        /// Dibujar efecto de eliminar linea
        if (effectCounterOn){

            // Hacemos que la linea a borrar se ponga rojo
            effectCounter++;
            g2.setColor(Color.RED);
            for(int i = 0; i < effectY.size(); i++)
            {
                g2.fillRect(leftX,effectY.get(i),ANCHO,Block.SIZE);
            }

            // Cuando termine de marcar la linea, se borra y se regresa tod0 a 0
            if (effectCounter == 10){
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        /// Dibujar el Pausa o Game Over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameOver){
            g2.setColor(Color.red);
            x = leftX + 25;
            y = topY + 320;
            g2.drawString("GAME OVER", x, y);
        }
        else if(KeyHandler.pausePressed){
            x = leftX + 70;
            y = topY + 320;
            g2.drawString("PAUSED", x, y);
        }

        // Dibujar el titulo del juego
        x = 35;
        y = topY + 325;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 60));
        g2.drawString("My Tetris :p", x, y);

    }

}
