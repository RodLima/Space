package br.tche.ucpel.doo3.game;

/**
 * Classe usada para representar os recordes do jogo
 * @author Rodrigo
 */
public class Record {

    private int score;
    private String name;
    
     public Record() {
    }

    /**
     * Construtor iniciado com nome e recorde
     *
     * @param score recorde
     * @param name nome
     */
    public Record(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    
    
}
