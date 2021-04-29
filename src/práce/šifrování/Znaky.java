/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package práce.šifrování;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author xxx
 */
public class Znaky {
    private SimpleStringProperty  znak;
    private SimpleIntegerProperty  pocetObjevu;
    private SimpleStringProperty  cetnost;
    private SimpleStringProperty pred;
    private SimpleStringProperty po;
    public Znaky(){
        
    }

    public Znaky(char znak, long pocetObjevu, double cetnost, String pred, String po) {
        this.znak = new SimpleStringProperty(String.valueOf(znak));
        this.pocetObjevu = new SimpleIntegerProperty((int)pocetObjevu);
        this.cetnost = new SimpleStringProperty(Double.toString(cetnost));
        this.pred = new SimpleStringProperty(pred);
        this.po = new SimpleStringProperty(po);
    }

    public String getZnak() {
        return znak.get();
    }

    public void setZnak(String znak) {
        this.znak = new SimpleStringProperty(znak);
    }

    public int getPocetObjevu() {
        return pocetObjevu.get();
    }

    public void setPocetObjevu(int pocetObjevu) {
        this.pocetObjevu = new SimpleIntegerProperty(pocetObjevu);
    }

    public String getCetnost() {
        return cetnost.get();
    }

    public void setCetnost(String cetnost) {
        this.cetnost = new SimpleStringProperty(cetnost);
    }

    public String getPred() {
        return pred.get();
    }

    public void setPred(String pred) {
        this.pred = new SimpleStringProperty(pred);
    }

    public String getPo() {
        return po.get();
    }

    public void setPo(String po) {
        this.po = new SimpleStringProperty(po);
    }
    
}
