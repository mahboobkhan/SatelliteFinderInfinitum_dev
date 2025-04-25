package com.example.satellitefinder.ui.adapters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0013B%\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\bH\u0016J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000e\u001a\u00020\bH\u0016J\u0018\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\bH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/NavigationAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/satellitefinder/ui/adapters/NavigationAdapter$NavigationItemViewHolder;", "items", "Ljava/util/ArrayList;", "Lcom/example/satellitefinder/ui/adapters/NavigationItemModel;", "Lkotlin/collections/ArrayList;", "currentPos", "", "(Ljava/util/ArrayList;I)V", "getItemCount", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "NavigationItemViewHolder", "Satellite Finder1.4.8__release"})
public final class NavigationAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.satellitefinder.ui.adapters.NavigationAdapter.NavigationItemViewHolder> {
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.example.satellitefinder.ui.adapters.NavigationItemModel> items;
    private int currentPos;
    
    public NavigationAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.example.satellitefinder.ui.adapters.NavigationItemModel> items, int currentPos) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.satellitefinder.ui.adapters.NavigationAdapter.NavigationItemViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.ui.adapters.NavigationAdapter.NavigationItemViewHolder holder, int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/NavigationAdapter$NavigationItemViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/satellitefinder/databinding/NavigationItemBinding;", "(Lcom/example/satellitefinder/databinding/NavigationItemBinding;)V", "getBinding", "()Lcom/example/satellitefinder/databinding/NavigationItemBinding;", "Satellite Finder1.4.8__release"})
    public static final class NavigationItemViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.satellitefinder.databinding.NavigationItemBinding binding = null;
        
        public NavigationItemViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.databinding.NavigationItemBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.satellitefinder.databinding.NavigationItemBinding getBinding() {
            return null;
        }
    }
}