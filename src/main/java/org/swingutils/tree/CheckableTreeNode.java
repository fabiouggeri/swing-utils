/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.tree;

import java.awt.Font;
import javax.swing.tree.TreeNode;
import org.swingutils.tree.listener.CheckableTreeNodeListener;
import org.swingutils.tree.listener.CustomTreeNodeListener;

/**
 *
 * @author fabio_uggeri
 */
public class CheckableTreeNode extends CustomTreeNode {

   private boolean checkable;
   private boolean checked = false;
   private boolean exclusiveCheck = false;
   private int defaultCheckedTextStyle = Font.PLAIN;
   private int defaultUncheckedTextStyle = Font.PLAIN;
   private boolean rootOfExclusivity = false;
   private boolean propagateCheckToSubnodes = true;
   private boolean propagateUncheckToSubnodes = true;
   private boolean propagateCheckFromSubnodes = true;
   private boolean propagateUncheckFromSubnodes = true;
   private boolean respectExclusivity = true;

   public CheckableTreeNode(final String label, final Object data, boolean checkable, boolean exclusive) {
      super(label, data);
      this.checkable = checkable;
      this.exclusiveCheck = exclusive;
   }

   public CheckableTreeNode(final String label, boolean checkable, boolean exclusive) {
      this(label, null, checkable, exclusive);
   }

   public CheckableTreeNode(final String label, final Object data) {
      this(label, data, true, false);
   }

   public CheckableTreeNode(final Object data) {
      this(data.toString(), data, true, false);
   }

   public CheckableTreeNode(final String label) {
      this(label, null, true, false);
   }

   public CheckableTreeNode getFirstCheckedLeaf() {
      return getFirstCheckedLeaf(this);
   }

   private CheckableTreeNode getFirstCheckedLeaf(CustomTreeNode node) {
      if (node.isLeaf()) {
         if (node instanceof CheckableTreeNode && ((CheckableTreeNode) node).isChecked()) {
            return (CheckableTreeNode) node;
         }
      } else {
         for (CustomTreeNode child : node.getChildren()) {
            CheckableTreeNode selNode = getFirstCheckedLeaf(child);
            if (selNode != null) {
               return selNode;
            }
         }
      }
      return null;
   }

   /**
    * @return the checked
    */
   public boolean isChecked() {
      return checked;
   }

   public boolean isRootOfExclusivity() {
      return rootOfExclusivity;
   }

   public void setRootOfExclusivity(boolean rootOfExclusivity) {
      this.rootOfExclusivity = rootOfExclusivity;
   }

   public boolean isPropagateCheckToSubnodes() {
      return propagateCheckToSubnodes;
   }

   public void setPropagateCheckToSubnodes(boolean propagateCheckToSubnodes) {
      this.propagateCheckToSubnodes = propagateCheckToSubnodes;
   }

   public boolean isPropagateUncheckToSubnodes() {
      return propagateUncheckToSubnodes;
   }

   public void setPropagateUncheckToSubnodes(boolean propagateUncheckToSubnodes) {
      this.propagateUncheckToSubnodes = propagateUncheckToSubnodes;
   }

   public boolean isPropagateCheckFromSubnodes() {
      return propagateCheckFromSubnodes;
   }

   public void setPropagateCheckFromSubnodes(boolean propagateCheckFromSubnodes) {
      this.propagateCheckFromSubnodes = propagateCheckFromSubnodes;
   }

   public boolean isPropagateUncheckFromSubnodes() {
      return propagateUncheckFromSubnodes;
   }

   public void setPropagateUncheckFromSubnodes(boolean propagateUncheckFromSubnodes) {
      this.propagateUncheckFromSubnodes = propagateUncheckFromSubnodes;
   }

   public boolean isRespectExclusivity() {
      return respectExclusivity;
   }

   public void setRespectExclusivity(boolean respectExclusivity) {
      this.respectExclusivity = respectExclusivity;
   }


   /**
    * @param check the checked to set
    */
   public void setChecked(boolean check) {
      if (checkable && check != checked) {
         final boolean oldValue = checked;
         if (check && respectExclusivity) {
            TreeNode root = findRootOfExclusivity();
            if (root instanceof CheckableTreeNode) {
               if (exclusiveCheck) {
                  ((CheckableTreeNode) root).uncheckAll();
               } else {
                  ((CheckableTreeNode) root).uncheckAllExclusive();
               }
            }
         }
         checked = check;
         if (checked) {
            setTextStyle(defaultCheckedTextStyle);
         } else {
            setTextStyle(defaultUncheckedTextStyle);
         }
         fireCheckableListeners(this, this, oldValue, checked);
         if (getChildCount() > 0 && ((check && propagateCheckToSubnodes) || (!check && propagateUncheckToSubnodes))) {
            for (CustomTreeNode child : getChildren()) {
               if (child instanceof CheckableTreeNode) {
                  ((CheckableTreeNode) child).setChecked(check);
               }
            }
         }
         updateParentCheck(checked);
      }
   }

   public void uncheckAll() {
      final boolean oldValue = checked;
      checked = false;
      fireCheckableListeners(this, this, oldValue, checked);
      if (getChildCount() > 0) {
         for (CustomTreeNode child : getChildren()) {
            if (child instanceof CheckableTreeNode) {
               ((CheckableTreeNode) child).uncheckAll();
            }
         }
      }
   }

   public void setSimpleChecked(final boolean checked) {
      this.checked = checked;
   }

   private void uncheckAllExclusive() {
      if (exclusiveCheck) {
         setChecked(false);
      }
      if (propagateCheckToSubnodes && getChildCount() > 0) {
         for (CustomTreeNode child : getChildren()) {
            if (child instanceof CheckableTreeNode) {
               ((CheckableTreeNode) child).uncheckAllExclusive();
            }
         }
      }
   }

   private TreeNode findRootOfExclusivity() {
      TreeNode root = this;
      while (root.getParent() != null) {
         root = root.getParent();
         if (root instanceof CheckableTreeNode && ((CheckableTreeNode) root).isRootOfExclusivity()) {
            return root;
         }
      }
      return root;
   }

   private void updateParentCheck(boolean check) {
      if (getParent() != null && getParent() instanceof CheckableTreeNode) {
         final CheckableTreeNode parent = (CheckableTreeNode) getParent();
         if ((check && parent.isPropagateCheckFromSubnodes()) || (!check && parent.isPropagateUncheckFromSubnodes())) {
            final boolean oldValue = parent.checked;
            boolean oneChecked = check;
            if (!oneChecked) {
               for (CustomTreeNode child : parent.getChildren()) {
                  if (((CheckableTreeNode) child).isChecked()) {
                     oneChecked = true;
                     break;
                  }
               }
            }
            parent.checked = oneChecked;
            fireCheckableListeners(parent, parent, oldValue, parent.checked);
            parent.updateParentCheck(oneChecked);
         }
      }
   }

   /**
    * @return the exclusiveCheck
    */
   public boolean isExclusiveCheck() {
      return exclusiveCheck;
   }

   /**
    * @param exclusiveCheck the exclusiveCheck to set
    */
   public void setExclusiveCheck(boolean exclusiveCheck) {
      this.exclusiveCheck = exclusiveCheck;
   }

   /**
    * @return the checkable
    */
   public boolean isCheckable() {
      return checkable;
   }

   /**
    * @param checkable the checkable to set
    */
   public void setCheckable(boolean checkable) {
      this.checkable = checkable;
   }

   private void fireCheckableListeners(CustomTreeNode nodeListen, CheckableTreeNode nodeChange, boolean oldValue, boolean newValue) {
      if (oldValue != newValue) {
         for (CustomTreeNodeListener listener : nodeListen.listeners()) {
            if (listener instanceof CheckableTreeNodeListener) {
               if (oldValue) {
                  if (!newValue) {
                     ((CheckableTreeNodeListener) listener).unchecked(nodeChange);
                  }
               } else {
                  if (newValue) {
                     ((CheckableTreeNodeListener) listener).checked(nodeChange);
                  }
               }
            }
         }
         if (nodeListen.getParent() != null && nodeListen.getParent() instanceof CustomTreeNode) {
            fireCheckableListeners((CustomTreeNode) nodeListen.getParent(), nodeChange, oldValue, newValue);
         }
      }
   }

   public int getDefaultCheckedTextStyle() {
      return defaultCheckedTextStyle;
   }

   public int getDefaultUncheckedTextStyle() {
      return defaultUncheckedTextStyle;
   }

   public void setDefaultCheckedTextStyle(int defaultCheckedTextStyle) {
      this.defaultCheckedTextStyle = defaultCheckedTextStyle;
   }

   public void setDefaultUncheckedTextStyle(int defaultUncheckedTextStyle) {
      this.defaultUncheckedTextStyle = defaultUncheckedTextStyle;
   }
}
