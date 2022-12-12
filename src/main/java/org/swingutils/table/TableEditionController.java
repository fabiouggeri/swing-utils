/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellEditor;
import org.swingutils.table.listener.DefaultCellKeyListener;
import org.swingutils.table.model.EditableTableModel;

/**
 *
 * @author fabio_uggeri
 */
public class TableEditionController {

   public static final int CONTROL_KEYS[] = new int[] { KeyEvent.VK_ENTER,
                                                        KeyEvent.VK_ESCAPE,
                                                        KeyEvent.VK_LEFT,
                                                        KeyEvent.VK_RIGHT,
                                                        KeyEvent.VK_TAB };
   private JTable table;

   private EditableTableModel model;

   private int firstEditableCol;

   private boolean insertEnabled;

   private boolean removeEnabled;

   private boolean editEnabled;

   public TableEditionController(JTable table, EditableTableModel model, int firstEditableCol, boolean insertEnabled,
           boolean removeEnabled, boolean editEnabled) {
      this.table = table;
      this.model = model;
      this.firstEditableCol = firstEditableCol;
      this.insertEnabled = insertEnabled;
      this.removeEnabled = removeEnabled;
      this.editEnabled = editEnabled;
   }

   public TableEditionController(JTable table, EditableTableModel model, int firstEditableCol) {
      this(table, model, firstEditableCol, true, true, true);
   }

   public static KeyListener comboListener(JTable table) {
      return new DefaultCellKeyListener(table, CONTROL_KEYS);
   }

   public static KeyListener textListener(JTable table) {
      return new DefaultCellKeyListener(table);
   }

   public void processKey(KeyEvent evt) {
      switch (evt.getKeyCode()) {
         case KeyEvent.VK_INSERT:
            if (insertEnabled && insertAction()) {
               evt.consume();
            }
            break;
         case KeyEvent.VK_DELETE:
            if (removeEnabled && removeAction()) {
               evt.consume();
            }
            break;
         case KeyEvent.VK_DOWN:
            if (editEnabled && downArrowKeyPressed()) {
               evt.consume();
            }
            break;
         case KeyEvent.VK_UP:
            if (editEnabled && upArrowKeyPressed()) {
               evt.consume();
            }
            break;
         case KeyEvent.VK_ESCAPE:
            if (table.isEditing() && table.getCellEditor() != null) {
               table.getCellEditor().cancelCellEditing();
            }
            evt.consume();
            break;
         case KeyEvent.VK_ENTER:
            if (editEnabled && enterKeyPressed(evt)) {
               evt.consume();
            }
            break;
         case KeyEvent.VK_TAB:
            if (tabKeyPressed(evt)) {
               evt.consume();
            }
            break;
//         case KeyEvent.VK_LEFT:
//         case KeyEvent.VK_RIGHT:
//         case KeyEvent.VK_PAGE_UP:
//         case KeyEvent.VK_PAGE_DOWN:
//            break;
//         default:
//            if(! editEnabled) {
//               evt.consume();
//            }
//            break;
      }
   }

   public boolean isEditEnabled() {
      return editEnabled;
   }

   public void setEditEnabled(boolean editEnabled) {
      this.editEnabled = editEnabled;
   }

   public boolean isInsertEnabled() {
      return insertEnabled;
   }

   public void setInsertEnabled(boolean insertEnabled) {
      this.insertEnabled = insertEnabled;
   }

   public boolean isRemoveEnabled() {
      return removeEnabled;
   }

   public void setRemoveEnabled(boolean removeEnabled) {
      this.removeEnabled = removeEnabled;
   }

   public void setReadOnly(boolean readOnly) {
      setInsertEnabled(! readOnly);
      setRemoveEnabled(! readOnly);
      setEditEnabled(! readOnly);
   }

   private boolean downArrowKeyPressed() {
      final int rowCount = table.getRowCount();
      final int currentRow = table.getSelectedRow();
      if (rowCount > 0) {
         if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
         }
         if (insertEnabled && !table.isEditing() && currentRow == rowCount - 1) {
            final int modelRow = table.convertRowIndexToModel(currentRow);
            if (!model.isEmptyKey(modelRow)) {
               appendAndSelect(true);
               return true;
            }
         }
      }
      return false;
   }

   private void appendAndSelect(boolean edit) {
      final int tableRow;
      model.appendItem();
      tableRow = table.convertRowIndexToView(model.getRowCount() - 1);
      table.changeSelection(tableRow, firstEditableCol, false, false);
      if (edit) {
         table.editCellAt(tableRow, firstEditableCol);
      }
   }

   private boolean upArrowKeyPressed() {
      final int rowCount = table.getRowCount();
      final int currentRow = table.getSelectedRow();
      if (rowCount > 0) {
         if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
         }
         if (removeEnabled && !table.isEditing() && currentRow == rowCount - 1 && rowCount > 1) {
            final int modelRow = table.convertRowIndexToModel(currentRow);
            if (model.isEmptyKey(modelRow)) {
               removeItem(modelRow, currentRow - 1);
               return true;
            }
         }
      }
      return false;
   }

   private void removeItem(final int index, final int newPosition) {
      model.removeItem(index);
      table.changeSelection(newPosition, firstEditableCol, false, false);
   }

   public boolean insertAction() {
      if (table.getRowCount() > 0) {
         final int rowModel = table.convertRowIndexToModel(table.getRowCount() - 1);
         if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
         }
         if (!table.isEditing() && !model.isEmptyKey(rowModel)) {
            appendAndSelect(true);
            return true;
         }
      } else {
         appendAndSelect(true);
         return true;
      }
      return false;
   }

   public boolean removeAction() {
      final int currentRow = table.getSelectedRow();
      if (currentRow >= 0) {
         final int modelRow = table.convertRowIndexToModel(currentRow);
         if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
         }
         removeItem(modelRow, currentRow == table.getRowCount() - 1 ? currentRow - 1 : currentRow);
         if (table.getRowCount() == 0) {
            appendAndSelect(false);
         }
      }
      return true;
   }

   private boolean enterKeyPressed(KeyEvent evt) {
      if (table.getRowCount() > 0) {
         int col = table.getSelectedColumn();
         int tableRow = table.getSelectedRow();
         if (col >= 0 && col  < table.getColumnCount() && tableRow >= 0 && tableRow < table.getRowCount()) {
            final TableCellEditor editor = table.getCellEditor(tableRow, col);
            if (!table.isEditing()
                    && editor != null
                    && editor.isCellEditable(evt)
                    && !Boolean.class.equals(table.getModel().getColumnClass(col))) {
               table.editCellAt(tableRow, col);
               requesFocus(table.getEditorComponent());
            } else {
               if (table.getCellEditor() != null) {
                  table.getCellEditor().stopCellEditing();
               }
               if (!table.isEditing()) {
                  if (col < table.getColumnCount() - 1) {
                     table.changeSelection(tableRow, col + 1, false, false);
                     table.editCellAt(tableRow, col + 1);
                     requesFocus(table.getEditorComponent());
                  } else if (tableRow == table.getRowCount() - 1) {
                     final int modelRow = table.convertRowIndexToModel(tableRow);
                     if (!model.isEmptyKey(modelRow)) {
                        appendAndSelect(true);
                        requesFocus(table.getEditorComponent());
                     }
                  } else {
                     table.changeSelection(tableRow + 1, firstEditableCol, false, false);
                     table.editCellAt(tableRow + 1, firstEditableCol);
                     requesFocus(table.getEditorComponent());
                  }
               }
            }
         }
      }
      return true;
   }

   private void requesFocus(Component editorComponent) {
      if (editorComponent != null) {
         editorComponent.requestFocus();
      }
   }

   public void changeSelection(ListSelectionEvent e) {
      final int lastRow = table.getRowCount() - 1;
      if (lastRow >= 0) {
         final int modelRow = table.convertRowIndexToModel(lastRow);
         if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
         }
         if (table.getSelectedRow() >= 0 && table.getSelectedRow() < lastRow && model.isEmptyKey(modelRow)) {
            model.removeItem(modelRow);
         }
      }
   }

   private boolean shiftTabKeyPressed(KeyEvent evt) {
      if (table.getRowCount() > 0) {
         int col = table.getSelectedColumn();
         int tableRow = table.getSelectedRow();
         if (col >= 0 && col  < table.getColumnCount() && tableRow >= 0 && tableRow < table.getRowCount()) {
            if (table.getCellEditor() != null) {
               table.getCellEditor().stopCellEditing();
            }
            if (!table.isEditing()) {
               if (col > 0) {
                  table.changeSelection(tableRow, col - 1, false, false);
                  table.editCellAt(tableRow, col - 1);
                  requesFocus(table.getEditorComponent());
               } else if (tableRow > 0) {
                  table.changeSelection(tableRow - 1, table.getColumnCount() - 1, false, false);
                  table.editCellAt(tableRow - 1, table.getColumnCount() - 1);
                  requesFocus(table.getEditorComponent());
               }
            }
         }
      }
      return true;
   }

   private boolean tabKeyPressed(KeyEvent evt) {
      if (table.isEditing()) {
         if (evt.isShiftDown()) {
            return shiftTabKeyPressed(evt);
         } else {
            return enterKeyPressed(evt);
         }
      } else {
         table.transferFocus();
      }
      return true;
   }

   public EditableTableModel getModel() {
      return model;
   }

   public void setModel(EditableTableModel model) {
      this.model = model;
   }

   public JTable getTable() {
      return table;
   }

   public void setTable(JTable table) {
      this.table = table;
   }

   public int getFirstEditableCol() {
      return firstEditableCol;
   }

   public void setFirstEditableCol(int firstEditableCol) {
      this.firstEditableCol = firstEditableCol;
   }
}
