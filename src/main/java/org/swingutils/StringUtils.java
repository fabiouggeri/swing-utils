/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.swingutils;

import java.util.Collection;

/**
 *
 * @author fabio_uggeri
 */
public class StringUtils {

   public static String toString(final Collection lista) {
      return toString(lista, ", ");
   }

   public static String toString(final Collection lista, final String sep) {
      if (lista != null && !lista.isEmpty()) {
         final StringBuilder sb = new StringBuilder();
         for (Object o : lista) {
            if (sb.length() > 0) {
               sb.append(sep).append(o);
            } else {
               sb.append(o);
            }
         }
         return sb.toString();
      } else {
         return "";
      }
   }

   public static String toString(final Object[] lista) {
      return toString(lista, ", ");
   }

   public static String toString(final Object[] lista, final String sep) {
      if (lista != null && lista.length > 0) {
         final StringBuilder sb = new StringBuilder();
         for (Object o : lista) {
            if (sb.length() > 0) {
               sb.append(sep).append(o);
            } else {
               sb.append(o);
            }
         }
         return sb.toString();
      } else {
         return "";
      }
   }

   public static boolean isEmpty(final String str) {
      return str == null || str.isBlank();
   }

   public static boolean isEmpty(final Collection<?> collection) {
      return collection == null || collection.isEmpty();
   }

   public static boolean equalsIgnoreCase(String str1, String str2) {
      if (str1 != null) {
         return str1.equalsIgnoreCase(str2);
      } else {
         return str2 == null;
      }
   }
   
   public static String padc(final String text, final int len) {
      return padc(text, len, ' ');
   }

   public static String padc(final String text, final int len, char fillChar) {
      int leftLen;
      int rightLen;
      StringBuilder resultText;
      if (text.length() == len) {
         return text;
      }
      if (text.length() > len) {
         return text.substring(0, len);
      }
      leftLen = (len - text.length()) / 2;
      rightLen = (len - text.length() + 1) / 2;
      resultText = new StringBuilder(len);
      for (int i = 0; i < leftLen; i++) {
         resultText.append(fillChar);
      }
      resultText.append(text);
      for (int i = 0; i < rightLen; i++) {
         resultText.append(fillChar);
      }
      return resultText.toString();
   }

   public static String replicate(final char fillChar, final int len) {
      StringBuilder str = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
         str.append(fillChar);
      }
      return str.toString();
   }

   public static String replicate(final String fillStr, final int len) {
      int fillLen = fillStr.length();
      StringBuilder str = new StringBuilder(len + fillLen);
      int filled = 0;
      while (filled < len) {
         str.append(fillStr);
         filled += fillLen;
      }
      return str.substring(0, len);
   }

}
