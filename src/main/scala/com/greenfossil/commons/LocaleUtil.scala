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

import java.util.Locale
import java.util.Locale.{Builder, LanguageRange}


trait LocaleUtil:
  /**
   * Get best matched locale from the accepted language ranges and available language tags. 
   *
   * @param acceptedLanguageRange Accepted Language Ranges
   * @param availableLanguageTags Available Language Tags, e.g. "en-SG", "en-US", "zh-CN"
   * @param variantOpt            Locale variant
   */
  def getBestMatchFromLanguageTags(acceptedLanguageRange: Seq[LanguageRange], availableLanguageTags: Seq[String], variantOpt: Option[String]): Locale =
    val availableLocales = availableLanguageTags.map(tag => new Locale.Builder().setLanguageTag(tag).build())
    getBestMatchLocale(acceptedLanguageRange, availableLocales, variantOpt)

  /**
   * Get best matched locale from the accepted language ranges and available locales.
   */
  def getBestMatchLocale(acceptedLanguageRanges: Seq[LanguageRange], availableLocales: Seq[Locale], variantOpt: Option[String]): Locale =
    import scala.jdk.CollectionConverters.*
    val bestMatchLocale = Option(Locale.lookup(acceptedLanguageRanges.asJava, availableLocales.asJava)).getOrElse(Locale.getDefault)
    new Builder().setLocale(bestMatchLocale).setVariant(variantOpt.getOrElse("")).build()

object LocaleUtil extends LocaleUtil
