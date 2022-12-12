/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import org.swingutils.StringUtils;

/**
 *
 * @author fabio_uggeri
 */
public class PasswordCellRenderer extends ComponentCellRenderer<JPasswordField>{

   public PasswordCellRenderer() {
      super(new JPasswordField());
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JPasswordField renderer = (JPasswordField) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (value == null) {
         renderer.setText("");
      } else if (value instanceof String) {
         renderer.setText(StringUtils.replicate('*', ((String)value).length()));
      } else {
         renderer.setText(StringUtils.replicate('*', ((char[])value).length));
      }
      return renderer;
   }
}
