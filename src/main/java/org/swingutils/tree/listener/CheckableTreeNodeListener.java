/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.swingutils.tree.listener;

import org.swingutils.tree.CheckableTreeNode;

/**
 *
 * @author fabio_uggeri
 */
public interface CheckableTreeNodeListener extends CustomTreeNodeListener {

   public void checked(CheckableTreeNode node);

   public void unchecked(CheckableTreeNode node);
}
