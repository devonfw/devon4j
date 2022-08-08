package ${package}.general.common.security.accesscontrol;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A {@link AccessControlPermission} represents an atomic permission of the application. Each operation (use-case)
 * should have its own {@link AccessControlPermission permission}. These operations are secured referencing the
 * {@link #getId() ID} of the {@link AccessControlPermission permission}. We do this by annotating the operation method
 * with {@link javax.annotation.security.RolesAllowed} (from JSR 250). Please do not get confused by the name
 * {@link javax.annotation.security.RolesAllowed} as we are not assigning roles (see also {@link AccessControlGroup})
 * but {@link AccessControlPermission permissions} instead. We want to use Java standards (such as
 * {@link javax.annotation.security.RolesAllowed}) where suitable but assigning the allowed roles to a method would end
 * up in unmaintainable system configurations if your application reaches a certain complexity.<br/>
 * <br/>
 * If a user is logged in and wants to invoke the operation he needs to own the required permission. Therefore his
 * {@link AccessControlGroup}s (resp. roles) have to contain the {@link AccessControlPermission permission}
 * {@link AccessControlGroup#getPermissions() directly} or {@link AccessControlGroup#getInherits() indirectly}.<br/>
 * In order to avoid naming clashes you should use the name of the application component as prefix of the permission.
 */
@XmlRootElement(name = "permission")
public class AccessControlPermission extends AccessControl {

  /**
   * The constructor.
   */
  public AccessControlPermission() {

    super();
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() ID}.
   */
  public AccessControlPermission(String id) {

    super(id);
  }

}
