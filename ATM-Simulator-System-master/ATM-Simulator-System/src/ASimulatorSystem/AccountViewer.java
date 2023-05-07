
package ASimulatorSystem;
import java.awt.*;
import javax.swing.*;

public class AccountViewer {
   private JFrame mainFrame;
   private JLabel headerLabel;
   private JLabel balanceLabel;

   public AccountViewer() {
      prepareGUI();
   }

   public static void main(String[] args){
      AccountViewer accountViewer = new AccountViewer();  
      accountViewer.showBalance(5000.00);      
   }

   private void prepareGUI(){
      mainFrame = new JFrame("ATM Account Viewer");
      mainFrame.setSize(400,400);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      headerLabel = new JLabel("",JLabel.CENTER );
      balanceLabel = new JLabel("",JLabel.CENTER );        
      mainFrame.add(headerLabel);
      mainFrame.add(balanceLabel);
      mainFrame.setVisible(true);  
   }

   private void showBalance(Double balance){       
      headerLabel.setText("Your Account Balance: ");
      balanceLabel.setText("$" + balance);    
   }
}

