/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.model;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public abstract class AbstractEditableTableModel<T> extends AbstractTableModel implements EditableTableModel<T> {

   private final List<BitSet> changed = new ArrayList<>();
   private final List<T> removed = new ArrayList<>();
   private final List<Integer> inserted = new ArrayList<>();
   private final List<T> items;
   private final String columnsNames[];
   private final Class<?> columnsTypes[];
   private final int fisrtEditableCol;
   private boolean readOnly = false;

   public AbstractEditableTableModel(List<T> itens, String[] columnsNames, Class<?>[] columnsTypes, int fisrtEditableCol) {
      this.items = itens;
      this.columnsNames = columnsNames;
      this.columnsTypes = columnsTypes;
      this.fisrtEditableCol = fisrtEditableCol;
   }

   public AbstractEditableTableModel(String[] columnsNames, Class<?>[] columnsTypes, int firstEditableCol) {
      this(new ArrayList<T>(), columnsNames, columnsTypes, firstEditableCol);
   }

   protected abstract boolean isValuesEquals(T item, int column, Object aValue);

   protected abstract void setValue(T item, int column, Object aValue);

   protected abstract T createItem();

   protected boolean isNewItem(final int row) {
      return inserted.contains(row);
   }

   @Override
   public String getColumnName(int column) {
      if (column >= 0 && column < columnsNames.length) {
         return columnsNames[column];
      }
      return "";
   }

   @Override
   public Class<?> getColumnClass(int column) {
      if (column >= 0 && column < columnsTypes.length) {
         return columnsTypes[column];
      }
      return Object.class;
   }

   @Override
   public int getRowCount() {
      return items.size();
   }

   @Override
   public int getColumnCount() {
      return columnsNames.length;
   }

   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return ! readOnly && columnIndex >= fisrtEditableCol && (!isKey(columnIndex) || isNewItem(rowIndex));
   }

   @Override
   public void setValueAt(Object aValue, int rowIndex, int column) {
      if (column >= fisrtEditableCol && column < columnsNames.length) {
         final T item = items.get(rowIndex);
         if (!isValuesEquals(item, column, aValue)) {
            setChanged(rowIndex, column);
            setValue(item, column, aValue);
            fireTableRowsUpdated(rowIndex, rowIndex);
         }
      }
   }

   @Override
   public List<T> getItems() {
      return items;
   }

   public void notificaAtualizacao() {
      fireTableDataChanged();
   }

   @Override
   public T getItemAt(int row) {
      if (row >= 0 && row < items.size()) {
         return items.get(row);
      }
      return null;
   }

   @Override
   public boolean isInserted(int index) {
      return inserted.contains(index);
   }

   @Override
   public boolean isChanged(int index) {
      return index < changed.size() && changed.get(index) != null;
   }

   @Override
   public boolean isChangedOrInserted(int index) {
      return isChanged(index) || inserted.contains(index);
   }

   @Override
   public T appendItem() {
      final T item = createItem();
      inserted.add(items.size());
      items.add(item);
      fireTableRowsInserted(items.size() - 1, items.size() - 1);
      return item;
   }

   @Override
   public T removeItem(int index) {
      final T item = items.remove(index);
      if (item != null) {
         if (!inserted.remove((Integer)index)) {
            removed.add(item);
         }
         removeChange(index);
         renumInserts(index);
         fireTableRowsDeleted(index, index);
      }
      return item;
   }

   private void renumInserts(int index) {
      for (int i = 0; i < inserted.size(); i++) {
         final int indexInserted = inserted.get(i);
         if (indexInserted >= index) {
            inserted.set(i, indexInserted - 1);
         }
      }
   }

   @Override
   public void add(T item) {
      items.add(item);
      fireTableRowsInserted(items.size() - 1, items.size() - 1);
   }

   @Override
   public void addAll(List<T> novosItens) {
      if (novosItens.size() > 0) {
         int first = items.size();
         items.addAll(novosItens);
         fireTableRowsInserted(first, items.size() - 1);
      }
   }

   @Override
   public boolean isEmptyKey(int index) {
      final T item = getItemAt(index);
      return item == null || isEmptyKey(item);
   }

   @Override
   public Collection<T> getInserted() {
      final ArrayList<T> insertedItems = new ArrayList<>(inserted.size());
      for (int index : inserted) {
         if (! isEmptyKey(index)) {
            insertedItems.add(getItemAt(index));
         }
      }
      return insertedItems;
   }

   @Override
   public Collection<T> getRemoved() {
      return removed;
   }

   @Override
   public Collection<T> getChanged() {
      final List<T> listaModificados = new ArrayList<>();
      for (int i = 0; i < changed.size(); i++) {
         if (changed.get(i) != null) {
            listaModificados.add(items.get(i));
         }
      }
      return listaModificados;
   }

   @Override
   public void clear() {
      items.clear();
      removed.clear();
      inserted.clear();
      changed.clear();
      fireTableDataChanged();
   }

   @Override
   public boolean isEmpty() {
      return items.isEmpty();
   }

   private boolean inseridosNaoVazios() {
      for (int index : inserted) {
         final T item = getItemAt(index);
         if (item != null && !isEmptyKey(item)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean isChanged() {
      return !changed.isEmpty() || inseridosNaoVazios() || !removed.isEmpty();
   }

   @Override
   public void reset() {
      changed.clear();
      removed.clear();
      inserted.clear();
   }

   protected boolean isEmptyKey(T item) {
      return false;
   }

   @Override
   public boolean isChanged(int row, int col) {
      if (isInserted(row)) {
         return true;
      } else if (row < changed.size()) {
         final BitSet bitSet = changed.get(row);
         return bitSet != null && bitSet.get(col);
      }
      return false;
   }

   private void setChanged(int row, int col) {
      if (! isInserted(row)) {
         BitSet bitSet;
         while (row >= changed.size()) {
            changed.add(null);
         }
         bitSet = changed.get(row);
         if (bitSet == null) {
            bitSet = new BitSet();
            changed.set(row, bitSet);
         }
         bitSet.set(col);
      }
   }

   private void removeChange(int row) {
      if (row < changed.size()) {
         changed.remove(row);
      }
   }

   @Override
   public int getInsertedCount() {
      if (inserted.size() > 0 && items.size() > 0 && isEmptyKey(items.size() - 1))  {
         return inserted.size() - 1;
      } else {
         return inserted.size();
      }
   }

   @Override
   public int getRemovedCount() {
      return removed.size();
   }

   @Override
   public boolean isReadOnly() {
      return readOnly;
   }

   @Override
   public void setReadOnly(boolean readOnly) {
      this.readOnly = readOnly;
   }
}
