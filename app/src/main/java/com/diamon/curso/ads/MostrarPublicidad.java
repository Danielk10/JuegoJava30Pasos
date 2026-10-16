package com.diamon.curso.ads;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.diamon.curso.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.lang.ref.WeakReference;

/**
 * Gestor unificado de publicidad para Flash SPI Tool.
 * Controla Banners Adaptativos, Interstitials y Rewarded Video con soporte para
 * omisión absoluta cuando el usuario tiene Pro activo (Pase 12h o Compra Permanente).
 */
public class MostrarPublicidad implements Publicidad {

    private static final String TAG = "MostrarPublicidad";
    public static final String BANNER_AD_UNIT_ID = "ca-app-pub-5141499161332805/1518371626";
    public static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-5141499161332805/8275351662";
    /** ID Real AdMob oficial de producción para Rewarded Video de Flash SPI Tool: */
    public static final String REWARDED_AD_UNIT_ID = "ca-app-pub-5141499161332805/9025351389";
    /** ID Oficial de Prueba de Google AdMob para testing local */
    public static final String REWARDED_TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";

    // Cooldown mínimo de 3 minutos entre interstitials para proteger la experiencia del usuario
    private static final long COOLDOWN_DEFAULT_MS = 3 * 60 * 1000;

    private final WeakReference<Activity> actividadRef;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private AdView adView;
    private final AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private boolean isLoadingInterstitial = false;
    private long ultimoTiempoInterstitial = 0;

    private RewardedAd rewardedAd;
    private boolean isRewardedLoading = false;

    private AdsDisabledChecker adsDisabledChecker;

    public MostrarPublicidad(Activity actividad) {
        this.actividadRef = new WeakReference<>(actividad);

        MobileAds.initialize(
                actividad,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(
                            @NonNull InitializationStatus initializationStatus) {
                    }
                });

        adView = new AdView(actividad);
        adView.setAdUnitId(BANNER_AD_UNIT_ID);
        adView.setAdSize(getAdaptiveBannerAdSize(actividad));
        adView.setAdListener(
                new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        Log.d(TAG, "Adaptive Banner cargado exitosamente.");
                        if (isAdDisabled() && adView != null) {
                            adView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.w(TAG, "Error cargando Adaptive Banner: " + loadAdError.getMessage());
                    }
                });
        adRequest = new AdRequest.Builder().build();

        // Precargar Rewarded Ad en segundo plano
        cargarRewardedAd();
    }

    @Override
    public void setAdsDisabledChecker(AdsDisabledChecker checker) {
        this.adsDisabledChecker = checker;
        if (isAdDisabled()) {
            ocultarBanner();
        }
    }

    public boolean isAdDisabled() {
        return adsDisabledChecker != null && adsDisabledChecker.areAdsDisabled();
    }

    /**
     * Calcula el tamaño óptimo de Anchored Adaptive Banner según el ancho de pantalla del dispositivo.
     */
    @SuppressWarnings("deprecation")
    private AdSize getAdaptiveBannerAdSize(Activity activity) {
        if (activity == null) {
            return AdSize.BANNER;
        }
        try {
            int adWidthPixels;
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
                adWidthPixels = windowMetrics.getBounds().width();
            } else {
                adWidthPixels = displayMetrics.widthPixels;
            }
            float density = displayMetrics.density;
            int adWidth = (int) (adWidthPixels / density);
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
        } catch (Exception e) {
            Log.e(TAG, "Error calculando adaptive banner size, fallback a estándar", e);
            return AdSize.BANNER;
        }
    }

    @Override
    public void mostrarInterstitial() {
        mostrarInterstitialConCooldown(0);
    }

    @Override
    public boolean mostrarInterstitialConCooldown() {
        return mostrarInterstitialConCooldown(COOLDOWN_DEFAULT_MS);
    }

    public boolean mostrarInterstitialConCooldown(long cooldownMs) {
        if (isAdDisabled()) {
            Log.d(TAG, "Interstitial omitido: Acceso Pro activo / Anuncios desactivados.");
            return false;
        }

        long ahora = System.currentTimeMillis();
        if (cooldownMs > 0 && (ahora - ultimoTiempoInterstitial < cooldownMs)) {
            Log.d(TAG, "Interstitial omitido por cooldown activo (" + (ahora - ultimoTiempoInterstitial) + " ms)");
            return false;
        }

        Activity actividad = actividadRef.get();
        if (mInterstitialAd != null && actividad != null && !actividad.isFinishing()) {
            ultimoTiempoInterstitial = ahora;
            mInterstitialAd.show(actividad);
            return true;
        } else {
            if (mInterstitialAd == null && !isLoadingInterstitial) {
                cargarInterstial();
            }
            return false;
        }
    }

    public void cargarInterstial() {
        if (isAdDisabled()) {
            return;
        }

        if (isLoadingInterstitial || mInterstitialAd != null) {
            return;
        }

        Activity actividad = actividadRef.get();
        if (actividad == null || actividad.isFinishing())
            return;

        isLoadingInterstitial = true;
        InterstitialAd.load(
                actividad,
                INTERSTITIAL_AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        isLoadingInterstitial = false;

                        mInterstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;
                                        if (!isAdDisabled()) {
                                            cargarInterstial();
                                        }
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        Log.w(TAG, "Fallo al mostrar Interstitial: " + adError.getMessage());
                                        mInterstitialAd = null;
                                        if (!isAdDisabled()) {
                                            cargarInterstial();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.w(TAG, "Fallo al cargar Interstitial: " + loadAdError.getMessage());
                        mInterstitialAd = null;
                        isLoadingInterstitial = false;
                    }
                });
    }

    @Override
    public void botonAtrasInterstitial() {
    }

    // --- REWARDED VIDEO ADS (PASE 12 HORAS) ---

    @Override
    public void cargarRewardedAd() {
        mainHandler.post(() -> {
            try {
                if (rewardedAd != null || isRewardedLoading) {
                    return;
                }

                Activity actividad = actividadRef.get();
                if (actividad == null || actividad.isFinishing()) {
                    return;
                }

                isRewardedLoading = true;
                Log.d(TAG, "Iniciando carga de RewardedAd...");
                AdRequest request = new AdRequest.Builder().build();
                RewardedAd.load(
                        actividad,
                        REWARDED_AD_UNIT_ID,
                        request,
                        new RewardedAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull RewardedAd ad) {
                                rewardedAd = ad;
                                isRewardedLoading = false;
                                Log.d(TAG, "RewardedAd cargado con éxito.");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                rewardedAd = null;
                                isRewardedLoading = false;
                                Log.w(TAG, "Error cargando RewardedAd: " + loadAdError.getMessage());
                            }
                        }
                );
            } catch (Exception e) {
                isRewardedLoading = false;
                Log.e(TAG, "Excepción cargando RewardedAd: " + e.getMessage(), e);
            }
        });
    }

    public void precargarRewardedAd() {
        cargarRewardedAd();
    }

    @Override
    public boolean isRewardedAdLoaded() {
        return rewardedAd != null;
    }

    @VisibleForTesting
    public void setRewardedAd(@Nullable RewardedAd rewardedAd) {
        this.rewardedAd = rewardedAd;
    }

    @Override
    public void mostrarRewardedAd(
            Activity activity,
            OnUserEarnedRewardListener rewardListener,
            Runnable onDismissedListener) {

        Activity targetActivity = (activity != null && !activity.isFinishing()) ? activity : actividadRef.get();
        if (targetActivity == null || targetActivity.isFinishing()) {
            if (onDismissedListener != null) {
                onDismissedListener.run();
            }
            return;
        }

        mainHandler.post(() -> {
            if (rewardedAd != null) {
                RewardedAd adParaMostrar = rewardedAd;
                rewardedAd = null;

                adParaMostrar.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.d(TAG, "RewardedAd cerrado por el usuario.");
                        cargarRewardedAd();
                        if (onDismissedListener != null) {
                            onDismissedListener.run();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.w(TAG, "Fallo al mostrar RewardedAd: " + adError.getMessage());
                        cargarRewardedAd();
                        if (onDismissedListener != null) {
                            onDismissedListener.run();
                        }
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "RewardedAd presentado en pantalla completa.");
                    }
                });

                adParaMostrar.show(
                        targetActivity,
                        rewardListener != null ? rewardListener : reward -> Log.d(TAG, "Recompensa otorgada sin listener explícito.")
                );
            } else {
                Log.w(TAG, "RewardedAd no listo. Notificando al usuario y recargando...");
                cargarRewardedAd();
                Toast.makeText(targetActivity, targetActivity.getString(R.string.ad_loading_rewarded_video_toast), Toast.LENGTH_SHORT).show();
                if (onDismissedListener != null) {
                    onDismissedListener.run();
                }
            }
        });
    }

    // --- BANNERS ---

    public AdView getBanner() {
        return adView;
    }

    public void cargarBanner() {
        if (isAdDisabled()) {
            ocultarBanner();
            return;
        }
        if (adView != null) {
            adView.setVisibility(View.VISIBLE);
            adView.loadAd(this.adRequest);
        }
    }

    @Override
    public void ocultarBanner() {
        mainHandler.post(() -> {
            if (adView != null) {
                adView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void mostrarBanner() {
        if (isAdDisabled()) {
            ocultarBanner();
            return;
        }
        mainHandler.post(() -> {
            if (adView != null) {
                adView.setVisibility(View.VISIBLE);
                cargarBanner();
            }
        });
    }

    public AdRequest getAdReques() {
        return this.adRequest;
    }

    @Override
    public void resumenBanner() {
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void pausarBanner() {
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
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
