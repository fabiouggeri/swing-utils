/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import javax.swing.event.CellEditorListener;

/**
 *
 * @author fabio_uggeri
 */
public interface CustomCellEditorListener extends CellEditorListener {

   public boolean isValidValue(CustomChangeEvent evt);
   
   public boolean editStarting(CustomChangeEvent evt);

   public boolean editStopping(CustomChangeEvent evt);
   
}
