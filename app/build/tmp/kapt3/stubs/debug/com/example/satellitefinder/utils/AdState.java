package com.example.satellitefinder.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/example/satellitefinder/utils/AdState;", "", "()V", "Failed", "Loaded", "Loading", "Lcom/example/satellitefinder/utils/AdState$Failed;", "Lcom/example/satellitefinder/utils/AdState$Loaded;", "Lcom/example/satellitefinder/utils/AdState$Loading;", "Satellite Finder1.4.5__debug"})
public abstract class AdState {
    
    private AdState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/satellitefinder/utils/AdState$Failed;", "Lcom/example/satellitefinder/utils/AdState;", "()V", "Satellite Finder1.4.5__debug"})
    public static final class Failed extends com.example.satellitefinder.utils.AdState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.satellitefinder.utils.AdState.Failed INSTANCE = null;
        
        private Failed() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/example/satellitefinder/utils/AdState$Loaded;", "Lcom/example/satellitefinder/utils/AdState;", "nativeAd", "Lcom/google/android/gms/ads/nativead/NativeAd;", "(Lcom/google/android/gms/ads/nativead/NativeAd;)V", "getNativeAd", "()Lcom/google/android/gms/ads/nativead/NativeAd;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "Satellite Finder1.4.5__debug"})
    public static final class Loaded extends com.example.satellitefinder.utils.AdState {
        @org.jetbrains.annotations.NotNull()
        private final com.google.android.gms.ads.nativead.NativeAd nativeAd = null;
        
        public Loaded(@org.jetbrains.annotations.NotNull()
        com.google.android.gms.ads.nativead.NativeAd nativeAd) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.android.gms.ads.nativead.NativeAd getNativeAd() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.google.android.gms.ads.nativead.NativeAd component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.utils.AdState.Loaded copy(@org.jetbrains.annotations.NotNull()
        com.google.android.gms.ads.nativead.NativeAd nativeAd) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/satellitefinder/utils/AdState$Loading;", "Lcom/example/satellitefinder/utils/AdState;", "()V", "Satellite Finder1.4.5__debug"})
    public static final class Loading extends com.example.satellitefinder.utils.AdState {
        @org.jetbrains.annotations.NotNull()
        public static final com.example.satellitefinder.utils.AdState.Loading INSTANCE = null;
        
        private Loading() {
        }
    }
}