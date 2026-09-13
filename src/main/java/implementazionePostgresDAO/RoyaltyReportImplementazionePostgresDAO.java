package implementazionePostgresDAO;

import dao.RoyaltyReportDAO;
import database.ConnessioneDatabase;
import eccezioni.DatabaseException;
import model.Release;
import model.RoyaltyReport;
import model.Tecnico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoyaltyReportImplementazionePostgresDAO implements RoyaltyReportDAO {

    @Override
    public void salvaRoyaltyReport(RoyaltyReport report) throws DatabaseException{
        String sql = "INSERT INTO royalty_report (id_report, periodo_riferimento, ricavi_totali, codice_release) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, report.getIdReport());
            pstmt.setString(2, report.getPeriodoRiferimento());
            pstmt.setDouble(3, report.getRicaviTotali());

            pstmt.setString(4, report.getReleaseRiferimento().getCodiceCatalogo());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new DatabaseException("Errore durante il salvataggio della Royalty Report: " + e.getMessage());
        }
    }
    @Override
    public List<RoyaltyReport> getRoyaltyReport() throws DatabaseException {
        List<RoyaltyReport> listaRoyaltyReport = new ArrayList<>();
        String sql = "SELECT * FROM royalty_report";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String idReport = rs.getString("id_report");
                String periodoRiferimento = rs.getString("periodo_riferimento");
                Double ricaviTotali = rs.getDouble("ricavi_totali");
                String releaseRiferimento = rs.getString("codice_release");


                Release releaseAssociata = new Release(releaseRiferimento, "Titolo Sconosciuto", "", null, "", null, new ArrayList<Tecnico>());

                RoyaltyReport royaltyReport = new RoyaltyReport(idReport, periodoRiferimento, ricaviTotali, releaseAssociata);

                listaRoyaltyReport.add(royaltyReport);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Errore durante la ricerca della royalty report: " + e.getMessage());
        }

        return listaRoyaltyReport;
    }
    @Override
    public void eliminaRoyaltyReport(String id) throws DatabaseException {
        String sql = "DELETE FROM royalty_report WHERE id_report = ?";

        try (Connection conn = ConnessioneDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int righeEliminate = pstmt.executeUpdate();
            if (righeEliminate == 1) {
                throw new DatabaseException("Nessun royalty report associato a questo id");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Impossibile eliminare il Royalty Report.\n" + e.getMessage());
        }
    }
}