package dao;

import eccezioni.DatabaseException;

public interface UtenteDAO {
    boolean verificaLogin(String username, String password) throws DatabaseException;
    void registraNuovoUtente(String username, String password) throws DatabaseException;
}
