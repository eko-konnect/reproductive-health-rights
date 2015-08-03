package org.ekokonnect.reprohealth.models.http;

/**
 * UserAuth Response Model
 * Created by oyewale on 8/1/15.
 */
public class UserAuthResponse {

    public UserMessage message;

    public static class UserMessage{
        public Long uid;
    }
}
