/*******************************************************************************
 * Copyright 2015-2018 Capgemini SE.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/
package com.devonfw.module.i18n.common.api.nls;

import javax.inject.Named;

import net.sf.mmm.util.nls.api.NlsBundle;
import net.sf.mmm.util.nls.api.NlsBundleMessage;
import net.sf.mmm.util.nls.api.NlsMessage;

/**
 * This is the {@link NlsBundle} for this application.
 *
 * @author kugawand
 * @since dev
 *
 */

public interface NlsBundleI18nRoot extends NlsBundle {
  /**
   * @param name
   * @return
   */
  @SuppressWarnings("javadoc")
  @NlsBundleMessage("{name}. This Module is related to internationalization ")
  public NlsMessage getLocale(@Named("name") String name);

  /**
   * @param name
   * @return
   */
  @SuppressWarnings("javadoc")
  @NlsBundleMessage("Hello {name}")
  NlsMessage messageSayHi(@Named("name") String name);

  @NlsBundleMessage("Sorry. The login \"{login}\" is already in use. Please choose a different login.")
  NlsMessage errorLoginInUse(@Named("login") String login);
}
