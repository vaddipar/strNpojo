package com.github.vaddipar;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StrNPOJO {

  private ObjectGetters objectGetters;
  public StrNPOJO(){
    this.objectGetters = new ObjectGetters();
  }

  public Object convert(String srcStr, Class dstClass, AtomicInteger curIndex) throws Exception {

    Object retVal = dstClass.forName(dstClass.getName()).newInstance();
    StringBuilder chars = new StringBuilder();

    String key = "", val;
    ArrayList<String> arrayList = new ArrayList<>();

    Field[] fields = dstClass.getDeclaredFields();
    HashMap<String, Class> typeHashMap = new HashMap<>();

    Boolean ignoreSpaces = true, readingArray = false;
    Integer curlyOpen=0, squareOpen=0;

    for (Field field : fields) {
      typeHashMap.put(field.getName(), field.getType());
    }

    PropertyDescriptor propertyDescriptor = null;

    while (curIndex.intValue() < srcStr.length()) {
      Character curChar = srcStr.charAt(curIndex.intValue());

      if (curChar == '{') {
        if (key.length() != 0){
          Object subObj = convert(srcStr, typeHashMap.get(key), curIndex);
          propertyDescriptor.getWriteMethod().invoke(retVal, subObj);
        } else{
          curlyOpen++;
        }

      } else if(curChar == '}'){
        curlyOpen--;
        break;
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
              .invoke(retVal, this.objectGetters.getObject(val, typeHashMap.get(key)));
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
        squareOpen++;
        arrayList = new ArrayList<>();
        readingArray = true;

      } else if (curChar == ']') {
        squareOpen--;
        readingArray = false;

        val = new String(chars);
        chars = new StringBuilder();
        arrayList.add(val);

        Class baseType = typeHashMap.get(key).getComponentType();
        Object attachment = this.objectGetters.getObject(arrayList, baseType);
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
          .invoke(retVal, this.objectGetters.getObject(val, typeHashMap.get(key)));
    }

    if (curlyOpen != 0){
      throw new Exception(String.format("Malformed JSON. Missing } at "+ curIndex.intValue()));
    } else if (squareOpen != 0){
      throw new Exception(String.format("Malformed JSON. Missing ] before end of string"));
    }

    return retVal;
  }
}
