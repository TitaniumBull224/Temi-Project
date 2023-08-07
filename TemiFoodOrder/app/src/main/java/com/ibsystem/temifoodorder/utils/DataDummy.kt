//package com.ibsystem.temifoodorder.utils
//
//import com.ibsystem.temifoodorder.R
//import com.ibsystem.temifoodorder.domain.model.AboutItem
//import com.ibsystem.temifoodorder.domain.model.CategoryItem
//import com.ibsystem.temifoodorder.domain.model.ProductItem
//import com.ibsystem.temifoodorder.ui.theme.*
//
//object DataDummy {
//
//    fun generateDummyProduct(): List<ProductItem> {
//        return listOf(
//            ProductItem(
//                id = "1",
//                name = "Macaroni",
//                description = "Macaroni is dry pasta shaped like narrow tubes.[2] Made with durum wheat, macaroni is commonly cut in short lengths; curved macaroni may be referred to as elbow macaroni. ",
//                image = R.drawable.product1,
//                price = 6.52
//            ),
//            ProductItem(
//                id = "2",
//                name = "Egg",
//                description = "Eggs have a hard shell of calcium carbonate enclosing a liquid white, a single yolk (or an occasional double yolk)and an air cell.",
//                image = R.drawable.product2,
//                price = 2.50
//            ),
//            ProductItem(
//                id = "3",
//                name = "Mayonnaise",
//                description = "Mayonnaise is the creamy white condiment you use to make tuna salad or spread on your BLT sandwich. Most mayonnaise is made from egg yolks, oil, and lemon juice. Mayonnaise is common in many foods around the world, from fancy French sauces like rémoulade to Japanese okonomiyaki.",
//                image = R.drawable.product3,
//                price = 7.5
//            ),
//            ProductItem(
//                id = "4",
//                name = "Egg Noodles",
//                description = "A type of flat pasta that differs from regular flour and water pasta in that eggs are added to enrich the dough. Several different widths are produced commercially and the noodles are available fresh or dried.",
//                image = R.drawable.product4,
//                price = 9.5
//            ),
//            ProductItem(
//                id = "5",
//                name = "Ginger",
//                description = "Ginger is one of the most popular spices in the world and comes from the underground stem of the ginger plant. The aromatic and fiery spice has been a signature ingredient in Asian cuisine since ancient times.",
//                image = R.drawable.product5,
//                price = 2.0
//            ),
//            ProductItem(
//                id = "6",
//                name = "Diet Coke",
//                description = "Diet coke® is the perfect balance of crisp and refreshing, with no sugar and no calories. enjoy the great diet cola flavour that's fizzing delicious! CARBONATED WATER, CARAMEL COLOUR, PHOSPHORIC AND CITRIC ACID, ASPARTAME (CONTAINS PHENYLALANINE), FLAVOUR, SODIUM BENZOATE, CAFFEINE, ACESULFAME-POTASSIUM.",
//                image = R.drawable.product6,
//                price = 1.5
//            ),
//            ProductItem(
//                id = "7",
//                name = "Broilers",
//                description = "A broiler is any chicken (Gallus gallus domesticus) that is bred and raised specifically for meat production. Most commercial broilers reach slaughter weight between four and six weeks of age, although slower growing breeds reach slaughter weight at approximately 14 weeks of age.",
//                image = R.drawable.product7,
//                price = 5.0
//            ),
//            ProductItem(
//                id = "8",
//                name = "Juice Apple",
//                description = "It is a very clear liquid from which the pulp has been removed. This juice is often used to flavor meats, or as an ingredient in dressings and sauces, adding a sweet apple flavor.",
//                image = R.drawable.product8,
//                price = 6.5
//            ),
//            ProductItem(
//                id = "9",
//                name = "Juice Orange",
//                description = "Orange juice is a fruit juice obtained by squeezing, pressing or otherwise crushing the interior of an orange. Orange juice is a nutrition powerhouse with naturally occurring vitamins and minerals and many antioxidants.",
//                image = R.drawable.product9,
//                price = 5.54
//            ),
//            ProductItem(
//                id = "10",
//                name = "Banana",
//                description = "A banana is an elongated, edible fruit – botanically a berry – produced by several kinds of large herbaceous flowering plants in the genus Musa. In some countries, bananas used for cooking may be called \"plantains\", distinguishing them from dessert bananas.",
//                image = R.drawable.product10,
//                price = 9.10
//            ),
//        )
//    }
//
//    fun generateDummyCategories(): List<CategoryItem> {
//        return listOf(
//            CategoryItem(
//                cat_id = "1",
//                cat_name = "Fresh Fruits\n" + "& Vegetable",
//                image = R.drawable.category1,
//                background = BackgroundCategory1
//            ),
//            CategoryItem(
//                cat_id = "2",
//                cat_name = "Cooking Oil\n" + "& Ghee",
//                image = R.drawable.category2,
//                background = BackgroundCategory2
//            ),
//            CategoryItem(
//                cat_id = "3",
//                cat_name = "Meat & Fish",
//                image = R.drawable.category3,
//                background = BackgroundCategory3
//            ),
//            CategoryItem(
//                cat_id = "4",
//                cat_name = "Bakery & Snacks",
//                image = R.drawable.category4,
//                background = BackgroundCategory4
//            ),
//            CategoryItem(
//                cat_id = "5",
//                cat_name = "Dairy & Eggs",
//                image = R.drawable.category5,
//                background = BackgroundCategory5
//            ),
//            CategoryItem(
//                cat_id = "6",
//                cat_name = "Beverages",
//                image = R.drawable.category6,
//                background = BackgroundCategory6
//            )
//        )
//    }
//
//    fun generateDummyAbout(): List<AboutItem> {
//        return listOf(
//            AboutItem(
//                image = R.drawable.ic_orders,
//                title = "Orders"
//            ),
//            AboutItem(
//                image = R.drawable.ic_my_details,
//                title = "My Details"
//            ),
//            AboutItem(
//                image = R.drawable.ic_address,
//                title = "Delivery Address"
//            ),
//            AboutItem(
//                image = R.drawable.ic_payment,
//                title = "Payment"
//            ),
//            AboutItem(
//                image = R.drawable.ic_notification,
//                title = "Notification"
//            ),
//        )
//    }
//
//}