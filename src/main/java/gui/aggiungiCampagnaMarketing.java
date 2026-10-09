package gui;

import controller.Controller;
import eccezioni.BudgetException;
import eccezioni.DatabaseException;
import eccezioni.DatiInvalidiException;
import model.Dipartimento;
import model.Release;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class aggiungiCampagnaMarketing {
    private JPanel mainPanel;
    private JTextField inserisciId;
    private JTextField inserisciPiattaforma;
    private JTextField inserisciCosto;
    private JComboBox<Dipartimento> selezionaDipartimento;
    private JComboBox<Release> selezionaRelease;
    private JButton confermaButton;

    private Controller controller;
    private JFrame frame;

    public aggiungiCampagnaMarketing(Controller controller, JFrame frameHome) {
        this.controller = controller;

        frame = new JFrame("Nuova Campagna Marketing");
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

        try {
            for (Dipartimento d : controller.getTuttiIDipartimenti()) {
                selezionaDipartimento.addItem(d);
            }

            for (Release r : controller.getTutteLeRelease()) {
                selezionaRelease.addItem(r);
            }
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(frame, "Impossibile caricare i dati dal database per i menu a tendina.", "Errore DB", JOptionPane.ERROR_MESSAGE);
        }

        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaCampagna(frameHome);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void salvaCampagna(JFrame frameHome) {
        try {
            String id = inserisciId.getText();
            String piattaforma = inserisciPiattaforma.getText();
            Double costo = Double.parseDouble(inserisciCosto.getText());

            Dipartimento dipScelto = (Dipartimento) selezionaDipartimento.getSelectedItem();
            Release releaseScelta = (Release) selezionaRelease.getSelectedItem();

            if (dipScelto == null || releaseScelta == null) {
                throw new DatiInvalidiException("Devi selezionare sia un Dipartimento che una Release!");
            }

            controller.registraCampagnaMarketing(id, piattaforma, costo, dipScelto, releaseScelta);

            JOptionPane.showMessageDialog(mainPanel, "Campagna registrata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);

            frameHome.setVisible(true);

            frame.dispose();

        }
        catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(mainPanel, "Errore: Il costo deve essere un numero valido.", "Errore Input", JOptionPane.WARNING_MESSAGE);
        }
        catch (DatiInvalidiException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Dati Mancanti", JOptionPane.WARNING_MESSAGE);
        }
        catch (BudgetException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Budget Superato", JOptionPane.WARNING_MESSAGE);
        }
        catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Errore di connessione al DB:\n" + ex.getMessage(), "Errore Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}