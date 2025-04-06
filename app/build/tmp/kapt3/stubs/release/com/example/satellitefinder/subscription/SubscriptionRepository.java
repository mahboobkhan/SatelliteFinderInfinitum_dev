package com.example.satellitefinder.subscription;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\"\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u0018H\u0002J\u000e\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020\u000fJ\u0010\u0010&\u001a\u00020!2\u0006\u0010\'\u001a\u00020(H\u0002J\b\u0010)\u001a\u00020*H\u0002J\u0010\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020#H\u0002J\u0006\u0010.\u001a\u00020!J\b\u0010/\u001a\u00020!H\u0002J\u0010\u00100\u001a\u00020*2\u0006\u0010-\u001a\u00020#H\u0002J\b\u00101\u001a\u00020*H\u0002J\u0018\u00102\u001a\u00020!2\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u00020\fH\u0002J\u0016\u00102\u001a\u00020!2\u0006\u00103\u001a\u0002042\u0006\u00106\u001a\u00020\u0019J\b\u00107\u001a\u00020!H\u0016J\u0010\u00108\u001a\u00020!2\u0006\u00109\u001a\u00020:H\u0016J \u0010;\u001a\u00020!2\u0006\u00109\u001a\u00020:2\u000e\u0010<\u001a\n\u0012\u0004\u0012\u00020#\u0018\u00010\u000eH\u0016J\u0016\u0010=\u001a\u00020,2\f\u0010>\u001a\b\u0012\u0004\u0012\u00020#0?H\u0002J\b\u0010@\u001a\u00020!H\u0002J\u0006\u0010A\u001a\u00020!J\u0006\u0010B\u001a\u00020!R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00020\u0011X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\'\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00180\u00178FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f\u00a8\u0006C"}, d2 = {"Lcom/example/satellitefinder/subscription/SubscriptionRepository;", "Lcom/android/billingclient/api/PurchasesUpdatedListener;", "Lcom/android/billingclient/api/BillingClientStateListener;", "application", "Landroid/content/Context;", "subscriptionDb", "Lcom/example/satellitefinder/db/SubscriptionDb;", "(Landroid/content/Context;Lcom/example/satellitefinder/db/SubscriptionDb;)V", "productsWithProductDetails", "Landroidx/lifecycle/MutableLiveData;", "", "", "Lcom/android/billingclient/api/ProductDetails;", "purchaseResultResponseListener", "", "Lcom/example/satellitefinder/subscription/SubscriptionCancelListener;", "storeBillingClient", "Lcom/android/billingclient/api/BillingClient;", "getStoreBillingClient", "()Lcom/android/billingclient/api/BillingClient;", "setStoreBillingClient", "(Lcom/android/billingclient/api/BillingClient;)V", "subSkuDetailsModelListLiveData", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/satellitefinder/subscription/SkuDetailsModel;", "getSubSkuDetailsModelListLiveData", "()Landroidx/lifecycle/LiveData;", "subSkuDetailsModelListLiveData$delegate", "Lkotlin/Lazy;", "getSubscriptionDb", "()Lcom/example/satellitefinder/db/SubscriptionDb;", "acknowledgeNonConsumablePurchasesAsync", "", "nonConsumables", "Lcom/android/billingclient/api/Purchase;", "addBillingResponseListener", "billingResultListener", "callBillingResponseListener", "responseCode", "", "connectToPlayBillingService", "", "disburseNonConsumableEntitlement", "Lkotlinx/coroutines/Job;", "purchase", "endDataSourceConnections", "instantiateAndConnectToPlayBillingService", "isSignatureValid", "isSubscriptionSupported", "launchBillingFlow", "activity", "Landroid/app/Activity;", "skuDetails", "skuDetailsModel", "onBillingServiceDisconnected", "onBillingSetupFinished", "billingResult", "Lcom/android/billingclient/api/BillingResult;", "onPurchasesUpdated", "purchases", "processPurchases", "purchasesResult", "", "queryProductDetails", "queryPurchasesAsync", "startDataSourceConnections", "Satellite Finder1.4.5__release"})
public final class SubscriptionRepository implements com.android.billingclient.api.PurchasesUpdatedListener, com.android.billingclient.api.BillingClientStateListener {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context application = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.satellitefinder.db.SubscriptionDb subscriptionDb = null;
    public com.android.billingclient.api.BillingClient storeBillingClient;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.Map<java.lang.String, com.android.billingclient.api.ProductDetails>> productsWithProductDetails = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.satellitefinder.subscription.SubscriptionCancelListener> purchaseResultResponseListener = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy subSkuDetailsModelListLiveData$delegate = null;
    
    public SubscriptionRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context application, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.db.SubscriptionDb subscriptionDb) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.satellitefinder.db.SubscriptionDb getSubscriptionDb() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.android.billingclient.api.BillingClient getStoreBillingClient() {
        return null;
    }
    
    public final void setStoreBillingClient(@org.jetbrains.annotations.NotNull()
    com.android.billingclient.api.BillingClient p0) {
    }
    
    public final void startDataSourceConnections() {
    }
    
    public final void endDataSourceConnections() {
    }
    
    private final void instantiateAndConnectToPlayBillingService() {
    }
    
    private final boolean connectToPlayBillingService() {
        return false;
    }
    
    @java.lang.Override()
    public void onBillingSetupFinished(@org.jetbrains.annotations.NotNull()
    com.android.billingclient.api.BillingResult billingResult) {
    }
    
    private final void queryProductDetails() {
    }
    
    @java.lang.Override()
    public void onBillingServiceDisconnected() {
    }
    
    public final void queryPurchasesAsync() {
    }
    
    private final kotlinx.coroutines.Job processPurchases(java.util.Set<? extends com.android.billingclient.api.Purchase> purchasesResult) {
        return null;
    }
    
    private final void acknowledgeNonConsumablePurchasesAsync(java.util.List<? extends com.android.billingclient.api.Purchase> nonConsumables) {
    }
    
    private final kotlinx.coroutines.Job disburseNonConsumableEntitlement(com.android.billingclient.api.Purchase purchase) {
        return null;
    }
    
    private final boolean isSignatureValid(com.android.billingclient.api.Purchase purchase) {
        return false;
    }
    
    private final boolean isSubscriptionSupported() {
        return false;
    }
    
    public final void launchBillingFlow(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SkuDetailsModel skuDetailsModel) {
    }
    
    private final void launchBillingFlow(android.app.Activity activity, com.android.billingclient.api.ProductDetails skuDetails) {
    }
    
    @java.lang.Override()
    public void onPurchasesUpdated(@org.jetbrains.annotations.NotNull()
    com.android.billingclient.api.BillingResult billingResult, @org.jetbrains.annotations.Nullable()
    java.util.List<com.android.billingclient.api.Purchase> purchases) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.satellitefinder.subscription.SkuDetailsModel>> getSubSkuDetailsModelListLiveData() {
        return null;
    }
    
    private final void callBillingResponseListener(int responseCode) {
    }
    
    public final void addBillingResponseListener(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SubscriptionCancelListener billingResultListener) {
    }
}