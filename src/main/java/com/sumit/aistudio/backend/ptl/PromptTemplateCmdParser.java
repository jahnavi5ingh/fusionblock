package com.sumit.aistudio.backend.ptl;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PromptTemplateCmdParser {

    public static ParsedTemplate parseTemplate(String input) {
        input = StringUtils.trim(input);
        // Define the regex pattern
        String regex = "^//\\#START:([\\w\\\\\\.]+):([\\w\\.]+)\\(\"([\\w\\.]+)\",(true|false),\\[(\"[^\"]*\"(,\\s*\"[^\"]*\")*)?\\]\\);$";
        String regex2 = "^//\\#START:([\\w\\\\\\.]+):([\\w\\.]+):([\\w\\.]+)";

        // Compile the pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the pattern against the input
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            // Extract parts
            String part1 = matcher.group(1);  // PromptID
            String part2 = matcher.group(2);  // Template
            String model = matcher.group(3);  // Model
            String booleanValue = matcher.group(4);  // Clean
            String arrayOfStrings = matcher.group(5);  // Tags

            return new ParsedTemplate(part1, part2, model, booleanValue, arrayOfStrings);
        } else {
            Pattern pattern2 = Pattern.compile(regex2);
            Matcher matcher2 = pattern2.matcher(input);
            if (matcher2.matches()) {
                String part1 = matcher2.group(1);  // PromptID
                String part2 = matcher2.group(2);  // Template
                String model = matcher2.group(3);  // Model
                String booleanValue = "true";  // Clean
                String arrayOfStrings = "[]";  // Tags

                return new ParsedTemplate(part1, part2, model, booleanValue, arrayOfStrings);

            } else {
                throw new IllegalArgumentException("Input string does not match the pattern:" + input);
            }
        }
    }
}
