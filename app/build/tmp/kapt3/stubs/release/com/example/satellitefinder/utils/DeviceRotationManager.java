package com.example.satellitefinder.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001:\u0001\u001bB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0006\u0010\u0017\u001a\u00020\u0010J\u0006\u0010\u0018\u001a\u00020\u0010J\b\u0010\u0019\u001a\u00020\u0010H\u0002J\b\u0010\u001a\u001a\u00020\u0010H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b8BX\u0082\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u000b\u001a\u0004\u0018\u00010\f8BX\u0082\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u001c"}, d2 = {"Lcom/example/satellitefinder/utils/DeviceRotationManager;", "Landroid/hardware/SensorEventListener;", "context", "Landroid/content/Context;", "onRotationChangedListener", "Lcom/example/satellitefinder/utils/DeviceRotationManager$OnRotationChangedListener;", "(Landroid/content/Context;Lcom/example/satellitefinder/utils/DeviceRotationManager$OnRotationChangedListener;)V", "mSensorManager", "Landroid/hardware/SensorManager;", "getMSensorManager", "()Landroid/hardware/SensorManager;", "rotationVectorSensor", "Landroid/hardware/Sensor;", "getRotationVectorSensor", "()Landroid/hardware/Sensor;", "onAccuracyChanged", "", "sensor", "accuracy", "", "onSensorChanged", "event", "Landroid/hardware/SensorEvent;", "onStart", "onStop", "registerSensorsListener", "unregisterSensorsListener", "OnRotationChangedListener", "Satellite Finder1.4.8__release"})
public final class DeviceRotationManager implements android.hardware.SensorEventListener {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.satellitefinder.utils.DeviceRotationManager.OnRotationChangedListener onRotationChangedListener = null;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.SensorManager mSensorManager;
    @org.jetbrains.annotations.Nullable()
    private android.hardware.Sensor rotationVectorSensor;
    
    public DeviceRotationManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.example.satellitefinder.utils.DeviceRotationManager.OnRotationChangedListener onRotationChangedListener) {
        super();
    }
    
    public final void onStart() {
    }
    
    public final void onStop() {
    }
    
    private final android.hardware.SensorManager getMSensorManager() {
        return null;
    }
    
    private final android.hardware.Sensor getRotationVectorSensor() {
        return null;
    }
    
    private final void registerSensorsListener() {
    }
    
    private final void unregisterSensorsListener() {
    }
    
    @java.lang.Override()
    public void onAccuracyChanged(@org.jetbrains.annotations.NotNull()
    android.hardware.Sensor sensor, int accuracy) {
    }
    
    @java.lang.Override()
    public void onSensorChanged(@org.jetbrains.annotations.NotNull()
    android.hardware.SensorEvent event) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\b`\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/example/satellitefinder/utils/DeviceRotationManager$OnRotationChangedListener;", "", "onRotationChanged", "", "x", "", "y", "Satellite Finder1.4.8__release"})
    public static abstract interface OnRotationChangedListener {
        
        public abstract void onRotationChanged(float x, float y);
    }
}