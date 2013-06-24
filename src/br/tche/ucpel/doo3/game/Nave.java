package br.tche.ucpel.doo3.game;

import javax.microedition.lcdui.*;

/**
 * Classe da nave do jogador
 *
 * @author Rodrigo
 */
public class Nave extends Sprite {

    private final int VELOCIDADE_X = 5;
    private Engine engine;
    private int counter;
    boolean explodindo;

    /**
     * Construtor da classe nave
     *
     * @param engine Logica
     */
    public Nave(Engine engine) {
        this.engine = engine;
        x = 120; 
        y = 280; 
        frameAtual = 1;
        totalDeFrames = 3;
        largura = 20;
        altura = 30;
        incrementoDeAnimacao = 21;
        numeroDeBullets = 0;
        ativo = true;
        figuraSprite = Repositorio.getInstance().nave;
    }

    /**
     * Método para mover para esquerda
     */
    public void moveEsquerda() {
        if (this.x - VELOCIDADE_X > 0) {
            moverPara(this.x - VELOCIDADE_X, this.y);
        }
    }

    /**
     * Método para mover para direita
     */
    public void moveDireita() {
        if (this.x + VELOCIDADE_X < 220) {
            moverPara(this.x + VELOCIDADE_X, this.y);
        }
    }

    /**
     * Método para a atualizar estado da nave
     */
    public void atualizar() {
        if (!explodindo && !engine.gameOver) {
            if (engine.rightPressed) {
                moveDireita();
            } else if (engine.leftPressed) {
                moveEsquerda();
            }
        } else if (explodindo) {
            if (++counter == 3) {
                explodindo = false;
                ativo = false;
                engine.gameOver = true;
                counter = 0;
                numeroDeBullets = 0;
            }
        }
        if (engine.leftPressed) {
            frameAtual = 0;
        } else if (engine.rightPressed) {
            frameAtual = 2;
        } else {
            frameAtual = 1;
        }
    }

    /**
     * Método para colocar um objeto bullet (tiro) na tela
     */
    public void atira() {
        if (numeroDeBullets < engine.playerMaxBullets) {
            engine.setBullet(this);
            numeroDeBullets++;
        }
    }

    /**
     * Método para desenhar a nave
     *
     * @param g Tela
     */
    public void desenhaSprite(Graphics g) {
        if (!explodindo) {
            g.setClip(x, y, largura, altura);
            g.drawImage(figuraSprite, x - frameAtual * incrementoDeAnimacao, y, Graphics.TOP | Graphics.LEFT);
        } else if (counter % 2 == 0) {
            g.setClip(x, y, largura, altura);
            g.drawImage(figuraSprite, x - frameAtual * incrementoDeAnimacao, y, Graphics.TOP | Graphics.LEFT);
        }
    }
}
