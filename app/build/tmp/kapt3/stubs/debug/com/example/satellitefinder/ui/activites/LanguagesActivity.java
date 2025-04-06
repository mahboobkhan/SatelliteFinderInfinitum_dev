package com.example.satellitefinder.ui.activites;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\b\u0010\u0014\u001a\u00020\u0011H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/example/satellitefinder/ui/activites/LanguagesActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/example/satellitefinder/ui/adapters/LanguagesAdapter;", "binding", "Lcom/example/satellitefinder/databinding/ActivityLanguagesBinding;", "getBinding", "()Lcom/example/satellitefinder/databinding/ActivityLanguagesBinding;", "binding$delegate", "Lkotlin/Lazy;", "myLangugesList", "", "Lcom/example/satellitefinder/utils/LanguagesModel;", "selectedLanguageCode", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "showNativeAd", "Satellite Finder1.4.5__debug"})
public final class LanguagesActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.Nullable()
    private com.example.satellitefinder.ui.adapters.LanguagesAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.satellitefinder.utils.LanguagesModel> myLangugesList;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String selectedLanguageCode = "en";
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy binding$delegate = null;
    
    public LanguagesActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.satellitefinder.databinding.ActivityLanguagesBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void showNativeAd() {
    }
}