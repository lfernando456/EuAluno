package com.andreoid.EuAluno;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.andreoid.EuAluno.fragment.MainFragment;
import com.andreoid.EuAluno.fragment.ViewPagerFragment;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class ProfileActivity extends NavigationLiveo implements OnItemClickListener {

    //Textview to show currently logged in user
    private TextView textView;
    private HelpLiveo mHelpLiveo;
    private SharedPreferences pref;
    public String name ;//= "Andre";
    public String email ;//= "andre@asdndsa.sas";


    @Override
    public void onInt(Bundle savedInstanceState) {
        //Fetching email from shared preferences
        pref = getSharedPreferences("EuAluno", Context.MODE_PRIVATE);

        // User Information
        this.userName.setText(pref.getString(Constants.NAME,""));
        this.userEmail.setText(pref.getString(Constants.EMAIL,""));
        this.userPhoto.setImageResource(R.mipmap.ic_no_user);
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.inbox), R.mipmap.ic_inbox_black_24dp, 100);
        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        mHelpLiveo.add(getString(R.string.presence), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.grades), R.mipmap.ic_send_black_24dp);
        mHelpLiveo.add(getString(R.string.disciplines), R.mipmap.ic_drafts_black_24dp);
        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.myProfile), R.mipmap.ic_delete_black_24dp);
        //mHelpLiveo.add(getString(R.string.spam), R.mipmap.ic_report_black_24dp, 120);


        //{optional} - Header Customization - method customHeader
//        View mCustomHeader = getLayoutInflater().inflate(R.layout.custom_header_user, this.getListView(), false);
//        ImageView imageView = (ImageView) mCustomHeader.findViewById(R.id.imageView);

        with(this).startingPosition(2) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())

                        //{optional} - List Customization "If you remove these methods and the list will take his white standard color"
                        //.selectorCheck(R.drawable.selector_check) //Inform the background of the selected item color
                        //.colorItemDefault(R.color.nliveo_blue_colorPrimary) //Inform the standard color name, icon and counter
                        //.colorItemSelected(R.color.nliveo_purple_colorPrimary) //State the name of the color, icon and meter when it is selected
                        //.backgroundList(R.color.nliveo_black_light) //Inform the list of background color
                        //.colorLineSeparator(R.color.nliveo_transparent) //Inform the color of the subheader line

                        //{optional} - SubHeader Customization
                .colorItemSelected(R.color.nliveo_blue_colorPrimary)
                .colorNameSubHeader(R.color.nliveo_blue_colorPrimary)
                        //.colorLineSeparator(R.color.nliveo_blue_colorPrimary)

                .footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                        //.footerSecondItem(R.string.settings, R.mipmap.ic_settings_black_24dp)

                        //{optional} - Header Customization
                        //.customHeader(mCustomHeader)

                        //{optional} - Footer Customization
                        //.footerNameColor(R.color.nliveo_blue_colorPrimary)
                        //.footerIconColor(R.color.nliveo_blue_colorPrimary)
                        //.footerBackground(R.color.nliveo_white)

                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                        //.setOnClickFooterSecond(onClickFooter)
                .build();

        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 2 ? 15 : 0);
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        //FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position){
            case 2:
                mFragment = new ViewPagerFragment();
                break;
            case 6:
                mFragment = new ProfileFragment();
                break;
            default:
                mFragment = MainFragment.newInstance(position + "");
                break;
        }

        if (mFragment != null){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,mFragment);
            ft.commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "ClickNaFoto()", Toast.LENGTH_SHORT).show();
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            closeDrawer();
        }
    };


    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Você deseja deslogar?");
        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Constants.IS_LOGGED_IN, false);
                        editor.putString(Constants.EMAIL,"");
                        editor.putString(Constants.NAME,"");
                        editor.putString(Constants.UNIQUE_ID, "");
                        editor.apply();


                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }
}
