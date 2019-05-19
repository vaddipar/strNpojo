package com.vaddipar.json;

public class SubObj {
  private Integer[] sandeep;
  private Vaddipar duck;

  public Vaddipar getDuck() {
    return duck;
  }

  public void setDuck(Vaddipar duck) {
    this.duck = duck;
  }

  public SubObj(Integer[] sandeep, Vaddipar duck) {
    this.sandeep = sandeep;
    this.duck = duck;
  }

  public SubObj() {}

  public Integer[] getSandeep() {
    return sandeep;
  }

  public void setSandeep(Integer[] sandeep) {
    this.sandeep = sandeep;
  }
}
