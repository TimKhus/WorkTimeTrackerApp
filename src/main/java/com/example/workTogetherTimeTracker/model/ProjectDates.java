package com.example.workTogetherTimeTracker.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProjectDates {
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    private static final List<DateTimeFormatter> dateFormatters = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd"),
            DateTimeFormatter.ofPattern("yyyy-M-d"),
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("yyyy.M.d"),
            DateTimeFormatter.ofPattern("yyyy-M-dd"),
            DateTimeFormatter.ofPattern("yyyy/M/dd"),
            DateTimeFormatter.ofPattern("yyyy.M.dd"),
            DateTimeFormatter.ofPattern("yyyy-MM-d"),
            DateTimeFormatter.ofPattern("yyyy/MM/d"),
            DateTimeFormatter.ofPattern("yyyy.MM.d"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d.M.yyyy"),
            DateTimeFormatter.ofPattern("dd-M-yyyy"),
            DateTimeFormatter.ofPattern("dd/M/yyyy"),
            DateTimeFormatter.ofPattern("dd.M.yyyy"),
            DateTimeFormatter.ofPattern("d-MM-yyyy"),
            DateTimeFormatter.ofPattern("d/MM/yyyy"),
            DateTimeFormatter.ofPattern("d.MM.yyyy"),
            DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.US),
            DateTimeFormatter.ofPattern("dd/MMM/yyyy", Locale.US),
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US),
            DateTimeFormatter.ofPattern("d.M.yyyy г.", Locale.getDefault())
    );

    public ProjectDates(String dateFromString, String dateToString) {
        this.dateFrom = parseAndFormatDate(dateFromString);
        this.dateTo = parseAndFormatDate(dateToString);
    }

    private LocalDate parseAndFormatDate(String dateString) {
        if (dateString.equals("NULL")) {
            return LocalDate.now();
        } else {
            for (DateTimeFormatter formatter : dateFormatters) {
                try {
                    LocalDate parsedDate = LocalDate.parse(dateString, formatter);
                    // Formatting the date to the desired format "yyyy-MM-dd"
                    return LocalDate.parse(parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (Exception ignored) {
                    // Next format if current doesn't fit
                }
            }
            throw new IllegalArgumentException("Impossible to recognise date format: " + dateString);
        }
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public ProjectDates calculateIntersection(ProjectDates other) {
        LocalDate intersectionStart = this.dateFrom.isAfter(other.dateFrom) ? this.dateFrom : other.dateFrom;
        LocalDate intersectionEnd = this.dateTo.isBefore(other.dateTo) ? this.dateTo : other.dateTo;

        if (!intersectionStart.isAfter(intersectionEnd)) {
            String intersectionStartString = intersectionStart.toString();
            String intersectionEndString = intersectionEnd.toString();
            return new ProjectDates(intersectionStartString, intersectionEndString);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDates that = (ProjectDates) o;
        return Objects.equals(dateFrom, that.dateFrom) && Objects.equals(dateTo, that.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateFrom, dateTo);
    }
}
