package com.example.satellitefinder.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0012\u001a\u00020\tJ\r\u0010\u0013\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\u0014J\r\u0010\u0015\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u0016\u001a\u0004\u0018\u00010\u0003J\u0006\u0010\u0017\u001a\u00020\u0018J\b\u0010\u0019\u001a\u0004\u0018\u00010\u0006J\r\u0010\u001a\u001a\u0004\u0018\u00010\t\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u001b\u001a\u00020\u0006H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\nR\u0012\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\nR\u001a\u0010\f\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/satellitefinder/utils/SatellitesPositionData;", "Ljava/io/Serializable;", "satelliteData", "Lcom/example/satellitefinder/utils/SatellitesInformationData;", "(Lcom/example/satellitefinder/utils/SatellitesInformationData;)V", "TAG", "", "mSatelliteDirection", "mSatelliteLatitude", "", "Ljava/lang/Double;", "mSatelliteLongitude", "mSatelliteName", "getMSatelliteName", "()Ljava/lang/String;", "setMSatelliteName", "(Ljava/lang/String;)V", "mSatellitesInformationData", "getLNBSkew", "getSatLatitude", "()Ljava/lang/Double;", "getSatLongitude", "getSatellite", "getSatelliteAzimut", "", "getSatelliteDirection", "getSatelliteElevation", "toString", "Satellite Finder1.4.5__release"})
public final class SatellitesPositionData implements java.io.Serializable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "SatellitePosition";
    @org.jetbrains.annotations.Nullable()
    private java.lang.String mSatelliteDirection;
    @org.jetbrains.annotations.Nullable()
    private com.example.satellitefinder.utils.SatellitesInformationData mSatellitesInformationData;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double mSatelliteLongitude;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Double mSatelliteLatitude;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String mSatelliteName = "";
    
    public SatellitesPositionData(@org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.utils.SatellitesInformationData satelliteData) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMSatelliteName() {
        return null;
    }
    
    public final void setMSatelliteName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSatelliteDirection() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getSatLongitude() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getSatLatitude() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.example.satellitefinder.utils.SatellitesInformationData getSatellite() {
        return null;
    }
    
    public final int getSatelliteAzimut() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Double getSatelliteElevation() {
        return null;
    }
    
    public final double getLNBSkew() {
        return 0.0;
    }
}