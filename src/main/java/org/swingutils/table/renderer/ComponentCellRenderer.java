/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public class ComponentCellRenderer<T extends JComponent> extends CustomTableCellRenderer<T> {

   private final T component;

   public ComponentCellRenderer(T component) {
      this.component = component;
   }

   @Override
   protected T getComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return component;
   }
}
