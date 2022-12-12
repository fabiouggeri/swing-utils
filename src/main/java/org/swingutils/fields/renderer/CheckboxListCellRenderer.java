/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields.renderer;

import java.awt.Component;
import java.util.Objects;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

/**
 *
 * @author fabio_uggeri
 * @param <E>
 */
public class CheckboxListCellRenderer<E> implements ListCellRenderer<E> {

   public interface SelectionChecker<E> {
      public boolean isSelected(final E object);
   }

   private final JLabel label = new JLabel(" ");
   private final JCheckBox check = new JCheckBox(" ");
   private SelectionChecker<E> selectionChecker;

   public CheckboxListCellRenderer(final SelectionChecker<E> selectionChecker) {
      this.selectionChecker = selectionChecker;
   }

   public CheckboxListCellRenderer() {
      this(null);
   }
   
   protected boolean isSelected(E value) {
      if (selectionChecker != null) {
         return selectionChecker.isSelected(value);
      }
      return false;
   }
   
   @Override
   public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
      if (index < 0) {
         String txt = getCheckedItemString(list.getModel());
         label.setText(txt.isEmpty() ? " " : txt);
         return label;
      } else {
         check.setText(Objects.toString(value, ""));
         check.setSelected(isSelected(value));
         if (isSelected) {
            check.setBackground(list.getSelectionBackground());
            check.setForeground(list.getSelectionForeground());
         } else {
            check.setBackground(list.getBackground());
            check.setForeground(list.getForeground());
         }
         return check;
      }
   }

   private String getCheckedItemString(ListModel<? extends E> model) {
      final StringBuilder sb = new StringBuilder();
      for (int i = 0; i < model.getSize(); i++) {
         final E item = model.getElementAt(i);
         if (isSelected(item)) {
            if (sb.length() > 0) {
               sb.append(", ");
            }
            sb.append(item.toString());
         }
      }
      return sb.toString();
   }
}
