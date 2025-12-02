package com.example.pipeline_gerencia.util;

import com.example.pipeline_gerencia.model.Task;

/**
 * Utility class for task validation
 */
public class TaskValidator {
    
    private static final int MIN_TITLE_LENGTH = 3;
    private static final int MAX_TITLE_LENGTH = 255;
    private static final int MAX_DESCRIPTION_LENGTH = 2000;

    public static boolean isValid(Task task) {
        if (task == null) {
            return false;
        }
        return isValidTitle(task.getTitle()) && 
               isValidDescription(task.getDescription()) &&
               isValidCompletionPercentage(task.getCompletionPercentage());
    }

    public static boolean isValidTitle(String title) {
        if (title == null) {
            return false;
        }
        int length = title.trim().length();
        return length >= MIN_TITLE_LENGTH && length <= MAX_TITLE_LENGTH;
    }

    public static boolean isValidDescription(String description) {
        if (description == null) {
            return true; // Description is optional
        }
        return description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    public static boolean isValidCompletionPercentage(int percentage) {
        return percentage >= 0 && percentage <= 100;
    }

    public static String getValidationError(Task task) {
        if (task == null) {
            return "Task cannot be null";
        }
        
        if (!isValidTitle(task.getTitle())) {
            return "Title must be between " + MIN_TITLE_LENGTH + " and " + MAX_TITLE_LENGTH + " characters";
        }
        
        if (!isValidDescription(task.getDescription())) {
            return "Description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters";
        }
        
        if (!isValidCompletionPercentage(task.getCompletionPercentage())) {
            return "Completion percentage must be between 0 and 100";
        }
        
        return null;
    }
}
