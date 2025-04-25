package com.example.satellitefinder.subscription;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010\u0016\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0017\u001a\u00020\u0018J\u0016\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\bJ\b\u0010\u001d\u001a\u00020\u0013H\u0014R&\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0004R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/example/satellitefinder/subscription/SubscriptionViewModel;", "Landroidx/lifecycle/ViewModel;", "subscriptionRepository", "Lcom/example/satellitefinder/subscription/SubscriptionRepository;", "(Lcom/example/satellitefinder/subscription/SubscriptionRepository;)V", "subSkuDetailsModelListLiveData", "Landroidx/lifecycle/LiveData;", "", "Lcom/example/satellitefinder/subscription/SkuDetailsModel;", "getSubSkuDetailsModelListLiveData", "()Landroidx/lifecycle/LiveData;", "setSubSkuDetailsModelListLiveData", "(Landroidx/lifecycle/LiveData;)V", "getSubscriptionRepository", "()Lcom/example/satellitefinder/subscription/SubscriptionRepository;", "setSubscriptionRepository", "viewModelScope", "Lkotlinx/coroutines/CoroutineScope;", "addBillingCancelListener", "", "billingResultListener", "Lcom/example/satellitefinder/subscription/SubscriptionCancelListener;", "getBySkuId", "skuId", "", "makePurchase", "activity", "Landroid/app/Activity;", "skuDetailsModel", "onCleared", "Satellite Finder1.4.8__debug"})
public final class SubscriptionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private com.example.satellitefinder.subscription.SubscriptionRepository subscriptionRepository;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope viewModelScope = null;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<java.util.List<com.example.satellitefinder.subscription.SkuDetailsModel>> subSkuDetailsModelListLiveData;
    
    public SubscriptionViewModel(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SubscriptionRepository subscriptionRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.satellitefinder.subscription.SubscriptionRepository getSubscriptionRepository() {
        return null;
    }
    
    public final void setSubscriptionRepository(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SubscriptionRepository p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.satellitefinder.subscription.SkuDetailsModel>> getSubSkuDetailsModelListLiveData() {
        return null;
    }
    
    public final void setSubSkuDetailsModelListLiveData(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<java.util.List<com.example.satellitefinder.subscription.SkuDetailsModel>> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.example.satellitefinder.subscription.SkuDetailsModel getBySkuId(@org.jetbrains.annotations.NotNull()
    java.lang.String skuId) {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    public final void makePurchase(@org.jetbrains.annotations.NotNull()
    android.app.Activity activity, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SkuDetailsModel skuDetailsModel) {
    }
    
    public final void addBillingCancelListener(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.subscription.SubscriptionCancelListener billingResultListener) {
    }
}