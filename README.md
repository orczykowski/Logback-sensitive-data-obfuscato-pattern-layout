# Logback sensitive data obfuscator pattern layout
## Why

Masking sensitive data in logs is crucial for protecting user privacy, complying with legal regulations such as
RODO/GDPR, minimizing risk, and facilitating the debugging process. It is a security best practice that should be
implemented in all systems handling sensitive data.

## Description

This library provides a solution for masking sensitive data in logs within applications that use
Logback Classic. It allows you to extend the XML configuration by defining names od fields/variables/properties which
contain in value sensitive data. The library includes predefined patterns for detecting sensitive values and also allows
for creating custom patterns.

The library provides two methods for masking sensitive data:

- **SensitiveDataAsShortcutLayout** -This method involves computing a hash of the sensitive data using the format
  FIRST LETTER OF SENSITIVE VALUE - LENGTH OF CHARACTER STRING - LAST LETTER OF SENSITIVE VALUE. It provides a
  lightweight qualitative verification opportunity, such as checking whether the submitted value consists of the correct
  number of characters or potentially correlating data across multiple logs. For example, for the value Gustaw, it will
  be G-6-w. This method can be helpful during error verification in production or while debugging code to investigate
  the root cause of bugs while anonymizing the data.
- **SensitiveDataAsMaskLayout** - This method replaces sensitive data with a fixed string mask. The default mask
  is '********'.

_example how it works_

```
  {
  "message": "some log with user email=[test@github.io]"
  }
```

can be converted to:

```
  {
  "message": "some log with user email=[********]"
  }
```

For more information, please refer to the configuration section.

## How to use

1. Add the following dependency to your POM file:

```xml

<dependency>
    <groupId>io.github.orczykowski</groupId>
    <artifactId>logback-sensitive-data-obfuscator-pattern-layout</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. Add layout to logback configuration

```xml

<encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
    ...
</encoder>
```

3. Choose type o masking by using `MaskSensitiveDataAsShortcutLayout` or `MaskSensitiveDataLayout`

```xml

<layout class="io.github.orczykowski.logback.obfuscator.MaskSensitiveDataLayout">
  ...
</layout>

```

4. Define fields / properties with sensitive data

```xml

<layout class="io.github.orczykowski.logback.obfuscator.MaskSensitiveDataLayout">
  <patternName>EQUAL_AND_SQUARE_BRACKETS</patternName>
  <fieldName>firstName</fieldName>
</layout>
```

_Example configuration:_

```xml

<configuration>
  <appender name="mask" class="ch.qos.logback.core.ConsoleAppender">
    <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
      <layout class="io.github.orczykowski.logback.obfuscator.MaskSensitiveDataLayout">
        <mask>***SENSITIVE*DATA***</mask>
        <patternName>JSON</patternName>
        <patternName>EQUAL_AND_SQUARE_BRACKETS</patternName>
        <fieldName>firstName</fieldName>
        <fieldName>email</fieldName>
        <pattern>%-5p %c: %m</pattern>
      </layout>
    </encoder>
  </appender>
  <root level="INFO">
    <appender-ref ref="mask"/>
  </root>

</configuration>
```

### Configuration options:

| Option                | description                                                                                                                                                                                                                                                                                                                  |
|-----------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ```<patternName>```   | The name of the predefined regular expression. It must be present in the configuration before adding the <fieldName>. You can add multiple regular expressions.                                                                                                                                                              |
| ```<customPattern>``` | A custom regular expression pattern. The pattern must comply with Java regular expression syntax and must contain a placeholder [PROPERTY_NAME] where the sensitive value appears in the log. The sensitive value must be enclosed in parentheses, e.g., `[PROPERTY_NAME]->'([^']+)'` for the log `email->'test@github.io'`. |
| ```<fieldName>```     | The names of fields/properties/variables that contain sensitive data.                                                                                                                                                                                                                                                        |
| ```<mask>```          | An optional string that represents the mask to which sensitive data will be replaced. This option is only applicable when using the SensitiveDataAsMaskDecorator.                                                                                                                                                            |

#### Available patterns names

- **JSON** : Matches sensitive values in JSON logs,
- **EQUAL_AND_SQUARE_BRACKETS** : Finds and replaces sensitive values if the log has the
  format `fieldName=[some sensitive value]`,
- **EQUAL_AND_BRACKETS** :   Finds and replaces sensitive values if the log has the
  format `fieldName=(some sensitive value)`,
- **EQUAL_AND_DOUBLE_QUOTES** : Finds sensitive values if the log has the format  `fieldName="some sensitive value"`,

Defining a regular expression (by selecting from predefined ones or adding a custom one) and a list of field names is
mandatory. For the SensitiveDataAsMaskDecorator, you can define a custom mask, but it is optional.

## How to contribute

### Hot to verify

- run tests `mvn test`
- run mutation tests `mvn test-compile org.pitest:pitest-maven:mutationCoverage`
- build `mvn install -DcreateChecksum=true`

### [MIT License](https://opensource.org/licenses/MIT)



