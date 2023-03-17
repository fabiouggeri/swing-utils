package org.swingutils.console;

import java.awt.Color;
import java.util.Arrays;

/**
 *
 * @author fabio_uggeri
 */
class ConsoleData {

   private int capacity = 0;
   public int rows;
   public int columns;
   public Color[] background;
   public Color[] foreground;
   public char[] text;

   ConsoleData() {
      // create empty console data
   }

   private void ensureCapacity(int minCapacity) {
      if (capacity >= minCapacity) {
         return;
      }

      char[] newText = new char[minCapacity];
      Color[] newBackground = new Color[minCapacity];
      Color[] newForeground = new Color[minCapacity];

      int size = rows * columns;
      if (size > 0) {
         System.arraycopy(text, 0, newText, 0, size);
         System.arraycopy(foreground, 0, newForeground, 0, size);
         System.arraycopy(background, 0, newBackground, 0, size);
      }

      text = newText;
      foreground = newForeground;
      background = newBackground;
      capacity = minCapacity;
   }

   void init(int columns, int rows, Color bgColor, Color fgColor) {
      ensureCapacity(rows * columns);
      this.rows = rows;
      this.columns = columns;
      Arrays.fill(background, bgColor);
      Arrays.fill(foreground, fgColor);
      Arrays.fill(text, ' ');
   }

   /**
    * Sets a single character position
    */
   public void setDataAt(int column, int row, char c, Color fg, Color bg) {
      int pos;
      if (row < 0 || row >= rows || column < 0 || column >= columns) {
         return;
      }
      pos = column + row * columns;
      text[pos] = c;
      foreground[pos] = fg;
      background[pos] = bg;
   }

   public char getCharAt(int column, int row) {
      int offset = column + row * columns;
      return text[offset];
   }

   public char[] getCharsRow(int row) {
      final int offset = row * columns;
      return Arrays.copyOfRange(text, offset, offset + columns);
   }

   public Color getForegroundAt(int column, int row) {
      int offset = column + row * columns;
      return foreground[offset];
   }

   public Color getBackgroundAt(int column, int row) {
      int offset = column + row * columns;
      return background[offset];
   }

   public void fillArea(char c, Color fg, Color bg, int column, int row, int width, int height) {
      for (int q = Math.max(0, row); q < Math.min(row + height, rows); q++) {
         for (int p = Math.max(0, column); p < Math.min(column + width, columns); p++) {
            int offset = p + q * columns;
            text[offset] = c;
            foreground[offset] = fg;
            background[offset] = bg;
         }
      }
   }
}
