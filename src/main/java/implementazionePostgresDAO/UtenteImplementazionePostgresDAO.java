package implementazionePostgresDAO;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import eccezioni.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteImplementazionePostgresDAO implements UtenteDAO {

    @Override
    public boolean verificaLogin(String username, String password) throws DatabaseException {

        String query = "SELECT * FROM utente WHERE username = ? AND password = ?";

//blocco try per "provare" la connessione al database
        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la verifica delle credenziali nel database:\n" + e.getMessage());
        }
    }
    @Override
    public void registraNuovoUtente(String username, String password) throws DatabaseException {
        String sql = "INSERT INTO utente (username, password) VALUES (?, ?)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new DatabaseException("Questo Username è già in uso. Scegline un altro.");
            }
            throw new DatabaseException("Errore durante la registrazione: " + e.getMessage());
        }
    }
}