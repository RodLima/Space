package br.tche.ucpel.doo3.game;

import javax.microedition.lcdui.*;

/**
 * Classe abstrata para manipulação dos Sprite
 *
 * @author Rodrigo
 */
public abstract class Sprite {

    protected int x, y;
    protected Image figuraSprite;
    protected int frameAtual, totalDeFrames;
    protected int largura, altura;
    protected int incrementoDeAnimacao;
    protected int numeroDeBullets;
    protected boolean ativo;

    /**
     * Metodo para desenhar Sprite na tela
     *
     * @param g Tela
     */
    public abstract void desenhaSprite(Graphics g);

    /**
     * Método para atualiza o estado do Sprite
     */
    public abstract void atualizar();

    /**
     * Método para mudar posição da Sprite
     *
     * @param x Posição x na tela
     * @param y Posição y na tela
     */
    public void moverPara(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Método para verificação de colisão de Sprites (Colisão de Quadrados)
     * @param outroSprite Sprite para verificação
     * @return Boolean caso exista colisão
     */
    public boolean colide(Sprite outroSprite) {
        if ((((x < outroSprite.x) && (x + largura > outroSprite.x)) || ((x > outroSprite.x) && (x < outroSprite.x + outroSprite.largura)))
                && (((y < outroSprite.y) && (y + altura > outroSprite.y)) || ((y > outroSprite.y) && (y < outroSprite.y + outroSprite.altura)))) {
            return true;
        } else {
            return false;
        }
    }
}
