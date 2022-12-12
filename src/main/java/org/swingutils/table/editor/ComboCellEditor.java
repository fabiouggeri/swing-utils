/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import org.swingutils.fields.CheckComboBox;

/**
 *
 * @author fabio_uggeri
 */
public class ComboCellEditor extends AbstractCustomCellEditor<JComboBox> {

   private static final long serialVersionUID = 5797204058279414436L;

   private final Object[] values;
   private final boolean checkBox;

   public ComboCellEditor(Object[] values, boolean checkBox) {
      super();
      this.values = values;
      this.checkBox = checkBox;
   }

   public ComboCellEditor(Object[] values) {
      this(values, false);
   }

   public ComboCellEditor(List<?> values) {
      this(values.toArray(), false);
   }

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
      final JComboBox component = getComponent();
      if (isCheckBox()) {
         if (value instanceof Collection) {
            ((CheckComboBox) component).setSelectedItems((Collection) value);
         } else {
            ((CheckComboBox) component).setSelectedItems(value);
         }
      } else {
         if (value == null) {
            component.setSelectedIndex(-1);
         } else {
            component.setSelectedItem(value);
         }
      }
      component.requestFocus();
      return component;
   }

   @Override
   public Object getEditorValue() {
      if (isCheckBox()) {
         return Arrays.asList(getComponent().getSelectedObjects());
      } else {
         return getComponent().getSelectedItem();
      }
   }

   @Override
   public JComboBox createComponent() {
      final JComboBox component;
      if (isCheckBox()) {
         component = new CheckComboBox(values);
      } else {
         component = new JComboBox(values);
      }
      if (values.length > 0) {
         component.setSelectedIndex(0);
      }
      component.setFocusable(true);
      component.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent e) {
            if (e.getComponent() instanceof JComboBox) {
               JComboBox comp = (JComboBox) e.getComponent();
               if (!comp.isPopupVisible()) {
                  comp.setPopupVisible(true);
               }
            }
         }
      });
      component.addKeyListener(new KeyAdapter() {

         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getComponent() instanceof JComboBox) {
               JComboBox comp = (JComboBox) e.getComponent();
               if (!comp.isPopupVisible()) {
                  comp.setPopupVisible(true);
               }
            }
         }
      });
      return component;
   }

   public boolean isCheckBox() {
      return checkBox;
   }

   @Override
   public void addKeyListener(KeyListener keyListener) {
      getComponent().addKeyListener(keyListener);
   }

   public void setValues(Object[] values) {
      getComponent().setModel(new DefaultComboBoxModel(values));
   }

   public void setValues(Collection values) {
      getComponent().setModel(new DefaultComboBoxModel(values.toArray()));
   }

   public boolean isPopupVisible() {
      return getComponent().isPopupVisible();
   }
}
