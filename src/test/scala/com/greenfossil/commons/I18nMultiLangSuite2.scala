package com.greenfossil.commons

import munit.FunSuite

import java.util.Locale

class I18nMultiLangSuite2 extends FunSuite{

  object TestI18n extends I18nSupport{
    override lazy val I18NFILENAME: String = "messages-test,messages-test2"
    override lazy val multiLangEnabled: Boolean = true
    override lazy val supportedLanguagesForMultiLang: Seq[String] = Seq("en", "zh")
  }

  given Locale = Locale.ENGLISH

  test("Multiple language with i18nWithDefault()"){
    assertEquals(TestI18n.i18nWithDefault("user.lastname", "Last Name"), "Last Name 姓氏")
    assertEquals(TestI18n.i18nWithDefault("user.firstname", "First Name"), "First Name 名字")
  }

  test("Multiple language with i18n()") {
    assertEquals(TestI18n.i18n("user.lastname"), "Last Name 姓氏")
    assertEquals(TestI18n.i18n("user.firstname"), "First Name 名字")
  }

}

