package com.devonfw.module.security.common.api.accesscontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents the security configuration for the mapping of {@link AccessControlGroup}s to
 * {@link AccessControlPermission}s. Everything is properly annotated for JAXB (de)serialization from/to XML.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "access-control-schema")
public class AccessControlSchema {

  /** @see #getGroups() */
  @XmlElement(name = "group")
  private List<AccessControlGroup> groups;

  /**
   * The constructor.
   */
  public AccessControlSchema() {

    super();
  }

  /**
   * @return the {@link List} of {@link AccessControlGroup}s contained in this {@link AccessControlSchema}.
   */
  public List<AccessControlGroup> getGroups() {

    if (this.groups == null) {
      this.groups = new ArrayList<>();
    }
    return this.groups;
  }

  /**
   * @param groups the new {@link #getGroups() groups}.
   */
  public void setGroups(List<AccessControlGroup> groups) {

    this.groups = groups;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.groups == null) ? 0 : this.groups.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AccessControlSchema other = (AccessControlSchema) obj;
    if (!Objects.equals(this.groups, other.groups)) {
      return false;
    }
    return true;
  }

}
