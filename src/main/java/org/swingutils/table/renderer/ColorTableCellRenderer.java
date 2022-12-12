/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import org.swingutils.common.ColorIcon;
import org.swingutils.common.ColorValue;

/**
 *
 * @author fabio_uggeri
 */
public class ColorTableCellRenderer extends CustomTableCellRenderer<JLabel> {

   private static final boolean IS_GTK = "GTK".equals(UIManager.getLookAndFeel().getID());

   private final int iconSize;

   public ColorTableCellRenderer(final int iconSize) {
      this.iconSize = iconSize;
   }

   public ColorTableCellRenderer() {
      this(0);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      final int height;
      Icon icon;
      if (iconSize > 0) {
         height = iconSize;
      } else {
         height = IS_GTK ? 10 : Math.max(label.getFont().getSize(), 4);
      }
      if (value instanceof Color) {
         icon = new ColorIcon((Color)value, height);
         label.setText(ColorValue.toText((Color)value));
      } else {
         icon = new ColorIcon(null, height);
         label.setText(ColorValue.DEFAULT_COLOR.getText());
      }
      label.setIcon(icon);
      return label;
   }
}
