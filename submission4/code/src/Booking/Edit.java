/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Booking;
import Database.*;
import Login.User;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author kashyap
 */
public class Edit extends javax.swing.JFrame {
   private static String a=null;
    Cab_Details ca;
    Classroom_booking cl;
    /**
     * Creates new form Edit
     * @param cl
     */
    public Edit(Classroom_booking cl){
        a="class_room";
        initComponents();
        this.cl=cl;
    }
    
     public Edit(Cab_Details ca) {
        a="cab";
         initComponents();
        this.ca=ca;
    }
      public void goToThanks(){
          Thanks t=new Thanks();
          t.labelclass(cl.getR_no());
          t.setVisible(true);
        this.setVisible(false);
      }
     public void goToThanks(Object [][] entries){
        Thanks t=new Thanks();
        
            t.labelcab((entries[0][0]).toString(),(entries[0][2]).toString(),(entries[0][1]).toString());
        
       
            
        
        t.setVisible(true);
        this.setVisible(false);
     }
     public void goToCab(){
         ca.setVisible(true);
         
     }
     public void goToClassroom(){
         cl.setVisible(true);
     }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Edit Details");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("To edit the details please click Edit Details");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("To submit click Submit");

        jButton1.setText("Edit Details");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      // Cab_Details ca=new Cab_Details();
       if(a.equals("cab")){
      goToCab();
       }
       else{
      goToClassroom();
       }
       this.setVisible(false); 
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Object[][] entries=new Object[10][10];
        
       MySQLAccess m=new MySQLAccess();
       if(a.equals("cab")){
       try {
        
           entries=m.CabDbase.sendCabDetails(ca.date,ca.getS_time(),ca.getE_time());
           if(entries==null){
               JOptionPane.showMessageDialog(null, "Cab for the entered details is not avialable  ");
           }
           else{
               this.goToThanks(entries);
               Time t1 = new Time(11, 30, 0);
	       Time t2 = new Time(12, 0, 0);
               System.out.println(ca.getS_time());
              m.CabDbase.updateDatabase(User.NAME,entries[0][0].toString(),entries[0][1].toString(),entries[0][2].toString(), ca.date,ca.getS_time(),ca.getE_time(),ca.place);
//              m.CabDbase.updateDatabase("Ram", "RajuRam", "GA5763", "24-11-2014", t1, t2, "Airport");
           }
       } catch (Exception ex) {
           Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
       }
       try {
         
       } catch (Exception ex) {
           Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
       }
       }
       else{
           try {
        
           boolean a = m.ClassDbase.sendClassRoomDetails(cl.date,cl.getS_time(),cl.getE_time(),cl.getR_no());
           if(!a){
               JOptionPane.showMessageDialog(null, "Classroom for the entered details is not avialable ."
                       + "please select another classroom ");
           }
           else{
               this.goToThanks();
               m.ClassDbase.updateAdminDatabase(User.NAME,cl.getR_no(),cl.date,cl.getS_time(),cl.getE_time(),cl.getPurpose(),cl.requirements);
           }
       } catch (Exception ex) {
           Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
       }
       try {
         
       } catch (Exception ex) {
           Logger.getLogger(Edit.class.getName()).log(Level.SEVERE, null, ex);
       }
       }
           
       
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Edit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
