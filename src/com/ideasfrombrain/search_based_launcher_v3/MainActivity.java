package com.ideasfrombrain.search_based_launcher_v3;


import java.util.ArrayList;

import java.util.List;


import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


import android.view.inputmethod.InputMethodManager;

import android.widget.ViewAnimator;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;



public class MainActivity extends Activity {


	public static final int FIRST_INDEX = 0;
	static String APP_PACKAGE_NAME ="com.ideasfrombrain.search_based_launcher_v3";
	
    boolean NewerAndroid=true;

    PackageList pkg = new PackageList();
    PackageList pkgFiltered = new PackageList();
    PackageList pkgExtra = new PackageList();
    PackageList pkgHide = new PackageList();
    PackageList pkgRecent = new PackageList();
    
    EditText DialogInput;
     
    private final BroadcastReceiver mPkgApplicationsReceiver = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {
				loadApps();
					boolean found = false;
					//ArrayList<String> pkgRecentFound = new ArrayList<String>(200);
					EscapeToBeSure: for(int i=0; i<pkgRecent.Name.size(); i++  ) 
        	    	{				
	    	        	for(int j=0;j<pkg.Name.size(); j++) {
	    	        		if(pkg.Activity.get(j).equals(pkgRecent.Activity.get(i))){
	    	        				found = true;
	    	        				//pkgRecentFound.add()
	    	        		}
	    	        	}
	    	        	if(!found) {
	    	        		pkgRecent.Activity.remove(i);
	    	        		pkgRecent.Name.remove(i);
	    	        		pkgRecent.Nick.remove(i);
	    	        		i--;
	    	        	}
	    	        	else {
	    	        		found=false;
	    	        	}
	    	        	if(i>=pkgRecent.Name.size()) {break EscapeToBeSure;}
        	    	}
					EscapeToBeSure2: for(int i=0; i<pkgHide.Name.size(); i++  ) 
        	    	{				
	    	        	for(int j=0;j<pkg.Name.size(); j++) {
	    	        		if(pkg.Activity.get(j).equals(pkgHide.Activity.get(i))){
	    	        				found = true;
	    	        				//pkgHideFound.add()
	    	        		}
	    	        	}
	    	        	if(!found) {
	    	        		pkgHide.Activity.remove(i);
	    	        		pkgHide.Name.remove(i);
	    	        		pkgHide.Nick.remove(i);
	    	        		i--;
	    	        	}
	    	        	else {
	    	        		found=false;
	    	        	}
	    	        	if(i>=pkgHide.Name.size()) {break EscapeToBeSure2;}
        	    	}
					EscapeToBeSure3: for(int i=0; i<pkgExtra.Name.size(); i++  ) 
        	    	{				
	    	        	for(int j=0;j<pkg.Name.size(); j++) {
	    	        		if(pkg.Activity.get(j).equals(pkgExtra.Activity.get(i))){
	    	        				found = true;
	    	        				//pkgExtraFound.add()
	    	        		}
	    	        	}
	    	        	if(!found) {
	    	        		pkgExtra.Activity.remove(i);
	    	        		pkgExtra.Name.remove(i);
	    	        		pkgExtra.Nick.remove(i);
	    	        		i--;
	    	        	}
	    	        	else {
	    	        		found=false;
	    	        	}
	    	        	if(i>=pkgExtra.Name.size()) {break EscapeToBeSure3;}
        	    	}
        	    
        	    SaveExtRemLists(false);
				loadApps();
				refresh();
		}
    };
	private SearchText searchText;
	private AppListView appListView;
	private AutostartButton autostartButton;
	private WifiButton wifiButton;
	private BluetoothButton bluetoothButton;
	private FlashButton flashButton;
	private CameraButton cameraButton;
	private RadioButtons radioButtons;

	private void registerIntentReceivers() {
        IntentFilter pkgFilter = new IntentFilter( );
        pkgFilter.addAction( Intent.ACTION_PACKAGE_ADDED );
        pkgFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pkgFilter.addDataScheme("package");
        registerReceiver(mPkgApplicationsReceiver, pkgFilter);
    }
    
    public void loadApps() {
    	Log.d("DEBUG", "start loading apps");
		Log.d("DEBUG", "activity arrays prepared");

           final Intent main=new Intent(Intent.ACTION_MAIN, null);
           final PackageManager pm = getPackageManager();
           
           int i=0;
           int j=0;
           String tmpNick="";
          
            if(pkg.Name.size()!=0 ) {pkg.Name.clear(); pkg.Activity.clear(); pkg.Nick.clear();}
        
        	
           switch(radioButtons.getCheckedRadioButton()) {
           case 0:
        	        // LAUCHABLES VARIANT OF LOAD APPS
        	        
   	        
		   	        for(i=0; i<pkgExtra.Name.size(); i++  ) 
		   	    	{
		   	        	pkg.Name.add(pkgExtra.Name.get(i));
		   	        	pkg.Nick.add(pkgExtra.Nick.get(i));
		   	        	pkg.Activity.add(pkgExtra.Activity.get(i));
		   	    	}
        	            
        	        main.addCategory(Intent.CATEGORY_LAUNCHER); // will show only Regular Apps
        	        final List<ResolveInfo> launchables=pm.queryIntentActivities(main, 0);
        	        
        	        for(ResolveInfo launchable : launchables) 
        	    	{
        	        	if (ItemNumInHide(launchable.activityInfo.name)==-1)
        	        	{
            	        	pkg.Name.add(launchable.activityInfo.packageName);
            	        	pkg.Activity.add(launchable.activityInfo.name);
            	        	pkg.Nick.add(launchable.activityInfo.loadLabel(pm).toString());//tmpSplit[length-2]+ ":" +tmpSplit[length-1];
        	        	}


        	    	}

        	break;
            case 1:
        		
        			final List<ResolveInfo> launchables2=pm.queryIntentActivities(main, 0);

        			int length=0;
        	        for(ResolveInfo launchable : launchables2) 
        	    	{
        	        	String[] tmpSplit = new String[10];
        	            tmpSplit=launchable.activityInfo.name.split("\\.");
 					    length=tmpSplit.length;

        	        	pkg.Name.add(launchable.activityInfo.packageName);
        	        	pkg.Activity.add(launchable.activityInfo.name);
        	        	tmpNick="";
        	        	tmpNick=tmpSplit[1];
        	        	for(j=2; j<length; j++) {
        	        		tmpNick=tmpNick + ":" + tmpSplit[j];
        	        	}
        	        	
        	        	pkg.Nick.add(tmpNick);
        	    	}


            break;
            case 2:

    	        for(i=0; i<pkgExtra.Name.size(); i++  ) 
    	    	{
    	        	pkg.Name.add(pkgExtra.Name.get(i));
    	        	pkg.Nick.add(pkgExtra.Nick.get(i));
    	        	pkg.Activity.add(pkgExtra.Activity.get(i));
    	    	}

           break;
            case 3:

    	        for(i=0; i<pkgHide.Name.size(); i++  ) 
    	    	{

    	        	pkg.Name.add(pkgHide.Name.get(i));
    	        	pkg.Nick.add(pkgHide.Nick.get(i));
    	        	pkg.Activity.add(pkgHide.Activity.get(i));
    	    	}

           break;
           }
           pkg.Name.add(APP_PACKAGE_NAME + ".Menu");
       	   pkg.Nick.add(" Menu-Launcher");
       	   pkg.Activity.add(APP_PACKAGE_NAME + ".Menu");

     }
        
        
    public void LoadExtRemLists(boolean check) {
    	try{
        	final Context myContext =  getApplicationContext();
        	LoadList(pkgExtra.Name,"pkgExtra.Name", myContext);
        	LoadList(pkgExtra.Nick,"pkgExtra.Nick", myContext);
        	LoadList(pkgExtra.Activity,"pkgExtra.Activity", myContext);
        	LoadList(pkgHide.Name,"pkgHide.Name", myContext);
        	LoadList(pkgHide.Nick,"pkgHide.Nick", myContext);
        	LoadList(pkgHide.Activity,"pkgHide.Activity", myContext);	
        	LoadList(pkgRecent.Name,"pkgRecent.Name", myContext);
        	LoadList(pkgRecent.Nick,"pkgRecent.Nick", myContext);
        	LoadList(pkgRecent.Activity,"pkgRecent.Activity", myContext);
    	}
    	catch(Exception e) {
    		//Log.d("DEBUG","excep. during load" + e.getStackTrace().toString());
    		SaveExtRemLists(false);
    	}
    }
    
    public void SaveExtRemLists(boolean check) {
    	final Context myContext =  getApplicationContext();
    	SaveList(pkgExtra.Name,"pkgExtra.Name", myContext);
    	SaveList(pkgExtra.Nick,"pkgExtra.Nick", myContext);
    	SaveList(pkgExtra.Activity,"pkgExtra.Activity", myContext);
    	SaveList(pkgHide.Name,"pkgHide.Name", myContext);
    	SaveList(pkgHide.Nick,"pkgHide.Nick", myContext);
    	SaveList(pkgHide.Activity,"pkgHide.Activity", myContext);
    	SaveList(pkgRecent.Name,"pkgRecent.Name", myContext);
    	SaveList(pkgRecent.Nick, "pkgRecent.Nick", myContext);
    	SaveList(pkgRecent.Activity, "pkgRecent.Activity", myContext);
    }

    public boolean SaveList(List<String> list, String listName, Context mContext) { 
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
        SharedPreferences.Editor editor = prefs.edit();  
        editor.putInt(listName + "_size", list.size());
        for(int i=0;i<list.size();i++)  
            editor.putString(listName + "_" + i, list.get(i));  
        return editor.commit();  
    } 

    

    
    public void LoadList(ArrayList<String> List, String ListName, Context mContext) {  
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
        int size = prefs.getInt(ListName + "_size", 0);  
        List.clear();
        for(int i=0;i<size;i++)  
            List.add(prefs.getString(ListName + "_" + i, null));  
    }  
    
    @Override
    public boolean onKeyUp(int keycode, KeyEvent event ) {
     if(keycode == KeyEvent.KEYCODE_MENU){
    	 myShowNext(false);
     }
     else if(keycode == KeyEvent.KEYCODE_SEARCH ) {
    	 startSearch("",false,null,true);
     }
     else if(keycode == KeyEvent.KEYCODE_BACK ) {
    	  ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
    	 	if(mViewAnimator.getDisplayedChild()==1) {
    	 		myShowNext(false);
    	 	}
     }
     else {
    	 return super.onKeyUp(keycode,event);  
     }
         return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appListView = new AppListView(this);
		searchText = new SearchText(this);
		autostartButton = new AutostartButton(this);
		wifiButton = new WifiButton(this);
		bluetoothButton = new BluetoothButton(this);
		flashButton = new FlashButton(this);
		cameraButton = new CameraButton(this);


		//---------------MENU CODE
            
            
            
   	   	 findViewById(R.id.donateButton).setOnClickListener(new View.OnClickListener() {
			 public void onClick(View v) {
				 Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME));
				 marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				 startActivity(marketIntent);
			 }
		 });
   	    	 
   	       	findViewById(R.id.donateButton).setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PACKAGE_NAME + "_donate"));
					marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					startActivity(marketIntent);
				}
			});

		radioButtons = new RadioButtons(this);


		//android version check
   	       	String Aversion = android.os.Build.VERSION.RELEASE;
        	if(savedInstanceState==null) {
        		if (Aversion.startsWith("1.") ||
        				Aversion.startsWith("2.0") ||
        				Aversion.startsWith("2.1"))
        			 { NewerAndroid = false;}
        		else {
        				NewerAndroid = true;
        		}
        		
        		LoadExtRemLists(false);
        		loadApps();
        	}
        	else {

                NewerAndroid = savedInstanceState.getBoolean( "NewerAndroid");
                
                pkg.Name=savedInstanceState.getStringArrayList("pkg.Name");
                pkg.Nick=savedInstanceState.getStringArrayList("pkg.Nick");
                pkg.Activity=savedInstanceState.getStringArrayList("pkg.Activity");
                
                pkgExtra.Name=savedInstanceState.getStringArrayList("pkgExtra.Name");
                pkgExtra.Nick=savedInstanceState.getStringArrayList("pkgExtra.Nick");
                pkgExtra.Activity=savedInstanceState.getStringArrayList("pkgExtra.Activity");
                
                pkgHide.Name=savedInstanceState.getStringArrayList("pkgHide.Name");
                pkgHide.Nick=savedInstanceState.getStringArrayList("pkgHide.Nick");
                pkgHide.Activity=savedInstanceState.getStringArrayList("pkgHide.Activity");
                
                pkgRecent.Name=savedInstanceState.getStringArrayList("pkgRecent.Name");
                pkgRecent.Nick=savedInstanceState.getStringArrayList("pkgRecent.Nick");
                pkgRecent.Activity=savedInstanceState.getStringArrayList("pkgRecent.Activity");
                
        	}
        	registerIntentReceivers();        
        
        
    

    }

    
    public void myShowNext(Boolean DoLoadApps) {
		searchText.setNormalColor();
       	ViewAnimator mViewAnimator = (ViewAnimator) findViewById(R.id.viewAnimator);
    	//mViewAnimator.setInAnimation(null);  // Skipping this will cause trouble
       // mViewAnimator.setOutAnimation(null);
   	 	mViewAnimator.showNext();
    	
   	 	if(mViewAnimator.getDisplayedChild()==0) {
   	 		//final TextView myToggleButton = (TextView) findViewById(R.id.toggleButton0);
   	 		if (DoLoadApps) {loadApps();}
   	 		
   	 		if(radioButtons.getCheckedRadioButton() > 0) {
   	 			searchText.setSpaceCharacterToText();
				radioButtons.setInvisible();
   	 		}
   	 		else {
				wifiButton.setVisibleIfAvailable();
				bluetoothButton.setVisibleIfAvailable();
				cameraButton.setVisibleIfAvailable();
   	 			searchText.clearText();
   	 		}
   	 		
   	 	}
   	 	else {
   	 		RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
   	 		mRadioGroup.requestFocus();
   	 	}
    	
 
    	
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    // Save UI state changes to the savedInstanceState.
    // This bundle will be passed to onCreate if the process is
    // killed and restarted.

        radioButtons.save();
        savedInstanceState.putBoolean( "NewerAndroid", NewerAndroid);
        
        savedInstanceState.putStringArrayList("pkg.Name", pkg.Name);
        savedInstanceState.putStringArrayList("pkg.Nick", pkg.Nick);
        savedInstanceState.putStringArrayList("pkg.Activity", pkg.Activity);

        savedInstanceState.putStringArrayList("pkgExtra.Name", pkgExtra.Name);
        savedInstanceState.putStringArrayList("pkgExtra.Nick", pkgExtra.Nick);
        savedInstanceState.putStringArrayList("pkgExtra.Activity", pkgExtra.Activity);
        
        savedInstanceState.putStringArrayList("pkgHide.Name", pkgHide.Name);
        savedInstanceState.putStringArrayList("pkgHide.Nick", pkgHide.Nick);
        savedInstanceState.putStringArrayList("pkgHide.Activity", pkgHide.Activity);
        
        savedInstanceState.putStringArrayList("pkgRecent.Name", pkgRecent.Name);
        savedInstanceState.putStringArrayList("pkgRecent.Nick", pkgRecent.Nick);
        savedInstanceState.putStringArrayList("pkgRecent.Activity", pkgRecent.Activity);
        
        super.onSaveInstanceState(savedInstanceState);
    }

    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

     }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        searchText.setNormalColor();
		if((radioButtons.getCheckedRadioButton() == 0)  && autostartButton.isAutostart()) {
			searchText.clearText();
		 }

		if(!NewerAndroid) {
			final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null){
			   imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	    	}
		}
	
			
    }
    
    
    @Override
    public void onDestroy() {
		wifiButton.unregisterReceiver();
        unregisterReceiver(mPkgApplicationsReceiver);
    	super.onDestroy();	
    }
 
	public void runApp(int index) {
		searchText.setActivatedColor();

		int tmpint = ItemNumInRecent(pkgFiltered.Activity.get(index));
		if((pkgRecent.Nick.size() >=10) && (tmpint==-1)){
			pkgRecent.Name.remove(0);
    		pkgRecent.Nick.remove(0);
    		pkgRecent.Activity.remove(0);   		
		}
		else if  (tmpint!=-1) {
			pkgRecent.Name.remove(tmpint);
    		pkgRecent.Nick.remove(tmpint);
    		pkgRecent.Activity.remove(tmpint);
		}
		final Context myContext =  getApplicationContext();
    	SaveList(pkgRecent.Name, "pkgRecent.Name", myContext);
    	SaveList(pkgRecent.Nick, "pkgRecent.Nick", myContext);
    	SaveList(pkgRecent.Activity, "pkgRecent.Activity", myContext);
		
		String tmpNickBefore = pkgFiltered.Nick.get(index);
		pkgRecent.Name.add(pkgFiltered.Name.get(index));
			if((tmpNickBefore.matches("R:.*"))  ) {tmpNickBefore=tmpNickBefore.substring(2,tmpNickBefore.length());}
		pkgRecent.Nick.add( tmpNickBefore);
		pkgRecent.Activity.add(pkgFiltered.Activity.get(index));
		
		if(!NewerAndroid) {
			final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null){
			   imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	    	}
		}
		
		
		if((APP_PACKAGE_NAME +".Menu").equals(pkgFiltered.Name.get(index))) {
			myShowNext(false);
		}
		else {
		 try{
	
	        	final Intent intent = new Intent(Intent.ACTION_MAIN, null);
	        	intent.addCategory(Intent.CATEGORY_LAUNCHER);

			 intent.setComponent(new ComponentName(pkgFiltered.Name.get(index) , pkgFiltered.Activity.get(index)));

	        	
	        	
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  |
	                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	        	startActivity( intent);
	        	



		 }	
		 catch(Exception e) {
			// TODO Auto-generated catch block
			Log.d("DEBUG", e.getMessage());
			searchText.setNormalColor();

			if(!NewerAndroid) {
				final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null){
				   imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
		    	}
			}
		  }
		}
		
	}


	public void refresh() {
		pkgFiltered.Name.clear();
		pkgFiltered.Nick.clear();
		pkgFiltered.Activity.clear();
			String FilterText = searchText.getFilterText();
			

			int tmpsize = pkgRecent.Name.size();
			for(int i=0; i<tmpsize; i++)
			{
				//Log.d("DEBUG", pkgRecent.Name.get(tmpsize-1-i));
		           if (pkgRecent.Nick.get(tmpsize-1-i).toLowerCase().matches(FilterText) )  
				   {
		        	   pkgFiltered.Name.add(pkgRecent.Name.get(tmpsize-1-i));
		        	   pkgFiltered.Activity.add(pkgRecent.Activity.get(tmpsize-1-i));
		        	   pkgFiltered.Nick.add("R:" + pkgRecent.Nick.get(tmpsize-1-i));
				   }
			}
			
			for(int i=0; i<pkg.Name.size(); i++)
			{
			   
	           if ((pkg.Nick.get(i).toLowerCase().matches(FilterText) ) && (ItemNumInRecent(pkg.Activity.get(i))==-1))  
			   {
					  pkgFiltered.Name.add(pkg.Name.get(i));
					  pkgFiltered.Activity.add(pkg.Activity.get(i));
					  pkgFiltered.Nick.add(pkg.Nick.get(i));
				  //Log.d("DEBUG", pkg.Nick[i]);
			   }
	           
		   }
		   if(((pkgFiltered.Name.size())==1) && autostartButton.isAutostart()) {
				runApp(FIRST_INDEX) ;
			 }
			 else {
			   appListView.setAppList(pkgFiltered.Nick);
			}
			
		//}
		

	}

	//@Override
	public boolean showOptionsForApp(final int index) {
		final String tmpActivity = pkgFiltered.Activity.get(index);
		final String tmpName= pkgFiltered.Name.get(index);
		String tmpNickBefore= pkgFiltered.Nick.get(index);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(pkgFiltered.Nick.get(index));

		if((tmpActivity== APP_PACKAGE_NAME +".Menu")  ) {return false;}
		if((tmpNickBefore.matches("R:.*"))  ) {tmpNickBefore=tmpNickBefore.substring(2,tmpNickBefore.length());}
		final String tmpNick=tmpNickBefore;
			
		
        switch(radioButtons.getCheckedRadioButton()) {
        case 0:
        	int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);

			if((tmpItemNumInExtra==-1) && (MainActivity.this.ItemNumInHide(tmpActivity)==-1 ) ) {
				DialogInput =   new EditText(this);
				DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				DialogInput.setText(tmpNick);
				
				dialog.setView(DialogInput);
				dialog.setMessage("Hide this application from applications list, rename application (add to Hide and Extra list with diferent names) or uninstall it?");
				dialog.setCancelable(true);
				dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	  //int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);
				        	  if(MainActivity.this.ItemNumInHide(tmpActivity)==-1 )  {
				        		  pkgHide.Name.add(tmpName);
				        		  pkgHide.Nick.add(tmpNick);
				        		  pkgHide.Activity.add(tmpActivity);
				        		  
				        		  int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
				        		  if(tmpItemNumInRecent!=-1) {
				        			  pkgRecent.Nick.remove(tmpItemNumInRecent);
				        			  pkgRecent.Name.remove(tmpItemNumInRecent);
				        			  pkgRecent.Activity.remove(tmpItemNumInRecent);
				        		  }
				        		  
				        		  SaveExtRemLists(false);
				        		  loadApps();
				        		  refresh();
				        		  dialog.dismiss();
				        	  }
				           }
				       });
				
				dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + tmpName));
						startActivity(intent);
						//loadApps();
						refresh();
						dialog.dismiss();
					}
				});
				dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
			        	  if(MainActivity.this.ItemNumInHide(tmpActivity)==-1 )  {
			        		  pkgHide.Name.add(tmpName);
			        		  pkgHide.Nick.add(tmpNick);
			        		  pkgHide.Activity.add(tmpActivity);
			        		  
			        		  pkgExtra.Nick.add(DialogInput.getText().toString());
			        		  pkgExtra.Name.add(tmpName);
			        		  pkgExtra.Activity.add(tmpActivity);
			        		  
			        		  int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
			        		  if(tmpItemNumInRecent!=-1) {
			        			  pkgRecent.Nick.set(tmpItemNumInRecent,DialogInput.getText().toString());
			        		  }
			        		  
			        		  SaveExtRemLists(false);
			        		  loadApps();
			        		  refresh();
			        		  dialog.dismiss();
			        	  }
					}
				});
			}
			else if((tmpItemNumInExtra!=-1) && (MainActivity.this.ItemNumInHide(tmpActivity)!=-1 ) ) {
				DialogInput =   new EditText(this);
				DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
				DialogInput.setText(tmpNick);
				
				dialog.setView(DialogInput);
				dialog.setMessage("This application is in both add and hide lists, thus is probably renamed.");
				dialog.setCancelable(true);
				dialog.setPositiveButton("Hide", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	  int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);
				        	  if(MainActivity.this.ItemNumInHide(tmpActivity)==-1 )  {
				        		  //pkgHide.Name.add(tmpName);
				        		  //pkgHide.Nick.add(tmpNick);
				        		  //pkgHide.Activity.add(tmpActivity);
				        		  pkgExtra.Nick.remove(tmpItemNumInExtra);
				        		  pkgExtra.Name.remove(tmpItemNumInExtra);
				        		  pkgExtra.Activity.remove(tmpItemNumInExtra);
				        		  
				        		  int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
				        		  if(tmpItemNumInRecent!=-1) {
				        			  pkgRecent.Nick.remove(tmpItemNumInRecent);
				        			  pkgRecent.Name.remove(tmpItemNumInRecent);
				        			  pkgRecent.Activity.remove(tmpItemNumInRecent);
				        		  }
				        		  
				        		  SaveExtRemLists(false);
				        		  loadApps();
				        		  refresh();
				        		  dialog.dismiss();
				        	  }
				           }
				       });
				
				dialog.setNeutralButton("Uninstall", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + tmpName));
						startActivity(intent);
						//loadApps();
						refresh();
						dialog.dismiss();
					}
				});
				dialog.setNegativeButton("Rename", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
							  int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);
			        		  //pkgHide.Name.add(tmpName);
			        		  //pkgHide.Nick.add(tmpNick);
			        		  //pkgHide.Activity.add(tmpActivity);
			        		  
			        		  pkgExtra.Nick.remove(tmpItemNumInExtra);
			        		  pkgExtra.Name.remove(tmpItemNumInExtra);
			        		  pkgExtra.Activity.remove(tmpItemNumInExtra);
			        		  
			        		  pkgExtra.Nick.add(DialogInput.getText().toString());
			        		  pkgExtra.Name.add(tmpName);
			        		  pkgExtra.Activity.add(tmpActivity);
			        		  
			        		  int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
			        		  if(tmpItemNumInRecent!=-1) {
			        			  pkgRecent.Nick.set(tmpItemNumInRecent,DialogInput.getText().toString());
			        		  }
			        		  
			        		  SaveExtRemLists(false);
			        		  loadApps();
			        		  refresh();
			        		  dialog.dismiss();
			        	  
					}
				});
			}
			else if(tmpItemNumInExtra!=-1) {
				
				dialog.setMessage("Remove activity " + tmpActivity +  " from extra added (to applications list) list ?");
				dialog.setCancelable(true);
				dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	  int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);
				        		  pkgExtra.Activity.remove(tmpItemNumInExtra);
				        		  pkgExtra.Name.remove(tmpItemNumInExtra);
				        		  pkgExtra.Nick.remove(tmpItemNumInExtra);
				        		  
				        		  int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
				        		  if(tmpItemNumInRecent!=-1) {
				        			  pkgRecent.Nick.remove(tmpItemNumInRecent);
				        			  pkgRecent.Name.remove(tmpItemNumInRecent);
				        			  pkgRecent.Activity.remove(tmpItemNumInRecent);
				        		  }
				        		  
				        		  SaveExtRemLists(false) ;
				        		  loadApps();
				        		  refresh();
				        		  dialog.dismiss();
				        		  
				           }
				       });
			}

			dialog.create().show();
        				
		break;
        case 1:

        	
        	
        	
			DialogInput =   new EditText(this);
			DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			DialogInput.setText(pkgFiltered.Nick.get(index));
			
			dialog.setView(DialogInput);
			dialog.setMessage("Add this activity to applications list?");
			dialog.setCancelable(true);
			dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
				        	  pkgExtra.Name.add(pkgFiltered.Name.get(index));
				   			  pkgExtra.Nick.add(DialogInput.getText().toString() );
				   			  pkgExtra.Activity.add(pkgFiltered.Activity.get(index));
				   			  SaveExtRemLists(true);
				   			  loadApps();
				   			  refresh();
				   			  dialog.dismiss();
			           }
			       });

			dialog.create().show();        	
        	
		break;
        case 2:	
			DialogInput =   new EditText(this);
			DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			DialogInput.setText(pkgFiltered.Nick.get(index));
			
			dialog.setView(DialogInput);
			dialog.setMessage("Remove this (extra added list of all activities) activity from applications list?");
			dialog.setCancelable(true);
			dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
				        	  int tmpItemNumInExtra=MainActivity.this.ItemNumInExtra(tmpActivity);
				        	  if(tmpItemNumInExtra!=-1) {
				        		  pkgExtra.Activity.remove(tmpItemNumInExtra);
				        		  pkgExtra.Name.remove(tmpItemNumInExtra);
				        		  pkgExtra.Nick.remove(tmpItemNumInExtra);
				        		  
				        		  int tmpItemNumInRecent = ItemNumInRecent(tmpActivity);
				        		  if(tmpItemNumInRecent!=-1) {
				        			  pkgRecent.Nick.remove(tmpItemNumInRecent);
				        			  pkgRecent.Name.remove(tmpItemNumInRecent);
				        			  pkgRecent.Activity.remove(tmpItemNumInRecent);
				        		  }
				        		  
				        		  MainActivity.this.SaveExtRemLists(false) ;
				        		  loadApps();
				        		  refresh();
				        		  dialog.dismiss();
				        	  }
			           }
			       });

			dialog.create().show();            	
        	
        break;	
        case 3:    	
			DialogInput =   new EditText(this);
			DialogInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			DialogInput.setText(pkgFiltered.Nick.get(index));
			
			dialog.setView(DialogInput);
			dialog.setMessage("Remove this activity (hidden app) from hidden applications list?");
			dialog.setCancelable(true);
			dialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
				        	  int tmpItemNumInHide=MainActivity.this.ItemNumInHide(tmpActivity);
				        	  if(tmpItemNumInHide!=-1) {
				        		  pkgHide.Activity.remove(tmpItemNumInHide);
				        		  pkgHide.Name.remove(tmpItemNumInHide);
				        		  pkgHide.Nick.remove(tmpItemNumInHide);
				        		  MainActivity.this.SaveExtRemLists(false) ;
				        		  loadApps();
				        		  refresh();
				        		  dialog.dismiss();
				        	  }
			           }
			       });

			dialog.create().show();   
			break;
        }	   
		     
			
		return false;
	}

	public int ItemNumInExtra(String Activity) { // NOTE: if not found returns -1
		for(int i=0; i<pkgExtra.Activity.size(); i++) {
			if(Activity.equals(pkgExtra.Activity.get(i))) {
				return i;
			}
		}
		return -1;
	}

	public int ItemNumInHide(String Activity) { // NOTE: if not found returns -1
		for(int i=0; i<pkgHide.Activity.size(); i++) {
			if(Activity.equals(pkgHide.Activity.get(i))) {	
				return i;
			}
		}
		return -1;
	}	
	public int ItemNumInRecent(String Activity) { // NOTE: if not found returns -1
		for(int i=0; i<pkgRecent.Activity.size(); i++) {
			if(Activity.equals(pkgRecent.Activity.get(i))) {	
				return i;
			}
		}
		return -1;
	}

}