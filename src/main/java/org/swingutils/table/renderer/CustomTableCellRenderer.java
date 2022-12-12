/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Collection;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import org.swingutils.StringUtils;
import org.swingutils.SwingUtils;
import org.swingutils.table.CellDataChecker;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public class CustomTableCellRenderer<T extends JComponent> extends DefaultTableCellRenderer {

   private Color pairBackground;
   private Color pairForeground;
   private Color oddBackground;
   private Color oddForeground;
   private Color changedBackground = null;
   private Color changedForeground = null;
   private Icon changedIcon = null;
   private Font selectedFont = null;
   private Font pairFont = null;
   private Font oddFont = null;
   private Font changedFont = null;
   private CellDataChecker cellDataChecker = null;

   public CustomTableCellRenderer(Color background, Color foreground, Color oddBackground, Color oddForeground) {
      super();
      this.pairBackground = background;
      this.pairForeground = foreground;
      this.oddForeground = oddForeground;
      this.oddBackground = oddBackground;
   }

   public CustomTableCellRenderer() {
      this(Color.WHITE, Color.BLACK, new Color(255, 255, 220), Color.BLACK);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      final JComponent renderer = getComponent(table, value, isSelected, hasFocus, row, column);
      final boolean changed = isChanged(row, column);

      renderer.setBackground(getColorBackground(table, row, isSelected, hasFocus, changed));
      renderer.setForeground(getColorForeground(table, row, isSelected, hasFocus, changed));
      renderer.setFont(getFont(table, row, isSelected, changed));
      if (renderer instanceof JLabel) {
         ((JLabel) renderer).setIcon(getIcon(table, isSelected, changed));
      } else if (renderer instanceof AbstractButton) {
         ((AbstractButton) renderer).setIcon(getIcon(table, isSelected, changed));
      }
      if (hasFocus) {
         Border border = null;
         if (isSelected) {
            border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
         }
         if (border == null) {
            border = UIManager.getBorder("Table.focusCellHighlightBorder");
         }
         renderer.setBorder(border);
      } else {
         renderer.setBorder(SwingUtils.getTableNoFocusBorder());
      }
      return renderer;
   }

   public Color getPairBackground() {
      return pairBackground;
   }

   public void setPairBackground(Color pairBackground) {
      this.pairBackground = pairBackground;
   }

   public Color getPairForeground() {
      return pairForeground;
   }

   public void setPairForeground(Color pairForeground) {
      this.pairForeground = pairForeground;
   }

   public Color getOddForeground() {
      return oddForeground;
   }

   public void setOddForeground(Color oddForeColor) {
      this.oddForeground = oddForeColor;
   }

   /**
    * @return the oddBackground
    */
   public Color getOddBackground() {
      return oddBackground;
   }

   /**
    * @param oddBackColor the oddBackground to set
    */
   public void setOddBackground(Color oddBackColor) {
      this.oddBackground = oddBackColor;
   }

   public Color getChangedBackground() {
      return changedBackground;
   }

   public Color getChangedForeground() {
      return changedForeground;
   }

   public void setChangedBackground(Color changedBackground) {
      this.changedBackground = changedBackground;
   }

   public void setChangedForeground(Color changedForeground) {
      this.changedForeground = changedForeground;
   }

   public Icon getChangedIcon() {
      return changedIcon;
   }

   public void setChangedIcon(Icon changedIcon) {
      this.changedIcon = changedIcon;
   }

   public Font getPairFont() {
      return pairFont;
   }

   public Font getOddFont() {
      return oddFont;
   }

   public Font getChangedFont() {
      return changedFont;
   }

   public Font getSelectedFont() {
      return selectedFont;
   }

   public void setPairFont(Font pairFont) {
      this.pairFont = pairFont;
   }

   public void setOddFont(Font oddFont) {
      this.oddFont = oddFont;
   }

   public void setChangedFont(Font changedFont) {
      this.changedFont = changedFont;
   }

   public void setSelectedFont(Font selectedFont) {
      this.selectedFont = selectedFont;
   }

   public void setCellDataChecker(CellDataChecker cellDataChecker) {
      this.cellDataChecker = cellDataChecker;
   }

   protected boolean isChanged(int row, int column) {
      return cellDataChecker != null && cellDataChecker.isChanged(row, column);
   }

   protected Color getColorBackground(JTable table, int row, boolean selected, boolean hasFocus, boolean changed) {
      final Color color;
      if (selected) {
         color = table.getSelectionBackground();
      } else if (changed && changedBackground != null) {
         color = changedBackground;
      } else if (row % 2 == 0) {
         if (pairBackground != null) {
            color = pairBackground;
         } else {
            color = table.getBackground();
         }
      } else if (oddBackground != null) {
         color = oddBackground;
      } else {
         color = table.getBackground();
      }
      return color;
   }

   protected Color getColorForeground(JTable table, int row, boolean selected, boolean hasFocus, boolean changed) {
      final Color color;
      if (selected) {
         color = table.getSelectionForeground();
      } else if (changed && changedForeground != null) {
         color = changedForeground;
      } else if (row % 2 == 0) {
         if (pairForeground != null) {
            color = pairForeground;
         } else {
            color = table.getForeground();
         }
      } else if (oddForeground != null) {
         color = oddForeground;
      } else {
         color = table.getForeground();
      }
      return color;
   }

   protected Font getFont(JTable table, int row, boolean selected, boolean changed) {
      if (selected && selectedFont != null) {
         return selectedFont;
      }
      if (changed && changedFont != null) {
         return changedFont;
      }
      if (row % 2 == 0) {
         if (pairFont != null) {
            return pairFont;
         }
      } else if (oddFont != null) {
         return oddFont;
      }
      return table.getFont();
   }

   protected Icon getIcon(JTable table, boolean selected, boolean changed) {
      if (!selected && changed) {
         return changedIcon;
      } else {
         return null;
      }
   }

   @Override
   protected void setValue(Object value) {
      if (value instanceof Collection) {
         super.setValue(StringUtils.toString((Collection) value, ", "));
      } else {
         super.setValue(value);
      }
   }

   protected T getComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      return (T) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
   }

}
