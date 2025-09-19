package com.trello.util;

import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigLoader {
    private static final Pattern ENV_PATTERN = Pattern.compile("\\$\\{([^:}]+):?([^}]*)}");

    public static Properties loadConfig(String path) {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(path)) {
            props.load(input);
        } catch (Exception e) {
            Logger logger = Logger.getLogger(ConfigLoader.class.getName());
            logger.severe(MessageFormat.format("Failed to load properties file: [{0}]",
                    e.getMessage()));
            return null;
        }
        Properties resolvedProps = new Properties();
        for (String key : props.stringPropertyNames()) {
            String rawValue = props.getProperty(key);
            String resolvedValue = resolveEnvVars(rawValue);
            resolvedProps.setProperty(key, resolvedValue);
        }
        return resolvedProps;
    }

    private static String resolveEnvVars(String value) {
        Matcher matcher = ENV_PATTERN.matcher(value);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String envVar = matcher.group(1);
            String defaultValue = matcher.group(2);
            String envValue = System.getenv(envVar);
            if (envValue == null) {
                envValue = defaultValue;
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(envValue));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
