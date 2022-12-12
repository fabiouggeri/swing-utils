/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.event.KeyListener;
import java.util.Comparator;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public interface CustomCellEditor<T> extends TableCellEditor {

   void addKeyListener(final KeyListener keyListener);

   boolean isChanged();

   boolean isEditable();

   void setEditable(boolean editable);
   
   void setValueComparator(Comparator comparator);

   public T getComponent();
   
}
