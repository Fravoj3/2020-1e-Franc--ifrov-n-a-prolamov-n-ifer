/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package práce.šifrování;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Double.NaN;
import java.util.ArrayList;

/**
 *
 * @author xxx
 */
public class AnalyzaZnaku {

    String slozka;
    String cesta;
    String[] soubory;
    int pocetZnaku = 0;
    ArrayList<AnalyzaZnak> znaky = new ArrayList<AnalyzaZnak>();
    ArrayList<Bigram> bigram = new ArrayList<Bigram>();
    ArrayList<Bigram> trigram = new ArrayList<Bigram>();

    AnalyzaZnaku(String slozka) {
        this.slozka = slozka;
        File f = new File(slozka);
        soubory = f.list();
    }

    public void Analyzuj() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File f = new File(slozka);
        soubory = f.list();
        System.out.println("Zahajeni");
        for (int i = 0; i < soubory.length; i++) {
            File soubor = new File(slozka + File.separator + soubory[i]);
            FileInputStream fis = new FileInputStream(soubor);
            FileReader reader = new FileReader(soubor);
            int a = reader.read();
            int b = reader.read();
            int c = reader.read();
            while (true) {
                if (a == -1 || b == -1 || c == -1) {
                    break;
                }
                int znakPred = a;
                int znak = b;
                int znakPo = c;

                pocetZnaku++;
                if (pocetZnaku % 10000 == 0) {
                    System.out.println(pocetZnaku);
                    System.out.println(bigram.size());
                }

                boolean existuje = false;
                for (int j = 0; j < znaky.size(); j++) {
                    if (znaky.get(j).getZnak() == (char) znak) {
                        znaky.get(j).zvysPocet((char) znakPred, (char) znakPo);
                        existuje = true;
                        break;
                    }
                }
                if (!existuje) {
                    znaky.add(new AnalyzaZnak((char) znak, (char) znakPred, (char) znakPo));
                }
                String bigramNyni = String.valueOf((char) znak) + String.valueOf((char) znakPo);
                existuje = false;
                for (int j = 0; j < bigram.size(); j++) {
                    if (bigram.get(j).getZnak().equals(bigramNyni)) {
                        bigram.get(j).zvysPocet();
                        existuje = true;
                        break;
                    }
                }
                if (!existuje) {
                    bigram.add(new Bigram(bigramNyni));
                }

                String trigramNyni = String.valueOf((char) znakPred) + String.valueOf((char) znak) + String.valueOf((char) znakPo);
                if (!trigramNyni.contains(" ") && slozka.equals("knihy")) {
                    existuje = false;
                    for (int j = 0; j < trigram.size(); j++) {
                        if (trigram.get(j).getZnak().equals(trigramNyni)) {
                            trigram.get(j).zvysPocet();
                            existuje = true;
                            break;
                        }
                    }
                    if (!existuje) {
                        trigram.add(new Bigram(trigramNyni));
                    }
                }

                a = b;
                b = c;
                c = reader.read();
            }
            /*InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            System.out.println(new InputStreamReader(fis).getEncoding());
            BufferedReader ctecka = new BufferedReader(isr);
            try {
                String line = ctecka.readLine();

                while (line != null) {
                    char[] linePole = line.toCharArray();
                    for (int a = 1; a < linePole.length - 1; a++) {

                    }
                    line = ctecka.readLine();
                }
            } catch (IOException ex) {
                Logger.getLogger(AnalyzaZnaku.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
        System.out.println("Dokonceni");
    }

    void vytiskni() {
        for (int i = 0; i < znaky.size(); i++) {
            System.out.println(znaky.get(i).getZnak() + " " + znaky.get(i).getPocet());
        }
        System.out.println(znaky.size());
    }

    void vytiskni(char pismeno) {
        for (int i = 0; i < znaky.size(); i++) {
            if (znaky.get(i).getZnak() == pismeno) {
                for (int j = 0; j < znaky.get(i).getZnakyPred().size(); j++) {
                    System.out.println(znaky.get(i).getZnakyPred().get(j).getZnak() + " " + znaky.get(i).getZnakyPred().get(j).getPocet());
                }
            }
        }
    }

    public void serad() {
        for (int i = 0; i < znaky.size(); i++) {
            double pocetZnakuPred = 0;
            for (int j = 0; j < znaky.get(i).getZnakyPred().size(); j++) {
                pocetZnakuPred += znaky.get(i).getZnakyPred().get(j).getPocet();
                for (int k = 0; k < znaky.get(i).getZnakyPred().size(); k++) {
                    if (znaky.get(i).getZnakyPred().get(j).getPocet() > znaky.get(i).getZnakyPred().get(k).getPocet()) {
                        Znak mensi = znaky.get(i).getZnakyPred().get(j);
                        znaky.get(i).getZnakyPred().set(j, znaky.get(i).getZnakyPred().get(k));
                        znaky.get(i).getZnakyPred().set(k, mensi);
                    }
                }
            }
            for (int j = 0; j < znaky.get(i).getZnakyPred().size(); j++) {
                znaky.get(i).getZnakyPred().get(j).setCetnost((int) (znaky.get(i).getZnakyPred().get(j).getPocet() * (100.0 / pocetZnakuPred)));
            }
            long pocetZnakuPo = 0;
            for (int j = 0; j < znaky.get(i).getZnakyPo().size(); j++) {
                pocetZnakuPo += znaky.get(i).getZnakyPo().get(j).getPocet();
                for (int k = 0; k < znaky.get(i).getZnakyPo().size(); k++) {
                    if (znaky.get(i).getZnakyPo().get(j).getPocet() > znaky.get(i).getZnakyPo().get(k).getPocet()) {
                        Znak mensi = znaky.get(i).getZnakyPo().get(j);
                        znaky.get(i).getZnakyPo().set(j, znaky.get(i).getZnakyPo().get(k));
                        znaky.get(i).getZnakyPo().set(k, mensi);
                    }
                }
            }
            for (int j = 0; j < znaky.get(i).getZnakyPo().size(); j++) {
                znaky.get(i).getZnakyPo().get(j).setCetnost((int) (znaky.get(i).getZnakyPo().get(j).getPocet() * (100.0 / Double.valueOf(pocetZnakuPo))));
            }
            for (int j = 0; j < znaky.size(); j++) {
                if (znaky.get(i).getPocet() > znaky.get(j).getPocet()) {
                    AnalyzaZnak mensi = znaky.get(i);
                    znaky.set(i, znaky.get(j));
                    znaky.set(j, mensi);
                }
            }
        }
        for (int i = 0; i < bigram.size(); i++) {
            for (int j = 0; j < bigram.size(); j++) {
                if (bigram.get(i).getPocet() > bigram.get(j).getPocet()) {
                    Bigram mensi = bigram.get(i);
                    bigram.set(i, bigram.get(j));
                    bigram.set(j, mensi);
                }
            }
        }
        for (int i = 0; i < trigram.size(); i++) {
            for (int j = 0; j < trigram.size(); j++) {
                if (trigram.get(i).getPocet() > trigram.get(j).getPocet()) {
                    Bigram mensi = trigram.get(i);
                    trigram.set(i, trigram.get(j));
                    trigram.set(j, mensi);
                }
            }
        }
    }

    public int getPocetZnaku() {
        return pocetZnaku;
    }

    //------------------------   třída Analyza znak   --------------------------
    class AnalyzaZnak extends Znak {

        ArrayList<Znak> znakyPred = new ArrayList<Znak>();
        ArrayList<Znak> znakyPo = new ArrayList<Znak>();
        char zasifrovane;

        public AnalyzaZnak(char znak, char znakPred, char znakPo) {
            super(znak);
            if (znakPred != NaN) {
                pridejZnakPred(znakPred);
            }
            if (znakPo != NaN) {
                pridejZnakPo(znakPo);
            }
        }

        public void pridejZnakPred(char znak) {
            boolean existuje = false;
            for (int i = 0; i < znakyPred.size(); i++) {
                if (znakyPred.get(i).getZnak() == znak) {
                    existuje = true;
                    znakyPred.get(i).zvysPocet();
                    break;
                }
            }
            if (!existuje) {
                znakyPred.add(new Znak(znak));
            }
        }

        public void pridejZnakPo(char znak) {
            boolean existuje = false;
            for (int i = 0; i < znakyPo.size(); i++) {
                if (znakyPo.get(i).getZnak() == znak) {
                    existuje = true;
                    znakyPo.get(i).zvysPocet();
                    break;
                }
            }
            if (!existuje) {
                znakyPo.add(new Znak(znak));
            }
        }

        public ArrayList<Znak> getZnakyPred() {
            return znakyPred;
        }

        public ArrayList<Znak> getZnakyPo() {
            return znakyPo;
        }

        public void zvysPocet(char pred, char po) {
            pocet++;
            if (pred != NaN) {
                pridejZnakPred(pred);
            }
            if (po != NaN) {
                pridejZnakPo(po);
            }
        }

        public char getZasifrovane() {
            return zasifrovane;
        }

        public void setZasifrovane(char zasifrovane) {
            this.zasifrovane = zasifrovane;
        }

    }

//----------------------------  trida zank   -----------------------------------
    class Znak {

        final char znak;
        long pocet;
        double sance;
        private int cetnost;

        Znak(char znak) {
            this.znak = znak;
            pocet = 1;
        }

        public char getZnak() {
            return znak;
        }

        public long getPocet() {
            return pocet;
        }

        public int getCetnost() {
            return cetnost;
        }

        public void zvysPocet() {
            this.pocet++;
        }

        public void setCetnost(int cetnost) {
            this.cetnost = cetnost;
        }

        public double getSance() {
            return sance;
        }

        public void setSance(double sance) {
            this.sance = sance;
        }

    }

    class Bigram {

        final String znak;
        long pocet;
        double sance;
        private int cetnost;

        Bigram(String znak) {
            this.znak = znak;
            pocet = 1;
        }

        public String getZnak() {
            return znak;
        }

        public long getPocet() {
            return pocet;
        }

        public int getCetnost() {
            return cetnost;
        }

        public void zvysPocet() {
            this.pocet++;
        }

        public void setCetnost(int cetnost) {
            this.cetnost = cetnost;
        }

        public double getSance() {
            return sance;
        }

        public void setSance(double sance) {
            this.sance = sance;
        }

    }
}
