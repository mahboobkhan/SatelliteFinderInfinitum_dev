package com.example.satellitefinder.ui.adapters;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\bH&\u00a8\u0006\n"}, d2 = {"Lcom/example/satellitefinder/ui/adapters/BaseItem;", "", "()V", "bindItem", "", "holder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "position", "", "itemType", "Satellite Finder1.4.5__debug"})
public abstract class BaseItem {
    
    public BaseItem() {
        super();
    }
    
    public abstract int itemType();
    
    public abstract void bindItem(@org.jetbrains.annotations.Nullable()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position);
}