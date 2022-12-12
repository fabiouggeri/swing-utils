/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.lang.reflect.ParameterizedType;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.swingutils.SwingUtils;
import org.swingutils.fields.FloatTextField;
import org.swingutils.fields.IntTextField;

/**
 *
 * @author fabio_uggeri
 * @param <T>
 */
public class NumberCellEditor<T extends Number> extends AbstractCustomCellEditor<JTextField> {

   private static final NumberFormat FORMATTER = new DecimalFormat("###,###,###,###,###,###,###,###,##0.##########");

   private static final char GROUP_SEPARATOR = DecimalFormatSymbols.getInstance().getGroupingSeparator();

   private final T defaultValue;

   private final boolean floatType;

   public NumberCellEditor(final T defaultValue) {
      super();
      this.defaultValue = defaultValue;
      if (((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments().length > 0) {
         final Class<T> type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
         this.floatType = Float.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type);
      } else {
         this.floatType = false;
      }
   }

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
      getComponent().setForeground(table.getForeground());
      getComponent().setBackground(table.getBackground());
      getComponent().setFont(table.getFont());
      getComponent().setBorder(SwingUtils.getTableNoFocusBorder());
      if (value != null) {
         getComponent().setText(numberToString((T)value));
         getComponent().selectAll();
      }
      return getComponent();
   }

   @Override
   public Object getEditorValue() {
      if (getComponent().getDocument() != null) {
         return stringtoNumber(getComponent().getText());
      }
      return defaultValue;
   }

   public T getDefaultValue() {
      return defaultValue;
   }

   protected Object stringtoNumber(String text) {
      try {
         if (floatType) {
            return FORMATTER.parse(text).doubleValue();
         } else {
            return FORMATTER.parse(text).longValue();
         }
      } catch (NumberFormatException | ParseException ex) {
         return 0f;
      }
   }

   protected String numberToString(T value) {
      if (floatType) {
         return FORMATTER.format(value.doubleValue()).replace(Character.toString(GROUP_SEPARATOR), "");
      } else {
         return FORMATTER.format(value.longValue()).replace(Character.toString(GROUP_SEPARATOR), "");
      }
   }

   @Override
   public void addKeyListener(KeyListener keyListener) {
   }

   @Override
   public JTextField createComponent() {
      final JTextField component;
      if (floatType) {
         component = new FloatTextField();
      } else {
         component = new IntTextField();
      }
      component.setText(numberToString(getDefaultValue()));
      return component;
   }
}
