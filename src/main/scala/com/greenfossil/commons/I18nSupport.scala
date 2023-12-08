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

import com.typesafe.config.*
import org.slf4j.LoggerFactory

import java.net.URL
import java.util.{Locale, PropertyResourceBundle, ResourceBundle}

private [commons] val I18nLogger = LoggerFactory.getLogger("com.greenfossil.commons.i18n")

trait I18nSupport:

  type LocaleLike = Locale | LocaleProvider

  /**
    * I18NFILENAME can be a list of basenames either comma or space separated
    */
  lazy val I18NFILENAME: String = DefaultConfig().getStringOpt("app.i18n.resourcebundle.basename").getOrElse("messages")

  lazy val bundle = MultiPropertyResourceBundle(I18NFILENAME.split("\\s*,\\s*|\\s+") *)

  def i18nMessageFn(message: String, key: String, localeLike: LocaleLike): String = message

  /**
    * Search of the key is based on the order or baseNames.
    * If a baseName exists in other jars, the order of the key retrievable is based on
    * the order of the jars defined in the classpath
    *
    * @param key
    * @param args - used as format args, if value exists
    * @param locale
    * @return If key is not found, returns the key
    */
  inline def i18n(key: String, args: Any*): String =
    bundle.i18nWithDefault(key, key, args*)(using summonLocale)

  /**
    * Search of the key is based on the order or baseNames.
    * If a baseName exists in other jars, the order of the key retrievable is based on
    * the order of the jars defined in the classpath
    *
    * @param key
    * @param defaultValue
    * @param args - used as format args, if value exists
    * @param locale
    * @returnIf key is not found, returns defaultValue
    */
  inline def i18nWithDefault(key: String, defaultValue: String, args: Any*): String =
    bundle.i18nWithDefault(i18nMessageFn, key, defaultValue, args*)(using summonLocale)

  inline def i18n(keys: Seq[String], args: Any*): String =
    val localeLike = summonLocale
    (
      for {
        key <- keys
      } yield bundle.i18nWithDefault(i18nMessageFn, key, key, args *)(using localeLike)
      ).mkString(",")

  /**
    * Dumps the resource bundles bases on the locale
    * @param locale
    * @return - a seq of resource bundles based on the search order of key
    */
  inline def dumpBundles: Seq[(LocaleLike, Seq[(URL, PropertyResourceBundle)])] =
    bundle.dumpBundles(using summonLocale)

  /**
    * Clear bundleCache
    */
  def clear(): Unit = bundle.clear()

end I18nSupport

