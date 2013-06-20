package com.vampireneoapp.passiontimes;

import android.accounts.AccountManager;
import android.content.Context;

import com.vampireneoapp.passiontimes.authenticator.BootstrapAuthenticatorActivity;
import com.vampireneoapp.passiontimes.authenticator.LogoutService;
import com.vampireneoapp.passiontimes.core.CheckIn;
import com.vampireneoapp.passiontimes.core.TimerService;
import com.vampireneoapp.passiontimes.ui.ArticleActivity;
import com.vampireneoapp.passiontimes.ui.ArticleListFragment;
import com.vampireneoapp.passiontimes.ui.BootstrapTimerActivity;
import com.vampireneoapp.passiontimes.ui.CarouselActivity;
import com.vampireneoapp.passiontimes.ui.CheckInsListFragment;
import com.vampireneoapp.passiontimes.ui.ItemListFragment;
import com.vampireneoapp.passiontimes.ui.NewsActivity;
import com.vampireneoapp.passiontimes.ui.NewsListFragment;
import com.vampireneoapp.passiontimes.ui.UserActivity;
import com.vampireneoapp.passiontimes.ui.UserListFragment;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module for setting up provides statements.
 * Register all of your entry points below.
 */
@Module
(
        complete = false,

        injects = {
                BootstrapApplication.class,
                BootstrapAuthenticatorActivity.class,
                CarouselActivity.class,
                BootstrapTimerActivity.class,
                CheckInsListFragment.class,
                NewsActivity.class,
                NewsListFragment.class,
                UserActivity.class,
                UserListFragment.class,
                TimerService.class,
                ArticleActivity.class,
                ArticleListFragment.class
        }

)
public class BootstrapModule  {

    @Singleton
    @Provides
    Bus provideOttoBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    LogoutService provideLogoutService(final Context context, final AccountManager accountManager) {
        return new LogoutService(context, accountManager);
    }

}
