package com.devonfw.example.base.to;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.devonfw.module.kafka.common.messaging.impl.MessageSenderImpl;

/**
 * A sample User Pojo class used to test {@link MessageSenderImpl} and used a value for {@link ProducerRecord}.
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

    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    return super.equals(obj);
  }

  /**
   * The constructor.
   *
   * @param name the name
   * @param contactNo contactNo
   * @param address the address
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
   * The name.
   *
   * @return name
   */
  public String getName() {

    return this.name;
  }

  /**
   * Set the name for {@link #getName()}
   *
   * @param name the name.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * The ContactNo.
   *
   * @return contactNo
   */
  public String getContactNo() {

    return this.contactNo;
  }

  /**
   * Set the Contactno for {@link #getContactNo()}.
   *
   * @param contactNo contact number.
   */
  public void setContactNo(String contactNo) {

    this.contactNo = contactNo;
  }

  /**
   * The Address.
   *
   * @return address
   */
  public String getAddress() {

    return this.address;
  }

  /**
   * Set the address for {@link #getAddress()}
   *
   * @param address the address.
   */
  public void setAddress(String address) {

    this.address = address;
  }

}
