/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author fabio_uggeri
 */
public class ValidateDuplicatedKeyListener implements CustomCellEditorListener {

   private final JTable table;
   private final int column;
   private final String message;
   private final boolean ignoreCase;
   private final boolean removeSpaces;
   private final boolean validStopping;

   public ValidateDuplicatedKeyListener(final JTable table, int column, final String message, final boolean ignoreCase,
           final boolean removeSpaces, final boolean validStopping) {
      this.table = table;
      this.column = column;
      this.message = message;
      this.ignoreCase = ignoreCase;
      this.removeSpaces = removeSpaces;
      this.validStopping = validStopping;
   }

   public ValidateDuplicatedKeyListener(final JTable table, int column, final String message, final boolean ignoreCase,
           final boolean removeSpaces) {
      this(table, column, message, ignoreCase, removeSpaces, false);
   }

   public ValidateDuplicatedKeyListener(final JTable table, final int column, final String message, final boolean ignoreCase) {
      this(table, column, message, ignoreCase, true);
   }

   public ValidateDuplicatedKeyListener(final JTable table, final int column, final String message) {
      this(table, column, message, false);
   }

   @Override
   public boolean editStarting(CustomChangeEvent evt) {
      return true;
   }

   @Override
   public boolean editStopping(CustomChangeEvent evt) {
      return validStopping ? validValue(evt) : true;
   }

   @Override
   public void editingStopped(ChangeEvent e) {
   }

   @Override
   public void editingCanceled(ChangeEvent e) {
   }

   private boolean isDuplicatedKey(int row, Object value) {
      final int rowModel = table.convertRowIndexToModel(row);
      final int colModel = table.convertColumnIndexToModel(column);
      final Object rowValue = table.getModel().getValueAt(rowModel, colModel);
      if (rowValue instanceof String && ignoreCase) {
         String currentStrValue;
         if (removeSpaces) {
            currentStrValue = ((String) rowValue).replace(" ", "");
         } else {
            currentStrValue = (String) rowValue;
         }
         if (!currentStrValue.equalsIgnoreCase((String) value)) {
            return false;
         }
      } else if (rowValue == null || !rowValue.equals(value)) {
         return false;
      }
      return true;
   }

   @Override
   public boolean isValidValue(CustomChangeEvent evt) {
      return validStopping || validValue(evt);
   }

   private boolean validValue(CustomChangeEvent evt) throws HeadlessException {
      final TableCellEditor cellEditor = (TableCellEditor) evt.getSource();
      final int currentRow = table.getSelectedRow();
      final Object value = cellEditor.getCellEditorValue();
      for (int i = 0; i < table.getRowCount(); i++) {
         if (i != currentRow) {
            if (isDuplicatedKey(i, value)) {
               JOptionPane.showMessageDialog(null, message, "Aviso", JOptionPane.WARNING_MESSAGE);
               return false;
            }
         }
      }
      return true;
   }

}
