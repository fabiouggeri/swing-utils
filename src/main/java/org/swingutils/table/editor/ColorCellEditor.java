/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import org.swingutils.fields.ColorComboBox;

/**
 *
 * @author fabio_uggeri
 */
public class ColorCellEditor extends AbstractCustomCellEditor<ColorComboBox> {

   public ColorCellEditor() {
      super();
   }

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
      final ColorComboBox component = getComponent();
      component.setSelectedColor((Color)value);
      component.requestFocus();
      return component;
   }

   @Override
   public Object getEditorValue() {
      return getComponent().getSelectedColor();
   }

   @Override
   public ColorComboBox createComponent() {
      final ColorComboBox component;
      component = new ColorComboBox();
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
}
