package ${package}.general.common.api;

import net.sf.mmm.util.nls.api.NlsBundle;
import net.sf.mmm.util.nls.api.NlsBundleMessage;
import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * This is the {@link NlsBundle} for this application.
 */
public interface NlsBundleApplicationRoot extends NlsBundle {

  /**
   * @see ${package}.general.common.api.exception.NoActiveUserException
   *
   * @return the {@link NlsMessage}.
   */
  @NlsBundleMessage("There is currently no user logged in")
  NlsMessage errorNoActiveUser();

}
