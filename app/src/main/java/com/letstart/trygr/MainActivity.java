package com.letstart.trygr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.letstart.commonmodule.*;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import it.necst.grabnrun.SecureDexClassLoader;
import it.necst.grabnrun.SecureLoaderFactory;



public class MainActivity extends ActionBarActivity {

    private String TAG="MainActivity";
    SecureDexClassLoader mSecureDexClassLoader = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Activity myClassInstance = null;

        String dexContainerPath = "https://drive.google.com/uc?export=download&id=0B8N9YOctHcf_SmcxQ2dwVC14U0U";

        try {
            Map<String, URL> packageNamesToCertMap = new HashMap<String, URL>();


            packageNamesToCertMap.put("com.letstart", new URL("https://drive.google.com/uc?export=download&id=0B8N9YOctHcf_bTdWRnpQcTNaMGs"));
            Log.i(TAG,"put package name in the map");
            SecureLoaderFactory mSecureLoaderFactory = new SecureLoaderFactory(this);
            mSecureDexClassLoader = mSecureLoaderFactory.createDexClassLoader( dexContainerPath,
                    null,
                    getClass().getClassLoader(),
                    packageNamesToCertMap);

            Log.i(TAG,"created Secure Dex Class Loader Object");

            Class<?> loadedActivity = mSecureDexClassLoader.loadClass("com.letstart.extlib.MainClassAct");
            Class<?> loadedClass = mSecureDexClassLoader.loadClass("com.letstart.extlib.ExternalClass");
            Class<?> loadedClassExtAbs = mSecureDexClassLoader.loadClass("com.letstart.extlib.ExternalClassExtAbs");




            if (loadedClass != null) {
                Log.i(TAG," class loaded: " +loadedClass.getSimpleName());
                Object myInstance =  loadedClass.newInstance();

            }
            if (loadedClassExtAbs != null) {
                Log.i(TAG,"loadedClasses: " +loadedClassExtAbs.getSimpleName());


                CommonAbs myInstance =   (CommonAbs) loadedClassExtAbs.newInstance();

            }
            // Check whether the signature verification process succeeded
            if (loadedActivity != null) {
                Log.i(TAG,"loadedClasses: " +loadedActivity.getSimpleName());


                //Intent it=new Intent("com.letstart.trygr.START_APP2");
          /*      Intent it=new Intent();
                it.setClassName("com.letstart.extlib", "com.letstart.extlib.MainClassAct");
                startActivity(it);*/

                /*myClassInstance = (Activity) loadedClass.newInstance();
                Intent in = new Intent();
                in.setClass(this, loadedClass);
                startActivity(in);
                */

            }
        } catch (ClassNotFoundException e) {
            // This exception will be raised when the container of the target class
            // is genuine but this class file is missing..
            e.printStackTrace();
        }

        catch (MalformedURLException e) {
            // The previous URL used for the packageNamesToCertMap entry was a malformed one.
            Log.e("Error", "A malformed URL was provided for a remote certificate location");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSecureDexClassLoader!=null)
            mSecureDexClassLoader.wipeOutPrivateAppCachedData(true,true);
    }
}
