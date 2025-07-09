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

/**
  * Created by chungonn on 11/7/16.
  */
class I18nSuite extends munit.FunSuite  {

  val obj = new I18nSupport {
    //File messages-test is at /test/resources folder
    override lazy val I18NFILENAME: String = "messages-test,messages-test2"
  }

  import obj.*

  test("test english locale") {
    val locale = Locale.ENGLISH
    assertNoDiff(locale.toLanguageTag, "en")
    assertNoDiff(Locale.getDefault.getLanguage, "en")
  }

  test("test en_SG locale"){
    val locale = Locale.of("en", "SG")
    assertNoDiff(locale.toLanguageTag, "en-SG")
    assertNoDiff(Locale.getDefault.getLanguage, "en")
  }

  test("non existing key"){
    given Locale = Locale.ENGLISH
    assertNoDiff(i18n("nokey"), "nokey")
  }

  test("existing key") {
    given Locale = Locale.ENGLISH
    assertNoDiff(i18n("home.title"), "Home Sweet Home")
  }

  test("with arguments"){
    given Locale = Locale.ENGLISH
    assertNoDiff(i18n("files.summary", 10, "root"), "The disk root contains 10 file(s).")
  }

  test("single quote"){
    given Locale = Locale.ENGLISH
    assertNoDiff(i18n("info.error"), "You aren't logged in!")
  }

  test("triple quote") {
    given Locale = Locale.ENGLISH
    assertNoDiff(i18n("example.formatting"), "When using MessageFormat, '{0}' is replaced with the first parameter.")
  }

  test("Country") {
    given Locale = Locale.of("zh")
    assertNoDiff(obj.i18n("home.title"), "chinese home:zh")
  }

  test("Country") {
    given Locale = Locale.of("zh", "CN")

    assertNoDiff(obj.i18n("home.title"), "chinese home:zh_CN")
  }

  test("Local with variant") {
    given Locale = Locale.of("en", "SG", "gf123")
    assertNoDiff(obj.i18n("home.title"), "ayer rajah:en_SG_gf123")
  }

  test("loop".ignore){
    implicit val gfLocale = Locale.of("en", "SG", "gf123")

    1 to 10000 foreach { i =>
      val value = obj.i18n("home.title")
      if( i % 1000 == 0) {
        println(s"i ${i} -- $value")
      }
    }
  }

  test("keys"){
    given Locale = Locale.CHINESE
    val xs = obj.dumpBundles
    assertEquals(xs.size, 2)
  }

}
