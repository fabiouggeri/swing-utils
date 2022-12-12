/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swingutils.common;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fabio_uggeri
 */
public class ColorValue {

   /**
    * @return the text
    */
   public String getText() {
      return text;
   }

   /**
    * @return the color
    */
   public Color getColor() {
      return color;
   }

   /**
    * @return the custom
    */
   public boolean isCustom() {
      return custom;
   }

   public static final ColorValue CUSTOM_COLOR = new ColorValue("Personalizada", null, false);
   public static final ColorValue DEFAULT_COLOR = new ColorValue("Default", null, false);

   private static final Map<Color, String> COLOR_MAP = new HashMap<Color, String>();

   static {
      COLOR_MAP.put(Color.BLACK, "Preta");
      COLOR_MAP.put(Color.BLUE, "Azul");
      COLOR_MAP.put(Color.CYAN, "Ciano");
      COLOR_MAP.put(Color.DARK_GRAY, "Cinza escura");
      COLOR_MAP.put(Color.GRAY, "Cinza");
      COLOR_MAP.put(Color.GREEN, "Verde");
      COLOR_MAP.put(Color.LIGHT_GRAY, "Cinza clara");
      COLOR_MAP.put(Color.MAGENTA, "Magenta");
      COLOR_MAP.put(Color.ORANGE, "Laranja");
      COLOR_MAP.put(Color.PINK, "Rosa");
      COLOR_MAP.put(Color.RED, "Vermelha");
      COLOR_MAP.put(Color.WHITE, "Branca");
      COLOR_MAP.put(Color.YELLOW, "Amarela");
   }

   private final String text;
   private final Color color;
   private final boolean custom;

   public static String toText(Color color) {
      String text = COLOR_MAP.get(color);
      if (null == text && null != color) {
         StringBuilder sb = new StringBuilder();
         sb.append('[').append(color.getRed()).
                 append(',').append(color.getGreen()).
                 append(',').append(color.getBlue()).
                 append(']');
         text = sb.toString();
      }
      return text;
   }

   public ColorValue(Color color, boolean custom) {
      this(toText(color), color, custom);
   }

   public ColorValue(String text, Color color, boolean custom) {
      this.text = text;
      this.color = color;
      this.custom = custom;
   }

   @Override
   public String toString() {
      return getText();
   }
}
