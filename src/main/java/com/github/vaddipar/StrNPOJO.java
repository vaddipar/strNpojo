package com.github.vaddipar;

import com.github.vaddipar.utilities.ObjectGetters;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StrNPOJO {

  public Object convert(String srcStr, Class dstClass, AtomicInteger curIndex) throws Exception {

    Object retVal = dstClass.forName(dstClass.getName()).newInstance();
    StringBuilder chars = new StringBuilder();

    String key = "", val;
    ArrayList<String> arrayList = new ArrayList<>();

    Field[] fields = dstClass.getDeclaredFields();
    HashMap<String, Class> typeHashMap = new HashMap<>();

    Boolean ignoreSpaces = true, readingArray = false;

    for (Field field : fields) {
      typeHashMap.put(field.getName(), field.getType());
    }

    PropertyDescriptor propertyDescriptor = null;

    while (curIndex.intValue() < srcStr.length() && srcStr.charAt(curIndex.intValue()) != '}') {
      Character curChar = srcStr.charAt(curIndex.intValue());

      if (curChar == '{') {
        curIndex.set(curIndex.intValue() + 1);
        Object subObj = convert(srcStr, typeHashMap.get(key), curIndex);
        propertyDescriptor.getWriteMethod().invoke(retVal, subObj);

      } else if ((curChar == ' ') || (curChar == '\n')) {
        // ignore whitespaces everywhere except inside the key and value name.
        if (!ignoreSpaces) {
          chars.append(curChar);
        }
      } else if (curChar == '"') {
        ignoreSpaces = !ignoreSpaces;
      } else if (curChar == ',') {

        val = new String(chars);

        if (!readingArray && chars.toString().length() != 0) {
          propertyDescriptor
              .getWriteMethod()
              .invoke(retVal, ObjectGetters.getObject(val, typeHashMap.get(key)));
          chars = new StringBuilder();
        } else {
          arrayList.add(val);
          chars = new StringBuilder();
        }

      } else if (curChar == ':') {

        key = new String(chars);
        chars = new StringBuilder();
        propertyDescriptor = new PropertyDescriptor(key, dstClass);

      } else if (curChar == '[') {
        arrayList = new ArrayList<>();
        readingArray = true;

      } else if (curChar == ']') {
        readingArray = false;

        val = new String(chars);
        chars = new StringBuilder();
        arrayList.add(val);

        Class baseType = typeHashMap.get(key).getComponentType();
        Object attachment = ObjectGetters.getObject(arrayList, baseType);
        propertyDescriptor.getWriteMethod().invoke(retVal, attachment);

      } else {
        chars.append(curChar);
      }

      curIndex.set(curIndex.intValue() + 1);
    }

    val = chars.toString();
    if (val.length() != 0) {
      propertyDescriptor
          .getWriteMethod()
          .invoke(retVal, ObjectGetters.getObject(val, typeHashMap.get(key)));
    }

    // todo: add cases here to fail malformed JSON's

    return retVal;
  }
}
