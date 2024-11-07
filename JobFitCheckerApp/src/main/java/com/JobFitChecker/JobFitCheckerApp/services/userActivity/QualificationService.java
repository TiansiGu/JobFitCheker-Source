package com.JobFitChecker.JobFitCheckerApp.services.userActivity;

import com.JobFitChecker.JobFitCheckerApp.model.data.AIFeedBack;
import com.JobFitChecker.JobFitCheckerApp.model.entities.User;
import com.JobFitChecker.JobFitCheckerApp.repository.UserRepository;
import com.google.gson.Gson;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public final class QualificationService {
    private static final Logger log = LoggerFactory.getLogger(QualificationService.class);

    private static final String NOT_PROVIDED = "not provided";

    private UserRepository userRepository;

    @Value("${open.ai.api.key}")
    private String apiKey;

    interface Checker {
        @UserMessage("""
      Check job description: {{jobDescription}} and user qualification data: {{qualificationData}},
      give feedback of whether this user is qualified for the given {{jobDescription}}.
      If the degree, major, graduation date, sponsorship, or workExperience clearly does not meet the basic requirement, 
      stop checking the rest fields as you already know the user should not apply for this job.
      Otherwise, check if the skills match the job description, and give a response.
      Explain your feedback in field reason in second-person narration.
      If you feel there is not enough information to get a response, please include "The information you provided is 
      not enough. Please update profile or resume." as the last paragraph.
      """)
        AIFeedBack check(@V("jobDescription") String jobDescription, @V("qualificationData") String qualificationData);
    }

    @Autowired
    public QualificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AIFeedBack askAICheckQualifications(long userId, String jobDescription) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            log.info("Checking if user {} is qualified for the given job description...", userId);

            User user = optionalUser.get();
            String qualificationJsonString = fromUserToQualificationJsonString(user);

            ChatLanguageModel model = OpenAiChatModel.builder()
                    .apiKey(apiKey)
                    .modelName(OpenAiChatModelName.GPT_4_O_MINI)
                    .logRequests(true)
                    .logResponses(true)
                    .build();

            Checker checker = AiServices.create(Checker.class, model);
            return checker.check(jobDescription, qualificationJsonString);
        } else {
            throw new NoSuchElementException("The user with id " + userId + "does not exist");
        }
    }

    private String fromUserToQualificationJsonString(User user) {
        String degree = (user.getDegree() != null)? user.getDegree() : NOT_PROVIDED;
        String major = (user.getMajor() != null)? user.getMajor() : NOT_PROVIDED;
        String graduationDate = (user.getGraduationDate() != null)? user.getGraduationDate() : NOT_PROVIDED;
        String previousJobTitles = (user.getPreviousJobTitles() != null)? user.getPreviousJobTitles() : NOT_PROVIDED;
        String skills = (user.getSkills() != null)? user.getSkills() : NOT_PROVIDED;
        String needSponsor = (user.getNeedSponsor() != null)? user.getNeedSponsor() : NOT_PROVIDED;

        Gson gson = new Gson();
        QualificationData data = new QualificationData(
                degree,
                major,
                graduationDate,
                previousJobTitles,
                skills,
                needSponsor
        );
        return gson.toJson(data);
    }

    private static final class QualificationData {
        private final String degree;
        private final String major;
        private final String graduationDate;
        private final String previousJobTitles;
        private final String skills;
        private final String needSponsor;

        public QualificationData(String degree, String major, String graduationDate, String previousJobTitles, String skills, String needSponsor) {
            this.degree = degree;
            this.major = major;
            this.graduationDate = graduationDate;
            this.previousJobTitles = previousJobTitles;
            this.skills = skills;
            this.needSponsor = needSponsor;
        }
    }

}
