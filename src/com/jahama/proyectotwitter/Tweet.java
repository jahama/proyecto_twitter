package com.jahama.proyectotwitter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;
import android.util.Log;

public class Tweet {
	
	  private static final String TAG = null;
	  private Date fecha;
	  private String usuario;
	  private String nombreUsuario;
	  private String texto;
	  
	  public Date getFecha() {
			return fecha;
		}
		public String getUsuario() {
			return usuario;
		}
		public String getNombreUsuario() {
			return nombreUsuario;
		}
		public String getTexto() {
			return texto;
		}
	  

	 

	  public Tweet(Date fecha, String usuario, String nombreUsuario, String texto) {
			super();
			this.fecha = fecha;
			this.usuario = usuario;
			this.nombreUsuario = nombreUsuario;
			this.texto = texto;
			
			Log.i(TAG, " -- OBJETO TWEET CREADO : " + this.toString());
		}
	

	  @Override
	  public String toString() {
	    SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
	    String dateString = sdf.format(this.fecha);
	    DecimalFormat df = new DecimalFormat("#.##");
	    return dateString + ": " + this.usuario + " " + this.nombreUsuario + " " + this.texto;
	  }

}
