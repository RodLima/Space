package br.tche.ucpel.doo3.game;

import br.tche.ucpel.doo3.midlet.Midlet;
import br.tche.ucpel.doo3.servlet.ServletClient;
import br.tche.ucpel.doo3.rms.RmsRanking;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Classe que representa todas as telas de entrada e de menu do jogo
 *
 * @author Rodrigo
 */
public class CanvasMenu extends GameCanvas {

    private final byte SPLASH_SCREEN_1 = 0;
    private final byte SPLASH_SCREEN_2 = 1;
    private final byte MAIN_MENU = 2;
    private final byte SCORE_MENU = 3;
    private final byte INSERT_RECORD = 4;
    private final byte LOCAL_RECORDS = 5;
    private final byte ONLINE_RECORDS = 6;
    private final byte GETTING_RECORDS = 8;
    private final byte SENDING_RECORD = 9;
    private final byte RECORD_SENT = 10;
    private Midlet midlet;
    private RmsRanking rmsRanking;
    private ServletClient sevletClient;
    private Repositorio repositorio;
    private byte telaExibida;
    private int index;
    private int indexScore;
    private Image splashScreen1;
    private Image splashScreen2;
    private Image menu;
    private Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    private Font fontScore = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_SMALL);
    private int score;
    private char[] name;
    private int charIndex;
    private Record[] localRecords;
    private Record[] onlineRecords;

    /**
     * Método constutor da classe
     *
     * @param midlet Midlet
     */
    public CanvasMenu(Midlet midlet) {
        super(false);
        this.midlet = midlet;

        repositorio = Repositorio.getInstance();
        splashScreen1 = repositorio.splashScreen1;
        telaExibida = SPLASH_SCREEN_1;

        rmsRanking = new RmsRanking();
        sevletClient = ServletClient.getInstance(this);

        name = new char[5];
        onlineRecords = rmsRanking.getHighestScores();
    }

    /**
     * Método para desenhar na tela
     *
     * @param g Tela
     */
    public void paint(Graphics g) {

        switch (telaExibida) {
            case SPLASH_SCREEN_1:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(splashScreen1, 0, 0, Graphics.TOP | Graphics.LEFT);
                break;

            case SPLASH_SCREEN_2:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(splashScreen2, 0, 0, Graphics.TOP | Graphics.LEFT);
                break;

            case MAIN_MENU:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Selecionar", 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.LEFT);
                g.drawString("Sair", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                if (index == 0) {
                    g.setColor(255, 255, 255);
                    g.drawString("JOGAR", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 0);
                    g.drawString("RANKING", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.drawString("SAIR", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                } else if (index == 1) {
                    g.setColor(255, 255, 0);
                    g.drawString("JOGAR", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 255);
                    g.drawString("RANKING", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 0);
                    g.drawString("SAIR", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                } else {
                    g.setColor(255, 255, 0);
                    g.drawString("JOGAR", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.drawString("RANKING", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 255);
                    g.drawString("SAIR", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                }

                break;

            case SCORE_MENU:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Selecionar", 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.LEFT);
                g.drawString("Voltar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                if (indexScore == 0) {
                    g.setColor(255, 255, 255);
                    g.drawString("RECORDES LOCAIS", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 0);
                    g.drawString("RECORDES ONLINE", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.drawString("ENVIAR MELHOR RECORDE", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                } else if (indexScore == 1) {
                    g.setColor(255, 255, 0);
                    g.drawString("RECORDES LOCAIS", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 255);
                    g.drawString("RECORDES ONLINE", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 0);
                    g.drawString("ENVIAR MELHOR RECORDE", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                } else if (indexScore == 2) {
                    g.setColor(255, 255, 0);
                    g.drawString("RECORDES LOCAIS", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.drawString("RECORDES ONLINE", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 255);
                    g.drawString("ENVIAR MELHOR RECORDE", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 0);
                } else {
                    g.setColor(255, 255, 0);
                    g.drawString("RECORDES LOCAIS", getWidth() / 2, 180, Graphics.HCENTER | Graphics.BASELINE);
                    g.drawString("RECORDES ONLINE", getWidth() / 2, 180 + 2 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.drawString("ENVIAR MELHOR RECORDE", getWidth() / 2, 180 + 4 * font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                    g.setColor(255, 255, 255);
                }

                break;

            case INSERT_RECORD:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Inserir", 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.LEFT);
                g.drawString("Cancelar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                g.setColor(255, 255, 0);

                g.drawString("Nome", getWidth() / 2, font.getHeight() + 3, Graphics.HCENTER | Graphics.BASELINE);

                for (int i = 0; i < name.length; i++) {
                    if (i != charIndex) {
                        g.drawChar(name[i], getWidth() / 5 - 10 + (i + 1) * 20, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);
                    } else {
                        g.setColor(255, 255, 255);
                        g.drawChar(name[i], getWidth() / 5 - 10 + (i + 1) * 20, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);
                        g.setColor(255, 255, 0);
                    }
                }

                break;

            case LOCAL_RECORDS:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Ok", 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.LEFT);
                g.drawString("Voltar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                g.setColor(255, 255, 0);

                g.drawString("Recordes Locais", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);

                for (int i = 0; i < localRecords.length; i++) {
                    g.drawString(localRecords[i].getName(), 3, (i + 10) * font.getHeight() + 3, Graphics.TOP | Graphics.LEFT);
                    g.drawString("" + localRecords[i].getScore(), getWidth() - 3, (i + 10) * font.getHeight() + 3, Graphics.TOP | Graphics.RIGHT);
                }

                break;

            case ONLINE_RECORDS:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Voltar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                g.setColor(255, 255, 0);

                g.drawString("Recordes Online", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);

                for (int i = 0; i < onlineRecords.length; i++) {
                    if (onlineRecords[i] != null) {
                        g.drawString(onlineRecords[i].getName(), 3, (i + 10) * font.getHeight() + 3, Graphics.TOP | Graphics.LEFT);
                        g.drawString("" + onlineRecords[i].getScore(), getWidth() - 3, (i + 10) * font.getHeight() + 3, Graphics.TOP | Graphics.RIGHT);
                    } else {
                        g.drawString("_____", 3, (i + 10) * font.getHeight() + 3, Graphics.TOP | Graphics.LEFT);
                        g.drawString("" + 0, getWidth() - 3, (i + 10) * font.getHeight() + 3, Graphics.TOP | Graphics.RIGHT);
                    }
                }

                break;

            case GETTING_RECORDS:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Cancelar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                g.setColor(255, 255, 0);

                g.drawString("Obtendo recordes", getWidth() / 2, getHeight() / 2 - font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);
                g.drawString("online", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);
                g.drawString("Aguarde...", getWidth() / 2, getHeight() / 2 + font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);

                break;

            case SENDING_RECORD:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Cancelar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                g.setColor(255, 255, 0);

                g.drawString("Enviando recorde", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);
                g.drawString("Aguarde...", getWidth() / 2, getHeight() / 2 + font.getHeight(), Graphics.HCENTER | Graphics.BASELINE);

                break;

            case RECORD_SENT:
                g.setClip(0, 0, getWidth(), getHeight());
                g.drawImage(menu, 0, 0, Graphics.TOP | Graphics.LEFT);

                g.setFont(fontScore);
                g.setColor(255, 255, 0);
                g.drawString("Ok", 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.LEFT);
                g.drawString("Voltar", getWidth() - 5, 317 - fontScore.getHeight(), Graphics.TOP | Graphics.RIGHT);

                g.setFont(font);

                g.setColor(255, 255, 0);

                g.drawString("Recorde Enviado!", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.BASELINE);

                break;

        }
    }

    /**
     * Método para interpretação da tecla segurada
     *
     * @param keyCode Código da tecla segurada
     */
    public void keyRepeated(int keyCode) {
        if (telaExibida == INSERT_RECORD) {
            switch (getGameAction(keyCode)) {
                case UP:
                    if (--name[charIndex] < 33) {
                        name[charIndex] = 129;
                    }
                    break;

                case DOWN:
                    if (++name[charIndex] > 129) {
                        name[charIndex] = 33;
                    }
                    break;

                case LEFT:
                    left();
                    break;

                case RIGHT:
                    right();
                    break;
            }
            repaint();
            serviceRepaints();
        }
    }

    /**
     * Método para interpretação do teclado
     *
     * @param keyCode Código da tecla pressionada
     */
    public void keyPressed(int keyCode) {

        switch (telaExibida) {
            case SPLASH_SCREEN_1:
            case SPLASH_SCREEN_2:
                break;

            case MAIN_MENU:
                if (getKeyCode(UP) == keyCode || keyCode == KEY_NUM2) {
                    up();
                }
                if (getKeyCode(DOWN) == keyCode || keyCode == KEY_NUM8) {
                    down();
                }
                if (getKeyCode(FIRE) == keyCode || keyCode == KEY_NUM5 || -6 == keyCode) {
                    if (index == 0) {

                        for (int i = 0; i < name.length; i++) {
                            name[i] = 'A';
                        }

                        charIndex = 0;

                        midlet.startGame();
                    } /**
                     * Ranking
                     */
                    else if (index == 1) {
                        telaExibida = SCORE_MENU;
                    } /**
                     * Sair
                     */
                    else {
                        midlet.exitGame();
                    }

                    index = 0;

                }
                /**
                 * Sair
                 */
                if (-7 == keyCode) {
                    midlet.exitGame();
                }

                break;

            case SCORE_MENU:
                if (getKeyCode(UP) == keyCode || keyCode == KEY_NUM2) {
                    upScore();
                }
                if (getKeyCode(DOWN) == keyCode || keyCode == KEY_NUM8) {
                    downScore();
                }
                if (getKeyCode(FIRE) == keyCode || -6 == keyCode) {
                    if (indexScore == 0) {
                        telaExibida = LOCAL_RECORDS;
                        retrieveLocalRecords();
                    } /*Selecionou Recordes OnLine*/ else if (indexScore == 1) {
                        telaExibida = GETTING_RECORDS;
                        retrieveOnLineRecords();
                    } /*Selecionou Enviar Recordes*/ else if (indexScore == 2) {
                        telaExibida = SENDING_RECORD;

                        submitRecord();

                    } /* Selecionou LOCAL = ONLINE */ else {
                        //  rmsRanking = null;
                        // for (int j = 0; j < onlineRecords.length; j++) {
                        if (onlineRecords[0] != null) {
                            retrieveLocalRecords();
                            boolean existe = false;
                            for (int i = 0; i < localRecords.length; i++) {
                                if (onlineRecords[0].getScore() == localRecords[i].getScore()
                                        && onlineRecords[0].getName().equals(localRecords[i].getName())) {
                                    existe = true;
                                }
                            }
                        }
                    }

                    indexScore = 0;
                }


                if (-7 == keyCode) {
                    /*Voltar.*/
                    telaExibida = MAIN_MENU;
                }

                break;

            case INSERT_RECORD:
                if (getKeyCode(UP) == keyCode) {
                    if (--name[charIndex] < 33) {
                        name[charIndex] = 129;
                    }
                }
                if (getKeyCode(DOWN) == keyCode) {
                    if (++name[charIndex] > 129) {
                        name[charIndex] = 33;
                    }
                }
                if (getKeyCode(LEFT) == keyCode) {
                    left();
                }
                if (getKeyCode(RIGHT) == keyCode) {
                    right();
                }
                if (getKeyCode(FIRE) == keyCode || -6 == keyCode) {
                    rmsRanking.addHighScore(score, new String(name));
                    telaExibida = LOCAL_RECORDS;
                    retrieveLocalRecords();
                }

                if (-7 == keyCode) {
                    telaExibida = SCORE_MENU;
                }
                break;

            case LOCAL_RECORDS:
                if (getKeyCode(FIRE) == keyCode || -6 == keyCode || -7 == keyCode) {
                    telaExibida = SCORE_MENU;
                }
                break;

            case ONLINE_RECORDS:
                if (-7 == keyCode) {
                    telaExibida = SCORE_MENU;
                }
                break;

            case GETTING_RECORDS:
                if (-7 == keyCode) {
                    telaExibida = SCORE_MENU;
                    sevletClient.cancelarRecebimentoRecordes();
                }
                break;

            case SENDING_RECORD:
                if (-7 == keyCode) {
                    telaExibida = SCORE_MENU;
                    sevletClient.cancelarEnvioRecorde();
                }
                break;


            case RECORD_SENT:
                if (getKeyCode(FIRE) == keyCode || -6 == keyCode || -7 == keyCode) {
                    telaExibida = SCORE_MENU;
                }

                break;
        }

        repaint();
        serviceRepaints();


    }

    /**
     * Método para carregar as splash screen
     */
    public void doSplashScreen() {

        long inicio, tempo;

        inicio = System.currentTimeMillis();

        repositorio.loadImages1();
        splashScreen2 = repositorio.splashScreen2;
        menu = repositorio.menu;

        tempo = System.currentTimeMillis() - inicio;

        if (tempo < 2000) {
            try {
                Thread.sleep(2000 - tempo);
            } catch (InterruptedException ioe) {
            }
        }


        inicio = System.currentTimeMillis();

        telaExibida = SPLASH_SCREEN_2;

        repaint();
        serviceRepaints();

        repositorio.loadImages2();

        tempo = System.currentTimeMillis() - inicio;

        if (tempo < 2000) {
            try {
                Thread.sleep(2000 - tempo);
            } catch (InterruptedException ioe) {
            }
        }

        telaExibida = MAIN_MENU;

        repaint();
        serviceRepaints();
    }

    /**
     * Método verifica se o record vai ser gravado
     *
     * @param score
     */
    public void storeRecord(int score) {
        if (score > rmsRanking.getLowestScore()) {
            telaExibida = INSERT_RECORD;
            this.score = score;
        } else {
            telaExibida = MAIN_MENU;
        }
    }

    /**
     * Método para recuperar os records da RMS
     */
    private void retrieveLocalRecords() {
        localRecords = rmsRanking.getHighestScores();
    }

    /**
     * Método para pegar os records online
     */
    private void retrieveOnLineRecords() {
        sevletClient.receberMelhoresRecordes();
    }

    /**
     * Método de recebimento dos recordes online
     *
     * @param onlineRecords Ranking Online
     */
    public void onLineRecords(Record[] onlineRecords) {
        this.onlineRecords = onlineRecords;
        telaExibida = ONLINE_RECORDS;
        repaint();
        serviceRepaints();
    }

    /**
     * Método para eviar o melhor recorde para o servidor
     */
    private void submitRecord() {
        sevletClient.enviarRecorde(rmsRanking.getHighestScores()[0]);
    }

    /**
     * Método para atualiza a tela em caso de sucesso de envio
     *
     * @param success resposta do servlet
     */
    public void recordSubmited(boolean success) {
        telaExibida = RECORD_SENT;
        repaint();
        serviceRepaints();
    }

    /**
     * Método para controlar o indice
     */
    private void up() {
        if (--index < 0) {
            index = 2;
        }
    }

    /**
     * Método para controlar o indice
     */
    private void down() {
        if (++index > 2) {
            index = 0;
        }
    }

    /**
     * Método para controlar a indice charIndex
     */
    private void left() {
        if (--charIndex < 0) {
            charIndex = 0;
        }
    }

    /**
     * Método para controlar a indice charIndex
     */
    private void right() {
        if (++charIndex > name.length - 1) {
            charIndex = name.length - 1;
        }
    }

    /**
     * Método para controlar a indice indexScore
     */
    private void upScore() {
        if (--indexScore < 0) {
            indexScore = 2;
        }
    }

    /**
     * Método para controlar a indice indexScore
     */
    private void downScore() {
        if (++indexScore > 2) {
            indexScore = 0;
        }
    }
}
