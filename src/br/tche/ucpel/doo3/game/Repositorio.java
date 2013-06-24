package br.tche.ucpel.doo3.game;

import javax.microedition.lcdui.*;
import java.io.IOException;

/**
 * Classe que representa o repositorio de imagens que vão ser carregadas no jogo
 *
 * @author Rodrigo
 */
public class Repositorio {

    private static Repositorio instance;
    Image splashScreen1;
    Image splashScreen2;
    Image menu;
    Image nave;
    Image enemy;
    Image bullet1, bullet2;
    Image background;

    /**
     * Construtor da classe
     */
    private Repositorio() {
        try {
            splashScreen1 = Image.createImage("/ucpel.png");
        } catch (IOException ioe) {
        }
    }

    /**
     * Método usado para obter a referencia para um objeto da classe Repositorio
     * @return 
     */
    public static Repositorio getInstance() {
        if (instance == null) {
            instance = new Repositorio();
        }

        return instance;
    }

    /**
     * Método para carregar as primeiras imagens
     */
    public void loadImages1() {
        try {
            splashScreen2 = Image.createImage("/space.png");
            menu = Image.createImage("/menu.png");
            background = Image.createImage("/fundo.png");
        } catch (IOException ioe) {
        }
    }

    /**
     * Metodo para carregar as imagens in-game
     */
    public void loadImages2() {
        try {
            nave = Image.createImage("/nave.png");
            bullet1 = Image.createImage("/tiro2.png");
            bullet2 = Image.createImage("/tiro2_inv.png");
            enemy = Image.createImage("/inimigo.png");
        } catch (IOException ioe) {
        }
    }
}
