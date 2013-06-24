package br.tche.ucpel.doo3.game;

import java.util.Random;

/**
 * Classe com a logica do jogo
 *
 * @author Rodrigo
 */
public class Engine {

    private static final int ENEMIES_PER_LINE = 6;
    public int playerMaxBullets;
    public int enemyMaxBullets;
    public int maxBullets;
    public int linesOfEnemies;
    public int maxEnemies;
    public int enemySpeed;
    public int fase;
    public int score;
    private CanvasGame gameCanvas;
    private Random random;
    Nave jogador;
    Bullet[] bullets;
    Enemy[] enemies;
    int[] velocidadeDaLinha;
    int enemyNumeroDeBullets;
    int numeroDeInimigos;
    boolean rightPressed;
    boolean leftPressed;
    boolean gameOver;

    /**
     * Construtor
     *
     * @param gameCanvas Tela do jogo
     */
    public Engine(CanvasGame gameCanvas) {
        this.gameCanvas = gameCanvas;

        random = new Random();
    }

    /**
     * Método para iniciar o jogo
     */
    public void initGame() {
        gameOver = false;

        playerMaxBullets = 3;
        enemyMaxBullets = 2;
        maxBullets = playerMaxBullets + enemyMaxBullets;
        linesOfEnemies = 1;
        maxEnemies = ENEMIES_PER_LINE * linesOfEnemies;
        numeroDeInimigos = maxEnemies;
        enemySpeed = 1;
        enemyNumeroDeBullets = 0;

        fase = 1;
        score = 0;

        createSprites();
    }

    public void nextLevel() {

        gameCanvas.novaFase = true;

        fase++;

        if (fase % 2 == 0) {
            linesOfEnemies++;
            maxEnemies = linesOfEnemies * ENEMIES_PER_LINE;

            enemyMaxBullets++;
            playerMaxBullets++;
            maxBullets = playerMaxBullets + enemyMaxBullets;
        }

        if (fase % 3 == 0) {
            enemySpeed++;
        }

        numeroDeInimigos = maxEnemies;
        enemyNumeroDeBullets = 0;

        createSprites();

        System.gc();
    }

    /**
     * Método responsavel por atualizar o jogo
     */
    public void atualizar() {

        jogador.atualizar();

        for (int i = 0; i < maxBullets; i++) {
            if (bullets[i].ativo) {
                bullets[i].atualizar();
            }
        }

        for (int j = 0; j < maxEnemies; j++) {
            if (enemies[j].ativo || enemies[j].explodindo) {
                enemies[j].atualizar();
            }
        }

        for (int k = 0; k < linesOfEnemies; k++) {
            for (int j = 0; j < ENEMIES_PER_LINE; j++) {
                enemies[j + k * ENEMIES_PER_LINE].velocidadeX = velocidadeDaLinha[k];
            }

            if (enemies[k].ativo) {
                if (enemies[k].colide(jogador)) {
                    jogador.explodindo = true;
                    gameOver = true;
                }
            }
        }

        for (int i = 0; i < maxBullets; i++) {
            if (bullets[i].ativo) {
                if (bullets[i].dono instanceof Nave) {
                    for (int j = 0; j < maxEnemies; j++) {
                        if (enemies[j].ativo) {
                            if (bullets[i].colide(enemies[j])) {
                                updateScore();
                                bullets[i].killBullet();
                                enemies[j].killEnemy();
                            }
                        }
                    }
                } else {
                    if (bullets[i].colide(jogador)) {
                        jogador.explodindo = true;
                        gameOver = true;
                    }
                }
            }
        }
    }

    /**
     * Metodo para chamar o tiro
     *
     * @param dono Quem disparou
     */
    public void setBullet(Sprite dono) {

        for (int i = 0; i < maxBullets; i++) {
            if (bullets[i].ativo == false) {
                if (dono instanceof Nave) {
                    bullets[i].setBullet(dono.x + 7, dono.y - 12, -5, dono);
                    return;
                } else {
                    bullets[i].setBullet(dono.x + 5, dono.y + 15, 5, dono);
                    return;
                }
            }
        }
    }

    /**
     * Método para atualizar a pontuação
     */
    private void updateScore() {
        score += 10 * fase;
    }

    /**
     * Método para instanciar os objetos que apareceram na tela
     */
    private void createSprites() {

        jogador = new Nave(this);

        bullets = new Bullet[maxBullets];

        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet(this);
        }

        enemies = new Enemy[maxEnemies];

        velocidadeDaLinha = new int[linesOfEnemies];

        for (int k = 0; k < linesOfEnemies; k++) {
            for (int j = 0; j < ENEMIES_PER_LINE; j++) {
                enemies[j + k * ENEMIES_PER_LINE] = new Enemy(10 + 20 * j, 10 + 20 * k, k, this, random.nextLong());
            }
        }
    }
}
