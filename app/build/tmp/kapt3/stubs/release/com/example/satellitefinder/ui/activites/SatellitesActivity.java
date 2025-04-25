package com.example.satellitefinder.ui.activites;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0014J\u0018\u0010\'\u001a\u0012\u0012\u0004\u0012\u00020\u00110\u001bj\b\u0012\u0004\u0012\u00020\u0011`(H\u0002J\b\u0010)\u001a\u00020$H\u0016J\u0012\u0010*\u001a\u00020$2\b\u0010+\u001a\u0004\u0018\u00010,H\u0014J\u0012\u0010-\u001a\u00020$2\b\u0010.\u001a\u0004\u0018\u00010\u0011H\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001c\u0010\u000f\u001a\u0010\u0012\f\u0012\n \u0012*\u0004\u0018\u00010\u00110\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0014X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0015\"\u0004\b\u0019\u0010\u0017R \u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\u001bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u0014\u0010 \u001a\b\u0012\u0004\u0012\u00020\"0!X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/example/satellitefinder/ui/activites/SatellitesActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter;", "getAdapter", "()Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter;", "setAdapter", "(Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter;)V", "binding", "Lcom/example/satellitefinder/databinding/ActivitySatellitesBinding;", "getBinding", "()Lcom/example/satellitefinder/databinding/ActivitySatellitesBinding;", "binding$delegate", "Lkotlin/Lazy;", "compare", "Ljava/util/Comparator;", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "kotlin.jvm.PlatformType", "isAutoAdsRemoved", "", "()Z", "setAutoAdsRemoved", "(Z)V", "isNativeAdEnable", "setNativeAdEnable", "mSatelliteList", "Ljava/util/ArrayList;", "getMSatelliteList", "()Ljava/util/ArrayList;", "setMSatelliteList", "(Ljava/util/ArrayList;)V", "resultLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "attachBaseContext", "", "newBase", "Landroid/content/Context;", "loadSatellitesData", "Lkotlin/collections/ArrayList;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "sendDataResult", "satellitePositionData", "Satellite Finder1.4.8__release"})
public final class SatellitesActivity extends androidx.appcompat.app.AppCompatActivity {
    public com.example.satellitefinder.ui.adapters.SatellitesAdapter adapter;
    private boolean isNativeAdEnable = true;
    private boolean isAutoAdsRemoved = false;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> mSatelliteList;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy binding$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> resultLauncher;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Comparator<com.example.satellitefinder.utils.SatellitesPositionData> compare = null;
    
    public SatellitesActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.satellitefinder.ui.adapters.SatellitesAdapter getAdapter() {
        return null;
    }
    
    public final void setAdapter(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.ui.adapters.SatellitesAdapter p0) {
    }
    
    public final boolean isNativeAdEnable() {
        return false;
    }
    
    public final void setNativeAdEnable(boolean p0) {
    }
    
    public final boolean isAutoAdsRemoved() {
        return false;
    }
    
    public final void setAutoAdsRemoved(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> getMSatelliteList() {
        return null;
    }
    
    public final void setMSatelliteList(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> p0) {
    }
    
    private final com.example.satellitefinder.databinding.ActivitySatellitesBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    protected void attachBaseContext(@org.jetbrains.annotations.Nullable()
    android.content.Context newBase) {
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void sendDataResult(com.example.satellitefinder.utils.SatellitesPositionData satellitePositionData) {
    }
    
    private final java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> loadSatellitesData() {
        return null;
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
}