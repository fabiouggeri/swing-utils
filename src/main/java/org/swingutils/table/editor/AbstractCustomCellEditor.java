/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Color;
import java.awt.Component;
import java.util.Comparator;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public abstract class AbstractCustomCellEditor<T extends Component> extends AbstractCellEditor implements CustomCellEditor<T> {

   private static final long serialVersionUID = 3260764305725151649L;

   private T component = null;

   private boolean editable = true;

   private int rowEdition = -1;

   private int colEdition = -1;

   private Object initialValue = null;

   private Comparator valueComparator = null;

   public AbstractCustomCellEditor() {
   }

   /**
    * @return the component
    */
   @Override
   public T getComponent() {
      if (component == null) {
         component = createComponent();
      }
      return component;
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
      if ((isSelected || isReturnComponentNotSelected())) {
         if (fireEditStarting(rowIndex, colIndex)) {
            Component c = getEditorComponent(table, value, isSelected, rowIndex, colIndex);
            c.setBackground(Color.getHSBColor(61, 5, 94));
            initialValue = value;
            rowEdition = rowIndex;
            colEdition = colIndex;
            return c;
         }
      }
      return null;
   }

   @Override
   public Object getCellEditorValue() {
      return getEditorValue();
   }

   @Override
   public boolean stopCellEditing() {
      boolean stopEdition = false;
      if (fireValidateValue(rowEdition, colEdition)) {
         stopEdition = fireEditingStop(rowEdition, colEdition);
         if (stopEdition) {
            fireEditingStopped();
            rowEdition = -1;
            colEdition = -1;
            initialValue = null;
         }
      } else {
         if (!getComponent().hasFocus()) {
            getComponent().requestFocusInWindow();
         }
      }
      return stopEdition;
   }

   public abstract Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex);

   public abstract Object getEditorValue();

   public abstract T createComponent();

   private boolean fireValidateValue(int rowIndex, int colIndex) {
      boolean validValue = true;
      for (CellEditorListener listener : getCellEditorListeners()) {
         if (listener instanceof CustomCellEditorListener) {
            if (!((CustomCellEditorListener) listener).isValidValue(new CustomChangeEvent(this, rowIndex, colIndex))) {
               validValue = false;
            }
         }
      }
      return validValue;
   }

   protected void startCellEditing() {
   }

   private boolean fireEditStarting(int rowIndex, int colIndex) {
      boolean canStart = true;
      startCellEditing();
      for (CellEditorListener listener : getCellEditorListeners()) {
         if (listener instanceof CustomCellEditorListener) {
            if (!((CustomCellEditorListener) listener).editStarting(new CustomChangeEvent(this, rowIndex, colIndex))) {
               canStart = false;
            }
         }
      }
      return canStart;
   }

   private boolean fireEditingStop(int rowIndex, int colIndex) {
      boolean canStop = true;
      for (CellEditorListener listener : getCellEditorListeners()) {
         if (listener instanceof CustomCellEditorListener) {
            if (!((CustomCellEditorListener) listener).editStopping(new CustomChangeEvent(this, rowIndex, colIndex))) {
               canStop = false;
            }
         }
      }
      return canStop;
   }

   protected boolean isReturnComponentNotSelected() {
      return false;
   }

   @Override
   public void setEditable(boolean editable) {
      this.editable = editable;
   }

   @Override
   public boolean isEditable() {
      return editable;
   }

   @Override
   public boolean isCellEditable(EventObject e) {
      return editable;
   }

   @Override
   public void setValueComparator(Comparator comparator) {
      this.valueComparator = comparator;
   }

   @Override
   public boolean isChanged() {
      final Object value = getEditorValue();
      if (valueComparator != null) {
         return valueComparator.compare(initialValue, value) == 0;
      } else {
         return (initialValue != null && ! initialValue.equals(value))
                 || (initialValue == null && value != null);
      }
   }

}
