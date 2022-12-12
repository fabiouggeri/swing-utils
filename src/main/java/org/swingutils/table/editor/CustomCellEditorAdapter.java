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
public class CustomCellEditorAdapter implements CustomCellEditorListener {

   @Override
   public boolean isValidValue(CustomChangeEvent evt) {
      return true;
   }

   @Override
   public boolean editStarting(CustomChangeEvent evt) {
      return true;
   }

   @Override
   public boolean editStopping(CustomChangeEvent evt) {
      return true;
   }

   @Override
   public void editingStopped(ChangeEvent e) {
   }

   @Override
   public void editingCanceled(ChangeEvent e) {
   }
   
}
