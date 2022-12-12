/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import org.swingutils.fields.renderer.CheckboxListCellRenderer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JList;
import javax.swing.ListModel;
import org.swingutils.listener.SelectionChangeListener;

/**
 *
 * @author fabio_uggeri
 * @param <E>
 */
public class CheckList<E> extends JList<E> implements MouseListener, KeyListener {

   private final Map<E, Boolean> selected = new HashMap<>();

   private int selectedCount = 0;

   public CheckList() {
      super();
      configure();
   }

   public CheckList(ListModel<E> model) {
      super(model);
   }

   public CheckList(E[] items) {
      super(items);
   }

   private void configure() {
      setCellRenderer(new CheckboxListCellRenderer<>((object) -> isItemSelected(object)));
      addMouseListener(this);
      addKeyListener(this);
   }

   @Override
   public Object[] getSelectedValues() {
      final List<E> items = new ArrayList<>();
      for (Map.Entry<E, Boolean> e : selected.entrySet()) {
         if (e.getValue()) {
            items.add(e.getKey());
         }
      }
      return items.toArray(Object[]::new);
   }

   @Override
   public List<E> getSelectedValuesList() {
      final List<E> items = new ArrayList<>();
      for (Map.Entry<E, Boolean> e : selected.entrySet()) {
         if (e.getValue()) {
            items.add(e.getKey());
         }
      }
      return items;
   }

   public void setSelectedItems(Collection<E> items) {
      selected.clear();
      if (items != null) {
         for (E item : items) {
            setItemSelected(item, true);
         }
      }
   }

   public void setSelectedItems(E... items) {
      selected.clear();
      for (E item : items) {
         setItemSelected(item, true);
      }
   }

   public void selectAll() {
      for (int i = 0; i < getModel().getSize(); i++) {
         final E item = getModel().getElementAt(i);
         setItemSelected(item, true);
      }
      updateUI();
   }

   public void unselectAll() {
      for (int i = 0; i < getModel().getSize(); i++) {
         final E item = getModel().getElementAt(i);
         setItemSelected(item, false);
      }
      updateUI();
   }

   public void invertSelections() {
      for (int i = 0; i < getModel().getSize(); i++) {
         final E item = getModel().getElementAt(i);
         setItemSelected(item, !isItemSelected(item));
      }
      updateUI();
   }

   public int getSelectedCount() {
      return selectedCount;
   }

   private boolean isItemSelected(E item) {
      return selected.getOrDefault(item, false);
   }

   private void setItemSelected(E item, boolean value) {
      final Boolean oldValue = selected.put(item, value);
      if (value) {
         if (oldValue == null || ! oldValue) {
            ++selectedCount;
         }
      } else {
         if (oldValue != null && oldValue) {
            --selectedCount;
         }
      }
      if ((oldValue == null && value) || oldValue != value) {
         fireItemSelectionChanged(item, value);
      }
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      if (!e.isConsumed() && e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
         final int index = locationToIndex(e.getPoint());
         if (index >= 0) {
            final E item = getModel().getElementAt(index);
            setItemSelected(item, !isItemSelected(item));
            updateUI();
            e.consume();
         }
      }
   }

   @Override
   public void mousePressed(MouseEvent e) {
   }

   @Override
   public void mouseReleased(MouseEvent e) {
   }

   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (!e.isConsumed() && e.getKeyCode() == KeyEvent.VK_SPACE) {
         final int index = getSelectedIndex();
         if (index >= 0) {
            final E item = getModel().getElementAt(index);
            setItemSelected(item, !isItemSelected(item));
            setSelectedIndex(index);
            updateUI();
            e.consume();
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }

   public void addSelectionChangeListener(SelectionChangeListener listener) {
      listenerList.add(SelectionChangeListener.class, listener);
   }

   public void removeSelectionChangeListener(SelectionChangeListener listener) {
      listenerList.remove(SelectionChangeListener.class, listener);
   }

   private void fireItemSelectionChanged(E item, boolean selected) {
      final SelectionChangeListener listeners[] = getListeners(SelectionChangeListener.class);
      for (SelectionChangeListener l : listeners) {
         l.selectionChanged(item, selected);
      }
   }
}
