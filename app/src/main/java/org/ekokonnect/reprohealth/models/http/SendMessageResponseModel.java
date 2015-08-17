package org.ekokonnect.reprohealth.models.http;

/**
 * Send message http response model
 * Created by oyewale on 8/15/15.
 */
public class SendMessageResponseModel {
    public int status;

    public Message message;

    public static class Message{
        public long mid;

        public MessageObject msg;

        public static class MessageObject{
            public long date;
        }
    }
}
