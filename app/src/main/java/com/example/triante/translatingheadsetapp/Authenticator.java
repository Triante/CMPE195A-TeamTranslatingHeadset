package com.example.triante.translatingheadsetapp;

import java.io.IOException;

/**
 * Created by Jorge Aguiniga on 10/7/2016.
 */

/*Authenticator interface for grabbing token needed to do 
    translations (MicrosoftTranslate) or speech recognition/synthesis (IBM)
*/
public interface Authenticator {
    
    /*Method creates a placeholder for storing the Authentication token from the cloud service (Microsoft or IBM) */
    public void createAuthToken() throws IOException;
    /* Method to check whether the current token has expired */
    public boolean isExpired();
    /* Method to retrieve the token from one of the cloud services (Microsoft or IBM) */
    public String getAuthToken();
}
