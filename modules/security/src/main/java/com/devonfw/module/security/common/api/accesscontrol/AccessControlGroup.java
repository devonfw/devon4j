package com.devonfw.module.security.common.api.accesscontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

/**
 * A {@link AccessControlGroup} represents a collection of {@link AccessControlPermission permissions}. A security
 * administrator assigns a {@link java.security.Principal user} to a {@link AccessControlGroup group} to grant him the
 * {@link AccessControlPermission permissions} of that {@link AccessControlGroup group}.<br/>
 * Please note that a <em>role</em> is a special form of a {@link AccessControlGroup group} that also represents a
 * strategic function. Therefore not every {@link AccessControlGroup group} is a role. Often a user can only have one
 * role or can only act under one role at a time. Unfortunately these terms are often mixed up what is causing
 * confusion.
 *
 */
@XmlRootElement(name = "group")
public class AccessControlGroup extends AccessControl { // implements java.security.acl.Group {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /** @see #getInherits() */
  @XmlIDREF
  @XmlElementWrapper(name = "inherits")
  @XmlElement(name = "group-ref")
  private List<AccessControlGroup> inherits;

  /** @see #getPermissions() */
  @XmlElementWrapper(name = "permissions")
  @XmlElement(name = "permission")
  private List<AccessControlPermission> permissions;

  /** @see #getType() */
  @XmlAttribute(name = "type", required = false)
  @XmlSchemaType(name = "string")
  private String type;

  /**
   * The constructor.
   */
  public AccessControlGroup() {

    super();
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() ID}.
   */
  public AccessControlGroup(String id) {

    super(id);
  }

  /**
   * @return the type of this group. E.g. "role", "department", "use-case-group", etc. You can use this for your own
   *         purpose.
   */
  public String getType() {

    if (this.type == null) {
      return "";
    }
    return this.type;
  }

  /**
   * @param type the type to set
   */
  public void setType(String type) {

    this.type = type;
  }

  /**
   * @return inherits
   */
  public List<AccessControlGroup> getInherits() {

    if (this.inherits == null) {
      this.inherits = new ArrayList<>();
    }
    return this.inherits;
  }

  /**
   * @param inherits the inherits to set
   */
  public void setInherits(List<AccessControlGroup> inherits) {

    this.inherits = inherits;
  }

  /**
   * @return the {@link List} of {@link AccessControlPermission}s.
   */
  public List<AccessControlPermission> getPermissions() {

    if (this.permissions == null) {
      this.permissions = new ArrayList<>();
    }
    return this.permissions;
  }

  /**
   * @param permissions the new {@link #getPermissions() permissions}.
   */
  public void setPermissions(List<AccessControlPermission> permissions) {

    this.permissions = permissions;
  }

  @Override
  public int hashCode() {

    return Objects.hash(super.hashCode(), this.type);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    AccessControlGroup other = (AccessControlGroup) obj;
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    // other attributes may be mutable and id should already be unique...
    return true;
  }

}
