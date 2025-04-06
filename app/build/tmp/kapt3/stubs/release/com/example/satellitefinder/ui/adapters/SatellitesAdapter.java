package com.example.satellitefinder.ui.adapters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00043456B!\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\u0002\u0010\tJ\b\u0010\u001f\u001a\u00020\u0012H\u0016J\u0010\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u0012H\u0016J\u0010\u0010#\u001a\u00020\u00122\u0006\u0010\"\u001a\u00020\u0012H\u0016J\u0018\u0010$\u001a\u00020\b2\u0006\u0010%\u001a\u00020\u00022\u0006\u0010\"\u001a\u00020\u0012H\u0016J\u0018\u0010&\u001a\u00020\u00022\u0006\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u0012H\u0016JF\u0010*\u001a\u00020\b2\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020,2\u0016\u0010.\u001a\u0012\u0012\u0004\u0012\u00020\u00070/j\b\u0012\u0004\u0012\u00020\u0007`02\u0006\u00101\u001a\u00020,2\u0006\u00102\u001a\u00020\u00122\u0006\u0010\n\u001a\u00020\u000bR\u001c\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR&\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00130\u0011X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u00067"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "context", "Landroid/content/Context;", "onItemClick", "Lkotlin/Function1;", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "", "(Landroid/content/Context;Lkotlin/jvm/functions/Function1;)V", "activity", "Landroid/app/Activity;", "getActivity", "()Landroid/app/Activity;", "setActivity", "(Landroid/app/Activity;)V", "adsHashMap", "Ljava/util/HashMap;", "", "", "getAdsHashMap", "()Ljava/util/HashMap;", "setAdsHashMap", "(Ljava/util/HashMap;)V", "getContext", "()Landroid/content/Context;", "entitiesList", "", "Lcom/example/satellitefinder/ui/adapters/BaseItem;", "getOnItemClick", "()Lkotlin/jvm/functions/Function1;", "getItemCount", "getItemId", "", "position", "getItemViewType", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "setData", "isNativeAdEnable", "", "isAutoAdsRemoved", "itemList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "isNetworkAvilable", "countAfter", "NativeItem", "NativeViewHolder", "SatelliteItemView", "SatelliteViewHolder", "Satellite Finder1.4.5__release"})
public final class SatellitesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.satellitefinder.utils.SatellitesPositionData, kotlin.Unit> onItemClick = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.example.satellitefinder.ui.adapters.BaseItem> entitiesList;
    @org.jetbrains.annotations.NotNull()
    private java.util.HashMap<java.lang.Integer, java.lang.Object> adsHashMap;
    @org.jetbrains.annotations.Nullable()
    private android.app.Activity activity;
    
    public SatellitesAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.satellitefinder.utils.SatellitesPositionData, kotlin.Unit> onItemClick) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final android.content.Context getContext() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlin.jvm.functions.Function1<com.example.satellitefinder.utils.SatellitesPositionData, kotlin.Unit> getOnItemClick() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.HashMap<java.lang.Integer, java.lang.Object> getAdsHashMap() {
        return null;
    }
    
    public final void setAdsHashMap(@org.jetbrains.annotations.NotNull()
    java.util.HashMap<java.lang.Integer, java.lang.Object> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.app.Activity getActivity() {
        return null;
    }
    
    public final void setActivity(@org.jetbrains.annotations.Nullable()
    android.app.Activity p0) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void setData(boolean isNativeAdEnable, boolean isAutoAdsRemoved, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.example.satellitefinder.utils.SatellitesPositionData> itemList, boolean isNetworkAvilable, int countAfter, @org.jetbrains.annotations.NotNull()
    android.app.Activity activity) {
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004\u00a8\u0006\u000f"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter$NativeItem;", "Lcom/example/satellitefinder/ui/adapters/BaseItem;", "nativeAd", "", "(Ljava/lang/Object;)V", "getNativeAd", "()Ljava/lang/Object;", "setNativeAd", "bindItem", "", "holder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "position", "", "itemType", "Satellite Finder1.4.5__release"})
    static final class NativeItem extends com.example.satellitefinder.ui.adapters.BaseItem {
        @org.jetbrains.annotations.Nullable()
        private java.lang.Object nativeAd;
        
        public NativeItem(@org.jetbrains.annotations.Nullable()
        java.lang.Object nativeAd) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.Object getNativeAd() {
            return null;
        }
        
        public final void setNativeAd(@org.jetbrains.annotations.Nullable()
        java.lang.Object p0) {
        }
        
        @java.lang.Override()
        public int itemType() {
            return 0;
        }
        
        @java.lang.Override()
        public void bindItem(@org.jetbrains.annotations.Nullable()
        androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\t"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter$NativeViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/satellitefinder/databinding/ListAdItemBinding;", "(Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter;Lcom/example/satellitefinder/databinding/ListAdItemBinding;)V", "getBinding", "()Lcom/example/satellitefinder/databinding/ListAdItemBinding;", "bind", "", "Satellite Finder1.4.5__release"})
    public final class NativeViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.satellitefinder.databinding.ListAdItemBinding binding = null;
        
        public NativeViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.databinding.ListAdItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.databinding.ListAdItemBinding getBinding() {
            return null;
        }
        
        public final void bind() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000e"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter$SatelliteItemView;", "Lcom/example/satellitefinder/ui/adapters/BaseItem;", "satellite", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "(Lcom/example/satellitefinder/utils/SatellitesPositionData;)V", "getSatellite", "()Lcom/example/satellitefinder/utils/SatellitesPositionData;", "bindItem", "", "holder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "position", "", "itemType", "Satellite Finder1.4.5__release"})
    public static final class SatelliteItemView extends com.example.satellitefinder.ui.adapters.BaseItem {
        @org.jetbrains.annotations.NotNull()
        private final com.example.satellitefinder.utils.SatellitesPositionData satellite = null;
        
        public SatelliteItemView(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.utils.SatellitesPositionData satellite) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.utils.SatellitesPositionData getSatellite() {
            return null;
        }
        
        @java.lang.Override()
        public int itemType() {
            return 0;
        }
        
        @java.lang.Override()
        public void bindItem(@org.jetbrains.annotations.Nullable()
        androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\r"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter$SatelliteViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/satellitefinder/databinding/SatelliteItemBinding;", "(Lcom/example/satellitefinder/ui/adapters/SatellitesAdapter;Lcom/example/satellitefinder/databinding/SatelliteItemBinding;)V", "getBinding", "()Lcom/example/satellitefinder/databinding/SatelliteItemBinding;", "bindData", "", "item", "Lcom/example/satellitefinder/utils/SatellitesPositionData;", "position", "", "Satellite Finder1.4.5__release"})
    public final class SatelliteViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.satellitefinder.databinding.SatelliteItemBinding binding = null;
        
        public SatelliteViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.databinding.SatelliteItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.databinding.SatelliteItemBinding getBinding() {
            return null;
        }
        
        @android.annotation.SuppressLint(value = {"SetTextI18n"})
        public final void bindData(@org.jetbrains.annotations.Nullable()
        com.example.satellitefinder.utils.SatellitesPositionData item, int position) {
        }
    }
}