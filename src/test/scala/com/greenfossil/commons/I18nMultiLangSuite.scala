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
    val locale2 = Locale.of("zh", "CN", "gf123")
    assertEquals(locale, locale2)
    assertNoDiff(TestI18n.i18nGetMultiLangOfLocale(locale, "zh", "home.title", "default home"), "chinese home:zh_CN_gf123")
    assertNoDiff(TestI18n.i18nGetMultiLangOfLocale(locale2, "zh", "home.title", "default home"), "chinese home:zh_CN_gf123")
  }

  test("i18nGetListOfTranslationOfLangs"){
    assertEquals(TestI18n.i18nGetListOfTranslationOfLangs(Seq("en"),  "home.title", "default home"), Seq(("en", "Home Sweet Home")))
    assertEquals(TestI18n.i18nGetListOfTranslationOfLangs(Seq("zh"),  "home.title", "default home"), Seq(("zh", "chinese home:zh")))
    assertEquals(TestI18n.i18nGetListOfTranslationOfLangs(Seq("en","zh"),  "home.title", "default home"), Seq(("en", "Home Sweet Home"), ("zh", "chinese home:zh")))
  }

  test("i18nGetMultiLang") {
    assertNoDiff(TestI18n.i18nGetMultiLang("home.title", "default home"), "Home Sweet Home chinese home:zh")
    assertNoDiff(TestI18n.i18nGetMultiLang(Seq("en"), "home.title", "default home"), "Home Sweet Home")
    assertNoDiff(TestI18n.i18nGetMultiLang(Seq("zh"), "home.title", "default home"), "chinese home:zh")
  }

  test("i18nGetMultiLang with default values") {
    assertNoDiff(TestI18n.i18nGetMultiLang("food.english", "Default English"), "english food")
    assertNoDiff(TestI18n.i18nGetMultiLang("food.chinese", "Default English"), "Default English chinese food:zh")
    assertNoDiff(TestI18n.i18nGetMultiLang("food.notFound", "Default English"), "Default English")
  }
}
