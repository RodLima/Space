package br.tche.ucpel.doo3.game;

import javax.microedition.lcdui.*;
import java.util.Random;

/**
 * Classe que representa o inimigo
 * @author Rodrigo
 */
public class Enemy extends Sprite {
    
    private Engine engine;
    private Random random;
    private int linha, counter, counterY;
    int velocidadeX, velocidadeY;
    boolean explodindo;

   
    /**
     * Contrutor da classe
     * @param x
     * @param y
     * @param linha
     * @param engine
     * @param seed 
     */
    public Enemy(int x, int y, int linha, Engine engine, long seed) {
        this.engine = engine;
        this.x = x;
        this.y = y;
        this.linha = linha;

        largura = 15;
        altura = 15;
        ativo = true;
        figuraSprite = Repositorio.getInstance().enemy;
        random = new Random();
        random.setSeed(seed);

        if (linha % 2 == 1) {
            velocidadeX = 5;
        } else {
            velocidadeX = -5;
        }

        engine.velocidadeDaLinha[linha] = velocidadeX;
    }

    /**
     * Método para desativar inimigo
     */
    public void killEnemy() {
        ativo = false;
        explodindo = true;
        counter = 0;

        if (--engine.numeroDeInimigos == 0) {
            engine.nextLevel();
        }
    }

    /**
     * Método para atualiza o inimigo
     */
    public void atualizar() {

        if (!explodindo && !engine.gameOver) {
            if (this.x + velocidadeX < 15) {
                moverPara(this.x + velocidadeX, this.y + velocidadeY);
                engine.velocidadeDaLinha[linha] = 5;
            } else if (this.x + velocidadeX > 210) { //160
                moverPara(this.x + velocidadeX, this.y + velocidadeY);
                engine.velocidadeDaLinha[linha] = -5;
            } else {
                moverPara(this.x + velocidadeX, this.y + velocidadeY);
            }

            if (this.y > 155) {
                engine.gameOver = true;
            }

            if (counterY++ > 5) {
                velocidadeY = engine.enemySpeed;
                counterY = 0;
            } else {
                velocidadeY = 0;
            }

            if ((random.nextInt() & 127) < 10) {
                if (engine.enemyNumeroDeBullets < engine.enemyMaxBullets) {
                    engine.setBullet(this);
                    engine.enemyNumeroDeBullets++;
                }
            }
        } else if (explodindo) {
            if (counter++ > 4) {
                explodindo = false;
            }
        }
    }

    /**
     * Metodo que desenha o inimigo na tela
     * @param g 
     */
    public void desenhaSprite(Graphics g) {
        if (!explodindo) {
            g.setClip(x, y, largura, altura);
            g.drawImage(figuraSprite, x, y, Graphics.TOP | Graphics.LEFT);
        } else if (counter % 2 == 1) {
            g.setClip(x, y, largura, altura);
            g.drawImage(figuraSprite, x, y, Graphics.TOP | Graphics.LEFT);
        }
    }
}
