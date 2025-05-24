package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R
import com.example.food_label_scanner.data.Section


@Composable
fun HowToUse() {
    val instrumentSerif = FontFamily(Font(R.font.instrument_serif_regular))

    val sections = remember {
        mutableStateListOf(
            Section(
                title = "Home (Label Scan)",
                content = "   În cadrul acestui ecran principal utilizatorul îndreaptă" +
                        " camera spre eticheta cu ingrediente a unui produs unde se vede scrisul (Ingrediente:)" +
                        ", la o distanță nu foarte mare sau mică astfel încât imaginea să nu fie blurată." +
                        " Aplicația pe urmă oferă o analiză asupra ingredientelor constituente ale acesteia.",
                imageResId = R.drawable.label_scan_example // Add your image resource ID here

            ),
            Section(
                title = "Nutritional Dictionary",
                content ="  Aici sunt explicați diverși termeni nutriționali precum" +
                        " conservanți, emulgatori, emulsifianți, coloranți, vitamine, aditivi" +
                        " (categoriile din care fac parte alte ingrediente) " +
                        "sau surse proteice de diverse feluri, carbohidrați," +
                        " minerale sau chiar procese prin care trec ingredientele precum" +
                        " pasteurizare, fermentare, omogenizare. "
            ),
            Section(
                title = "Barcode Scan",
                content = "  Utilizatorul poate scana codul de bare al unui produs pentru" +
                        " a obține informații despre ingrediente.\n\n" +
                        "    Există cazuri în care nu există produsul în baza de date de produse (Open Food Facts)" +
                        " sau alte probleme - lipsa de ingrediente adăugate, adăugare incorectă a ingredientelor," +
                        " traducere eronată;" +
                        " în cazul acesta se recomandă scanarea clasică de etichetă"
            ),
            Section(
                title = "Search",
                content = "  Prin această funcționalitate utilizatorul" +
                        " poate căuta ingrediente și să afle date" +
                        " despre acestea fără o scanare a unui produs."
            ),
            Section(
                title = "Favourites Items",
                content = "   Pentru acest ecran, utilizatorului i se recomandă să își" +
                        " creeze o galerie cu produsele preferate." +
                        " Fotografia poate fi realizată fie prin funcția de Take Photo" +
                        " sau cu aplicația de cameră a telefonului.\n\n" +
                        "     Pozele adăugate aici pot fi șterse din galeria clasică a telefonului," +
                        " ele rămânând aici, realizându-se o copie pentru fiecare" +
                        " în parte care se regăsește în fișierele telefonului calea fiind: " +
                        "Android/data/com.example.food_label_scanner/files/Pictures/FoodLabelScanner\n\n" +
                        "   În cadrul fiecărei poze se poate deschide un meniu apăsând pe cele 3 puncte din colțul din stânga sus" +
                        " Funcția de Remove doar înlătură fotografia din galerie dar nu o șterge din calea respectivă iar cea de Delete o înlătură și o și șterge din folderul indicat mai sus."

            ),
            Section(
                title = "Add from Gallery",
                content = "  Funcția Add from Gallery permite utilizatorului" +
                        " în cadrul scanării etichetei să adauge pentru o scanare" +
                        " o poză din galeria proprie"
            ),
            Section(
                title = "Evaluarea ingredientelor",
                content = "  Ingredientele au fiecare o categorie din care fac parte" +
                        " (însemnătatea acesteia poate fi văzută în ecranul de dicționar nutritiv) dar și un rating.\n\n" +
                        "    Acest rating se află pe o scală de la 1 la 10," +
                        " unde 1 resprezintă alimentele foarte dăunătoare pentru sănătoase," +
                        " care trebuie evitate cu orice preț iar 10 cele care se recomandă a fi consumate regulat. "
            ),
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(
                    text = "     În cele ce urmează, sunt explicate fiecare funcționalitate" +
                            " dar și modul de evaluare a ingredientelor:\n",
                    style = TextStyle(
                        fontFamily = instrumentSerif,
                        fontSize = 25.sp
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
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = if (section.isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (section.isExpanded) "Collapse" else "Expand"
                        )
                    }
                    AnimatedVisibility(visible = section.isExpanded) {
                        Column {
                            Text(
                                text = section.content,
                                style = TextStyle(
                                    fontFamily = instrumentSerif,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.padding(16.dp)
                            )

                            // Display the image if it exists, below the text
                            section.imageResId?.let { imageResId ->
                                Image(
                                    painter = painterResource(id = imageResId),
                                    contentDescription = "Example of label scan",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}