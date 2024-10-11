package com.JobFitChecker.JobFitCheckerApp.services.resumePostProcessWorkflow;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.JobFitChecker.JobFitCheckerApp.utils.Constant.OPENAI_API_KEY;


@Component
public final class ExtractQualificationActivity {
    private static final Logger log = LoggerFactory.getLogger(ExtractQualificationActivity.class);

    interface Extractor {
        @UserMessage("""
      Extract the degree, major. graduation date of the user's resume described below.
      For skills, be sure to also extract keywords in work experience and projects.
      Return only JSON, without any markdown markup surrounding it.
      Here is the id of the user: {{id}}
      Here is the resume of the user:
      ---
      {{text}}
      """)
        Qualification extract(@V("text") String text, @V("id") long id);
    }

    public Qualification extractDataFromResumeText(long userId, String resumeText) {
        ChatLanguageModel model = OpenAiChatModel.builder()
                .apiKey(OPENAI_API_KEY)
                .modelName(OpenAiChatModelName.GPT_4_O_MINI)
                .logRequests(true)
                .logResponses(true)
                .build();

        // long id = 7L; // ToDo: Change to dynamic data
        Extractor extractor = AiServices.create(Extractor.class, model);
        Qualification qualification = extractor.extract(resumeText, userId);
        log.info("Extracted resume metadata for user with user id ${} ", userId);

        return qualification;
    }

    // ToDo: Delete this partial test before production
    public static void main(String[] args) {
        String text = "Tiansi Gu \u2029\n" +
                "tiansigu10@gmail.com |  (626) 637-7982 |  San Francisco, California | linkedin.com/in/tiansi-gu-51796b267/ \n" +
                "EDUCATION \n" +
                "Master of Science, Computer Science    Aug 2023 – May 2026 \n" +
                "University of San Francisco, San Francisco, CA       GPA: 4.0 \n" +
                "Bachelor of Business Administration Sep 2017 – Jun 2021 \n" +
                "Renmin University of China, Beijing, China              GPA: 3.79     \n" +
                "Coursework: Object-Oriented Programming, Data Structures and Algorithms, System Programming, Artificial Intelligence \n" +
                "Foundations, Software Development Principles, Web Development \n" +
                "TECHNICAL SKILLS \n" +
                "• Programming Languages: Java, Kotlin, C, Python, Typescript, XML, JSON, HTML, CSS, SQL \n" +
                "• Tools/Frameworks/Systems: Spring, SpringBoot, Node.js, ExpressJS, \n" +
                "Django, REST APIs, JUnit, Jest, Gradle, Maven, Docker, Unix, Linux, Mac OS, Windows, Git \n" +
                "• Database/Cloud Computing: DynamoDB, MongoDB, Postgres SQL, Google Cloud \n" +
                "• Latency Sensitive Web Service, Web Development, Mobile Application Development, Multi-threading, Networking \n" +
                "WORK EXPERIENCE \n" +
                "Amazon Web Service - Transcribe Seattle, WA \n" +
                "Software Development Engineer Intern May 2024 – Aug 2024 \n" +
                "• Delivered and documented per-customer throttle limit control for AWS’s HealthScribe service in an agile environment, \n" +
                "which prevents an excessive number of clinical audio summarization requests from overwhelming the Bedrock LLM \n" +
                "models and reduces latency by up to 64% as the system scales. \n" +
                "• Added and updated scan, insert, query, and delete APIs to encapsulate interactions with AWS DynamoDB to track the jobs \n" +
                "status of customers in Kotlin, and managed tables/indexes via Typescript in AWS CDK. \n" +
                "• Implemented Lambda functions deployed on AWS, being triggered by EventBridge to enforce limits, initiate NLP \n" +
                "workflows in parallel, and ensure data consistency in a high throughput environment; Configured IAM authentications for \n" +
                "secure visibilities among components and published embedding metrics on CloudWatch for system monitoring.  \n" +
                "• Conducted thorough unit tests with a 95% coverage rate, and implemented integration and canary tests to support CI/CD. \n" +
                "PROJECTS \n" +
                "Parktacular \n" +
                "• Developed a website that improves SFMTA’s parking map by creating filter and data visualizing features for available \n" +
                "parking hours, price, radius-based distance, safety, etc. allowing users to plan and tailor their parking in San Francisco. \n" +
                "• Utilized Python Django to build a server that handles HTTP requests and computation, and connected it with Azure \n" +
                "Postgres SQL cloud database through ODBC.  \n" +
                "• Leveraged XGBoost Machine Learning model to determine the likelihoods of various types of crime’s occurrences \n" +
                "various types of crime’s occurrences near a parking facility and display them on the map, helping residents cope with the \n" +
                "increasingly rampant car burglary. \n" +
                "Raincheck Mobile App \n" +
                "• Worked in team to develop a clothing and local event recommendation App in a hackathon team based on real-time \n" +
                "weather updates. \n" +
                "• Delivered UI implementations and navigations among screens with React Native, supporting Android and IOS systems. \n" +
                "• Integrated Google geocode and National Weather APIs into the App to locate users’ coordinates, get hourly weather data, \n" +
                "and generate clothing and entertainment suggestions, and mapped the dynamic data to the user interface with JavaScript. \n" +
                "Mastermind Guessing Game \n" +
                "• Designed, delivered, and deployed a full-stack web guessing game featuring CRUD with React and Java Spring, where \n" +
                "users can register, log in, play in two modes, look up player rankings, and redeem rewards in the marketplace.  \n" +
                "• Leveraged HTML, CSS, and JavaScript to build 10+ reusable JS components, and streamlined state management using \n" +
                "Redux and React-Hook. \n" +
                "• Implemented 10+ RESTful APIs, utilized JPA and Spring Data for communications between server and database, used \n" +
                "JUnit for testing, and managed build by Maven.";

        String text2 = "Yumejichi Fujita\n" +
                "BRANDMERCH, Remote, US Full Stack Developer Intern\n" +
                "● Designed, developed, and tested mainly back-end of the web application with JavaScript/TypeScript and React.\n" +
                "● Implemented APIs and Database(PostgreSQL) to store information, process data, and show product details.\n" +
                "● Learned and applied new technologies to solve technical challenges such as using NodeJS and Express for the backend.\n" +
                "● Selected appropriate technologies and implementations based on an evaluation of trade-offs and capabilities.\n" +
                "● Collaborated with the company founder to understand project requirements and objectives.\n" +
                "DENSO Corporation, Aichi, Japan\n" +
                "Embedded Software Engineer, Safety Sensor & Components Engineering Division April 2022 - July 2023\n" +
                "● Developed camera sensor for light recognition and control systems for auto and adaptive high beam systems for cars.\n" +
                "● Conducted real-world testing using vehicles equipped with the same camera and software under development, and\n" +
                "subsequently analyzed the real testing data and simulation results. Based on the findings, made essential coding changes and\n" +
                "optimizations to enhance system performance and functionality.\n" +
                "● Enhanced the source code of the latest generation system, adapting it to specific customer requirements using C.\n" +
                "● Enhanced image and video visualization during simulations by improving the debugging code and introduced new logic for\n" +
                "evaluating light colors.\n" +
                "● Conducted code reviews, ensuring adherence to coding conventions, and created detailed design documents with Doxygen.\n" +
                "● Designed and implemented advanced debug modes on the Linux platform in docker environments with a particular emphasis\n" +
                "on incorporating image analysis features, enhancing future data analysis and facilitating code changes and optimizations.\n" +
                "Regulatory Affairs Specialist, Engineering development Promotion Division April 2017 - March 2022 PROJECTS\n" +
                "Raincheck App - A mobile weather forecast app that will suggest what to wear based on the forecast November 2023\n" +
                "● Led a team of 4 to build the app using React Native over the course of a 3 day hackathon.\n" +
                "● Utilized Geocoding API and National Weather Service (NWS) API to get the location and the hourly/weekly weather and to\n" +
                "suggest the clothes depending on the predicted weather.\n" +
                "OED(open Energy Dashboard) CTI-CodeDay SWE Micro-internship January 2024 - February 2024\n" +
                "● Contributed to OED, a free, web-based application to display the energy use information to help organizations to manage their energy use in the building which is implemented as a web application using React for the front-end which communicates\n" +
                "with a NodeJS backend and a PostGreSQL database.\n" +
                "● Collaborated with other two team members and a current software engineer mentor.\n" +
                "● Created a new test case to verify data in database(PostgreSQL) and from API matches by using Javascript(pull request) to\n" +
                "ensure their functionality in React components. SKILLS\n" +
                "● Programming Languages and Frameworks:\n" +
                "Java, C, Python, JavaScript, TypeScript, HTML, CSS, React, React Native, Node, Express\n" +
                "● Tools and Technologies:\n" +
                "GitHub/Bitbucket, Jira/Figma, Jenkins, Firebase, GCloud, Linux(Ubuntu), docker, , MongoDB, PostgreSQL";
        ExtractQualificationActivity extractQualificationData = new ExtractQualificationActivity();
        Qualification q = extractQualificationData.extractDataFromResumeText(7L, text);
        System.out.println(q);
    }
}
