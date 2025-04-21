package com.JobFitChecker.ResumePostProcessor.resumePostProcessWorkflow;

import java.util.Collections;
import java.util.Set;

public class Qualification {
    private long id;
    private String degree;
    private String major;
    private String graduationDate;
    private Set<String> previousJobTitles;
    private Set<String> skills;

    public Qualification(long id, String degree, String major, String graduationDate, Set<String> previousJobTitles, Set<String> skills) {
        this.id = id;
        this.degree = degree;
        this.major = major;
        this.graduationDate = graduationDate;
        this.previousJobTitles = previousJobTitles;
        this.skills = skills;
    }

    // ToDo: Used for debugging, delete it before production
    @Override
    public String toString() {
        return "Qualification{" + System.lineSeparator() +
                "id=" + id + System.lineSeparator() +
                "degree=" + degree + System.lineSeparator() +
                "major=" + major + System.lineSeparator() +
                "graduationDate=" + graduationDate + System.lineSeparator() +
                "previousJobTitles=" + previousJobTitles.toString() + System.lineSeparator() +
                "skills=" + skills.toString() + System.lineSeparator() +
                '}';
    }

    public long getId() {
        return id;
    }

    public String getDegree() {
        return degree;
    }

    public String getMajor() {
        return major;
    }

    public String getGraduationDate() {
        return graduationDate;
    }

    public Set<String> getPreviousJobTitles() {
        return Collections.unmodifiableSet(previousJobTitles);
    }

    public Set<String> getSkills() {
        return Collections.unmodifiableSet(skills);
    }
}
