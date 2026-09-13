package implementazionePostgresDAO;

import dao.CampagnaMarketingDAO;
import database.ConnessioneDatabase;
import eccezioni.DatabaseException;
import model.CampagnaMarketing;

import model.Dipartimento;
import model.Release;
import org.postgresql.util.PSQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampagnaMarketingImplementazionePostgresDAO implements CampagnaMarketingDAO {

    @Override
    public void salvaCampagnaMarketing(CampagnaMarketing campagna) throws DatabaseException {
        String sql = "INSERT INTO campagna_marketing (id_campagna, piattaforma, costo_stimato, id_dipartimento, codice_release) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, campagna.getIdCampagna());
            pstmt.setString(2, campagna.getPiattaforma());
            pstmt.setDouble(3, campagna.getCostoStimato());
            pstmt.setString(4, campagna.getDipartimentoFinanziatore().getIdDipartimento());
            pstmt.setString(5, campagna.getReleasePromossa().getCodiceCatalogo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            String messaggioPulito = e.getMessage();

            if (e instanceof PSQLException) {
                PSQLException pgEx = (PSQLException) e;
                if (pgEx.getServerErrorMessage() != null) {
                    messaggioPulito = pgEx.getServerErrorMessage().getMessage();
                }
            }

            throw new DatabaseException("Impossibile salvare la campagna di marketing.\n" + messaggioPulito);
        }
    }
    @Override
    public List<CampagnaMarketing> getCampagneMarketing() throws DatabaseException{
        List<CampagnaMarketing> lista = new ArrayList<>();
        String sql = "SELECT c.*, d.nome_dipartimento FROM campagna_marketing c JOIN dipartimento d ON c.id_dipartimento = d.id_dipartimento";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String idCampagna = rs.getString("id_campagna");
                String piattaforma = rs.getString("piattaforma");
                Double costoStimato = rs.getDouble("costo_stimato");

                String codiceRelease = rs.getString("codice_release");
                String idDipartimento = rs.getString("id_dipartimento");

                Release releaseAssociata = new Release(codiceRelease, "Titolo Sconosciuto", "", null, "", null);
                String nomeDipartimento = rs.getString("nome_dipartimento");
                Dipartimento dipartimentoAssociato = new Dipartimento(idDipartimento, nomeDipartimento,0.0);

                CampagnaMarketing campagnaMarketing = new CampagnaMarketing(idCampagna, piattaforma, costoStimato, releaseAssociata, dipartimentoAssociato);

                lista.add(campagnaMarketing);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il salvataggio del dipartimento: " + e.getMessage());
        }

        return lista;
    }
    @Override
    public void eliminaCampagna(String id) throws DatabaseException {
        String sql = "DELETE FROM campagna_marketing WHERE id_campagna = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int righeEliminate = pstmt.executeUpdate();
            if (righeEliminate == 0) {
                throw  new DatabaseException("Nessuna campagna associata a questo id.");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Impossibile eliminare la campagna di marketing.\n" + e.getMessage());
        }
    }
}