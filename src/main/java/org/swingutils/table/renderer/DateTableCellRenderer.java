/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class DateTableCellRenderer extends CustomTableCellRenderer<JLabel> {

   private DateFormat dateFormat;

   public DateTableCellRenderer(DateFormat dateFormat) {
      this.dateFormat = dateFormat;
   }

   public DateTableCellRenderer(final String format) {
      this(new SimpleDateFormat(format));
   }

   public DateTableCellRenderer() {
      this("dd/MM/yyyy");
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if (value instanceof Date) {
         renderer.setText(dateFormat.format((Date)value));
      } else if (value instanceof String) {
         try {
            renderer.setText(dateFormat.format(dateFormat.parse((String)value)));
         } catch (ParseException ex) {
            renderer.setText("Data inválida");
         }
      }
      return renderer;
   }

}
