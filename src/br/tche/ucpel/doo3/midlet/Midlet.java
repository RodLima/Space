package br.tche.ucpel.doo3.midlet;

import br.tche.ucpel.doo3.game.CanvasGame;
import br.tche.ucpel.doo3.game.CanvasMenu;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class Midlet extends MIDlet {

    private CanvasMenu canvasMenu;
    private CanvasGame canvasGame;

    public Midlet() {
        canvasMenu = new CanvasMenu(this);
        canvasGame = new CanvasGame(this);
    }

    public void startApp() {
        Display.getDisplay(this).setCurrent(canvasMenu);
        canvasMenu.doSplashScreen();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        System.gc();
    }

    public void displayMenu(int score) {
        canvasMenu.storeRecord(score);
        Display.getDisplay(this).setCurrent(canvasMenu);
    }

    public void startGame() {
        canvasGame.start();
    }

    public void exitGame() {
        destroyApp(true);
        notifyDestroyed();
    }
}
