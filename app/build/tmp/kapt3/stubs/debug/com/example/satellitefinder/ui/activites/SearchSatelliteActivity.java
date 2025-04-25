package com.example.satellitefinder.ui.activites;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0014J\u0018\u0010\u001d\u001a\u0012\u0012\u0004\u0012\u00020\u00110\u001ej\b\u0012\u0004\u0012\u00020\u0011`\u001fH\u0002J\b\u0010 \u001a\u00020\u001aH\u0016J\u0012\u0010!\u001a\u00020\u001a2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0014J\u0010\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\'H\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\u000e\u001a\u0004\b\u000b\u0010\fR\u001c\u0010\u000f\u001a\u0010\u0012\f\u0012\n \u0012*\u0004\u0018\u00010\u00110\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u00020\u0014X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018\u00a8\u0006("}, d2 = {"Lcom/example/satellitefinder/ui/activites/SearchSatelliteActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter;", "getAdapter", "()Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter;", "setAdapter", "(Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter;)V", "binding", "Lcom/example/satellitefinder/databinding/ActivitySearchSatelliteBinding;", "getBinding", "()Lcom/example/satellitefinder/databinding/ActivitySearchSatelliteBinding;", "binding$delegate", "Lkotlin/Lazy;", "compare", "Ljava/util/Comparator;", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "kotlin.jvm.PlatformType", "mSearchView", "Landroid/widget/SearchView;", "getMSearchView", "()Landroid/widget/SearchView;", "setMSearchView", "(Landroid/widget/SearchView;)V", "attachBaseContext", "", "newBase", "Landroid/content/Context;", "loadSatelliteData", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "Satellite Finder1.4.8__debug"})
public final class SearchSatelliteActivity extends androidx.appcompat.app.AppCompatActivity {
    public com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter adapter;
    public android.widget.SearchView mSearchView;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy binding$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Comparator<com.example.satellitefinder.utils.SatellitesPositionData> compare = null;
    
    public SearchSatelliteActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter getAdapter() {
        return null;
    }
    
    public final void setAdapter(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.widget.SearchView getMSearchView() {
        return null;
    }
    
    public final void setMSearchView(@org.jetbrains.annotations.NotNull()
    android.widget.SearchView p0) {
    }
    
    private final com.example.satellitefinder.databinding.ActivitySearchSatelliteBinding getBinding() {
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
    
    private final java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> loadSatelliteData() {
        return null;
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
}