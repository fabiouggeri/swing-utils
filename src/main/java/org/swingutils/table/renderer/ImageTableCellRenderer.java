/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class ImageTableCellRenderer extends CustomTableCellRenderer<JLabel> {

   private boolean scaleY;
   private boolean scaleX;
   private Image defaultImage;

   public ImageTableCellRenderer(boolean scaleY, boolean scaleX, Image image) {
      this.scaleY = scaleY;
      this.scaleX = scaleX;
      this.defaultImage = image;
   }

   public ImageTableCellRenderer() {
      this.scaleY = true;
      this.scaleX = false;
      try {
         this.defaultImage = ImageIO.read(ImageTableCellRenderer.class.getResource("/image-fault.png"));
      } catch (Exception ex) {
         this.defaultImage = null;
      }
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      Image image = value != null ? (Image) value : defaultImage;
      int scaleWidth;
      int scaleHeight;

      if (image instanceof BufferedImage) {
         scaleWidth = scaleX ? table.getColumnModel().getColumn(column).getWidth() : ((BufferedImage) image).getWidth();
         scaleHeight = scaleY ? table.getRowHeight() : ((BufferedImage) image).getHeight();
      } else {
         scaleWidth = scaleX ? table.getColumnModel().getColumn(column).getWidth() : 0;
         scaleHeight = scaleY ? table.getRowHeight() : 0;
      }
      if (scaleWidth > 0 && scaleHeight > 0) {
         image = ((Image) image).getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_DEFAULT);
      }
      if (image != null) {
         label.setIcon(new ImageIcon(image));
         label.setText("");
      } else {
         label.setIcon(null);
         label.setText("<sem imagem>");
      }
      label.setHorizontalAlignment(CENTER);
      return label;
   }

   public void setDefaultImage(Image defaultImage) {
      this.defaultImage = defaultImage;
   }

   public void setScaleX(boolean scaleX) {
      this.scaleX = scaleX;
   }

   public void setScaleY(boolean scaleY) {
      this.scaleY = scaleY;
   }
}
