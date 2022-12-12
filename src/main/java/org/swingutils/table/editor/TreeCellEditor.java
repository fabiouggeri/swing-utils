/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JTable;
import org.swingutils.fields.TreeField;

/**
 *
 * @author fabio_uggeri
 */
public class TreeCellEditor extends AbstractCustomCellEditor<TreeField> {

   private static final long serialVersionUID = -1428222117603729946L;

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
      final TreeField component = getComponent();
      if (value instanceof Collection) {
         component.setCheckedNodesPath((List)value);
      } else {
         component.setCheckedNodesPath(Collections.emptyList());
      }
      component.setFocusable(true);
      component.requestFocus();
      return component;
   }

   @Override
   public Object getEditorValue() {
      return getComponent().getCheckedNodesPath();
   }

   @Override
   public boolean stopCellEditing() {
      if (super.stopCellEditing()) {
         getComponent().hidePopup();
         return true;
      }
      return false;
   }

   @Override
   protected void startCellEditing() {
      getComponent().showPopup();
   }

   @Override
   public TreeField createComponent() {
      return new TreeField();
   }

   @Override
   public void addKeyListener(KeyListener keyListener) {
      getComponent().addKeyListener(keyListener);
   }
}
