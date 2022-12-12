/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormatSymbols;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 *
 * @author fabio_uggeri
 */
public class FloatTextField extends JTextField implements KeyListener {

   private static final char DECIMAL_SEPARATOR = DecimalFormatSymbols.getInstance().getDecimalSeparator();
   private static final Pattern SIGNED_PATTERN = Pattern.compile("\\-[0-9]*(\\" + DECIMAL_SEPARATOR + "[0-9]*)?");
   private static final Pattern UNSIGNED_PATTERN = Pattern.compile("[0-9]+(\\" + DECIMAL_SEPARATOR + "[0-9]*)?");

   private boolean unsigned = false;

   public FloatTextField() {
      super();
      configureKeyListener();
   }

   @Override
   protected Document createDefaultModel() {
      return new FloatTextField.FloatTextDocument();
   }

   public float floatValue() {
      if (getDocument() != null) {
         try {
            return Float.parseFloat(getText());
         } catch (NumberFormatException e) {
         }
      }
      return 0L;
   }

   public double doubleValue() {
      if (getDocument() != null) {
         try {
            return Double.parseDouble(getText());
         } catch (NumberFormatException e) {
         }
      }
      return 0;
   }

   public boolean isUnsigned() {
      return unsigned;
   }

   public void setUnsigned(boolean unsigned) {
      this.unsigned = unsigned;
   }

   @Override
   public void keyTyped(KeyEvent e) {
      if (e.getKeyChar() == ',' || e.getKeyChar() == '.') {
         final String text = getText();
         if (text == null) {
            e.setKeyChar(DECIMAL_SEPARATOR);
         } else {
            int sepIndex = text.indexOf(DECIMAL_SEPARATOR);
            if (sepIndex < 0) {
               e.setKeyChar(DECIMAL_SEPARATOR);
            } else {
               setCaretPosition(sepIndex + 1);
               e.consume();
            }
         }
      }
   }

   @Override
   public void keyPressed(KeyEvent e) {
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }

   private void configureKeyListener() {
      addKeyListener(this);
   }

   private class FloatTextDocument extends PlainDocument {
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
         if (unsigned) {
            if (UNSIGNED_PATTERN.matcher(newString).matches()) {
               super.insertString(offs, str, attrSet);
            }
         } else {
            if (UNSIGNED_PATTERN.matcher(newString).matches() || SIGNED_PATTERN.matcher(newString).matches()) {
               super.insertString(offs, str, attrSet);
            }
         }
      }
   }
}
