package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import eccezioni.DatiInvalidiException;
import eccezioni.DatabaseException;
import model.Release;
import java.util.List;

public class aggiungiTecnico {
    private JPanel mainPanel;
    private JButton aggiungiTecnicoButton;
    private JTextField inserisciId;
    private JTextField inserisciNome;
    private JTextField inserisciCognome;
    private JTextField inserisciDataAssunzione;
    private JTextField inserisciRuolo;
    private JComboBox<String> inserisciRelease;

    private Controller controller;
    private JFrame frame;

    public aggiungiTecnico(Controller controller, JFrame frameHome) {
        this.controller = controller;

        frame = new JFrame("Assunzione nuovo Tecnico");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                frameHome.setVisible(true);
            }
        });
        inserisciRelease.addItem("Nessuna");
        try {
            List<Release> tutteLeRelease = controller.getTutteLeRelease();
            for (Release r : tutteLeRelease) {
                inserisciRelease.addItem(r.getCodiceCatalogo() + " - " + r.getTitolo());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Errore caricamento Release: " + ex.getMessage());
        }
        aggiungiTecnicoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaTecnico(frameHome);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void salvaTecnico(JFrame frameHome) {
        try {
            String id = inserisciId.getText();
            String nome = inserisciNome.getText();
            String cognome = inserisciCognome.getText();
            LocalDate data = LocalDate.parse(inserisciDataAssunzione.getText());
            String ruolo = inserisciRuolo.getText();

            String releaseSelezionata = (String) inserisciRelease.getSelectedItem();
            String codiceRelease = null;

            if (releaseSelezionata != null && !releaseSelezionata.equals("Nessuna")) {
                codiceRelease = releaseSelezionata.split(" - ")[0];
            }

            controller.registraNuovoTecnico(id, nome, cognome, data, ruolo, codiceRelease);

            JOptionPane.showMessageDialog(mainPanel, "Tecnico " + nome + " " + cognome + " assunto con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

            frameHome.setVisible(true);
            frame.dispose();
        } catch (java.time.format.DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(mainPanel, "Formato data errato!\nAssicurati di usare il formato YYYY-MM-DD.", "Errore Data", JOptionPane.WARNING_MESSAGE);
        } catch (DatiInvalidiException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Dati Mancanti", JOptionPane.WARNING_MESSAGE);
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Errore di connessione al DB:\n" + ex.getMessage(), "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}
