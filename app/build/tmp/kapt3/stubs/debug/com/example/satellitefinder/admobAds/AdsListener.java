package com.example.satellitefinder.admobAds;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u001a\u0010\u0007\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0006H&J)\u0010\b\u001a\u00020\u00032\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0005\u001a\u00020\u0006H&\u00a2\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u0003H\u0016J\u001a\u0010\u000f\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u001a\u0010\u0010\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0006H&J\u001a\u0010\u0011\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0016\u00a8\u0006\u0012"}, d2 = {"Lcom/example/satellitefinder/admobAds/AdsListener;", "", "onAdClicked", "", "ad", "type", "Lcom/example/satellitefinder/admobAds/AdType;", "onAdClosed", "onAdFailed", "error", "", "extraCode", "", "(Ljava/lang/String;Ljava/lang/Integer;Lcom/example/satellitefinder/admobAds/AdType;)V", "onAdImpression", "onAdLeftApplication", "onAdLoaded", "onAdOpened", "Satellite Finder1.4.8__debug"})
public abstract interface AdsListener {
    
    public abstract void onAdLoaded(@org.jetbrains.annotations.Nullable()
    java.lang.Object ad, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdType type);
    
    public abstract void onAdClosed(@org.jetbrains.annotations.Nullable()
    java.lang.Object ad, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdType type);
    
    public abstract void onAdFailed(@org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.Nullable()
    java.lang.Integer extraCode, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdType type);
    
    public abstract void onAdImpression();
    
    public abstract void onAdLeftApplication(@org.jetbrains.annotations.Nullable()
    java.lang.Object ad, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdType type);
    
    public abstract void onAdClicked(@org.jetbrains.annotations.Nullable()
    java.lang.Object ad, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdType type);
    
    public abstract void onAdOpened(@org.jetbrains.annotations.Nullable()
    java.lang.Object ad, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.admobAds.AdType type);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        public static void onAdImpression(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdsListener $this) {
        }
        
        public static void onAdLeftApplication(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdsListener $this, @org.jetbrains.annotations.Nullable()
        java.lang.Object ad, @org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdType type) {
        }
        
        public static void onAdClicked(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdsListener $this, @org.jetbrains.annotations.Nullable()
        java.lang.Object ad, @org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdType type) {
        }
        
        public static void onAdOpened(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdsListener $this, @org.jetbrains.annotations.Nullable()
        java.lang.Object ad, @org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.admobAds.AdType type) {
        }
    }
}