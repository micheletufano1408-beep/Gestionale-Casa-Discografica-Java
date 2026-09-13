package gui;

import controller.Controller;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Home {
    private JPanel mainPanel;

    private JButton aggiungiArtistaButton;
    private JButton apriAggiungiManagerButton;
    private JButton assumiNuovoTecnicoButton;
    private JButton aggiungiReleaseButton;
    private JButton creaUnaCampagnaDiButton;
    private JButton aggiungiUnaRoyaltyReportButton;

    private JButton btnVistaArtisti;
    private JButton btnVistaPersonale;
    private JButton btnVistaRelease;
    private JButton btnVistaRoyalty;
    private JButton btnVistaCampagne;
    private JButton btnVistaDipartimenti;
    private JTable tabellaDati;


    private static JFrame frameHome;
    private Controller controller;
    private String vistaAttuale = "Artisti";

    public Home(Controller controller) {
        this.controller = controller;

        // Listener per i bottoni di aggiunta dati
        aggiungiArtistaButton.addActionListener(e -> {
            new aggiungiArtista(Home.this.controller, frameHome);
            frameHome.setVisible(false);
        });
        apriAggiungiManagerButton.addActionListener(e -> {
            new aggiungiManager(Home.this.controller, frameHome);
            frameHome.setVisible(false);
        });
        assumiNuovoTecnicoButton.addActionListener(e -> {
            new aggiungiTecnico(Home.this.controller, frameHome);
            frameHome.setVisible(false);
        });
        aggiungiReleaseButton.addActionListener(e -> {
            new aggiungiRelease(Home.this.controller, frameHome);
            frameHome.setVisible(false);
        });
        creaUnaCampagnaDiButton.addActionListener(e -> {
            new aggiungiCampagnaMarketing(Home.this.controller, frameHome);
            frameHome.setVisible(false);
        });
        aggiungiUnaRoyaltyReportButton.addActionListener(e -> {
            new aggiungiRoyaltyReport(Home.this.controller, frameHome);
            frameHome.setVisible(false);
        });

        //Listener per i bottoni di vista
        btnVistaArtisti.addActionListener(e -> caricaTabellaArtisti());
        btnVistaPersonale.addActionListener(e -> caricaTabellaPersonale());
        btnVistaRelease.addActionListener(e -> caricaTabellaRelease());
        btnVistaRoyalty.addActionListener(e -> caricaTabellaRoyalty());
        btnVistaCampagne.addActionListener(e -> caricaTabellaCampagne());
        btnVistaDipartimenti.addActionListener(e -> caricaTabellaDipartimenti());

        //Doppio click per eliminare
        tabellaDati.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int rigaSelezionata = tabellaDati.getSelectedRow();
                    if (rigaSelezionata != -1) {
                        String idRecord = tabellaDati.getValueAt(rigaSelezionata, 0).toString();
                        String infoRecord = tabellaDati.getValueAt(rigaSelezionata, 1).toString();

                        int scelta = JOptionPane.showConfirmDialog(frameHome, "Sei sicuro di voler eliminare il record selezionato dal database?\nQuesta operazione è irreversibile.",
                                "Conferma Eliminazione", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (scelta == JOptionPane.YES_OPTION) {
                            try {
                                switch (vistaAttuale) {
                                    case "Artisti":
                                        Home.this.controller.eliminaArtista(idRecord);
                                        caricaTabellaArtisti();
                                        break;
                                    case "Personale":
                                        String ruolo = tabellaDati.getValueAt(rigaSelezionata, 1).toString();
                                        if (ruolo.equals("Manager")) {
                                            Home.this.controller.eliminaManager(idRecord);
                                        } else if (ruolo.equals("Tecnico")) {
                                            Home.this.controller.eliminaTecnico(idRecord);
                                        }
                                        caricaTabellaPersonale();
                                        break;
                                    case "Release":
                                        Home.this.controller.eliminaRelease(idRecord);
                                        caricaTabellaRelease();
                                        break;
                                    case "Campagne Marketing":
                                        Home.this.controller.eliminaCampagna(idRecord);
                                        caricaTabellaCampagne();
                                        break;
                                    case "Royalty Report":
                                        Home.this.controller.eliminaRoyalty(idRecord);
                                        caricaTabellaRoyalty();
                                        break;
                                    case "Dipartimenti":
                                        Home.this.controller.eliminaDipartimento(idRecord);
                                        caricaTabellaDipartimenti();
                                        break;
                                }
                                JOptionPane.showMessageDialog(frameHome, "Eliminazione avvenuta con successo. ", "Successo",  JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frameHome,
                                        "Impossibile eliminare il record.\nVerifica che non sia collegato ad altri dati (Vincolo di Chiave Esterna).\n" + ex.getMessage(),
                                        "Errore DB",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        frameHome = new JFrame("Home Page Discografica");
        frameHome.setContentPane(this.mainPanel);
        frameHome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameHome.pack();
        frameHome.setLocationRelativeTo(null);
        frameHome.setVisible(true);

        caricaTabellaArtisti();
    }

    //Metodi per popolare le tabelle

    private void caricaTabellaArtisti() {
        vistaAttuale = "Artisti";
        try {
            List<Artista> lista = this.controller.getTuttiGliArtisti();
            String[] colonne = {"ID", "Nome d'Arte", "Genere", "Data inizio", "Data fine", "Manager"};
            DefaultTableModel tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Artista a : lista) {
                String nomeManager = (a.getManager() != null) ? a.getManager().getNome() + " " + a.getManager().getCognome() : "Nessuno";
                tableModel.addRow(new Object[]{a.getIdArtista(), a.getNomeArte(), a.getGenereMusicale(), a.getDataInizioContratto(), a.getDataFineContratto(), nomeManager});
            }
            tabellaDati.setModel(tableModel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameHome, "Errore DB Artisti:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaTabellaPersonale() {
        vistaAttuale = "Personale";
        try {
            List<Manager> listaManager = this.controller.getTuttiIManager();
            List<model.Tecnico> listaTecnici = this.controller.getTuttiITecnici();

            String[] colonne = {"ID Dipendente", "Ruolo", "Nome", "Cognome", "Data Assunzione", "Dettagli Aggiuntivi"};
            DefaultTableModel tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Manager m : listaManager) {
                String dettagli = "Bonus: " + m.getBonusPercentuale() + "%";
                tableModel.addRow(new Object[]{
                        m.getIdDipendente(), "Manager", m.getNome(), m.getCognome(), m.getDataAssunzione(), dettagli
                });
            }

            for (model.Tecnico t : listaTecnici) {
                String dettagli = "Spec: " + t.getRuoloSpecializzato();
                tableModel.addRow(new Object[]{
                        t.getIdDipendente(), "Tecnico", t.getNome(), t.getCognome(), t.getDataAssunzione(), dettagli
                });
            }

            tabellaDati.setModel(tableModel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameHome, "Errore DB Personale:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaTabellaRelease() {
        vistaAttuale = "Release";
        try {
            List<Release> lista = this.controller.getTutteLeRelease();
            String[] colonne = {"Codice", "Titolo", "Formato", "Data Pubblicazione", "Stato"};
            DefaultTableModel tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Release r : lista) {
                tableModel.addRow(new Object[]{r.getCodiceCatalogo(), r.getTitolo(), r.getTipoFormato(), r.getDataPubblicazione(), r.getStato()});
            }
            tabellaDati.setModel(tableModel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameHome, "Errore DB Release:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaTabellaRoyalty() {
        vistaAttuale = "Royalty Report";
        try {
            List<RoyaltyReport> lista = this.controller.getRoyaltyReport();
            String[] colonne = {"ID Report", "Periodo Riferimento", "Ricavi Totali", "Codice Release"};
            DefaultTableModel tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (RoyaltyReport r : lista) {
                tableModel.addRow(new Object[]{r.getIdReport(), r.getPeriodoRiferimento(), r.getRicaviTotali(), r.getReleaseRiferimento().getCodiceCatalogo()});
            }
            tabellaDati.setModel(tableModel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameHome, "Errore DB Release:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void caricaTabellaCampagne() {
        vistaAttuale = "Campagne Marketing";
        try {
            List<CampagnaMarketing> lista = this.controller.getCampagneMarketing();
            String[] colonne = {"ID Campagna", "Piattaforma", "Costo Stimato", "Release Promossa", "Dipartimento"};
            DefaultTableModel tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (CampagnaMarketing c : lista) {
                String infoRelease = (c.getReleasePromossa() != null) ? c.getReleasePromossa().getCodiceCatalogo() : "Nessuna";
                String infoDipartimento = (c.getDipartimentoFinanziatore() != null) ? c.getDipartimentoFinanziatore().getIdDipartimento() : "Nessuno";

                tableModel.addRow(new Object[]{
                        c.getIdCampagna(),
                        c.getPiattaforma(),
                        c.getCostoStimato(),
                        infoRelease,
                        infoDipartimento
                });
            }
            tabellaDati.setModel(tableModel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameHome, "Errore DB Release:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void caricaTabellaDipartimenti() {
        vistaAttuale = "Dipartimenti";
        try {
            List<Dipartimento> lista = this.controller.getTuttiIDipartimenti();

            String[] colonne = {"ID Dipartimento", "Nome Dipartimento", "Budget"};

            DefaultTableModel tableModel = new DefaultTableModel(colonne, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (Dipartimento d : lista) {
                tableModel.addRow(new Object[]{
                        d.getIdDipartimento(),
                        d.getNomeDipartimento(),
                        d.getBudgetAnnuale()
                });
            }
            tabellaDati.setModel(tableModel);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frameHome, "Errore DB Dipartimenti:\n" + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}
