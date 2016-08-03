package com.jonasgerdes.stoppelmap.usecases.tutorial;

import com.jonasgerdes.stoppelmap.R;
import com.stephentuso.welcome.WelcomeScreenBuilder;
import com.stephentuso.welcome.util.WelcomeScreenConfiguration;

/**
 * Created by Jonas on 02.08.2016.
 */
public class TutorialActivity extends com.stephentuso.welcome.ui.WelcomeActivity {

    @Override
    protected WelcomeScreenConfiguration configuration() {
        return new WelcomeScreenBuilder(this)
                .theme(R.style.TutorialTheme)
                .defaultBackgroundColor(R.color.colorPrimary)
                .basicPage(R.drawable.tutorial_page1, "Moin!", "Mit der StoppelMap findest Du Dich auf dem Stoppelmarkt immer zurecht!", R.color.colorPrimary, true)
                .basicPage(R.drawable.tutorial_page2, "Karte", "Tippe auf die Karte, um mehr Infos zu Zelten, Bierständen, Fahrgeschäften usw. zu sehen.", R.color.colorPrimary, true)
                .basicPage(R.drawable.tutorial_page3, "Suche", "Über die Lupe oben rechts findest Du die nächste Toilette, das Riesenrad oder die Wuba!", R.color.colorPrimary, true)
                .basicPage(R.drawable.tutorial_page4, "Fahrpläne", "Dieses Jahr neu: Busfahrpläne! Öffne diese über das Menü auf der linken Seite und schaue, wann an einer Haltestelle in Deiner Nähe der nächste Bus fährt.", R.color.colorPrimary, true)
                .basicPage(R.drawable.tutorial_page1, "Viel Spaß!", "Los geht's. Man sieht sich auf dem Stoppelmarkt!", R.color.colorPrimary, true)
                .swipeToDismiss(true)
                .build();
    }

    public static String welcomeKey() {
        return "StoppelMap 2.0.0";
    }
}
