/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.swingutils;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import static java.util.Arrays.asList;
import java.util.Enumeration;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author fabio_uggeri
 */
public class SwingUtils {

   public static final Border NO_FOCUS_CELL_BORDER = new EmptyBorder(0, 1, 0, 1);

   private static final int COLUMNS = 2;

   public static Border getTableNoFocusBorder() {
      Border border = UIManager.getBorder("Table.cellNoFocusBorder");
      if (border != null) {
         return border;
      }
      return NO_FOCUS_CELL_BORDER;
   }

   public static void centralize(Container window) {
      final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      if (device != null) {
         Rectangle bounds = device.getDefaultConfiguration().getBounds();
         window.setLocation((bounds.width - window.getSize().width) / COLUMNS, (bounds.height - window.getSize().height) / COLUMNS);
      } else {
         final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         window.setLocation((screenSize.width - window.getSize().width) / COLUMNS, (screenSize.height - window.getSize().height) / COLUMNS);
      }
   }

   public static void centralize(Container relativeWindow, Container window) {
      if (relativeWindow != null) {
         window.setLocation(relativeWindow.getX() + ((relativeWindow.getWidth() - window.getSize().width) / COLUMNS),
                 relativeWindow.getY() + ((relativeWindow.getHeight() - window.getSize().height) / COLUMNS));
      } else {
         centralize(window);
      }
   }

   public static void setUIFont(FontUIResource f) {
      final Enumeration keys = UIManager.getDefaults().keys();
      while (keys.hasMoreElements()) {
         final Object key = keys.nextElement();
         final Object value = UIManager.get(key);
         if (value instanceof FontUIResource) {
            UIManager.put(key, f);
         }
      }
   }

   public static GraphicsDevice getWindowDevice(Window window) {
      Rectangle bounds = window.getBounds();
      return asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()).stream()
              // pick devices where window located
              .filter(d -> d.getDefaultConfiguration().getBounds().intersects(bounds))
              // sort by biggest intersection square
              .sorted((f, s) -> Long.compare(//
              square(f.getDefaultConfiguration().getBounds().intersection(bounds)),
              square(s.getDefaultConfiguration().getBounds().intersection(bounds))))
              // use one with the biggest part of the window
              .reduce((f, s) -> s) //

              // fallback to default device
              .orElse(window.getGraphicsConfiguration().getDevice());
   }

   public static long square(Rectangle rec) {
      return Math.abs(rec.width * rec.height);
   }
}
