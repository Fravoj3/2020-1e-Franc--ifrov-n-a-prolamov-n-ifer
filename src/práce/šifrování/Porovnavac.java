/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package práce.šifrování;

import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

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
    int[] nejmensiIdBigram;
    int[] nejmensiIdPodleBigramu;
    int[] nejmensiIdTrigram;

    Porovnavac(AnalyzaZnaku predloha, AnalyzaZnaku sifra, GridPane tabulkaDesifrace) {
        this.tabulkaDesifrace = tabulkaDesifrace;
        this.predloha = predloha;
        this.sifra = sifra;
    }

    public void porovnej() {
        pravdepodobnostZnak = new double[sifra.znaky.size()][predloha.znaky.size()];
        nejmensiId = new int[sifra.znaky.size()];
        nejmensiIdPodleBigramu  = new int[sifra.znaky.size()];
        nejmensiIdBigram = new int[Math.max(sifra.znaky.size(), Math.max(sifra.bigram.size(), predloha.bigram.size()))];
        for (int i = 0; i < sifra.znaky.size(); i++) {
            double nejmensi = Double.MAX_VALUE;
            for (int j = 0; j < predloha.znaky.size(); j++) {
                //if(predloha.znaky.get(j).getPocet() > 10){// 
                double procenta = Math.pow(Math.abs(((double) sifra.znaky.get(i).getPocet() * (100.0 / (double) sifra.getPocetZnaku())) - (predloha.znaky.get(j).getPocet() * (100.0 / (double) predloha.getPocetZnaku()))), 2);
                double procentaPred = 0;
                for (int k = 0; k < Math.min(sifra.znaky.get(i).getZnakyPred().size(), 5); k++) {
                    double procenta2 = -1;
                    for (int l = 0; l < predloha.znaky.get(i).getZnakyPred().size(); l++) {

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
                procentaPo = procentaPo / Math.min(sifra.znaky.get(i).getZnakyPo().size(), 5);
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
        for (int i = sifra.bigram.size()-1; i >=0 ; i--) {
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
            for (int j = 0; j < sifra.znaky.size(); j++) {
                if (sifra.bigram.get(nejmensiIdBigram[i]).getZnak().substring(0).equals(sifra.znaky.get(j).getZnak())) {
                    if (sifra.bigram.get(i).getZnak().substring(0).equals(String.valueOf(sifra.znaky.get(j).getZnak()))) {
                        for (int k = 0; k < predloha.znaky.size(); k++) {
                            if(String.valueOf(predloha.znaky.get(k).getZnak()).equals(predloha.bigram.get(nejmensiIdBigram[i]).getZnak().substring(0))){
                                nejmensiIdPodleBigramu[j] = k;
                                break;
                            }
                        }
                    }
                }
                if (sifra.bigram.get(nejmensiIdBigram[i]).getZnak().substring(1).equals(sifra.znaky.get(j).getZnak())) {
                    if (sifra.bigram.get(i).getZnak().substring(1).equals(String.valueOf(sifra.znaky.get(j).getZnak()))) {
                        for (int k = 0; k < predloha.znaky.size(); k++) {
                            if(String.valueOf(predloha.znaky.get(k).getZnak()).equals(predloha.bigram.get(nejmensiIdBigram[i]).getZnak().substring(1))){
                                nejmensiIdPodleBigramu[j] = k;
                                break;
                            }
                        }
                    }
                }
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
                tabulkaDesifrace.add(bt, i, j + 1);
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
        tabulkaDesifrace.getChildren().get((1 + sifra.znaky.size() + predloha.znaky.size()) + y + ((x) * predloha.znaky.size())).setStyle("-fx-background-color: orange;");
        tabulkaDesifrace.getChildren().get((1 + sifra.znaky.size() + predloha.znaky.size()) + nejmensiId[x] + ((x) * predloha.znaky.size())).setStyle("-fx-background-color: none; -fx-border-width: 2;-fx-cursor: hand;-fx-border-radius: 0;");
        nejmensiId[x] = y;
        System.out.println(x);
    }

}
