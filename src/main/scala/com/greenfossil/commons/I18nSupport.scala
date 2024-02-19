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



object I18nSupport:

  type LocaleLike = Locale | LocaleProvider

  extension (ll: LocaleLike) def locale: Locale = toLocale(ll)

  /**
   *
   * @param localeLike
   * @return
   */
  def toLocale(localeLike: LocaleLike): Locale =
    localeLike match
      case l : Locale => l
      case p: LocaleProvider => p.locale

trait I18nSupport:

  export I18nSupport.*

  /**
    * I18NFILENAME can be a list of basenames either comma or space separated
    */
  lazy val I18NFILENAME: String = DefaultConfig().getStringOpt("app.i18n.resourcebundle.basename").getOrElse("messages")

  lazy val bundle = MultiPropertyResourceBundle(I18NFILENAME.split("\\s*,\\s*|\\s+") *)

  lazy val supportedLanguagesForMultiLang: Seq[String] =
    import scala.jdk.CollectionConverters.*
    DefaultConfig().getStringList("app.i18n.multi.showLang").asScala.toList

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
  def i18n(key: String, args: Any*)(using localeLike: LocaleLike): String =
    i18nWithDefault(key, key, args*)

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
  def i18nWithDefault(key: String, defaultValue: String, args: Any*)(using localeLike: LocaleLike): String =
    bundle.i18nWithDefault(i18nMessageFn, key, defaultValue, args*)

  /**
   *
   * @param keys
   * @param args
   * @param localeLike
   * @return
   */
  def i18n(keys: Seq[String], args: Any*)(using localeLike: LocaleLike): String =
    (for {
      key <- keys
    } yield bundle.i18nWithDefault(i18nMessageFn, key, key, args *)).mkString(",")

  /**
    * Dumps the resource bundles bases on the locale
    * @param locale
    * @return - a seq of resource bundles based on the search order of key
    */
  def dumpBundles(using localeLike: LocaleLike): Seq[(LocaleLike, Seq[(URL, PropertyResourceBundle)])] =
    bundle.dumpBundles

  /**
   *
   * @param variant - must be 5 to 8 characters
   * @param langTag - e.g. en_SG
   * @return locale
   */
  def createLocaleForLang(variant: String, langTag: String): Locale =
    if variant.nonEmpty then
      require(variant.length >= 5 && variant.length <= 8, s"variant [$variant] length:${variant.length}, it must be between 5 and 8")

    Option(variant).filter(_.nonEmpty).map { variant =>
      Locale.Builder().setLanguageTag(langTag).setVariant(variant).build()
    }.getOrElse(Locale.Builder().setLanguageTag(langTag).build())

  /**
   *
   * @param i18nKey
   * @param defaultValue
   * @return if multiple languages are found, concatenates them with a space
   */
  def i18nGetMultiLang(i18nKey: String, defaultValue: String): String =
    i18nGetMultiLang(i18nKey, defaultValue, " ")

  /**
   *
   * @param i18nKey
   * @param defaultValue
   * @param separator
   * @return if multiple languages are found, concatenates them with indicated separator
   */
  def i18nGetMultiLang(i18nKey: String, defaultValue: String, separator : String): String =
    i18nGetListOfTranslationOfLangs(i18nKey, defaultValue).map(_._2).distinct.mkString(separator)

  /**
   *
   * @param supportedLanguages - e.g. Seq("en", "zh")
   * @param i18nKey
   * @param defaultValue
   * @return if multiple languages are found, concatenates them with a space
   */
  def i18nGetMultiLang(supportedLanguages: Seq[String], i18nKey: String, defaultValue: String): String =
    i18nGetMultiLang(supportedLanguages, i18nKey, defaultValue, " ")

  /**
   *
   * @param supportedLanguages - e.g. Seq("en", "zh")
   * @param i18nKey
   * @param defaultValue
   * @param separator
   * @return if multiple languages are found, concatenates them with indicated separator
   */
  def i18nGetMultiLang(supportedLanguages: Seq[String], i18nKey: String, defaultValue: String, separator: String): String =
    i18nGetListOfTranslationOfLangs(supportedLanguages, i18nKey, defaultValue).map(_._2).distinct.mkString(separator)



  /**
   *
   * @param i18nKey
   * @param defaultValue
   * @return
   */
  def i18nGetListOfTranslationOfLangs(i18nKey: String, defaultValue: String): Seq[(String, String)] =
    i18nGetListOfTranslationOfLangs(supportedLanguagesForMultiLang, i18nKey, defaultValue)

  /**
   *
   * @param supportedLanguages - e.g. Seq("en", "zh")
   * @param i18nKey
   * @param defaultValue
   * @return (lang, i18nKey's value)
   */
  def i18nGetListOfTranslationOfLangs(supportedLanguages: Seq[String], i18nKey: String, defaultValue: String): Seq[(String, String)] =
    val variant = DefaultConfig().getString("app.i18n.variant")
    supportedLanguages.map { lang =>
      //Create the locale for supported lang
      val locale = createLocaleForLang(variant, lang)
      lang -> i18nGetMultiLangOfLocale(locale, lang, i18nKey, defaultValue)
    }.filter(_._2.nonEmpty)

  /**
   *
   * @param locale
   * @param lang
   * @param i18nKey
   * @param defaultValue
   * @return
   */
  def i18nGetMultiLangOfLocale(locale: Locale, lang: String, i18nKey: String, defaultValue: String): String =
    val translatedValue = i18nWithDefault(i18nKey, defaultValue)(using locale)
    I18nLogger.debug(s"lang = $lang, value = ${translatedValue}, locale = $locale")
    translatedValue


  /**
    * Clear bundleCache
    */
  def clear(): Unit = bundle.clear()

end I18nSupport

