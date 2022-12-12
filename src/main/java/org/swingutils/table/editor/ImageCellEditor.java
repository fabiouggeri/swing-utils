/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import static javax.swing.SwingConstants.CENTER;
import org.swingutils.Utils;
import org.swingutils.panel.ImagePreviewPanel;

/**
 *
 * @author fabio_uggeri
 */
public class ImageCellEditor extends AbstractCustomCellEditor<JPanel> implements ActionListener {

   private File defaultDir;

   private boolean scaleY;

   private boolean scaleX;

   private Image emptyImage;

   private JLabel imageLabel;

   private Image image;

   private JTable table = null;

   private int tableColumn = -1;

   public ImageCellEditor(File defaultDir, Image defaultImage, boolean scaleY, boolean scaleX) {
      this.defaultDir = defaultDir;
      this.emptyImage = defaultImage;
      this.scaleY = scaleY;
      this.scaleX = scaleX;
   }

   public ImageCellEditor(File defaultDir) {
      this.defaultDir = defaultDir;
      this.scaleY = true;
      this.scaleX = false;
      this.image = null;
      try {
         this.emptyImage = ImageIO.read(ImageCellEditor.class.getResource("/image-fault.png"));
      } catch (IOException ex) {
         this.emptyImage = null;
      }
   }

   public ImageCellEditor() {
      this(new File(System.getProperty("user.home")));
   }

   @Override
   public Object getEditorValue() {
      return !Utils.equals(image, emptyImage) ? image : null;
   }

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      final Component component = getComponent();
      image = value != null ? (Image) value : emptyImage;
      this.table = table;
      this.tableColumn = column;
      updateLabelIcon();
      return component;
   }

   private void updateLabelIcon() {
      final int scaleWidth;
      final int scaleHeight;

      if (image instanceof BufferedImage) {
         scaleWidth = scaleX ? table.getColumnModel().getColumn(tableColumn).getWidth() : ((BufferedImage) image).getWidth();
         scaleHeight = scaleY ? table.getRowHeight() : ((BufferedImage) image).getHeight();
      } else {
         scaleWidth = scaleX ? table.getColumnModel().getColumn(tableColumn).getWidth() : 0;
         scaleHeight = scaleY ? table.getRowHeight() : 0;
      }
      if (image != null) {
         if (scaleWidth > 0 && scaleHeight > 0) {
            imageLabel.setIcon(new ImageIcon(((Image) image).getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_DEFAULT)));
         } else {
            imageLabel.setIcon(new ImageIcon(image));
         }
         imageLabel.setText("");
      } else {
         imageLabel.setIcon(null);
         imageLabel.setText("<sem imagem>");
      }
      imageLabel.setHorizontalAlignment(CENTER);
   }

   @Override
   public JPanel createComponent() {
      JPanel panel;
      JPanel buttonsPanel;
      final JButton openButton;
      final JButton clearButton;
      openButton = new JButton();
      openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open_image_folder16.png")));
      openButton.addActionListener(this);
      openButton.setFocusable(false);
      openButton.setFocusPainted(false);
      openButton.setMargin(new Insets(0, 0, 0, 0));
      openButton.setOpaque(false);
      openButton.setName("open");
      clearButton = new JButton();
      clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/borracha_16.png")));
      clearButton.addActionListener(this);
      clearButton.setFocusable(false);
      clearButton.setFocusPainted(false);
      clearButton.setMargin(new Insets(0, 0, 0, 0));
      clearButton.setOpaque(false);
      clearButton.setName("clear");
      imageLabel = new JLabel();
      imageLabel.setFocusable(true);
      imageLabel.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (!e.isConsumed() && e.getKeyCode() == KeyEvent.VK_SPACE) {
               e.consume();
               openImageSelectionDialog();
            }
         }

      });
      panel = new JPanel(new BorderLayout()) {

         @Override
         public void setBackground(Color bg) {
            imageLabel.setBackground(bg);
         }

         @Override
         public void setForeground(Color fg) {
            imageLabel.setForeground(fg);
         }

         @Override
         public void addNotify() {
            super.addNotify();
            imageLabel.requestFocus();
         }
      };
      panel.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (!e.isConsumed() && e.getKeyCode() == KeyEvent.VK_SPACE) {
               e.consume();
               openImageSelectionDialog();
            }
         }

      });
      buttonsPanel = new JPanel(new BorderLayout());
      buttonsPanel.add(openButton, BorderLayout.WEST);
      buttonsPanel.add(clearButton, BorderLayout.EAST);
      panel.add(imageLabel);
      panel.add(buttonsPanel, BorderLayout.EAST);
      return panel;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() instanceof JButton) {
         if ("open".equalsIgnoreCase(((JButton) e.getSource()).getName())) {
            openImageSelectionDialog();
         } else if ("clear".equalsIgnoreCase(((JButton) e.getSource()).getName())) {
            clearImage();
         }
      }
   }

   private void clearImage() throws HeadlessException {
      image = emptyImage;
      updateLabelIcon();
   }

   private void openImageSelectionDialog() throws HeadlessException {
      final JFileChooser chooser = new JFileChooser(defaultDir);
      final ImagePreviewPanel preview = new ImagePreviewPanel();
      chooser.setAccessory(preview);
      chooser.addPropertyChangeListener(preview);
      if (chooser.showOpenDialog(getComponent()) == JFileChooser.APPROVE_OPTION) {
         try {
            image = ImageIO.read(chooser.getSelectedFile());
            if (image == null) {
               image = emptyImage;
            }
            updateLabelIcon();
         } catch (IOException ex) {
            JOptionPane.showMessageDialog(getComponent(), ex, "Erro ao carregar imagem", JOptionPane.ERROR_MESSAGE);
         }
      }
      defaultDir = chooser.getCurrentDirectory();
   }

   @Override
   public void addKeyListener(final KeyListener keyListener) {
      getComponent().addKeyListener(keyListener);
      imageLabel.addKeyListener(keyListener);
   }

   public boolean isScaleX() {
      return scaleX;
   }

   public void setScaleX(boolean scaleX) {
      this.scaleX = scaleX;
   }

   public boolean isScaleY() {
      return scaleY;
   }

   public void setScaleY(boolean scaleY) {
      this.scaleY = scaleY;
   }

   public void setEmptyImage(Image emptyImage) {
      this.emptyImage = emptyImage;
   }

   public File getDefaultDir() {
      return defaultDir;
   }

   public void setDefaultDir(File defaultDir) {
      this.defaultDir = defaultDir;
   }
}
