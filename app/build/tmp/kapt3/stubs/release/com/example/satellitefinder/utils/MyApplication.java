package com.example.satellitefinder.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u0000 \u00062\u00020\u00012\u00020\u0002:\u0001\u0006B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016\u00a8\u0006\u0007"}, d2 = {"Lcom/example/satellitefinder/utils/MyApplication;", "Landroid/app/Application;", "Landroidx/lifecycle/LifecycleObserver;", "()V", "onCreate", "", "Companion", "Satellite Finder1.4.5__release"})
public final class MyApplication extends android.app.Application implements androidx.lifecycle.LifecycleObserver {
    private static boolean isForegrounded = false;
    private static boolean canRequestAdByConsent = true;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.satellitefinder.utils.MyApplication.Companion Companion = null;
    
    public MyApplication() {
        super();
    }
    
    @java.lang.Override()
    public void onCreate() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\u0006\"\u0004\b\n\u0010\b\u00a8\u0006\u000b"}, d2 = {"Lcom/example/satellitefinder/utils/MyApplication$Companion;", "", "()V", "canRequestAdByConsent", "", "getCanRequestAdByConsent", "()Z", "setCanRequestAdByConsent", "(Z)V", "isForegrounded", "setForegrounded", "Satellite Finder1.4.5__release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isForegrounded() {
            return false;
        }
        
        public final void setForegrounded(boolean p0) {
        }
        
        public final boolean getCanRequestAdByConsent() {
            return false;
        }
        
        public final void setCanRequestAdByConsent(boolean p0) {
        }
    }
}