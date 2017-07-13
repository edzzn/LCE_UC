package lib;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mensaje extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel txtMensaje;

    public Mensaje() {
        setContentPane(contentPane);
        setModal(true);
        this.setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public void mostrarMensaje(String mensaje){
        txtMensaje.setText(mensaje);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Mensaje dialog = new Mensaje();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
