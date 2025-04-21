package com.JobFitChecker.ResumePostProcessor.resumePostProcessWorkflow;

import com.JobFitChecker.ResumePostProcessor.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public final class UpdateQualificationActivity {
    private static final Logger log = LoggerFactory.getLogger(UpdateQualificationActivity.class);

    private UserRepository userRepository;

    @Autowired
    public UpdateQualificationActivity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateQualification(Qualification qualification) {
        long id = qualification.getId();
        String degree = qualification.getDegree();
        String major = qualification.getMajor();
        String graduationDate = qualification.getGraduationDate();
        String prevJobTitles = getPreviousJobTitlesString(qualification.getPreviousJobTitles());
        String skills = getSkillsString(qualification.getSkills());

        userRepository.updateQualification(id, degree, major, graduationDate, prevJobTitles, skills);

        log.info("Updated qualification data for user {} in PostgreSQL", id);
    }

    private String getPreviousJobTitlesString(Set<String> jobTitles) {
        String titleSetStr = jobTitles.toString();
        return titleSetStr.substring(1, titleSetStr.length() - 1);
    }

    private String getSkillsString(Set<String> skills) {
        String skillSetStr = skills.toString();
        return skillSetStr.substring(1, skillSetStr.length() - 1);
    }
}
