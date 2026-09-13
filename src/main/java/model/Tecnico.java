package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tecnico extends Personale {
    private String ruoloSpecializzato;
    private Release releaseAssegnata;

    public Tecnico(String idDipendente, String nome, String cognome, LocalDate dataAssunzione, String ruoloSpecializzato, Release releaseAssegnata) {
        super(idDipendente, nome, cognome, dataAssunzione);
        this.ruoloSpecializzato = ruoloSpecializzato;
        this.releaseAssegnata = releaseAssegnata;

    }

    public String getRuoloSpecializzato() { return ruoloSpecializzato; }
    public void setRuoloSpecializzato(String ruoloSpecializzato) { this.ruoloSpecializzato = ruoloSpecializzato; }

    public Release getReleaseAssegnata(){
        return releaseAssegnata;
    }

    public void setReleaseAssegnata(Release releaseAssegnata){
        this.releaseAssegnata = releaseAssegnata;
    }

}