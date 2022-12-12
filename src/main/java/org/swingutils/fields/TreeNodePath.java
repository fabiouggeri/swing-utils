/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import java.util.Objects;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public class TreeNodePath<T> {

   private final TreeNodePath<T> parent; 
   private final T value;

   private TreeNodePath(TreeNodePath<T> parent, T value) {
      this.parent = parent;
      this.value = value;
   }
   
   public static <T> TreeNodePath from(T... values) {
      TreeNodePath<T> parent = null;
      for (T s : values) {
         parent = new TreeNodePath<>(parent, s);
      }
      return parent;
   }

   public TreeNodePath<T> getParent() {
      return parent;
   }

   public TreeNodePath<T> getRoot() {
      if (parent != null) {
         return parent.getRoot();
      }
      return this;
   }

   public int length() {
      if (parent != null) {
         return parent.length() + 1;
      }
      return 1;
   }

   public T getValue() {
      return value;
   }
   
   public TreeNodePath<T> path(T value) {
      return new TreeNodePath<>(this, value);
   }
   
   
   private void appendValue(final T[] values, final int index) {
      if (index >= 0) {
         values[index] = this.value;
         if (parent != null) {
            parent.appendValue(values, index - 1);
         }
      }
   }
   
   public T[] values() {
      final int pathLen = length();
      final T[] values = (T[])new Object[pathLen];
      appendValue(values, pathLen - 1);
      return values;
   }
   

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }
      if (obj instanceof TreeNodePath) {
         if ((value == null && ((TreeNodePath)obj).value == null) || value.equals(((TreeNodePath)obj).value)) {
            if (parent != null) {
               return parent.equals(((TreeNodePath)obj).parent);
            } else {
               return ((TreeNodePath)obj).parent == null;
            }
         }
      }
      return false;
   }

   @Override
   public int hashCode() {
      int hash = 7;
      hash = 89 * hash + Objects.hashCode(this.parent);
      hash = 89 * hash + Objects.hashCode(this.value);
      return hash;
   }
   
   private void appendValue(final StringBuilder text) {
      if (parent != null) {
         parent.appendValue(text);
         text.append('/');
      }
      text.append(value);
   }
   
   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      appendValue(sb);
      return sb.toString();
   }

   public TreePath toTreePath() {
      final T values[] = values();
      final DefaultMutableTreeNode nodes[] = new DefaultMutableTreeNode[values.length];
      for (int i = 0; i < values.length; i++) {
         nodes[i] = new DefaultMutableTreeNode(values[i]);
      }
      return new TreePath(nodes);
   }
}
