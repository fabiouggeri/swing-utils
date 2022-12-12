/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class ProgressBarTableCellRenderer extends ComponentCellRenderer<JProgressBar> {

   public ProgressBarTableCellRenderer(int min, int max) {
      super(new JProgressBar(JProgressBar.HORIZONTAL, min, max));
   }

   public ProgressBarTableCellRenderer() {
      this(0, 100);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      int n;
      JProgressBar renderer = (JProgressBar) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      renderer.setStringPainted(true);
      renderer.setBorderPainted(false);
      try {
         n = ((Number) value).intValue();
      } catch (Exception ex) {
         n = 0;
      }
      setValue(n);
      return renderer;
   }
}
