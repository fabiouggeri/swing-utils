/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author fabio_uggeri
 */
public class CustomListCellRenderer extends DefaultListCellRenderer {

   private static final long serialVersionUID = -7381024156853460934L;

   private Color noFocusSelectedBackground = null;
   private Color noFocusSelectedForeground = null;
   private Font selectedFont = null;
   private Font noFocusSelectedFont = null;

   public CustomListCellRenderer() {
      super();
   }

   @Override
   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean hasFocus) {
      final JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
      renderer.setFont(getFont(list, isSelected, hasFocus));
      renderer.setBackground(getBackground(list, isSelected, hasFocus));
      renderer.setForeground(getForeground(list, isSelected, hasFocus));
      return renderer;
   }


   private Font getFont(JList list, boolean selected, boolean hasFocus) {
      if (selected) {
         if (hasFocus) {
            if (selectedFont != null) {
               return selectedFont;
            }
         } else if (noFocusSelectedFont != null) {
            return noFocusSelectedFont;
         }
      }
      return list.getFont();
   }

   private Color getBackground(JList list, boolean selected, boolean hasFocus) {
      if (selected) {
         if (! hasFocus && noFocusSelectedBackground != null) {
            return noFocusSelectedBackground;
         }
         return list.getSelectionBackground();
      } else {
         return list.getBackground();
      }
   }

   private Color getForeground(JList list, boolean selected, boolean hasFocus) {
      if (selected) {
         if (! hasFocus && noFocusSelectedForeground != null) {
            return noFocusSelectedForeground;
         }
         return list.getSelectionForeground();
      } else {
         return list.getForeground();
      }
   }

   public Font getSelectedFont() {
      return selectedFont;
   }

   public void setSelectedFont(Font selectedFont) {
      this.selectedFont = selectedFont;
   }

   public Font getNoFocusSelectedFont() {
      return noFocusSelectedFont;
   }

   public void setNoFocusSelectedFont(Font noFocusSelectedFont) {
      this.noFocusSelectedFont = noFocusSelectedFont;
   }

   public Color getNoFocusSelectedBackground() {
      return noFocusSelectedBackground;
   }

   public void setNoFocusSelectedBackground(Color noFocusSelectedBackground) {
      this.noFocusSelectedBackground = noFocusSelectedBackground;
   }

   public Color getNoFocusSelectedForeground() {
      return noFocusSelectedForeground;
   }

   public void setNoFocusSelectedForeground(Color noFocusSelectedForeground) {
      this.noFocusSelectedForeground = noFocusSelectedForeground;
   }
}
