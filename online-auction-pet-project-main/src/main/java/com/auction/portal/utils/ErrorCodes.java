package com.auction.portal.utils;

public class ErrorCodes {

    public static final String USER_NOT_FOUND = "USER NOT FOUND!";
    public static final String EMAIL_ALREADY_REGISTERED = "EMAIL ALREADY REGISTERED WITH ANOTHER USER!";
    public static final String EMAIL_INVALID = "EMAIL INVALID, PLEASE GIVE A VALID EMAIL";
    public static final String PASSWORD_INVALID = "PASSWORD NOT IN THE GIVEN FORMAT!";
    public static final String FORBIDDEN = "USER IS FORBIDDEN TO ACCESS THIS FEATURE";
    public static final String NOT_NULLABLE = "FIELD CANNOT BE NULL";
    public static final String BID_MINVALUE_REQUIRED = "PROVIDED VALUE SHOULD BE GREATER THAN MINIMUM VALUE FOR BIDDING";
    public static final String IMAGE_SIZE_EXCEEDED = "MAXIMUM IMAGE SIZE ALLOWED IS 5MB";
    public static final String EXP_DATE_MIN_1DAY = "EXPIRY DATE SHOULD BE MINIMUM 1 DAY FROM CURRENT DATE";
    public static final String PRICE_ALREADY_BID = "CURRENT PRICE IS ALREADY BID FOR THE AUCTION";
    public static final String BID_LESS_THAN_CURRENT_BID = "CANNOT BID LESS THAN CURRENT BID";
    public static final String AUCTION_NOT_FOUND = "AUCTION NOT FOUND!";
    public static final String BID_NOT_FOUND = "BID NOT FOUND!";
    public static final String PRODUCT_NOT_FOUND = "PRODUCT WAS NOT FOUND";
    public static final String BID_INVALID = "INVALID BID!";
    public static final String EMAIL_NOT_NULL = "EMAIL SHOULD NOT BE EMPTY";
    public static final String USER_NAME_NOT_NULL = "USERNAME SHOULD NOT BE EMPTY";
    public static final String FILE_NOT_FOUND = "FILE NOT FOUND!";
    public static final String IMAGE_UPLOAD_ERROR = "UNABLE TO CREATE DIRECTORY FOR UPLOADING IMAGES";
    public static final String BAD_CREDENTIALS = "EMAIL OR PASSWORD INVALID!";
}
