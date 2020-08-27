package com.example.informationnation

class HomeFeed(val features: MutableList<Feature>)

class Feature(val properties: Properties, val geometry: Geometry)

class Properties(val name: String?, val icon: String?, val id: Long?)

class Geometry(val type: String?, val coordinates: List<Double>)

class Place(val id: Long, val name: String, val comments: String, val banner: String)

class Details(val place: Place)