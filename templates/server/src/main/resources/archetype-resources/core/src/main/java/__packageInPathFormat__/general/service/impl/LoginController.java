package ${package}.general.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for Login-Page.
 */
@Controller
public class LoginController {

  /**
   * Default URL to redirect to after successfully login.
   */
  public final static String defaultTargetUrl = "/";

  /**
   * Builds the model for the login page---mainly focusing on the error message handling.
   *
   * @param authentication_failed flag for authentication failed
   * @param invalid_session flag for invalid session
   * @param access_denied flag for access denied
   * @param logout flag for successful logout
   * @return the view model
   */
  @RequestMapping(value = "/login**", method = {RequestMethod.GET,RequestMethod.POST})
  public ModelAndView login(
      @RequestParam(value = "authentication_failed", required = false) boolean authentication_failed,
      @RequestParam(value = "invalid_session", required = false) boolean invalid_session,
      @RequestParam(value = "access_denied", required = false) boolean access_denied,
      @RequestParam(value = "logout", required = false) boolean logout) {

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!authentication.getPrincipal().equals("anonymousUser")) {
      return new ModelAndView("redirect:" + defaultTargetUrl);
    }

    ModelAndView model = new ModelAndView();
    if (authentication_failed) {
      model.addObject("error", "Authentication failed!");
    } else if (invalid_session) {
      model.addObject("error", "You are currently not logged in!");
    } else if (access_denied) {
      model.addObject("error", "You have insufficient permissions to access this page!");
    } else if (logout) {
      model.addObject("msg", "Logout successful!");
    }

    return model;
  }

}
