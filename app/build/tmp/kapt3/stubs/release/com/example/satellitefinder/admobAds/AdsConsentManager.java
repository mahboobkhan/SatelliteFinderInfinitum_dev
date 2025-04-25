package com.example.satellitefinder.admobAds;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 \u00132\u00020\u0001:\u0002\u0013\u0014B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ(\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/example/satellitefinder/admobAds/AdsConsentManager;", "", "activity", "Landroid/app/Activity;", "(Landroid/app/Activity;)V", "consentInformation", "Lcom/google/android/ump/ConsentInformation;", "isCallbackUMPSuccess", "Ljava/util/concurrent/atomic/AtomicBoolean;", "requestUMP", "", "umpResultListener", "Lcom/example/satellitefinder/admobAds/AdsConsentManager$UMPResultListener;", "enableDebug", "", "testDevice", "", "resetData", "showPrivacyOption", "Companion", "UMPResultListener", "Satellite Finder1.4.8__release"})
public final class AdsConsentManager {
    @org.jetbrains.annotations.NotNull()
    private final android.app.Activity activity = null;
    @org.jetbrains.annotations.Nullable()
    private com.google.android.ump.ConsentInformation consentInformation;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicBoolean isCallbackUMPSuccess = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AdsConsentManager";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.satellitefinder.admobAds.AdsConsentManager.Companion Companion = null;
    
    public AdsConsentManager(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
        super();
    }
    
    public final void requestUMP(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdsConsentManager.UMPResultListener umpResultListener) {
    }
    
    public final void requestUMP(boolean enableDebug, @org.jetbrains.annotations.Nullable()
    java.lang.String testDevice, boolean resetData, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdsConsentManager.UMPResultListener umpResultListener) {
    }
    
    public final void showPrivacyOption(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdsConsentManager.UMPResultListener umpResultListener) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/satellitefinder/admobAds/AdsConsentManager$Companion;", "", "()V", "TAG", "", "getConsentResult", "", "context", "Landroid/content/Context;", "Satellite Finder1.4.8__release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean getConsentResult(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/satellitefinder/admobAds/AdsConsentManager$UMPResultListener;", "", "onCheckUMPSuccess", "", "canRequestAds", "", "Satellite Finder1.4.8__release"})
    public static abstract interface UMPResultListener {
        
        public abstract void onCheckUMPSuccess(boolean canRequestAds);
    }
}