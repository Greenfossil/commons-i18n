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
class I18nLocaleProviderSuite extends munit.FunSuite  {

  val obj = new I18nSupport {
    //File messages-test is at /test/resources folder
    override lazy val I18NFILENAME: String = "messages-test,messages-test2"
  }

  import obj.*


  given LocaleProvider = new LocaleProvider {
    override def locale: Locale = Locale.getDefault
  }
  test("non existing key"){
    assert(i18n("nokey") == "nokey")
  }

  test("existing key") {
    assert(i18n("home.title") == "Home Sweet Home")
  }

  test("with arguments"){
    assert(i18n("files.summary", 10, "root") == "The disk root contains 10 file(s).")
  }

  test("single quote"){
    assert(i18n("info.error") == "You aren't logged in!")
  }

  test("triple quote") {
    assert(i18n("example.formatting") == "When using MessageFormat, '{0}' is replaced with the first parameter.")
  }

  test("Country") {
    given LocaleProvider = new LocaleProvider {
      override def locale: Locale =  new Locale("zh", "CN")
    }
    assert(obj.i18n("home.title") == "chinese home")
  }

  test("Local with variant") {
    given LocaleProvider = new LocaleProvider {
      override def locale: Locale =  new Locale("en", "SG", "gf")
    }
    assert(obj.i18n("home.title") == "ayer rajah")
  }

  test("loop"){
    given LocaleProvider = new LocaleProvider {
      override def locale: Locale = new Locale("en", "SG", "gf")
    }

    1 to 10000 foreach { i =>
      val value = obj.i18n("home.title")
      if( i % 1000 == 0) {
        println(s"i ${i} -- $value")
      }
    }
  }

  test("keys"){
    given LocaleProvider = new LocaleProvider {
      override def locale: Locale = Locale.CHINESE
    }

//    given Locale = Locale.CHINESE

    val xs = obj.dumpBundles
    assertEquals(xs.size, 2)
  }

}
