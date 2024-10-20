package com.diamon.publicidad;

import android.app.Activity;
import android.content.Intent;

import com.diamon.helicoptero.Publicidad;
import com.diamon.terminos.Terminos;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import androidx.annotation.NonNull;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.RequestConfiguration;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.DisplayMetrics;
import android.view.WindowMetrics;


public class MostrarPublicidad implements Publicidad {

    
      private static final String AD_UNIT_ID = "ca-app-pub-5141499161332805/1518371626";
    
    	private Activity actividad;
    	
    	private AdView adView;
    	
    	private AdRequest adRequest;
    	
    

    public MostrarPublicidad(Activity actividad) {

       
		this.actividad = actividad;
		
 MobileAds.initialize(actividad, new OnInitializationCompleteListener() {
                		        @Override
                		        public void onInitializationComplete(InitializationStatus initializationStatus) {
                		            }
                		    });
       
    adView = new AdView(actividad);
    adView.setAdUnitId(AD_UNIT_ID);
    adView.setAdSize(AdSize.BANNER);
    adRequest = new AdRequest.Builder().build();

     
    }


    @Override
    public void mostrarInterstitial() {}

    @Override
    public void botonAtrasInterstitial() {}
    
    	public AdView getBanner() {
      
          return adView;
      
    	}
    
   
    
    	
    	public void cargarBanner() {
    
      adView.loadAd(this.adRequest);
    
    
    	}
    
    	
    	public void mostrarBanner() {
    
          if (adView != null) {
        		
    
        		 }  
        		 
    
    	}
    
    	
    	public void ocultarBanner() {
    
        	  if (adView != null) {
        	       
        	      }
       
    		
    
    	}
    
    
    
}
