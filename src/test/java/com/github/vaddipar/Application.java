package com.github.vaddipar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
  public static void main(String[] args) {
    StrNPOJO str2POJO = new StrNPOJO();
    String sampleStr = "";
    String fileName = "/Users/vaddipar/personal/repos/vaddipar/strNpojo/src/test/resources/sample.json";

    try{
      sampleStr = new String(Files.readAllBytes(Paths.get(fileName)), "UTF-8");
    } catch(FileNotFoundException noFileFound){
      noFileFound.printStackTrace();
    } catch (IOException ioExcept){
      ioExcept.printStackTrace();
    }

    try{
      Sample retObject = (Sample) str2POJO.convert( sampleStr, Sample.class, new AtomicInteger(0));
      System.out.println(retObject.getFoo());
      System.out.println(retObject.getBar());
      System.out.println(retObject.getSomeFloat());
      System.out.println(retObject.getTOrF());
      Integer[] fooBar = retObject.getStrs();
      for (Integer str :fooBar) {
        System.out.println(str);
      }

      Integer[] foopa2 = retObject.getSomeObj().getSandeep();
      for (Integer str :foopa2) {
        System.out.println(str);
      }

      Vaddipar duck = retObject.getSomeObj().getDuck();
      System.out.println(duck.getSrivastav());
    } catch (Exception e){
      e.printStackTrace();
    }
  }
}
