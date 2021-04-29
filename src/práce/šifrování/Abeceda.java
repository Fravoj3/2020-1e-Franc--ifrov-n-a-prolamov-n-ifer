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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author xxx
 */
public class Abeceda {

    ArrayList<Znak> znaky = new ArrayList<Znak>();

    Abeceda() {

    }

    public void nacti(String slozka) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File f = new File(slozka);
        String[] soubory = f.list();
        System.out.println("Zahajeni");
        for (int i = 0; i < soubory.length; i++) {
            File soubor = new File(slozka + File.separator + soubory[i]);
            FileInputStream fis = new FileInputStream(soubor);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader ctecka = new BufferedReader(isr);
            String line = ctecka.readLine();

            while (line != null) {
                char[] linePole = line.toCharArray();
                for (int a = 0; a < linePole.length; a++) {
                    boolean existuje = false;
                    for (int j = 0; j < znaky.size(); j++) {
                        if (znaky.get(j).getZnak() == linePole[a]) {
                            existuje = true;
                            break;
                        }
                    }
                    if (!existuje) {
                        znaky.add(new Znak(linePole[a]));
                    }
                }
                line = ctecka.readLine();
            }
        }

        System.out.println("Dokonceni");
    }

    public void serad() {
        for (int i = 0; i < znaky.size(); i++) {
            for (int j = 0; j < znaky.size(); j++) {
                if ((int) znaky.get(i).getZnak() < (int) znaky.get(j).getZnak()) {
                    Znak mensi = znaky.get(j);
                    znaky.set(j, znaky.get(i));
                    znaky.set(i, mensi);
                }
            }
        }
        System.out.println("Serazeno");
    }

    public String getNahled() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        HashMap<String, String> sifra = new HashMap<String, String>();
        for(int j=0;j<znaky.size();j++){
            sifra.put(String.valueOf(znaky.get(j).getZnak()), String.valueOf(znaky.get(j).getSifrovatNa()));
        }
        System.out.println(sifra);
        File f = new File("import");
        String[] soubory = f.list();
        System.out.println("Zahajeni");
        String vysledek = "";
        for (int i = 0; i < Math.min(soubory.length,2); i++) {
            File soubor = new File("import" + File.separator + soubory[i]);
            FileInputStream fis = new FileInputStream(soubor);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader ctecka = new BufferedReader(isr);
            String line = ctecka.readLine();

            int pocetKNahlednuti = 0;
            while (line != null) {
                char[] radek = line.toCharArray();
                for(int j=0;j<radek.length;j++){
                    pocetKNahlednuti++;
                    vysledek = vysledek +sifra.get(String.valueOf(radek[j]));
                    System.out.println(String.valueOf(radek[j]));
                }
                if(pocetKNahlednuti>1000){
                    break;
                }
                line = ctecka.readLine();
            }
        }
        System.out.println("dokonceno...");
        return vysledek;
    }
    
    public String getNahled(String cesta,HashMap<String, String>sifra) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        
        File f = new File(cesta);
        String[] soubory = f.list();
        System.out.println("Zahajeni");
        String vysledek = "";
        for (int i = 0; i < Math.min(soubory.length,2); i++) {
            File soubor = new File(cesta + File.separator + soubory[i]);
            FileInputStream fis = new FileInputStream(soubor);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader ctecka = new BufferedReader(isr);
            String line = ctecka.readLine();

            int pocetKNahlednuti = 0;
            while (line != null) {
                char[] radek = line.toCharArray();
                for(int j=0;j<radek.length;j++){
                    pocetKNahlednuti++;
                    vysledek = vysledek +sifra.get(String.valueOf(radek[j]));
                    if(pocetKNahlednuti>1000){
                    break;
                    }
                }
                if(pocetKNahlednuti>1000){
                    break;
                }
                line = ctecka.readLine();
            }
        }
        System.out.println(vysledek);
        System.out.println("dokonceno...");
        return vysledek;
    }
    
    public String getNahled(HashMap sifra) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        
        System.out.println(sifra);
        File f = new File("import");
        String[] soubory = f.list();
        System.out.println("Zahajeni");
        String vysledek = "";
        for (int i = 0; i < Math.min(soubory.length,2); i++) {
            File soubor = new File("import" + File.separator + soubory[i]);
            FileInputStream fis = new FileInputStream(soubor);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader ctecka = new BufferedReader(isr);
            String line = ctecka.readLine();

            int pocetKNahlednuti = 0;
            while (line != null) {
                char[] radek = line.toCharArray();
                for(int j=0;j<radek.length;j++){
                    pocetKNahlednuti++;
                    vysledek = vysledek +sifra.get(String.valueOf(radek[j]));
                    System.out.println(String.valueOf(radek[j]));
                }
                if(pocetKNahlednuti>1000){
                    break;
                }
                line = ctecka.readLine();
            }
        }
        System.out.println("dokonceno...");
        return vysledek;
    }
    
    public void ulozit() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        HashMap<String, String> sifra = new HashMap<String, String>();
        for(int j=0;j<znaky.size();j++){
            sifra.put(String.valueOf(znaky.get(j).getZnak()), String.valueOf(znaky.get(j).getSifrovatNa()));
        }
        System.out.println(sifra);
        File f = new File("import");
        String[] soubory = f.list();
        System.out.println("Zahajeni");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        File dest = fileChooser.showSaveDialog(new Stage());
        
        System.out.println("ukladam");
        for (int i = 0; i < soubory.length; i++) {
            File kUlozeni = new File("export"+File.separator+soubory[i]+".txt");
            FileWriter fw = new FileWriter(kUlozeni);
            File soubor = new File("import" + File.separator + soubory[i]);
            FileInputStream fis = new FileInputStream(soubor);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader ctecka = new BufferedReader(isr);
            String line = ctecka.readLine();
            

            while (line != null) {
                String vysledek = "";
                char[] radek = line.toCharArray();
                for(int j=0;j<radek.length;j++){
                    vysledek = vysledek +sifra.get(String.valueOf(radek[j]));
                    System.out.println(String.valueOf(radek[j]));
                }
                fw.write(vysledek);
                line = ctecka.readLine();
            }
            fw.close();
            
        
        if (dest != null) {
            try {
                Files.copy(kUlozeni.toPath(), dest.toPath());
                kUlozeni.delete();
            } catch (IOException ex) {
                // handle exception...
            }
        }
        }
        System.out.println("dokonceno...");
        System.out.println(sifra.get("m"));
    }

    public ArrayList<Znak> getZnaky() {
        return znaky;
    }

    class Znak {

        char znak;
        char sifrovatNa;

        Znak(char znak) {
            this.znak = znak;
        }

        public char getZnak() {
            return znak;
        }

        public int getIndex() {
            return (int) znak;
        }

        public char getSifrovatNa() {
            return sifrovatNa;
        }

        public void setSifrovatNa(char sifrovatNa) {
            this.sifrovatNa = sifrovatNa;
        }

    }
}
