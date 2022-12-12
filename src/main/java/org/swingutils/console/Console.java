package org.swingutils.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author fabio_uggeri
 */
public class Console extends JPanel implements HierarchyListener {

   private static final long serialVersionUID = 3571518591759968333L;

   private static final Color DEFAULT_FOREGROUND = Color.LIGHT_GRAY;
   private static final Color DEFAULT_BACKGROUND = Color.BLACK;
   private static final Font DEFAULT_FONT = new Font("Courier New", Font.PLAIN, 12);
   private static final int DEFAULT_BLINKRATE = 200;

   private final ConsoleData data = new ConsoleData();

   private int fontWidth;
   private int fontHeight;
   private int fontYOffset;

   private boolean cursorVisible = false;
   private boolean cursorBlinkOn = false;
   private boolean cursorInverted = true;

   private int cursorX = 0;
   private int cursorY = 0;
   private Font currentFont = null;
   private Color currentForeground = DEFAULT_FOREGROUND;
   private Color currentBackground = DEFAULT_BACKGROUND;

   private BufferedImage image;

   private Timer blinkTimer;

   public Console(int columns, int rows) {
      configure(columns, rows);
   }

   public Console() {
      this(80, 25);
   }

   private void configure(int columns, int rows) {
      setFont(DEFAULT_FONT);
      data.init(columns, rows, currentBackground, currentForeground);
      setPreferredSize(new Dimension(columns * fontWidth, rows * fontHeight));
   }

   /**
    * Utility class to handle the cursor blink animations
    */
   private class TimerAction implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         if (cursorBlinkOn && isShowing()) {
            cursorInverted = !cursorInverted;
            repaintArea(getCursorX(), getCursorY(), 1, 1);
         } else {
            stopBlinking();
         }
      }
   }

   private void stopBlinking() {
      if (blinkTimer != null) {
         blinkTimer.stop();
         cursorInverted = true;
      }
   }

   private void startBlinking() {
      getTimer().start();
   }

   public void setCursorBlink(boolean blink) {
      if (blink) {
         cursorBlinkOn = true;
         startBlinking();
      } else {
         cursorBlinkOn = false;
         stopBlinking();
      }
   }

   public void setBlinkDelay(int millis) {
      getTimer().setDelay(millis);
   }

   private Timer getTimer() {
      if (blinkTimer == null) {
         blinkTimer = new Timer(DEFAULT_BLINKRATE, new TimerAction());
         blinkTimer.setRepeats(true);
         if (cursorBlinkOn) {
            startBlinking();
         }
      }
      return blinkTimer;
   }

   @Override
   public void addNotify() {
      super.addNotify();
      addHierarchyListener(this);
   }

   @Override
   public void removeNotify() {
      removeHierarchyListener(this);
      super.removeNotify();
   }

   public void setRows(int rows) {
      setSize(this.data.columns * fontWidth, rows * fontHeight);
   }


   public void setFontSize(final int newSize) {
      setFont(currentFont.deriveFont((float)newSize));
   }

   @Override
   public void setFont(Font f) {
      currentFont = f;
      resize();
   }

   private void resize() {
      final FontRenderContext fontRenderContext = new FontRenderContext(currentFont.getTransform(), false, false);
      final Rectangle2D charBounds = currentFont.getStringBounds("X",fontRenderContext);
      fontWidth = (int) charBounds.getWidth();
      fontHeight = (int) charBounds.getHeight();
  		fontYOffset = -(int) charBounds.getMinY();

      if (data != null) {
         setPreferredSize(new Dimension(data.columns * fontWidth, (data.rows * fontHeight) + fontYOffset));
         repaint();
      }
   }

   public void setCursorVisible(boolean visible) {
      cursorVisible = visible;
   }

   public int getRows() {
      return data.rows;
   }

   public void setColumns(int columns) {
      setSize(columns * fontWidth, this.data.rows * fontHeight);
   }

   public int getColumns() {
      return data.columns;
   }

   public int getFontWidth() {
      return fontWidth;
   }

   public int getFontHeight() {
      return fontHeight;
   }

   /**
    * Fires a repaint event on a specified rectangle of characters in the console
    * @param column
    * @param row
    * @param width
    * @param height
    */
   public void repaintArea(int column, int row, int width, int height) {
      int fw = getFontWidth();
      int fh = getFontHeight();
      repaint(column * fw, row * fh, width * fw, height * fh);
   }

   public void clear() {
      clearArea(0, 0, data.columns, data.rows);
   }

   public void resetCursor() {
      repaintArea(cursorX, cursorY, 0, 0);
      cursorX = 0;
      cursorY = 0;
      repaintArea(cursorX, cursorY, 0, 0);
   }

   public void clearScreen() {
      clear();
      resetCursor();
   }

   private void clearArea(int column, int row, int width, int height) {
      data.fillArea(' ', currentForeground, currentBackground, column, row, width, height);
      repaintArea(0, 0, width, height);
   }

   private BufferedImage getImage() {
      if (image == null) {
         image = new BufferedImage(fontWidth * data.columns, fontHeight * data.rows, BufferedImage.TYPE_INT_RGB);
      } else {
         final int width = fontWidth * data.columns;
         final int height = fontYOffset + (fontHeight * data.rows);
         if (image.getWidth() != width || image.getHeight() != height) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
         }
      }
      return image;
   }

   private void drawImage() {
      final BufferedImage img = getImage();
      final Graphics2D graphic = (Graphics2D) img.getGraphics();
      final char chars[] = new char[1];

      for (int row = 0; row < data.rows; row++) {
         for (int col = 0; col < data.columns; col++) {
            final int x;
            final int y;
            Color bgColor = data.getBackgroundAt(col, row);
            Color fgColor = data.getForegroundAt(col, row);
            chars[0] = data.getCharAt(col, row);
            if (col == cursorX && row == cursorY && cursorVisible && cursorBlinkOn && cursorInverted) {
               // swap foreground and background colours
               Color tmpColor = fgColor;
               fgColor = bgColor;
               bgColor = tmpColor;
            }
            // set font
            graphic.setFont(currentFont);

            x = col * fontWidth;
            y = fontYOffset + (row * fontHeight);
            // draw background
            graphic.setBackground(bgColor);
            graphic.clearRect(x, y, x + fontWidth, y + fontHeight);

            // draw chars up to this point
            graphic.setColor(fgColor);
            graphic.drawChars(chars, 0, 1, x, y);
         }
      }
   }

   @Override
   public void paintComponent(Graphics graphics) {
      drawImage();
      graphics.drawImage(image, 0, 0, this);
   }

   public void setCursorPos(int row, int column) {
      if ((column < 0) || (column >= data.columns)) {
         throw new Error("Invalid X cursor position: " + column);
      }
      if ((row < 0) || (row >= data.rows)) {
         throw new Error("Invalid Y cursor position: " + row);
      }
      cursorX = column;
      cursorY = row;
   }

   public int getCursorX() {
      return cursorX;
   }

   public int getCursorY() {
      return cursorY;
   }

   @Override
   public void setForeground(Color c) {
      currentForeground = c;
   }

   @Override
   public void setBackground(Color c) {
      currentBackground = c;
   }

   @Override
   public Color getForeground() {
      return currentForeground;
   }

   @Override
   public Color getBackground() {
      return currentBackground;
   }

   public char getCharAt(int column, int row) {
      return data.getCharAt(column, row);
   }

   public Color getForegroundAt(int column, int row) {
      return data.getForegroundAt(column, row);
   }

   public Color getBackgroundAt(int column, int row) {
      return data.getBackgroundAt(column, row);
   }

   /**
    * Redirects System.out to this console by calling System.setOut
    */
   public void captureStdOut() {
      final PrintStream ps = new PrintStream(new OutputStream() {
         @Override
         public void write(int b) throws IOException {
            print((char)b);
         }
      });
      System.setOut(ps);
   }

   /**
    * Redirects System.out to this console by calling System.setOut
    */
   public void captureStdErr() {
      final PrintStream ps = new PrintStream(new OutputStream() {
         @Override
         public void write(int b) throws IOException {
            print((char)b);
         }
      });
      System.setErr(ps);
   }

   public void print(char c) {
      data.setDataAt(cursorX, cursorY, c, currentForeground, currentBackground);
      moveCursor(c);
   }

   private void moveCursor(char c) {
      switch (c) {
         case '\n':
            cursorY++;
            cursorX = 0;
            break;
         default:
            cursorX++;
            if (cursorX >= data.columns) {
               cursorX = 0;
               cursorY++;
            }
            break;
      }
   }

   public void println(String line) {
      print(line);
      print('\n');
   }

   public void print(String string, Color foreGround, Color backGround) {
      print(cursorY, cursorX, string, foreGround, backGround);
   }

   public void print(int row, int col, String string, Color foreGround, Color backGround) {
      Color foreTemp = currentForeground;
      Color backTemp = currentBackground;
      setCursorPos(row, col);
      setForeground(foreGround);
      setBackground(backGround);
      print(string);
      setForeground(foreTemp);
      setBackground(backTemp);
   }

   public void print(int row, int col, String string) {
      setCursorPos(row, col);
      print(string);
   }

   public void print(String string) {
      for (int i = 0; i < string.length(); i++) {
         char c = string.charAt(i);
         print(c);
      }
   }

   public void fillArea(char c, Color fg, Color bg, int column, int row, int width, int height) {
      data.fillArea(c, fg, bg, column, row, width, height);
      repaintArea(column, row, width, height);
   }

   @Override
   public void hierarchyChanged(HierarchyEvent e) {
      if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
         if (isShowing()) {
            startBlinking();
         } else {
            stopBlinking();
         }
      }
   }

}
