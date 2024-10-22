package mino;

import main.KeyHandler;
import main.PlayManager;

import java.awt.*;

public class Mino {

    /// Atributos
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1; // Los Mino tienen 4 direcciones (1/2/3/4)
    boolean leftCollision, rightCollision, bottomCollision; // Booleanos que controlan las colisiones
    public boolean active = true; // Booleanos que controla si ya llego al final
    public boolean deactivating; // Booleanos que analiza si esta tocando el final
    int deactivateCounter = 0; // Contador para antes de que se ponga active en false

    /// Metodo Create
    public void create(Color c){
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    /// Metodos Extra
    public void setXY(int x, int y){}
    public void updateXY(int direction) {

        checkRotationCollision(); // Primero hacemos el Chekeo de colision con rotacion

        // Si no ocurre ninguna colision, entonces podemos hacer la rotacion
        if (leftCollision == false && rightCollision == false && bottomCollision == false){
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
    }

    /// Metodos de Rotacion
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}

    /// Metodos de Collision
    public void checkMovementCollision(){ // Collision cuando el Mino se mueve
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // Checkeamos la colision con bloques estaticos
        checkStaticBlockCollision();

        /// Checkear las colisiones
        // Pared Izquierda
        for (int i = 0; i < b.length; i++){
            if (b[i].x == PlayManager.leftX){
                leftCollision = true;
            }
        }

        // Pared Derecha
        for (int i = 0; i < b.length; i++){
            if (b[i].x + Block.SIZE == PlayManager.rightX){
                rightCollision = true;
            }
        }

        // Piso
        for (int i = 0; i < b.length; i++){
            if (b[i].y + Block.SIZE == PlayManager.bottomY){
                bottomCollision = true;
            }
        }
    }
    public void checkRotationCollision(){ // Collision cuando el Mino rota
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // Checkeamos la colision con bloques estaticos
        checkStaticBlockCollision();

        /// Checkear las colisiones
        // Pared Izquierda
        for (int i = 0; i < b.length; i++){
            if (tempB[i].x < PlayManager.leftX){
                leftCollision = true;
            }
        }

        // Pared Derecha
        for (int i = 0; i < b.length; i++){
            if (tempB[i].x + Block.SIZE > PlayManager.rightX){
                rightCollision = true;
            }
        }

        // Piso
        for (int i = 0; i < b.length; i++){
            if (tempB[i].y + Block.SIZE > PlayManager.bottomY){
                bottomCollision = true;
            }
        }
    }
    private void checkStaticBlockCollision(){

        // Escaneamos el ArrayList de Bloques Estaticos
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++){

            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // Colision con el Piso
            for (int ii = 0; ii < b.length; ii++){
                if (b[ii].y + Block.SIZE == targetY && b[ii].x == targetX){
                    // Si el bloque estatico esta abajo tuyo
                    bottomCollision = true;
                }
            }

            // Colision con la Izquierda
            for (int ii = 0; ii < b.length; ii++){
                if (b[ii].x - Block.SIZE == targetX && b[ii].y == targetY){
                    // Si el bloque estatico esta a la izquierda
                    leftCollision = true;
                }
            }

            // Colision con la Derecha
            for (int ii = 0; ii < b.length; ii++){
                if (b[ii].x + Block.SIZE == targetX && b[ii].y == targetY){
                    // Si el bloque estatico esta abajo tuyo
                    rightCollision = true;
                }
            }
        }
    }

    /// Metodo Deactivating (Permite el Slide)
    private void deactivating(){
        deactivateCounter++;

        // Esperamos 45 fotogramos antes de activarse de vuelta
        if (deactivateCounter == 45){
            deactivateCounter = 0;
            checkMovementCollision(); // Checkeamos si toca el fondo

            // Si aun toca el fondo despues de 45, desactivamos el Mino
            if (bottomCollision){
                active = false;
            }
        }
    }

    /// Metodo Update
    public void update(){

        if (deactivating){
            deactivating();
        }

        /// Movimiento del Mino
        if (KeyHandler.upPressed) { /// ROTACION DEL MINO
            switch (direction){
                case 1: getDirection2();break;
                case 2: getDirection3();break;
                case 3: getDirection4();break;
                case 4: getDirection1();break;
            }
            KeyHandler.upPressed = false;
        }

        checkMovementCollision(); /// Antes de mover, primero verificamos si esta con alguna colision

        if (KeyHandler.downPressed) { // Si presionamos "S"
            if (bottomCollision == false){ // Si no colisiono con el Piso
                // Lo movemos 1 hacia abajo
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                autoDropCounter = 0; // Reiniciamos el AutoDrop
            }

            KeyHandler.downPressed = false;
        }
        if (KeyHandler.leftPressed){ // Si presionamos "A"
            if (leftCollision == false){ // Si no colisiono con la pared izquierda
                // Lo movemos 1 hacia la izquierda
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }
            KeyHandler.leftPressed = false;
        }
        if (KeyHandler.rightPressed){ // Si presionamos "D"
            if(rightCollision == false){ // Si no colisiono con la pared derecha
                // Lo movemos 1 hacia la derecha
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }
            KeyHandler.rightPressed = false;
        }


        if(bottomCollision){ // Si el mino colisiona con el Piso
            deactivating = true;
        }
        else { // Si no, continua cayendo
            /// Caida del Mino
            autoDropCounter++; // Se incrementa uno con cada Frame
            if (autoDropCounter == PlayManager.dropInterval){ // Cuando llegue a 60, bajara un bloque
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0; // Vuelve a 0
            }
        }
    }

    /// Metodo Draw
    public void draw(Graphics2D g2){

        int margin = 2; // Con esto cambiamos su diseño, sin alterar sus posiciones X e Y
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));

    }

}
