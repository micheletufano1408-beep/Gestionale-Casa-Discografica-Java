package gui;

import controller.Controller;
import eccezioni.DatabaseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JPanel mainPanel;
    private JTextField inserisciUsername;
    private JPasswordField inserisciPassword;
    private JButton accediButton;
    private JButton btnRegistrati;

    private JFrame frame;
    private Controller controller;

    public Login(Controller controller) {
        this.controller = controller;
        inizializzaGUI();
    }

    private void inizializzaGUI() {
        frame = new JFrame("Login di Sistema");
        frame.setContentPane(mainPanel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.getRootPane().setDefaultButton(accediButton);

        accediButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = inserisciUsername.getText();
                String pass = new String(inserisciPassword.getPassword());

                try {
                    if (controller.effettuaLogin(user, pass)) {
                        frame.dispose();
                        new Home(controller);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Username o password errati!", "Credenziali errate", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (DatabaseException ex) {
                    JOptionPane.showMessageDialog(frame, "Impossibile collegarsi al server.\nDettaglio: " + ex.getMessage(), "Errore di Connessione", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnRegistrati.addActionListener(e -> {
            JTextField usernameField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            Object[] message = {
                    "Scegli un Username:", usernameField,
                    "Scegli una Password:", passwordField
            };

            int opzione = JOptionPane.showConfirmDialog(frame, message, "Registrazione Nuovo Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (opzione == JOptionPane.OK_OPTION) {
                String nuovoUser = usernameField.getText();
                String nuovaPass = new String(passwordField.getPassword());

                try {
                    controller.registraUtente(nuovoUser, nuovaPass);
                    JOptionPane.showMessageDialog(frame, "Account creato con successo!\nOra puoi effettuare l'accesso.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore Registrazione", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }
}