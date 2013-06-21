
package com.vampireneoapp.passiontimes;

import android.accounts.AccountsException;
import android.app.Activity;

import com.vampireneoapp.passiontimes.authenticator.ApiKeyProvider;
import com.vampireneoapp.passiontimes.core.PassionTimesService;
import com.vampireneoapp.passiontimes.core.UserAgentProvider;

import java.io.IOException;

import javax.inject.Inject;

/**
 * Provider for a {@link com.vampireneoapp.passiontimes.core.BootstrapService} instance
 */
public class PassionTimesServiceProvider {

    @Inject ApiKeyProvider keyProvider;
    @Inject UserAgentProvider userAgentProvider;

    /**
     * Get service for configured key provider
     * <p>
     * This method gets an auth key and so it blocks and shouldn't be called on the main thread.
     *
     * @return bootstrap service
     * @throws java.io.IOException
     * @throws android.accounts.AccountsException
     */
    public PassionTimesService getService(Activity activity) throws IOException, AccountsException {
        return new PassionTimesService(keyProvider.getAuthKey(activity), userAgentProvider);
    }
}
