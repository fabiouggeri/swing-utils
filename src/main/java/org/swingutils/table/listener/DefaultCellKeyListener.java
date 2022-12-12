/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTable;

/**
 *
 * @author fabio_uggeri
 */
public class DefaultCellKeyListener implements KeyListener {

   private final JTable table;
   private final int keys[];

   public DefaultCellKeyListener(final JTable table, int... keys) {
      this.table = table;
      this.keys = keys;
   }

   public DefaultCellKeyListener(final JTable table) {
      this(table, KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_TAB);
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (!e.isConsumed()) {
         for (int key : keys) {
            if (e.getKeyCode() == key) {
               e.consume();
               table.dispatchEvent(new KeyEvent(table, e.getID(), e.getWhen(), e.getModifiersEx(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation()));
               return;
            }
         }
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }
}
