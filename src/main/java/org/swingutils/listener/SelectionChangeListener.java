/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.listener;

import java.util.EventListener;

/**
 *
 * @author fabio_uggeri
 */
public interface SelectionChangeListener extends EventListener {
   public void selectionChanged(Object item, boolean selected);
}
