package dao;

import model.Artista;
import eccezioni.DatabaseException;
import java.util.List;


public interface ArtistaDAO {
    void salvaArtista(Artista artista) throws DatabaseException;
    List<Artista> getTuttiGliArtisti() throws DatabaseException;
    void eliminaArtista(String idArtista) throws DatabaseException;
    void assegnaManager(String idArtista, String idManager) throws DatabaseException;

}
