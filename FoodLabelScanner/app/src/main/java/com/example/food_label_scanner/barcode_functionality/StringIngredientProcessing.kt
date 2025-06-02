package com.example.food_label_scanner.barcode_functionality

import android.util.Log
import com.example.food_label_scanner.DBHelper




// Function to return UPPERCASE string:
fun uppercase_text(ingredients: String): String {
    return ingredients.uppercase()
}

//Removes percentage and percentage numbers if they are not preceded by "E":
fun removePercentageNumbers(ingredients: String): String {
    // Negative lookbehind ensures we're not matching percentages after E-numbers like E100
    val regex = """(?<!E\d)\s*\(\s*\d+(?:[.,]\d+)?\s*%\s*\)|(?<!E\d)\d+(?:[.,]\d+)?\s*%"""
    return ingredients.replace(Regex(regex, RegexOption.IGNORE_CASE), "")
}

//Extracts the text after "INGREDIENTE:" and before ".":
fun extractIngredientsSection(text: String): String {
    val keyword = "INGREDIENTE:"
    val startIndex = text.indexOf(keyword, ignoreCase = true)

    if (startIndex == -1) {
        Log.w("extractIngredientsSection", "INGREDIENTE: not found in text: '$text'")
        return ""
    }

    val endOfKeywordIndex = startIndex + keyword.length
    var remainingText = text.substring(endOfKeywordIndex).trim()

    // Stop extraction at known non-ingredient starters (like allergy warnings)
    val stopKeywords = listOf(
        "poate contine",
        "poate conține",
        "poate include",
        "poate avea",
        "alergeni",
        "traces of", // for multilingual cases
        "may contain"
    )

    for (stopWord in stopKeywords) {
        val index = remainingText.indexOf(stopWord, ignoreCase = true)
        if (index != -1) {
            remainingText = remainingText.substring(0, index).trim()
            break
        }
    }

    // Optional: cut off at the first period if you still want that safety
    val periodIndex = remainingText.indexOf('.')
    if (periodIndex != -1) {
        remainingText = remainingText.substring(0, periodIndex).trim()
    }

    Log.d("extractIngredientsSection", "Clean ingredients: '$remainingText'")
    return remainingText
}



//Removes any diacritics or symbols:
fun remove_diacritics(ingredients: String): String {
    val correctedIngredients = StringBuilder()

    if (ingredients.equals("quinoa", ignoreCase = true)) {
        return ingredients // Return the original string without changes
    }

    for (char in ingredients) {
        when (char) {
            // A / a
            'à', 'á', 'â', 'ã', 'ä', 'å', 'ā', 'ă', 'ą', 'ǎ', 'ȧ', 'ạ', 'ả', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ' -> correctedIngredients.append('a')
            'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Ā', 'Ă', 'Ą', 'Ǎ', 'Ȧ', 'Ạ', 'Ả', 'Ấ', 'Ầ', 'Ẩ', 'Ẫ', 'Ậ', 'Ắ', 'Ằ', 'Ẳ', 'Ẵ', 'Ặ' -> correctedIngredients.append('A')

            // C / c
            'ç', 'ć', 'ĉ', 'ċ', 'č' -> correctedIngredients.append('c')
            'Ç', 'Ć', 'Ĉ', 'Ċ', 'Č' -> correctedIngredients.append('C')

            // D / d
            'ď', 'đ' -> correctedIngredients.append('d')
            'Ď', 'Đ' -> correctedIngredients.append('D')

            // E / e
            'è', 'é', 'ê', 'ë', 'ē', 'ĕ', 'ė', 'ę', 'ě', 'ẹ', 'ẻ', 'ẽ', 'ế', 'ề', 'ể', 'ễ', 'ệ' -> correctedIngredients.append('e')
            'È', 'É', 'Ê', 'Ë', 'Ē', 'Ĕ', 'Ė', 'Ę', 'Ě', 'Ẹ', 'Ẻ', 'Ẽ', 'Ế', 'Ề', 'Ể', 'Ễ', 'Ệ' -> correctedIngredients.append('E')

            // G / g
            'ĝ', 'ğ', 'ġ', 'ģ' -> correctedIngredients.append('g')
            'Ĝ', 'Ğ', 'Ġ', 'Ģ' -> correctedIngredients.append('G')

            // H / h
            'ĥ', 'ħ' -> correctedIngredients.append('h')
            'Ĥ', 'Ħ' -> correctedIngredients.append('H')

            // I / i
            'ì', 'í', 'î', 'ï', 'ĩ', 'ī', 'ĭ', 'į', 'ı', 'ǐ', 'ȉ', 'ȋ', 'ị', 'ỉ' -> correctedIngredients.append('i')
            'Ì', 'Í', 'Î', 'Ï', 'Ĩ', 'Ī', 'Ĭ', 'Į', 'İ', 'Ǐ', 'Ȉ', 'Ȋ', 'Ị', 'Ỉ' -> correctedIngredients.append('I')

            // J / j
            'ĵ' -> correctedIngredients.append('j')
            'Ĵ' -> correctedIngredients.append('J')

            // K / k
            'ķ', 'ĸ' -> correctedIngredients.append('k')
            'Ķ' -> correctedIngredients.append('K')

            // L / l
            'ĺ', 'ļ', 'ľ', 'ŀ', 'ł' -> correctedIngredients.append('l')
            'Ĺ', 'Ļ', 'Ľ', 'Ŀ', 'Ł' -> correctedIngredients.append('L')

            // N / n
            'ñ', 'ń', 'ņ', 'ň', 'ŋ' -> correctedIngredients.append('n')
            'Ñ', 'Ń', 'Ņ', 'Ň', 'Ŋ' -> correctedIngredients.append('N')

            // O / o
            'ò', 'ó', 'ô', 'õ', 'ö', 'ø', 'ō', 'ŏ', 'ő', 'ǒ', 'ȯ', 'ọ', 'ỏ', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ', 'ớ', 'ờ', 'ở', 'ỡ', 'ợ' -> correctedIngredients.append('o')
            'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'Ō', 'Ŏ', 'Ő', 'Ǒ', 'Ȯ', 'Ọ', 'Ỏ', 'Ố', 'Ồ', 'Ổ', 'Ỗ', 'Ộ', 'Ớ', 'Ờ', 'Ở', 'Ỡ', 'Ợ' -> correctedIngredients.append('O')

            // Q / q
            'q' -> correctedIngredients.append('g')
            'Q' -> correctedIngredients.append('G')

            // R / r
            'ŕ', 'ŗ', 'ř' -> correctedIngredients.append('r')
            'Ŕ', 'Ŗ', 'Ř' -> correctedIngredients.append('R')

            // S / s
            'ś', 'ŝ', 'ş', 'š', 'ș' -> correctedIngredients.append('s')
            'Ś', 'Ŝ', 'Ş', 'Š', 'Ș' -> correctedIngredients.append('S')

            // T / t
            'ţ', 'ť', 'ŧ', 'ț' -> correctedIngredients.append('t')
            'Ţ', 'Ť', 'Ŧ', 'Ț' -> correctedIngredients.append('T')

            // U / u
            'ù', 'ú', 'û', 'ü', 'ũ', 'ū', 'ŭ', 'ů', 'ű', 'ų', 'ǔ', 'ȕ', 'ȗ', 'ụ', 'ủ', 'ứ', 'ừ', 'ử', 'ữ', 'ự' -> correctedIngredients.append('u')
            'Ù', 'Ú', 'Û', 'Ü', 'Ũ', 'Ū', 'Ŭ', 'Ů', 'Ű', 'Ų', 'Ǔ', 'Ȕ', 'Ȗ', 'Ụ', 'Ủ', 'Ứ', 'Ừ', 'Ử', 'Ữ', 'Ự' -> correctedIngredients.append('U')

            // W / w
            'ŵ' -> correctedIngredients.append('w')
            'Ŵ' -> correctedIngredients.append('W')

            // Y / y
            'ý', 'ÿ', 'ŷ', 'ỳ', 'ỵ', 'ỷ', 'ỹ' -> correctedIngredients.append('y')
            'Ý', 'Ÿ', 'Ŷ', 'Ỳ', 'Ỵ', 'Ỷ', 'Ỹ' -> correctedIngredients.append('Y')

            // Z / z
            'ź', 'ż', 'ž' -> correctedIngredients.append('z')
            'Ź', 'Ż', 'Ž' -> correctedIngredients.append('Z')

            // Your special cases

            '*' -> correctedIngredients.append("")
            '_' -> correctedIngredients.append("")
            '-' -> correctedIngredients.append(" ")

            else -> correctedIngredients.append(char)
        }
    }

    return correctedIngredients.toString()
}

//Extracts the content between (Paranthesis):
fun extractParenthesisContent(text: String): String {
    val start = text.indexOf("(")
    val end = text.indexOf(")")
    return if (start != -1 && end != -1 && end > start) {
        text.substring(start + 1, end)
    } else {
        ""
    }
}

//Splits the text by "," to determine individual ingredients:
fun splitIngredients(processedIngredients: String): List<String> {
    // 1. Split the string by the comma delimiter
    val ingredientsList = processedIngredients.split(",")

    // 2. Trim whitespace from each ingredient (optional but recommended)
    val trimmedIngredientsList = ingredientsList.map { it.trim() }

    return trimmedIngredientsList
}

fun process_ingredients(ingredients: String): String {
    val withoutPercentage = removePercentageNumbers(ingredients)
    val withoutDiacritics = remove_diacritics(withoutPercentage)
    return uppercase_text(withoutDiacritics)
}

//Splits the text by "," to determine individual ingredients between parenthesis (ingredient1, ingredient2):
fun splitIngredientsAdvanced(text: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var parenthesesLevel = 0

    for (char in text) {
        when (char) {
            '(' -> {
                parenthesesLevel++
                current.append(char)
            }
            ')' -> {
                parenthesesLevel--
                current.append(char)
            }
            ',' -> {
                if (parenthesesLevel == 0) {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                } else {
                    current.append(char)
                }
            }
            else -> current.append(char)
        }
    }
    if (current.isNotBlank()) {
        result.add(current.toString().trim())
    }
    return result
}

val ignoredKeywords = setOf(
    "EMULSIFIANTI", "CONSERVANTI", "AGENTI DE CRESTERE", "COLORANTI",
    "REGULATOR DE ACIDITATE", "STABILIZATORI", "EMULGATOR:", "AROME", "INGREDIENT:", "EMULSIFIANT:","VOPSEA:"
)




fun extractValidIngredients(rawIngredients: List<String>, dbHelper: DBHelper): List<String> {
    val validIngredients = mutableListOf<String>()
    val ingredientPattern = Regex("[A-Z][A-Z\\s\\-()]+")

    for (ingredient in rawIngredients) {
        val cleaned = ingredient.uppercase()
            .replace(Regex("^(CONSERVANT(I)?|EMULGATOR(I)?|EMULSIFIANT(I)?|COLORANT(I)?|REGULATOR DE ACIDITATE|AGENTI DE CRESTERE|VOPSEA|AGENT DE GLAZURARE|AGENTI DE GLAZURARE|AGENT DE UMEZIRE|AGENTI DE UMEZIRE|AGENT DE INGROSARE|AGENTI DE INGROSARE):?\\s*"), "")
        val keyword = cleaned.split("(", ",", ";", ":")[0].trim()
        Log.d("extractValidIngredients", "Processing ingredient: '$cleaned', keyword: '$keyword'")

        if (keyword in ignoredKeywords) {
            val inside = extractParenthesisContent(cleaned)
            if (inside.isNotEmpty()) {
                val nested = splitIngredientsAdvanced(inside)
                for (nestedIng in nested) {
                    val name = nestedIng.trim()
                    if (ingredientPattern.matches(name) && dbHelper.getIngredientByNameFuzzy(name) != null) {
                        validIngredients.add(name)
                    }
                }
            }
            continue
        }

        val plain = cleaned.substringBefore("(").trim()
        if (ingredientPattern.matches(plain) && dbHelper.getIngredientByNameFuzzy(plain) != null) {
            validIngredients.add(plain)
        }

        val inner = extractParenthesisContent(cleaned)
        if (inner.isNotEmpty()) {
            val innerTrimmed = inner.trim()
            if (ingredientPattern.matches(innerTrimmed) && dbHelper.getIngredientByNameFuzzy(innerTrimmed) != null) {
                validIngredients.add(innerTrimmed)
            }
        }
    }
    return validIngredients.distinct()
}

//Retrieves the details for each ingredient extracted/found in Database:
fun getIngredientDetails(ingredients: List<String>, dbHelper: DBHelper): List<String> {
    val details = mutableListOf<String>()
    for (ingredientName in ingredients) {
        val ingredientData = dbHelper.getIngredientByNameFuzzy(ingredientName)
        if (ingredientData != null) {
            val name = ingredientData["name"]
            val nutritionalValue = ingredientData["nutritional_value"]
            val categoryId = ingredientData["category_id"] as Int
            val healthRating = ingredientData["health_rating"]
            val description = ingredientData["description"]
            val categoryName = dbHelper.getCategoryNameById(categoryId) ?: "Category not found"
            val detail = "Name: $name, Nutritional Value: $nutritionalValue, Category: $categoryName, Health Rating: $healthRating, Description: $description"
            details.add(detail)
        } else {
            details.add("Ingredient '$ingredientName' not found in database")
        }
    }
    return details
}