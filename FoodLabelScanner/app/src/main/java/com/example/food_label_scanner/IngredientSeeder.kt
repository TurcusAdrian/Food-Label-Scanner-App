package com.example.food_label_scanner

import android.content.Context
import android.util.Log

class IngredientSeeder(private val context: Context) {

    fun seedIngredients() {
        val dbHelper = DBHelper(context)
        val db = dbHelper.readableDatabase

        try {
            // Insert categories if they don’t exist
            insertCategoryIfNotExists(dbHelper, 1, "Îndulcitori", "Ingrediente" +
                    " naturale sau artificiale folosite pentru a conferi gust dulce")
            insertCategoryIfNotExists(dbHelper, 2, "Grăsimi și Uleiuri", "Surse de grăsimi" +
                    " alimentare, utilizate pentru gătit sau textură")
            insertCategoryIfNotExists(dbHelper, 3, "Conservanți", "Utilizați pentru" +
                    " a prelungi durata de viață a produselor alimentare")
            insertCategoryIfNotExists(dbHelper, 4, "Îngroșători / Stabilizatori",
                "Îmbunătățesc textura sau consistența produselor")
            insertCategoryIfNotExists(dbHelper, 5, "Emulgatori", "Permite amestecarea" +
                    " apei cu uleiul și stabilizează compozițiile")
            insertCategoryIfNotExists(dbHelper, 6, "Acidifianți / Regulatori de aciditate",
                "Controlează aciditatea alimentelor pentru gust sau conservare")
            insertCategoryIfNotExists(dbHelper, 7, "Arome",
                "Amplifică sau imită gusturile, pot fi naturale sau artificiale")
            insertCategoryIfNotExists(dbHelper, 8, "Coloranți",
                "Modifică sau îmbunătățesc culoarea produselor alimentare")
            insertCategoryIfNotExists(dbHelper, 9, "Lactate",
                "Ingrediente pe bază de lapte pentru gust, textură sau valoare nutritivă")
            insertCategoryIfNotExists(dbHelper, 10, "Cereale / Făinuri",
                "Surse de carbohidrați de bază pentru volum și structură")
            insertCategoryIfNotExists(dbHelper, 11, "Proteine",
                "Ingrediente bogate în proteine de origine animală sau vegetală")
            insertCategoryIfNotExists(dbHelper, 12, "Vitamine / Minerale",
                "Nutrienți adăugați pentru sănătate sau cerințe legale")
            insertCategoryIfNotExists(dbHelper, 13, "Aditivi (diversi)", "Agenți funcționali precum antiaglomeranți, umectanți, etc.")
            insertCategoryIfNotExists(dbHelper, 14, "Nuci și Semințe", "Surse vegetale de grăsimi, proteine și fibre")
            insertCategoryIfNotExists(dbHelper, 15, "Fructe și Legume", "Ingrediente din plante pentru gust sau valoare nutritivă")


            insertIngredientIfNotExists(dbHelper, "L-CISTEINA", "Aminoacid semi-esențial cu rol tehnologic",
                11, 4, "L-cisteina este un aminoacid utilizat ca aditiv în panificație pentru a îmbunătăți textura și elasticitatea aluatului. Deși este o substanță naturală, utilizarea excesivă ca aditiv ridică unele controverse, iar sursa poate fi uneori animală. În cantități mici este considerată sigură.")

            insertIngredientIfNotExists(dbHelper, "ACID ASCORBIC", "Vitamina C, antioxidant natural",
                12, 5, "Acidul ascorbic este o formă de vitamina C folosită în alimente ca antioxidant pentru prevenirea oxidării. Este benefic pentru sistemul imunitar și metabolismul celular. În doză moderată este foarte sănătos și sigur.")

            insertIngredientIfNotExists(dbHelper, "DROJDIE", "Agent de fermentare natural, bogat în vitamine B",
                15, 5, "Drojdiile sunt ciuperci microscopice utilizate pentru fermentare în panificație și băuturi. Conțin vitamine din complexul B și proteine. Sunt în general considerate sănătoase și valoroase nutrițional.")

            insertIngredientIfNotExists(dbHelper, "GLUTEN DIN GRAU", "Proteină naturală din grâu responsabilă pentru elasticitate",
                10, 4, "Glutenul este o proteină prezentă natural în grâu și alte cereale. Este inofensiv pentru majoritatea oamenilor, dar trebuie evitat în cazul bolii celiace sau al sensibilității la gluten. În cantități normale, este considerat sigur.")

            insertIngredientIfNotExists(dbHelper, "ALCOOL ETILIC", "Solvent sau conservant alimentar volatil",
                3, 3, "Alcoolul etilic este uneori folosit în produse alimentare ca solvent pentru arome, agent de conservare sau component al produselor de patiserie cu rom/arome. În cantități foarte mici este sigur, dar nu este recomandat în exces.")

            insertIngredientIfNotExists(dbHelper, "PUDRA DIN GALBENUS DE OU", "Sursă concentrată de lipide și nutrienți",
                1, 4, "Pudra de gălbenuș de ou este obținută prin deshidratarea ouălor și păstrează proprietățile nutritive, fiind bogată în grăsimi, colesterol și lecitină. Poate fi o sursă sănătoasă de grăsimi bune și vitamine, dar se recomandă consum moderat.")

            insertIngredientIfNotExists(dbHelper, "LACTOZA", "Zahăr natural din lapte",
                9, 4, "Lactoza este un dizaharid natural prezent în produsele lactate. Este o sursă de energie și ajută la absorbția calciului. Poate cauza intoleranță la unele persoane, dar este sigură pentru majoritatea oamenilor.")

            insertIngredientIfNotExists(dbHelper, "GUMA DE CELULOZA", "Îngroșător vegetal obținut din fibre naturale",
                4, 4, "Guma de celuloză este un aditiv alimentar obținut din celuloză vegetală. Se folosește pentru îngroșarea și stabilizarea produselor. Este considerată sigură și este adesea folosită în produse fără gluten și alimente dietetice.")



            insertIngredientIfNotExists(
                dbHelper,
                "ULEI DE FLOAREA SOARELUI",
                "9 kcal/g, bogat în grăsimi nesaturate și vitamina E",
                2,
                4,
                "Uleiul de floarea-soarelui este extras din semințele plantei Helianthus annuus. Este bogat în acizi grași nesaturați (în special omega-6) și vitamina E. Variantele nerafinate sunt mai benefice, însă consumul excesiv sau gătirea la temperaturi ridicate poate reduce calitatea. Se recomandă consum echilibrat, alternativ cu uleiuri ce conțin omega-3."
            )


            insertIngredientIfNotExists(
                dbHelper,
                "FAINA DE PORUMB",
                "3.5–4 kcal/g, sursă de carbohidrați complecși",
                10,
                5,
                "Făina de porumb este obținută prin măcinarea boabelor de porumb. Este o sursă naturală de carbohidrați complecși și fibre, fiind utilizată în mămăligă, produse de panificație sau coacere. Nu conține gluten și oferă energie susținută. Variantele nerafinate au un profil nutrițional mai bogat."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "PRAF DE OU",
                "5.5–6 kcal/g, bogat în proteine și grăsimi",
                11,
                5, // foarte sanatos
                "Praful de ou este obținut prin deshidratarea ouălor întregi sau a albușului/gălbenușului separat. Este o sursă concentrată de proteine, grăsimi, vitamine și minerale. Utilizat în produse de patiserie, panificație și alimente procesate. Păstrează mare parte din valoarea nutritivă a oului proaspăt. Se recomandă moderație în funcție de conținutul lipidic și sodiu adăugat în unele formule."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "FAINA ALBA DE GRAU",
                "3.6 kcal/g, rafinată, conține carbohidrați simpli",
                10,
                3,
                "Făina albă de grâu este obținută prin rafinarea grâului, îndepărtând tărâțele și germenii. Conține carbohidrați rapizi, proteine (gluten) și puține fibre. Consum frecvent în exces poate afecta glicemia și digestia. Alternativa mai sănătoasă: făină integrală."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "AMIDON MODIFICAT DE PORUMB",
                "3.5–4 kcal/g, îngroșător derivat din porumb",
                4,
                2,
                "Este un derivat al amidonului de porumb, tratat chimic sau enzimatic pentru a-i modifica proprietățile funcționale (rezistență la temperatură, aciditate). Folosit în supe, sosuri și produse procesate. Considerat sigur în alimentație în doze uzuale."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "ZAHAR ARS",
                "4 kcal/g, colorant și îndulcitor cu risc redus",
                1,
                3,
                "Zahărul ars este zahăr încălzit până la caramelizare, folosit ca îndulcitor și colorant (caramel simplu) în deserturi, băuturi și produse procesate. Este natural și sigur în cantități mici, dar este tot zahăr și contribuie la aportul glicemic total."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "SARE DE MARE",
                "Conține sodiu și urme de minerale",
                6,
                3,
                "Sarea de mare este obținută prin evaporarea apei marine și conține în principal clorură de sodiu și urme de minerale (magneziu, potasiu). Are aceleași riscuri ca sarea obișnuită dacă este consumată în exces. Limita recomandată: <5g sodiu/zi."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "GUMA XANTAN",
                "Aproape fără calorii, îngroșător natural",
                4,
                4,
                "Guma xantan este un polizaharid obținut prin fermentarea glucozei de către bacterii Xanthomonas. Este folosită ca stabilizator și agent de îngroșare în sosuri, produse fără gluten sau băuturi. Considerată sigură în doze obișnuite (1-15g/zi)."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "SORBITOL",
                "2.6 kcal/g, îndulcitor cu absorbție lentă",
                1,
                3,
                "Sorbitolul este un îndulcitor poliol obținut natural sau sintetic, cu gust dulce și absorbție lentă. Este folosit în produse fără zahăr (gumă, bomboane, siropuri). Poate avea efect laxativ în cantități mari (>20g/zi). Este permis, dar trebuie consumat cu moderație."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "VANILINA",
                "Aromatizant sintetic, fără valoare nutritivă",
                7,
                1,
                "Vanilina este un compus aromatic sintetic ce imită aroma naturală de vanilie. Este utilizată în prăjituri, înghețată, ciocolată și alte produse pentru gust. Deși este considerată sigură în cantități mici, nu are beneficii nutriționale și poate irita în cantități mari."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "LAPTE PRAF DEGRESAT",
                "3.5 kcal/g, bogat în proteine și calciu, sărac în grăsimi",
                9,
                5, //foarte sanatos
                "Laptele praf degresat este obținut prin eliminarea grăsimilor din laptele integral, urmat de deshidratare. Este o sursă valoroasă de proteine, calciu și vitamine solubile în apă, cu un conținut redus de calorii și grăsimi. Util în alimentația dietetică și sportivă."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "UNT DESHIDRATAT",
                "7 kcal/g, grăsimi și compuși lactici concentrați",
                9,
                3,
                "Untul deshidratat este unt normal din care a fost eliminată apa, rezultând un produs concentrat în grăsimi și aromă. Este utilizat frecvent în produse de patiserie industriale. Conține grăsimi saturate și vitamine liposolubile. Se recomandă un consum moderat pentru menținerea sănătății cardiovasculare."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "UNT DE CACAO",
                "9 kcal/g, sursă de grăsimi naturale din cacao",
                2,
                3, // Benefic în cantități moderate
                "Untul de cacao este grăsimea naturală extrasă din boabele de cacao. Este utilizat în mod frecvent în ciocolată și alte produse de cofetărie. Conține acizi grași saturați și nesaturați, având o stabilitate ridicată la oxidare. Este benefic în cantități moderate, dar trebuie evitat în exces din cauza valorii calorice ridicate."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "ULEI DE SHEA",
                "9 kcal/g, conține grăsimi nesaturate benefice",
                2,
                4, // Sănătos în mod moderat
                "Uleiul de shea este extras din nucile arborelui de shea și este folosit atât în industria alimentară, cât și cosmetică. Conține acizi grași nesaturați și antioxidanți naturali. Poate susține sănătatea cardiovasculară dacă este consumat în cantități moderate (20–30 g/zi ca parte a aportului total de grăsimi sănătoase)."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "CARBONAT ACID DE AMONIU",
                "Agent de afânare, fără valoare nutritivă",
                3,
                3, // Sănătos în doze mici, dar nu benefic
                "Carbonatul acid de amoniu este un agent de afânare utilizat în produse de patiserie uscate (ex: biscuiți). În timpul coacerii se descompune în gaz și nu lasă reziduuri. Este sigur în cantitățile uzuale din alimente, dar nu are valoare nutritivă."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "CARBONAT ACID DE SODIU",
                "Utilizat ca agent de creștere sau regulator de aciditate",
                3,
                4,
                "Carbonatul acid de sodiu, cunoscut ca bicarbonat de sodiu, este folosit ca agent de dospire în produse de panificație și ca regulator de aciditate. Este considerat sigur în doze normale și se descompune în compuși inofensivi la coacere. Nu are valoare nutritivă, dar este funcțional."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "PUDRA CACAO DEGRESATA",
                "3.2 kcal/g, conține antioxidanți și compuși bioactivi",
                11,
                5,
                "Pudra de cacao degresată este obținută din masa de cacao prin eliminarea grăsimii (untul de cacao). Are un conținut redus de calorii, dar păstrează flavonoidele și compușii cu efect antioxidant. Este recomandată în alimentația sănătoasă pentru beneficiile sale asupra inimii și circulației."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "ULEI DE COCOS",
                "9 kcal/g, conține grăsimi saturate cu lanț mediu",
                2,
                3,
                "Uleiul de cocos este un ulei vegetal extras din pulpa de nucă de cocos. Este bogat în grăsimi saturate, în special trigliceride cu lanț mediu (MCT), care pot fi metabolizate rapid pentru energie. Poate avea efecte benefice asupra sănătății metabolice, dar consumul excesiv trebuie evitat din cauza conținutului de grăsimi saturate."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "USTUROI PUDRA",
                "3.3 kcal/g, bogat în compuși sulfurici benefici",
                15,
                5,
                "Usturoiul pudră este o formă deshidratată și măcinată a usturoiului proaspăt. Este utilizat ca aromatizant natural în diverse preparate. Conține compuși activi precum alicina, cunoscută pentru proprietățile sale antimicrobiene, antiinflamatoare și cardioprotective. Consumul regulat, chiar și în formă pudră, poate sprijini sănătatea inimii și sistemul imunitar."
            )
            insertIngredientIfNotExists(
                dbHelper,
                "FAINA DE GRAU",
                "3.6 kcal/g, sursă principală de carbohidrați",
                10,
                3,
                "Făina de grâu este obținută prin măcinarea boabelor de grâu și este utilizată frecvent în panificație, patiserie și produse procesate. Oferă energie prin conținutul de carbohidrați, dar variantele rafinate au conținut redus de fibre și micronutrienți. Se recomandă alegerea făinii integrale pentru un aport mai mare de fibre și vitamine."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "SEMINTE DE SUSAN",
                "5.7 kcal/g, bogate în grăsimi sănătoase, proteine și fibre",
                14,
                5,
                "Semințele de susan sunt o sursă excelentă de acizi grași nesaturați, proteine vegetale, fibre și minerale precum calciu, magneziu și fier. Conțin și antioxidanți (sesamina, sesamol). Pot contribui la sănătatea cardiovasculară și digestivă dacă sunt consumate moderat (1–2 linguri/zi)."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "MASA DE CACAO",
                 "6 kcal/g, conține grăsimi, proteine și antioxidanți naturali",
                 11,
                 4,
                 "Masa de cacao este obținută prin măcinarea boabelor de cacao uscate și prăjite. Conține grăsimi (unt de cacao), proteine și antioxidanți naturali. Este folosită ca ingredient principal în ciocolată. Poate oferi beneficii prin conținutul de flavonoide, dar este și calorică. Nu este recomandată în exces pentru persoanele sensibile la cofeină sau cu regim hipocaloric."
            )

            // Insert ingredients only if they don't already exist
            insertIngredientIfNotExists(dbHelper, "ZAHAR", "4 kcal/g, bogat in calorii," +
                    " fara macronutrienti",
                1, 1,
                "Zahărul este un carbohidrat simplu utilizat pentru a oferi gust dulce." +
                        " Oferă energie rapidă, dar nu conține nutrienți esențiali." +
                        " Consumul frecvent sau în cantități mari este asociat cu obezitate," +
                        " diabet de tip 2 și boli de inimă." +
                        " Se găsește frecvent în dulciuri," +
                        " băuturi răcoritoare și alimente procesate." +
                        " Se recomandă consumul moderat - ideal sub 25 de grame/zi pentru un adult.")



            insertIngredientIfNotExists(dbHelper, "ULEI DE PALMIER", "9 kcal/g," +
                    " bogat in grasimi saturate",
                2, 1, "Uleiul de palmier este un ulei vegetal" +
                        " folosit frecvent în alimente procesate" +
                        " datorită costului scăzut și stabilității la temperaturi ridicate. " +
                        "Deși oferă energie, este bogat în grăsimi saturate, care pot crește" +
                        " riscul bolilor cardiovasculare.\n" +
                    "\n" + "În timpul rafinării la temperaturi înalte, pot apărea compuși precum" +
                        " esteri ai acizilor grași de glicidol (GE), 3-MCPD și 2-MCPD." +
                        " Glicidolul este considerat posibil cancerigen, iar 3-MCPD este" +
                        " legat de afectarea rinichilor și fertilității." +
                        " Autoritățile europene recomandă limitarea expunerii," +
                        " în special în cazul copiilor." +
                        " Începând cu 2022, unele țări au impus limite" +
                        " stricte pentru acești compuși în uleiul procesat.")

            insertIngredientIfNotExists(
                dbHelper,
                "ULEI VEGETAL DE PALMIER",
                "9 kcal/g, grăsimi saturate și stabile la temperaturi înalte",
                2,
                2,
                "Uleiul vegetal de palmier este derivat din fructul palmierului și este frecvent folosit în produse procesate datorită prețului redus și stabilității la gătire. Deși este o sursă de energie, conține un nivel ridicat de grăsimi saturate. În timpul procesării industriale pot apărea compuși potențial dăunători. Se recomandă consum limitat, în special la copii."
            )

            insertIngredientIfNotExists(
                dbHelper,
                "ULEI DE PALMIER NEHIDROGENAT",
                "9 kcal/g, bogat în grăsimi saturate, dar fără grăsimi trans",
                2,
                3,
                "Uleiul de palmier nehidrogenat este o formă mai puțin procesată a uleiului de palmier, utilizat frecvent în produse alimentare. Nu conține grăsimi trans, dar este totuși bogat în grăsimi saturate. Poate fi stabil la temperaturi ridicate, însă consumul trebuie moderat pentru a preveni efectele negative asupra sănătății cardiovasculare."
            )

            insertIngredientIfNotExists(dbHelper, "ULEI DE SHEA", "9 kcal/g, contine grasimi nesaturate benefice",
                2, 4, "Uleiul de shea este extras din nucile arborelui de shea" +
                        " și este folosit atât în industria alimentară, cât și în produse cosmetice." +
                        " Conține grăsimi sănătoase, precum acizi grași nesaturați și antioxidanți." +
                        " Poate susține sănătatea cardiovasculară atunci când este consumat în cantități moderate" +
                        " - intre 20-30 g/zi (adult) din totalul grasimilor sanatoase (incluzand uleiul de shea)")


            insertIngredientIfNotExists(dbHelper, "CARAMEL 11.8%", "4 kcal/g, bogat in zaharuri",
                3, 2, "Caramelul este obținut prin încălzirea zahărului și este folosit" +
                        " pentru culoare și aromă în produse de cofetărie, băuturi sau cereale. " +
                        "Deși oferă un gust plăcut, este bogat în zaharuri și poate contribui la apariția cariilor" +
                        " și dezechilibrului metabolic dacă este consumat frecvent " +
                        "- inclus in limita recomandata zilnica de zahar")



            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA", "3.5 ~ 4 kcal/g, indice glicemic ridicat ",
                3, 2, "Siropul de glucoză este un îndulcitor lichid obținut din amidon," +
                        " folosit frecvent în dulciuri și produse procesate pentru a îndulci fără a modifica gustul." +
                        " Este o alternativa la zahăr, care consumată în cantități mari duce la rezistența la insulină" +
                        " și probleme ale metabolismului. " +
                        "Consumul trebuie limitat în cadrul consumului maxim de zahăr recomandat.")



            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA-FRUCTOZA", "4 kcal/g, compus din glucoză și fructoză",
                3, 1, "Este un îndulcitor lichid derivat din amidon," +
                    " conținând atât glucoză, cât și fructoză. Este adesea folosit în sucuri, produse de patiserie și dulciuri. " +
                    "A fost asociat cu tulburări metabolice precum obezitatea, ficatul gras și diabetul de tip 2. " +
                    "Se recomandă consumul redus (alternativă la zahăr)")


            insertIngredientIfNotExists(dbHelper, "LAPTE PRAF INTEGRAL", "5 kcal/g, bogat în proteine și calciu",
                1, 3, "Lapte evaporat până la stadiul de pudră, păstrând componentele nutritive esențiale. Este o sursă bună de proteine, grăsimi, calciu și vitamine." +
                        " Se folosește în ciocolată, prăjituri și produse de panificație. " +
                        "Poate fi îndulcit cu zahăr în funcție de producător sau adăugată vitamina D. " +
                        "Este de preferat totuși consumul laptelui clasic fiind mult mai nutritiv")


            insertIngredientIfNotExists(dbHelper, "GRASIME DIN LAPTE", "9 kcal/g, conține acizi grași esențiali",
                1, 4, "Grăsimea din lapte este componenta principală din unt, smântână și alte produse lactate." +
                        " Conține acizi grași esențiali și vitamine liposolubile (A, D, E, K), dar și grăsimi saturate." +
                        " Consum moderat recomandat pentru menținerea sănătății cardiovasculare")


            insertIngredientIfNotExists(dbHelper, "ZER PUDRA", "Aproximativ 3.5 kcal/g, bogat în proteine de calitate",
                1, 4, "Zerul pudră este obținut prin uscarea zerului" +
                        " rămas din procesul de fabricare a brânzeturilor. Este bogat în proteine ușor asimilabile" +
                        " și aminoacizi esențiali, fiind des utilizat în suplimente sportive și produse dietetice.")


            insertIngredientIfNotExists(dbHelper, "ACID CITRIC ANHIDRU", "Contains antioxidants and supports metabolism",
                1, 6, "Acidul citric este un compus natural prezent în citrice," +
                        " utilizat frecvent ca conservant și pentru reglarea acidității alimentelor." +
                    " Are efect antioxidant și contribuie la menținerea pH-ului produselor. Găsit în tablete efervescente sau folosit ca înlocuitor la sucul de lămâie.")

            insertIngredientIfNotExists(dbHelper, "ACID CITRIC", "Natural preservative and acidity regulator",
                1, 6, "Acidul citric este un aditiv alimentar natural, găsit în fructe precum lămâia și portocala. Este utilizat pentru a conserva alimentele și pentru a regla aciditatea acestora. Are și proprietăți antioxidante moderate.")



            insertIngredientIfNotExists(dbHelper, "APA", "0 kcal/g",
                1,5,"Apă potabilă, esențială pentru hidratare," +
                        " reglarea temperaturii corpului și funcționarea optimă a organelor. Este utilizată ca solvent, mediu de procesare sau ingredient în majoritatea produselor alimentare.”\n" +
                        "Cantitate recomandată: 2–2.5 litri pe zi pentru un adult")

            insertIngredientIfNotExists(dbHelper, "UNT", " ~7 kcal/g, bogat în grăsimi saturate",
                9,4, "Untul este obținut prin baterea smântânii și conține grăsimi saturate," +
                        " vitamine liposolubile (A, D, E) și colesterol." +
                        " Se folosește în gătit, patiserie și ca ingredient de aromă.”\n" +
                        "Cantitate recomandată: Maxim 10% din aportul caloric zilnic să provină din grăsimi saturate")


            insertIngredientIfNotExists(dbHelper,"AMIDON MODIFICAT", "3.5–4 kcal/g",
                4, 3, "Derivat din amidon natural (de porumb, cartofi etc.)" +
                        " modificat chimic sau enzimatic pentru a rezista proceselor industriale." +
                        " Folosit pentru îngroșare și stabilizare.”\n" +
                        "Cantitate recomandată: Nu există un prag specific, dar este considerat sigur" +
                        " în cantități normale din alimente.")

            insertIngredientIfNotExists(
                dbHelper,
                "BICARBONAT DE AMONIU",
                "Nu are valoare energetică, agent de creștere",
                13,
                3,
                "Bicarbonatul de amoniu este un aditiv alimentar folosit ca agent de afânare în produse de patiserie uscate. În timpul coacerii se descompune complet în gaze, fără a lăsa reziduuri. Considerat sigur dacă este folosit conform limitărilor stabilite de autorități alimentare."
            )
            insertIngredientIfNotExists(dbHelper, "GELATINA", "Sursă de colagen", 1, 3, "Proteină obținută din colagen animal. Folosită ca agent gelifiant. Doză uzuală în suplimente: 10g/zi.")
            insertIngredientIfNotExists(dbHelper, "FERMENTE LACTICE", "Contribuie la sănătatea digestivă", 1, 5, "Culturi bacteriene benefice pentru digestie și echilibrul florei intestinale. Se recomandă minim 1 miliard CFU/zi.")
            insertIngredientIfNotExists(dbHelper, "SARE", "Conține sodiu, esențial pentru echilibrul electrolitic", 3, 3, "Folosită pentru gust și conservare. Consumul excesiv este asociat cu hipertensiune. Limita recomandată: <5g/zi.")
            insertIngredientIfNotExists(
                dbHelper,
                "SARE ALIMENTARĂ",
                "0 kcal/g, sursă de sodiu esențial",
                6,
                3,
                "Sarea alimentară este folosită pentru a îmbunătăți gustul și a conserva alimentele. Aportul controlat de sodiu este necesar pentru echilibrul hidric și funcția musculară. Consumul excesiv este asociat cu hipertensiune arterială și alte afecțiuni cardiovasculare. Doza recomandată: sub 5g/zi pentru un adult."
            )
            insertIngredientIfNotExists(dbHelper, "E171", "Interzis în UE din 2022", 3, 1, "Fost colorant alb opac. Eliminat din uz din cauza riscurilor potențiale asupra sănătății. Nu se recomandă consumul.")
            insertIngredientIfNotExists(dbHelper, "SORBAT DE POTASIU", "Conservant antifungic", 2, 3, "Folosit pentru prevenirea mucegaiurilor în produse. Doză zilnică admisă: 25 mg/kg corp/zi.")
            insertIngredientIfNotExists(dbHelper, "BETA CAROTEN", "Antioxidant, precursor de vitamina A", 1, 5, "Pigment natural din morcovi, oferă culoare portocalie. Se recomandă 3–6 mg/zi.")
            insertIngredientIfNotExists(dbHelper, "AROMĂ NATURALĂ", "Aromatizant derivat din surse naturale", 2, 4, "Substanțe extrase din plante și condimente, fără aport nutritiv. Folosită în cantități mici.")
            insertIngredientIfNotExists(dbHelper, "VITAMINA A", "Susține vederea și sistemul imunitar", 1, 5, "Vitamina esențială pentru ochi, piele și imunitate. Doza zilnică recomandată: 700–900 µg RAE.")
            insertIngredientIfNotExists(dbHelper, "VITAMINA E", "Antioxidant ce protejează celulele", 1, 5, "Protejează celulele împotriva stresului oxidativ. Se găsește în uleiuri și nuci. Doza recomandată: 15 mg/zi.")
            insertIngredientIfNotExists(
                dbHelper, "CARBONATI DE POTASIU",
                 "Aditiv alimentar",
                 3,
                3,
                  "Carbonatul de potasiu este un aditiv alimentar utilizat ca regulator de aciditate și agent de creștere. În general este considerat sigur în doze mici."
            )


            insertIngredientIfNotExists(dbHelper, "CARTOFI", "Sursă de carbohidrați naturali, oferă sațietate și textură",
                15, 4, "Cartofii sunt legume rădăcinoase bogate în amidon, folosiți frecvent în chipsuri, piureuri sau produse procesate. Aduc volum și textură alimentelor și sunt o sursă moderată de fibre și vitamine.")

            insertIngredientIfNotExists(dbHelper, "EXTRACT DE DROJDIE", "Potentator natural de aromă umami",
                15, 4, "Extractul de drojdie este utilizat pentru a intensifica aroma alimentelor, având un gust natural de tip umami. Este frecvent întâlnit în supe instant, snacksuri sau sosuri și poate conține compuși benefici precum vitaminele din complexul B.")

            insertIngredientIfNotExists(dbHelper, "EXTRACT DE ARDEI ROȘU", "Aromă naturală și sursă de culoare ușoară",
                15, 5, "Extractul de ardei roșu este utilizat pentru a oferi aromă specifică de legumă și o ușoară culoare naturală. Conține compuși precum capsaicina și este folosit în produse procesate pentru gustul intens și autentic.")

            insertIngredientIfNotExists(dbHelper, "CEAPĂ PUDRĂ", "Aromă intensă de ceapă pentru condimente și gust",
                15, 5, "Ceapa pudră este un condiment deshidratat obținut prin uscarea și măcinarea cepei. Este folosită pentru a adăuga un gust concentrat în supe, sosuri, snacksuri și preparate instant.")




            Log.d("IngredientSeeder", "Seeding complete")
        } catch (e: Exception) {
            Log.e("IngredientSeeder", "Error seeding ingredients", e)
        } finally {
            db.close()
        }
    }

    private fun insertCategoryIfNotExists(dbHelper: DBHelper, categoryId: Int, name: String, description: String) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM health_categories WHERE category_id = ?", arrayOf(categoryId.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()

        if (!exists) {
            dbHelper.insertCategory(categoryId, name, description)
        }
    }

    private fun insertIngredientIfNotExists(
        dbHelper: DBHelper,
        name: String,
        nutritionalValue: String,
        categoryId: Int,
        healthRating: Int,
        description: String
    ) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM ingredients WHERE name = ?", arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()

        if (!exists) {
            dbHelper.insertIngredient(name, nutritionalValue, categoryId, healthRating, description)
        }
    }
}