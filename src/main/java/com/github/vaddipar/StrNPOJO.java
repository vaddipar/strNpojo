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
    Object attachment = null;
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

    scanner: while (curIndex.intValue() < srcStr.length()) {
      Character curChar = srcStr.charAt(curIndex.intValue());

      switch (curChar){
        case '{':
          if (key.length() != 0){
            attachment = convert(srcStr, typeHashMap.get(key), curIndex);
          } else{
            curlyOpen++;
          }
          break;
        case '}':
          curlyOpen--;
          break scanner;
        case ' ':
          if (!ignoreSpaces) chars.append(curChar);
          break;
        case '\n':
          if (!ignoreSpaces) chars.append(curChar);
          break;
        case '"':
          ignoreSpaces = !ignoreSpaces;
          break;
        case ',':
          val = new String(chars);
          if (!readingArray && chars.toString().length() != 0) {
            attachment = this.objectGetters.getObject(val, typeHashMap.get(key));
            chars = new StringBuilder();
          } else {
            arrayList.add(val);
            chars = new StringBuilder();
          }
          break;
        case ':':
          key = new String(chars);
          chars = new StringBuilder();
          propertyDescriptor = new PropertyDescriptor(key, dstClass);
          break;
        case '[':
          squareOpen++;
          arrayList = new ArrayList<>();
          readingArray = true;
          break ;
        case ']':
          squareOpen--;
          readingArray = false;
          val = new String(chars);
          chars = new StringBuilder();

          arrayList.add(val);
          attachment = this.objectGetters.getObject(arrayList, typeHashMap.get(key).getComponentType());
          break;
        default:
          chars.append(curChar);
          break;
      }

      if (attachment != null){
        propertyDescriptor.getWriteMethod().invoke(retVal, attachment);
        attachment = null;
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
