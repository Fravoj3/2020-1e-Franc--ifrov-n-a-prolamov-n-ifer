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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import práce.šifrování.Abeceda.Znak;

/**
 *
 * @author xxx
 */
public class FXMLDocumentController implements Initializable {

    AnalyzaZnaku analyza = new AnalyzaZnaku("knihy");
    AnalyzaZnaku[] zasifrovane = new AnalyzaZnaku[50];
    Abeceda text = new Abeceda();
    Abeceda nahled = new Abeceda();
    HashMap<String, String> sifra = new HashMap<String, String>();
    Porovnavac srovnani;

    //stavy 
    @FXML
    private GridPane zasifrovaniAbeceda;

    @FXML
    private GridPane desifraceSeznamVybrani;

    @FXML
    private GridPane tabulkaDesifrace;

    @FXML
    private Label infoRadek;

    @FXML
    private Label zasifrovatNahled;

    @FXML
    private Label nahledDesifrace;

    @FXML
    private BarChart<?, ?> graf;

    @FXML
    private CategoryAxis pismena;

    @FXML
    private NumberAxis procenta;

    @FXML
    private BarChart<?, ?> graf1;

    @FXML
    private CategoryAxis pismena1;

    @FXML
    private NumberAxis procenta1;

    @FXML
    private TableView<Znaky> tabulka;

    @FXML
    private TableView<Znaky> tabulka1;

    @FXML
    private TableColumn<Znaky, String> tabulkaZnak;

    @FXML
    private TableColumn<Znaky, Integer> tabulkaPocet;

    @FXML
    private TableColumn<Znaky, String> tabulkaVyskyt;

    @FXML
    private TableColumn<Znaky, String> tabulkaZnakyPred;

    @FXML
    private TableColumn<Znaky, String> tabulkaZnakyPo;

    @FXML
    private TableColumn<Znaky, String> tabulkaZnak1;

    @FXML
    private TableColumn<Znaky, Integer> tabulkaPocet1;

    @FXML
    private TableColumn<Znaky, String> tabulkaVyskyt1;

    @FXML
    private TableColumn<Znaky, String> tabulkaZnakyPred1;

    @FXML
    private TableColumn<Znaky, String> tabulkaZnakyPo1;

    @FXML
    private ListView<String> listReferencnichTextu;

    @FXML
    private ListView<String> bigram;

    @FXML
    private ListView<String> trigram;

    @FXML
    private ListView<String> bigram1;

    @FXML
    private ListView<String> trigram1;

    @FXML
    private ListView<String> listReferencnichTextu1;

    @FXML
    private ListView<String> seznamKZasifrovani;

    //---------------------------------    Desifrace     -----------------------
    //-----------------     Vlozeni referencino textu
    @FXML
    void vlozitSoubory(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vložit referenční texty");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT soubory (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Všechny typy", "*.*"));
        List<File> file = fileChooser.showOpenMultipleDialog(new Stage());

        for (int i = 0; i < file.size(); i++) {
            System.out.println(file.get(i));
            Files.copy(file.get(i).toPath(),
                    (new File("knihy" + File.separator + file.get(i).getName())).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        UpdateListReferencnichTextu();
    }

    @FXML
    void vymazatRefImport(ActionEvent event) {
        smazatSlozku("knihy");
        UpdateListReferencnichTextu();
    }

    //-----------------     Analyza referencniho textu
    @FXML
    void buttAnalyzovatRef(ActionEvent event) throws UnsupportedEncodingException, IOException {

        infoRadek.setText("Analyzuji text...");

        tabulka.getItems().clear();
        bigram.getItems().clear();
        trigram.getItems().clear();
        graf.getData().clear();
        try {
            analyza.Analyzuj();
            analyza.serad();
            analyza.vytiskni();
            System.out.println("Pred a");
            analyza.vytiskni('U');

            pismena.setLabel("písmena");
            procenta.setLabel("procenta");

            XYChart.Series series = new XYChart.Series();

            for (int i = 0; i < analyza.znaky.size(); i++) {
                double pocet = (analyza.znaky.get(i).getPocet() * (100.0 / (double) analyza.getPocetZnaku()));
                if (pocet > 0.2) {
                    String jmenoZnak = String.valueOf(analyza.znaky.get(i).getZnak());
                    series.getData().add(new XYChart.Data(jmenoZnak, pocet));

                }
                String znakyPred = "";
                String znakyPo = "";
                for (int k = 0; k < analyza.znaky.get(i).getZnakyPred().size(); k++) {
                    if (k < 8) {
                        znakyPred = znakyPred + analyza.znaky.get(i).getZnakyPred().get(k).getZnak() + "(" + analyza.znaky.get(i).getZnakyPred().get(k).getCetnost() + "%) , ";
                    }
                }
                for (int k = 0; k < analyza.znaky.get(i).getZnakyPo().size(); k++) {
                    if (k < 8) {
                        znakyPo = znakyPo + analyza.znaky.get(i).getZnakyPo().get(k).getZnak() + "(" + analyza.znaky.get(i).getZnakyPo().get(k).getCetnost() + "%) , ";
                    }
                }
                tabulka.getItems().add(new Znaky(analyza.znaky.get(i).getZnak(), analyza.znaky.get(i).getPocet(), pocet, znakyPred, znakyPo));
            }
            graf.getData().add(series);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        double pocetBigram = 0;
        for (int k = 0; k < analyza.bigram.size(); k++) {
            pocetBigram += analyza.bigram.get(k).getPocet();
        }
        for (int j = 0; j < analyza.bigram.size(); j++) {
            if (!analyza.bigram.get(j).getZnak().contains(" ")) {
                bigram.getItems().add(analyza.bigram.get(j).getZnak() + " ("+analyza.bigram.get(j).getPocet() * (100.0 / pocetBigram)+ "%))");
            }
        }
        
        double pocetTrigram = 0;
        for (int k = 0; k < analyza.trigram.size(); k++) {
            pocetTrigram += analyza.trigram.get(k).getPocet();
        }
        for (int j = 0; j < analyza.trigram.size(); j++) {
                trigram.getItems().add(analyza.trigram.get(j).getZnak()+" ("+analyza.trigram.get(j).getPocet() * (100.0 / pocetTrigram)+"%)");
        }
        infoRadek.setText("Analýza dokončena");
    }

    //-----------------     Vlozeni zasifrovaneho textu
    @FXML
    void vlozitSoubory1(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vložit texty k dešifrování stejnou šifrou");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT soubory (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Všechny typy", "*.*"));
        List<File> file = fileChooser.showOpenMultipleDialog(new Stage());

        for (int i = 0; i < file.size(); i++) {
            System.out.println(file.get(i));
            Files.copy(file.get(i).toPath(),
                    (new File("zasifrovane" + File.separator + file.get(i).getName())).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        UpdateListReferencnichTextu1();
    }

    @FXML
    void vymazatRefImport1(ActionEvent event) {
        smazatSlozku("zasifrovane");
        UpdateListReferencnichTextu1();
    }

    //-----------------     Analyza zasifrovaneho textu
    @FXML
    void buttAnalyzovatRef1(ActionEvent event) throws UnsupportedEncodingException, IOException {
        infoRadek.setText("Analyzuji text...");
        bigram1.getItems().clear();
        trigram1.getItems().clear();
        tabulka1.getItems().clear();
        graf1.getData().clear();
        try {
            zasifrovane[0] = new AnalyzaZnaku("zasifrovane");
            zasifrovane[0].Analyzuj();
            zasifrovane[0].serad();
            zasifrovane[0].vytiskni();

            pismena1.setLabel("písmena");
            procenta1.setLabel("procenta");

            XYChart.Series series = new XYChart.Series();

            for (int i = 0; i < zasifrovane[0].znaky.size(); i++) {
                double pocet = (zasifrovane[0].znaky.get(i).getPocet() * (100.0 / (double) zasifrovane[0].getPocetZnaku()));
                if (pocet > 0.2) {
                    String jmenoZnak = String.valueOf(zasifrovane[0].znaky.get(i).getZnak());
                    series.getData().add(new XYChart.Data(jmenoZnak, pocet));

                }
                String znakyPred = "";
                String znakyPo = "";
                for (int k = 0; k < zasifrovane[0].znaky.get(i).getZnakyPred().size(); k++) {
                    if (k < 8) {
                        znakyPred = znakyPred + zasifrovane[0].znaky.get(i).getZnakyPred().get(k).getZnak() + "(" + zasifrovane[0].znaky.get(i).getZnakyPred().get(k).getCetnost() + "%) , ";
                    }
                }
                for (int k = 0; k < zasifrovane[0].znaky.get(i).getZnakyPo().size(); k++) {
                    if (k < 8) {
                        znakyPo = znakyPo + zasifrovane[0].znaky.get(i).getZnakyPo().get(k).getZnak() + "(" + zasifrovane[0].znaky.get(i).getZnakyPo().get(k).getCetnost() + "%) , ";
                    }
                }
                tabulka1.getItems().add(new Znaky(zasifrovane[0].znaky.get(i).getZnak(), zasifrovane[0].znaky.get(i).getPocet(), pocet, znakyPred, znakyPo));
            }
            graf1.getData().add(series);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        double pocetBigram = 0;
        for (int k = 0; k < zasifrovane[0].bigram.size(); k++) {
            pocetBigram += zasifrovane[0].bigram.get(k).getPocet();
        }
        for (int j = 0; j < zasifrovane[0].bigram.size(); j++) {
            bigram1.getItems().add(zasifrovane[0].bigram.get(j).getZnak() + " ("+zasifrovane[0].bigram.get(j).getPocet() * (100.0 / pocetBigram)+ "%) (" + zasifrovane[0].bigram.get(j).getPocet() + ")");
        }
        
        double pocetTrigram = 0;
        for (int k = 0; k < zasifrovane[0].trigram.size(); k++) {
            pocetTrigram += zasifrovane[0].trigram.get(k).getPocet();
        }
        for (int j = 0; j < zasifrovane[0].trigram.size(); j++) {
                trigram1.getItems().add(zasifrovane[0].trigram.get(j).getZnak() +" ("+zasifrovane[0].trigram.get(j).getPocet() * (100.0 / pocetTrigram)+"%)");
        }
        infoRadek.setText("Analýza dokončena");
    }

    //-----------------     Desifrace
    @FXML
    void najitPodobnosti(ActionEvent event) throws InterruptedException {
        if (zasifrovane[0].getPocetZnaku() != 0 && analyza.getPocetZnaku() != 0) {
            tabulkaDesifrace.getChildren().clear();
            infoRadek.setText("Hledám podobnosti...");
            srovnani = new Porovnavac(analyza, zasifrovane[0], tabulkaDesifrace);
            srovnani.porovnej();

            try {
                nahled.nacti("zasifrovane");
                sifra = srovnani.setSifra(sifra);
                System.out.println(sifra);
                UpdateNahledDesifrace(sifra);

            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            for (int j = 0; j < zasifrovane[0].znaky.size(); j++) {
                Button bt = new Button("(" + String.valueOf(zasifrovane[0].znaky.get(j).getZnak()) + ")");
                bt.setMinWidth(40);
                bt.setStyle("-fx-background-color: none; -fx-border-width: 0;");
                tabulkaDesifrace.add(bt, j + 1, 0);
            }
            for (int j = 0; j < analyza.znaky.size(); j++) {
                Button bt = new Button("(" + String.valueOf(analyza.znaky.get(j).getZnak()) + ")");
                bt.setMinWidth(40);
                bt.setStyle("-fx-background-color: none; -fx-border-width: 0;");
                tabulkaDesifrace.add(bt, 0, j + 1);
            }
            try {
                srovnani.vykresli();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            infoRadek.setText("Podobnosti nalezeny");
        } else {
            infoRadek.setText("Chybí zanalyzované podklady");
        }
    }

    @FXML
    void updateNahleduDesifrace(ActionEvent event) {
        if (sifra.size() != 0) {
            sifra = srovnani.setSifra(sifra);

            UpdateNahledDesifrace(sifra);
        } else {
            infoRadek.setText("Nejprve nutné nalézt podobnosti");
        }
    }

    @FXML
    void desifraceExport(ActionEvent event) {
        if (sifra.size() != 0) {
            sifra = srovnani.setSifra(sifra);
            try {
                nahled.ulozit("zasifrovane", sifra);
            } catch (UnsupportedEncodingException ex) {
                infoRadek.setText("Ukládání se nezdařilo");
            } catch (IOException ex) {
                infoRadek.setText("Ukládání se nezdařilo");
            }
        } else {
            infoRadek.setText("Nejprve nutné nalézt podobnosti");
        }
    }

    //--------------------------------    Zasifrovani     ----------------------
    //-----------------     Import
    @FXML
    void vlozitKZasifrovani(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vložit texty k zašifrování");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("TXT soubory (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Všechny typy", "*.*"));
        List<File> file = fileChooser.showOpenMultipleDialog(new Stage());

        for (int i = 0; i < file.size(); i++) {
            System.out.println(file.get(i));
            Files.copy(file.get(i).toPath(),
                    (new File("import" + File.separator + file.get(i).getName())).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        UpdateListImportKZasifrovani();
    }

    @FXML
    void vymazatKZasifrovani(ActionEvent event) {
        smazatSlozku("import");
        UpdateListImportKZasifrovani();
    }

    //-----------------     ziskani abecedy
    @FXML
    public void zjistiAbecedu(ActionEvent event) {
        zasifrovaniAbeceda.setStyle("-fx-min-width: 180;");
        try {
            text.nacti("import");
            text.serad();
            for (int i = 0; i < text.getZnaky().size(); i++) {
                ChoiceBox chb = new ChoiceBox();
                chb.setValue(chb);
                for (int j = 0; j < text.getZnaky().size(); j++) {
                    chb.getItems().add(String.valueOf(text.getZnaky().get(j).znak));
                }
                desifraceSeznamVybrani.add(chb, 2, i + 1);
            }

            for (int i = 0; i < text.getZnaky().size(); i++) {
                Button bt = new Button(String.valueOf(text.getZnaky().get(i).znak));
                bt.setStyle("-fx-background-color: none; -fx-border-width: 0;-fx-min-width: 50;");
                zasifrovaniAbeceda.add(bt, 0, i + 1);
                bt = new Button(String.valueOf(text.getZnaky().get(i).getIndex()));
                bt.setStyle("-fx-background-color: none; -fx-border-width: 0;-fx-min-width: 50;");
                zasifrovaniAbeceda.add(bt, 1, i + 1);

            }
            Button bt = new Button("písmeno");
            bt.setStyle("-fx-background-color: none; -fx-border-width: 0;");
            zasifrovaniAbeceda.add(bt, 0, 0);
            bt = new Button("UTF-8 index");
            bt.setStyle("-fx-background-color: none; -fx-border-width: 0;");
            zasifrovaniAbeceda.add(bt, 1, 0);
        } catch (UnsupportedEncodingException ex) {
            infoRadek.setText("Nepodporované kódování souboru");
        } catch (IOException ex) {
            infoRadek.setText("Soubor se nepodařilo načíst");
        }
    }

    @FXML
    public void nahodneZasifruj(ActionEvent event) {
        desifraceSeznamVybrani.getChildren().clear();
        ArrayList<Znak> kProhazeni = (ArrayList<Znak>) text.getZnaky().clone();
        for (int i = 0; i < text.getZnaky().size(); i++) {
            Random random = new Random();
            int indexZmeny = random.nextInt(kProhazeni.size());

            ChoiceBox chb = new ChoiceBox();
            chb.setValue(chb);
            chb.setValue(String.valueOf(kProhazeni.get(indexZmeny).znak));
            for (int j = 0; j < text.getZnaky().size(); j++) {
                chb.getItems().add(String.valueOf(text.getZnaky().get(j).znak));
            }
            desifraceSeznamVybrani.add(chb, 2, i + 1);
            text.getZnaky().get(i).setSifrovatNa(kProhazeni.get(indexZmeny).znak);
            kProhazeni.remove(indexZmeny);
        }
    }

    //-----------------     Export
    @FXML
    public void zasifrovaneExport(ActionEvent event) {
        if (text.znaky.size() != 0) {
            try {
                text.ulozit();
                infoRadek.setText("Soubor byl úspěšně uložen");
            } catch (UnsupportedEncodingException ex) {
                infoRadek.setText("Soubor se nepodařilo uložit");
            } catch (IOException ex) {
                infoRadek.setText("Soubor se nepodařilo uložit");
            }
        } else {
            infoRadek.setText("Nejprve je nutné nastavit šifrování");
        }
    }

    @FXML
    public void sifrovat(ActionEvent event) {
        if (text.znaky.size() != 0) {
            try {
                zasifrovatNahled.setText(text.getNahled());
            } catch (UnsupportedEncodingException ex) {
                infoRadek.setText("Chyba při šifrování");
            } catch (IOException ex) {
                infoRadek.setText("Chyba při šifrování");
            }
        } else {
            infoRadek.setText("Nejprve je nutné nastavit šifrování");
        }
    }

    void TabulkaVolani() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabulkaZnak.setCellValueFactory(new PropertyValueFactory<Znaky, String>("znak"));
        tabulkaPocet.setCellValueFactory(new PropertyValueFactory<Znaky, Integer>("pocetObjevu"));
        tabulkaVyskyt.setCellValueFactory(new PropertyValueFactory<Znaky, String>("cetnost"));
        tabulkaZnakyPred.setCellValueFactory(new PropertyValueFactory<Znaky, String>("pred"));
        tabulkaZnakyPo.setCellValueFactory(new PropertyValueFactory<Znaky, String>("po"));

        tabulkaZnak1.setCellValueFactory(new PropertyValueFactory<Znaky, String>("znak"));
        tabulkaPocet1.setCellValueFactory(new PropertyValueFactory<Znaky, Integer>("pocetObjevu"));
        tabulkaVyskyt1.setCellValueFactory(new PropertyValueFactory<Znaky, String>("cetnost"));
        tabulkaZnakyPred1.setCellValueFactory(new PropertyValueFactory<Znaky, String>("pred"));
        tabulkaZnakyPo1.setCellValueFactory(new PropertyValueFactory<Znaky, String>("po"));

        UpdateListReferencnichTextu();
        UpdateListReferencnichTextu1();
        UpdateListImportKZasifrovani();
    }

    void UpdateListReferencnichTextu() {
        listReferencnichTextu.getItems().clear();
        String[] soubory = new File("knihy").list();
        for (int i = 0; i < soubory.length; i++) {
            listReferencnichTextu.getItems().add(soubory[i]);
        }
    }

    void UpdateListReferencnichTextu1() {
        listReferencnichTextu1.getItems().clear();
        String[] soubory = new File("zasifrovane").list();
        for (int i = 0; i < soubory.length; i++) {
            listReferencnichTextu1.getItems().add(soubory[i]);
        }
    }

    void UpdateListImportKZasifrovani() {
        seznamKZasifrovani.getItems().clear();
        String[] soubory = new File("import").list();
        for (int i = 0; i < soubory.length; i++) {
            seznamKZasifrovani.getItems().add(soubory[i]);
        }
    }

    void smazatSlozku(String slozka) {
        String[] soubory = new File(slozka).list();
        for (int i = 0; i < soubory.length; i++) {
            new File(slozka + File.separator + soubory[i]).delete();
        }
    }

    public void UpdateNahledDesifrace(HashMap<String, String> sifra) {
        try {
            nahledDesifrace.setText(nahled.getNahled("zasifrovane", sifra));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
