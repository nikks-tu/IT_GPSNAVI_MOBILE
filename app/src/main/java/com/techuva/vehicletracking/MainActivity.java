package com.techuva.vehicletracking;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.techuva.vehicletracking.activity.LoginActivity;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.fragments.AboutApp;
import com.techuva.vehicletracking.fragments.ChangePasswordFragment;
import com.techuva.vehicletracking.fragments.PrivacyPolicy;
import com.techuva.vehicletracking.fragments.TermsAndConditions;
import com.techuva.vehicletracking.ui.home.HomeFragment;
import com.techuva.vehicletracking.utils.CustomTypefaceSpan;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FragmentManager fragmentManager;
    Context mainContext;
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;
    TextView navUsername, navVersionCode, tv_email, tv_title;
    Typeface faceDenmark;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        mainContext = MainActivity.this;
         navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tv_title = toolbar.findViewById(R.id.tv_title);
        navUsername = headerView.findViewById(R.id.tv_user_name);
        tv_email = headerView.findViewById(R.id.tv_email);
        navVersionCode = headerView.findViewById(R.id.last_logged_time);
        String userName = MApplication.getString(mainContext, Constants.UserName);
        String email_id = MApplication.getString(mainContext, Constants.UserMailId);
        tv_email.setText(email_id);
        String firstLetterCapitalized =
                userName.substring(0, 1).toUpperCase(Locale.getDefault()) + userName.substring(1);
        navUsername.setText(firstLetterCapitalized);
        navVersionCode.setText(mainContext.getResources().getString(R.string.last_login)+" --");
        navVersionCode.setVisibility(View.GONE);
        Typeface faceLight = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/AvenirLTStd-Medium.otf");

        faceDenmark = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_title.setText(mainContext.getResources().getString(R.string.app_name));
        tv_title.setTypeface(faceDenmark);
        navUsername.setTypeface(faceLight);
        navVersionCode.setTypeface(faceLight);
        tv_email.setTypeface(faceLight);
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        setUpMenu();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        exitToast = Toast.makeText(getApplicationContext(), mainContext.getResources().getString(R.string.exit_text), Toast.LENGTH_SHORT);
        loadFragments(HomeFragment.newInstance(),"Home");
    }

    private void setUpMenu() {

        Menu menuNav = navigationView.getMenu();
        MenuItem nav_vehicles = menuNav.findItem(R.id.nav_vehicles);
        nav_vehicles.setEnabled(false);
        MenuItem nav_devices = menuNav.findItem(R.id.nav_devices);
        nav_devices.setEnabled(false);
        MenuItem nav_driver = menuNav.findItem(R.id.nav_driver);
        nav_driver.setEnabled(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_home: {
                tv_title.setText(mainContext.getResources().getString(R.string.app_name));
                //tv_title.setTextColor(mainContext.getResources().getColor(R.color.bug_red));
                tv_title.setTypeface(faceDenmark);
                loadFragments(HomeFragment.newInstance(),"Home");
                break;
            }
            case R.id.change_password: {
                loadFragments(ChangePasswordFragment.newInstance(), "ChangePassword");
                break;
            }
            case R.id.about_app: {
                loadFragments(AboutApp.newInstance(), "AboutApp");
                break;
            }
            case R.id.terms_and_conditions: {
                loadFragments(TermsAndConditions.newInstance(), "TermsAndConditions");
                break;
            }
            case R.id.privacy_policy: {
                loadFragments(PrivacyPolicy.newInstance(), "PrivacyPolicy");
                break;
            }
            case R.id.logout: {
                gotologout();
                break;
            }
        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public  void loadFragments(Fragment fragment, String tag)
    {
        fragmentManager =getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment, tag).commit();
    }

    private void gotologout() {
        MApplication.setString(mainContext, Constants.AccessToken, "");
        MApplication.setString(mainContext, Constants.UserID, "");
        MApplication.setString(mainContext, Constants.UserName, "");
        MApplication.setBoolean(mainContext, Constants.IsLoggedIn, false);
        MApplication.setString(mainContext, Constants.CompanyID, "");
        MApplication.setString(mainContext, Constants.COMPANY_ID, "");
        MApplication.setString(mainContext, Constants.UserIDSelected, "");
        MApplication.setBoolean(mainContext, Constants.IsDefaultDeviceSaved, false);
        Intent intent = new Intent(mainContext, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Light.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());

        SpannableString spannableString = new SpannableString("Menu item");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, spannableString.length(), 0);


        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        mi.setTitle(mNewTitle);
    }


    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
           finishAffinity();
            super.onBackPressed();
        }
    }
}
