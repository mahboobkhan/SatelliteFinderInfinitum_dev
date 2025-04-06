package com.example.satellitefinder.admobAds;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u0016\u001a\u00020\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0003J\u0006\u0010\u001a\u001a\u00020\u0007J\u001a\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000f2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000fH\u0016J\u0010\u0010 \u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000fH\u0016J\u0010\u0010!\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000fH\u0016J\u0018\u0010\"\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010#\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000fH\u0016J\u0010\u0010$\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000fH\u0016J\u0018\u0010%\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020&2\u0006\u0010\'\u001a\u00020(H\u0016J\u0006\u0010)\u001a\u00020\u0017R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/example/satellitefinder/admobAds/OpenApp;", "Landroid/app/Application$ActivityLifecycleCallbacks;", "Landroidx/lifecycle/LifecycleEventObserver;", "globalClass", "Lcom/example/satellitefinder/utils/MyApplication;", "(Lcom/example/satellitefinder/utils/MyApplication;)V", "adVisible", "", "appOpenAd", "Lcom/google/android/gms/ads/appopen/AppOpenAd;", "getAppOpenAd", "()Lcom/google/android/gms/ads/appopen/AppOpenAd;", "setAppOpenAd", "(Lcom/google/android/gms/ads/appopen/AppOpenAd;)V", "currentActivity", "Landroid/app/Activity;", "fullScreenContentCallback", "Lcom/google/android/gms/ads/FullScreenContentCallback;", "isShowingAd", "log", "", "myApplication", "fetchAd", "", "getAdRequest", "Lcom/google/android/gms/ads/AdRequest;", "isAdAvailable", "onActivityCreated", "p0", "p1", "Landroid/os/Bundle;", "onActivityDestroyed", "onActivityPaused", "onActivityResumed", "onActivitySaveInstanceState", "onActivityStarted", "onActivityStopped", "onStateChanged", "Landroidx/lifecycle/LifecycleOwner;", "event", "Landroidx/lifecycle/Lifecycle$Event;", "showAdIfAvailable", "Satellite Finder1.4.5__debug"})
public final class OpenApp implements android.app.Application.ActivityLifecycleCallbacks, androidx.lifecycle.LifecycleEventObserver {
    @org.jetbrains.annotations.NotNull()
    private final com.example.satellitefinder.utils.MyApplication globalClass = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String log = "AppOpenManager";
    private boolean adVisible = false;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.ads.appopen.AppOpenAd appOpenAd;
    @org.jetbrains.annotations.Nullable()
    private android.app.Activity currentActivity;
    private boolean isShowingAd = false;
    @org.jetbrains.annotations.NotNull()
    private com.example.satellitefinder.utils.MyApplication myApplication;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.gms.ads.FullScreenContentCallback fullScreenContentCallback;
    
    public OpenApp(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.utils.MyApplication globalClass) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.google.android.gms.ads.appopen.AppOpenAd getAppOpenAd() {
        return null;
    }
    
    public final void setAppOpenAd(@org.jetbrains.annotations.Nullable()
    com.google.android.gms.ads.appopen.AppOpenAd p0) {
    }
    
    /**
     * Request an ad
     */
    public final void fetchAd() {
    }
    
    public final void showAdIfAvailable() {
    }
    
    /**
     * Creates and returns ad request.
     */
    @org.jetbrains.annotations.NotNull()
    private final com.google.android.gms.ads.AdRequest getAdRequest() {
        return null;
    }
    
    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public final boolean isAdAvailable() {
        return false;
    }
    
    @java.lang.Override()
    public void onActivityCreated(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0, @org.jetbrains.annotations.Nullable()
    android.os.Bundle p1) {
    }
    
    @java.lang.Override()
    public void onActivityStarted(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0) {
    }
    
    @java.lang.Override()
    public void onActivityResumed(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0) {
    }
    
    @java.lang.Override()
    public void onActivityPaused(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0) {
    }
    
    @java.lang.Override()
    public void onActivityStopped(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0) {
    }
    
    @java.lang.Override()
    public void onActivitySaveInstanceState(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0, @org.jetbrains.annotations.NotNull()
    android.os.Bundle p1) {
    }
    
    @java.lang.Override()
    public void onActivityDestroyed(@org.jetbrains.annotations.NotNull()
    android.app.Activity p0) {
    }
    
    @java.lang.Override()
    public void onStateChanged(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LifecycleOwner p0, @org.jetbrains.annotations.NotNull()
    androidx.lifecycle.Lifecycle.Event event) {
    }
}