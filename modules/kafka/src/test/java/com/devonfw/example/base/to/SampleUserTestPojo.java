package com.devonfw.example.base.to;

/**
 * @author ravicm
 *
 */
public class SampleUserTestPojo {

  private String name;

  private String contactNo;

  private String address;

  @Override
  public String toString() {

    return "SampleUserTestPojo [name=" + this.name + ", contactNo=" + this.contactNo + ", address=" + this.address
        + "]";
  }

  @Override
  public int hashCode() {

    // TODO Auto-generated method stub
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    // TODO Auto-generated method stub
    return super.equals(obj);
  }

  /**
   * The constructor.
   * 
   * @param name
   * @param contactNo
   * @param address
   */
  public SampleUserTestPojo(String name, String contactNo, String address) {

    super();
    this.name = name;
    this.contactNo = contactNo;
    this.address = address;
  }

  /**
   * The constructor.
   */
  public SampleUserTestPojo() {

    super();
  }

  /**
   * @return name
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getname}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return contactNo
   */
  public String getContactNo() {

    return this.contactNo;
  }

  /**
   * @param contactNo new value of {@link #getcontactNo}.
   */
  public void setContactNo(String contactNo) {

    this.contactNo = contactNo;
  }

  /**
   * @return address
   */
  public String getAddress() {

    return this.address;
  }

  /**
   * @param address new value of {@link #getaddress}.
   */
  public void setAddress(String address) {

    this.address = address;
  }

}
