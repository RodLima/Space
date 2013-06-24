package br.tche.ucpel.doo3.game;

import javax.microedition.lcdui.*;



/**
 * Classe que representa o tiro no jogo
 * @author Rodrigo
 */
public class Bullet extends Sprite {

    //Atributos do tiro.    
    private Engine engine;
    private Image figuraSprite2;
    private int velocidadeY;
    Sprite dono;


    /**
     * Construtor da classe Bullet
     * @param engine Logica do jogo
     */
    public Bullet(Engine engine) {
        this.engine = engine;
        totalDeFrames = 2;
        largura = 6;
        altura = 10;
        figuraSprite = Repositorio.getInstance().bullet1;
        figuraSprite2 = Repositorio.getInstance().bullet2;
    }

    //M�todo que coloca o tiro na tela.
    //Usado para promover a reutiliza��o de objetos.
    /**
     * Método que posiciona o tiro na tela
     * @param x Posição x 
     * @param y Posição y 
     * @param velocidadeY Velocidade
     * @param dono Diparador do tiro
     */
    public void setBullet(int x, int y, int velocidadeY, Sprite dono) {
        this.x = x;
        this.y = y;
        this.velocidadeY = velocidadeY;
        this.dono = dono;
        ativo = true;
    }

    
    /**
     * Método de controle da bala
     */
    public void killBullet() {
        ativo = false;
        if (dono instanceof Nave) {
            dono.numeroDeBullets--;
        } else {
            engine.enemyNumeroDeBullets--;
        }
    }

    /**
     * Método para atualizar o estado da bala
     */
    public void atualizar() {
        if (this.ativo && !engine.gameOver) {
            moverPara(this.x, this.y + this.velocidadeY);
            if (this.y < 0 || this.y > 320) {//208
                this.killBullet();
            }
        }
    }

    /**
     * Método que desenha a bala na tela
     * @param g Tela
     */
    public void desenhaSprite(Graphics g) {
        if (velocidadeY < 0) {
            g.setClip(x, y, largura, altura);
            g.drawImage(figuraSprite, x, y, Graphics.TOP | Graphics.LEFT);
        } else {
            g.setClip(x, y, largura, altura);
            g.drawImage(figuraSprite2, x, y, Graphics.TOP | Graphics.LEFT);
        }
    }
}
