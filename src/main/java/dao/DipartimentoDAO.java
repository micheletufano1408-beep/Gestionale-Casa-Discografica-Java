package dao;

import eccezioni.DatabaseException;
import model.Dipartimento;
import java.util.List;

public interface DipartimentoDAO {
    List<Dipartimento> getTuttiIDipartimenti() throws DatabaseException;
    void eliminaDipartimento(String idDipartimento) throws DatabaseException;
    void aggiornaBudgetDipartimento(String idDipartimento, Double nuovoBudget) throws DatabaseException;
}