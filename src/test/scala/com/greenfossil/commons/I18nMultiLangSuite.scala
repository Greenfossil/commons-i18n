package com.greenfossil.commons

import com.typesafe.config.DefaultConfig
import munit.FunSuite

import java.util.Locale

class I18nMultiLangSuite extends FunSuite, I18nSupport {

  object TestI18n extends I18nSupport {
    //File messages-test is at /test/resources folder
    override lazy val I18NFILENAME: String = "messages-test,messages-test2"
  }

  test("create Locale for lang"){
    val locale = TestI18n.createLocaleForLang(variant = "gf123", langTag = "zh-CN")
    println(s"locale = ${locale}")
    val locale2 = new Locale("zh", "CN", "gf123")
    assertEquals(locale, locale2)
//    assertNoDiff(TestI18n.i18nGetMultiLang("home.title", "ayer rajah"), "chinese home")
  }

}
