/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.limod.gui;

import de.limod.portals.AutoScout;
import de.limod.portals.Car;
import de.limod.portals.EbayKleinanzeigen;
import de.limod.portals.Mobile;
import de.limod.portals.Portal;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;

/**
 *
 * @author dominic
 */
public class AutoradarUI extends javax.swing.JFrame {

    private final static Logger LOGGER = Logger.getLogger(AutoradarUI.class.getName());

    // vw polo, radius 200km 84389, ab 2003, bis 5000 euro, max. 80.000km, neuste Angebote zuerst
    static String mobileVwPolo = "?zipcodeRadius=200&grossPrice=true&damageUnrepaired=NO_DAMAGE_UNREPAIRED&scopeId=C&ambitAddress=DE%2C+84389&userPosition=48.41961%2C12.90542&minFirstRegistrationDate=2003-01-01&maxPrice=5000&maxMileage=80000&makeModelVariant1.makeId=25200&makeModelVariant1.modelId=27&isSearchRequest=true&sortOption.sortBy=creationTime&sortOption.sortOrder=DESCENDING";
    static String autoscoutVwPolo = "?atype=C&make=74&model=-99&mmvmk0=74&mmvmd0=-99&mmvco=1&fregfrom=2003&priceto=5000&kmto=80000&cy=D&zip=84389&zipc=D&zipr=200&ustate=N,U&sort=age&desc=1&results=80&page=1&event=pag&dtr";
    static String ebayVwPolo = "s-autos/84389/preis::5000/vw-polo/k0c216l5921r200+autos.ez_i:2003,+autos.km_i:,80000";

    SwingWorker worker = new SwingWorker<Object, Void>() {
        @Override
        public Object doInBackground() {
            while (!this.isCancelled()) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AutoradarUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                AutoradarUI.LOGGER.info("Background task");

                Date d = new Date();
                lblDate.setText(d.toString());

                List<Car> newCars = AutoradarUI.getCars();
                CarModel oldModel = (CarModel) tblCars.getModel();
                List<Car> oldCars = oldModel.getCars();

                for (Car newCar : newCars) {
                    Boolean isNew = true;
                    Boolean isInList = false;
                    for (Car oldCar : oldCars) {
                        if (newCar.equals(oldCar)) {
                            isInList = true;
                            if (!oldCar.getIsNew()) {
                                isNew = false;
                            }
                        }
                    }
                    if (!isInList) {
                        AutoradarUI.LOGGER.info("Car is new: " + newCar.getId());
                        newCar.setIsNew(true);
                        oldModel.addCar(0, newCar);
                        oldModel.fireTableDataChanged();
                        AutoradarUI.playSound("/horn.wav");
                    }
                }

//                CarModel newModel = new CarModel(newCars);
//                tblCars.setModel(newModel);
//                tblCars.setAutoCreateRowSorter(true);
            }
            return new Date();
        }

        @Override
        public void done() {
            //Remove the "Loading images" label.

        }
    };

    /**
     * Creates new form AutoradarUI
     */
    public AutoradarUI() {

        initComponents();
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

        List<Car> newCars = AutoradarUI.getCars();
        CarModel newModel = new CarModel(newCars);
        tblCars.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCars.setRowSelectionAllowed(true);
        tblCars.setModel(newModel);
        tblCars.setDefaultRenderer(Object.class, new CarCellRenderer());
        tblCars.getColumnModel().getColumn(1).setPreferredWidth(300);
        tblCars.getColumnModel().getColumn(2).setPreferredWidth(30);
        worker.execute();
        
        tblCars.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AutoradarUI.LOGGER.info("Table clicked");
                int row = tblCars.rowAtPoint(evt.getPoint());
                int col = tblCars.columnAtPoint(evt.getPoint());
                if (row >= 0 && col >= 0) {
                    CarModel cm = (CarModel) tblCars.getModel();
                    Car c = cm.getRow(row);
                    c.setIsNew(false);

                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            URL url = new URL(c.getUrl());

                            URI uri = url.toURI();
                            desktop.browse(uri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private static List<Car> getCars() {
        List<Portal> polos = new ArrayList<Portal>();

        polos.add(new Mobile(mobileVwPolo));
        polos.add(new AutoScout(autoscoutVwPolo));
        polos.add(new EbayKleinanzeigen(ebayVwPolo));

        List<Car> cars = new ArrayList<>();

        for (Portal portal : polos) {
            cars.addAll(portal.getCars());
        }

        return cars;
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
  // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            AutoradarUI.class.getResourceAsStream(url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println("playsound: " +e.getMessage());
                }
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblCars = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Autoradar 1.0");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tblCars.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblCars);

        jLabel1.setText("Letztes Update:");

        lblDate.setText("jLabel2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:

//        this.setIconImage(img.getImage());
//        this.setIconImage(img.getImage());
        this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(AutoradarUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AutoradarUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AutoradarUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AutoradarUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AutoradarUI autoradarUI = new AutoradarUI();
//                URL iconURL = getClass().getResource("car-icon.png");

                ImageIcon img = new ImageIcon(loadImageIcon("/car-icon.png").getImage()); //new ImageIcon("car-4-multi-size.ico");
                autoradarUI.setIconImage(img.getImage());
                autoradarUI.setVisible(true);
            }
        });

    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    private static ImageIcon loadImageIcon(String path) {
        URL imgURL = AutoradarUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDate;
    private javax.swing.JTable tblCars;
    // End of variables declaration//GEN-END:variables
}
