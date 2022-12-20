# commons-i18n 

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/Greenfossil/commons-i18n/run-tests.yml?branch=master)
![](https://img.shields.io/github/license/Greenfossil/commons-i18n)
![](https://img.shields.io/github/v/tag/Greenfossil/commons-i18n)

This is the official Greenfossil Scala library for internationalization and localization.

## How to Build

This library uses sbt as its build tool. It requires at least Java 17 or later to build.

Follow the official guide on how to install [sbt](https://www.scala-sbt.org/download.html).

## Usages

To use this library, simply import the package and extend `I18nSupport` as such

```scala
import com.greenfossil.commons.I18nSupport
class myClass extends I18nSupport
```

You can define a list of key value within a `messages` properties file using the file structure specified below.

```properties
# library.title -> key
# commons-18n -> value
library.title=commons-18n
```

Define your properties file as such

<ul>
    <li>src</li>
    <ul>
        <li>main</li>
        <ul>
            <li>resources</li>
            <ul>
                <li>Resource Bundle 'messages'</li>
                <ul>
                    <li>messages.properties</li>
                    <li>messages_en.properties</li>   
                    <li>messages_en_SG.properties</li>
                    <li>messages_en_SG_i18n.properties</li>
                </ul>
            </ul>
        </ul>
    </ul>
</ul>

You can specify additional properties file in your application. A properties file can be segregated into language, country and variant.

`messages_en` : A properties file which consist of language.

`messages_en_SG` : A properties file which consist of language and country.

`messages_en_SG_i18n` : A properties file which consist of language and country and variant.

You can access this translation by invoking either of these 2 methods

```scala
import java.util.Locale
given Locale = Locale.getDefault()

// The value will be retrieve in the properties file according to the key defined
i18n("library.title")

// Default value will be shown if key does not exist
i18nWithDefault("library.title","My Library")
```

Configure some translation according to the locale set

```
app.i18n.langs = ["en", "zh", "my"] # Note: `en` is already defined by default
app.i18n.variant = "i18n"
```

## License

commons-i18n is licensed under the Apache license version 2.

See [LICENSE](LICENSE.txt).
