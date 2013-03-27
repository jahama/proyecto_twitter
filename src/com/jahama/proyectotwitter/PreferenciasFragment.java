package com.jahama.proyectotwitter;

import java.util.List;


import android.preference.PreferenceActivity;

public class PreferenciasFragment extends PreferenceActivity {

  public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
  public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
  public static final String PREF_IDIOMA = "PREF_IDIOMA";

  @Override
  public void onBuildHeaders(List<Header> target) {
    loadHeadersFromResource(R.xml.preference_header, target);
  }

}