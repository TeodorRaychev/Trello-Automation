package com.trello.util;

import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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
            String resolvedValue = resolveEnvVars(stripComment(rawValue));
            resolvedProps.setProperty(key, resolvedValue);
        }
        return resolvedProps;
    }

    public static String stripComment(String line) {
        int commentIndex = line.indexOf('#');
        if (commentIndex >= 0) {
            return line.substring(0, commentIndex).trim();
        }
        return line.trim();
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

    public static void setLogLevel(Logger logger, Level level) {
        logger.setLevel(level);
        Handler[] handlers = logger.getHandlers();
        if (handlers.length == 0) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(level);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);
        } else {
            for (Handler handler : handlers) {
                handler.setLevel(level);
            }
        }
        logger.setUseParentHandlers(false);
    }
}
