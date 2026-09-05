package com.ashepping.fuelcost.domain

enum class Road { CITY, HIGHWAY, OFFROAD }

// Order: currencies of the most populated countries first (unique ISO codes).
enum class Currency(val code: String) {
    CNY("CNY"),
    INR("INR"),
    USD("USD"),
    IDR("IDR"),
    PKR("PKR"),
    NGN("NGN"),
    BRL("BRL"),
    BDT("BDT"),
    RUB("RUB"),
    MXN("MXN"),
    JPY("JPY"),
    ETB("ETB"),
    PHP("PHP"),
    EGP("EGP"),
    VND("VND"),
    CDF("CDF"),
    TRY("TRY"),
    IRR("IRR"),
    EUR("EUR"),
    THB("THB"),
    GBP("GBP"),
    TZS("TZS"),
    ZAR("ZAR"),
    KES("KES"),
    MMK("MMK"),
    COP("COP"),
    KRW("KRW"),
    UGX("UGX"),
    DZD("DZD"),
    IQD("IQD"),
    ARS("ARS"),
    CAD("CAD"),
    PLN("PLN"),
    MAD("MAD"),
    UAH("UAH"),
    UZS("UZS"),
    MYR("MYR"),
    PEN("PEN"),
    GHS("GHS"),
    SAR("SAR"),
    XOF("XOF"),
    XAF("XAF"),
    AUD("AUD"),
    TWD("TWD"),
    LKR("LKR"),
    KZT("KZT"),
    CLP("CLP"),
    CZK("CZK"),
    RON("RON"),
    AED("AED")
}

data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val cityL100: Double?,
    val highwayL100: Double?
) {
    val title: String get() = "$brand $model"
}

data class EstimateInput(
    val cityL100: Double?,
    val highwayL100: Double?,
    val mixedL100: Double?,
    val year: Int?,
    val distanceKm: Double,
    val road: Road,
    val acOn: Boolean,
    val heatOn: Boolean,
    val pricePerLiter: Double,
    val nowYear: Int = 2026
)

data class Estimate(
    val liters: Double,
    val cost: Double,
    val litersText: String,
    val costText: String
)
