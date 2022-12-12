/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.table.editor;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.jdesktop.swingx.JXDatePicker;
import org.swingutils.StringUtils;
import org.swingutils.SwingUtils;

/**
 *
 * @author fabio_uggeri
 */
public class DateCellEditor extends AbstractCustomCellEditor<JXDatePicker> {

   private boolean editable;
   private DateFormat dateFormat;

   public DateCellEditor(DateFormat dateFormat, boolean editable) {
      this.dateFormat = dateFormat;
      this.dateFormat.getCalendar().setLenient(false);
      this.editable = editable;
   }

   public DateCellEditor() {
      this(new SimpleDateFormat("dd/MM/yyyy"), true);
   }

   @Override
   public Component getEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
      final JXDatePicker component = getComponent();
      final JFormattedTextField editor = component.getEditor();
      component.setDate((Date) value);
      component.requestFocus();
      component.setEditable(editable);
      editor.setForeground(table.getForeground());
      editor.setBackground(table.getBackground());
      editor.setFont(table.getFont());
      editor.setBorder(SwingUtils.getTableNoFocusBorder());
      if (editable) {
         editor.selectAll();
      }
      return component;
   }

   @Override
   public Object getEditorValue() {
      return getComponent().getDate();
   }

   @Override
   public JXDatePicker createComponent() {
      final JXDatePicker component = new JXDatePicker();
      component.setBorder(BorderFactory.createEmptyBorder());
      component.setFocusable(true);
      component.setEditor(new JFormattedTextField(dateFormat));
      return component;
   }

   @Override
   public void addKeyListener(KeyListener keyListener) {
      getComponent().addKeyListener(keyListener);
      getComponent().getEditor().addKeyListener(keyListener);
   }

   public void setDateFormat(DateFormat dateFormat) {
      final KeyListener listeners[] = getComponent().getEditor().getKeyListeners();
      final JFormattedTextField editor = new JFormattedTextField(dateFormat);
      this.dateFormat = dateFormat;
      getComponent().setEditor(editor);
      for (KeyListener l : listeners) {
         editor.addKeyListener(l);
      }
   }

   @Override
   public boolean stopCellEditing() {
      try {
         if (! StringUtils.isEmpty(getComponent().getEditor().getText())) {
            getComponent().commitEdit();
         } else {
            getComponent().setDate(null);
         }
         return super.stopCellEditing();
      } catch (ParseException ex) {
         JOptionPane.showMessageDialog(null, "Informe uma data válida", "Data inválida", JOptionPane.ERROR_MESSAGE);
         return false;
      }
   }

}
