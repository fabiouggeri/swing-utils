/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class BooleanTableCellRenderer extends ComponentCellRenderer<JCheckBox> {

   public BooleanTableCellRenderer() {
      super(new JCheckBox());
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JCheckBox renderer = (JCheckBox) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      renderer.setHorizontalAlignment(JLabel.CENTER);
      renderer.setBorderPaintedFlat(true);
      if (value != null) {
         if (value instanceof Boolean) {
            renderer.setSelected((Boolean)value);
         } else {
            renderer.setSelected(Boolean.parseBoolean(value.toString()));
         }
      } else {
         renderer.setSelected(false);
      }
      return renderer;
   }
}
