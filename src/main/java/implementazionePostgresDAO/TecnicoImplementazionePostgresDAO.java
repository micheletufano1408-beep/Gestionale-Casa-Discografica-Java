package implementazionePostgresDAO;

import dao.TecnicoDAO;
import database.ConnessioneDatabase;
import eccezioni.DatabaseException;
import model.Manager;
import model.Tecnico;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TecnicoImplementazionePostgresDAO implements TecnicoDAO {

    @Override
    public void salvaTecnico(Tecnico tecnico) throws DatabaseException {
        String sql = "INSERT INTO tecnico (id_dipendente, nome, cognome, data_assunzione, ruolo_specializzato, codice_release) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tecnico.getIdDipendente());
            pstmt.setString(2, tecnico.getNome());
            pstmt.setString(3, tecnico.getCognome());
            pstmt.setDate(4, Date.valueOf(tecnico.getDataAssunzione()));
            pstmt.setString(5, tecnico.getRuoloSpecializzato());

            if (tecnico.getReleaseAssegnata() != null && tecnico.getReleaseAssegnata().getCodiceCatalogo() != null) {
                pstmt.setString(6, tecnico.getReleaseAssegnata().getCodiceCatalogo());
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }

            pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il salvataggio del tecnico: " + e.getMessage());
        }
    }
    @Override
    public List<Tecnico> getTuttiITecnici() throws DatabaseException {
        List<Tecnico> listaTecnici = new ArrayList<>();
        String sql = "SELECT * FROM tecnico";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id_dipendente");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                LocalDate dataAssunzione = rs.getDate("data_assunzione").toLocalDate();
                String ruoloSpecializzato = rs.getString("ruolo_specializzato");

                Tecnico tecnico = new Tecnico(id, nome, cognome, dataAssunzione, ruoloSpecializzato, null);
                listaTecnici.add(tecnico);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la ricerca del tecnico: " + e.getMessage());
        }

        return listaTecnici;
    }
    @Override
    public void eliminaTecnico(String id) throws DatabaseException {
        String sql = "DELETE FROM tecnico WHERE id_dipendente = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int righeEliminate = pstmt.executeUpdate();

            if (righeEliminate == 0) {
                throw new DatabaseException("Nessun tecnico trovato con questo ID.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante l'eliminazione del tecnico: " + e.getMessage());
        }
    }
    @Override
    public List<Tecnico> getTecniciDiRelease(String codiceRelease) throws DatabaseException {
        List<Tecnico> lista = new ArrayList<>();
        String sql = "SELECT * FROM tecnico WHERE codice_release = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codiceRelease);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id_dipendente");
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    LocalDate data = rs.getDate("data_assunzione").toLocalDate();
                    String ruolo = rs.getString("ruolo_specializzato");

                    lista.add(new Tecnico(id, nome, cognome, data, ruolo, null));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il recupero dei tecnici della release: " + e.getMessage());
        }
        return lista;
    }
}