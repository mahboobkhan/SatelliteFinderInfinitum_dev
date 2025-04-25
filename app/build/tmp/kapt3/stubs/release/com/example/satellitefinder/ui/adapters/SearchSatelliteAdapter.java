package com.example.satellitefinder.ui.adapters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u00012\u00020\u0003:\u0002\u001d\u001eB\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u000e\u001a\u00020\bH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\u001c\u0010\u0014\u001a\u00020\u00152\n\u0010\u0016\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0010H\u0017J\u001c\u0010\u0017\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0010H\u0016J\u0014\u0010\u001b\u001a\u00020\u00152\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000b0\nj\b\u0012\u0004\u0012\u00020\u000b`\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter$ViewHolder;", "Landroid/widget/Filterable;", "actionListner", "Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter$ActionListener;", "(Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter$ActionListener;)V", "filter12", "Landroid/widget/Filter;", "itemsList", "Ljava/util/ArrayList;", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "Lkotlin/collections/ArrayList;", "satelliteDataSource", "getFilter", "getItemCount", "", "getItemId", "", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setData", "loadData", "ActionListener", "ViewHolder", "Satellite Finder1.4.8__release"})
public final class SearchSatelliteAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter.ViewHolder> implements android.widget.Filterable {
    @org.jetbrains.annotations.NotNull()
    private final com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter.ActionListener actionListner = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> itemsList = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> satelliteDataSource;
    @org.jetbrains.annotations.NotNull()
    private android.widget.Filter filter12;
    
    public SearchSatelliteAdapter(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter.ActionListener actionListner) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.ui.adapters.SearchSatelliteAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.widget.Filter getFilter() {
        return null;
    }
    
    public final void setData(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> loadData) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter$ActionListener;", "", "sendData", "", "satellitePositionData", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "Satellite Finder1.4.8__release"})
    public static abstract interface ActionListener {
        
        public abstract void sendData(@org.jetbrains.annotations.Nullable()
        com.example.satellitefinder.utils.SatellitesPositionData satellitePositionData);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/satellitefinder/databinding/SatelliteItemBinding;", "(Lcom/example/satellitefinder/ui/adapters/SearchSatelliteAdapter;Lcom/example/satellitefinder/databinding/SatelliteItemBinding;)V", "getBinding", "()Lcom/example/satellitefinder/databinding/SatelliteItemBinding;", "Satellite Finder1.4.8__release"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.satellitefinder.databinding.SatelliteItemBinding binding = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.databinding.SatelliteItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.databinding.SatelliteItemBinding getBinding() {
            return null;
        }
    }
}