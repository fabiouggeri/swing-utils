/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import org.swingutils.fields.renderer.CheckboxListCellRenderer;
import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author fabio_uggeri
 * @param <E>
 */
public class CheckComboBox<E> extends JComboBox<E> {

   private static final long serialVersionUID = 445060885921875612L;

   private boolean keepOpen;
   private transient ActionListener listener;
   private final Map<Object, Boolean> selected = new HashMap<>();

   public CheckComboBox() {
      super();
   }

   public CheckComboBox(ComboBoxModel<E> model) {
      super(model);
   }

   public CheckComboBox(E[] items) {
      super(items);
   }

   @Override
   public void updateUI() {
      setRenderer(null);
      removeActionListener(listener);
      super.updateUI();
      listener = e -> {
         if ((e.getModifiers() & AWTEvent.MOUSE_EVENT_MASK) != 0) {
            updateItem(getSelectedIndex());
            keepOpen = true;
         }
      };
      setRenderer(new CheckboxListCellRenderer<>((object) -> isItemSelected(object)));
      addActionListener(listener);
      getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "checkbox-select");
      getActionMap().put("checkbox-select", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Accessible a = getAccessibleContext().getAccessibleChild(0);
            if (a instanceof ComboPopup) {
               updateItem(((ComboPopup) a).getList().getSelectedIndex());
            }
         }
      });
   }

   protected void updateItem(int index) {
      if (isPopupVisible()) {
         E item = getItemAt(index);
         setItemSelected(item, !isItemSelected(item));
         setSelectedIndex(-1);
         setSelectedItem(item);
      }
   }

   @Override
   public void setPopupVisible(boolean v) {
      if (keepOpen) {
         keepOpen = false;
      } else {
         super.setPopupVisible(v);
      }
   }

   @Override
   public Object[] getSelectedObjects() {
      final List<Object> items = new ArrayList<>();
      for (Entry<Object, Boolean> e : selected.entrySet()) {
         if (e.getValue()) {
            items.add(e.getKey());
         }
      }
      return items.toArray(new Object[items.size()]);
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

   private boolean isItemSelected(Object item) {
      return selected.getOrDefault(item, false);
   }

   private void setItemSelected(Object item, boolean value) {
      selected.put(item, value);
   }
}
