package com.example.satellitefinder.admobAds;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u001a\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0006H\u0002JL\u0010\u000f\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00062\u0010\b\u0002\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00112\u0010\b\u0002\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00112\u0010\b\u0002\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u0011J&\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00062\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u0011H\u0002J\u0016\u0010\u0019\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u000e\u001a\u00020\u0006J\u0010\u0010\u001a\u001a\u00020\n2\u0006\u0010\u001b\u001a\u00020\u0006H\u0002J\u0010\u0010\u001c\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002Jr\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u001e\u001a\u00020\b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00062\u0016\b\u0002\u0010\u001f\u001a\u0010\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\n\u0018\u00010 2\u0010\b\u0002\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00112\u0010\b\u0002\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00112\u0010\b\u0002\u0010\u0013\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u0011R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/satellitefinder/admobAds/InterstitialAdClass;", "", "()V", "loadingDialog", "Landroid/app/Dialog;", "logTag", "", "someOpPerformed", "", "dismissLoadingDialog", "", "activity", "Landroid/app/Activity;", "loadAgainPriorityInterstitialAd", "adIDLow", "loadAndShowSplashInterstitial", "closeListener", "Lkotlin/Function0;", "failListener", "showListener", "loadInterstitialAdPriority", "context", "Landroid/content/Context;", "adId", "adLoadedCallback", "loadPriorityInterstitialAds", "showInterstitialAdLog", "message", "showLoadingDialog", "showPriorityInterstitialAdNew", "loadAgain", "loadAd", "Lkotlin/Function1;", "Lcom/google/android/gms/ads/interstitial/InterstitialAd;", "Companion", "Satellite Finder1.4.8__release"})
public class InterstitialAdClass {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String logTag = "interstitialAdFlow";
    private boolean someOpPerformed = false;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.satellitefinder.admobAds.InterstitialAdClass instance;
    @org.jetbrains.annotations.Nullable()
    private android.app.Dialog loadingDialog;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.satellitefinder.admobAds.InterstitialAdClass.Companion Companion = null;
    
    public InterstitialAdClass() {
        super();
    }
    
    /**
     * pre load interstitial ad by priority and show later when needed for splash and other
     */
    public final void loadAndShowSplashInterstitial(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    java.lang.String adIDLow, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> closeListener, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> failListener, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> showListener) {
    }
    
    public final void loadPriorityInterstitialAds(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String adIDLow) {
    }
    
    private final void loadInterstitialAdPriority(android.content.Context context, java.lang.String adId, kotlin.jvm.functions.Function0<kotlin.Unit> adLoadedCallback) {
    }
    
    public final void showPriorityInterstitialAdNew(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, boolean loadAgain, @org.jetbrains.annotations.Nullable()
    java.lang.String adIDLow, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super com.google.android.gms.ads.interstitial.InterstitialAd, kotlin.Unit> loadAd, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> closeListener, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> failListener, @org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> showListener) {
    }
    
    private final void loadAgainPriorityInterstitialAd(android.app.Activity activity, java.lang.String adIDLow) {
    }
    
    /**
     * *************************************************************************
     * Normal Ad Flow
     */
    private final void showInterstitialAdLog(java.lang.String message) {
    }
    
    private final void showLoadingDialog(android.app.Activity activity) {
    }
    
    private final void dismissLoadingDialog(android.app.Activity activity) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/satellitefinder/admobAds/InterstitialAdClass$Companion;", "", "()V", "instance", "Lcom/example/satellitefinder/admobAds/InterstitialAdClass;", "getInstance", "Satellite Finder1.4.8__release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.admobAds.InterstitialAdClass getInstance() {
            return null;
        }
    }
}