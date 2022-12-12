/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.fields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ItemEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import org.swingutils.common.ColorIcon;
import org.swingutils.common.ColorValue;

public class ColorComboBox extends JComboBox {

   private static final boolean IS_GTK = "GTK".equals(UIManager.getLookAndFeel().getID());

   private final boolean allowCustomColors;

   private final boolean allowDefault;

   private ColorValue lastSelection;

   /**
    * C'tor The combo box is initialized with some basic colors and user can also pick a custom color
    */
   public ColorComboBox() {
      this(new Color[]{
         Color.BLACK,
         Color.BLUE,
         Color.CYAN,
         Color.DARK_GRAY,
         Color.GRAY,
         Color.GREEN,
         Color.LIGHT_GRAY,
         Color.MAGENTA,
         Color.ORANGE,
         Color.PINK,
         Color.RED,
         Color.WHITE,
         Color.YELLOW,}, new String[0], true, true);
   }

   /**
    * Initialize the combo with given list of Colors.
    *
    * @param values Color values.
    * @param names Name of colors.
    * @param allowCustomColors True to allow users to pick a custom colors, false if user can choose from given colors only.
    */
   public ColorComboBox(Color[] values, String[] names, boolean allowCustomColors, boolean allowDefault) {
      super.setModel(createModel(values, names, allowCustomColors, allowDefault));
      this.allowCustomColors = allowCustomColors;
      this.allowDefault = allowDefault;
      setEditable(false);
      setRenderer(new ColorComboBoxRendererWrapper(this));
      if (allowCustomColors) {
         addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
               SwingUtilities.invokeLater(() -> {
                  if (getSelectedItem() == ColorValue.CUSTOM_COLOR) {
                     pickCustomColor();
                  }
                  lastSelection = (ColorValue) getSelectedItem();
               });
            }
         });
      }
   }

   /**
    * Change the combo content.
    *
    * @param colors Colors to show in the combo box.
    * @param names Names of the colors.
    */
   public void setModel(Color[] colors, String[] names) {
      super.setModel(createModel(colors, names, allowCustomColors, allowDefault));
      SwingUtilities.invokeLater(() -> {
         repaint();
      });
   }

   /**
    * Retrieve currently selected color.
    *
    * @return Selected Color or null.
    */
   public Color getSelectedColor() {
      ColorValue cv = (ColorValue) getSelectedItem();
      return null == cv ? null : cv.getColor();
   }

   /**
    * Select given in the combo box.
    *
    * @param newColor Color to be selected or null to clear selection. If the color isn't in the combo box list and custom colors
    * are not allowed the selection does not change.
    * @see #ColorComboBox(java.awt.Color[], java.lang.String[], boolean)
    */
   public void setSelectedColor(Color newColor) {
      if (null == newColor) {
         if (allowDefault) {
            setSelectedItem(ColorValue.DEFAULT_COLOR);
         } else {
            setSelectedIndex(-1);
         }
         return;
      }
      for (int i = 0; i < getItemCount(); i++) {
         ColorValue cv = (ColorValue) getItemAt(i);
         if (newColor.equals(cv.getColor())) {
            setSelectedItem(cv);
            return;
         }
      }
      if (allowCustomColors) {
         removeCustomValue();
         ColorValue cv = new ColorValue(newColor, true);
         DefaultComboBoxModel model = (DefaultComboBoxModel) getModel();
         model.insertElementAt(cv, 0);
         setSelectedItem(cv);
      }
   }

   private void removeCustomValue() {
      for (int i = 0; i < getItemCount(); i++) {
         ColorValue cv = (ColorValue) getItemAt(i);
         if (cv.isCustom()) {
            DefaultComboBoxModel model = (DefaultComboBoxModel) getModel();
            model.removeElementAt(i);
            return;
         }
      }
   }

   private void pickCustomColor() {
      Color c = JColorChooser.showDialog(
              SwingUtilities.getAncestorOfClass(Dialog.class, this),
              "Selecione a cor", //NOI18N
              lastSelection != null ? ((ColorValue) lastSelection).getColor() : null);
      if (c != null) {
         setSelectedColor(c);
      } else if (lastSelection != null) {
         setSelectedItem(lastSelection);
      }
   }

   private static DefaultComboBoxModel createModel(Color[] colors, String[] names, boolean allowCustomColors, boolean allowDefault) {
      DefaultComboBoxModel model = new DefaultComboBoxModel();

      for (int i = 0; i < colors.length; i++) {
         Color c = colors[i];
         String text = null;
         if (i < names.length) {
            text = names[i];
         }
         if (null == text) {
            text = ColorValue.toText(c);
         }
         model.addElement(new ColorValue(text, c, false));
      }
      if (allowDefault) {
         model.addElement(ColorValue.DEFAULT_COLOR);
      }
      if (allowCustomColors) {
         model.addElement(ColorValue.CUSTOM_COLOR);
      }
      return model;
   }

   private class ColorComboBoxRendererWrapper implements ListCellRenderer, UIResource {

      private final ListCellRenderer renderer;

      ColorComboBoxRendererWrapper(JComboBox comboBox) {
         this.renderer = comboBox.getRenderer();
         if (renderer instanceof ColorComboBoxRendererWrapper) {
            throw new IllegalStateException("Custom renderer is already initialized."); //NOI18N
         }
         comboBox.setRenderer(this);
      }

      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         Component res = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
         if (res instanceof JLabel) {
            synchronized (renderer) {
               JLabel label = (JLabel) res;
               int height = IS_GTK ? 10 : Math.max(res.getPreferredSize().height - 4, 4);
               Icon icon;
               if (value instanceof ColorValue) {
                  ColorValue color = (ColorValue) value;
                  if (value == ColorValue.CUSTOM_COLOR) {
                     icon = null;
                  } else {
                     icon = new ColorIcon(color.getColor(), height);
                  }
                  label.setText(color.getText());
               } else {
                  icon = null;
               }
               label.setIcon(icon);
            }
         }
         return res;
      }
   }
}
