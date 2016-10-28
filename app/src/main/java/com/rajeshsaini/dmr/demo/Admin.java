package com.rajeshsaini.dmr.demo;

/**
 * Created by DMRSAINI on 3/10/2016.
 */

public class Admin {
    public static final String USER_ID="user_id";
    public static final String USER_PASS="user_pass";
    public static final String USER_NAME="name";
    public static final String CHILD_ID="child_id";
    public  static final String USER_PROFILE="user_profile";
    public static final String USER_AGENT = "Mozilla/5.0";
    public static final String REQUEST="request";
    public static final String SN="sn";
    public static final String ACTION="action";
    public static final String CONFIRM="CONFIRM";
    public static final String REJECT="REJECT";
    public static final String MESSAGE="message";
    public static final String SUCCESS="success";
    public static final String GENDER="gender";
    public static final String ADDRESS="address";
    public static final String DOB="dob";
    public static final String EMAIL="email";
    public static final String IMAGE="image";
    public static final String LATITUDE="latitude";
    public static final String LONGITUDE="longitude";

    public static final String DATE="date";
    public static final String TIME="time";
    public static final String MOBILE="mobile";
    public static final String DURATION="duration";
    public static final String TYPE="type";
    public static final String LOG="log";

    public static final String OLD_CALL_UPDATE="old_call_update";

    public static final String CALL_MISS="3";
    public static final String CALL_OUTGOING="2";
    public static final String CALL_INCOMING="1";
//    private static final String URL_ADDRESS="http://192.168.56.1/demo";
//    private static final String GIFT_CARD_IMAGE_PATH="http://192.168.56.1/demo/profile/";
//    private static final String URL_ADDRESS="http://192.168.43.64/demo";
//    private static final String GIFT_CARD_IMAGE_PATH="http://192.168.43.64/demo/profile/";

//    private static final String URL_ADDRESS="http://10.0.2.2/demo";
//    private static final String GIFT_CARD_IMAGE_PATH="http://10.0.2.2/demo/profile/";

//    private static final String URL_ADDRESS="http://192.168.0.101/rajesh/demo";
//    private static final String GIFT_CARD_IMAGE_PATH="http://192.168.0.101/rajesh/demo/profile/";

    private static final String URL_ADDRESS="http://dmrcoding.com/android/web_services_t_child";
    private static final String GIFT_CARD_IMAGE_PATH="http://dmrcoding.com/android/web_services_t_child/profile/";

    public static final String getUserInfo(){
        return URL_ADDRESS.concat("/user_login.php");
    }
    public static final String getAllChild(){
        return URL_ADDRESS.concat("/user.php");
    }
    public static final String getMyProfile(){
        return URL_ADDRESS.concat("/profile.php");
    }public static final String getImagePath(String url){
        return GIFT_CARD_IMAGE_PATH.concat(url);
    }
    public static final String getUploadServerImage(){
        return URL_ADDRESS.concat("/UploadToServer.php");
    }
    public static final String getProfileUpdate(){
        return URL_ADDRESS.concat("/profileUpdate.php");
    }
    public static final String getChildRequest(){
        return URL_ADDRESS.concat("/RequestToChild.php");
    }public static final String getParentRequest(){
        return URL_ADDRESS.concat("/ParentRequest.php");
    }
    public static final String getRequestAction(){
        return URL_ADDRESS.concat("/RequestAction.php");
    }
    public static final String getLocationUpdate(){
        return URL_ADDRESS.concat("/loation_update.php");
    }
    public static final String getCallUpdate(){
        return URL_ADDRESS.concat("/call_log_update.php");
    }
    public static final String getCallLogs(){
        return URL_ADDRESS.concat("/call_detetails.php");
    }
    public static final String getLastLocation(){
        return URL_ADDRESS.concat("/loation_last.php");
    }
}
