package com.diamon.publicidad;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MostrarPublicidad implements Publicidad {

    private static final String AD_UNIT_ID = "ca-app-pub-5141499161332805/1518371626";

    private Activity actividad;

    private AdView adView;

    private AdRequest adRequest;

    private InterstitialAd mInterstitialAd;

    public MostrarPublicidad(Activity actividad) {

        this.actividad = actividad;

        MobileAds.initialize(
                actividad,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            InitializationStatus initializationStatus) {}
                });

        adView = new AdView(actividad);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);
        adRequest = new AdRequest.Builder().build();
    }

    @Override
    public void mostrarInterstitial() {

        if (mInterstitialAd != null) {

            mInterstitialAd.show(actividad);
        }
    }

    public void cargarInterstial() {

        InterstitialAd.load(
                actividad,
                "ca-app-pub-5141499161332805/8275351662",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public void botonAtrasInterstitial() {}

    public AdView getBanner() {

        return adView;
    }

    public void cargarBanner() {

        adView.loadAd(this.adRequest);
    }

    public AdRequest getAdReques() {

        return this.adRequest;
    }

    public void resumenBanner() {

        if (adView != null) {

            adView.resume();
        }
    }

    public void pausarBanner() {

        if (adView != null) {

            adView.pause();
        }
    }

    public void disposeBanner() {

        if (adView != null) {

            adView.destroy();
        }
    }

    public InterstitialAd getMInterstitialAd() {
        return this.mInterstitialAd;
    }

    public void setMInterstitialAd(InterstitialAd mInterstitialAd) {
        this.mInterstitialAd = mInterstitialAd;
    }
}
