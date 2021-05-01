/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package práce.šifrování;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author xxx
 */
public class Porovnavac {

    GridPane tabulkaDesifrace;
    AnalyzaZnaku predloha;
    AnalyzaZnaku sifra;

    double[][] pravdepodobnostZnak;
    int[] nejmensiId;
    int[] nejmensiId2;
    int[] nejmensiId3;
    int[] nejmensiIdBigram;
    int[] nejmensiIdTrigram;

    Porovnavac(AnalyzaZnaku predloha, AnalyzaZnaku sifra, GridPane tabulkaDesifrace) {
        this.tabulkaDesifrace = tabulkaDesifrace;
        this.predloha = predloha;
        this.sifra = sifra;
    }

    public void porovnej() {
        NejmensiIdPodleBigramu nejmensiIdPodleBigramu[] = new NejmensiIdPodleBigramu[sifra.znaky.size()];
        for (int k = 0; k < sifra.znaky.size(); k++) {
            nejmensiIdPodleBigramu[k] = new NejmensiIdPodleBigramu();
        }
        pravdepodobnostZnak = new double[sifra.znaky.size()][predloha.znaky.size()];
        nejmensiId = new int[sifra.znaky.size()];
        nejmensiId2 = new int[sifra.znaky.size()];
        nejmensiId3 = new int[sifra.znaky.size()];
        nejmensiIdBigram = new int[Math.max(sifra.znaky.size(), Math.max(sifra.bigram.size(), predloha.bigram.size()))];
        for (int i = 0; i < sifra.znaky.size(); i++) {
            double nejmensi = Double.MAX_VALUE;
            for (int j = 0; j < predloha.znaky.size(); j++) {
                //if(predloha.znaky.get(j).getPocet() > 10){// 
                double procenta = Math.pow(Math.abs(((double) sifra.znaky.get(i).getPocet() * (100.0 / (double) sifra.getPocetZnaku())) - (predloha.znaky.get(j).getPocet() * (100.0 / (double) predloha.getPocetZnaku()))), 2);
                /*double procentaPred = 0;
                for (int k = 0; k < Math.min(sifra.znaky.get(i).getZnakyPred().size(), 5); k++) {
                    double procenta2 = -1;
                    for (int l = 0; l < predloha.znaky.get(j).getZnakyPred().size(); l++) {

                        procenta2 = Math.abs(predloha.znaky.get(i).getZnakyPred().get(l).getCetnost() - sifra.znaky.get(i).getZnakyPred().get(k).getCetnost());

                    }
                    if (procenta2 == -1) {
                        procenta2 = 100;
                    }
                    procentaPred += procenta2;
                }
                procentaPred = procentaPred / Math.min(sifra.znaky.get(i).getZnakyPred().size(), 5);

                double procentaPo = 0;
                for (int k = 0; k < Math.min(sifra.znaky.get(i).getZnakyPo().size(), 5); k++) {
                    double procenta2 = -1;
                    for (int l = 0; l < predloha.znaky.get(i).getZnakyPo().size(); l++) {

                        procenta2 = Math.abs(predloha.znaky.get(i).getZnakyPo().get(l).getCetnost() - sifra.znaky.get(i).getZnakyPo().get(k).getCetnost());

                    }
                    if (procenta2 == -1) {
                        procenta2 = 100;
                    }
                    procentaPo += procenta2;
                }
                procentaPo = procentaPo / Math.min(sifra.znaky.get(i).getZnakyPo().size(), 5);*/
                //procenta += procentaPo+procentaPred;
                System.out.println(sifra.znaky.get(i).getZnak() + "," + predloha.znaky.get(j).getZnak() + " (" + procenta + ")");
                //sifra.znaky.get(i).setSance(procenta);
                pravdepodobnostZnak[i][j] = procenta;
                System.out.println(procenta);
                if (procenta < nejmensi) {
                    nejmensi = procenta;
                    nejmensiId[i] = j;
                }
                //}    
            }
        }

        /*for (int i = 0; i < sifra.bigram.size(); i++) {
            String predlohaBigram1;
            String predlohaBigram2;
            double nejmensi = Double.MAX_VALUE;
            double cetnostSifraBigram = sifra.bigram.get(i).getPocet() * (100.0 / Double.valueOf(sifra.getPocetZnaku() - 1));
            for (int j = 0; j < predloha.bigram.size(); j++) {
                double cetnostPredlohaBigram = predloha.bigram.get(j).getPocet() * (100.0 / Double.valueOf(predloha.getPocetZnaku() - 1));
                double neshoda = Math.pow(cetnostSifraBigram - cetnostPredlohaBigram, 2);
                if (neshoda < nejmensi) {
                    nejmensi = neshoda;
                    nejmensiIdBigram[i] = j;
                }
            }
            System.out.println();
        }
        for (int i = 0; i < sifra.bigram.size(); i++) {
            int indexZnaky = 0;
            int indexPredloha = 0;
            for (int j = 0; j < sifra.znaky.size(); j++) {
                //System.out.println(sifra.bigram.get(i).getZnak().substring(0,1));
                //if (sifra.bigram.get(nejmensiIdBigram[i]).getZnak().substring(0,1).equals(sifra.znaky.get(j).getZnak())) {
                if (sifra.bigram.get(i).getZnak().substring(0, 1).equals(String.valueOf(sifra.znaky.get(j).getZnak()))) {
                    indexZnaky = j;
                }
                    /*if (sifra.bigram.get(nejmensiIdBigram[i]).getZnak().substring(1,2).equals(sifra.znaky.get(j).getZnak())) {
                    if (sifra.bigram.get(i).getZnak().substring(1,2).equals(String.valueOf(sifra.znaky.get(j).getZnak()))) {
                        for (int k = 0; k < predloha.znaky.size(); k++) {
                            if (String.valueOf(predloha.znaky.get(k).getZnak()).equals(predloha.bigram.get(nejmensiIdBigram[i]).getZnak().substring(1,2))) {
                                nejmensiIdPodleBigramu[j].add(k);
                                break;
                            }
                        }
                    }
                }
                }
            for(int j = 0;j<predloha.znaky.size();j++){
                if (predloha.bigram.get(nejmensiIdBigram[i]).getZnak().substring(0, 1).equals(String.valueOf(predloha.znaky.get(j).getZnak()))) {
                    indexPredloha = j;
            }
            }
            /*for (int i = 0; i < sifra.znaky.size(); i++) {
            if (nejmensiIdPodleBigramu[i].getNejpocetnejsi() != -1) {
                nejmensiId[i] = nejmensiIdPodleBigramu[i].getNejpocetnejsi();
                System.out.println(predloha.znaky.get(nejmensiIdPodleBigramu[i].getNejpocetnejsi()).getZnak());
            }
            System.out.println(sifra.znaky.get(indexZnaky).getZnak()+" -> "+predloha.znaky.get(indexPredloha).getZnak());

        }*/
        int pocetBigramSifra = 0;
        int pocetBigramPredloha = 0;
        for (int i = 0; i < sifra.bigram.size(); i++) {
            pocetBigramSifra += sifra.bigram.get(i).getPocet();
        }
        for (int i = 0; i < predloha.bigram.size(); i++) {
            pocetBigramPredloha += predloha.bigram.get(i).getPocet();
        }

        NejmensiIdPodleBigramu hodiciSe = new NejmensiIdPodleBigramu();

        for (int i = sifra.bigram.size() - 1; i >= 0; i--) {
            double relativniCetnostSifra = sifra.bigram.get(i).getPocet() * (100.0 / pocetBigramSifra);
            int indexNejpodobnejsiPredlohy = -1;
            double nejvetsiPodobnost = Double.MAX_VALUE;
            for (int j = 0; j < predloha.bigram.size(); j++) {
                double relativniCetnostPredloha = predloha.bigram.get(j).getPocet() * (100.0 / pocetBigramPredloha);
                double nepodobnost = Math.pow(relativniCetnostSifra - relativniCetnostPredloha, 2);
                if (nepodobnost < nejvetsiPodobnost) {
                    indexNejpodobnejsiPredlohy = j;
                    nejvetsiPodobnost = nepodobnost;
                }
            }

            if (indexNejpodobnejsiPredlohy != -1) {
                char bigramSifra1 = sifra.bigram.get(i).getZnak().charAt(0);
                char bigramSifra2 = sifra.bigram.get(i).getZnak().charAt(1);

                char bigramPredloha1 = predloha.bigram.get(indexNejpodobnejsiPredlohy).getZnak().charAt(0);
                char bigramPredloha2 = predloha.bigram.get(indexNejpodobnejsiPredlohy).getZnak().charAt(1);

                int idSifra1 = -1;
                int idSifra2 = -1;

                System.out.println(sifra.bigram.get(i).getZnak() + " -> " + predloha.bigram.get(indexNejpodobnejsiPredlohy).getZnak());

                for (int j = 0; j < sifra.znaky.size(); j++) {
                    if (sifra.znaky.get(j).getZnak() == bigramSifra1) {
                        idSifra1 = j;
                    }
                    if (sifra.znaky.get(j).getZnak() == bigramSifra2) {
                        idSifra2 = j;
                    }
                }
                for (int j = 0; j < predloha.znaky.size(); j++) {
                    if (predloha.znaky.get(j).getZnak() == bigramPredloha1) {
                        if (idSifra1 != -1) {
                            hodiciSe.get(idSifra1).add(j);
                            System.out.println(sifra.znaky.get(idSifra1).getZnak() + " -> " + predloha.znaky.get(j).getZnak());
                        }
                    }
                    if (predloha.znaky.get(j).getZnak() == bigramPredloha2) {
                        if (idSifra2 != -1) {
                            hodiciSe.get(idSifra2).add(j);
                        }
                    }
                }
            }
        }

        for (int i = hodiciSe.znakySifra.size() - 1; i >= 0; i--) {
            nejmensiId2[hodiciSe.id.get(i)] = hodiciSe.znakySifra.get(i).getNejpocetnejsi();
        }

        int pocetTrigramSifra = 0;
        int pocetTrigramPredloha = 0;
        for (int i = 0; i < sifra.trigram.size(); i++) {
            pocetTrigramSifra += sifra.trigram.get(i).getPocet();
        }
        for (int i = 0; i < predloha.trigram.size(); i++) {
            pocetTrigramPredloha += predloha.trigram.get(i).getPocet();
        }

        hodiciSe = new NejmensiIdPodleBigramu();

        for (int i = sifra.trigram.size() - 1; i >= 0; i--) {
            double relativniCetnostSifra = sifra.trigram.get(i).getPocet() * (100.0 / pocetTrigramSifra);
            int indexNejpodobnejsiPredlohy = -1;
            double nejvetsiPodobnost = Double.MAX_VALUE;
            for (int j = 0; j < predloha.trigram.size(); j++) {
                double relativniCetnostPredloha = predloha.trigram.get(j).getPocet() * (100.0 / pocetTrigramPredloha);
                double nepodobnost = Math.pow(relativniCetnostSifra - relativniCetnostPredloha, 2);
                if (nepodobnost < nejvetsiPodobnost) {
                    indexNejpodobnejsiPredlohy = j;
                    nejvetsiPodobnost = nepodobnost;
                }
            }

            if (indexNejpodobnejsiPredlohy != -1) {
                char tringramSifra1 = sifra.trigram.get(i).getZnak().charAt(0);
                char trigramSifra2 = sifra.trigram.get(i).getZnak().charAt(1);
                char trigramSifra3 = sifra.trigram.get(i).getZnak().charAt(2);

                char trigramPredloha1 = predloha.trigram.get(indexNejpodobnejsiPredlohy).getZnak().charAt(0);
                char trigramPredloha2 = predloha.trigram.get(indexNejpodobnejsiPredlohy).getZnak().charAt(1);
                char trigramPredloha3 = predloha.trigram.get(indexNejpodobnejsiPredlohy).getZnak().charAt(2);

                int idSifra1 = -1;
                int idSifra2 = -1;
                int idSifra3 = -1;

                System.out.println(sifra.trigram.get(i).getZnak() + " -> " + predloha.trigram.get(indexNejpodobnejsiPredlohy).getZnak());

                for (int j = 0; j < sifra.znaky.size(); j++) {
                    if (sifra.znaky.get(j).getZnak() == tringramSifra1) {
                        idSifra1 = j;
                    }
                    if (sifra.znaky.get(j).getZnak() == trigramSifra2) {
                        idSifra2 = j;
                    }
                    if (sifra.znaky.get(j).getZnak() == trigramSifra3) {
                        idSifra3 = j;
                    }
                }
                for (int j = 0; j < predloha.znaky.size(); j++) {
                    if (predloha.znaky.get(j).getZnak() == trigramPredloha1) {
                        if (idSifra1 != -1) {
                            hodiciSe.get(idSifra1).add(j);
                            System.out.println(sifra.znaky.get(idSifra1).getZnak() + " -> " + predloha.znaky.get(j).getZnak());
                        }
                    }
                    if (predloha.znaky.get(j).getZnak() == trigramPredloha2) {
                        if (idSifra2 != -1) {
                            hodiciSe.get(idSifra2).add(j);
                        }
                    }
                    if (predloha.znaky.get(j).getZnak() == trigramPredloha3) {
                        if (idSifra3 != -1) {
                            hodiciSe.get(idSifra3).add(j);
                        }
                    }
                }
            }
        }

        for (int i = hodiciSe.znakySifra.size() - 1; i >= 0; i--) {
            nejmensiId3[hodiciSe.id.get(i)] = hodiciSe.znakySifra.get(i).getNejpocetnejsi();
        }

        //-----------   Závěrečné hodnocení
        for (int i = nejmensiId3.length - 1; i >= 0; i--) {
            if (nejmensiId3[i] == nejmensiId2[i]) {
                nejmensiId[i] = nejmensiId2[i];
            }
        }

    }

    public void vykresli() throws IOException {
        for (int i = 0; i < sifra.znaky.size(); i++) {
            for (int j = 0; j < predloha.znaky.size(); j++) {
                Button bt = new Button(String.valueOf(predloha.znaky.get(j).znak));//Double.toString(Math.round(pravdepodobnostZnak[i][j]))
                bt.setMinWidth(40);
                bt.setStyle("-fx-background-color: none; -fx-border-width: 0;-fx-cursor: hand;");
                bt.setId(i + "," + j);
                bt.setOnAction(this::updateTabulkaPodobnosti);
                if (nejmensiId[i] == j) {
                    bt.setStyle("-fx-background-color: orange;");
                }
                tabulkaDesifrace.add(bt, i + 1, j + 1);
            }
        }
    }

    public HashMap setSifra(HashMap sifraExport) {
        for (int i = 0; i < sifra.znaky.size(); i++) {

            //for(int j=0;j<predloha.znaky.size();j++){
            sifraExport.put(String.valueOf(sifra.znaky.get(i).getZnak()), String.valueOf(predloha.znaky.get(nejmensiId[i]).getZnak()));
            //}
        }
        return sifraExport;
    }

    void updateTabulkaPodobnosti(ActionEvent event) {
        System.out.println("Stisknuto");
        final Node source = (Node) event.getSource();
        String id = source.getId();
        int x = Integer.parseInt(id.split(",")[0]);
        int y = Integer.parseInt(id.split(",")[1]);
        tabulkaDesifrace.getChildren().get((sifra.znaky.size() + predloha.znaky.size()) + nejmensiId[x] + ((x) * predloha.znaky.size())).setStyle("-fx-background-color: none; -fx-border-width: 2;-fx-cursor: hand;-fx-border-radius: 0;");
        tabulkaDesifrace.getChildren().get((sifra.znaky.size() + predloha.znaky.size()) + y + ((x) * predloha.znaky.size())).setStyle("-fx-background-color: orange;");
        nejmensiId[x] = y;
        System.out.println(x);
    }

    class znakyKHodnoceni {

        ArrayList znak = new ArrayList();
        ArrayList pocet = new ArrayList();
        ArrayList idPredloha = new ArrayList();

        znakyKHodnoceni() {

        }

        void add(char vstup) {
            if (true) {

            }
        }
    }

    class NejmensiIdPodleBigramu {

        ArrayList<znakyPredloha> znakySifra = new ArrayList<znakyPredloha>();
        ArrayList<Integer> id = new ArrayList<Integer>();

        NejmensiIdPodleBigramu() {

        }

        znakyPredloha get(int id) {
            boolean neuspech = true;
            for (int i = 0; i < this.id.size(); i++) {
                if (this.id.get(i).equals(id)) {
                    neuspech = false;
                    return this.znakySifra.get(i);
                }
            }
            if (neuspech) {
                this.id.add(id);
                this.znakySifra.add(new znakyPredloha());
                return this.znakySifra.get(znakySifra.size() - 1);
            }
            return null;
        }

        class znakyPredloha {

            ArrayList<Integer> id = new ArrayList<Integer>();
            ArrayList<Integer> pocet = new ArrayList<Integer>();

            znakyPredloha() {

            }

            void add(int id) {
                boolean neuspech = true;
                for (int i = 0; i < this.id.size(); i++) {
                    if (this.id.get(i).equals(id)) {
                        pocet.set(i, pocet.get(i) + 1);
                        neuspech = false;
                    }
                }
                if (neuspech) {
                    this.id.add(id);
                    pocet.add(1);
                }
            }

            int getNejpocetnejsi() {
                int id = 0;
                int pocet = -1;
                for (int i = 0; i < this.pocet.size(); i++) {
                    if (Integer.valueOf(this.pocet.get(i)) > pocet) {
                        pocet = this.pocet.get(i);
                        id = i;
                    }
                }
                if (pocet == -1) {
                    return -1;
                } else {
                    return this.id.get(id);
                }
            }
        }

    }
}
