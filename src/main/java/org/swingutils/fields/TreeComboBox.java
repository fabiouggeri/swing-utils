/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import org.swingutils.tree.renderer.CheckBoxTreeCellRenderer;

public class TreeComboBox extends JComboBox<TreeNode> {

   private static final long serialVersionUID = 7719462452591493064L;

   private JTree tree;

   private TreeComboBox(JTree tree) {
      super();
      final TreeNode root = (TreeNode) tree.getModel().getRoot();
      final DefaultComboBoxModel<TreeNode> comboModel = new DefaultComboBoxModel<>();
      this.tree = tree;
      Collections.list((Enumeration<?>) preorderEnumeration(root)).stream()
              .filter(TreeNode.class::isInstance)
              .map(TreeNode.class::cast)
              .filter(n -> !isRoot(n))
              .forEach(comboModel::addElement);
      setModel(comboModel);
   }

   public TreeComboBox(TreeModel model) {
      this(new JTree(model));
      tree.setShowsRootHandles(true);
      tree.setCellRenderer(new CheckBoxTreeCellRenderer());
   }

   public TreeComboBox(TreeNode root) {
      this(new JTree());
      tree.setShowsRootHandles(true);
      tree.setCellRenderer(new CheckBoxTreeCellRenderer());
   }

   @Override
   public void updateUI() {
      final ListCellRenderer<? super TreeNode> listRenderer;

      super.updateUI();
      listRenderer = getRenderer();
      setRenderer((list, value, index, isSelected, cellHasFocus) -> {
         Component c = listRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (value == null) {
            return c;
         }
         if (index < 0) {
            String txt = Arrays.stream(getPath(value))
                    .filter(TreeNode.class::isInstance)
                    .map(TreeNode.class::cast)
                    .filter(n -> !isRoot(n))
                    .map(Objects::toString)
                    .collect(Collectors.joining(" / "));
            ((JLabel) c).setText(txt);
            return c;
         } else {
            final TreeCellRenderer cellRenderer = tree.getCellRenderer();
            final boolean leaf = value.isLeaf();
            final JComponent cellComponent = (JComponent) cellRenderer.getTreeCellRendererComponent(
                    tree, value, isSelected, true, leaf, index, false);
            final int indent = Math.max(0, getLevel(value) - 1) * 16;
            cellComponent.setBorder(BorderFactory.createEmptyBorder(1, indent + 1, 1, 1));
            return cellComponent;
         }
      });
   }

   public int getLevel(TreeNode node) {
      TreeNode parent;
      int levels = 0;

      parent = node.getParent();
      while (parent != null) {
         ++levels;
         parent = parent.getParent();
      }
      return levels;
   }

   public boolean isRoot(TreeNode node) {
      return node.getParent() == null;
   }

   public TreeNode[] getPath(TreeNode node) {
      return getPathToRoot(node, 0);
   }

   protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
      TreeNode[] retNodes;

      /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
      if (aNode == null) {
         if (depth == 0) {
            return null;
         } else {
            retNodes = new TreeNode[depth];
         }
      } else {
         depth++;
         retNodes = getPathToRoot(aNode.getParent(), depth);
         retNodes[retNodes.length - depth] = aNode;
      }
      return retNodes;
   }

   private Enumeration<TreeNode> preorderEnumeration(TreeNode node) {
      return new PreorderEnumeration(node);
   }

   private final class PreorderEnumeration implements Enumeration<TreeNode> {

      private final Stack<Enumeration<? extends TreeNode>> stack = new Stack<>();

      public PreorderEnumeration(TreeNode rootNode) {
         super();
         Vector<TreeNode> v = new Vector<>(1);
         v.addElement(rootNode);     // PENDING: don't really need a vector
         stack.push(v.elements());
      }

      @Override
      public boolean hasMoreElements() {
         return (!stack.empty() && stack.peek().hasMoreElements());
      }

      @Override
      public TreeNode nextElement() {
         Enumeration<? extends TreeNode> enumer = stack.peek();
         TreeNode node = enumer.nextElement();
         Enumeration<? extends TreeNode> children = node.children();

         if (!enumer.hasMoreElements()) {
            stack.pop();
         }
         if (children != null && children.hasMoreElements()) {
            stack.push(children);
         }
         return node;
      }

   }  // End of class PreorderEnumeration

}
