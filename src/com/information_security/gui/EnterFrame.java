package com.information_security.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.information_security.encrypters.RandomXorEncoder;

public class EnterFrame extends javax.swing.JFrame {

	public EnterFrame() {
		initComponents();
	}

	private void initComponents() {

		jButton1 = new javax.swing.JButton();
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jTextField1 = new javax.swing.JPasswordField();
		jTextField2 = new javax.swing.JPasswordField();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jButton1.setText("Ввод");

		jLabel1.setText("Придумайте пароль для входа в программу");

		jLabel2.setText("повторите пароль");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup().addGap(155, 155, 155)
												.addComponent(jButton1))
						.addGroup(layout.createSequentialGroup().addGap(94, 94, 94).addComponent(jLabel1))
						.addGroup(layout.createSequentialGroup().addGap(109, 109, 109).addGroup(layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(jTextField1).addComponent(jTextField2,
												javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)))))
				.addContainerGap(84, Short.MAX_VALUE)));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap(79, Short.MAX_VALUE).addComponent(jLabel1).addGap(18, 18, 18)
								.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(1, 1, 1).addComponent(jLabel2).addGap(3, 3, 3)
						.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(27, 27, 27).addComponent(jButton1).addGap(81, 81, 81)));
		setView();
		pack();
	}// </editor-fold>

	private void setView() {
		getPasswordIfExists();
		if (isPasswordSet) {
			jTextField1.setVisible(false);
			jLabel1.setVisible(false);
			jLabel2.setText("Введите пароль");
		}

	}

	private void getPasswordIfExists() {
		try (BufferedReader in = new BufferedReader(new FileReader("password.iss"))) {
			password = in.readLine();
			isPasswordSet = password != null;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Файл с пароолем поврежден или не найден");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Файл с пароолем поврежден или не найден");
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {

		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(EnterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(EnterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(EnterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(EnterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new EnterFrame().setVisible(true);
			}
		});
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		RandomXorEncoder encoder = new RandomXorEncoder();
		if (isPasswordSet) {
			if(attemtsCount>2){
				try {
					JOptionPane.showMessageDialog(null, "Попытки ввода исчерпаны");
				attemtsCount=0;
					Thread.sleep(5000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			attemtsCount++;
			if (password.equals(encoder.decrypt(jTextField2.getText()))) {
				new MainFrame().open();
				this.hide();
			} else {
				JOptionPane.showMessageDialog(null,
						"Введен неверный пароль. Осталось" + (3 - attemtsCount) + " попыток");
			}
		} else {
			createPassword(jTextField2.getText());
		}
	}

	private void createPassword(String pass) {
		RandomXorEncoder encoder = new RandomXorEncoder();
		if (pass.equals(jTextField1.getText())) {
			try (BufferedWriter out = new BufferedWriter(new FileWriter("password.iss"))) {
				out.write(encoder.decrypt(pass));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Файл с пароолем поврежден или не найден");
				e.printStackTrace();
			}
			setView();
		} else {
			JOptionPane.showMessageDialog(null, "пароли не совпадают. Повторите ввод");
		}

	}

	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPasswordField jTextField1;
	private javax.swing.JPasswordField jTextField2;
	private int attemtsCount;
	private boolean isPasswordSet;
	private String password;
}
