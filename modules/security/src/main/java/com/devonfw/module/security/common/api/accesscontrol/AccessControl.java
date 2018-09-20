package com.devonfw.module.security.common.api.accesscontrol;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This is the abstract base class for a node of the {@link AccessControlSchema} that represents a tree of
 * {@link AccessControlGroup}s and {@link AccessControlPermission}s. If a {@link java.security.Principal} "has" a
 * {@link AccessControl} he also "has" all {@link AccessControl}s with according permissions in the spanned sub-tree.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AccessControl implements Serializable {

  /** UID for serialization. */
  private static final long serialVersionUID = 1L;

  /** @see #getId() */
  @XmlID
  @XmlAttribute(name = "id", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NCName")
  private String id;

  /**
   * The constructor.
   */
  public AccessControl() {

    super();
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() ID}.
   */
  public AccessControl(String id) {

    super();
    this.id = id;
  }

  /**
   * @return the unique identifier of this {@link AccessControl}. Has to be unique for all {@link AccessControl} in a
   *         {@link AccessControlSchema}.
   */
  public String getId() {

    return this.id;
  }

  /**
   * @param id the new {@link #getId() id}.
   */
  public void setId(String id) {

    this.id = id;
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.id);
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
    AccessControl other = (AccessControl) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {

    return this.id;
  }

}
