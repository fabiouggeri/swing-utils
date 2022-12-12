/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EventListener;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.swingutils.SwingUtils;

/**
 *
 * @author fabio_uggeri
 */
public class TextCellEditor extends AbstractCustomCellEditor<JPanel> implements ActionListener {

   private final Pattern editionPattern;

   private final Pattern validationPattern;

   private JButton button;

   private JTextField textField;

   private boolean upperCase = false;

   private JTable table = null;

   private int column = -1;

   private boolean showButton = true;

   private TextCellEditorListener editorListener = null;

   private boolean wrapTextArea = false;

   public TextCellEditor(final String editionRegExpr, final String validationRegExpr, final boolean upperCase, final boolean showButton) {
      super();
      this.upperCase = upperCase;
      this.showButton = showButton;
      if (editionRegExpr != null) {
         this.editionPattern = Pattern.compile(editionRegExpr);
      } else {
         this.editionPattern = null;
      }
      if (validationRegExpr != null) {
         this.validationPattern = Pattern.compile(validationRegExpr);
      } else {
         this.validationPattern = null;
      }
   }

   public TextCellEditor(final String editionRegExpr, final boolean upperCase, final boolean showButton) {
      this(editionRegExpr, editionRegExpr, upperCase, showButton);
   }

   public TextCellEditor(final boolean upperCase, final boolean showButton) {
      this(null, upperCase, showButton);
   }

   public TextCellEditor(final boolean upperCase) {
      this(null, upperCase, false);
   }

   public TextCellEditor() {
      this(null, false, false);
   }

   @Override
   public Object getEditorValue() {
      if (textField != null && textField.getDocument() != null) {
         if (editionPattern == null || editionPattern.matcher(textField.getText()).matches()) {
            return insertNewLines(textField.getText());
         }
      }
      return null;
   }

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int colIndex) {
      final Component component = getComponent();
      this.table = table;
      this.column = colIndex;
      if (value == null) {
         textField.setText("");
      } else if (editionPattern != null && !editionPattern.matcher(value.toString()).matches()) {
         textField.setText("");
      } else {
         textField.setText(removeNewLines(upperCase ? value.toString().toUpperCase() : value.toString()));
      }
      textField.setForeground(table.getForeground());
      textField.setBackground(table.getBackground());
      textField.setFont(table.getFont());
      textField.setBorder(SwingUtils.getTableNoFocusBorder());
      textField.selectAll();
      return component;
   }

   private String removeNewLines(final String str) {
      return str.replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r");
   }

   private String insertNewLines(final String str) {
      return str.replaceAll("\\\\n", "\n").replaceAll("\\\\r", "\r");
   }

   @Override
   public JPanel createComponent() {
      JPanel panel;
      if (showButton) {
         button = new JButton("...");
         button.addActionListener(this);
         button.setFocusable(false);
         button.setFocusPainted(false);
         button.setMargin(new Insets(0, 0, 0, 0));
      }
      textField = new JTextField();
      if (editionPattern != null) {
         textField.setDocument(new RegExprDocument(editionPattern, upperCase));
      } else {
         textField.setDocument(new RegExprDocument(upperCase));
      }
      textField.getDocument().putProperty("filterNewlines", false);
      textField.getDocument().addDocumentListener(new DocumentListener() {
         @Override
         public void insertUpdate(DocumentEvent e) {
            if (editorListener != null) {
               editorListener.textChanged(textField);
            }
         }

         @Override
         public void removeUpdate(DocumentEvent e) {
            if (editorListener != null) {
               editorListener.textChanged(textField);
            }
         }

         @Override
         public void changedUpdate(DocumentEvent e) {
            if (editorListener != null) {
               editorListener.textChanged(textField);
            }
         }
      });
      panel = new JPanel(new BorderLayout()) {

         @Override
         public void setBackground(Color bg) {
            textField.setBackground(bg);
         }

         @Override
         public void setForeground(Color fg) {
            textField.setForeground(fg);
         }

         @Override
         public void addNotify() {
            super.addNotify();
            textField.requestFocus();
         }

         @Override
         protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
            InputMap map = textField.getInputMap(condition);
            ActionMap am = textField.getActionMap();

            if (!textField.isFocusOwner()) {
               textField.requestFocus();
            }
            if (map != null && am != null && isEnabled()) {
               Object binding = map.get(ks);
               Action action = (binding == null) ? null : am.get(binding);
               if (action != null) {
                  if (!textField.isFocusOwner()) {
                     textField.requestFocus();
                  }
                  return SwingUtilities.notifyAction(action, ks, e, textField, e.getModifiers());
               }
            }
            return false;
         }
      };
      panel.add(textField);
      if (showButton) {
         panel.add(button, BorderLayout.EAST);
      }
      return panel;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (editorListener != null) {
         editorListener.buttonClicked(textField);
      } else {
         showTextArea();
      }
   }

   private void showTextArea() throws HeadlessException {
      final RSyntaxTextArea textArea = new RSyntaxTextArea(10, 80);
      final JCheckBox chkWrap = new JCheckBox("Quebra de linha", isWrapTextArea());
      final JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      final JOptionPane pane =  new JOptionPane(scroll, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      final JDialog dialog =  pane.createDialog(null, (String) table.getColumnName(column));
      Object value = getEditorValue();
      if (value != null) {
         textArea.setText(insertNewLines(((String) value)));
         textArea.discardAllEdits();
         textArea.setCaretPosition(0);
      }
      textArea.setLineWrap(isWrapTextArea());
      textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
      chkWrap.addActionListener((ActionEvent e1) -> {
         setWrapTextArea(chkWrap.isSelected());
         textArea.setLineWrap(isWrapTextArea());
      });
      textArea.setMarkOccurrences(true);
      textArea.setMarkOccurrencesDelay(1);
      textArea.setBracketMatchingEnabled(true);
      textArea.setAnimateBracketMatching(true);
      dialog.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/texto.png")).getImage());
      dialog.add(chkWrap, java.awt.BorderLayout.NORTH);
      dialog.setSize(scroll.getWidth() + 100, scroll.getHeight() + 150);
      dialog.setResizable(true);
      dialog.setVisible(true);
      dialog.dispose();
      if (pane.getValue() != null) {
         if ((int)pane.getValue() == JOptionPane.OK_OPTION) {
            textField.setText(removeNewLines(textArea.getText()));
         }
      }
   }

   /**
    * @return the showButton
    */
   public boolean isShowButton() {
      return showButton;
   }

   @Override
   public void addKeyListener(final KeyListener keyListener) {
      getComponent().addKeyListener(keyListener);
      textField.addKeyListener(keyListener);
   }

   public void setTextCellEditorListener(TextCellEditorListener editorListener) {
      this.editorListener = editorListener;
   }

   @Override
   public boolean stopCellEditing() {
      if (super.stopCellEditing() && validationPattern != null) {
         final String value = (String) getEditorValue();
         if (value != null) {
            return validationPattern.matcher(value).matches();
         }
      }
      return false;
   }

   public boolean isWrapTextArea() {
      return wrapTextArea;
   }

   public void setWrapTextArea(boolean wrapTextArea) {
      this.wrapTextArea = wrapTextArea;
   }

   private class RegExprDocument extends PlainDocument {

      private Pattern pattern;

      private boolean upperCase;

      RegExprDocument(final boolean upperCase) {
         this.upperCase = upperCase;
      }

      RegExprDocument(final Pattern pattern, final boolean upperCase) {
         this(upperCase);
         this.pattern = pattern;
      }

      @Override
      public void insertString(int offs, String str, AttributeSet attrSet) throws BadLocationException {
         String newString;
         if (str == null) {
            return;
         }
         if (getLength() == 0) {
            newString = str;
         } else {
            final String oldString = getText(0, getLength());
            newString = oldString.substring(0, offs) + str + oldString.substring(offs);
         }
         if (pattern == null || pattern.matcher(newString).matches()) {
            if (upperCase) {
               super.insertString(offs, str.toUpperCase(), attrSet);
            } else {
               super.insertString(offs, str, attrSet);
            }
         }
      }
   }

   public interface TextCellEditorListener extends EventListener {
      public void buttonClicked(JTextField field);
      public void textChanged(JTextField field);
   }
}
