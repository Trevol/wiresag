package com.example.wiresag.mapView

import android.content.Context
import android.graphics.drawable.Drawable
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

class ItemizedIconOverlay2<Item : OverlayItem>(
    items: List<Item>,
    defaultMarker: Drawable,
    onItemGestureListener: OnItemGestureListener<Item>?,
    context: Context
) : ItemizedIconOverlay<Item>(items, defaultMarker, onItemGestureListener, context) {
    val items get() = mItemList
}