package com.ashepping.fuelcost.data

import com.ashepping.fuelcost.domain.Car

object Cars {
    const val CUSTOM_ID = "custom"

    val catalog: List<Car> = listOf(
        Car("sandero", "Dacia", "Sandero", 6.8, 5.0),
        Car("clio", "Renault", "Clio", 6.6, 4.8),
        Car("troc", "VW", "T-Roc", 8.0, 5.8),
        Car("tiguan", "VW", "Tiguan", 8.6, 6.2),
        Car("golf", "VW", "Golf", 7.2, 5.2),
        Car("yaris-cross", "Toyota", "Yaris Cross", 6.2, 4.8),
        Car("208", "Peugeot", "208", 6.4, 4.7),
        Car("2008", "Peugeot", "2008", 7.0, 5.2),
        Car("duster", "Dacia", "Duster", 8.2, 6.0),
        Car("yaris", "Toyota", "Yaris", 5.8, 4.4),
        Car("corsa", "Opel", "Corsa", 6.5, 4.8),
        Car("c3", "Citroen", "C3", 6.6, 4.9),
        Car("octavia", "Skoda", "Octavia", 7.0, 5.0),
        Car("puma", "Ford", "Puma", 7.2, 5.3),
        Car("tucson", "Hyundai", "Tucson", 8.4, 6.1),
        Car("sportage", "Kia", "Sportage", 8.4, 6.1),
        Car("qashqai", "Nissan", "Qashqai", 8.0, 5.8),
        Car("captur", "Renault", "Captur", 7.2, 5.4),
        Car("zs", "MG", "ZS", 8.0, 6.0),
        Car("a3", "Audi", "A3", 7.4, 5.3),
        Car("3008", "Peugeot", "3008", 8.2, 6.0),
        Car("x1", "BMW", "X1", 8.5, 6.2),
        Car("panda", "Fiat", "Panda", 6.4, 4.8),
        Car("chr", "Toyota", "C-HR", 6.4, 5.0),
        Car("corolla", "Toyota", "Corolla", 6.4, 4.8),
        Car("tcross", "VW", "T-Cross", 7.2, 5.4),
        Car("fabia", "Skoda", "Fabia", 6.4, 4.7),
        Car("superb", "Skoda", "Superb", 7.6, 5.4),
        Car("kodiaq", "Skoda", "Kodiaq", 8.8, 6.4),
        Car("passat", "VW", "Passat", 7.6, 5.4),
        Car("polo", "VW", "Polo", 6.6, 4.8),
        Car("rav4", "Toyota", "RAV4", 7.4, 5.8),
        Car("civic", "Honda", "Civic", 7.0, 5.2),
        Car("cx5", "Mazda", "CX-5", 8.2, 6.0),
        Car("mazda3", "Mazda", "3", 7.2, 5.2),
        Car("bmw3", "BMW", "3 Series", 8.0, 5.6),
        Car("cclass", "Mercedes", "C-Class", 8.2, 5.8),
        Car("a4", "Audi", "A4", 8.0, 5.6),
        Car("xc60", "Volvo", "XC60", 8.8, 6.4),
        Car("ceed", "Kia", "Ceed", 7.2, 5.2)
    )

    fun byId(id: String): Car? = catalog.find { it.id == id }
}
