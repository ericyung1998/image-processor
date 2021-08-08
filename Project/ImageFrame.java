import java.nio.Buffer;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.text.*;
import javax.imageio.ImageIO;

public class ImageFrame extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
    private JDesktopPane theDesktop;
    private JInternalFrame frameOriginal;
    private JInternalFrame frameProcessed;
    private JInternalFrame textProcessed;

    // ------------------------------------------

    private ImagePanel imagePanelOriginal;
    private ImagePanel imagePanelProcessed;
    private JTextPane textPanelResult;
    private JLabel infoBar;

    // ------------------------------------------

    private JMenuBar menuBar;
    ;
    private JMenu menuFile;
    private JMenu menuProcessing;
    private JMenu menuLookAndFeel;
    private JMenu menuHelp;

    // ------------------------------------------

    private JMenuItem menuItemOpen;
    private JMenuItem menuItemExit;
    private JMenuItem menuItem_darken;
    private JMenuItem menuItem_sharpen;
    private JMenuItem menuItem_beautify;
    private JMenuItem menuItem_channelMask;
    private JMenuItem menuItem_disco;
    private JMenuItem menuItem_flip;

    private JRadioButtonMenuItem menuItemMetal;
    private JRadioButtonMenuItem menuItemMotif;
    private JRadioButtonMenuItem menuItemWindows;

    private JMenuItem menuItemAbout;

    // ------------------------------------------

    private BufferedImage inputImage;
    private Image resultImage;
    private JScrollPane scrollPane;
    private String title;
    private Vector imageBuffer;
    private Vector messageBuffer;

    public ImageFrame(String tit) {
        theDesktop = new JDesktopPane();     // create desktop pane
        add(theDesktop);                     // add desktop pane to frame

        title = tit;
        menuBar = new JMenuBar();

        menuFile = new JMenu("File");
        menuItemOpen = new JMenuItem("Open");
        menuItemOpen.addActionListener(this);
        menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(this);
        menuFile.add(menuItemOpen);
        menuFile.addSeparator();
        menuFile.add(menuItemExit);

        menuProcessing = new JMenu("Processing");
        menuItem_darken = new JMenuItem("Darken");
        menuItem_sharpen = new JMenuItem("Sharpen");
        menuItem_disco = new JMenuItem("Disco");
        menuItem_beautify = new JMenuItem("Beautify");
        menuItem_flip = new JMenuItem("Flip");
        menuItem_channelMask = new JMenuItem("Channel masking");


        menuItem_darken.addActionListener(this);
        menuItem_sharpen.addActionListener(this);
        menuItem_disco.addActionListener(this);
        menuItem_beautify.addActionListener(this);
        menuItem_flip.addActionListener(this);
        menuItem_channelMask.addActionListener(this);

        menuProcessing.add(menuItem_darken);
        menuProcessing.add(menuItem_sharpen);
        menuProcessing.add(menuItem_beautify);
        menuProcessing.add(menuItem_disco);
        menuProcessing.add(menuItem_flip);
        menuProcessing.add(menuItem_channelMask);

        menuItem_darken.setEnabled(false);
        menuItem_sharpen.setEnabled(false);
        menuItem_beautify.setEnabled(false);
        menuItem_disco.setEnabled(false);
        menuItem_flip.setEnabled(false);
        menuItem_channelMask.setEnabled(false);

        menuProcessing.setEnabled(false);

        menuLookAndFeel = new JMenu("Look and Feel");
        menuItemMetal = new JRadioButtonMenuItem("Metal");
        menuItemMotif = new JRadioButtonMenuItem("Motif");
        menuItemWindows = new JRadioButtonMenuItem("Windows");
        menuItemMetal.addActionListener(this);
        menuItemMotif.addActionListener(this);
        menuItemWindows.addActionListener(this);
        menuLookAndFeel.add(menuItemMetal);
        menuLookAndFeel.add(menuItemMotif);
        menuLookAndFeel.add(menuItemWindows);
        ButtonGroup lookAndFeelGroup = new ButtonGroup();
        lookAndFeelGroup.add(menuItemMetal);
        lookAndFeelGroup.add(menuItemMotif);
        lookAndFeelGroup.add(menuItemWindows);
        menuItemMetal.setSelected(true);

        menuHelp = new JMenu("Help");
        menuItemAbout = new JMenuItem("About");
        menuItemAbout.addActionListener(this);
        menuHelp.add(menuItemAbout);

        infoBar = new JLabel(" Info.:");
        infoBar.setFont(new Font("Arial", Font.PLAIN, 12));

        menuBar.add(menuFile);
        menuBar.add(menuProcessing);
        menuBar.add(menuLookAndFeel);
        menuBar.add(menuHelp);

        imagePanelOriginal = new ImagePanel(new Rectangle());
        imagePanelOriginal.addMouseMotionListener(this);
        imagePanelOriginal.addMouseListener(this);

        imagePanelProcessed = new ImagePanel(new Rectangle());
        getContentPane().add(infoBar, BorderLayout.SOUTH);
        setIconImage(new ImageIcon("logo.png").getImage());

        inputImage = null;
        resultImage = null;
    }

    public void showWin() {
        setSize(800, 600); // set frame size
        setTitle(title);
        setJMenuBar(menuBar);
        setLocationRelativeTo(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void imageFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open a file");
        fileChooser.addChoosableFileFilter(new ExtensionFileDialogFilter("jpg"));

        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                inputImage = ImageIO.read(file);
                resultImage = ImageIO.read(file);
                imagePanelOriginal.setImage(inputImage);
                imagePanelProcessed.setImage(resultImage);

                // ---------------------------
                frameOriginal = new JInternalFrame("Original Image", true, true, true, true);
                frameOriginal.add(imagePanelOriginal, BorderLayout.CENTER);
                frameOriginal.pack();
                theDesktop.add(frameOriginal);
                frameOriginal.setVisible(true);

                frameProcessed = new JInternalFrame("Processed Image", true, true, true, true);
                frameProcessed.add(imagePanelProcessed, BorderLayout.CENTER);
                frameProcessed.pack();
                theDesktop.add(frameProcessed);

                textPanelResult = new JTextPane();

                textProcessed = new JInternalFrame("Processed Image", true, true, true, true);
                textProcessed.add(textPanelResult, BorderLayout.CENTER);
                textProcessed.pack();
                theDesktop.add(textProcessed);

                scrollPane = new JScrollPane(textPanelResult);
                BufferedImage bufImg = ImageProcessor.convert(inputImage);
                scrollPane.setPreferredSize(new Dimension(bufImg.getWidth(), bufImg.getHeight()));
                textProcessed.add(scrollPane, BorderLayout.CENTER);
                textProcessed.pack();

                JInternalFrame[] frames = theDesktop.getAllFrames();
                int x = 10;
                int y = 10;
                for (int i = frames.length - 1; i >= 0; i--) {
                    frames[i].setLocation(x, y);
                    x += 30;
                    y += 30;
                }

                getContentPane().add(theDesktop);
                // ---------------------------

                imageBuffer = new Vector();
                imageBuffer.add(inputImage);
                messageBuffer = new Vector();

                menuItem_beautify.setEnabled(true);
                menuItem_sharpen.setEnabled(true);
                menuItem_darken.setEnabled(true);
                menuItem_flip.setEnabled(true);
                menuItem_channelMask.setEnabled(true);
                menuItem_disco.setEnabled(true);

                infoBar.setText(" Info.: Filename = " + file.getName() + ", Image's size = " + inputImage.getWidth() + " x " + inputImage.getHeight());
                messageBuffer.add(infoBar.getText());

                menuProcessing.setEnabled(true);
            } catch (IOException e) {
            }
        }
    }
    
     private void beautify(){
            int blurRadius = 2;
            double brightenFactor = 1.1; 
        try {
            String fileName = "beautify.jpg";
            File outputfile = new File(fileName);
            BufferedImage result = ImageProcessor.beautify(inputImage, blurRadius, brightenFactor);
            infoBar.setText(" Info.: Creating image ...");
            ImageIO.write(result, "jpg", outputfile);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    resultImage = Toolkit.getDefaultToolkit().createImage(fileName);
                    imagePanelProcessed.setImage(resultImage);
                    infoBar.setText("Info.: beautified image is obtained");
                    textProcessed.setVisible(false);
                    frameProcessed.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }        
    }    

     private void channelMask(){
        try {
            String colorOption = JOptionPane.showInputDialog("Please input the color option: red, blue or green");
            String fileName = "channelMask_"+colorOption+".jpg";
            File outputfile = new File(fileName);
            BufferedImage result = ImageProcessor.channelMask(inputImage, colorOption);
            infoBar.setText(" Info.: Creating image ...");
            ImageIO.write(result, "jpg", outputfile);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    resultImage = Toolkit.getDefaultToolkit().createImage(fileName);
                    imagePanelProcessed.setImage(resultImage);
                    infoBar.setText("Info.: channel-masked image is obtained");
                    textProcessed.setVisible(false);
                    frameProcessed.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }        
    }    
 
     private void sharpen(){
        try {
            String fileName = "sharpen.jpg";
            File outputfile = new File(fileName);
            BufferedImage result = ImageProcessor.sharpen(inputImage);
            infoBar.setText(" Info.: Creating image ...");
            ImageIO.write(result, "jpg", outputfile);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    resultImage = Toolkit.getDefaultToolkit().createImage(fileName);
                    imagePanelProcessed.setImage(resultImage);
                    infoBar.setText("Info.: sharpened image is obtained");
                    textProcessed.setVisible(false);
                    frameProcessed.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }        
    }
 
     private void disco(){      //some problem
        int fps = 4;
        try {
            String fileName = "disco.gif";
            ImageOutputStream imageOutput = new FileImageOutputStream(new File(fileName));
            GifSequenceWriter gifWriter = new GifSequenceWriter(imageOutput, BufferedImage.TYPE_INT_RGB, 1000 / fps, true);
            infoBar.setText(" Info.: Creating gif frames ...");
            
            BufferedImage[] results = ImageProcessor.disco(inputImage);
            
            for (int i = 0; i < fps; i++) {
                gifWriter.writeToSequence(results[i]);
            }
            gifWriter.close();
            imageOutput.close();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    resultImage = Toolkit.getDefaultToolkit().createImage(fileName);
                    imagePanelProcessed.setImage(resultImage);
                    infoBar.setText("Info.: disco gif is obtained");
                    textProcessed.setVisible(false);
                    frameProcessed.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }        
    }
 
 
    private void flip(){
        int fps = 2;
        try {
            String fileName = "flipped.gif";
            ImageOutputStream imageOutput = new FileImageOutputStream(new File(fileName));
            GifSequenceWriter gifWriter = new GifSequenceWriter(imageOutput, BufferedImage.TYPE_INT_RGB, 1000 / fps, true);
            infoBar.setText(" Info.: Creating gif frames ...");
            BufferedImage[] frames = new BufferedImage[fps];
            
            gifWriter.writeToSequence(inputImage);
            frames[0] = inputImage;
            BufferedImage newFrame = ImageProcessor.flip(inputImage);
            gifWriter.writeToSequence(newFrame);
            frames[1] = newFrame;

            
            gifWriter.close();
            imageOutput.close();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    resultImage = Toolkit.getDefaultToolkit().createImage(fileName);
                    imagePanelProcessed.setImage(resultImage);
                    infoBar.setText("Info.: Flipping gif is obtained");
                    textProcessed.setVisible(false);
                    frameProcessed.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }        
    }
 
    private void darken(){
        double darkenFactor = 0.5;
        int fps = 6;
        try {
            String fileName = "darken.gif";
            ImageOutputStream imageOutput = new FileImageOutputStream(new File(fileName));
            GifSequenceWriter gifWriter = new GifSequenceWriter(imageOutput, BufferedImage.TYPE_INT_RGB, 1000 / fps, true);
            infoBar.setText(" Info.: Creating gif frames ...");
            BufferedImage[] frames = new BufferedImage[fps];
            for (int i = 0; i < fps; i++) {
                BufferedImage newFrame = ImageProcessor.darken(inputImage, Math.pow(darkenFactor, i));          //the function call 
                gifWriter.writeToSequence(newFrame);
                frames[i] = newFrame;
                if (i!=0 && i != fps) {
                    frames[i] = newFrame;
                }
            }
            gifWriter.close();
            imageOutput.close();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    resultImage = Toolkit.getDefaultToolkit().createImage(fileName);
                    imagePanelProcessed.setImage(resultImage);
                    infoBar.setText("Info.: darkening gif is obtained");
                    textProcessed.setVisible(false);
                    frameProcessed.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }        
    }


    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == menuItemOpen)
            imageFileChooser();
        else if (source == menuItemExit)
            System.exit(0);
        else if (source == menuItem_sharpen)
            sharpen();
        else if (source == menuItem_beautify)
            beautify();
        else if (source == menuItem_disco)
            disco();
        else if (source == menuItem_flip)
            flip();
        else if (source == menuItem_channelMask)
            channelMask();
        else if (source == menuItem_darken)
            darken();
        else if (source == menuItemAbout) {
            AboutDialog ad = new AboutDialog(this);
            ad.setVisible(true);
        } else {
            String str = "";
            if (source == menuItemMetal)
                str = "javax.swing.plaf.metal.MetalLookAndFeel";
            else if (source == menuItemMotif)
                str = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            else if (source == menuItemWindows)
                str = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

            try {
                UIManager.setLookAndFeel(str);
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception exception) {
            }
        }
        repaint();
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}