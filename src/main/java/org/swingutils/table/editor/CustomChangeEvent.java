/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import javax.swing.event.ChangeEvent;

/**
 *
 * @author fabio_uggeri
 */
public class CustomChangeEvent extends ChangeEvent {
   
   private final int row;
   private final int col;
   
   public CustomChangeEvent(CustomCellEditor source, int row, int col) {
      super(source);
      this.row = row;
      this.col = col;
   }

   public int getRow() {
      return row;
   }

   public int getCol() {
      return col;
   }

   public CustomCellEditor getCellEditor() {
      return (CustomCellEditor)super.getSource();
   }
   
   
}
