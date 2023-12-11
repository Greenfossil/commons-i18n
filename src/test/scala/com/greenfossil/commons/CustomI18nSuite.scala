package com.greenfossil.commons

import munit.FunSuite

import java.io.{File, FileWriter}
import java.nio.file.Paths
import java.util.Locale

class CustomI18nSuite extends FunSuite, I18nSupport:

  val file: File = Paths.get("/tmp/app-messages.properties").toFile
  override lazy val I18NFILENAME: String = "/tmp/app-messages,messages-test"

  test("Setting of custom I18n"):
    given Locale = Locale.forLanguageTag("en")
    assertEquals(i18n("menu.ministryTree.label"), "Ministry Tree 2")
    assertEquals(i18n("home.title"), "Home Sweet Home")


  override def beforeAll(): Unit =
    file.createNewFile()
    val fileWriter = new FileWriter(file)
    fileWriter.write("menu.ministryTree.label=Ministry Tree 2")
    fileWriter.close()

  override def afterAll(): Unit =
    file.delete()