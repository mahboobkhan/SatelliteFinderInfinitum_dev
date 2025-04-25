package com.example.satellitefinder.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001:\u0001\u0005J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&\u00a8\u0006\u0006"}, d2 = {"Lcom/example/satellitefinder/utils/ConnectivityObserver;", "", "observe", "Lkotlinx/coroutines/flow/Flow;", "Lcom/example/satellitefinder/utils/ConnectivityObserver$Status;", "Status", "Satellite Finder1.4.8__debug"})
public abstract interface ConnectivityObserver {
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.example.satellitefinder.utils.ConnectivityObserver.Status> observe();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/example/satellitefinder/utils/ConnectivityObserver$Status;", "", "(Ljava/lang/String;I)V", "Disconnected", "Connected", "Satellite Finder1.4.8__debug"})
    public static enum Status {
        /*public static final*/ Disconnected /* = new Disconnected() */,
        /*public static final*/ Connected /* = new Connected() */;
        
        Status() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.example.satellitefinder.utils.ConnectivityObserver.Status> getEntries() {
            return null;
        }
    }
}