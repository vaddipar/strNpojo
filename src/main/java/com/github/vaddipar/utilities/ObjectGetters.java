package com.github.vaddipar.utilities;

import java.util.ArrayList;

public class ObjectGetters {
  public static Object getObject(String val, Class className) {
    Object retVal = null;

    if (className == Integer.class) {
      retVal = Integer.parseInt(val);
    } else if (className == Float.class) {
      retVal = Float.parseFloat(val);
    } else if (className == Boolean.class) {
      retVal = Boolean.parseBoolean(val);
    } else {
      retVal = val;
    }
    return retVal;
  }

  public static Object getObject(ArrayList<String> arrayList, Class baseType) {
    Object attachment;

    if (baseType == Integer.class) {
      Integer[] objects = new Integer[arrayList.size()];

      Integer strC = 0;
      for (String str : arrayList) {
        objects[strC] = Integer.parseInt(str);
        strC++;
      }

      attachment = objects;

    } else if (baseType == Float.class) {
      Float[] objects = new Float[arrayList.size()];

      Integer strC = 0;
      for (String str : arrayList) {
        objects[strC] = Float.parseFloat(str);
        strC++;
      }

      attachment = objects;

    } else if (baseType == Boolean.class) {
      Boolean[] objects = new Boolean[arrayList.size()];

      Integer strC = 0;
      for (String str : arrayList) {
        objects[strC] = Boolean.parseBoolean(str);
        strC++;
      }

      attachment = objects;

    } else {
      String[] objects = new String[arrayList.size()];

      Integer strC = 0;
      for (String str : arrayList) {
        objects[strC] = str;
        strC++;
      }

      attachment = objects;
    }

    return attachment;
  }
}
