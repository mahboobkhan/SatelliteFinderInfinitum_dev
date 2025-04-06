package com.example.satellitefinder.subscription;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004H\u0002J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0004H\u0002J\u001e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0004R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/example/satellitefinder/subscription/Security;", "", "()V", "BASE_64_ENCODED_PUBLIC_KEY", "", "getBASE_64_ENCODED_PUBLIC_KEY", "()Ljava/lang/String;", "KEY_FACTORY_ALGORITHM", "SIGNATURE_ALGORITHM", "TAG", "createPublicKey", "Ljava/security/PublicKey;", "encodedPublicKey", "verifyKey", "", "publicKey", "signedData", "signature", "verifyPurchaseKey", "base64PublicKey", "Satellite Finder1.4.5__debug"})
public final class Security {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "IABUtil/Security";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_FACTORY_ALGORITHM = "RSA";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SIGNATURE_ALGORITHM = "SHA1withRSA";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtidHztp11vDGLQAd7jTl7LNuXG+mEpnHDtnRuUEAUzirNQA72H2BqcGX2IUJoHcymrfODFS692h2++U0pCja1Ncu+rOVhoT+J9n0GFkNtOZMYXYCMVBuqQAjB7+2WOqcthpX3hWBR8jqsV+7eaXWlPm01Efr3bH0UPBnUBVv6U9KNBDAUCjzQB8eGGIbJf7jT/zrgUFeGhqmrw+8MlFdXIt8m7H61pgIFXnmn78c9Jw7YJCbPjOjEPZl4HWhTYGk4LvcOPErlAymgvR78cPToJMH3L1BTQ+TU0IiQ5dUZcdrH0DFTZ3+NKNakujwxZ2hCRJRRzH8A40+9c6T3/PugwIDAQAB";
    @org.jetbrains.annotations.NotNull()
    public static final com.example.satellitefinder.subscription.Security INSTANCE = null;
    
    private Security() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getBASE_64_ENCODED_PUBLIC_KEY() {
        return null;
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    public final boolean verifyPurchaseKey(@org.jetbrains.annotations.NotNull()
    java.lang.String base64PublicKey, @org.jetbrains.annotations.NotNull()
    java.lang.String signedData, @org.jetbrains.annotations.NotNull()
    java.lang.String signature) throws java.io.IOException {
        return false;
    }
    
    @kotlin.jvm.Throws(exceptionClasses = {java.io.IOException.class})
    private final java.security.PublicKey createPublicKey(java.lang.String encodedPublicKey) throws java.io.IOException {
        return null;
    }
    
    private final boolean verifyKey(java.security.PublicKey publicKey, java.lang.String signedData, java.lang.String signature) {
        return false;
    }
}