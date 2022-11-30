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

import scala.compiletime.summonFrom
import java.util.Locale


inline def summonLocale: Locale =
/*
 * Provider Locale has higher priority
 */
  summonFrom {
    case provider: LocaleProvider =>
      I18nLogger.debug(s"Using provider's locale:${provider.locale}")
      provider.locale
    case l: Locale =>
      I18nLogger.debug(s"Using in-scope locale:${l}")
      l
    case _ => compiletime.error("Locale is missing, please ensure a LocaleProvider or Locale is in scope")
  }

trait LocaleProvider:
  def locale: java.util.Locale
