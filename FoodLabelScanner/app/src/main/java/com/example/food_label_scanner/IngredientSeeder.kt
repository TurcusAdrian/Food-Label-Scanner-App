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


            insertIngredientIfNotExists(
                dbHelper,
                "MASA DE CACAO",
                 "6 kcal/g, conține grăsimi, proteine și antioxidanți naturali",
                 11, // Existing "Proteine" category
                 2, // Medium impact
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
                2, 2, "Uleiul de palmier este un ulei vegetal" +
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



            insertIngredientIfNotExists(dbHelper, "ULEI DE SHEA", "9 kcal/g, contine grasimi nesaturate benefice",
                2, 2, "Uleiul de shea este extras din nucile arborelui de shea" +
                        " și este folosit atât în industria alimentară, cât și în produse cosmetice." +
                        " Conține grăsimi sănătoase, precum acizi grași nesaturați și antioxidanți." +
                        " Poate susține sănătatea cardiovasculară atunci când este consumat în cantități moderate" +
                        " - intre 20-30 g/zi (adult) din totalul grasimilor sanatoase (incluzand uleiul de shea)")


            insertIngredientIfNotExists(dbHelper, "CARAMEL 11.8%", "4 kcal/g, bogat in zaharuri",
                3, 8, "Caramelul este obținut prin încălzirea zahărului și este folosit" +
                        " pentru culoare și aromă în produse de cofetărie, băuturi sau cereale. " +
                        "Deși oferă un gust plăcut, este bogat în zaharuri și poate contribui la apariția cariilor" +
                        " și dezechilibrului metabolic dacă este consumat frecvent " +
                        "- inclus in limita recomandata zilnica de zahar")



            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA", "3.5 ~ 4 kcal/g, indice glicemic ridicat ",
                3, 1, "Siropul de glucoză este un îndulcitor lichid obținut din amidon," +
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
                1, 9, "Lapte evaporat până la stadiul de pudră, păstrând componentele nutritive esențiale. Este o sursă bună de proteine, grăsimi, calciu și vitamine." +
                        " Se folosește în ciocolată, prăjituri și produse de panificație. " +
                        "Poate fi îndulcit cu zahăr în funcție de producător sau adăugată vitamina D. " +
                        "Este de preferat totuși consumul laptelui clasic fiind mult mai nutritiv")


            insertIngredientIfNotExists(dbHelper, "GRASIME DIN LAPTE", "9 kcal/g, conține acizi grași esențiali",
                1, 9, "Grăsimea din lapte este componenta principală din unt, smântână și alte produse lactate." +
                        " Conține acizi grași esențiali și vitamine liposolubile (A, D, E, K), dar și grăsimi saturate." +
                        " Consum moderat recomandat pentru menținerea sănătății cardiovasculare")


            insertIngredientIfNotExists(dbHelper, "ZER PUDRA", "Aproximativ 3.5 kcal/g, bogat în proteine de calitate",
                1, 9, "Zerul pudră este obținut prin uscarea zerului" +
                        " rămas din procesul de fabricare a brânzeturilor. Este bogat în proteine ușor asimilabile" +
                        " și aminoacizi esențiali, fiind des utilizat în suplimente sportive și produse dietetice.")


            insertIngredientIfNotExists(dbHelper, "ACID CITRIC ANHIDRU", "Contains antioxidants and supports metabolism",
                1, 6, "Acidul citric este un compus natural prezent în citrice," +
                        " utilizat frecvent ca conservant și pentru reglarea acidității alimentelor." +
                    " Are efect antioxidant și contribuie la menținerea pH-ului produselor. Găsit în tablete efervescente sau folosit ca înlocuitor la sucul de lămâie.")

            insertIngredientIfNotExists(dbHelper, "ACID CITRIC", "Natural preservative and acidity regulator",
                1, 6, "Acidul citric este un aditiv alimentar natural, găsit în fructe precum lămâia și portocala. Este utilizat pentru a conserva alimentele și pentru a regla aciditatea acestora. Are și proprietăți antioxidante moderate.")



            insertIngredientIfNotExists(dbHelper, "APA", "0 kcal/g",
                1,10,"Apă potabilă, esențială pentru hidratare," +
                        " reglarea temperaturii corpului și funcționarea optimă a organelor. Este utilizată ca solvent, mediu de procesare sau ingredient în majoritatea produselor alimentare.”\n" +
                        "Cantitate recomandată: 2–2.5 litri pe zi pentru un adult")

            insertIngredientIfNotExists(dbHelper, "UNT", " ~7 kcal/g, bogat în grăsimi saturate",
                9,8, "Untul este obținut prin baterea smântânii și conține grăsimi saturate," +
                        " vitamine liposolubile (A, D, E) și colesterol." +
                        " Se folosește în gătit, patiserie și ca ingredient de aromă.”\n" +
                        "Cantitate recomandată: Maxim 10% din aportul caloric zilnic să provină din grăsimi saturate")


            insertIngredientIfNotExists(dbHelper,"AMIDON MODIFICAT", "3.5–4 kcal/g",
                4, 5, "Derivat din amidon natural (de porumb, cartofi etc.)" +
                        " modificat chimic sau enzimatic pentru a rezista proceselor industriale." +
                        " Folosit pentru îngroșare și stabilizare.”\n" +
                        "Cantitate recomandată: Nu există un prag specific, dar este considerat sigur" +
                        " în cantități normale din alimente.")


            insertIngredientIfNotExists(dbHelper, "GELATINA", "Sursă de colagen", 1, 3, "Proteină obținută din colagen animal. Folosită ca agent gelifiant. Doză uzuală în suplimente: 10g/zi.")
            insertIngredientIfNotExists(dbHelper, "FERMENTE LACTICE", "Contribuie la sănătatea digestivă", 1, 9, "Culturi bacteriene benefice pentru digestie și echilibrul florei intestinale. Se recomandă minim 1 miliard CFU/zi.")
            insertIngredientIfNotExists(dbHelper, "SARE", "Conține sodiu, esențial pentru echilibrul electrolitic", 3, 6, "Folosită pentru gust și conservare. Consumul excesiv este asociat cu hipertensiune. Limita recomandată: <5g/zi.")
            insertIngredientIfNotExists(dbHelper, "E171", "Interzis în UE din 2022", 3, 6, "Fost colorant alb opac. Eliminat din uz din cauza riscurilor potențiale asupra sănătății. Nu se recomandă consumul.")
            insertIngredientIfNotExists(dbHelper, "SORBAT DE POTASIU", "Conservant antifungic", 2, 6, "Folosit pentru prevenirea mucegaiurilor în produse. Doză zilnică admisă: 25 mg/kg corp/zi.")
            insertIngredientIfNotExists(dbHelper, "BETA CAROTEN", "Antioxidant, precursor de vitamina A", 1, 7, "Pigment natural din morcovi, oferă culoare portocalie. Se recomandă 3–6 mg/zi.")
            insertIngredientIfNotExists(dbHelper, "AROMĂ NATURALĂ", "Aromatizant derivat din surse naturale", 2, 8, "Substanțe extrase din plante și condimente, fără aport nutritiv. Folosită în cantități mici.")
            insertIngredientIfNotExists(dbHelper, "VITAMINA A", "Susține vederea și sistemul imunitar", 1, 7, "Vitamina esențială pentru ochi, piele și imunitate. Doza zilnică recomandată: 700–900 µg RAE.")
            insertIngredientIfNotExists(dbHelper, "VITAMINA E", "Antioxidant ce protejează celulele", 1, 7, "Protejează celulele împotriva stresului oxidativ. Se găsește în uleiuri și nuci. Doza recomandată: 15 mg/zi.")
            insertIngredientIfNotExists(
                dbHelper, "CARBONATI DE POTASIU",
                 "Aditiv alimentar",
                 3, // ajustează în funcție de categoriile tale (ex: 3 = Aditivi)
                2, // valoare exemplu: 1 = rău, 5 = sănătos
                  "Carbonatul de potasiu este un aditiv alimentar utilizat ca regulator de aciditate și agent de creștere. În general este considerat sigur în doze mici."
            )


            insertIngredientIfNotExists(dbHelper, "CARTOFI", "Sursă de carbohidrați naturali, oferă sațietate și textură",
                15, 15, "Cartofii sunt legume rădăcinoase bogate în amidon, folosiți frecvent în chipsuri, piureuri sau produse procesate. Aduc volum și textură alimentelor și sunt o sursă moderată de fibre și vitamine.")

            insertIngredientIfNotExists(dbHelper, "EXTRACT DE DROJDIE", "Potentator natural de aromă umami",
                15, 7, "Extractul de drojdie este utilizat pentru a intensifica aroma alimentelor, având un gust natural de tip umami. Este frecvent întâlnit în supe instant, snacksuri sau sosuri și poate conține compuși benefici precum vitaminele din complexul B.")

            insertIngredientIfNotExists(dbHelper, "EXTRACT DE ARDEI ROȘU", "Aromă naturală și sursă de culoare ușoară",
                15, 7, "Extractul de ardei roșu este utilizat pentru a oferi aromă specifică de legumă și o ușoară culoare naturală. Conține compuși precum capsaicina și este folosit în produse procesate pentru gustul intens și autentic.")

            insertIngredientIfNotExists(dbHelper, "CEAPĂ PUDRĂ", "Aromă intensă de ceapă pentru condimente și gust",
                15, 7, "Ceapa pudră este un condiment deshidratat obținut prin uscarea și măcinarea cepei. Este folosită pentru a adăuga un gust concentrat în supe, sosuri, snacksuri și preparate instant.")




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