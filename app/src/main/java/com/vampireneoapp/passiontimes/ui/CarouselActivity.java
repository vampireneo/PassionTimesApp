

package com.vampireneoapp.passiontimes.ui;

import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.vampireneoapp.passiontimes.BootstrapServiceProvider;
import com.vampireneoapp.passiontimes.R;
import com.vampireneoapp.passiontimes.core.BootstrapService;
import com.vampireneoapp.passiontimes.util.SafeAsyncTask;
import com.viewpagerindicator.TitlePageIndicator;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import net.simonvt.menudrawer.MenuDrawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Activity to view the carousel and view pager indicator with fragments.
 */
public class CarouselActivity extends BootstrapFragmentActivity {// implements ActionBar.OnNavigationListener{

    @InjectView(R.id.tpi_header) TitlePageIndicator indicator;
    @InjectView(R.id.vp_pages) ViewPager pager;

    @Inject BootstrapServiceProvider serviceProvider;

    private MenuDrawer menuDrawer;

    //private boolean userHasAuthenticated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        // Set up navigation drawer
        menuDrawer = MenuDrawer.attach(this);
        menuDrawer.setMenuView(R.layout.navigation_drawer);
        menuDrawer.setContentView(R.layout.carousel_view);
        menuDrawer.setSlideDrawable(R.drawable.ic_drawer);
        menuDrawer.setDrawerIndicatorEnabled(true);

        /*String[] articleCategories = getResources().getStringArray(R.array.articleCategories);
        ArrayList<String> categoryList = new ArrayList<String>(Arrays.asList(articleCategories));
        categoryList.add("test");
        Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<String> list = new ArrayAdapter<String>(context, R.layout.sherlock_spinner_item, categoryList);
        //ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.articleCategories, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        //list.add("test");


        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this);*/

        Views.inject(this);

        //checkAuth();
        initScreen();
    }

    private void initScreen() {
        //if(userHasAuthenticated) {
            pager.setAdapter(new BootstrapPagerAdapter(getResources(), getSupportFragmentManager()));

            indicator.setViewPager(pager);
            pager.setCurrentItem(0);

        //}

        setNavListeners();
    }

    /*private void checkAuth() {
        new SafeAsyncTask<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                    final BootstrapService svc = serviceProvider.getService(CarouselActivity.this);
                    return svc != null;

            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                if(e instanceof OperationCanceledException) {
                    // User cancelled the authentication process (back button, etc).
                    // Since auth could not take place, lets finish this activity.
                    finish();
                }
            }

            @Override
            protected void onSuccess(Boolean hasAuthenticated) throws Exception {
                super.onSuccess(hasAuthenticated);
                userHasAuthenticated = true;
                initScreen();
            }
        }.execute();
    }*/


    private void setNavListeners() {

        menuDrawer.findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDrawer.toggleMenu();
            }
        });

        /*menuDrawer.findViewById(R.id.timer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDrawer.toggleMenu();
                navigateToTimer();
            }
        });*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                menuDrawer.toggleMenu();
                return true;
/*            case R.id.timer:
                navigateToTimer();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        //mSelected.setText("Selected: " + mLocations[itemPosition]);
        return true;
    }*/
/*    private void navigateToTimer() {
        final Intent i = new Intent(this, BootstrapTimerActivity.class);
        startActivity(i);
    }*/
}
