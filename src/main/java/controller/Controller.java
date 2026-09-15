package controller;


import eccezioni.BudgetException;
import eccezioni.DatabaseException;
import eccezioni.DatiInvalidiException;
import implementazionePostgresDAO.UtenteImplementazionePostgresDAO;
import model.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class Controller {


    private dao.ManagerDAO managerDAO;
    private dao.TecnicoDAO tecnicoDAO;
    private dao.ArtistaDAO artistaDAO;
    private dao.ReleaseDAO releaseDAO;
    private dao.CampagnaMarketingDAO campagnaMarketingDAO;
    private dao.DipartimentoDAO dipartimentoDAO;
    private dao.RoyaltyReportDAO royaltyReportDAO;
    private dao.UtenteDAO utenteDAO;

    public Controller() {

        this.managerDAO = new implementazionePostgresDAO.ManagerImplementazionePostgresDAO();
        this.tecnicoDAO = new implementazionePostgresDAO.TecnicoImplementazionePostgresDAO();
        this.artistaDAO = new implementazionePostgresDAO.ArtistaImplementazionePostgresDAO();
        this.releaseDAO = new implementazionePostgresDAO.ReleaseImplementazionePostgresDAO();
        this.campagnaMarketingDAO = new implementazionePostgresDAO.CampagnaMarketingImplementazionePostgresDAO();
        this.dipartimentoDAO = new implementazionePostgresDAO.DipartimentoImplementazionePostgresDAO();
        this.royaltyReportDAO = new implementazionePostgresDAO.RoyaltyReportImplementazionePostgresDAO();
        this.utenteDAO = new UtenteImplementazionePostgresDAO();


    }

    //METODI PER ARTISTA
    public void registraNuovoArtista(String id, String nomeArte, String genereMusicale, LocalDate dataInizio, LocalDate dataFine, Manager manager)  throws DatiInvalidiException, DatabaseException {
        if (nomeArte == null || nomeArte.trim().isEmpty() || id == null || id.trim().isEmpty()  || genereMusicale == null || genereMusicale.isEmpty() || dataInizio == null || dataFine == null) {
            throw new DatiInvalidiException("Tutti i campi sono obbligatori");
        }

        Artista nuovoArtista = new Artista(id, nomeArte, genereMusicale, dataInizio, dataFine, manager);

        artistaDAO.salvaArtista(nuovoArtista);

        System.out.println("Artista Registrato: " + nuovoArtista.getNomeArte());
    }
    public void eliminaArtista(String idArtista) throws DatabaseException {
        artistaDAO.eliminaArtista(idArtista);
        System.out.println("Artista eliminato dal DB: " + idArtista);
    }

    public List<Artista> getTuttiGliArtisti() throws DatabaseException{

        return artistaDAO.getTuttiGliArtisti();
    }
    public void assegnaManagerAdArtista(String idArtista, String idManager) throws DatabaseException {
        artistaDAO.assegnaManager(idArtista, idManager);
        System.out.println("Manager " + idManager + " assegnato all'artista " + idArtista);
    }

    //METODI PER MANAGER
    public void registraNuovoManager(String id, String nome, String cognome, LocalDate dataAssunzione, Double bonus) throws DatabaseException, DatiInvalidiException{

        if (id == null || id.trim().isEmpty() || nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty() || dataAssunzione == null || bonus == null) {
            throw new DatiInvalidiException("Tutti i dati del manager sono obbligatori.");
        }

        Manager nuovoManager = new Manager(id, nome, cognome, dataAssunzione, bonus);

        managerDAO.salvaManager(nuovoManager);

        System.out.println("Manager Registrato nel DB: " + nuovoManager.getNome() + " " + nuovoManager.getCognome());
    }

    public List<Manager> getTuttiIManager() throws DatabaseException{

        return managerDAO.getTuttiIManager();
    }
    public void eliminaManager(String idDipendente) throws DatabaseException{
        managerDAO.eliminaManager(idDipendente);
        System.out.println("Manager eliminato dal DB: " + idDipendente);
    }
    // METODI PER TECNICI
    public void registraNuovoTecnico(String id, String nome, String cognome, LocalDate dataAssunzione, String ruoloSpecializzato, String codiceRelease) throws DatabaseException, DatiInvalidiException {
        if (id == null || id.trim().isEmpty() || nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty() || ruoloSpecializzato == null || ruoloSpecializzato.trim().isEmpty()) {
            throw new DatiInvalidiException("Tutti i dati del tecnico sono obbligatori.");
        }
        Release releaseAssociata = null;
        if (codiceRelease != null) {
            releaseAssociata = new Release(codiceRelease, "", "", null, "", null, new ArrayList<>());
        }

        Tecnico nuovoTecnico = new model.Tecnico(id, nome, cognome, dataAssunzione, ruoloSpecializzato, releaseAssociata);

        tecnicoDAO.salvaTecnico(nuovoTecnico);

        System.out.println("Tecnico registrato: " + nome + " " + cognome + " | Ruolo: " + ruoloSpecializzato);
    }
    public List<Tecnico> getTuttiITecnici() throws DatabaseException {
        return tecnicoDAO.getTuttiITecnici();
    }
    public List<Tecnico> getTecniciDiRelease(String codiceRelease) throws DatabaseException {
        return tecnicoDAO.getTecniciDiRelease(codiceRelease);
    }
    public void eliminaTecnico(String idDipendente) throws DatabaseException {
        tecnicoDAO.eliminaTecnico(idDipendente);
        System.out.println("Tecnico eliminato dal DB: " + idDipendente);
    }
    public void assegnaReleaseATecnico(String idTecnico, String codiceRelease) throws DatabaseException {
        tecnicoDAO.assegnaRelease(idTecnico, codiceRelease);
        System.out.println("Release " + codiceRelease + " assegnata al tecnico " + idTecnico);
    }
    //METODI PER RELEASE
    public void registraNuovaRelease(String codice, String titolo, String tipoFormato, LocalDate dataPubblicazione, String stato, Artista artista) throws DatabaseException, DatiInvalidiException {
        if (codice == null || codice.trim().isEmpty() || artista == null || titolo == null || titolo.trim().isEmpty() || tipoFormato == null || tipoFormato.trim().isEmpty() || dataPubblicazione == null || stato == null || stato.trim().isEmpty()) {
            throw new DatiInvalidiException("Tutti i dettagli della release sono campi obbligatori.");
        }
        Release nuovaRelease = new Release(codice, titolo, tipoFormato, dataPubblicazione, stato, artista, new ArrayList<Tecnico>());
        releaseDAO.salvaRelease(nuovaRelease);

        System.out.println("Release registrata: " + titolo + " dell'artista " + artista.getNomeArte());
    }
    public List<Release> getTutteLeRelease() throws DatabaseException {

        return releaseDAO.getTutteLeRelease();
    }
    public List<Release> getReleaseDiArtista(String idArtista) throws DatabaseException {
        return releaseDAO.getReleaseDiArtista(idArtista);
    }
    public void eliminaRelease(String codiceRelease) throws DatabaseException {
        releaseDAO.eliminaRelease(codiceRelease);
        System.out.println("Release eliminata dal DB: " + codiceRelease);
    }
    //METODI PER ROYALTY REPORT
    public void registraRoyaltyReport(String idReport, String periodo, Double ricavi, Release release) throws DatabaseException, DatiInvalidiException {
        if (idReport == null || idReport.trim().isEmpty() ||  periodo == null || periodo.trim().isEmpty() || ricavi == null || release == null) {
            throw new DatiInvalidiException("Tutti i dati sono obbligatori.");
        }



        RoyaltyReport nuovoReport = new model.RoyaltyReport(idReport, periodo, ricavi, release);

        royaltyReportDAO.salvaRoyaltyReport(nuovoReport);
    }
    public List<RoyaltyReport> getRoyaltyReport() throws DatabaseException {

        return royaltyReportDAO.getRoyaltyReport();
    }
    public void eliminaRoyalty(String idReport) throws DatabaseException {
        royaltyReportDAO.eliminaRoyaltyReport(idReport);
        System.out.println("Royalty Report eliminato dal DB: " + idReport);
    }

    // METODI PER CAMPAGNE MARKETING
    public void registraCampagnaMarketing(String id, String piattaforma, Double costo, Dipartimento dipartimento, Release release) throws BudgetException, DatabaseException, DatiInvalidiException{
        if (id == null || id.trim().isEmpty() || piattaforma == null || piattaforma.trim().isEmpty() || costo == null || dipartimento == null || release == null) {
            throw new DatiInvalidiException("Tutti i dati della campagna di marketing sono obbligatori!");
        }

        if (costo > dipartimento.getBudgetAnnuale()) {
            throw new BudgetException("Il costo della campagna (" + costo + "€) supera il budget disponibile del dipartimento (" + dipartimento.getBudgetAnnuale() + "€).");
        }

        CampagnaMarketing nuovaCampagna = new CampagnaMarketing(id, piattaforma, costo, release, dipartimento);

        campagnaMarketingDAO.salvaCampagnaMarketing(nuovaCampagna);

        Double nuovoBudget = dipartimento.getBudgetAnnuale() - costo;
        dipartimento.setBudgetAnnuale(nuovoBudget);

        System.out.println("Campagna Registrata! Nuovo budget dipartimento: " + dipartimento.getBudgetAnnuale());
    }
    public List<CampagnaMarketing> getCampagneMarketing() throws DatabaseException {
        return campagnaMarketingDAO.getCampagneMarketing();
    }
    public void eliminaCampagna(String idCampagna) throws DatabaseException {
        campagnaMarketingDAO.eliminaCampagna(idCampagna);
        System.out.println("Campagna Marketing eliminata dal DB: " + idCampagna);
    }

    public void eliminaDipartimento(String idDipartimento) throws DatabaseException{
        dipartimentoDAO.eliminaDipartimento(idDipartimento);
        System.out.println("Dipartimento eliminato con successo");
    }

    public List<Dipartimento> getTuttiIDipartimenti() throws DatabaseException{

        return dipartimentoDAO.getTuttiIDipartimenti();
    }
//Metodi per utente
    public boolean effettuaLogin(String username, String password) throws DatabaseException{
        return utenteDAO.verificaLogin(username, password);
    }
    public void registraUtente(String username, String password) throws DatabaseException, DatiInvalidiException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new DatiInvalidiException("Username e Password non possono essere vuoti.");
        }
        utenteDAO.registraNuovoUtente(username, password);
    }
}