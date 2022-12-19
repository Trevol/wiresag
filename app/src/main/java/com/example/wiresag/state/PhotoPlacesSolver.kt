package com.example.wiresag.state

import com.example.wiresag.math.PointsAtDistanceToLineSegmentMidpoint
import com.example.wiresag.math.geo.Earth

const val PhotoDistToSpan = 20.0
val photoPlacesSolver = PointsAtDistanceToLineSegmentMidpoint(PhotoDistToSpan / Earth.r)