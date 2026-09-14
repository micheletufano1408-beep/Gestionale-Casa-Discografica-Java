package dao;

import eccezioni.DatabaseException;
import model.Tecnico;
import java.util.List;

public interface TecnicoDAO {
    void salvaTecnico(Tecnico tecnico) throws DatabaseException;
    List<Tecnico>getTuttiITecnici() throws DatabaseException;
    void eliminaTecnico(String idTecnico) throws DatabaseException;
    List<Tecnico> getTecniciDiRelease(String codiceRelease) throws DatabaseException;
}