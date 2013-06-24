package br.tche.ucpel.doo3.rms;

import br.tche.ucpel.doo3.game.Record;
import java.io.*;
import javax.microedition.rms.*;


/**
 * Classe de armazenamento de records
 * 
 * @author Rodrigo
 */
public class RmsRanking implements RecordComparator {

    private int[] scores;
    private String[] names;
    private RecordStore rs;
    private final int NUM_RECORDS = 5;
    private boolean update;

    /**
     * Construtor da classe. Cria os arrays que armazenarão os valores dos
     * recordes e os preenche com os valores do RecordStore.
     */
    public RmsRanking() {
        scores = new int[NUM_RECORDS];
        names = new String[NUM_RECORDS];
        updateRecords();
    }

    /**
     * Método para criar ou abrir o RecordStore
     * @throws RecordStoreException 
     */
    private void openRecordStore() throws RecordStoreException {
        rs = RecordStore.openRecordStore("SpaceRMS", true);
    }

    /**
     * Método para fechar o RecordStore
     * @throws RecordStoreException 
     */
    private void closeRecordStore() throws RecordStoreException {
        if (rs != null) {
            rs.closeRecordStore();
        }
    }

    /**
     * Método para salvar os valores no RecordStore
     */
    private void saveRecords() {

        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] b = null;

        try {

            openRecordStore();

            for (int i = 0; i < NUM_RECORDS; i++) {
                try {
                    baos = new ByteArrayOutputStream();
                    dos = new DataOutputStream(baos);
                    dos.writeInt(scores[i]);
                    dos.writeUTF(names[i]);

                    b = baos.toByteArray();
                } catch (IOException ex) {
                } finally {
                    try {
                        dos.close();
                    } catch (IOException ex) {
                    }
                }

                rs.setRecord(i + 1, b, 0, b.length);
            }

        } catch (RecordStoreException ex) {
        } finally {
            try {
                closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }
    }

    /**
     * Método para atualiza os arrays. Caso o RecordStore não tenha registro, 
     * popula com registros default.
     */
    private void updateRecords() {

        try {
            openRecordStore();

            if (rs.getNumRecords() == 0) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                byte[] record = null;

                try {
                    dos.writeInt(0);
                    dos.writeUTF("AAAAA");
                    record = baos.toByteArray();
                } catch (IOException ex) {
                } finally {
                    try {
                        if (dos != null) {
                            dos.close();
                        }
                    } catch (IOException ex) {
                    }
                }

                for (int i = 0; i < NUM_RECORDS; i++) {
                    rs.addRecord(record, 0, record.length);
                    names[i] = "AAAAA";
                }

            } else {

                DataInputStream dis = null;

                for (int i = 0; i < NUM_RECORDS; i++) {
                    try {
                        dis = new DataInputStream(new ByteArrayInputStream(rs.getRecord(i + 1)));
                        scores[i] = dis.readInt();
                        names[i] = dis.readUTF();
                    } catch (IOException ex) {
                    } finally {
                        try {
                            dis.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        } catch (RecordStoreException ex) {
        } finally {
            try {
                closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }
    }

    /**
     * Método que retorna os recordes em ordem decrescente. Usa-se as interfaces
     * RecordEnumeration (para iterar sobre os registros) e RecordComparator
     * (para ordenar os registros).
     *
     * @return Retorna os recordes em ordem decrescente de pontos em um array de
     * Record
     */
    public Record[] getHighestScores() {

        RecordEnumeration re = null;
        DataInputStream dis = null;
        Record[] recordes = new Record[NUM_RECORDS];
        int i = 0;

        try {
            openRecordStore();

            re = rs.enumerateRecords(null, this, false);

            while (re.hasNextElement()) {
                try {
                    dis = new DataInputStream(new ByteArrayInputStream(re.nextRecord()));
                    recordes[i] = new Record();
                    recordes[i].setScore(dis.readInt());
                    recordes[i].setName(dis.readUTF()); 
                     
                } catch (IOException ex) {
                } finally {
                    try {
                        dis.close();
                    } catch (IOException ex) {
                    }
                }

                i++;
            }
        } catch (RecordStoreException ex) {
        } finally {
            re.destroy();

            try {
                closeRecordStore();
            } catch (RecordStoreException ex) {
            }
        }

        return recordes;
    }

    /**
     * Método para armazenar um novo score. Verifica se esse score é maior que 
     * lowestScore antes de armazená-lo.
     *
     * @param score recorde
     * @param name nome
     */
    public void addHighScore(int score, String name) {

        int lowestScore = scores[0];
        int index = 0;

        for (int i = 1; i < NUM_RECORDS; i++) {
            if (scores[i] < lowestScore) {
                lowestScore = scores[i];
                index = i;
            }
        }

        if (score > lowestScore) {
            scores[index] = score;
            names[index] = name;
            saveRecords();
        }
    }

    /**
     * Retorna a quantidade de pontos e o nome do maior pontuador.
     *
     * @return Retorna o maior recorde local
     */
    public Record getHighestScore() {

        int highestScore = scores[0];
        int index = 0;

        for (int i = 1; i < NUM_RECORDS; i++) {
            if (scores[i] > highestScore) {
                highestScore = scores[i];
                index = i;
            }
        }

        Record record = new Record();

        record.setScore(highestScore);
        record.setName(names[index]);

        return record;
    }

    /**
     * Retorna o menor score.
     *
     * @return Retorna o menor recorde 
     */
    public int getLowestScore() {

        int lowestScore = scores[0];

        for (int i = 1; i < NUM_RECORDS; i++) {
            if (scores[i] < lowestScore) {
                lowestScore = scores[i];
            }
        }

        return lowestScore;
    }

    /**
     * Método que faz a comparação do recorde
     * 
     */
    public int compare(byte[] rec1, byte[] rec2) {
        int score1 = 0, score2 = 0;

        for (int i = 0; i < 4; i++) {
            score1 = (score1 << 8);
            score1 |= (rec1[i] & 0x000000ff);

            score2 = (score2 << 8);
            score2 |= (rec2[i] & 0x000000ff);
        }

        if (score1 == score2) {
            return RecordComparator.EQUIVALENT;
        } else if (score1 > score2) {
            return RecordComparator.PRECEDES;
        } else {
            return RecordComparator.FOLLOWS;
        }
    }
}
