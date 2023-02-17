/*
 * Copyright 2022 Greenfossil Pte Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greenfossil.commons

import munit.FunSuite

import java.util.Locale
import java.util.Locale.LanguageRange
import scala.jdk.CollectionConverters.*

class LocaleUtilSuite extends FunSuite{

  test("Get best match"){
    val acceptedLanguages = LanguageRange.parse("en-SG;q=1.0,zh-CN;q=0.5,fr-CA;q=0.0").asScala.toList
    val availableLanguages = Seq("zh", "en", "kr").map(x => new Locale(x))

    val bestMatch = LocaleUtil.getBestMatchLocale(acceptedLanguages, availableLanguages, None)
    assertEquals(bestMatch, new Locale("en"))
  }

  test("Get best match with variant"){
    val acceptedLanguages = LanguageRange.parse("en-SG;q=1.0,zh-CN;q=0.5,fr-CA;q=0.0").asScala.toList
    val availableLanguages = Seq("zh", "en", "kr").map(x => new Locale(x))

    val bestMatch = LocaleUtil.getBestMatchLocale(acceptedLanguages, availableLanguages, Some("elemx"))
    assertEquals(bestMatch, new Locale("en", "", "elemx"))
  }

  test("Get best match with region variant"){
    val acceptedLanguages = LanguageRange.parse("en-SG;q=1.0,zh-CN;q=0.5,fr-CA;q=0.0").asScala.toList
    val availableLanguageTags = Seq("zh-CN", "en-SG")

    val bestMatch = LocaleUtil.getBestMatchFromLanguageTags(acceptedLanguages, availableLanguageTags, Some("elemx"))
    assertEquals(bestMatch, new Locale("en", "SG", "elemx"))
  }

  test("Get best match where language tag has no match with available languages") {
    val acceptedLanguages = LanguageRange.parse("zh-CN").asScala.toList
    val availableLanguageTags = Seq("en-SG")

    val bestMatch = LocaleUtil.getBestMatchFromLanguageTags(acceptedLanguages, availableLanguageTags, Some("elemx"))
    assertEquals(bestMatch, new Locale("en", "SG", "elemx"))
  }

}
