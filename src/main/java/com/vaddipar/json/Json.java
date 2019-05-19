package com.vaddipar.json;

import sun.net.idn.StringPrep;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Json {
  private String sandeep;
  private static Object actualConvert(Object retVal, int index, String srcStr, ArrayList<String> fieldNames){
    for (int i = index; i < srcStr.length(); i++) {
      if(srcStr[i]  == "{"){
        Json.actualConvert(retVal, index, )
      }
    }
  }

  public static Object stringToPOJO(String srcStr, Class dstClass){
    Field[] fields = dstClass.getDeclaredFields();
    ArrayList<String> fieldNames = new ArrayList<String>();
    Object retVal;

    try{
      retVal = dstClass.forName(dstClass.getName()).newInstance();
      System.out.println(fields.length);
      for (Field field : fields) {
        fieldNames.add(field.getName());
      }
      retVal = Json.actualConvert(retVal, 0, srcStr, fieldNames);
      return retVal;
    }  catch (ClassNotFoundException classNotFound){
      classNotFound.printStackTrace();
    } catch (IllegalAccessException illegalAccess){
      illegalAccess.printStackTrace();
    } catch (InstantiationException instantiationException){
      instantiationException.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args) {
    Json.stringToPOJO("foo", Json.class);
  }
}
