/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class ButtonRenderer extends ComponentCellRenderer<JButton> {

   public ButtonRenderer(final String label, final Icon icon) {
      super(new JButton(label, icon));
   }

   public ButtonRenderer(final Icon icon) {
      this("", icon);
   }

   public ButtonRenderer(final String label) {
      this(label, null);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      final JButton renderer = (JButton) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      renderer.setOpaque(true);
      renderer.setFocusable(false);
      return renderer;
   }
}
