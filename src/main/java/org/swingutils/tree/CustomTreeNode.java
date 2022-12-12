/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import javax.swing.Icon;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.swingutils.tree.listener.CustomTreeNodeListener;

/**
 *
 * @author fabio_uggeri
 */
public class CustomTreeNode implements MutableTreeNode {

   private Font font = null;

   private Color selectedBackground = null;
   private Color selectedForeground = null;

   private Color unselectedBackground = null;
   private Color unselectedForeground = null;

   private int textStyle = Font.PLAIN;

   private Icon icon = null;
   private Icon expandedIcon = null;

   private CustomTreeNode parent = null;
   private String label;
   private Object userObject;
   private List<CustomTreeNode> children = null;
   private List<CustomTreeNodeListener> listeners = null;

   public CustomTreeNode(final String label, final Object data) {
      this.label = label;
      this.userObject = data;
   }

   public CustomTreeNode(final Object data) {
      this(data.toString(), data);
   }

   public CustomTreeNode(final String label) {
      this(label, null);
   }

   public Font getFont() {
      return font;
   }

   public void setFont(Font font, boolean setParent) {
      this.font = font;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setFont(font, setParent);
      }
   }

   public void setFont(Font font) {
      setFont(font, false);
   }

   /**
    * @return the selectedBackground
    */
   public Color getSelectedBackground() {
      return selectedBackground;
   }

   /**
    * @param color the selectedBackground to set
    * @param setParent
    */
   public void setSelectedBackground(Color color, boolean setParent) {
      this.selectedBackground = color;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setSelectedBackground(color, setParent);
      }
   }

   public void setSelectedBackground(Color color) {
      setSelectedBackground(color, false);
   }

   /**
    * @return the selectedForeground
    */
   public Color getSelectedForeground() {
      return selectedForeground;
   }

   /**
    * @param color the selectedForeground to set
    * @param setParent
    */
   public void setSelectedForeground(Color color, boolean setParent) {
      this.selectedForeground = color;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setSelectedForeground(color, setParent);
      }
   }

   public void setSelectedForeground(Color color) {
      setSelectedForeground(color, false);
   }

   /**
    * @return the unselectedBackground
    */
   public Color getUnselectedBackground() {
      return unselectedBackground;
   }

   /**
    * @param color the unselectedBackground to set
    * @param setParent
    */
   public void setUnselectedBackground(Color color, boolean setParent) {
      this.unselectedBackground = color;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setUnselectedBackground(color, setParent);
      }
   }

   public void setUnselectedBackground(Color color) {
      setUnselectedBackground(color, false);
   }

   /**
    * @return the unselectedForeground
    */
   public Color getUnselectedForeground() {
      return unselectedForeground;
   }

   /**
    * @param color the unselectedForeground to set
    * @param setParent
    */
   public void setUnselectedForeground(Color color, boolean setParent) {
      this.unselectedForeground = color;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setUnselectedForeground(color, setParent);
      }
   }

   public void setUnselectedForeground(Color color) {
      setUnselectedForeground(color, false);
   }

   public Icon getIcon() {
      return icon;
   }

   public void setIcon(Icon icon, boolean setParent) {
      this.icon = icon;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setIcon(icon, setParent);
      }
   }

   public void setIcon(Icon icon) {
      setIcon(icon, false);
   }

   public Icon getExpandedIcon() {
      return expandedIcon != null ? expandedIcon : icon;
   }
   public void setExpandedIcon(Icon icon, boolean setParent) {
      this.expandedIcon = icon;
      if (setParent && parent instanceof CustomTreeNode) {
         ((CustomTreeNode)parent).setExpandedIcon(icon, setParent);
      }
   }

   public void setExpandedIcon(Icon icon) {
      setExpandedIcon(icon, false);
   }

   public CustomTreeNode findLabel(final String findLabel) {
      if (label.equalsIgnoreCase(findLabel)) {
         return this;
      } else if (children != null) {
         for (CustomTreeNode child : children) {
            final CustomTreeNode node = child.findLabel(findLabel);
            if (node != null) {
               return node;
            }
         }
      }
      return null;
   }

   public CustomTreeNode findUserObject(Object userObj) {
      if (userObj.equals(userObject)) {
         return this;
      } else if (children != null) {
         for (CustomTreeNode child : children) {
            final CustomTreeNode node = child.findUserObject(userObj);
            if (node != null) {
               return node;
            }
         }
      }
      return null;
   }

   @Override
   public TreeNode getChildAt(int childIndex) {
      if (children != null && children.size() > 0) {
         return children.get(childIndex);
      }
      return null;
   }

   @Override
   public int getChildCount() {
      if (children != null) {
         return children.size();
      }
      return 0;
   }

   @Override
   public TreeNode getParent() {
      return parent;
   }

   @Override
   public int getIndex(TreeNode node) {
      if (children != null) {
         if (node instanceof CustomTreeNode) {
            for (int i = 0; i < children.size(); i++) {
               if (children.get(i).getLabel().equalsIgnoreCase(((CustomTreeNode) node).getLabel())) {
                  return i;
               }
            }
         } else {
            for (int i = 0; i < children.size(); i++) {
               if (children.get(i).equals(node)) {
                  return i;
               }
            }
         }
      }
      return -1;
   }

   @Override
   public boolean getAllowsChildren() {
      return children != null && !children.isEmpty();
   }

   @Override
   public boolean isLeaf() {
      return children == null || children.isEmpty();
   }

   @Override
   public Enumeration<CustomTreeNode> children() {
      if (children != null) {
         return Collections.enumeration(children);
      }
      return null;
   }

   private List<CustomTreeNode> getChildrenList() {
      if (children == null) {
         children = new ArrayList<>();
      }
      return children;
   }

   public void addAllNodes(Collection<CustomTreeNode> nodes) {
      for (CustomTreeNode node : nodes) {
         addNode(node);
      }
   }

   public void addNode(CustomTreeNode node) {
      node.removeFromParent();
      node.parent = this;
      getChildrenList().add(node);
   }

   public CustomTreeNode addNode(final String label, final Object data) {
      CustomTreeNode node = new CustomTreeNode(label, data);
      node.parent = this;
      getChildrenList().add(node);
      return node;
   }

   public CustomTreeNode addNode(final Object data) {
      CustomTreeNode node = new CustomTreeNode(data);
      node.parent = this;
      getChildrenList().add(node);
      return node;
   }

   public boolean removeNode(CustomTreeNode node) {
      if (children != null) {
         final int index = getIndex(node);
         if (index >= 0) {
            return children.remove(index) != null;
         }
      }
      return false;
   }

   public void removeAllNodes() {
      if (children != null) {
         children.clear();
         children = null;
      }
   }

   /**
    * @return the data
    */
   public Object getUserObject() {
      return userObject;
   }

   public List<CustomTreeNode> getChildren() {
      return getChildrenList();
   }

   /**
    * @return the label
    */
   public String getLabel() {
      return label;
   }

   /**
    * @param label the label to set
    */
   public void setLabel(String label) {
      this.label = label;
   }

   @Override
   public String toString() {
      return label;
   }

   private void addToPath(List<CustomTreeNode> treeNodes) {
      if (parent != null) {
         parent.addToPath(treeNodes);
      }
      treeNodes.add(this);
   }

   public TreePath getTreePath() {
      TreePath treePath;
      List<CustomTreeNode> treeNodes = new ArrayList<>();
      addToPath(treeNodes);
      treePath = new TreePath(treeNodes.toArray());
      return treePath;
   }

   @Override
   public void insert(MutableTreeNode child, int index) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void remove(int index) {
      if (children != null) {
         children.remove(index);
      }
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
   }

   @Override
   public void remove(MutableTreeNode node) {
      remove(getIndex(node));
   }

   @Override
   public void setUserObject(Object data) {
      this.userObject = data;
   }

   @Override
   public void removeFromParent() {
      if (parent != null) {
         parent.removeNode(this);
      }
   }

   @Override
   public void setParent(MutableTreeNode newParent) {
      removeFromParent();
      if (newParent != null) {
         ((CustomTreeNode) newParent).addNode(this);
      }
   }

   public void addNodeListener(CustomTreeNodeListener listener) {
      if (listeners == null) {
         listeners = new ArrayList<>();
         listeners.add(listener);
      } else if (!listeners.contains(listener)) {
         listeners.add(listener);
      }
   }

   public boolean removeNodeListener(CustomTreeNodeListener listener) {
      if (listeners != null) {
         return listeners.remove(listener);
      }
      return false;
   }

   public void removeAllNodeListeners() {
      listeners = null;
   }

   protected List<CustomTreeNodeListener> listeners() {
      if (listeners != null) {
         return listeners;
      }
      return Collections.emptyList();
   }

   public int getTextStyle() {
      return textStyle;
   }

   public void setTextStyle(int textStyle) {
      this.textStyle = textStyle;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj == this) {
         return true;
      }
      if (obj instanceof CustomTreeNode) {
         return label.equals(((CustomTreeNode)obj).label);
      }
      return label.equals(obj.toString());
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 31 * hash + Objects.hashCode(this.label);
      return hash;
   }


}
