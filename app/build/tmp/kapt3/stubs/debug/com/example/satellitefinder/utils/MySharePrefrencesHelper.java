package com.example.satellitefinder.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0012J \u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0015\u001a\u00020\u0012J$\u0010\u0019\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u001a\u001a\u00020\u00142\b\u0010\u001b\u001a\u0004\u0018\u00010\u00182\b\u0010\u0015\u001a\u0004\u0018\u00010\u0018J\u0016\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001d\u001a\u00020\u0012J\u001e\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u001d\u001a\u00020\u0012J$\u0010\u001f\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u001b\u001a\u0004\u0018\u00010\u00182\b\u0010 \u001a\u0004\u0018\u00010\u0018R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcom/example/satellitefinder/utils/MySharePrefrencesHelper;", "", "()V", "editor", "Landroid/content/SharedPreferences$Editor;", "getEditor", "()Landroid/content/SharedPreferences$Editor;", "setEditor", "(Landroid/content/SharedPreferences$Editor;)V", "sharedPreferences", "Landroid/content/SharedPreferences;", "getSharedPreferences", "()Landroid/content/SharedPreferences;", "setSharedPreferences", "(Landroid/content/SharedPreferences;)V", "deleteAllSharedPrefs", "", "getBillingBoolean", "", "context", "Landroid/content/Context;", "default", "getBoolean", "key", "", "getKey", "contextGetKey", "Key", "putBillingBoolean", "value", "putBoolean", "putKey", "Value", "Satellite Finder1.4.8__debug"})
public final class MySharePrefrencesHelper {
    @org.jetbrains.annotations.Nullable()
    private static android.content.SharedPreferences sharedPreferences;
    @org.jetbrains.annotations.Nullable()
    private static android.content.SharedPreferences.Editor editor;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.satellitefinder.utils.MySharePrefrencesHelper INSTANCE = null;
    
    private MySharePrefrencesHelper() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.SharedPreferences getSharedPreferences() {
        return null;
    }
    
    public final void setSharedPreferences(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final android.content.SharedPreferences.Editor getEditor() {
        return null;
    }
    
    public final void setEditor(@org.jetbrains.annotations.Nullable()
    android.content.SharedPreferences.Editor p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String putKey(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.Nullable()
    java.lang.String Key, @org.jetbrains.annotations.Nullable()
    java.lang.String Value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getKey(@org.jetbrains.annotations.NotNull()
    android.content.Context contextGetKey, @org.jetbrains.annotations.Nullable()
    java.lang.String Key, @org.jetbrains.annotations.Nullable()
    java.lang.String p2_772401952) {
        return null;
    }
    
    public final void deleteAllSharedPrefs() {
    }
    
    public final void putBoolean(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key, boolean value) {
    }
    
    public final boolean getBoolean(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String key, boolean p2_772401952) {
        return false;
    }
    
    public final void putBillingBoolean(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean value) {
    }
    
    public final boolean getBillingBoolean(@org.jetbrains.annotations.NotNull()
    android.content.Context context, boolean p1_772401952) {
        return false;
    }
}