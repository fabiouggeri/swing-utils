/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Component;
import java.awt.event.KeyListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class BooleanCellEditor extends AbstractCustomCellEditor<JCheckBox> {

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      if (value != null) {
         if (value instanceof Boolean) {
            getComponent().setSelected((Boolean) value);
         } else {
            getComponent().setSelected(Boolean.parseBoolean(value.toString()));
         }
      } else {
         getComponent().setSelected(false);
      }
      if (isSelected) {
         getComponent().setBackground(table.getSelectionBackground());
         getComponent().setForeground(table.getSelectionForeground());
      }
      return getComponent();
   }

   @Override
   public Object getEditorValue() {
      return getComponent().isSelected();
   }

   @Override
   public JCheckBox createComponent() {
      JCheckBox checkBox = new JCheckBox();
      checkBox.setHorizontalAlignment(JLabel.CENTER);
      checkBox.setOpaque(true);
      return checkBox;
   }

   @Override
   protected boolean isReturnComponentNotSelected() {
      return true;
   }

   @Override
   public void addKeyListener(KeyListener keyListener) {
      getComponent().addKeyListener(keyListener);
   }
}
