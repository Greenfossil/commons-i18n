package com.greenfossil.commons

import java.util.Locale

class LocaleLikeSuite extends munit.FunSuite, I18nSupport {

  test("LocaleLike.toLocale extension - Locale"){
    val ll: LocaleLike =  Locale.getDefault
    assertEquals(ll.locale, Locale.getDefault)
    assertEquals(toLocale(ll), Locale.getDefault)
  }

  test("LocaleLike.toLocale extension - LocaleProvider") {
    val ll: LocaleLike = new LocaleProvider:
      override def locale: Locale = Locale.getDefault

    assertEquals(ll.locale, Locale.getDefault)
    assertEquals(toLocale(ll), Locale.getDefault)
  }

}
