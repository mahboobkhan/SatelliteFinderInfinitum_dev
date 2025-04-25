package com.example.satellitefinder.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\'J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\'J\u0014\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\n0\tH\'J\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\n0\tH\'J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0005H\'J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0017J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0012H\u0017J\u0018\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0012H\'\u00a8\u0006\u0014"}, d2 = {"Lcom/example/satellitefinder/db/SubscriptionDao;", "", "deleteAll", "", "getById", "Lcom/example/satellitefinder/subscription/SkuDetailsModel;", "sku", "", "getInappSkuDetails", "Landroidx/lifecycle/LiveData;", "", "getSubscriptionSkuDetails", "insert", "skuDetailsModel", "insertOrUpdate", "Lcom/android/billingclient/api/ProductDetails;", "skuDetails", "canPurchase", "", "update", "Satellite Finder1.4.8__release"})
@androidx.room.Dao()
public abstract interface SubscriptionDao {
    
    @androidx.room.Query(value = "SELECT * FROM SkuDetailsModel WHERE type = \'subs\'")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.lifecycle.LiveData<java.util.List<com.example.satellitefinder.subscription.SkuDetailsModel>> getSubscriptionSkuDetails();
    
    @androidx.room.Query(value = "SELECT * FROM SkuDetailsModel WHERE type = \'inapp\'")
    @org.jetbrains.annotations.NotNull()
    public abstract androidx.lifecycle.LiveData<java.util.List<com.example.satellitefinder.subscription.SkuDetailsModel>> getInappSkuDetails();
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.NotNull()
    public abstract com.android.billingclient.api.ProductDetails insertOrUpdate(@org.jetbrains.annotations.NotNull()
    com.android.billingclient.api.ProductDetails skuDetails);
    
    @androidx.room.Transaction()
    public abstract void insertOrUpdate(@org.jetbrains.annotations.NotNull()
    java.lang.String sku, boolean canPurchase);
    
    @androidx.room.Query(value = "SELECT * FROM SkuDetailsModel WHERE sku = :sku")
    @org.jetbrains.annotations.Nullable()
    public abstract com.example.satellitefinder.subscription.SkuDetailsModel getById(@org.jetbrains.annotations.NotNull()
    java.lang.String sku);
    
    @androidx.room.Query(value = "DELETE FROM SkuDetailsModel")
    public abstract void deleteAll();
    
    @androidx.room.Insert(onConflict = 1)
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SkuDetailsModel skuDetailsModel);
    
    @androidx.room.Query(value = "UPDATE SkuDetailsModel SET canPurchase = :canPurchase WHERE sku = :sku")
    public abstract void update(@org.jetbrains.annotations.NotNull()
    java.lang.String sku, boolean canPurchase);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        @androidx.room.Transaction()
        @org.jetbrains.annotations.NotNull()
        public static com.android.billingclient.api.ProductDetails insertOrUpdate(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.db.SubscriptionDao $this, @org.jetbrains.annotations.NotNull()
        com.android.billingclient.api.ProductDetails skuDetails) {
            return null;
        }
        
        @androidx.room.Transaction()
        public static void insertOrUpdate(@org.jetbrains.annotations.NotNull()
        com.example.satellitefinder.db.SubscriptionDao $this, @org.jetbrains.annotations.NotNull()
        java.lang.String sku, boolean canPurchase) {
        }
    }
}