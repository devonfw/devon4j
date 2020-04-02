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
package com.devonfw.module.i18n.logic.impl;

import org.junit.jupiter.api.Test;

import com.devonfw.module.i18n.locale.api.I18n;
import com.devonfw.module.i18n.locale.impl.I18nImpl;
import com.devonfw.module.i18n.locale.impl.LocaleNlsResourceImpl;
import com.devonfw.module.i18n.locale.impl.LocaleResourceImpl;
//import com.devonfw.module.i18n.common.I18nTestApp;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Test cases for i18n (Internationalization)
 *
 */
public class I18nImplTest extends ComponentTest {

  /**
   * @throws Exception thrown by testlanguageFiles
   */
  @SuppressWarnings("unused")
  @Test
  public void testlanguageFiles() throws Exception {

    I18n i18nDefaultImpl = new I18nImpl(new LocaleResourceImpl());
    I18n i18nNlsImpl = new I18nImpl(new LocaleNlsResourceImpl());
    // given
    assertThat(i18nDefaultImpl).isNotNull();
    assertThat(i18nNlsImpl).isNotNull();

    // With default implementation

    String strWholeFile = i18nDefaultImpl.getResourceObject("en_US", "");
    assertThat(strWholeFile).isNotNull();

    String strKeyValue = i18nDefaultImpl.getResourceObject("en_US", "i18n.msg.helloworld"); // when
    assertThat(strKeyValue).isNotNull(); // then
    assertThat(strKeyValue).isEqualTo("{\"i18n\":{\"msg\":{\"helloworld\":\"Hello World\"}}}");

    String strUnknownkey = i18nDefaultImpl.getResourceObject("en_US", "unknownkey"); // when
    assertThat(strUnknownkey).isNotNull(); // then assertThat(strUnknownkey).isEqualTo("{}");

    String strWholeFile_DE = i18nDefaultImpl.getResourceObject("de_DE", "");
    assertThat(strWholeFile_DE).isNotNull();
    // With MMM implementation

    String strMmmWholeFile = i18nNlsImpl.getResourceObject("en_US", "");
    assertThat(strMmmWholeFile).isNotNull();

    String strMmmKeyValue = i18nNlsImpl.getResourceObject("en_US", "getLocale"); // when
    assertThat(strMmmKeyValue).isNotNull();
    // then
    assertThat(strMmmKeyValue)
        .isEqualTo("{\"getLocale\":\"TODO(en):{name}. This Module is related to internationalization\"}");

    String strMmmUnknownkey = i18nNlsImpl.getResourceObject("en_US", "unknownkey"); // when
    assertThat(strMmmUnknownkey).isNotNull(); // then assertThat(strMmmUnknownkey).isEqualTo("{}");

  }
};
