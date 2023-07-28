package io.github.orczykowski.logback.obfuscator;

public class IncorrectConfigurationException extends RuntimeException {
    IncorrectConfigurationException(String msg) {
        super(msg);
    }
}
