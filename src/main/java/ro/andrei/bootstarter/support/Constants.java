package ro.andrei.bootstarter.support;

public final class Constants {

    // Token constants
    public static final int TOKEN_VALIDITY_DAYS = 31;
    public static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * TOKEN_VALIDITY_DAYS;
    public static final int DEFAULT_SERIES_LENGTH = 16;
    public static final int DEFAULT_TOKEN_LENGTH = 16;
    public static final int MAX_USER_AGENT_LENGTH = 255;
    
    //Regex for acceptable logins
    public static final String USERNAME_REGEX = "^[_'.@A-Za-z0-9-]*$";
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SYSTEM_ACCOUNT = "system";
    
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private Constants() {
    }
}
