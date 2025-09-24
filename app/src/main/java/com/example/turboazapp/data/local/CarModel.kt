package com.example.turboazapp.data.local

import com.example.turboazapp.R

data class Model(
    val id: String,
    val name: String
)

data class Brand(
    val id: String,
    val name: String,
    val models: List<Model>,
    val logoRes: Int
)

object CarData {
    fun brands(): List<Brand> = listOf(
        Brand(
            "toyota", "Toyota",
            listOf(
                Model("corolla", "Toyota Corolla"),
                Model("camry", "Toyota Camry"),
                Model("rav4", "Toyota RAV4")
            ),
            R.drawable.bmw_icon
        ),
        Brand(
            "honda", "Honda",
            listOf(
                Model("civic", "Honda Civic"),
                Model("accord", "Honda Accord"),
                Model("crv", "Honda CR-V")
            ),
            R.drawable.bmw_icon
        ),
        Brand(
            "bmw", "BMW",
            listOf(
                Model("series3", "BMW 3 Series"),
                Model("series5", "BMW 5 Series"),
                Model("x5", "BMW X5")
            ),
            R.drawable.bmw_icon
        ),
        Brand(
            "mercedes", "Mercedes-Benz",
            listOf(
                Model("cclass", "Mercedes C-Class"),
                Model("eclass", "Mercedes E-Class"),
                Model("gle", "Mercedes GLE")
            ),
            R.drawable.mercedes_icon
        ),
        Brand(
            "audi", "Audi",
            listOf(
                Model("a4", "Audi A4"),
                Model("a6", "Audi A6"),
                Model("q7", "Audi Q7")
            ),
            R.drawable.mercedes_icon
        )
    )
}
