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

import java.net.URL
import java.util.{Locale, PropertyResourceBundle, ResourceBundle}
import scala.util.{Try, Using}
import scala.util.chaining.scalaUtilChainingOps

case class MultiPropertyResourceBundle(baseNames: String*):

  import scala.jdk.CollectionConverters.*

  /**
    * Feature to retrieve and show the key beside the value if enabled
    */
  lazy val SHOWI18NKEYS: Boolean = DefaultConfig().getBooleanOpt("app.i18n.showI18nKeys").getOrElse(false)

  private val control = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES)

  private val bundleCache = collection.mutable.Map.empty[(Locale, URL), PropertyResourceBundle]

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
  def i18n(key: String, args: Any*)(using locale: Locale): String =
    i18nWithDefault(key, "", args *)

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
  def i18nWithDefault(key: String, defaultValue: String, args: Any*)(using locale: Locale): String =
    getBundles.flatMap { case (locale, tup2List) =>
      tup2List.flatMap {
        case (url, bundle) =>
          Try {
            if SHOWI18NKEYS then
              val value = bundle.getString(key)
              new java.text.MessageFormat(s"$value [$key]", locale).format(args.toArray)
            else
              val value = bundle.getString(key)
              new java.text.MessageFormat(value, locale).format(args.toArray)
            val value = bundle.getString(key)
            new java.text.MessageFormat(value, locale).format(args.toArray)
          }.toOption.tap { opt =>
            I18nLogger.debug(s"path:${url.getPath} - key/value $key:$opt")
          }
      }
    }.headOption.getOrElse(defaultValue)

  /**
    * @param locale
    * @return - a seq of resource bundles based on the search order of key
    */
  private def getBundles(using locale: Locale): Seq[(Locale, Seq[(URL, PropertyResourceBundle)])] =
    Try {
      val candidateLocales = control.getCandidateLocales("", locale).asScala

      val resourceURLsGroupByLocale = candidateLocales.map { candidateLocale =>
        candidateLocale -> baseNames.flatMap { name =>
          val bundleName = name + (if (candidateLocale.toString.isEmpty) ".properties" else "_" + candidateLocale.toString + ".properties")
          val classLoader = Option(Thread.currentThread().getContextClassLoader).getOrElse(ClassLoader.getSystemClassLoader)
          val urls = classLoader.getResources(bundleName).asScala.toSeq
          I18nLogger.debug(s"basename-locale = ${name} - [$candidateLocale], bundle=$bundleName, urls.size = ${urls.size}")
          urls
        }
      }

      resourceURLsGroupByLocale.map {
        case (locale, urls) =>
          locale -> urls.map { url =>
            val bundle = bundleCache.getOrElseUpdate(locale -> url, {
              Using.resource(url.openStream()) { is =>
                new PropertyResourceBundle(is)
              }
            })
            url -> bundle
          }
      }.toSeq
    }.fold(
      exception => {
        I18nLogger.error("Unable to get bundles", exception)
        Nil
      },
      result => result
    )


  /**
    * Dumps the resource bundles bases on the locale
    *
    * @param locale
    * @return - a seq of resource bundles based on the search order of key
    */
  def dumpBundles(using locale: Locale): Seq[(Locale, Seq[(URL, PropertyResourceBundle)])] = {
    val bundles = getBundles
    bundles.foreach {
      case (l, xs) =>
        xs.zipWithIndex.foreach {
          case ((url, bundle), localeIndex) =>
            val sortedKeys = bundle.getKeys.asScala.toSeq.sorted
            I18nLogger.debug(s"Bundle locale suffix:[${l}] #${localeIndex + 1}, keys:${sortedKeys.size}, url:${url.getPath}")
            sortedKeys.zipWithIndex.foreach { case (key, index) =>
              I18nLogger.debug(s"$index - ${key}=${bundle.getString(key)}")
            }
            I18nLogger.debug("---")
        }
    }
    bundles
  }

  /**
    * Clear bundleCache
    */
  def clear(): Unit = bundleCache.clear()

end MultiPropertyResourceBundle
