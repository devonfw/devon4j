package ${package}.general.common.security.accesscontrol;

import java.util.Objects;

/**
 * This is the abstract base class for an access control. It is either a {@link AccessControlGroup} or a
 * {@link AccessControlPermission}. If a {@link java.security.Principal} "has" a {@link AccessControl} he also "has" all
 * {@link AccessControl}s with according permissions in the spanned sub-tree.
 */
public abstract class AccessControl {

  /** @see #getId() */
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
   * @return the unique identifier of this {@link AccessControl}.
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
