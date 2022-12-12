/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.tree.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import org.swingutils.tree.CustomTreeNode;

/**
 *
 * @author fabio_uggeri
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

   private static final long serialVersionUID = -8200091597414263624L;

   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object node, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, node, selected, expanded, leaf, row, hasFocus);
      Icon icon;
      Font font;
      Color background;
      Color foreground;

      if (node instanceof CustomTreeNode) {
         if (expanded) {
            icon = ((CustomTreeNode)node).getExpandedIcon();
         } else {
            icon = ((CustomTreeNode)node).getIcon();
         }
         font = ((CustomTreeNode)node).getFont();
         if (selected) {
            background = ((CustomTreeNode)node).getSelectedBackground();
            foreground = ((CustomTreeNode)node).getSelectedForeground();
         } else {
            background = ((CustomTreeNode)node).getUnselectedBackground();
            foreground = ((CustomTreeNode)node).getUnselectedForeground();
         }
      } else {
         icon = null;
         font = null;
         background = null;
         foreground = null;
      }
      if (icon != null) {
         label.setIcon(icon);
      }
      if (font != null) {
         label.setFont(font);
      } else {
         label.setFont(tree.getFont());
      }
      if (background != null) {
         label.setBackground(background);
      }
      if (foreground != null) {
         label.setForeground(foreground);
      }
      return label;
   }
}
