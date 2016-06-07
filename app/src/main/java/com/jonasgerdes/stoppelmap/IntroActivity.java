package com.jonasgerdes.stoppelmap;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Jonas on 09.08.2015.
 */
public class IntroActivity extends AppIntro2 {


    @Override
    public void init(Bundle bundle) {
        addSlide(AppIntroFragment.newInstance("StoppelMap", "StoppelMap ist eine Karte des Stoppelmarktes mit allen Ständen, Zelten und Fahrgeschäften.", R.drawable.icon_128, Color.rgb(49, 27, 146)));
        addSlide(AppIntroFragment.newInstance("Karte", "Die Karte kannst du bewegen, drehen und zoomen wie bei GoogleMaps.", R.drawable.ic_intro_map, Color.rgb(129, 199, 132)));
        addSlide(AppIntroFragment.newInstance("Suche", "Über die Suchfunktion kannst du z.b. Toiletten, die Wilde Maus oder den nächsten Bierstand finden.", R.drawable.intro_search, Color.rgb(179, 157, 219)));
        addSlide(AppIntroFragment.newInstance("Menü", "Berühre den Menü-Button oder wische von links, um das Menü zu öffnen. Hier findest du eine Legende und weitere Infos.", R.drawable.ic_intro_menu, Color.rgb(140, 158, 255)));
        addSlide(AppIntroFragment.newInstance("Los geht's!", "Viel Spaß auf dem Stoppelmarkt!", R.drawable.icon_128, Color.rgb(49, 27, 146)));
    }

    @Override
    public void onDonePressed() {
        finish();
    }
}
