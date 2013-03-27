package com.jahama.proyectotwitter;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesActivity extends PreferenceActivity {

  public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
  public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
  public static final String PREF_IDIOMA = "PREF_IDIOMA";

  @SuppressWarnings("deprecation")
@Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.userpreferences);
  }

}