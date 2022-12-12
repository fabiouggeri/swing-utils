/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.model;

import java.util.Collection;
import java.util.List;
import javax.swing.table.TableModel;
import org.swingutils.table.CellDataChecker;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public interface EditableTableModel<T> extends  TableModel, CellDataChecker {

   public T getItemAt(int row);
   public boolean isChanged();
   public boolean isInserted(final int row);
   public boolean isChanged(final int row);
   public boolean isChangedOrInserted(final int row);
   public T appendItem();
   public T removeItem(final int row);
   public void reset();
   public void clear();
   public void add(T item);
   public void addAll(List<T> items);
   public boolean isEmptyKey(final int row);
   public boolean isKey(final int col);
   public boolean isEmpty();
   public Collection<T> getItems();
   public Collection<T> getInserted();
   public Collection<T> getChanged();
   public Collection<T> getRemoved();
   public int getInsertedCount();
   public int getRemovedCount();
   public boolean isReadOnly();
   public void setReadOnly(boolean readOnly);
}
