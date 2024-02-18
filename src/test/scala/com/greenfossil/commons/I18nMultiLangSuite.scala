package com.greenfossil.commons

import com.typesafe.config.DefaultConfig
import munit.FunSuite

import java.util.Locale

class I18nMultiLangSuite extends FunSuite, I18nSupport {

  object TestI18n extends I18nSupport {
    //File messages-test is at /test/resources folder
    override lazy val I18NFILENAME: String = "messages-test,messages-test2"
  }

  test("i18nGetMultiLangOfLocale"){
    val locale = TestI18n.createLocaleForLang(variant = "gf123", langTag = "zh-CN")
    val locale2 = new Locale("zh", "CN", "gf123")
    assertEquals(locale, locale2)
    assertNoDiff(TestI18n.i18nGetMultiLangOfLocale(locale, "zh", "home.title", "default home"), "chinese home:zh_CN_gf123")
    assertNoDiff(TestI18n.i18nGetMultiLangOfLocale(locale2, "zh", "home.title", "default home"), "chinese home:zh_CN_gf123")
  }

  test("i18nGetListOfTranslationOfLangs"){
    assertEquals(TestI18n.i18nGetListOfTranslationOfLangs(Seq("en"),  "home.title", "default home"), Seq(("en", "Home Sweet Home")))
    assertEquals(TestI18n.i18nGetListOfTranslationOfLangs(Seq("zh"),  "home.title", "default home"), Seq(("zh", "chinese home:zh")))
    assertEquals(TestI18n.i18nGetListOfTranslationOfLangs(Seq("en","zh"),  "home.title", "default home"), Seq(("en", "Home Sweet Home"), ("zh", "chinese home:zh")))
  }

  test("i18nGetMultiLang".fail) {
    //Check that application.conf is set properly, then confirm the expectation - 'Home Sweet Home' or 'chinese home:zh'
    assertNoDiff(TestI18n.i18nGetMultiLang( "home.title", "ayer rajah"), "") //What is the expectation
  }

}
