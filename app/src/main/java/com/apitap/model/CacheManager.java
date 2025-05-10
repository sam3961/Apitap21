package com.apitap.model;

import android.content.Context;

import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class CacheManager {
    private static SimpleCache simpleCache;

    public static synchronized SimpleCache getInstance(Context context) {
        if (simpleCache == null) {
            long maxCacheSize = 100 * 1024 * 1024; // 100 MB
            LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
            simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), evictor);
        }
        return simpleCache;
    }
}
