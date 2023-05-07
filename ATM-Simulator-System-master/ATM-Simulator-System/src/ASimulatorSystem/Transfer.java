package ASimulatorSystem;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import java.util.Date;

public class Transfer extends JFrame implements ActionListener {

    JLabel l1, l2, l3, l4, l5;
    JTextField t1, t2, t3;
    JButton b1, b2, b3;
    String pin;

    Transfer(String pin) {
        this.pin = pin;
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("ASimulatorSystem/icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 1180, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l6 = new JLabel(i3);
        l6.setBounds(0, 0, 960, 1080);
        add(l6);

        l1 = new JLabel("TRANSFER MONEY");
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("System", Font.BOLD, 16));

        l2 = new JLabel("Enter recipient's account number:");
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("System", Font.BOLD, 16));

        l3 = new JLabel("Enter amount to transfer:");
        l3.setForeground(Color.WHITE);
        l3.setFont(new Font("System", Font.BOLD, 16));

        t1 = new JTextField();
        t2 = new JTextField();

        b1 = new JButton("Transfer");
        b2 = new JButton("Clear");
        b3 = new JButton("Cancel");

        setLayout(null);

        l1.setBounds(290, 350, 700, 35);
        l6.add(l1);

        l2.setBounds(150, 420, 700, 35);
        l6.add(l2);

        t1.setBounds(410, 425, 140, 25);
        l6.add(t1);

        l3.setBounds(150, 470, 700, 35);
        l6.add(l3);

        t2.setBounds(350, 475, 200, 25);
        l6.add(t2);

        b1.setBounds(200, 550, 100, 35);
        l6.add(b1);

        b2.setBounds(320, 550, 100, 35);
        l6.add(b2);

        b3.setBounds(440, 550, 100, 35);
        l6.add(b3);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);

        setSize(960, 1080);
        setLocation(500, 0);
        setUndecorated(true);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == b1) {
                String recipientAccNum = t1.getText();
                String amount2 = t2.getText();
                Conn c = new Conn();

                // Check if the recipient's account exists
                ResultSet rs = c.s.executeQuery("select * from signup3 where cardnumber = '"+recipientAccNum+"'");
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Recipient account does not exist" );
                    return;
                }

                String query = "select amount from bank where pin = '" + pin + "'";
                ResultSet rs1= c.s.executeQuery(query);
                rs1.next();
                double balance = rs1.getDouble("amount");

                // Check if there is enough balance to transfer
                if (Double.parseDouble(amount2) > balance) {
                    JOptionPane.showMessageDialog(null, "Insufficient balance");
                    return;
                }

                // Update the sender's balance
                double newBalance = balance - Double.parseDouble(amount2);
                String updateQuery1 = "update bank set amount = '" + newBalance + "' where pin = '" + pin + "'";
                c.s.executeUpdate(updateQuery1);

                // Update the recipient's balance
                ResultSet rs2 = c.s.executeQuery("select amount from bank where pin = '"+recipientAccNum+"'");
                rs2.next();
                double recipientBalance = rs2.getDouble("amount");
                double newRecipientBalance = recipientBalance + Double.parseDouble(amount2);
                String updateQuery2 = "update bank set amount = '"+newRecipientBalance+"' where pin = '"+recipientAccNum+"'";
                c.s.executeUpdate(updateQuery2);

                // Add transaction record to the sender's transaction table
                Date date = new Date();
                String transactionDate = date.toString();
                String transactionQuery = "insert into " + pin + " values ('" + transactionDate + "', 'Transfer', '" + amount2 + "', '0')";
                c.s.executeUpdate(transactionQuery);

                JOptionPane.showMessageDialog(null, "Money Transferred Successfully");
                setVisible(false);
                new Transactions(pin).setVisible(true);
            } else if (ae.getSource() == b2) {
                t1.setText("");
                t2.setText("");
            } else if (ae.getSource() == b3) {
                setVisible(false);
                new Transactions(pin).setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Transfer("").setVisible(true);
    }
}