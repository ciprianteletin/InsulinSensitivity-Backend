package com.insulin.shared.constants;

/**
 * Large class of constants used for email, starting from the configuration to the actual message passed
 * to the user.
 */
public class EmailConstants {
    public static final String SIMPLE_MAIL_TRANSFER_PROTOCOL = "smtps";
    public static final String FROM_EMAIL = "support@insulin.com";
    public static final String CC_EMAIL = "";

    public static final String EMAIL_SUBJECT = "Insulin Sensitivity - Welcome abroad!";
    public static final String DELETE_SUBJECT = "Insulin Sensitivity - We are sorry";
    public static final String RESET_SUBJECT = "Insulin Sensitivity - Forgot your password?";

    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT = 465;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";

    public static final String REGISTER_TEXT_MESSAGE = "Hello Mr./Mrs. %s,\n\nWe are pleased to announce you that your account was created with " +
            "success. In order to use your account, login using the credentials inserted to the register form. We hope that our application " +
            "will be useful for you and your healthy.\n\nWe thank you for your trust,\nInsulin Sensitivity Team";
    public static final String DELETE_TEXT_MESSAGE = "Dear Mr./Mrs. %s, \n\nWe are sorry that you choose to leave us, no matter what the reason is. " +
            "We hope that someday you will come back, we are welcoming you anytime with our arms open. Until then, best of luck and thank you for all your " +
            "support.\n\nWe thank you for your trust,\nInsulin Sensitivity Team";
    public static final String RESET_PASSWORD_MESSAGE = "Dear Mr/Mrs,\n\nYou requested a link to reset your password. " +
            "Click here: http://localhost:4200/resetPassword/%s in order to change your password. " +
            "Please pay attention that the link is available only three hours! " +
            "You will be prompted to a form where you can insert your newly password.\nIf you did not request to change your password, please ignore the link from above.\n\n"
            + "Insulin Sensitivity team.";
}
