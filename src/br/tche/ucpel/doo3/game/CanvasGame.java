package br.tche.ucpel.doo3.game;

import br.tche.ucpel.doo3.midlet.Midlet;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Classe que desenha as telas e cuida das threads do jogo
 *
 * @author Rodrigo
 */
public class CanvasGame extends GameCanvas implements Runnable {

    private final int TICK = 80;
    private Midlet midlet;
    private Engine engine;
    private Thread gameThread;
    private Font fontScore = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
    private Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE);
    private Font fontMenu = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    private long inicio, tempo;
    private boolean keyLocked;
    private boolean paused;
    private boolean quit;
    boolean novaFase;

    /**
     * Construtor GanvasGame
     *
     * @param midlet Midlet
     */
    public CanvasGame(Midlet midlet) {
        super(false);
        this.midlet = midlet;
        engine = new Engine(this);
    }

    /**
     * Metódo paint responsavél por desenhar objetos na tela
     *
     * @param g Tela
     */
    public void paint(Graphics g) {

        g.setClip(0, 0, getWidth(), getHeight());

        g.drawImage(Repositorio.getInstance().background, 0, 0, Graphics.TOP | Graphics.LEFT);

        if (engine.jogador.ativo || engine.jogador.explodindo) {
            engine.jogador.desenhaSprite(g);
        }

        for (int i = 0; i < engine.maxBullets; i++) {
            if (engine.bullets[i].ativo) {
                engine.bullets[i].desenhaSprite(g);
            }
        }

        for (int j = 0; j < engine.maxEnemies; j++) {
            if (engine.enemies[j].ativo || engine.enemies[j].explodindo) {
                engine.enemies[j].desenhaSprite(g);
            }
        }

        g.setFont(fontScore);
        g.setClip(0, 0, getWidth(), 3 + fontScore.getHeight());
        g.setColor(255, 255, 0);
        g.drawString("" + engine.score, getWidth() - 3, 3, Graphics.TOP | Graphics.RIGHT);

        if (engine.gameOver) {

            g.setFont(font);
            g.setClip(0, 0, getWidth(), getHeight());
            g.setColor(0, 0, 0);

            g.drawString("GAME OVER", getWidth() / 2 - 1, 159, Graphics.TOP | Graphics.HCENTER);
            g.drawString("GAME OVER", getWidth() / 2 - 1, 161, Graphics.TOP | Graphics.HCENTER);
            g.drawString("GAME OVER", getWidth() / 2 + 1, 159, Graphics.TOP | Graphics.HCENTER);
            g.drawString("GAME OVER", getWidth() / 2 + 1, 161, Graphics.TOP | Graphics.HCENTER);

            g.setColor(255, 255, 0);
            g.drawString("GAME OVER", getWidth() / 2 , 160, Graphics.TOP | Graphics.HCENTER); //88 -104

            g.setFont(fontMenu);
            g.drawString("Continuar", 5, 317 - fontMenu.getHeight(), Graphics.TOP | Graphics.LEFT);

        } else if (novaFase) {

            g.setFont(font);
            g.setClip(0, 0, getWidth(), getHeight());
            g.setColor(0, 0, 0);

            g.drawString("FASE " + engine.fase, getWidth() / 2 - 1, 159, Graphics.TOP | Graphics.HCENTER);
            g.drawString("FASE " + engine.fase, getWidth() / 2 - 1, 161, Graphics.TOP | Graphics.HCENTER);
            g.drawString("FASE " + engine.fase, getWidth() / 2 + 1, 159, Graphics.TOP | Graphics.HCENTER);
            g.drawString("FASE " + engine.fase, getWidth() / 2 + 1, 161, Graphics.TOP | Graphics.HCENTER);

            g.setColor(255, 255, 0);
            g.drawString("FASE " + engine.fase, getWidth() / 2, 160, Graphics.TOP | Graphics.HCENTER);

            g.setFont(fontMenu);
            g.drawString("Continuar", 5, 317 - fontMenu.getHeight(), Graphics.TOP | Graphics.LEFT);
            g.drawString("Sair", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

        } else {

            g.setClip(0, getHeight() - fontMenu.getHeight() - 3, getWidth(), fontMenu.getHeight() + 3);
            g.setFont(fontMenu);
            g.drawString("Pause", 5, 317 - fontMenu.getHeight(), Graphics.TOP | Graphics.LEFT);
            g.drawString("Sair", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);
        }
    }

    /**
     * Método run da thread do jogo
     */
    public void run() {
        while (!engine.gameOver) {

            inicio = System.currentTimeMillis();

            keyLocked = false;

            if (paused || novaFase) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                    }
                }
            }

            engine.atualizar();

            repaint();
            serviceRepaints();

            tempo = System.currentTimeMillis() - inicio;

            if (tempo > TICK) {
                continue;
            } else {
                try {
                    Thread.sleep(TICK - tempo);
                } catch (InterruptedException ie) {
                }
            }
        }

        if (!quit) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                }
            }

            quit = false;
        }

        midlet.displayMenu(engine.score);
    }

    /**
     * Método que cria a thread do jogo
     */
    public void start() {
        novaFase = true;
        paused = false;
        quit = false;
        engine.initGame();
        Display.getDisplay(midlet).setCurrent(this);

        gameThread = new Thread(this);
        gameThread.start();
        gameThread.setPriority(Thread.MAX_PRIORITY);
    }

    /**
     * Método quando o foco retorna para o jogo
     */
    public void showNotify() {
    }

    /**
     * Método que pausa o jogo quando se perde o foco no jogo
     */
    public void hideNotify() {
        paused = true;
    }

    /**
     * Método pela analise da tecla pressionada
     *
     * @param keyCode Código da tecla pressionada
     */
    public void keyPressed(int keyCode) {
        if (getKeyCode(FIRE) == keyCode || KEY_NUM5 == keyCode || -6 == keyCode) {
            if (!keyLocked) {
                engine.jogador.atira();
            }
            if (novaFase || paused || engine.gameOver) {
                novaFase = false;
                paused = false;

                synchronized (this) {
                    this.notify();
                }
            }
        }
        if (-7 == keyCode) {
            if (!engine.gameOver) {
                engine.gameOver = true;
                quit = true;
            }
            if (novaFase || paused) {
                synchronized (this) {
                    this.notify();
                }
            }

        }
        if (-6 == keyCode) {
            if (novaFase || paused || engine.gameOver) {
                novaFase = false;
                paused = false;

                synchronized (this) {
                    this.notify();
                }
            } else {
                paused = true;
            }
        }
        if (getKeyCode(LEFT) == keyCode) {
            engine.rightPressed = false;
            engine.leftPressed = true;
        }
        if (getKeyCode(RIGHT) == keyCode) {
            engine.rightPressed = true;
            engine.leftPressed = false;

        }
    }

    /**
     * Metodo resposável pela analise das teclas liberadas
     *
     * @param keyCode Código da tecla liberada
     */
    public void keyReleased(int keyCode) {
        switch (getGameAction(keyCode)) {
            case LEFT:
            case RIGHT:
                engine.leftPressed = false;
                engine.rightPressed = false;
                break;
        }
    }
}
