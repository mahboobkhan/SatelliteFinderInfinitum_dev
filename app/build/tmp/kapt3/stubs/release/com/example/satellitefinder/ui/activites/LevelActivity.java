package com.example.satellitefinder.ui.activites;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\u0018\u0000 \u001a2\u00020\u00012\u00020\u0002:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0012\u0010\r\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J*\u0010\u0010\u001a\u00020\t2\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0016J\b\u0010\u0017\u001a\u00020\tH\u0014J\b\u0010\u0018\u001a\u00020\tH\u0014J\b\u0010\u0019\u001a\u00020\tH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/satellitefinder/ui/activites/LevelActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/example/satellitefinder/leveler/orientation/OrientationListener;", "()V", "binding", "Lcom/example/satellitefinder/databinding/ActivityLevelBinding;", "callback", "Landroidx/activity/OnBackPressedCallback;", "onCalibrationReset", "", "success", "", "onCalibrationSaved", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOrientationChanged", "orientation", "Lcom/example/satellitefinder/leveler/orientation/Orientation;", "pitch", "", "roll", "balance", "onPause", "onResume", "showBannerAd", "Companion", "Satellite Finder1.4.5__release"})
public final class LevelActivity extends androidx.appcompat.app.AppCompatActivity implements com.example.satellitefinder.leveler.orientation.OrientationListener {
    private com.example.satellitefinder.databinding.ActivityLevelBinding binding;
    private static com.example.satellitefinder.leveler.orientation.OrientationProvider provider;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.OnBackPressedCallback callback = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.satellitefinder.ui.activites.LevelActivity.Companion Companion = null;
    
    public LevelActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    @java.lang.Override()
    public void onOrientationChanged(@org.jetbrains.annotations.Nullable()
    com.example.satellitefinder.leveler.orientation.Orientation orientation, float pitch, float roll, float balance) {
    }
    
    @java.lang.Override()
    public void onCalibrationSaved(boolean success) {
    }
    
    @java.lang.Override()
    public void onCalibrationReset(boolean success) {
    }
    
    private final void showBannerAd() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/example/satellitefinder/ui/activites/LevelActivity$Companion;", "", "()V", "provider", "Lcom/example/satellitefinder/leveler/orientation/OrientationProvider;", "getProvider", "Satellite Finder1.4.5__release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.leveler.orientation.OrientationProvider getProvider() {
            return null;
        }
    }
}