package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R
import com.example.food_label_scanner.data.Section
import com.example.food_label_scanner.ui.theme.Cream

@Composable
fun NutritionalDictionary(){

    val instrumentSerif = FontFamily(Font(R.font.instrument_serif_regular))

    val sections = remember {
        mutableStateListOf(
            Section(
                title = "Carbohidrati",
                content = "   Carbohidrații sunt una dintre cele trei macronutriente principale" +
                        " (alături de proteine și grăsimi)" +
                        " și reprezintă principala sursă de energie pentru organism.\n\n" +
                        "   Sunt de două tipuri - simpli (zahăr, fructoză)" +
                        " care se absorb rapid în sânge și complexi (amidon, fibre)" +
                        " care se digeră mai lent, oferind energie pe termen lung\n\n" +
                        "   Regăsiți în pâine, paste, orez, fructe legume, zahăr și produse dulci.\n\n" +
                        "   Ar trebui să reprezinte 45-65% din aportul caloric zilnic," +
                        " preferați fiind carbohidrații complexi," +
                        " cei simpli putând aduce diverse probleme" +
                        " de metabolism dacă sunt consumați în cantități mari",

            ),
            Section(
                title = "Proteine",
                content ="  Proteinele sunt macronutrienți (alături de carbohidrați și grăsimi) ,care joacă un rol important în organismul uman\n\n" +
                        "Ele realizează majoritatea sarcinilor în cadrul celulelor" +
                        " - necesare pentru structură, funcționare," +
                        " regularea țesutului organelor, producerea enzimelor și hormonilor.\n\n" +
                        "   Proteinele sunt complete (au toți aminoacizii esențiali)" +
                        " și incomplete. De regulă cele complete sunt de origine animală" +
                        " (carne, pește, ouă, lactate)" +
                        " iar cele incomplete sunt de origine vegetală" +
                        " (fasole, linte, nuci). Excepție de regulă sunt Quinoa și Soia care sunt sigurele surse vegetale complete.\n\n " +
                        "Proteinele ar trebui să reprezinte 10-35 %" +
                        " din aportul caloric zilnic ele contribuind și la menținerea masei musculare.\n" +
                        "O cantitate prea mare de proteine poate duce la probleme cu rinichii și luarea în greutate."
            ),
            Section(
                title = "Grăsimile",
                content = "  Grăsimile sunt al treilea tip de macronutrienți" +
                        " alături de proteine și grăsimi, denumite și lipide" +
                        " sunt responsabile pentru multe funcții precum producția de hormoni ai sexului,  " +
                        " menținerea integrității celulare," +
                        " stocare de energie sub forma grăsimii corporale, " +
                        "controlarea temperaturii corporale sau absorbția unor vitamine" +
                        "(A, D, E, K)\n\n" +
                        "   Ele sunt de 2 tiprui principale:" +
                        " Grăsimi saturate care provin din carne grasă," +
                        " uleiuri (de palmier, de rapiță, din nucă de cocos, din soia)" +
                        " si sunt de regulă în fiecare preparat procesat (hot dog, pizza, burger,etc)," +
                        " ele pot provoca probleme cardiovasculare (cresc colesterolul rău)" +
                        " dacă sunt consumate frecvent și în cantități mari.\n" +
                        " Grăsimi nesaturate care se împart ulterior în mononesaturate (cum ar fi uleiul de măsline, avocado, nuci) " +
                        "și polinesaturate (semințele de in, uleiul de floarea soarelui, peștele gras precum somonul)" +
                        " ele fiind benefice pentru consum ajutând la sănătatea cardiovasculară și a creierului\n\n"

            ),
            Section(
                title = "Emulgatori",
                content = "  Emulgatorii sau E-urile sunt substanțe care permit combinarea" +
                        " a două lichide care nu se combină natural (exemplu fiind uleiul și apa)" +
                        " Un emulgator este compus în general" +
                        " dintr-o parte hidrofilă și o altă parte care se combină cu uleiul.\n" +
                        " Sunt fie naturali - lecitina (din gălbenuș de ou sau soia)," +
                        " monogliceride & digliceride (derivate din grăsimi)" +
                        " sau agar-agar (din alge roșii)," +
                        " fie sintetici precum polisorbat 80 (E433)," +
                        " carboximetilceluloza (E466)," +
                        " esterii de zaharoză (E473) și mulți alții.\n " +
                        "Ca beneficii ele pot elimina din necesarul" +
                        " de grăsimi și îmbunătățesc textura" +
                        " iar dezavantajele sunt date de afectarea microbiotei intestinale" +
                        " dacă sunt consumați în cantități considerabile" +
                        " iar consumul mare de emulgatori sintetici pot duce la inflamație intestinală"
            ),
            Section(
                title = "Conservanții",
                content = " Ei sunt aditivi alimentari cu rolul" +
                        " de a prelungi perioada de viață a produsului" +
                        " la raft și de a împiedica apariția bacteriilor sau mucegaiurilor." +
                        "(împiedică schimbările de culoare, textură sau gust)\n " +
                        " Conservanții sunt fie naturali, fie sintetici (atât cei naturali cât și cei sintetici" +
                        " se mai împart ulterior în două categorii și anume antioxidanți și antimicrobiali).\n" +
                        " Cei mai importanți conservanți naturali sunt zahărul," +
                        " sarea, oțetul care sunt de cei mai multe ori folosiți" +
                        " în gospodării la gemuri, compoturi, murături." +
                        " Alte exemple importante din această categorie sunt oregano," +
                        " uleiurile esențiale (rozmarin, usturoi), mierea, turmericul" +
                        " sau natamicina (E235) care se realizează folosind bacterii deja existente în sol.\n" +
                        " Conservanții sintetici sunt compuși realizați" +
                        " de către om și au aceelași rol ca cei naturali doar că" +
                        " sunt mult mai folosiți în producția de mare consum a alimentelor." +
                        " Aceștia ridică în general îngrijorări pentru alergiile" +
                        " care le provoacă pentru oamenii sensibili la diferite substanțe (cum ar fi sulfiții) - astm." +
                        "Dar astmul nu este singura problemă de sănătate care poate fi provocată de conservanții sintetici." +
                        " Ei (în acest caz nitrații și nitriții)" +
                        " mai pot reacționa cu alte substanțe din produse formând" +
                        " un compus numit nitrosamină un compus cancerogen bine cunoscut.\n" +
                        " Conservanții sintetici se află între:\n" +
                        "E200 – E203, E210 - E213, E214 - E219, E220 - E228, E249 - E252, E270, E280 - E283, E300 - E304, E306 - E309, E310 - E312, E338 "

            ),
            Section(
                title = "Agenți de îngroșare și stabilizatorii",
                content = " Agenții de îngroșare măresc vâscozitatea produselor" +
                        " alimentare dându-le o textură mai groasă sau gelatinoasă." +
                        " Ei se împart în agenți naturali cum ar fi pectina," +
                        " guma de guar sau amidonul modificat sau sintetici precum" +
                        " guma de xantan sau caboximetilceluloza.\n" +
                        "Provoacă probleme digestive doar consumați în cantități mari." +
                        " Se regăsesc în pâine, dulciuri, produse de patiserie," +
                        " sosuri, dulciuri, băuturi (sucuri cu pulpă).\n\n" +

                        "   Similar cu agenții de îngroșare," +
                        " stabilizatori mențin structura și" +
                        " consistența alimentelor prevening formarea" +
                        " de cristale (în cazul înghețatei)" +
                        " sau a cocoloașelor și stabilizează emulsia.\n" +
                        "Se împart la fel ca și agenții de îngroșare și în general aceia sunt folosiți și ca stabilizatori pe lângă alții precum alginat de sodiu."
            ),
            Section(
                title = "Îndulcitori",
                content = "  Sunt alternative la zahăr sub diferite forme - lichid sau solid." +
                        " Sunt fie din extrase ale plantelor precum ștevia, fructul călugărului," +
                        " taumatină (din cojile semințelor fructului katemfe)" +
                        " sau psicoza (obținută din fructoză)," +
                        " fie create în laborator - xilitol, lactiol, aspartam, sorbitol, malticol." +
                        " Desi sunt bune pentru reducerea aportului caloric și" +
                        " permit unelor persoane cu diabet să consume produse dulci," +
                        " pot provoca dureri de cap, anxietate, depresie, insomnie" +
                        " sau în cazuri izolate convulsii (toate în cazul aspartamului).\n" +
                        " Alți îndulcitori provoacă probleme cardiovasculare și cresc riscul de accident vascular cerebral dar și cel al obezității." +
                        " Există peste 40 de alternative la zahăr, se recomandă căutarea și citirea despre cei de interes.\n\n"

            ),
            Section(
                title = "Acidifianți",
                content = " Reglează nivelul de aciditate din cadrul alimentului respectiv," +
                        " poate spori conservarea acestuia" +
                        " și chiar îmbunătăți aroma în unele cazuri.\n\n" +
                        "Nu prezintă riscuri de sănătate," +
                        " doar în cazul unei cantități mari," +
                        " care poate duce la dezechilibre ale echilibrului" +
                        " acidității sistemului digestiv "

            ),
            Section(
                title = "Coloranți",
                content = " Sunt meniți să dea o anumită culoare sau aspect unui produs," +
                        " îmbunătățind astfel aspectul vizual al acestuia\n" +
                        "Sunt fie naturali proveniți din plante, insecte sau minerale," +
                        " fie sintetici realizați în laborator. Cei naturali sunt considerați mai" +
                        " puțin stabiliîn procesul de includere" +
                        " în produs dar nu prezintă nici un risc pentru sănătate.\n" +
                        "Cei artificiali sunt mult mai stabili" +
                        " în procesul de producție dar s-a descoperit" +
                        " că sunt cancerigeni. Coloranți precum" +
                        " E102, E110, E129 și alții au fost descoperiți" +
                        " că sunt contaminate cu benzidină, un bine cunoscut cancerigen." +
                        " Testele realizate arată că tot acestea provoacă hiper-senzitivitate" +
                        " și ridică suspiciuni." +
                        " Alți coloranți (E121, Oranj B) au avut un" +
                        " nivel mare de probleme la testele de toxicitate\n\n"

            ),
            Section(
                title = "Potențiatori de gust",
                content = " Au rolul de a intensifica sau îmbunătăți un gust," +
                        " miros deja existent al unui produs." +
                        " Partea bună este că pot reduce conținutul de zahăr" +
                        " sau sare al produsul dar potențiatorii de gust au alte efecte secundare: " +
                        "inflamația, dureri de cap frecvente," +
                        " iritație la oamenii sensibili," +
                        " creșterea tensiunii arteriale, balonare," +
                        " creștere în greutate (acestea toate în cazul Monoglutamatului de sodiu)\n"

            ),
            Section(
                title = "Aditivii",
                content = " Sunt substanțe adăugate pentru a crește" +
                        " perioada de prospețime, intensifica gustul," +
                        " mirosul, culoarea sau alte caracteristici ce țin de produs\n" +
                        "O parte bună din categoriile de aditivi sunt regăsite mai sus:" +
                        " coloranți, potențiatori de gust, stabilizatori, acidifianți," +
                        " îndulcitori, emulgatori, stabilizatori de gust sau conservanți\n" +
                        "Alți aditivi care sunt incluși în această categorie sunt umectanții, agenții antiaglomerare, balsamuri pentru aluat."
            ),
            Section(
                title = "Vitamine și minerale",
                content = "  Vitaminele sunt molecule organice esențiale" +
                        " corpului pentru o funcționare corectă a organismului." +
                        " Mare parte dintre vitamine nu pot fi produse în cantități suficiente" +
                        " și trebuie luate din surse externe organismului\n\n" +
                        "Cele 13 vitamine esențiale și cele mai bune surse pentru fiecare sunt:" +
                        " A (ficat de vită, miel, ouă, ulei de cod)," +
                        " B1 (porc, somon, fasole neagră)," +
                        " B2 (Lactate, ouă, carne, migdale),  B3 (piept de pui, ton, curcan)," +
                        " B5 (cereale, ciuperci, vită, avocado, nuci, ficat de vită, miel)," +
                        " B6 (năut, ficat de vită, ton, somon, cartofi fierți)," +
                        " B7 (ouă, avocado, cartofi dulci, somon)," +
                        " B9 (mazăre, linte, fasole, asparagus, spanac, kale)," +
                        " B12 (scoici, sardine, vită)," +
                        " C (portocale, lămâi, roșii, căpșuni, kiwi)," +
                        " D (hering, sardine, pește gras, expunerea la soare)," +
                        " E (semințe de floarea soarelui, nuci de pădure, nuci braziliene, semințe de dovleac, carnea de gâscă)," +
                        " K (varză de brussels, spanac, kale, brocoli)\n\n" +
                        "   Mineralele sunt unele dintre nutrienții esențiali alături de vitamine, acizi grași esențiali și amino acizi esențiali." +
                        " Mineralele principale din corpul uman sunt calciu, fosfor, potasiu, sodiu și magneziu, celălalte sunt considerate secundare (fier, iod, seleniu, cupru, zinc, etc).\n" +
                        "Principalele surse pentru a realimenta organismul sunt: nucile și semințele, scoicile, legume precum conopidă și brocoli, ficat de vită, ouă."
            )

        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Cream, RectangleShape)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Cream, RectangleShape),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(
                    text = "     În cele ce urmează, sunt explicate" +
                            " categoriile ingredientelor dar și unii termeni nutriționali:\n",
                    style = TextStyle(
                        fontFamily = instrumentSerif,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                )
            }

            items(sections.size) { index ->
                val section = sections[index]
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                sections[index] = section.copy(isExpanded = !section.isExpanded)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = section.title,
                            style = TextStyle(
                                fontFamily = instrumentSerif,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                        Icon(
                            imageVector = if (section.isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (section.isExpanded) "Collapse" else "Expand",tint = Color.Black
                        )
                    }
                    AnimatedVisibility(visible = section.isExpanded) {
                        Column {
                            Text(
                                text = section.content,
                                style = TextStyle(
                                    fontFamily = instrumentSerif,
                                    fontSize = 20.sp,
                                    color = Color.Black
                                ),
                                modifier = Modifier.padding(16.dp)
                            )

                        }
                    }
                }
            }
        }
    }
}