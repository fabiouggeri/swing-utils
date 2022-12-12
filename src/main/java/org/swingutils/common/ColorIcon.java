/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author fabio_uggeri
 */
public class ColorIcon implements Icon {
   
   private final Color color;
   private final int size;

   public ColorIcon(Color color, int size) {
      this.color = color;
      this.size = size;
   }

   @Override
   public void paintIcon(Component c, Graphics g, int x, int y) {
      g.setColor(Color.black);
      g.drawRect(x, y, size - 1, size - 1);
      if (null == color) {
         g.drawLine(x, y + size - 1, x + size - 1, y);
      } else {
         g.setColor(color);
         g.fillRect(x + 1, y + 1, size - 2, size - 2);
      }
   }

   @Override
   public int getIconWidth() {
      return size;
   }

   @Override
   public int getIconHeight() {
      return size;
   }
   
}
