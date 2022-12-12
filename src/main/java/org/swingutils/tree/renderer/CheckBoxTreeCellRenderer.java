/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.tree.renderer;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import org.swingutils.tree.CheckableTreeNode;
import org.swingutils.tree.CustomTreeNode;

/**
 *
 * @author fabio_uggeri
 */
public class CheckBoxTreeCellRenderer extends CustomTreeCellRenderer {

   private static final long serialVersionUID = -4867095553659292488L;

   private final JCheckBox checkableRenderer = new JCheckBox();
   private final JLabel labelRenderer = new JLabel();

   public CheckBoxTreeCellRenderer() {
      Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
      checkableRenderer.setFocusPainted((booleanValue != null) && (booleanValue));
      labelRenderer.setOpaque(true);
   }

   @Override
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf,
           int row, boolean hasFocus) {
      final boolean checkable = value instanceof CheckableTreeNode && ((CheckableTreeNode) value).isCheckable();
      final boolean customNode;
      Component component;
      if (checkable) {
         component = checkableRenderer;
         checkableRenderer.setText(((CheckableTreeNode)value).getLabel());
         checkableRenderer.setSelected(((CheckableTreeNode)value).isChecked());
         customNode = true;
      } else {
         component = labelRenderer;
         labelRenderer.setText(value.toString());
         customNode = value instanceof CustomTreeNode;
      }
      if (customNode) {
         CustomTreeNode node = (CustomTreeNode) value;

         if (selected) {
            if (node.getSelectedForeground()!= null) {
               component.setForeground(node.getSelectedForeground());
            } else {
               component.setForeground(getTextSelectionColor());
            }
            if (node.getSelectedBackground() != null) {
               component.setBackground(node.getSelectedBackground());
            } else {
               component.setBackground(getBackgroundSelectionColor());
            }
         } else {
            if (node.getUnselectedForeground() != null) {
               component.setForeground(node.getUnselectedForeground());
            } else {
               component.setForeground(getTextNonSelectionColor());
            }
            if (node.getUnselectedBackground() != null) {
               component.setBackground(node.getUnselectedBackground());
            } else {
               component.setBackground(getBackgroundNonSelectionColor());
            }
         }
         if (node.getFont() != null) {
            component.setFont(node.getFont().deriveFont(node.getTextStyle()));
         } else if (getFont() != null) {
            component.setFont(getFont().deriveFont(node.getTextStyle()));
         } else if (tree.getFont() != null) {
            component.setFont(tree.getFont().deriveFont(node.getTextStyle()));
         }
      }
      component.setEnabled(tree.isEnabled());
      return component;
   }
}
