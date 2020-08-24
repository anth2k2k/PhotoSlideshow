package source;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FrmMain extends javax.swing.JFrame {
    // =================== CONSTRUCTOR ===================
    // ===================================================

    private Connection con;
    private Statement stm;
    private ResultSet rs;
    private ArrayList<String> link = new ArrayList<>();
    private int index = 0;
    private int flag = -1;
    private Color moveIn = new Color(56, 56, 56);       // Change color when Cursor moves in
    private Color moveOut = new Color(34, 34, 34);      // Change color when Cursor moves out
    private Color clicked = new Color(78, 78, 78);      // Change color when Object is clicked

    public FrmMain() {
        initComponents();
        connect();
        getLink();
    }

    // ================== FUNCTION ==================
    // ==============================================
    
    /**
     * Get Connection from SQL Server
     */
    public void connect() {
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url = "jdbc:sqlserver://localhost:1433;databasename=ImageSlideshow";
        String username = "sa";
        String password = "123";
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("> CONNECT SUCESSFULLY...");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Get all images in Database
     */
    private void getLink() {
        try {
            stm = con.createStatement();
            rs = stm.executeQuery("SELECT * FROM tblImg ORDER BY NEWID()");
            link.clear();
            while (rs.next())
                link.add(rs.getString(1));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Resize Original-size image
     * @param i 
     */
    public void SetImage(int i) {
        ImageIcon icon = new ImageIcon(link.get(i));
        Image img = icon.getImage();
        int resizeWidth = resizeImageWidth(icon.getIconWidth(), icon.getIconHeight(), 574);
        Image newImg;
        if (resizeWidth > 999) {
            int resizeHeight = resizeImageHeight(icon.getIconWidth(), icon.getIconHeight(), 999);
            newImg = img.getScaledInstance(999, resizeHeight, Image.SCALE_SMOOTH);
        } else
            newImg = img.getScaledInstance(resizeWidth, 574, Image.SCALE_SMOOTH);
        ImageIcon newImc = new ImageIcon(newImg);
        lblImg.setIcon(newImc);
    }

    private int resizeImageWidth(int width, int height, int resize) {
        double i = (double) height / resize;
        double j = 1 / i;
        double k = width * j;
        return (int) k;
    }

    private int resizeImageHeight(int width, int height, int resize) {
        double i = (double) width / resize;
        double j = 1 / i;
        double k = height * j;
        return (int) k;
    }

    /**
     * Run Thread
     */
    private Thread slideShow = new Thread() {
        @Override
        public void run() {
            while (true)
                try {
                    SetImage(index);
                    index++;
                    if (index >= link.size())
                        index = 0;
                    Thread.sleep(1250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    };

    // ================ INIT COMPONENTS ================
    // =================================================
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblImg = new javax.swing.JLabel();
        btnShuffle = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Image Slideshow");
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(java.awt.Color.white);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(34, 34, 34));

        lblImg.setBackground(new java.awt.Color(255, 255, 255));
        lblImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnShuffle.setBackground(new java.awt.Color(34, 34, 34));
        btnShuffle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/shuffle.png"))); // NOI18N
        btnShuffle.setToolTipText("Shuffle");
        btnShuffle.setContentAreaFilled(false);
        btnShuffle.setFocusPainted(false);
        btnShuffle.setOpaque(true);
        btnShuffle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnShuffleMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnShuffleMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnShuffleMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnShuffleMouseReleased(evt);
            }
        });
        btnShuffle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShuffleActionPerformed(evt);
            }
        });

        btnPlay.setBackground(new java.awt.Color(34, 34, 34));
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/play.png"))); // NOI18N
        btnPlay.setToolTipText("Play");
        btnPlay.setContentAreaFilled(false);
        btnPlay.setFocusPainted(false);
        btnPlay.setOpaque(true);
        btnPlay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPlayMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPlayMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnPlayMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnPlayMouseReleased(evt);
            }
        });
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        btnPrevious.setBackground(new java.awt.Color(34, 34, 34));
        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/previous.png"))); // NOI18N
        btnPrevious.setToolTipText("Previous");
        btnPrevious.setContentAreaFilled(false);
        btnPrevious.setFocusPainted(false);
        btnPrevious.setOpaque(true);
        btnPrevious.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPreviousMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPreviousMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnPreviousMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnPreviousMouseReleased(evt);
            }
        });
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnFirst.setBackground(new java.awt.Color(34, 34, 34));
        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/first.png"))); // NOI18N
        btnFirst.setToolTipText("First");
        btnFirst.setContentAreaFilled(false);
        btnFirst.setFocusPainted(false);
        btnFirst.setOpaque(true);
        btnFirst.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFirstMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFirstMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnFirstMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnFirstMouseReleased(evt);
            }
        });
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnNext.setBackground(new java.awt.Color(34, 34, 34));
        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/next.png"))); // NOI18N
        btnNext.setToolTipText("Next");
        btnNext.setContentAreaFilled(false);
        btnNext.setFocusPainted(false);
        btnNext.setOpaque(true);
        btnNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNextMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNextMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnNextMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnNextMouseReleased(evt);
            }
        });
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setBackground(new java.awt.Color(34, 34, 34));
        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/last.png"))); // NOI18N
        btnLast.setToolTipText("Last");
        btnLast.setContentAreaFilled(false);
        btnLast.setFocusPainted(false);
        btnLast.setOpaque(true);
        btnLast.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLastMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLastMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnLastMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnLastMouseReleased(evt);
            }
        });
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnUpload.setBackground(new java.awt.Color(34, 34, 34));
        btnUpload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/upload.png"))); // NOI18N
        btnUpload.setToolTipText("Upload Image");
        btnUpload.setContentAreaFilled(false);
        btnUpload.setFocusPainted(false);
        btnUpload.setOpaque(true);
        btnUpload.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnUploadMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnUploadMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnUploadMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnUploadMouseReleased(evt);
            }
        });
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        btnPause.setBackground(new java.awt.Color(34, 34, 34));
        btnPause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/pause.png"))); // NOI18N
        btnPause.setToolTipText("Play");
        btnPause.setContentAreaFilled(false);
        btnPause.setEnabled(false);
        btnPause.setFocusPainted(false);
        btnPause.setOpaque(true);
        btnPause.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPauseMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPauseMouseExited(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnPauseMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnPauseMouseReleased(evt);
            }
        });
        btnPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btnShuffle, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(324, 324, 324)
                                                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 325, Short.MAX_VALUE)
                                                .addComponent(btnUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblImg, javax.swing.GroupLayout.PREFERRED_SIZE, 574, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnShuffle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnUpload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPrevious, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPlay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnFirst, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnLast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPause))
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    // ====================== ACTION EVENT ======================
    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (flag == 1)
            slideShow.resume();
        else {
            flag = 1;
            slideShow.start();
        }
        btnPlay.setEnabled(false);
        btnFirst.setEnabled(false);
        btnLast.setEnabled(false);
        btnNext.setEnabled(false);
        btnPrevious.setEnabled(false);
        btnShuffle.setEnabled(false);
        btnUpload.setEnabled(false);
        btnPause.setEnabled(true);
    }

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt)
    {
        slideShow.suspend();
        btnPause.setEnabled(false);
        btnPlay.setEnabled(true);
        btnFirst.setEnabled(true);
        btnLast.setEnabled(true);
        btnNext.setEnabled(true);
        btnPrevious.setEnabled(true);
        btnShuffle.setEnabled(true);
        btnUpload.setEnabled(true);
    }

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt)
    {
        index++;
        if (index >= link.size() - 1)
            index = 0;
        SetImage(index);
    }

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt)
    {
        index--;
        if (index <= 0)
            index = link.size() - 1;
        SetImage(index);
    }

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt)
    {
        index = link.size() - 1;
        SetImage(index);
    }

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt)
    {
        index = 0;
        SetImage(index);
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt)
    {
        JFileChooser browse = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (.jpg, .png)", "jpg", "png");
        browse.setFileFilter(filter);
        int returnValue = browse.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File path = browse.getSelectedFile();
            try {
                stm = con.createStatement();
                stm.execute("INSERT INTO tblImg VALUES('" + path + "')");
                System.out.println("> ADD SUCCESSFULLY...");
                slideShow.suspend();
                lblImg.setIcon(null);
                getLink();
                index = 0;

            } catch (SQLException ex) {
                Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void btnShuffleActionPerformed(java.awt.event.ActionEvent evt)
    {
        slideShow.suspend();
        lblImg.setIcon(null);
        getLink();
        index = 0;
    }

    // ====================== MOUSE EVENTS ======================
    private void btnShuffleMouseEntered(java.awt.event.MouseEvent evt) {
        btnShuffle.setBackground(moveIn);
    }

    private void btnShuffleMouseExited(java.awt.event.MouseEvent evt) {
        btnShuffle.setBackground(moveOut);
    }

    private void btnShuffleMousePressed(java.awt.event.MouseEvent evt) {
        btnShuffle.setBackground(clicked);
    }

    private void btnShuffleMouseReleased(java.awt.event.MouseEvent evt) {
        btnShuffle.setBackground(moveIn);
    }

    private void btnFirstMouseEntered(java.awt.event.MouseEvent evt) {
        btnFirst.setBackground(moveIn);
    }

    private void btnFirstMouseExited(java.awt.event.MouseEvent evt) {
        btnFirst.setBackground(moveOut);
    }

    private void btnFirstMousePressed(java.awt.event.MouseEvent evt) {
        btnFirst.setBackground(clicked);
    }

    private void btnFirstMouseReleased(java.awt.event.MouseEvent evt) {
        btnFirst.setBackground(moveIn);
    }

    private void btnPreviousMouseEntered(java.awt.event.MouseEvent evt) {
        btnPrevious.setBackground(moveIn);
    }

    private void btnPreviousMouseExited(java.awt.event.MouseEvent evt) {
        btnPrevious.setBackground(moveOut);
    }

    private void btnPreviousMousePressed(java.awt.event.MouseEvent evt) {
        btnPrevious.setBackground(clicked);
    }

    private void btnPreviousMouseReleased(java.awt.event.MouseEvent evt) {
        btnPrevious.setBackground(moveIn);
    }

    private void btnPlayMouseEntered(java.awt.event.MouseEvent evt) {
        btnPlay.setBackground(moveIn);
    }

    private void btnPlayMouseExited(java.awt.event.MouseEvent evt) {
        btnPlay.setBackground(moveOut);
    }

    private void btnPlayMousePressed(java.awt.event.MouseEvent evt) {
        btnPlay.setBackground(clicked);
    }

    private void btnPlayMouseReleased(java.awt.event.MouseEvent evt) {
        btnPlay.setBackground(moveIn);
    }

    private void btnNextMouseEntered(java.awt.event.MouseEvent evt) {
        btnNext.setBackground(moveIn);
    }

    private void btnNextMouseExited(java.awt.event.MouseEvent evt) {
        btnNext.setBackground(moveOut);
    }

    private void btnNextMousePressed(java.awt.event.MouseEvent evt) {
        btnNext.setBackground(clicked);
    }

    private void btnNextMouseReleased(java.awt.event.MouseEvent evt) {
        btnNext.setBackground(moveIn);
    }

    private void btnLastMouseEntered(java.awt.event.MouseEvent evt) {
        btnLast.setBackground(moveIn);
    }

    private void btnLastMouseExited(java.awt.event.MouseEvent evt) {
        btnLast.setBackground(moveOut);
    }

    private void btnLastMousePressed(java.awt.event.MouseEvent evt) {
        btnLast.setBackground(clicked);
    }

    private void btnLastMouseReleased(java.awt.event.MouseEvent evt) {
        btnLast.setBackground(moveIn);
    }

    private void btnUploadMouseEntered(java.awt.event.MouseEvent evt) {
        btnUpload.setBackground(moveIn);
    }

    private void btnUploadMouseExited(java.awt.event.MouseEvent evt) {
        btnUpload.setBackground(moveOut);
    }

    private void btnUploadMousePressed(java.awt.event.MouseEvent evt) {
        btnUpload.setBackground(clicked);
    }

    private void btnUploadMouseReleased(java.awt.event.MouseEvent evt) {
        btnUpload.setBackground(moveIn);
    }

    private void btnPauseMouseEntered(java.awt.event.MouseEvent evt) {
        btnPause.setBackground(moveIn);
    }

    private void btnPauseMouseExited(java.awt.event.MouseEvent evt) {
        btnPause.setBackground(moveOut);
    }

    private void btnPauseMousePressed(java.awt.event.MouseEvent evt) {
        btnPause.setBackground(clicked);
    }

    private void btnPauseMouseReleased(java.awt.event.MouseEvent evt) {
        btnPause.setBackground(moveIn);
    }

    /**
     * Start and execute program
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMain().setVisible(true);
            }
        });
    }

    // ================== VARIABLES ==================
    private javax.swing.JButton btnFirst;       // >>
    private javax.swing.JButton btnLast;        // <<
    private javax.swing.JButton btnNext;        // >|
    private javax.swing.JButton btnPause;       // ||
    private javax.swing.JButton btnPlay;        // >
    private javax.swing.JButton btnPrevious;    // |<
    private javax.swing.JButton btnShuffle;     // **
    private javax.swing.JButton btnUpload;      // ->
    private javax.swing.JPanel jPanel1;         // The dark theme panel
    private javax.swing.JLabel lblImg;          // Display image
}
