package com.github.vaddipar;

public class Sample {
  private String foo;
  private Integer bar;
  private Boolean tOrF;
  private Float someFloat;
  private Integer[] strs;
  private SubObj someObj;

  public Sample(String foo, Integer bar, Boolean tOrF, Float someFloat, Integer[] strs, SubObj someObj) {
    this.foo = foo;
    this.bar = bar;
    this.tOrF = tOrF;
    this.someFloat = someFloat;
    this.strs = strs;
    this.someObj = someObj;
  }

  public Sample() {
  }

  public String getFoo() {
    return foo;
  }

  public void setFoo(String foo){
    this.foo = foo;
  }

  public Integer getBar() {
    return bar;
  }

  public void setBar(Integer bar){
    this.bar = bar;
  }

  public Boolean getTOrF() {
    return tOrF;
  }

  public void setTOrF(Boolean tOrF) {
    this.tOrF = tOrF;
  }

  public Float getSomeFloat() {
    return someFloat;
  }

  public void setSomeFloat(Float someFloat) {
    this.someFloat = someFloat;
  }

  public Integer[] getStrs() {
    return strs;
  }

  public void setStrs(Integer[] strs) {
    this.strs = strs;
  }

  public SubObj getSomeObj() {
    return someObj;
  }

  public void setSomeObj(SubObj someObj) {
    this.someObj = someObj;
  }

}
