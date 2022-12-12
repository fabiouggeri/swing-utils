/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class ListCellRenderer extends CustomTableCellRenderer<JLabel> {

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (value != null) {
         if (value instanceof Collection) {
            final StringBuilder sb = new StringBuilder();
            final Collection items = (Collection) value;
            for (Object i : items) {
               if (sb.length() > 0) {
                  sb.append(", ");
               }
               sb.append(i.toString());
            }
            renderer.setText(sb.toString());
         } else if (value.getClass().isArray()) {
            final StringBuilder sb = new StringBuilder();
            final Object items[] = (Object[]) value;
            for (Object i : items) {
               if (sb.length() > 0) {
                  sb.append(", ");
               }
               sb.append(i.toString());
            }
            renderer.setText(sb.toString());
         } else {
            renderer.setText(value.toString());
         }
      } else {
         renderer.setText("");
      }
      return renderer;
   }
}
