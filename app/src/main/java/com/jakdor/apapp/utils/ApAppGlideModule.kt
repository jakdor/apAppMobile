package com.jakdor.apapp.utils

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class ApAppGlideModule : AppGlideModule() {

    /**
     * Disable manifest parsing to avoid adding similar modules twice
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}