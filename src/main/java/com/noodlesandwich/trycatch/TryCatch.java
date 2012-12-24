package com.noodlesandwich.trycatch;

public final class TryCatch {
    private TryCatch() { }

    public static void tryDoing(Runnable runnable) {
        runnable.run();
    }
}

