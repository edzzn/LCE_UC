public class Mensaje extends javax.swing.JDialog {
private javax.swing.JPanel contentPane;
private javax.swing.JButton buttonOK;
private javax.swing.JButton buttonCancel;

public Mensaje(){
setContentPane(contentPane);
setModal(true);
getRootPane().setDefaultButton(buttonOK);

buttonOK.addActionListener(new java.awt.event.ActionListener(){public void actionPerformed(java.awt.event.ActionEvent e){onOK();}});
}

private void onOK(){
 // add your code here
dispose();
}

public static void main(String[] args){
Mensaje dialog = new Mensaje();
dialog.pack();
dialog.setVisible(true);
System.exit(0);
}
}
