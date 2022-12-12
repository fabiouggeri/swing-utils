/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class PropertyValueCellRenderer extends CustomTableCellRenderer<JLabel> {

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      if (value != null) {
         value = "${" + value + "}";
      }
      return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
   }
}
