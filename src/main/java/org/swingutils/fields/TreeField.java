/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.swingutils.StringUtils;
import org.swingutils.tree.CheckableTreeNode;
import org.swingutils.tree.renderer.CheckBoxTreeCellRenderer;
import org.swingutils.window.PopupWindow;

/**
 *
 * @author fabio_uggeri
 */
public class TreeField extends JTextField {

   private static final long serialVersionUID = 4415401522116221664L;

   private static final int POPUP_HEIGHT = 240;

   private JTree tree;
   private Icon icon = null;
   private Insets dummyInsets = null;
   private TreePopup popup = null;
   private boolean confirmationOption = true;
   private boolean clearOption = true;
   private final TextFieldListener textListener = new TextFieldListener();
   private final TreeListener treeListener = new TreeListener();
   private boolean popupVisible = false;

   private TreeField(JTree tree) {
      super();
      configure(tree);
   }

   private void configure(JTree tree) {
      this.tree = tree;
      this.tree.setShowsRootHandles(true);
      this.tree.setCellRenderer(new CheckBoxTreeCellRenderer());
      this.tree.setFocusTraversalKeysEnabled(false);
      super.setEditable(false);
      super.addMouseListener(textListener);
      super.addKeyListener(textListener);
      super.addFocusListener(textListener);
      this.tree.addMouseListener(treeListener);
      this.tree.addKeyListener(treeListener);
      this.popup = new TreePopup(this, true);
      super.setDoubleBuffered(true);
   }

   public TreeField(TreeModel model) {
      super();
      configure(new JTree(model));
   }

   public TreeField(TreeNode root) {
      super();
      configure(new JTree(root));
   }

   public TreeField() {
      super();
      configure(new JTree());
   }

   @Override
   public final void setEditable(boolean b) {
      super.setEditable(b);
   }

   public void setIcon(Icon newIcon) {
      this.icon = newIcon;
   }

   public JTree getTree() {
      return tree;
   }

   public boolean isClearOption() {
      return clearOption;
   }

   public void setClearOption(boolean clearOption) {
      this.clearOption = clearOption;
   }

   public boolean isConfirmationOption() {
      return confirmationOption;
   }

   public void setConfirmationOption(boolean confirmationOption) {
      this.confirmationOption = confirmationOption;
   }

   public Collection<TreeNode> getCheckedNodes() {
      final TreeNode root = (TreeNode) tree.getModel().getRoot();
      final List<TreeNode> lista = new ArrayList<>();
      if (root != null) {
         if (tree.isRootVisible() && (root instanceof CheckableTreeNode) && ((CheckableTreeNode) root).isChecked()) {
            lista.add(root);
         }
         for (int i = 0; i < root.getChildCount(); i++) {
            appendCheckedNode(lista, root.getChildAt(i));
         }
      }
      return lista;
   }

   private void appendCheckedNode(List<TreeNode> lista, TreeNode node) {
      if (node instanceof CheckableTreeNode && ((CheckableTreeNode) node).isChecked()) {
         lista.add(node);
      }
      for (int i = 0; i < node.getChildCount(); i++) {
         appendCheckedNode(lista, node.getChildAt(i));
      }
   }

   public List<TreeNodePath> getCheckedNodesPath() {
      final TreeNode root = (TreeNode) tree.getModel().getRoot();
      final List<TreeNodePath> lista = new ArrayList<>();
      if (root != null) {
         boolean append = false;
         final TreeNodePath parent = tree.isRootVisible() ? TreeNodePath.from(root.toString()) : null;
         for (int i = 0; i < root.getChildCount(); i++) {
            if (appendCheckedNodePath(lista, parent, root.getChildAt(i))) {
               append = true;
            }
         }
         if (parent != null && !append && (root instanceof CheckableTreeNode) && ((CheckableTreeNode) root).isChecked()) {
            lista.add(parent);
         }
      }
      return lista;
   }

   private boolean appendCheckedNodePath(final List<TreeNodePath> lista, final TreeNodePath parent, final TreeNode node) {
      final TreeNodePath path = parent != null ? parent.path(node.toString()) : TreeNodePath.from(node.toString());
      boolean append = false;
      for (int i = 0; i < node.getChildCount(); i++) {
         if (appendCheckedNodePath(lista, path, node.getChildAt(i))) {
            append = true;
         }
      }
      if (!append
              && (node instanceof CheckableTreeNode)
              && ((CheckableTreeNode) node).isChecked()) {
         lista.add(path);
         append = true;
      }
      return append;
   }

   public void setCheckedNodesPath(List<TreeNodePath> checkedNodes) {
      uncheckAll((TreeNode)tree.getModel().getRoot());
      for (TreeNodePath path : checkedNodes) {
         if (path.getValue() instanceof CheckableTreeNode) {
            ((CheckableTreeNode) path.getValue()).setCheckable(true);
         } else {
            final TreeNode root = (TreeNode) tree.getModel().getRoot();
            final TreeNode node = findNode(root, tree.isRootVisible() ? TreeNodePath.from(root.toString()) : null, path);
            if (node instanceof CheckableTreeNode) {
               ((CheckableTreeNode) node).setChecked(true);
            } else if (node != null) {
               tree.setSelectionPath(path.toTreePath());
            }
         }
      }
      updateText();
   }

   private void uncheckAll(final TreeNode node) {
      if (node instanceof CheckableTreeNode) {
         ((CheckableTreeNode) node).setChecked(false);
      }
      for (int i = 0; i < node.getChildCount(); i++) {
         uncheckAll(node.getChildAt(i));
      }
      updateText();
   }

   private TreeNode findNode(TreeNode node, TreeNodePath nodePath, TreeNodePath pathSearch) {
      if (nodePath != null && nodePath.equals(pathSearch)) {
         return node;
      }
      for (int i = 0; i < node.getChildCount(); i++) {
         final TreeNode child = node.getChildAt(i);
         final TreeNodePath childPath = nodePath != null ? nodePath.path(child.toString()) : TreeNodePath.from(child.toString());
         final TreeNode foundNode = findNode(child, childPath, pathSearch);
         if (foundNode != null) {
            return foundNode;
         }
      }
      return null;
   }

   private void updateText() {
      setText(StringUtils.toString(getCheckedNodesPath(), ", "));
   }

   public boolean isPopupVisible() {
      return popupVisible;
   }

   public void showPopup() {
      if (!popupVisible) {
         this.popupVisible = true;
         repaint();
      }
   }

   public void hidePopup() {
      if (popupVisible) {
         this.popupVisible = false;
         popup.hide();
         repaint();
      }
   }

   @Override
   protected void paintComponent(Graphics g) {
      int textX = 2;
      super.paintComponent(g);

      if (this.dummyInsets == null) {
         this.dummyInsets = UIManager.getBorder("TextField.border").getBorderInsets(this);
      }

      if (this.icon == null) {
         this.icon = new ImageIcon(getClass().getResource("/tree16.png"));
      }

      if (this.icon != null) {
         int iconWidth = icon.getIconWidth();
         int iconHeight = icon.getIconHeight();
         int x = dummyInsets.left + 1;
         int y = (this.getHeight() - iconHeight) / 2;
         textX = x + iconWidth + 4;
         icon.paintIcon(this, g, x, y);
      }

      setMargin(new Insets(2, textX, 2, 2));
      if (isPopupVisible()) {
         if (popup.isVisible()) {
            popup.repaint();
         } else {
            popup.showPopup();
         }
      }
   }

   private class TreePopup extends PopupWindow {

      private final TreeField field;

      private boolean expandOnShow = true;

      private JPanel owner = null;

      public TreePopup(final TreeField owner, final boolean resizable) {
         super(resizable);
         this.field = owner;
      }

      @Override
      protected JComponent buildContent() {
         final JScrollPane scrollPane1 = new JScrollPane();
         Box commands = null;

         this.owner = new JPanel(new BorderLayout(3, 3));
         scrollPane1.setViewportView(tree);
         owner.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
         owner.setPreferredSize(new Dimension(field.getWidth(), POPUP_HEIGHT)); // default popup size

         if (field.confirmationOption) {
            final JToolBar toolbar = new JToolBar();
            commands = new Box(BoxLayout.LINE_AXIS);

            if (field.clearOption) {
               toolbar.add(new PopupWindow.CommandAction("Limpar Seleções",
                       new ImageIcon(getClass().getResource("/clear16.png"))) {
                  private static final long serialVersionUID = -6465295751224558465L;

                  @Override
                  protected boolean perform() {
                     uncheckAll((TreeNode) tree.getModel().getRoot());
                     tree.requestFocus();
                     return false;
                  }
               }).setFocusable(false);
            }
            toolbar.add(Box.createHorizontalGlue());
            toolbar.add(new PopupWindow.CommandAction("Confirma",
                    new ImageIcon(getClass().getResource("/confirm16.png"))) {
               private static final long serialVersionUID = -6465295751224558465L;

               @Override
               protected boolean perform() {
                  return true;
               }
            }).setFocusable(false);
            toolbar.add(Box.createHorizontalStrut(5));
            toolbar.setFloatable(false);
            toolbar.setOpaque(false);
            toolbar.setFocusable(false);
            commands.add(toolbar);
            commands.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            commands.setBackground(UIManager.getColor("Panel.background"));
            commands.setFocusable(false);
            commands.setOpaque(true);
         }

         owner.add(scrollPane1, BorderLayout.CENTER);
         if (commands != null) {
            owner.add(commands, BorderLayout.SOUTH);
         }
         return owner;
      }

      public void showPopup() {
         if (expandOnShow && (tree.getModel().getRoot() instanceof TreeNode)) {
            expandAll((TreeNode) tree.getModel().getRoot());
         }
         show(field);
         tree.requestFocus();
      }

      @Override
      protected void beforeHide() {
         field.popupVisible = false;
      }

      public boolean isExpandOnShow() {
         return expandOnShow;
      }

      public void setExpandOnShow(boolean expandOnShow) {
         this.expandOnShow = expandOnShow;
      }

      private void expandAll(TreeNode node) {
         if (!node.isLeaf()) {
            final TreePath path = treePath(node);
            final ArrayList<? extends TreeNode> list = Collections.list(node.children());
            if (tree.isCollapsed(path)) {
               tree.expandPath(path);
            }
            for (TreeNode treeNode : list) {
               expandAll(treeNode);
            }
         }
      }

      private TreePath treePath(TreeNode node) {
         if (node.getParent() != null) {
            return treePath(node.getParent()).pathByAddingChild(node);
         } else {
            return new TreePath(node);
         }
      }
   }

   private class TextFieldListener extends MouseAdapter implements KeyListener, FocusListener {

      @Override
      public void mousePressed(MouseEvent e) {
         if (e.getButton() == MouseEvent.BUTTON1) {
            e.consume();
            if (isPopupVisible()) {
               hidePopup();
            } else {
               showPopup();
            }
         }
      }

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
         switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
               if (!isPopupVisible()) {
                  e.consume();
                  showPopup();
               }
               break;

            case KeyEvent.VK_ESCAPE:
               if (isPopupVisible()) {
                  e.consume();
                  hidePopup();
               }
               break;
         }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }

      @Override
      public void focusGained(FocusEvent e) {
      }

      @Override
      public void focusLost(FocusEvent e) {
      }
   }

   private class TreeListener extends MouseAdapter implements KeyListener {

      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
         if (e.getButton() == MouseEvent.BUTTON1) {
            final TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
            if (treePath != null) {
               nodeClicked(treePath);
               e.consume();
            }
         }
      }

      @Override
      public void keyPressed(KeyEvent e) {
         switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
               nodeClicked(tree.getSelectionPath());
               break;
            case KeyEvent.VK_TAB:
               if (isPopupVisible()) {
                  popup.hide();
                  TreeField.this.processKeyEvent(e);
               }
               break;
            case KeyEvent.VK_ENTER:
               if (isPopupVisible()) {
                  popup.hide();
                  TreeField.this.processKeyEvent(e);
               }
               break;
         }
      }

      private void nodeClicked(TreePath path) {
         if (path.getLastPathComponent() instanceof CheckableTreeNode) {
            final CheckableTreeNode checkNode = (CheckableTreeNode) path.getLastPathComponent();
            checkNode.setChecked(!checkNode.isChecked());
            updateText();
         }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
   }
}
