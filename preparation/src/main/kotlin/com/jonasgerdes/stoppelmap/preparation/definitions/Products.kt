package com.jonasgerdes.stoppelmap.preparation.definitions

import com.jonasgerdes.stoppelmap.dto.Locale
import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.Locales.en
import com.jonasgerdes.stoppelmap.dto.data.Alias
import com.jonasgerdes.stoppelmap.dto.data.Product
import com.jonasgerdes.stoppelmap.preparation.localizedString

val foodProducts = listOf(
    product(slug = "item_beer", de = "Bier", en = "Beer"),
    product(slug = "item_softdrinks", de = "Softdrinks", en = "Soft drinks", de to "Limonaden"),
    product(slug = "item_shots", de = "Kurze", en = "Shots", de to "Shots"),
    product(slug = "item_longdrinks", de = "Longdrinks", en = "Long drinks", de to "Mischgetränke"),
    product(slug = "item_cocktails", de = "Cocktails", en = "Cocktails"),
    product(slug = "item_punchbowl", de = "Bowle", en = "Punchbowl"),
    product(slug = "item_wine", de = "Wein", en = "Wine"),
    product(slug = "item_pizza", de = "Pizza", en = "Pizza"),
    product(slug = "item_fries", de = "Pommes", en = "Fries"),
    product(slug = "item_fries_sweetpotato", de = "Süßkartoffelpommes", en = "Sweetpotato fries"),
    product(slug = "item_tornado_potato", de = "Kartoffeltwister", en = "Tornado potato"),
    product(slug = "item_sausauge", de = "Bratwurst", en = "Bratwurst"),
    product(slug = "item_currywurst", de = "Currywurst", en = "Currywurst"),
    product(slug = "item_hotdog", de = "Hotdog", en = "hot dog"),
    product(slug = "item_hamburger", de = "Hamburger", en = "Burger"),
    product(slug = "item_corn", de = "Mais", en = "Corn"),
    product(
        slug = "item_mushrooms",
        de = "Pilze",
        en = "Mushrooms",
        null to "Champignons",
    ),
    product(slug = "item_fish", de = "Fisch", en = "Fish"),
    product(slug = "item_salmon", de = "Lachs", en = "Salmon", de to "Räucherlachs"),
    product(slug = "item_steak", de = "Steak", en = "Steak"),
    product(slug = "item_bread", de = "Brote", en = "Bread"),
    product(slug = "item_ham", de = "Schinken", en = "Ham"),
    product(slug = "item_pretzel", de = "Brezel", en = "Pretzel"),
    product(slug = "item_churros", de = "Churros", en = "Churros"),
    product(slug = "item_broccoli", de = "Brokkoli", en = "Broccoli"),
    product(slug = "item_pasta", de = "Pasta", en = "Pasta", de to "Nudeln"),
    product(
        slug = "item_fried_potatoes",
        de = "Bratkartoffeln",
        en = "Fried potatoes",
        en to "Home fries"
    ),
    product(slug = "item_fried_egg", de = "Spiegelei", en = "Fried eg", en to "Sunny side up"),
    product(
        slug = "item_potato_fritter",
        de = "Reibekuchen",
        en = "Potato fritter",
        en to "Potato cake"
    ),
    product(slug = "item_potatoes", de = "Kartoffeln", en = "Potatoes"),
    product(slug = "item_chinese", de = "Chinesisch", en = "Chinese"),
    product(slug = "item_spit_roast", de = "Spießbraten", en = "Spit roast"),
    product(slug = "item_gyros", de = "Gyros", en = "Gyros"),
    product(slug = "item_kebap", de = "Kebap", en = "Kebap"),
    product(slug = "item_shashlik", de = "Schaschlik", en = "Shashlik"),
    product(slug = "item_cheese", de = "Käse", en = "Cheese"),
    product(slug = "item_tarte_flambee", de = "Flammkuchen", en = "Tarte flambée"),
    product(slug = "item_bruschetta", de = "Bruschetta", en = "Bruschetta"),
    product(slug = "item_langos", de = "Lángos", en = "Lángos"),
    product(slug = "item_vegetables", de = "Gemüse", en = "Vegetables"),
    product(slug = "item_cauliflower", de = "Blumenkohl", en = "Cauliflower"),
    product(
        slug = "item_pickles",
        de = "Essiggurken",
        en = "Pickles",
        de to "Gewürzgurken",
        en to "Gherkins"
    ),
    product(slug = "item_crepe", de = "Crepé", en = "Crepé"),
    product(slug = "item_roasted_almonds", de = "Gebrannte Mandeln", en = "Roasted Alonds"),
    product(slug = "item_ice_cream", de = "Eis", en = "Ice Cream", de to "Softeis"),
    product(slug = "item_slush", de = "Slusheis", en = "Slushy"),
    product(slug = "item_frozen_yogurt", de = "Frozen Yogurt", en = "Frozen Yogurt"),
    product(slug = "item_cotton_candy", de = "Zuckerwatte", en = "Cotton candy"),
    product(slug = "item_gingerbread", de = "Lebkuchen", en = "Gingerbread"),
    product(slug = "item_waffle", de = "Waffeln", en = "Waffles"),
    product(slug = "item_wine_gums", de = "Weingummi", en = "Wine gums"),
    product(slug = "item_bonbons", de = "Bonbons", en = "Candies"),
    product(slug = "item_licorice", de = "Lakritz", en = "Licorice"),
    product(slug = "item_poffertje", de = "Poffertje", en = "Poffertje"),
    product(
        slug = "item_deep_frying_dough",
        de = "Schmalzgebäck",
        en = "Deep-frying dough",
        en to "Olykoek"
    ),
    product(slug = "item_backery", de = "Backwaren", en = "pastries"),
    product(slug = "item_chocolate_fruits", de = "Schokofrüchte", en = "Chocolate fruits"),
    product(
        slug = "item_kurtosh_kolach",
        de = "Baumstriezel",
        en = "Kürtőskalács",
        en to "Kurtosh Kolach"
    ),
    product(slug = "item_bubble_tea", de = "Bubble Tea", en = "Bubble tea"),
)

private fun product(
    slug: String,
    de: String,
    en: String,
    vararg aliases: Pair<Locale?, String>
) = Product(
    slug = slug,
    name = localizedString(de = de, en = en),
    aliases = aliases.map {
        Alias(
            string = it.second,
            locale = it.first,
        )
    }
)